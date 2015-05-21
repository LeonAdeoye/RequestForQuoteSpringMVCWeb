package com.leon.rfq.repositories;

import java.util.List;

import com.leon.rfq.common.EnumTypes.ClientTier;
import com.leon.rfq.domains.ClientDetailImpl;

public interface ClientDao
{
	boolean delete(int clientId);

	boolean insert(String clientName,
			ClientTier tier,
			boolean isValid,
			String savedByUser);
	
	boolean update(int clientId,
			String clientName,
			ClientTier tier,
			boolean isValid,
			String savedByUser);

	List<ClientDetailImpl> getAll();
	 
	ClientDetailImpl get(int clientId);

	boolean clientExistsWithClientId(int clientId);
}
