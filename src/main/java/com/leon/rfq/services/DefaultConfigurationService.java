package com.leon.rfq.services;

import java.math.BigDecimal;

import com.leon.rfq.common.EnumTypes.LocationEnum;

public interface DefaultConfigurationService
{
	void setDefaultLocation(LocationEnum defaultLocation);
	LocationEnum getDefaultLocation();
	
	void setDefaultDayCountConvention(BigDecimal defaultDayConvention);
	BigDecimal getDefaultDayCountConvention();
	
	void setDefaultPriceVariance(BigDecimal priceVariance);
	BigDecimal getDefaultPriceVariance();
}
