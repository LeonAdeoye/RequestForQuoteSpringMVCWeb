package com.leon.rfq.simulators;

public interface PriceSimulator
{
	void initialize();
	void add(String underlyingRIC, double priceMean, double priceVariance, double priceSpread);
	
	void removeAll();
	void remove(String underlyingRIC);
	
	void suspendAll();
	void suspend(String underlyingRIC);
	
	void awakenAll();
	void awaken(String underlyingRIC);
	
	void terminate();
}
