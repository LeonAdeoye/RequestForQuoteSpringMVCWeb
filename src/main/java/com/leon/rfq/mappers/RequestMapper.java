package com.leon.rfq.mappers;

import java.util.List;

import com.leon.rfq.domains.RequestDetailImpl;

public interface RequestMapper
{
	List<RequestDetailImpl> getAll();
	
	RequestDetailImpl get(String requestId);
	
	int delete(String requestId);
	
	int insert(String bookCode, int clientId, String savedByUser);
	
	int update(String requestId, String bookCode, int clientId, boolean isValid, String updatedByUser);
	
	RequestDetailImpl requestExistsWithReqestId(String requestId);
}
