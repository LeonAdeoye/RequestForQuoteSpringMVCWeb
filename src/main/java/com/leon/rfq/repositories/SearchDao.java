package com.leon.rfq.repositories;

import java.util.Map;
import java.util.Set;

import com.leon.rfq.domains.SearchCriterionImpl;

public interface SearchDao
{
	Set<SearchCriterionImpl> get(String owner, String searchKey);

	Map<String, Set<SearchCriterionImpl>> get(String owner);
	
	Map<String, Map<String, Set<SearchCriterionImpl>>> get();

	boolean delete(String owner, String searchKey);
	
	boolean delete(String owner);

	boolean insert(String owner, String searchKey, String controlName,
			String controlValue, Boolean isPrivate, Boolean isFilter);

}
