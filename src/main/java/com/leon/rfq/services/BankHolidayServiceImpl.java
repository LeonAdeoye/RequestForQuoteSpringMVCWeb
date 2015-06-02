package com.leon.rfq.services;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.leon.rfq.common.EnumTypes.LocationEnum;
import com.leon.rfq.repositories.BankHolidayDao;

@Service
public final class BankHolidayServiceImpl implements BankHolidayService
{
	private static final Logger logger = LoggerFactory.getLogger(BankHolidayServiceImpl.class);
	private final Map<LocationEnum, Set<LocalDate>> bankHolidays = new HashMap<>();
	private ApplicationEventPublisher applicationEventPublisher;
	
	@Autowired(required=true)
	private BankHolidayDao dao;
	
	@Override
	public void setBankHolidayDao(BankHolidayDao bankHolidayDao)
	{
		this.dao = bankHolidayDao;
	}
	
	@Override
	@PostConstruct
	public void initialise()
	{
		if(logger.isDebugEnabled())
			logger.debug("Initializing bank holiday service by getting all existing bank holidays...");
		
		this.getAll();
	}
	
	@Override
	public void getAll()
	{
		// populate cache
	}

	public boolean isBankHolidayCached(LocationEnum location, LocalDate dateToBeChecked)
	{
		return this.bankHolidays.get(location).stream().anyMatch(theDate -> theDate.compareTo(dateToBeChecked) == 0);
	}

	@Override
	public long calculateBusinessDaysToExpiry(LocalDate startDate, LocalDate endDate, LocationEnum location)
	{
		long allDays = calculateAllDaysToExpiry(startDate, endDate);
		
		if(this.bankHolidays.containsKey(location))
		{
			return Stream.iterate(startDate, nextDate -> startDate.plusDays(1))
					.limit(allDays)
					.filter(theDate -> !isBankHoliday(theDate, location)).count();
		}
		
		return allDays;
	}

	@Override
	public long calculateAllDaysToExpiry(LocalDate startDate, LocalDate endDate)
	{
		if(startDate.compareTo(endDate)>= 0)
		{
			if(logger.isErrorEnabled())
				logger.error("startDate can not come before endDate");
			
			throw new IllegalArgumentException("startDate can not come before endDate");
		}
		
		 return startDate.until(endDate, ChronoUnit.DAYS);
	}

	@Override
	public long calculateAllDaysToExpiryFromToday(LocalDate endDate)
	{
		if(LocalDate.now().compareTo(endDate)>= 0)
		{
			if(logger.isErrorEnabled())
				logger.error("endDate can not come before today");
			
			throw new IllegalArgumentException("endDate can not come before today");
		}
		
		return calculateAllDaysToExpiry(LocalDate.now(), endDate);
	}

	@Override
	public long calculateBusinessDaysToExpiryFromToday(LocalDate endDate, LocationEnum location)
	{
		if(LocalDate.now().compareTo(endDate)>= 0)
		{
			if(logger.isErrorEnabled())
				logger.error("endDate can not come before today");
			
			throw new IllegalArgumentException("endDate can not come before today");
		}
		
		return calculateBusinessDaysToExpiry(LocalDate.now(), endDate, location);
	}

	@Override
	public boolean isBankHoliday(LocalDate dateToCheck, LocationEnum location)
	{
		return this.bankHolidays.get(location).stream().anyMatch(theDate -> theDate.compareTo(dateToCheck) == 0);
	}

	@Override
	public boolean isValidBusinessDay(LocalDate dateToValidate, LocationEnum location)
	{
		if((dateToValidate.getDayOfWeek() == DayOfWeek.SATURDAY)
				|| (dateToValidate.getDayOfWeek() == DayOfWeek.SUNDAY))
			return false;
		
		if(this.bankHolidays.containsKey(location))
			return this.bankHolidays.get(location).stream()
					.anyMatch(theDate -> theDate.compareTo(dateToValidate) != 0);
		
		return true;
	}

	@Override
	public boolean insert(LocationEnum location, LocalDate dateToBeInserted, String savedByUser)
	{
		if((savedByUser == null) || savedByUser.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("savedByUser argument is invalid");
			
			throw new IllegalArgumentException("savedByUser argument is invalid");
		}
		
		if(!isBankHolidayCached(location, dateToBeInserted))
			return this.dao.insert(location, dateToBeInserted, savedByUser);
		else
			return false;
	}

	@Override
	public Set<LocalDate> getHolidaysInLocation(LocationEnum location)
	{
		if(this.bankHolidays.containsKey(location))
			return this.bankHolidays.get(location);
		else
			return this.dao.getAll(location);
	}
}
