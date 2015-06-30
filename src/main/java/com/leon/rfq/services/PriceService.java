package com.leon.rfq.services;

import java.math.BigDecimal;

import com.leon.rfq.domains.PriceDetailImpl;

public interface PriceService
{
	BigDecimal getLastPrice(String ric);
	BigDecimal getAskPrice(String ric);
	BigDecimal getBidPrice(String ric);
	BigDecimal getMidPrice(String ric);
	PriceDetailImpl getAllPrices(String ric);
	void initialize();
	void terminate();
}
