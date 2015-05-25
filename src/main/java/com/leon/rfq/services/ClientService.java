package com.leon.rfq.services;

import java.util.List;

import com.leon.rfq.common.EnumTypes.ClientTierEnum;
import com.leon.rfq.common.Tag;
import com.leon.rfq.domains.ClientDetailImpl;
import com.leon.rfq.repositories.ClientDao;

public interface ClientService
{
	void initialise();
	
	ClientDetailImpl get(String clientName);

	List<ClientDetailImpl> getAll();
	
	List<ClientDetailImpl> getAllFromCacheOnly();
	
	boolean insert(String clientName, ClientTierEnum tier, boolean isValid, String savedByUser);
	
	boolean update(int clientId, String clientName, ClientTierEnum tier, boolean isValid, String updatedByUser);
	
	boolean delete(String clientName);

	void setClientDao(ClientDao clientDao);
	
	boolean isClientCached(String clientName);
	
	boolean clientExistsWithClientName(String clientName);

	List<Tag> getMatchingClientTags(String pattern);
}
