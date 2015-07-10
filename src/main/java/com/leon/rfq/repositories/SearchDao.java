package com.leon.rfq.repositories;

import java.util.Set;

import com.leon.rfq.domains.SearchCriterionImpl;

public interface SearchDao
{
	SearchCriterionImpl get(String searchKey);

	Set<SearchCriterionImpl> getAll();

	boolean delete(String searchId);

	boolean insert(String owner, String searchKey, String controlName,
			String controlValue, Boolean isPrivate, Boolean isFilter);

}
