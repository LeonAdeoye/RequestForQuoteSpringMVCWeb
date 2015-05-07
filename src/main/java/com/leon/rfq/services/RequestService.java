package com.leon.rfq.services;

import java.util.List;

import com.leon.rfq.dao.RequestDao;
import com.leon.rfq.domains.RequestDetailImpl;

public interface RequestService
{
	RequestDetailImpl get(String requestId);

	List<RequestDetailImpl> getAll();
	
	boolean insert(String requestSnippet, int clientId, String bookName, String savedByUser);
	
	boolean delete(String requestId);
	
	boolean updateValidity(String userId, boolean isValid, String updatedByUser);

	void setUserDao(RequestDao requestDao);
	
	boolean isRequestCached(String requestId);
	
	boolean requestExistsWithRequestId(String requestId);
}
