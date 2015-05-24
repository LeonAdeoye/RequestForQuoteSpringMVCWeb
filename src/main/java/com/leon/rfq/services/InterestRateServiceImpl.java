package com.leon.rfq.services;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public final class InterestRateServiceImpl implements InterestRateService
{
	private static Logger logger = LoggerFactory.getLogger(InterestRateServiceImpl.class);
	private final Map<String, BigDecimal> rates = new HashMap<>();
	
	public InterestRateServiceImpl() {}
	
	@Override
	@PostConstruct
	public void initialise()
	{
		if(logger.isDebugEnabled())
			logger.debug("Initializing interest rate service by getting all existing interest rates...");
		
		this.getAll();
	}
	
	private BigDecimal defaultRate = new BigDecimal("0.05");
	
	public BigDecimal getDefaultRate()
	{
		return this.defaultRate;
	}

	public void setDefaultRate(BigDecimal defaultRate)
	{
		this.defaultRate = defaultRate;
	}

	@Override
	public BigDecimal getInterestRate(String referenceProduct)
	{
		return this.rates.containsKey(referenceProduct) ? this.rates.get(referenceProduct) : this.defaultRate;
	}

	@Override
	public Map<String, BigDecimal> getAll()
	{
		return new HashMap<String, BigDecimal>();
	}
}
