package com.leon.rfq.services;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.locks.ReentrantLock;

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
import com.leon.rfq.domains.RequestDetailImpl;
import com.leon.rfq.domains.UnderlyingDetailImpl;
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
	
	@Autowired(required=true)
	private RequestDao requestDao;
	
	@Autowired(required=true)
	private OptionRequestFactory optionRequestFactory;
	
	@Autowired(required=true)
	private UnderlyingService underlyingService;
	
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
	public List<RequestDetailImpl> getAllFromCacheOnly()
	{
	
		return new LinkedList<RequestDetailImpl>(this.requests.values());
	}
	
	/**
	 * Clears the cache of its contents and publishes a remove all price simulator event
	 * Once retrieved the map is cleared and the requests re-inserted.
	 */
	private void clearCache()
	{
		this.requests.clear();
		
		this.applicationEventPublisher.publishEvent(
				new PriceSimulatorRequestEvent(this, PriceSimulatorRequestEnum.REMOVE_ALL));
	}

	/**
	 * Retrieves all the requests using the DAO instance.
	 * Once retrieved the map is cleared and the requests re-inserted.
	 * 
	 */
	@Override
	public List<RequestDetailImpl> getAll()
	{
		if(logger.isDebugEnabled())
			logger.debug("Getting all the requests");
		
		ReentrantLock lock = new ReentrantLock();
		
		try
		{
			lock.lock();
			
			clearCache();
		
			List<RequestDetailImpl> result = this.requestDao.getAll();
			
			if(result!= null)
			{
				// Could use a more complicated lambda expression here but below is far simpler
				for(RequestDetailImpl request : result)
				{
					this.requests.put(request.getIdentifier(), request);
					
					for(OptionDetailImpl leg :  request.getLegs())
					{
						UnderlyingDetailImpl underlying = this.underlyingService.get(leg.getUnderlyingRIC());
						
						this.applicationEventPublisher.publishEvent(new PriceSimulatorRequestEvent
								(this, PriceSimulatorRequestEnum.ADD_UNDERLYING,
										leg.getUnderlyingRIC(),
										underlying.getReferencePrice(),
										underlying.getSimulationPriceVariance(),
										underlying.getSpread()));
					}
				}
				
				return result;
			}
			else
				return new LinkedList<RequestDetailImpl>();
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
	public List<RequestDetailImpl> getAllFromTodayOnly()
	{
		if(logger.isDebugEnabled())
			logger.debug("Getting all the requests from today");
		
		ReentrantLock lock = new ReentrantLock();
		
		clearCache();
		
		try
		{
			lock.lock();
			
			List<RequestDetailImpl> result = this.requestDao.getAll();
					//.stream() //TODO
					//.filter(request -> request.getTradeDate().compareTo(LocalDate.now()) <= 0)
					//.collect(Collectors.toList());
			
			if(result!= null)
			{
				// Could use a more complicated lambda expression here but below is far simpler
				for(RequestDetailImpl request : result)
				{
					this.requests.put(request.getIdentifier(), request);
					
					if(request.getLegs() == null)
						continue;
					
					for(OptionDetailImpl leg :  request.getLegs())
					{
						UnderlyingDetailImpl underlying = this.underlyingService.get(leg.getUnderlyingRIC());
						
						this.applicationEventPublisher.publishEvent(new PriceSimulatorRequestEvent
								(this, PriceSimulatorRequestEnum.ADD_UNDERLYING,
										leg.getUnderlyingRIC(),
										underlying.getReferencePrice(),
										underlying.getSimulationPriceVariance(),
										underlying.getSpread()));
					}
				}
				// TODO - change back top "== 0"
				return result;
			}
			else
				return new LinkedList<RequestDetailImpl>();
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
	public boolean insert(String requestSnippet, int clientId, String bookCode, String savedByUser)
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
		        CalculationServiceImpl.calculate(new BlackScholesModelImpl(), newRequest);
				
				if(this.requestDao.insert(newRequest))
				{
					this.applicationEventPublisher.publishEvent(new NewRequestEvent(this, newRequest));
										
					this.requests.put(newRequest.getIdentifier(), newRequest);
					
					return true;
				}
			}
			
			return false;
		}
		catch(IllegalArgumentException iae)
		{
			throw iae;
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
		        CalculationServiceImpl.calculate(new BlackScholesModelImpl(), request);
				
				isImpacted = false;
			}
		}
	}
}
