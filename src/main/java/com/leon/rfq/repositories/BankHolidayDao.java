package com.leon.rfq.repositories;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

import com.leon.rfq.common.EnumTypes.LocationEnum;

public interface BankHolidayDao
{
	boolean delete(LocationEnum location);
	
	boolean delete(int identifier);

	boolean insert(LocationEnum location, LocalDate dateToBeInserted, String savedByUser);
	
	boolean updateValidity(LocationEnum location, LocalDate dateToBeUpdated, boolean isValid, String updatedByUser);
	 
	Set<LocalDate> getAll(LocationEnum location);
	
	Map<LocationEnum, Set<LocalDate>> getAll();

	boolean bankHolidayExists(LocationEnum location, LocalDate dateToCheck);
}
