package com.leon.rfq.repositories;

import java.util.List;

import com.leon.rfq.domains.RequestDetailImpl;

public interface RequestDao
{
	boolean delete(int requestId);

	boolean insert(RequestDetailImpl newRequest);
	 
	boolean update(int requestId, String bookCode, int clientId,	boolean isValid, String requesedByUser);

	List<RequestDetailImpl> getAll();
	 
	RequestDetailImpl get(int requestId);
	
	boolean requestExistsWithRequestId(int requestId);
}
