package com.leon.rfq.repositories;

import java.util.Map;
import java.util.Set;

import com.leon.rfq.domains.SearchCriterionImpl;

public class SearchDaoImpl implements SearchDao
{
	@Override
	public Set<SearchCriterionImpl> get(String owner, String searchKey)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Set<SearchCriterionImpl>> get(String owner)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Map<String, Set<SearchCriterionImpl>>> get()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean delete(String owner, String searchKey)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(String owner)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean insert(String owner, String searchKey, String controlName,
			String controlValue, Boolean isPrivate, Boolean isFilter)
	{
		// TODO Auto-generated method stub
		return false;
	}

}
