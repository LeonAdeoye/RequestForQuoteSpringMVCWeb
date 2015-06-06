package com.leon.rfq.mappers;

import java.util.Set;

import com.leon.rfq.domains.ClientDetailImpl;

public interface ClientMapper
{
	Set<ClientDetailImpl> getAll();
	
	ClientDetailImpl get(String clientName);
	
	int delete(String clientId);
	
	int insert(ClientDetailImpl clientDetailImpl);
	
	int update(ClientDetailImpl clientDetailImpl);
	
	ClientDetailImpl clientExistsWithClientName(String clientName);
}
