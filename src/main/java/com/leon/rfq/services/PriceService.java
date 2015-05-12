package com.leon.rfq.services;

import java.math.BigDecimal;
import java.util.Date;

public interface PriceService
{
	BigDecimal getLatestPrice(String ric);
	BigDecimal getClosePrice(String ric);
	BigDecimal getOpenPrice(String ric);
	BigDecimal getAskPrice(String ric);
	BigDecimal getBidPrice(String ric);
	BigDecimal getMidPrice(String ric);
	
	BigDecimal getClosePrice(String ric, Date referenceDate);
	BigDecimal getOpenPrice(String ric, Date referenceDate);
}
