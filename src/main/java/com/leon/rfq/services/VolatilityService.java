package com.leon.rfq.services;

import java.math.BigDecimal;
import java.util.Date;

public interface VolatilityService
{
	BigDecimal getVolatility(String ric);
	BigDecimal getVolatility(String ric, Date referenceDate);
	void setVolatility(String ric, BigDecimal volatility);
	void setVolatility(String ric, Date referenceDate, BigDecimal volatility);
}
