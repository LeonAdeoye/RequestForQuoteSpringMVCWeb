package com.leon.rfq.repositories;

import java.util.Set;

import com.leon.rfq.domains.SearchCriterionImpl;

public class SearchDaoImpl implements SearchDao
{
	@Override
	public SearchCriterionImpl get(String searchKey)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<SearchCriterionImpl> getAll()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean delete(String searchId)
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
