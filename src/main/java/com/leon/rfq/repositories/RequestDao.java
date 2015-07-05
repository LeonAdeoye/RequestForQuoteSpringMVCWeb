package com.leon.rfq.repositories;

import java.util.Set;

import com.leon.rfq.domains.RequestDetailImpl;

public interface RequestDao
{
	boolean delete(int requestId);

	boolean insert(RequestDetailImpl newRequest);
	 
	boolean updateStatus(RequestDetailImpl requestToUpdate);
	
	boolean update(RequestDetailImpl requestToUpdate);

	Set<RequestDetailImpl> getAll();
	 
	RequestDetailImpl get(int requestId);
	
	boolean requestExistsWithRequestId(int requestId);
}
