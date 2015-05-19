package com.leon.rfq.simulation;

public interface PriceSimulator
{
	public enum PriceSimulatorRequestEnum
	{
		ADD_UNDERLYING,
		REMOVE_UNDERLYING,
		SUSPEND_UNDERLYING,
		AWAKEN_UNDERLYING,
		SUSPEND_ALL,
		AWAKEN_ALL		
	}
	
	void initialize();
	void add(String underlyingRIC, double priceMean, double priceVariance);
	void remove(String underlyingRIC);
	void suspendAll();
	void suspend(String underlyingRIC);
	void awakenAll();
	void awaken(String underlyingRIC);
	void terminate();
}
