package com.leon.rfq.underlying;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class UnderlyingManagerDaoImpl implements UnderlyingManagerDao
{
	public UnderlyingManagerDaoImpl() {}

	@Override
	public UnderlyingDetailImpl insert(String ric, String description, boolean isValid, String savedBy)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UnderlyingDetailImpl update(String ric, String description, boolean isValid, String savedBy)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateValidity(String ric, boolean isValid, String updatedBy)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<UnderlyingDetailImpl> getAll()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UnderlyingDetailImpl get(String ric)
	{
		// TODO Auto-generated method stub
		return null;
	}


}
