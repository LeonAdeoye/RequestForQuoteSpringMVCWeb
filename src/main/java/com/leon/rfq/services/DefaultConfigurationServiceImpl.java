package com.leon.rfq.services;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.leon.rfq.common.EnumTypes.LocationEnum;
import com.leon.rfq.common.InstrumentConstants;

@Service
public final class DefaultConfigurationServiceImpl implements DefaultConfigurationService
{
	private BigDecimal defaultDayCountConvention = InstrumentConstants.DAY_COUNT_CONVENTION_250;
	private LocationEnum defaultLocation = LocationEnum.TOKYO;
	private BigDecimal defaultPriceVariance = BigDecimal.ONE;
	
	@Override
	public LocationEnum getDefaultLocation()
	{
		return this.defaultLocation;
	}

	@Override
	public void setDefaultLocation(LocationEnum defaultLocation)
	{
		this.defaultLocation = defaultLocation;
	}

	@Override
	public void setDefaultDayCountConvention(BigDecimal defaultDayConvention)
	{
		this.defaultDayCountConvention = defaultDayConvention;
	}

	@Override
	public BigDecimal getDefaultDayCountConvention()
	{
		return this.defaultDayCountConvention;
	}

	@Override
	public void setDefaultPriceVariance(BigDecimal defaultPriceVariance)
	{
		this.defaultPriceVariance = defaultPriceVariance;
	}

	@Override
	public BigDecimal getDefaultPriceVariance()
	{
		return this.defaultPriceVariance;
	}

}
