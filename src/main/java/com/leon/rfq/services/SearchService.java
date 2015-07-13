package com.leon.rfq.services;

import java.util.Map;
import java.util.Set;

import com.leon.rfq.domains.SearchCriterionImpl;
import com.leon.rfq.repositories.SearchDao;

public interface SearchService
{
	// For unit testing mocking framework.
	void setSearchDao(SearchDao searchDao);

	void initialise();

	Set<SearchCriterionImpl> get(String owner, String searchKey);

	Map<String, Set<SearchCriterionImpl>> get(String owner);
	
	Map<String, Map<String, Set<SearchCriterionImpl>>> get();

	boolean insert(String owner, String searchKey, String name, String value, Boolean isPrivate);

	boolean delete(String owner, String searchKey);
	
	boolean delete(String owner);
}