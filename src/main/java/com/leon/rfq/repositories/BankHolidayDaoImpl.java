package com.leon.rfq.repositories;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.leon.rfq.common.EnumTypes.LocationEnum;
import com.leon.rfq.domains.BankHolidayDetailImpl;
import com.leon.rfq.mappers.BankHolidayMapper;

@Repository
public class BankHolidayDaoImpl implements BankHolidayDao
{
	private static final Logger logger = LoggerFactory.getLogger(BankHolidayDaoImpl.class);
	
	@Autowired(required=true)
	private BankHolidayMapper bankHolidayMapper;
	
	@Override
	public boolean delete(LocationEnum location)
	{
		if(logger.isDebugEnabled())
			logger.debug("Delete the bank holiday with date: " + location);
		
		try
		{
			return this.bankHolidayMapper.deleteByLocation(location) > 0;
		}
		catch(Exception e)
		{
			if(logger.isErrorEnabled())
				logger.error("Failed to delete the bank holidays with location: " + location + " because of exception: " + e);
			
			return false;
		}
	}

	@Override
	public boolean delete(int identifier)
	{
		if(logger.isDebugEnabled())
			logger.debug("Delete the bank holiday with ID: " + identifier);
		
		try
		{
			return this.bankHolidayMapper.deleteById(identifier) == 1;
		}
		catch(Exception e)
		{
			if(logger.isErrorEnabled())
				logger.error("Failed to delete the bank holidays with identifier: " + identifier + " because of exception: " + e);
			
			return false;
		}
		
	}

	@Override
	public boolean insert(LocationEnum location, LocalDate dateToBeInserted, String savedByUser)
	{
		if(logger.isDebugEnabled())
			logger.debug("Inserting a new bank holiday with date: " + dateToBeInserted);
		
		try
		{
			BankHolidayDetailImpl bankHoliday = new BankHolidayDetailImpl(location, dateToBeInserted, true, savedByUser);
			return this.bankHolidayMapper.insert(bankHoliday) == 1;
		}
		catch(Exception e)
		{
			if(logger.isErrorEnabled())
				logger.error("Failed to insert the bank holiday with date " + dateToBeInserted + " because of exception: " + e);
			
			return false;
		}
	}
	
	@Override
	public boolean updateValidity(LocationEnum location, LocalDate dateToBeUpdated, boolean isValid, String savedByUser)
	{
		if(logger.isDebugEnabled())
			logger.debug("Updating the validity of a bank holiday with location: " + location.toString() + " and date: " + dateToBeUpdated);
		
		try
		{
			BankHolidayDetailImpl bankHoliday = new BankHolidayDetailImpl(location, dateToBeUpdated, isValid, savedByUser);
			return this.bankHolidayMapper.updateValidity(bankHoliday) == 1;

		}
		catch(Exception e)
		{
			if(logger.isErrorEnabled())
				logger.error("Failed to update the validity of bank holiday with date " + dateToBeUpdated + " because of exception: " + e);
			
			return false;
		}
	}

	@Override
	public Set<LocalDate> getAll(LocationEnum location)
	{
		if(logger.isDebugEnabled())
			logger.debug("Request to get all bank holidays from location: " + location);
		
		return this.bankHolidayMapper.getAllInLocation(location).stream()
				.map(BankHolidayDetailImpl::getBankHolidayDate).collect(Collectors.toSet());
	}
	
	@Override
	public Map<LocationEnum, Set<LocalDate>> getAll()
	{
		if(logger.isDebugEnabled())
			logger.debug("Request to get all bank holidays");
		
		
		Map<LocationEnum, Set<LocalDate>> result = new HashMap<>();
		
		for(Map.Entry<LocationEnum, List<BankHolidayDetailImpl>> entry : this.bankHolidayMapper.getAll().stream().collect(Collectors.groupingBy(BankHolidayDetailImpl::getLocation)).entrySet())
			result.put(entry.getKey(), entry.getValue().stream().map(BankHolidayDetailImpl::getBankHolidayDate).collect(Collectors.toSet()));
		
		return result;
	}

	@Override
	public boolean bankHolidayExists(LocationEnum location,	LocalDate dateToCheck)
	{
		BankHolidayDetailImpl bankHoliday = new BankHolidayDetailImpl(location, dateToCheck);
		return this.bankHolidayMapper.bankHolidayExists(bankHoliday) != null;
	}
}
