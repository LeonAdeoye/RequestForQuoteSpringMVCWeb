package com.leon.rfq.repositories;

import java.time.LocalDate;
import java.util.Set;

import com.leon.rfq.common.EnumTypes.LocationEnum;

public interface BankHolidayDao
{
	boolean delete(LocationEnum location);
	
	boolean delete(LocationEnum location, LocalDate dateToBeDeleted);

	boolean insert(LocationEnum location, LocalDate dateToBeInserted, String savedByUser);
	
	boolean update(LocationEnum location, LocalDate dateToBeUpdated, boolean isValid, String updatedByUser);

	Set<LocalDate> getAll();
	 
	Set<LocalDate> getAll(LocationEnum location);

	boolean BankHolidayExists(LocationEnum location, LocalDate dateToCheck);
}
