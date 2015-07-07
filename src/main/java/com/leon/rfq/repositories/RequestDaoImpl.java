package com.leon.rfq.repositories;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.leon.rfq.domains.RequestDetailImpl;
import com.leon.rfq.mappers.RequestMapper;
import com.leon.rfq.products.OptionRequestFactory;

@Repository
public class RequestDaoImpl implements RequestDao
{
	private static final Logger logger = LoggerFactory.getLogger(RequestDaoImpl.class);
	
	@Autowired(required=true)
	private RequestMapper requestMapper;
	
	@Autowired(required=true)
	private OptionRequestFactory optionRequestFactory;

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
	public boolean updateStatus(RequestDetailImpl requestToUpdate)
	{
		return this.requestMapper.updateStatus(requestToUpdate) == 1;
	}
	
	@Override
	public boolean update(RequestDetailImpl requestToUpdate)
	{
		return this.requestMapper.update(requestToUpdate) == 1;
	}

	@Override
	public Set<RequestDetailImpl> getAll()
	{
		Set<RequestDetailImpl> result = this.requestMapper.getAll();
			
		return result;
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

