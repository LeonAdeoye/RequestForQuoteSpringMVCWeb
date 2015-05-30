package com.leon.rfq.services;

import java.time.LocalDate;
import java.util.Set;

import com.leon.rfq.common.EnumTypes.LocationEnum;

public interface BankHolidayService
{
    long calculateBusinessDaysToExpiry(LocalDate startDate, LocalDate endDate, LocationEnum location);
    long calculateAllDaysToExpiry(LocalDate startDate, LocalDate endDate);
    long calculateAllDaysToExpiryFromToday(LocalDate endDate);
    long calculateBusinessDaysToExpiryFromToday(LocalDate endDate, LocationEnum location);
    boolean isBankHoliday(LocalDate dateToCheck, LocationEnum location);
    boolean isValidBusinessDay(LocalDate dateToValidate, LocationEnum location);
    boolean insert(LocationEnum location, LocalDate dateToBeInserted, String savedByUser);
    Set<LocalDate> getHolidaysInLocation(LocationEnum location);
}
