package com.leon.rfq.services;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.leon.rfq.domains.PriceDetailImpl;
import com.leon.rfq.domains.RequestDetailImpl;
import com.leon.rfq.products.OptionRequestFactory;
import com.leon.rfq.repositories.RequestDao;

public interface RequestService
{
	RequestDetailImpl get(int requestId);
	
	boolean insert(String requestSnippet, int clientId, String bookName, String savedByUser);
	
	boolean delete(int requestId);

	void setUserDao(RequestDao requestDao);
	
	boolean isRequestCached(int requestId);
	
	boolean requestExistsWithRequestId(int requestId);
	
	void setRequestDao(RequestDao requestDao); // For unit test mocking
	
	void setPriceService(PriceService priceService); // For unit test mocking.
	
	void setCalculationService(CalculationService calculationService); // For unit test mocking.
	
	void setOptionRequestFactory(OptionRequestFactory factory); // For unit test mocking
	
	List<RequestDetailImpl> getAll();

	List<RequestDetailImpl> getAllFromCacheOnly();

	List<RequestDetailImpl> getAllFromTodayOnly();

	Map<String, PriceDetailImpl> getPriceUpdates();

	Map<String, PriceDetailImpl> getPriceUpdates(Set<String> underlyings);
}
