package com.leon.rfq.mappers;

import java.util.List;

import com.leon.rfq.domains.ClientDetailImpl;

public interface ClientMapper
{
	List<ClientDetailImpl> getAll();
	
	ClientDetailImpl get(int clientId);
	
	int delete(int clientId);
	
	int insert(ClientDetailImpl clientDetailImpl);
	
	int update(ClientDetailImpl clientDetailImpl);
	
	ClientDetailImpl clientExistsWithClientId(int clientId);
}
