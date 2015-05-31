package com.leon.rfq.repositories;

import java.time.LocalDate;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.leon.rfq.common.EnumTypes.LocationEnum;
import com.leon.rfq.domains.BankHolidayDetailImpl;
import com.leon.rfq.mappers.BankHolidayMapper;

@Repository
public class BankHolidayDaoImpl implements BankHolidayDao
{
	@Autowired
	private BankHolidayMapper bankHolidayMapper;
	
	@Override
	public boolean delete(LocationEnum location)
	{
		return this.bankHolidayMapper.delete(location) > 0;
	}

	@Override
	public boolean delete(LocationEnum location, LocalDate dateToBeDeleted)
	{
		return this.bankHolidayMapper.delete(location) == 1;
	}

	@Override
	public boolean insert(LocationEnum location, LocalDate dateToBeInserted, String savedByUser)
	{
		BankHolidayDetailImpl bankHoliday = new BankHolidayDetailImpl(location, dateToBeInserted, true, savedByUser);
		return this.bankHolidayMapper.insert(bankHoliday) == 1;
	}
	
	@Override
	public boolean update(LocationEnum location, LocalDate dateToBeInserted, boolean isValid, String savedByUser)
	{
		BankHolidayDetailImpl bankHoliday = new BankHolidayDetailImpl(location, dateToBeInserted, isValid, savedByUser);
		return this.bankHolidayMapper.update(bankHoliday) == 1;
	}

	@Override
	public Set<LocalDate> getAll(LocationEnum location)
	{
		return this.bankHolidayMapper.getAll(location);
	}

	@Override
	public boolean bankHolidayExists(LocationEnum location,	LocalDate dateToCheck)
	{
		return this.bankHolidayMapper.BankHolidayExists(location, dateToCheck);
	}
}
