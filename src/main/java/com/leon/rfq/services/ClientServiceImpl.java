package com.leon.rfq.services;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.leon.rfq.common.EnumTypes.ClientTierEnum;
import com.leon.rfq.domains.ClientDetailImpl;
import com.leon.rfq.repositories.ClientDao;

@Service
public final class ClientServiceImpl implements ClientService
{
	private static final Logger logger = LoggerFactory.getLogger(ClientServiceImpl.class);
	private ApplicationEventPublisher applicationEventPublisher;
	private final Map<Integer, ClientDetailImpl> clients = new HashMap<>();
	
	@Autowired
	private ClientDao clientDao;
	
	// For unit testing mocking framework.
	@Override
	public void setClientDao(ClientDao clientDao)
	{
		this.clientDao = clientDao;
	}
	
	public ClientServiceImpl()
	{
		//this.getAll();
	}
	
	@Override
	public boolean isClientCached(int clientId)
	{
		return this.clients.containsKey(clientId);
	}
	
	@Override
	public ClientDetailImpl get(int clientId)
	{
		ClientDetailImpl client;
		
		if(isClientCached(clientId))
			client = this.clients.get(clientId);
		else
		{
			client = this.clientDao.get(clientId);
			if(client != null)
				this.clients.put(clientId, client);
		}
		
		return client;
	}
	
	@Override
	public boolean clientExistsWithClientId(int clientId)
	{
		return isClientCached(clientId) ? true : this.clientDao.clientExistsWithClientId(clientId);
	}
		
	@Override
	public List<ClientDetailImpl> getAll()
	{
		List<ClientDetailImpl> result = this.clientDao.getAll();
		
		if(result!= null)
		{
			this.clients.clear();
			
			// Could use a more complicated lambda expression here but below is far simpler
			for(ClientDetailImpl client : result)
				this.clients.put(client.getClientId(), client);
			
			return result;
		}
		else
			return new LinkedList<ClientDetailImpl>();
	}

	@Override
	public boolean insert(String clientName, ClientTierEnum tier, boolean isValid, String savedByUser)
	{
		if((clientName == null) || clientName.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("clientName argument is invalid");
			
			throw new IllegalArgumentException("clientName argument is invalid");
		}
		
		if((savedByUser == null) || savedByUser.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("savedByUser argument is invalid");
			
			throw new IllegalArgumentException("savedByUser argument is invalid");
		}
			
		return this.clientDao.insert(clientName, tier, isValid, savedByUser);
	}

	@Override
	public boolean delete(int clientId)
	{
		if(isClientCached(clientId))
		{
			this.clients.remove(clientId);
			
			return this.clientDao.delete(clientId);
		}
		
		return false;
	}

	@Override
	public boolean update(int clientId, String clientName, ClientTierEnum tier,
			boolean isValid, String updatedByUser)
	{
		if((clientName == null) || clientName.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("clientName argument is invalid");
			
			throw new IllegalArgumentException("clientName argument is invalid");
		}
		
		if((updatedByUser == null) || updatedByUser.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("updatedByUser argument is invalid");
			
			throw new IllegalArgumentException("updatedByUser argument is invalid");
		}

		return this.clientDao.update(clientId, clientName, tier, isValid, updatedByUser);
	}
}
