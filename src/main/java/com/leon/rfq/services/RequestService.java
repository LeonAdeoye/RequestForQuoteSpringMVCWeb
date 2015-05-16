package com.leon.rfq.services;

import java.util.List;

import com.leon.rfq.domains.RequestDetailImpl;
import com.leon.rfq.option.OptionRequestFactory;
import com.leon.rfq.repositories.RequestDao;

public interface RequestService
{
	RequestDetailImpl get(int requestId);

	List<RequestDetailImpl> getAll();
	
	boolean insert(String requestSnippet, int clientId, String bookName, String savedByUser);
	
	boolean delete(int requestId);

	void setUserDao(RequestDao requestDao);
	
	boolean isRequestCached(int requestId);
	
	boolean requestExistsWithRequestId(int requestId);
	
	void setRequestDao(RequestDao requestDao);
	
	void setOptionRequestFactory(OptionRequestFactory optionRequestFactory);
}
