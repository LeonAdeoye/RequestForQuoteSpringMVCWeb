package com.leon.rfq.services;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.leon.rfq.domains.HolidayDetailImpl;
import com.leon.rfq.domains.EnumTypes.LocationEnum;

@Service
public class BankHolidayMaintenanceServiceImpl implements BankHolidayMaintenanceService
{

	@Override
	public int CalculateBusinessDaysToExpiry(Date startDate, Date endDate, LocationEnum location)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int CalculateAllDaysToExpiry(Date startDate, Date endDate)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int CalculateAllDaysToExpiryFromToday(Date endDate)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int CalculateBusinessDaysToExpiryFromToday(Date endDate,
			LocationEnum location)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean IsHoliday(Date dateToValidate, LocationEnum location)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean IsValidBusinessDay(Date dateToValidate, LocationEnum location)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void AddHoliday(Date holidayDate, LocationEnum location)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean SaveToDatabase(Date holidayDate, LocationEnum location)
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
