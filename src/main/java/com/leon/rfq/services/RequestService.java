package com.leon.rfq.services;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

import com.leon.rfq.domains.PriceDetailImpl;
import com.leon.rfq.domains.RequestDetailImpl;
import com.leon.rfq.products.OptionRequestFactory;
import com.leon.rfq.repositories.RequestDao;

public interface RequestService
{
	RequestDetailImpl get(int requestId);
	
	RequestDetailImpl insert(String requestSnippet, int clientId, String bookName, String savedByUser);
	
	boolean update(RequestDetailImpl requestToUpdate);
	
	boolean updateStatus(RequestDetailImpl requestToUpdate);
	
	boolean delete(int requestId);

	void setUserDao(RequestDao requestDao);
	
	boolean isRequestCached(int requestId);
	
	boolean requestExistsWithRequestId(int requestId);
	
	void setRequestDao(RequestDao requestDao); // For unit test mocking
	
	void setPriceService(PriceService priceService); // For unit test mocking.
	
	void setCalculationService(CalculationService calculationService); // For unit test mocking.
	
	void setOptionRequestFactory(OptionRequestFactory factory); // For unit test mocking
	
	Set<RequestDetailImpl> getAll();

	Set<RequestDetailImpl> getAllFromCacheOnly();

	Set<RequestDetailImpl> getAllFromTodayOnly();

	Map<String, PriceDetailImpl> getPriceUpdates();

	Map<String, PriceDetailImpl> getPriceUpdates(Set<String> underlyings);

	Set<RequestDetailImpl> getStatusUpdates();
	
	Set<RequestDetailImpl> getStatusUpdates(Set<RequestDetailImpl> setOfRequests);
	
	Map<Integer, Map<String, BigDecimal>> getCalculationUpdates();
	
	Map<Integer, Map<String, BigDecimal>> getCalculationUpdates(Set<RequestDetailImpl> setOfRequests);
}
