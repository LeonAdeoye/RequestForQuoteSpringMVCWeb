package com.leon.rfq.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.leon.rfq.domains.EnumTypes.LocationEnum;
import com.leon.rfq.domains.HolidayDetailImpl;

@Service
public final class BankHolidayMaintenanceServiceImpl implements BankHolidayMaintenanceService
{

	@Override
	public int CalculateBusinessDaysToExpiry(LocalDate startDate, LocalDate endDate, LocationEnum location)
	{
		// TODO Auto-generated method stub
		return 250;
	}

	@Override
	public int CalculateAllDaysToExpiry(LocalDate startDate, LocalDate endDate)
	{
		// TODO Auto-generated method stub
		return 250;
	}

	@Override
	public int CalculateAllDaysToExpiryFromToday(LocalDate endDate)
	{
		// TODO Auto-generated method stub
		return 250;
	}

	@Override
	public int CalculateBusinessDaysToExpiryFromToday(LocalDate endDate, LocationEnum location)
	{
		// TODO Auto-generated method stub
		return 250;
	}

	@Override
	public boolean IsHoliday(LocalDate dateToValidate, LocationEnum location)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean IsValidBusinessDay(LocalDate dateToValidate, LocationEnum location)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void AddHoliday(LocalDate holidayDate, LocationEnum location)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean SaveToDatabase(LocalDate holidayDate, LocationEnum location)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<HolidayDetailImpl> GetHolidaysInLocation(LocationEnum location)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void Initialize()
	{
		// TODO Auto-generated method stub
		
	}

}
