package com.leon.rfq.services;

import java.math.BigDecimal;

public interface PriceService
{
	BigDecimal getLastPrice(String ric);
	BigDecimal getAskPrice(String ric);
	BigDecimal getBidPrice(String ric);
	BigDecimal getMidPrice(String ric);
	void initialize();
}
