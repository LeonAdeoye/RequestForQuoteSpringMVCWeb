package com.leon.rfq.services;

import java.math.BigDecimal;

public interface InterestRateService
{
	BigDecimal getInterestRate(String referenceProduct);
}
