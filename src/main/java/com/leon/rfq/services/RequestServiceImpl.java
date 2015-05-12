package com.leon.rfq.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

import com.leon.rfq.dao.RequestDao;
import com.leon.rfq.domains.RequestDetailImpl;
import com.leon.rfq.events.NewRequestEvent;

@Service
public class RequestServiceImpl implements RequestService, ApplicationEventPublisherAware
{
	private static Logger logger = LoggerFactory.getLogger(RequestServiceImpl.class);
	private ApplicationEventPublisher applicationEventPublisher;
	
	@Autowired
	private RequestDao requestDao;

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher)
	{
		this.applicationEventPublisher = applicationEventPublisher;
	}

	@Override
	public RequestDetailImpl get(int requestId)
	{
		return this.requestDao.get(requestId);
	}

	@Override
	public List<RequestDetailImpl> getAll()
	{
		return this.requestDao.getAll();
	}

	@Override
	public boolean insert(String requestSnippet, int clientId, String bookCode, String savedByUser)
	{
		boolean result = this.requestDao.insert(bookCode, clientId, savedByUser);
		
		this.applicationEventPublisher.publishEvent(new NewRequestEvent(this, new RequestDetailImpl())); //TODO
		
		return result;
	}

	@Override
	public boolean delete(int requestId)
	{
		return this.requestDao.delete(requestId);
	}

	@Override
	public void setUserDao(RequestDao requestDao)
	{
		this.requestDao = requestDao;
	}

	@Override
	public boolean isRequestCached(int requestId)
	{
		return false;
	}

	@Override
	public boolean requestExistsWithRequestId(int requestId)
	{
		return this.requestDao.requestExistsWithRequestId(requestId);
	}
}
