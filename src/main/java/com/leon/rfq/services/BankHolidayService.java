package com.leon.rfq.services;

import java.time.LocalDate;
import java.util.Set;

import com.leon.rfq.common.EnumTypes.LocationEnum;
import com.leon.rfq.domains.BankHolidayDetailImpl;
import com.leon.rfq.repositories.BankHolidayDao;

public interface BankHolidayService
{
    long calculateBusinessDaysToExpiry(LocalDate startDate, LocalDate endDate, LocationEnum location);
    
    long calculateAllDaysToExpiry(LocalDate startDate, LocalDate endDate);
    
    long calculateAllDaysToExpiryFromToday(LocalDate endDate);
    
    long calculateBusinessDaysToExpiryFromToday(LocalDate endDate, LocationEnum location);
    
    boolean isValidBusinessDay(LocalDate dateToValidate, LocationEnum location);
    
    int insert(LocationEnum location, LocalDate dateToBeInserted, String savedByUser);
    
    boolean updateValidity(LocationEnum location, LocalDate dateToBeUpdated, boolean validity, String updatedByUser);
    
    Set<BankHolidayDetailImpl> getHolidaysInLocation(LocationEnum location);
    
    void setBankHolidayDao(BankHolidayDao bankHolidayDao);
    
	void initialise();
	
	void getAll();

	Set<BankHolidayDetailImpl> getAllFromCacheOnly();
}
