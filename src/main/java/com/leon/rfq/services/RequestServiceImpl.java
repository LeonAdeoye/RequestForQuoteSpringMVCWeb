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

@Service
public class RequestServiceImpl implements RequestService, ApplicationEventPublisherAware
{
	private static Logger logger = LoggerFactory.getLogger(RequestServiceImpl.class);
	private ApplicationEventPublisher applicationEventPublisher;
	
	@Autowired
	private RequestDao requestDao;

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public RequestDetailImpl get(String requestId)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RequestDetailImpl> getAll()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean insert(String requestSnippet, int clientId, String bookName, String savedByUser)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(String requestId)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateValidity(String userId, boolean isValid, String updatedByUser)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setUserDao(RequestDao requestDao)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isRequestCached(String requestId)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean requestExistsWithRequestId(String requestId)
	{
		// TODO Auto-generated method stub
		return false;
	}
}
