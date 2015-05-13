package com.leon.rfq.services;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.leon.rfq.domains.EnumTypes.LocationEnum;

@Service
public class DefaultConfigurationServiceImpl implements	DefaultConfigurationService
{
	private BigDecimal defaultDayCountConvention = DefaultConfigurationService.DAY_COUNT_CONVENTION_250;
	private LocationEnum defaultLocation = LocationEnum.TOKYO;
	
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

}
