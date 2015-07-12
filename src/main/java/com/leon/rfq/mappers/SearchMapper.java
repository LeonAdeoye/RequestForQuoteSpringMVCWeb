package com.leon.rfq.mappers;

import java.util.Set;

import com.leon.rfq.domains.SearchCriterionImpl;

public interface SearchMapper
{
	Set<SearchCriterionImpl> getAll();
	
	Set<SearchCriterionImpl> getForOwner(String owner);
	
	Set<SearchCriterionImpl> get(SearchCriterionImpl searchCriterionImpl);
	
	int deleteForOwner(String owner);
	
	int delete(SearchCriterionImpl searchCriterionImpl);
	
	int insert(SearchCriterionImpl searchCriterionImpl);
}
