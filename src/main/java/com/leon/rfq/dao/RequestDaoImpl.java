package com.leon.rfq.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.leon.rfq.domains.RequestDetailImpl;
import com.leon.rfq.mappers.RequestMapper;

@Repository
public class RequestDaoImpl implements RequestDao
{
	private static final Logger logger = LoggerFactory.getLogger(RequestDaoImpl.class);
	
	@Autowired
	private RequestMapper requestMapper;

	@Override
	public boolean delete(int requestId)
	{
		return this.requestMapper.delete(requestId) == 1;
	}

	@Override
	public boolean insert(RequestDetailImpl newRequest)
	{
		return this.requestMapper.insert(newRequest) == 1;
	}

	@Override
	public boolean update(int requestId, String bookCode, int clientId, boolean isValid, String updatedByUser)
	{
		return this.requestMapper.update(requestId, bookCode, clientId, isValid, updatedByUser) == 1;
	}

	@Override
	public List<RequestDetailImpl> getAll()
	{
		return this.requestMapper.getAll();
	}

	@Override
	public RequestDetailImpl get(int requestId)
	{
		return this.requestMapper.get(requestId);
	}

	@Override
	public boolean requestExistsWithRequestId(int requestId)
	{
		return this.requestMapper.requestExistsWithRequestId(requestId) != null;
	}

}

