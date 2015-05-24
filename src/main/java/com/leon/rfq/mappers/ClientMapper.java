package com.leon.rfq.mappers;

import java.util.List;

import com.leon.rfq.domains.ClientDetailImpl;

public interface ClientMapper
{
	List<ClientDetailImpl> getAll();
	
	ClientDetailImpl get(String clientName);
	
	int delete(String clientId);
	
	int insert(ClientDetailImpl clientDetailImpl);
	
	int update(ClientDetailImpl clientDetailImpl);
	
	ClientDetailImpl clientExistsWithClientName(String clientName);
}
