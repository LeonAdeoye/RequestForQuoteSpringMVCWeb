package com.leon.rfq.services;

import java.math.BigDecimal;

import com.leon.rfq.domains.EnumTypes.LocationEnum;

public interface DefaultConfigurationService
{
    final static BigDecimal DAY_COUNT_CONVENTION_255 = new BigDecimal("255");
    final static BigDecimal DAY_COUNT_CONVENTION_250 = new BigDecimal("250");
    final static BigDecimal DAY_COUNT_CONVENTION_265 = new BigDecimal("265");
	
	void setDefaultLocation(LocationEnum defaultLocation);
	LocationEnum getDefaultLocation();
	
	void setDefaultDayCountConvention(BigDecimal defaultDayConvention);
	BigDecimal getDefaultDayCountConvention();
}
