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
import com.leon.rfq.option.OptionRequestParser;
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
	private OptionRequestParser optionRequestParser;
	
	@Override
	public void setRequestDao(RequestDao requestDao)
	{
		this.requestDao = requestDao;
	}
	
	public RequestServiceImpl()
	{
		//this.getAll(); //TODO.
	}
	
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
		
		RequestDetailImpl newRequest = new RequestDetailImpl();
		
		if(this.optionRequestParser.isValidOptionRequestSnippet(requestSnippet))
			this.optionRequestParser.parseRequest(requestSnippet, newRequest);
		
		newRequest.setBookCode(bookCode);
		newRequest.setClientId(clientId);
		newRequest.setLastUpdatedBy(savedByUser);
		
		boolean result = this.requestDao.insert(newRequest);
		
		this.applicationEventPublisher.publishEvent(new NewRequestEvent(this, newRequest)); //TODO request ID
		
		this.requests.put(newRequest.getIdentifier(), newRequest);

		return result;
	}

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

	@Override
	public void setUserDao(RequestDao requestDao)
	{
		this.requestDao = requestDao;
	}

	@Override
	public boolean requestExistsWithRequestId(int requestId)
	{
		if(isRequestCached(requestId))
			return true;
		else
			return this.requestDao.requestExistsWithRequestId(requestId);
	}
}
