package com.leon.rfq.services;

import java.util.Date;
import java.util.List;

import com.leon.rfq.domains.HolidayDetailImpl;
import com.leon.rfq.domains.RequestEnums.LocationEnum;

public interface BankHolidayMaintenanceService
{
    int CalculateBusinessDaysToExpiry(Date startDate, Date endDate, LocationEnum location);
    int CalculateAllDaysToExpiry(Date startDate, Date endDate);
    int CalculateAllDaysToExpiryFromToday(Date endDate);
    int CalculateBusinessDaysToExpiryFromToday(Date endDate, LocationEnum location);
    boolean IsHoliday(Date dateToValidate, LocationEnum location);
    boolean IsValidBusinessDay(Date dateToValidate, LocationEnum location);
    void AddHoliday(Date holidayDate, LocationEnum location);
    boolean SaveToDatabase(Date holidayDate, LocationEnum location);
    List<HolidayDetailImpl> GetHolidaysInLocation(LocationEnum location);
    void Initialize();
}
