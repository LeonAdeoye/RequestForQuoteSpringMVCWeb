package com.leon.rfq.repositories;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.leon.rfq.common.EnumTypes.ClientTierEnum;
import com.leon.rfq.domains.ClientDetailImpl;
import com.leon.rfq.mappers.ClientMapper;

@Repository
public class ClientDaoImpl implements ClientDao
{
	private static final Logger logger = LoggerFactory.getLogger(ClientDaoImpl.class);
	
	@Autowired
	private ClientMapper clientMapper;

	@Override
	public boolean delete(String clientName)
	{
		if(logger.isDebugEnabled())
			logger.debug("Delete the client with client name " + clientName);
		
		try
		{
			return this.clientMapper.delete(clientName) == 1;
		}
		catch(Exception e)
		{
			if(logger.isErrorEnabled())
				logger.error("Failed to delete the client with client name " + clientName + " because of exception: " + e);
			
			return false;
		}
	}

	@Override
	public boolean insert(String clientName, ClientTierEnum tier, boolean isValid, String savedByUser)
	{
		if(logger.isDebugEnabled())
			logger.debug("Inserting a new client with clientName: " + clientName);
		
		try
		{
			ClientDetailImpl newClient = new ClientDetailImpl(clientName, tier, isValid, savedByUser);
			return this.clientMapper.insert(newClient) == 1;
		}
		catch(Exception e)
		{
			if(logger.isErrorEnabled())
				logger.error("Failed to insert the client with clientId " + clientName + " because of exception: " + e);
			
			return false;
		}
	}
	
	@Override
	public boolean update(int clientId, String clientName, ClientTierEnum tier, boolean isValid, String updatedByUser)
	{
		if(logger.isDebugEnabled())
			logger.debug("Update the client with client ID: " + clientId);
		
		try
		{
			return this.clientMapper.update(new ClientDetailImpl(clientName, clientId, tier, isValid, updatedByUser)) == 1;
		}
		catch(Exception e)
		{
			if(logger.isErrorEnabled())
				logger.error("Failed to update the client with client ID:" + clientId + " because of exception: " + e);
			
			return false;
		}
	}

	@Override
	public Set<ClientDetailImpl> getAll()
	{
		if(logger.isDebugEnabled())
			logger.debug("Request to get all clients");
		
		return this.clientMapper.getAll();
	}

	@Override
	public ClientDetailImpl get(String clientName)
	{
		if(logger.isDebugEnabled())
			logger.debug("Request to get client with client name: " + clientName);
		
		return this.clientMapper.get(clientName);
	}
		
	@Override
	public boolean clientExistsWithClientName(String clientName)
	{
		if(logger.isDebugEnabled())
			logger.debug("Request to check if client exists with client name: " + clientName);
		
		return this.clientMapper.clientExistsWithClientName(clientName) != null;
	}
}
