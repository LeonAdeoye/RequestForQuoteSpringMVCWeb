package com.leon.rfq.simulators;

import java.math.BigDecimal;

public interface PriceSimulator
{
	void initialize();
	
	void removeAll();
	void remove(String underlyingRIC);
	
	void suspendAll();
	void suspend(String underlyingRIC);
	
	void awakenAll();
	void awaken(String underlyingRIC);
	
	void terminate();
	void add(String underlyingRIC, BigDecimal priceMean, BigDecimal priceVariance, BigDecimal priceSpread);
}
