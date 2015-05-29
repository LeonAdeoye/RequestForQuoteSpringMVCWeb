package com.leon.rfq.services;

import java.time.LocalDate;
import java.util.Set;

import com.leon.rfq.common.EnumTypes.LocationEnum;

public interface BankHolidayService
{
    int calculateBusinessDaysToExpiry(LocalDate startDate, LocalDate endDate, LocationEnum location);
    int calculateAllDaysToExpiry(LocalDate startDate, LocalDate endDate);
    int calculateAllDaysToExpiryFromToday(LocalDate endDate);
    int calculateBusinessDaysToExpiryFromToday(LocalDate endDate, LocationEnum location);
    boolean isBankHoliday(LocalDate dateToCheck, LocationEnum location);
    boolean isValidBusinessDay(LocalDate dateToValidate, LocationEnum location);
    boolean insert(LocationEnum location, LocalDate dateToBeInserted, String savedByUser);
    Set<LocalDate> getHolidaysInLocation(LocationEnum location);
}
