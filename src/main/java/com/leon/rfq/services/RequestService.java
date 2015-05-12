package com.leon.rfq.services;

import java.util.List;

import com.leon.rfq.dao.RequestDao;
import com.leon.rfq.domains.RequestDetailImpl;

public interface RequestService
{
	RequestDetailImpl get(int requestId);

	List<RequestDetailImpl> getAll();
	
	boolean insert(String requestSnippet, int clientId, String bookName, String savedByUser);
	
	boolean delete(int requestId);

	void setUserDao(RequestDao requestDao);
	
	boolean isRequestCached(int requestId);
	
	boolean requestExistsWithRequestId(int requestId);
}
