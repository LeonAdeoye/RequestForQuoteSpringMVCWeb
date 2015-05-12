package com.leon.rfq.services;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class VolatilityServiceImpl implements VolatilityService
{
	Map<String, Map<Date, BigDecimal>> volatilityCache = new HashMap<>();

	@Override
	public BigDecimal getVolatility(String ric)
	{
		// TODO Auto-generated method stub
		return new BigDecimal("0.2");
	}

	@Override
	public BigDecimal getVolatility(String ric, Date referenceDate)
	{
		// TODO
		if(this.volatilityCache.containsKey(ric))
		{
			Map<Date, BigDecimal> vols = this.volatilityCache.get(ric);
			if(vols.containsKey(referenceDate))
			{
				return vols.get(referenceDate);
			}
		}
		return new BigDecimal("0.2");
	}

	@Override
	public void setVolatility(String ric, BigDecimal volatility)
	{
		if(this.volatilityCache.containsKey(ric))
		{
			
		}
	}
	
	@Override
	public void setVolatility(String ric, Date referenceDate, BigDecimal volatility)
	{
		if(this.volatilityCache.containsKey(ric))
		{
			Map<Date, BigDecimal> vols = this.volatilityCache.get(ric);
			if(vols.containsKey(referenceDate))
			{
				// TODO
			}
		}
		else
		{
			
		}
	}

}
