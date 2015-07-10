package com.leon.rfq.services;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.locks.ReentrantLock;
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
	private final Map<LocationEnum, Set<LocalDate>> bankHolidays = new ConcurrentSkipListMap<>();
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
		this.bankHolidays.clear();
		this.bankHolidays.putAll(this.dao.getAll());
	}

	private boolean isBankHolidayCached(LocationEnum location, LocalDate dateToBeChecked)
	{
		return this.bankHolidays.get(location).stream().anyMatch(theDate -> theDate.compareTo(dateToBeChecked) == 0);
	}
	
	private boolean isLocationCached(LocationEnum location)
	{
		return this.bankHolidays.containsKey(location);
	}

	@Override
	public long calculateBusinessDaysToExpiry(LocalDate startDate, LocalDate endDate, LocationEnum location)
	{
		if(logger.isDebugEnabled())
			logger.debug("Start date: " + startDate + ", endDate: " + endDate + ", location: " + location);
		
		
		long allDays = calculateAllDaysToExpiry(startDate, endDate);
		
		if(isLocationCached(location))
		{
			
			long count = Stream.iterate(startDate, nextDate -> startDate.plusDays(1)).limit(allDays)
					.filter(theDate -> !isBankHolidayCached(location, theDate)).count();
			
			if(logger.isDebugEnabled())
				logger.debug("Count: " + count);

			return count;
		}
		
		if(logger.isDebugEnabled())
			logger.debug("All days: " + allDays);
		
		return allDays;
	}

	@Override
	public long calculateAllDaysToExpiry(LocalDate startDate, LocalDate endDate)
	{
		if(startDate.compareTo(endDate)>= 0)
		{
			if(logger.isErrorEnabled())
				logger.error("startDate can not come before endDate");
			
			throw new IllegalArgumentException("endDate cannot come before startDate");
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
		return calculateBusinessDaysToExpiry(LocalDate.now(), endDate, location);
	}

	@Override
	public boolean isValidBusinessDay(LocalDate dateToValidate, LocationEnum location)
	{
		if((dateToValidate.getDayOfWeek() == DayOfWeek.SATURDAY)
				|| (dateToValidate.getDayOfWeek() == DayOfWeek.SUNDAY))
			return false;
		
		if(isLocationCached(location))
			return this.bankHolidays.get(location).stream().anyMatch(theDate -> theDate.compareTo(dateToValidate) != 0);
		
		return true;
	}
	
	@Override
	public boolean updateValidity(LocationEnum location, LocalDate dateToBeUpdated, boolean validity, String updatedByUser)
	{
		if((updatedByUser == null) || updatedByUser.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("updatedByUser argument is invalid");
			
			throw new IllegalArgumentException("updatedByUser argument is invalid");
		}
		
		if(logger.isDebugEnabled())
			logger.debug("Updating validity of" + location.toString() + " bank holiday date" + dateToBeUpdated);
		
		ReentrantLock lock = new ReentrantLock();
		
		try
		{
			lock.lock();
			
			if(isBankHolidayCached(location, dateToBeUpdated))
				return this.dao.updateValidity(location, dateToBeUpdated, validity, updatedByUser);
			
			return false;
		}
		finally
		{
			lock.unlock();
		}
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
		
		if(logger.isDebugEnabled())
			logger.debug("Inserting " + location.toString() + " bank holiday date" + dateToBeInserted);
		
		ReentrantLock lock = new ReentrantLock();
		
		try
		{
			lock.lock();
			
			if(!isBankHolidayCached(location, dateToBeInserted))
			{
				if(this.bankHolidays.keySet().stream().anyMatch(key -> key.equals(location)))
				{
					Set<LocalDate> set = this.bankHolidays.get(location);
					set.add(dateToBeInserted);
					this.bankHolidays.put(location, set);
				}
				else
				{
					Set<LocalDate> set = new ConcurrentSkipListSet<>();
					set.add(dateToBeInserted);
					this.bankHolidays.putIfAbsent(location, set);
				}
				
				return this.dao.insert(location, dateToBeInserted, savedByUser);
			}
			
			return false;
		}
		finally
		{
			lock.unlock();
		}
	}

	@Override
	public Set<LocalDate> getHolidaysInLocation(LocationEnum location)
	{
		if(isLocationCached(location))
			return this.bankHolidays.get(location);
		else
			return this.dao.getAll(location);
	}
}
