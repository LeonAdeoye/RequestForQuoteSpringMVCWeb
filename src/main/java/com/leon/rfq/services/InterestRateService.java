package com.leon.rfq.services;

import java.math.BigDecimal;
import java.util.Map;

public interface InterestRateService
{
	BigDecimal getInterestRate(String referenceProduct);

	void initialise();
	
	Map<String, BigDecimal> getAll();
}
