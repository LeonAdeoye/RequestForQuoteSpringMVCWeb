package com.leon.rfq.services;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class VolatilityServiceImpl implements VolatilityService
{
	private static Logger logger = LoggerFactory.getLogger(VolatilityServiceImpl.class);
	Map<String, Map<Date, BigDecimal>> volatilityCache = new HashMap<>();
	
	@Override
	@PostConstruct
	public void initialise()
	{
		if(logger.isDebugEnabled())
			logger.debug("Initializing underlying service by getting all existing underlyings...");
		
		this.getAll();
	}

	@Override
	public Map<String, Map<Date, BigDecimal>> getAll()
	{
		return new HashMap<String, Map<Date, BigDecimal>>();
	}

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
