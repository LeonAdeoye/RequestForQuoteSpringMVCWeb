package com.leon.rfq.dao;

import java.util.List;

import com.leon.rfq.domains.RequestDetailImpl;

public interface RequestDao
{
	boolean delete(String requestId);

	boolean insert(String bookCode,	int clientId, String savedByUser);
	 
	boolean update(String requestId, String bookCode, int clientId,	boolean isValid, String requesedByUser);

	List<RequestDetailImpl> getAll();
	 
	RequestDetailImpl get(String requestId);
	
	boolean requestExistsWithRequestId(String requestId);
}
