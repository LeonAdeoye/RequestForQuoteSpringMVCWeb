package com.leon.rfq.services;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

import com.leon.rfq.domains.RequestDetailImpl;
import com.leon.rfq.events.NewRequestEvent;
import com.leon.rfq.option.OptionRequestFactory;
import com.leon.rfq.repositories.RequestDao;

@Service
public final class RequestServiceImpl implements RequestService, ApplicationEventPublisherAware
{
	private static Logger logger = LoggerFactory.getLogger(RequestServiceImpl.class);
	private ApplicationEventPublisher applicationEventPublisher;
	private final Map<Integer, RequestDetailImpl> requests = new HashMap<>();
	
	@Autowired(required=true)
	private RequestDao requestDao;
	
	@Autowired(required=true)
	private OptionRequestFactory optionRequestFactory;
	
	@Override
	public void setRequestDao(RequestDao requestDao)
	{
		this.requestDao = requestDao;
	}
	
	@Override
	public void setOptionRequestFactory(OptionRequestFactory optionRequestFactory)
	{
		this.optionRequestFactory = optionRequestFactory;
	}
	
	public RequestServiceImpl()
	{
		//this.getAll(); //TODO.
	}

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
		
		if(isRequestCached(requestId))
		{
			request = this.requests.get(requestId);
		}
		else
		{
			request = this.requestDao.get(requestId);
			if(request != null)
				this.requests.put(requestId, request);
		}
		
		return request;
	}

	/**
	 * Retrieves all the requests using the DAO instance.
	 * Once retrieved the map is cleared and the requests re-inserted.
	 * 
	 */
	@Override
	public List<RequestDetailImpl> getAll()
	{
		List<RequestDetailImpl> result = this.requestDao.getAll();
		
		if(result!= null)
		{
			this.requests.clear();
			
			// Could use a more complicated lambda expression here but below is far simpler
			for(RequestDetailImpl request : result)
				this.requests.put(request.getIdentifier(), request);
			
			return result;
		}
		else
			return new LinkedList<RequestDetailImpl>();
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
		
		boolean result = false;
		try
		{
			RequestDetailImpl newRequest = this.optionRequestFactory.getNewInstance(requestSnippet, clientId, bookCode, savedByUser);
			
			result = this.requestDao.insert(newRequest);
		
			this.applicationEventPublisher.publishEvent(new NewRequestEvent(this, newRequest)); //TODO request ID
		
			this.requests.put(newRequest.getIdentifier(), newRequest);
		}
		catch(IllegalArgumentException iae)
		{
			throw iae;
		}

		return result;
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
		if(isRequestCached(requestId))
		{
			this.requests.remove(requestId);
			
			return this.requestDao.delete(requestId);
		}
		
		return false;
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
}
