package com.leon.rfq.repositories;

import java.util.List;

import com.leon.rfq.common.EnumTypes.ClientTierEnum;
import com.leon.rfq.domains.ClientDetailImpl;

public interface ClientDao
{
	boolean delete(int clientId);

	boolean insert(String clientName,
			ClientTierEnum tier,
			boolean isValid,
			String savedByUser);
	
	boolean update(int clientId,
			String clientName,
			ClientTierEnum tier,
			boolean isValid,
			String savedByUser);

	List<ClientDetailImpl> getAll();
	 
	ClientDetailImpl get(int clientId);

	boolean clientExistsWithClientId(int clientId);
}
