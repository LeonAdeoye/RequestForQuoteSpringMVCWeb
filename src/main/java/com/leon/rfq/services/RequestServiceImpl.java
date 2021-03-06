package com.leon.rfq.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.leon.rfq.common.EnumTypes.PriceSimulatorRequestEnum;
import com.leon.rfq.domains.OptionDetailImpl;
import com.leon.rfq.domains.PriceDetailImpl;
import com.leon.rfq.domains.RequestDetailImpl;
import com.leon.rfq.domains.SearchCriterionImpl;
import com.leon.rfq.events.NewRequestEvent;
import com.leon.rfq.events.PriceSimulatorRequestEvent;
import com.leon.rfq.events.PriceUpdateEvent;
import com.leon.rfq.products.BlackScholesModelImpl;
import com.leon.rfq.products.OptionRequestFactory;
import com.leon.rfq.repositories.RequestDao;

@Component
@Configurable
public final class RequestServiceImpl implements RequestService, ApplicationEventPublisherAware,
ApplicationListener<PriceUpdateEvent>
{
	private static Logger logger = LoggerFactory.getLogger(RequestServiceImpl.class);
	private ApplicationEventPublisher applicationEventPublisher;
	// TODO need a map for each user
	private final Map<Integer, RequestDetailImpl> requests = new ConcurrentSkipListMap<>();
	
	private final Map<Integer, RequestDetailImpl> todaysRequests = new ConcurrentSkipListMap<>();
	
	@Autowired(required=true)
	private RequestDao requestDao;
	
	@Autowired(required=true)
	private OptionRequestFactory optionRequestFactory;
	
	@Autowired(required=true)
	private UnderlyingService underlyingService;
	
	@Autowired(required=true)
	private PriceService priceService;
	
	@Autowired(required=true)
	private CalculationService calculationService;
	
	@Override
	public void setRequestDao(RequestDao requestDao)
	{
		this.requestDao = requestDao;
	}
	
	@Override
	public void setOptionRequestFactory(OptionRequestFactory factory)
	{
		this.optionRequestFactory = factory;
	}
	
	@Override
	public void setPriceService(PriceService priceService)
	{
		this.priceService = priceService;
	}
	
	@Override
	public void setCalculationService(CalculationService calculationService)
	{
		this.calculationService = calculationService;
	}
	
	public RequestServiceImpl() {}

	/**
	 * Determines if the request with the matching requestId exists in the request cache
	 * 
	 * @param 	requestId 	the requestId of the request that needs to be checked against.
	 * @returns true if a request with the requestId exists, otherwise false.
	 */
	@Override
	public boolean isRequestCached(int requestId)
	{
		return this.requests.containsKey(requestId);
	}

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher)
	{
		this.applicationEventPublisher = applicationEventPublisher;
	}

	/**
	 * Gets the request with the matching requestId.
	 * 
	 * @param 	requestId 	the snippet of the request that needs to be inserted.
	 * @return 	An instance of RequestDetailImpl if the request which matches the requestId can be found, otherwise null.
	 */
	@Override
	public RequestDetailImpl get(int requestId)
	{
		RequestDetailImpl request;
		
		ReentrantLock lock = new ReentrantLock();
		
		if(logger.isDebugEnabled())
			logger.debug("Getting the request with request ID: " + requestId);
		
		try
		{
			lock.lock();
		
			if(isRequestCached(requestId))
			{
				request = this.requests.get(requestId);
			}
			else
			{
				request = this.requestDao.get(requestId);
				
				if(request != null)
					this.requests.putIfAbsent(requestId, request);
			}
			
			return request;
		}
		finally
		{
			lock.unlock();
		}
	}
	
	/**
	 * Gets all requests previously saved to the cache only. Does not retrieve from the database.
	 * @returns a list of requests that were previously saved in the cache.
	 */
	@Override
	public Set<RequestDetailImpl> getAllFromCacheOnly()
	{
	
		return new HashSet<RequestDetailImpl>(this.requests.values());
	}
	
	/**
	 * Clears the cache of its contents and publishes a remove all price simulator event
	 * Once retrieved the map is cleared and the requests re-inserted.
	 */
	private void clearCache()
	{
		this.requests.clear();
	}

	/**
	 * Retrieves all the requests using the DAO instance.
	 * Once retrieved the map is cleared and the requests re-inserted.
	 * 
	 */
	@Override
	public Set<RequestDetailImpl> getAll()
	{
		ReentrantLock lock = new ReentrantLock();
		
		try
		{
			lock.lock();
			
			clearCache();
		
			Set<RequestDetailImpl> result = this.requestDao.getAll();
			
			if(result!= null)
			{
				// TODO - remove once OptionDetailImpl DB persistence is implemented.
				result.forEach(request -> this.optionRequestFactory.parseRequest(request));
				result.forEach(request -> this.calculationService.calculate(new BlackScholesModelImpl(), request));
				
				for(RequestDetailImpl request : result)
				{
					this.requests.put(request.getIdentifier(), request);
					
					for(OptionDetailImpl leg :  request.getLegs())
						this.applicationEventPublisher.publishEvent(new PriceSimulatorRequestEvent
								(this, PriceSimulatorRequestEnum.ADD_UNDERLYING, leg.getUnderlyingRIC()));
				}
				
				return result;
			}
			else
				return new HashSet<RequestDetailImpl>();
		}
		finally
		{
			lock.unlock();
		}
	}
	
	/**
	 * Retrieves all the requests from today only using the DAO instance.
	 * Once today's RFQs are retrieved the cache is cleared and the requests re-inserted.
	 * 
	 */
	@Override
	public Set<RequestDetailImpl> getAllFromTodayOnly()
	{
		ReentrantLock lock = new ReentrantLock();
		
		clearCache();
		
		try
		{
			lock.lock();
						
			Set<RequestDetailImpl> result = this.requestDao.getAll().stream()
				.filter(request -> request.getTradeDate().compareTo(LocalDate.now()) == 0).collect(Collectors.toSet());
						
			if(result!= null)
			{
				// TODO - remove once OptionDetailImpl DB persistence is implemented.
				result.forEach(request -> this.optionRequestFactory.parseRequest(request));
				result.forEach(request -> this.calculationService.calculate(new BlackScholesModelImpl(), request));
				
				for(RequestDetailImpl request : result)
				{
					this.requests.put(request.getIdentifier(), request);
					
					if(request.getLegs() == null)
						continue;
					
					for(OptionDetailImpl leg :  request.getLegs())
						this.applicationEventPublisher.publishEvent(new PriceSimulatorRequestEvent
								(this, PriceSimulatorRequestEnum.ADD_UNDERLYING, leg.getUnderlyingRIC()));
				}
				return result;
			}
			
			return new TreeSet<RequestDetailImpl>();
		}
		finally
		{
			lock.unlock();
		}
	}

	/**
	 * Inserts the request with the using passed parameters.
	 * 
	 * @param 	requestSnippet 	the snippet of the request that needs to be inserted.
	 * @param	clientId		the client ID of the request that needs to be inserted.
	 * @param	bookCode		the bookCode of the request that needs to be inserted.
	 * @param	savedByUser		the user that is inserting the request.
	 * @throws 	IllegalArgumentException	if requestSnippet, bookCode, or savedByUser parameters are null or empty.
	 */
	@Override
	public RequestDetailImpl insert(String requestSnippet, int clientId, String bookCode, String savedByUser)
	{
		if((requestSnippet == null) || requestSnippet.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("requestSnippet argument is invalid");
			
			throw new IllegalArgumentException("requestSnippet argument is invalid");
		}
		
		if((bookCode == null) || bookCode.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("bookCode argument is invalid");
			
			throw new IllegalArgumentException("bookCode argument is invalid");
		}
		
		if((savedByUser == null) || savedByUser.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("savedByUser argument is invalid");
			
			throw new IllegalArgumentException("savedByUser argument is invalid");
		}
		
		if(logger.isDebugEnabled())
			logger.debug("Insert request with snippet: " + requestSnippet);
		
		ReentrantLock lock = new ReentrantLock();
		
		try
		{
			lock.lock();
			
			RequestDetailImpl newRequest = this.optionRequestFactory.getNewInstance(requestSnippet, clientId, bookCode, savedByUser);
			
			if((newRequest != null) && (newRequest.getLegs() != null))
			{
		        // TODO select model depending on certain criteria
		        this.calculationService.calculate(new BlackScholesModelImpl(), newRequest);
				
				if(this.requestDao.insert(newRequest))
				{
					if(this.applicationEventPublisher != null)
						this.applicationEventPublisher.publishEvent(new NewRequestEvent(this, newRequest));
										
					this.requests.put(newRequest.getIdentifier(), newRequest);
					
					return newRequest;
				}
			}
			
			return null;
		}
		catch(IllegalArgumentException iae)
		{
			return null;
		}
		finally
		{
			lock.unlock();
		}
	}

	/**
	 * Deletes the request with the requestId passed as a parameter.
	 * 
	 * @param 	requestId 	the requestId of the request that needs to be deleted.
	 * @returns true if the request is deleted, otherwise false.
	 */
	@Override
	public boolean delete(int requestId)
	{
		if(logger.isDebugEnabled())
			logger.debug("Insert request with ID: " + requestId);
		
		ReentrantLock lock = new ReentrantLock();
		
		try
		{
			lock.lock();
		
			if(isRequestCached(requestId))
			{
				this.requests.remove(requestId);
				
				return this.requestDao.delete(requestId);
			}
		
			return false;
		}
		finally
		{
			lock.unlock();
		}
	}
	/**
	 * Used for unit testing mock.
	 * 
	 * @param 	requestDao 	the DAO object of type RequestDao.
	 */
	@Override
	public void setUserDao(RequestDao requestDao)
	{
		this.requestDao = requestDao;
	}

	/**
	 * Determines if the request with the requestId passed as a parameter exists or not.
	 * 
	 * @param 	requestId 	the requestId of the request that needs to be checked against.
	 * @returns true if the request exists, otherwise false.
	 */
	@Override
	public boolean requestExistsWithRequestId(int requestId)
	{
		if(isRequestCached(requestId))
			return true;
		else
			return this.requestDao.requestExistsWithRequestId(requestId);
	}

	// TODO need to revisit - can this be simplified using streams?
	@Override
	public void onApplicationEvent(PriceUpdateEvent event)
	{
		boolean isImpacted = false;
		for(RequestDetailImpl request : this.requests.values())
		{
			for(OptionDetailImpl leg : request.getLegs())
			{
				if(leg.getUnderlyingRIC().equals(event.getUnderlyingRIC()))
				{
					leg.setUnderlyingPrice(BigDecimal.valueOf(event.getPriceUpdate()));
					isImpacted = true;
				}
			}
			
			if(isImpacted)
			{
		        // TODO select model depending on certain criteria
		        this.calculationService.calculate(new BlackScholesModelImpl(), request);
				
				isImpacted = false;
			}
		}
	}

	/**
	 * Retrieves the price details from the price server for those underlying RICs
	 * that currently exist in the requests map's legs.
	 * 
	 * @returns a map of price details keyed by the underlying ric.
	 */
	@Override
	public Map<String, PriceDetailImpl> getPriceUpdates()
	{
		Set<String> underlyings = this.requests.values().stream().map(RequestDetailImpl::getUnderlyingRIC)
				.distinct().collect(Collectors.toSet());
			
		return getPriceUpdates(underlyings);
	}
	
	/**
	 * Retrieves the price details from the price server for those set of underlying RICs
	 * passed in as a parameter.
	 * 
	 * @param 	underlyings 	the set of underlying that prices are required for.
	 * @returns a map of price details keyed by the underlying ric.
	 * @throws NullPointerException if the set of underlyings is null.
	 */
	@Override
	public Map<String, PriceDetailImpl> getPriceUpdates(Set<String> underlyings)
	{
		if(underlyings == null)
		{
			if(logger.isErrorEnabled())
				logger.error("underlyings argument is invalid");
			
			throw new NullPointerException("underlyings argument is invalid");
		}
		
		return underlyings.stream().collect(Collectors
				.toMap(underlying -> underlying, underlying -> this.priceService.getAllPrices(underlying)));
	}

	/**
	 * Updates all of the request details passed in as a parameter.
	 * 
	 * @param 	requestToUpdate 	the request will overwrite the one persisted.
	 * @returns true if the update completed successfully, otherwise false.
	 * @throws NullPointerException if the set of requestToUpdate is null.
	 */
	@Override
	public boolean update(RequestDetailImpl requestToUpdate)
	{
		if(requestToUpdate == null)
		{
			if(logger.isErrorEnabled())
				logger.error("requestToUpdate argument is invalid");
			
			throw new NullPointerException("requestToUpdate argument is invalid");
		}
		
		Boolean result = this.requestDao.update(requestToUpdate);
		
		return result && this.updateCache(requestToUpdate.getIdentifier());
	}

	/**
	 * Updates the status of the request passed in as a parameter.
	 * 
	 * @param 	requestToUpdate 	the request's status will overwrite the one persisted.
	 * @returns true if the status update is completed successfully, otherwise false.
	 * @throws NullPointerException if the set of requestToUpdate is null.
	 */
	@Override
	public boolean updateStatus(RequestDetailImpl requestToUpdate)
	{
		if(requestToUpdate == null)
		{
			if(logger.isErrorEnabled())
				logger.error("requestToUpdate argument is invalid");
			
			throw new NullPointerException("requestToUpdate argument is invalid");
		}
		
		boolean result = this.requestDao.updateStatus(requestToUpdate);
		
		return result && this.updateCache(requestToUpdate.getIdentifier());
	}
	
	/**
	 * Updates the cache with a request that either does not exist in or is out-of-date.
	 * The latest update comes from the persistence store.
	 * 
	 * @param 	identifier 	the request's identifier.
	 * @returns true if the cache update is completed successfully, otherwise false.
	 */
	public boolean updateCache(int identifier)
	{
		ReentrantLock lock = new ReentrantLock();
		
		try
		{
			lock.lock();
		
			if(isRequestCached(identifier))
				return this.requests.put(identifier, this.requestDao.get(identifier)) != null;
			else
				return this.get(identifier) != null;
		}
		finally
		{
			lock.unlock();
		}
	}

	/**
	 * Returns a set of all requests as there is no way to determine which ones have has changed.
	 * 
	 * @returns a set of requests.
	 */
	@Override
	public Set<RequestDetailImpl> getStatusUpdates()
	{
		return this.requests.values().stream().collect(Collectors.toSet());
	}
	
	/**
	 * Returns the requests that have changed status amongst the set of requests passed in as an argument.
	 * Some of the request may not exist in the cache (old requests) and will be retrieved from the
	 * persistence store.
	 * 
	 * @returns a set of requests that have changed status.
	 * @throws NullPointerException if the set of setOfRequests is null.
	 */
	@Override
	public Set<RequestDetailImpl> getStatusUpdates(Set<RequestDetailImpl> setOfRequests)
	{
		if(setOfRequests == null)
		{
			if(logger.isErrorEnabled())
				logger.error("setOfRequests argument is invalid");
			
			throw new NullPointerException("setOfRequests argument is invalid");
		}
		
		return setOfRequests.stream().filter(request ->
			request.getStatus() != this.get(request.getIdentifier()).getStatus())
			.collect(Collectors.toSet());
	}

	/**
	 * Returns the requests that have calculation changes for cached requests only.
	 * 
	 * @returns a map of calculation results keyed by the unique request ID.
	 */
	@Override
	public Map<Integer, Map<String, BigDecimal>> getCalculationUpdates()
	{
		return new TreeMap<Integer, Map<String, BigDecimal>>();
	}

	/**
	 * Returns the requests that have calculation changes amongst the set of requests passed in as an argument.
	 * Some of the request may not exist in the cache (old requests) and will be retrieved from the
	 * persistence store.
	 * 
	 * @returns a map of calculation results keyed by the unique request ID.
	 * @throws NullPointerException if the set of setOfRequests is null.
	 */
	@Override
	public Map<Integer, Map<String, BigDecimal>> getCalculationUpdates(Set<RequestDetailImpl> setOfRequests)
	{
		if(setOfRequests == null)
		{
			if(logger.isErrorEnabled())
				logger.error("setOfRequests argument is invalid");
			
			throw new NullPointerException("setOfRequests argument is invalid");
		}
		
		return new TreeMap<Integer, Map<String, BigDecimal>>();
	}
	
	/**
	 * Returns the requests that match the search criteria.
	 * All items will be retrieved from the persistence store.
	 * 
	 * @returns a set of request results matching the search criteria
	 * @throws NullPointerException if the set of criteria is null.
	 */
	@Override
	public Set<RequestDetailImpl> search(Set<SearchCriterionImpl> criteria)
	{
		if(criteria == null)
		{
			if(logger.isErrorEnabled())
				logger.error("criteria argument is invalid");
			
			throw new NullPointerException("criteria argument is invalid");
		}
		
		if(logger.isDebugEnabled())
			logger.debug("Searching for requests with criteria: " + criteria);
		
		ReentrantLock lock = new ReentrantLock();
		
		try
		{
			lock.lock();
			
			Set<RequestDetailImpl> requests = this.requestDao.search(criteria);
			// TODO debug
/*			requests.forEach(request -> this.optionRequestFactory.parseRequest(request));
			requests.forEach(request -> this.calculationService.calculate(new BlackScholesModelImpl(), request))*/;
			return requests;
			
		}
		finally
		{
			lock.unlock();
		}
	}
}
