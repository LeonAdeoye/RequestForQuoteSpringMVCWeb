package com.leon.rfq.repositories;

import java.util.List;

import com.leon.rfq.common.EnumTypes.ClientTierEnum;
import com.leon.rfq.domains.ClientDetailImpl;

public interface ClientDao
{
	boolean delete(String clientName);

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
	 
	ClientDetailImpl get(String clientName);

	boolean clientExistsWithClientName(String clientName);
}
