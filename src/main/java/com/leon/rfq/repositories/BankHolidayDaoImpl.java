package com.leon.rfq.repositories;

import java.time.LocalDate;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.leon.rfq.common.EnumTypes.LocationEnum;

@Repository
public class BankHolidayDaoImpl implements BankHolidayDao
{
	@Override
	public boolean delete(LocationEnum location)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(LocationEnum location, LocalDate dateToBeDeleted)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean insert(LocationEnum location, LocalDate dateToBeInserted, String savedByUser)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean update(LocationEnum location, LocalDate dateToBeUpdated,
			boolean isValid, String updatedByUser)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Set<LocalDate> getAll()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<LocalDate> getAll(LocationEnum location)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean BankHolidayExists(LocationEnum location,	LocalDate dateToCheck)
	{
		// TODO Auto-generated method stub
		return false;
	}
}
