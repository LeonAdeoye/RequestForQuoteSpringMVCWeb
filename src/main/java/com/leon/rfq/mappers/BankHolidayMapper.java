package com.leon.rfq.mappers;

import java.time.LocalDate;
import java.util.Set;

import com.leon.rfq.common.EnumTypes.LocationEnum;
import com.leon.rfq.domains.BankHolidayDetailImpl;

public interface BankHolidayMapper
{
	Set<BankHolidayDetailImpl> getAll(LocationEnum location);
	
	Set<BankHolidayDetailImpl> getAll();
	
	int delete(LocationEnum location);
	
	int delete(int identifier);
	
	int insert(BankHolidayDetailImpl bankHoliday);

	int updateValidity(BankHolidayDetailImpl bankHoliday);
	
	BankHolidayDetailImpl bankHolidayExists(LocationEnum location, LocalDate dateToCheck);
}
