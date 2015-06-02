package com.leon.rfq.mappers;

import java.time.LocalDate;
import java.util.List;

import com.leon.rfq.common.EnumTypes.LocationEnum;
import com.leon.rfq.domains.BankHolidayDetailImpl;

public interface BankHolidayMapper
{
	List<BankHolidayDetailImpl> getAll(LocationEnum location);
	
	int delete(LocationEnum location);
	
	int delete(int identifier);
	
	int insert(BankHolidayDetailImpl bankHoliday);

	int update(BankHolidayDetailImpl bankHoliday);
	
	boolean BankHolidayExists(LocationEnum location, LocalDate dateToCheck);
}
