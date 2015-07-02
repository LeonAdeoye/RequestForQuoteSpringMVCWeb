package com.leon.rfq.repositories;

import java.util.List;

import com.leon.rfq.domains.RequestDetailImpl;

public interface RequestDao
{
	boolean delete(int requestId);

	boolean insert(RequestDetailImpl newRequest);
	 
	boolean updateStatus(RequestDetailImpl requestToUpdate);
	
	boolean update(RequestDetailImpl requestToUpdate);

	List<RequestDetailImpl> getAll();
	 
	RequestDetailImpl get(int requestId);
	
	boolean requestExistsWithRequestId(int requestId);
}
