package com.leon.rfq.repositories;

import java.time.LocalDate;
import java.util.Set;

import com.leon.rfq.common.EnumTypes.LocationEnum;

public interface BankHolidayDao
{
	boolean delete(LocationEnum location);
	
	boolean delete(int identifier);

	boolean insert(LocationEnum location, LocalDate dateToBeInserted, String savedByUser);
	
	boolean update(LocationEnum location, LocalDate dateToBeInserted, boolean isValid, String updatedByUser);
	 
	Set<LocalDate> getAll(LocationEnum location);

	boolean bankHolidayExists(LocationEnum location, LocalDate dateToCheck);
}
