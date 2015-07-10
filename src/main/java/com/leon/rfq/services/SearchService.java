package com.leon.rfq.services;

import java.util.Set;

import com.leon.rfq.domains.SearchCriterionImpl;
import com.leon.rfq.repositories.SearchDao;

public interface SearchService
{
	// For unit testing mocking framework.
	void setSearchDao(SearchDao searchDao);

	void initialise();

	boolean isSearchCached(String searchId);

	SearchCriterionImpl get(String owner, String searchKey);

	Set<SearchCriterionImpl> getAll();

	boolean insert(String owner, String searchKey, String controlName,
			String controlValue, Boolean isPrivate, Boolean isFilter);

	boolean delete(String owner, String searchKey);
}