package com.leon.rfq.services;

import java.time.LocalDate;
import java.util.List;

import com.leon.rfq.common.EnumTypes.LocationEnum;
import com.leon.rfq.domains.HolidayDetailImpl;

public interface BankHolidayMaintenanceService
{
    int CalculateBusinessDaysToExpiry(LocalDate startDate, LocalDate endDate, LocationEnum location);
    int CalculateAllDaysToExpiry(LocalDate startDate, LocalDate endDate);
    int CalculateAllDaysToExpiryFromToday(LocalDate endDate);
    int CalculateBusinessDaysToExpiryFromToday(LocalDate endDate, LocationEnum location);
    boolean IsHoliday(LocalDate dateToValidate, LocationEnum location);
    boolean IsValidBusinessDay(LocalDate dateToValidate, LocationEnum location);
    void AddHoliday(LocalDate holidayDate, LocationEnum location);
    boolean SaveToDatabase(LocalDate holidayDate, LocationEnum location);
    List<HolidayDetailImpl> GetHolidaysInLocation(LocationEnum location);
    void Initialize();
}
