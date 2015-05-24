package com.leon.rfq.services;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

public interface VolatilityService
{
	BigDecimal getVolatility(String ric);
	BigDecimal getVolatility(String ric, Date referenceDate);
	void setVolatility(String ric, BigDecimal volatility);
	void setVolatility(String ric, Date referenceDate, BigDecimal volatility);
	void initialise();
	Map<String, Map<Date, BigDecimal>> getAll();
}
