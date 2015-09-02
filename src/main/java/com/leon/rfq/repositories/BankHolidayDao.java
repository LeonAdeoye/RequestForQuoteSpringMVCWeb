package com.leon.rfq.repositories;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

import com.leon.rfq.common.EnumTypes.LocationEnum;
import com.leon.rfq.domains.BankHolidayDetailImpl;

public interface BankHolidayDao
{
	boolean delete(LocationEnum location);
	
	boolean delete(int identifier);

	int insert(LocationEnum location, LocalDate dateToBeInserted, String savedByUser);
	
	boolean updateValidity(LocationEnum location, LocalDate dateToBeUpdated, boolean isValid, String updatedByUser);
	 
	Set<BankHolidayDetailImpl> getAll(LocationEnum location);
	
	Map<LocationEnum, Set<BankHolidayDetailImpl>> getAll();

	boolean bankHolidayExists(LocationEnum location, LocalDate dateToCheck);
}
