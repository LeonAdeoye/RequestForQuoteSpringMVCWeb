package com.leon.rfq.services;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public final class InterestRateServiceImpl implements InterestRateService
{
	private final Map<String, BigDecimal> rates = new HashMap<>();
	
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
}
