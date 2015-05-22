package com.leon.rfq.repositories;

import java.util.List;

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
	public boolean delete(int clientId)
	{
		if(logger.isDebugEnabled())
			logger.debug("Delete the client with clientId " + clientId);
		
		try
		{
			return this.clientMapper.delete(clientId) == 1;
		}
		catch(Exception e)
		{
			if(logger.isErrorEnabled())
				logger.error("Failed to delete the client with clientId " + clientId + " because of exception: " + e);
			
			return false;
		}
	}

	@Override
	public boolean insert(String clientName, ClientTierEnum tier, boolean isValid,	String savedByUser)
	{
		if(logger.isDebugEnabled())
			logger.debug("Insert the client with clientName: " + clientName);
		
		try
		{
			return this.clientMapper.insert(new ClientDetailImpl(clientName, -1, tier, isValid, savedByUser)) == 1;
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
			logger.debug("Update the client with clientId " + clientId);
		
		try
		{
			return this.clientMapper.update(new ClientDetailImpl(clientName, clientId, tier, isValid, updatedByUser)) == 1;
		}
		catch(Exception e)
		{
			if(logger.isErrorEnabled())
				logger.error("Failed to update the client with clientId " + clientId + " because of exception: " + e);
			
			return false;
		}
	}

	@Override
	public List<ClientDetailImpl> getAll()
	{
		if(logger.isDebugEnabled())
			logger.debug("Request to get all clients");
		
		logger.debug(String.format("Size of clients to be returned: %d", this.clientMapper.getAll().size()));

		return this.clientMapper.getAll();
	}

	@Override
	public ClientDetailImpl get(int clientId)
	{
		if(logger.isDebugEnabled())
			logger.debug("Request to get client with clientId: " + clientId);
		
		return this.clientMapper.get(clientId);
	}
	
	@Override
	public boolean clientExistsWithClientId(int clientId)
	{
		if(logger.isDebugEnabled())
			logger.debug("Request to check if client exists with clientId: " + clientId);
		
		return this.clientMapper.clientExistsWithClientId(clientId) != null;
	}
}
