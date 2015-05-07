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
	public boolean delete(String requestId)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean insert(String requestId, String bookCode, int clientId, String savedByUser)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean update(String requestId, String bookCode, int clientId, boolean isValid, String updatedByUser)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<RequestDetailImpl> getAll()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RequestDetailImpl get(String requestId)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean requestExistsWithRequestId(String requestId)
	{
		// TODO Auto-generated method stub
		return false;
	}

}

