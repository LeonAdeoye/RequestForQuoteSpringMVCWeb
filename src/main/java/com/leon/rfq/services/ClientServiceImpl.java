package com.leon.rfq.services;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

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
	private final Map<String, ClientDetailImpl> clients = new HashMap<>();
	
	@Autowired
	private ClientDao clientDao;
	
	@PostConstruct
	public void initialise()
	{
		this.getAll();
	}
	
	// For unit testing mocking framework.
	@Override
	public void setClientDao(ClientDao clientDao)
	{
		this.clientDao = clientDao;
	}
	
	public ClientServiceImpl()
	{
	}
	
	@Override
	public boolean isClientCached(String clientName)
	{
		return this.clients.containsKey(clientName);
	}
	
	@Override
	public ClientDetailImpl get(String clientName)
	{
		if((clientName == null) || clientName.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("clientName argument is invalid");
			
			throw new IllegalArgumentException("clientName argument is invalid");
		}
		
		ClientDetailImpl client;
		
		if(isClientCached(clientName))
			client = this.clients.get(clientName);
		else
		{
			client = this.clientDao.get(clientName);
			if(client != null)
				this.clients.put(clientName, client);
		}
		
		return client;
	}
		
	@Override
	public boolean clientExistsWithClientName(String clientName)
	{
		if((clientName == null) || clientName.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("clientName argument is invalid");
			
			throw new IllegalArgumentException("clientName argument is invalid");
		}
		
		return isClientCached(clientName) ? true : this.clientDao.clientExistsWithClientName(clientName);
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
				this.clients.put(client.getName(), client);
			
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
		
		if(!isClientCached(clientName))
		{
			this.clients.put(clientName, new ClientDetailImpl(clientName, tier, isValid, savedByUser));
			return this.clientDao.insert(clientName, tier, isValid, savedByUser);
		}
			
		return false;
	}

	@Override
	public boolean delete(String clientName)
	{
		if((clientName == null) || clientName.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("clientName argument is invalid");
			
			throw new IllegalArgumentException("clientName argument is invalid");
		}
		
		if(isClientCached(clientName))
		{
			this.clients.remove(clientName);
			return this.clientDao.delete(clientName);
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

		if(isClientCached(clientName))
		{
			this.clients.put(clientName, new ClientDetailImpl(clientName, clientId, tier, isValid, updatedByUser));
			return this.clientDao.update(clientId, clientName, tier, isValid, updatedByUser);
		}
		else
			return false;
	}
}
