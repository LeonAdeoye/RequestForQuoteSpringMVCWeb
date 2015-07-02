package com.leon.rfq.mappers;

import java.util.List;

import com.leon.rfq.domains.RequestDetailImpl;

public interface RequestMapper
{
	List<RequestDetailImpl> getAll();
	
	RequestDetailImpl get(int requestId);
	
	int delete(int requestId);
	
	int insert(RequestDetailImpl newRequest);
	
	int update(RequestDetailImpl newRequest);
	
	int updateStatus(RequestDetailImpl newRequest);
	
	RequestDetailImpl requestExistsWithRequestId(int requestId);
}
