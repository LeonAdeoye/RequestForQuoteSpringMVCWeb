package com.leon.rfq.repositories;

import java.util.Set;

import com.leon.rfq.domains.RequestDetailImpl;
import com.leon.rfq.domains.SearchCriterionImpl;

public interface RequestDao
{
	boolean delete(int requestId);

	boolean insert(RequestDetailImpl newRequest);
	 
	boolean updateStatus(RequestDetailImpl requestToUpdate);
	
	boolean update(RequestDetailImpl requestToUpdate);

	Set<RequestDetailImpl> getAll();
	 
	RequestDetailImpl get(int requestId);
	
	boolean requestExistsWithRequestId(int requestId);

	Set<RequestDetailImpl> search(Set<SearchCriterionImpl> criteriaRequests);
}
