package com.leon.rfq.repositories;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.leon.rfq.common.EnumTypes.LocationEnum;
import com.leon.rfq.domains.BankHolidayDetailImpl;
import com.leon.rfq.mappers.BankHolidayMapper;

@Repository
public class BankHolidayDaoImpl implements BankHolidayDao
{
	@Autowired(required=true)
	private BankHolidayMapper bankHolidayMapper;
	
	@Override
	public boolean delete(LocationEnum location)
	{
		return this.bankHolidayMapper.deleteByLocation(location) > 0;
	}

	@Override
	public boolean delete(int identifier)
	{
		return this.bankHolidayMapper.deleteById(identifier) == 1;
	}

	@Override
	public boolean insert(LocationEnum location, LocalDate dateToBeInserted, String savedByUser)
	{
		BankHolidayDetailImpl bankHoliday = new BankHolidayDetailImpl(location, dateToBeInserted, true, savedByUser);
		return this.bankHolidayMapper.insert(bankHoliday) == 1;
	}
	
	@Override
	public boolean updateValidity(LocationEnum location, LocalDate dateToBeInserted, boolean isValid, String savedByUser)
	{
		BankHolidayDetailImpl bankHoliday = new BankHolidayDetailImpl(location, dateToBeInserted, isValid, savedByUser);
		return this.bankHolidayMapper.updateValidity(bankHoliday) == 1;
	}

	@Override
	public Set<LocalDate> getAll(LocationEnum location)
	{
		return this.bankHolidayMapper.getAllInLocation(location).stream()
				.map(BankHolidayDetailImpl::getBankHolidayDate).collect(Collectors.toSet());
	}
	
	@Override
	public Map<LocationEnum, Set<LocalDate>> getAll()
	{
		Map<LocationEnum, Set<LocalDate>> result = new HashMap<>();
		
		for(Map.Entry<LocationEnum, List<BankHolidayDetailImpl>> entry : this.bankHolidayMapper.getAll().stream().collect(Collectors.groupingBy(BankHolidayDetailImpl::getLocation)).entrySet())
			result.put(entry.getKey(), entry.getValue().stream().map(BankHolidayDetailImpl::getBankHolidayDate).collect(Collectors.toSet()));
		
		return result;
	}

	@Override
	public boolean bankHolidayExists(LocationEnum location,	LocalDate dateToCheck)
	{
		return this.bankHolidayMapper.bankHolidayExists(location, dateToCheck) != null;
	}
}
