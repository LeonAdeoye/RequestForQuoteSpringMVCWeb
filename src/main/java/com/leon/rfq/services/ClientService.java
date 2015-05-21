package com.leon.rfq.services;

import java.util.List;

import com.leon.rfq.common.EnumTypes.ClientTier;
import com.leon.rfq.domains.ClientDetailImpl;
import com.leon.rfq.repositories.ClientDao;

public interface ClientService
{
	ClientDetailImpl get(int clientId);

	List<ClientDetailImpl> getAll();
	
	boolean insert(String clientName, ClientTier tier, boolean isValid, String savedByUser);
	
	boolean update(int clientId, String clientName, ClientTier tier, boolean isValid, String updatedByUser);
	
	boolean delete(int clientId);

	void setClientDao(ClientDao clientDao);
	
	boolean isClientCached(int clientId);
	
	boolean clientExistsWithClientId(int clientId);
}
