package com.leon.rfq.simulators;

public interface PriceSimulator
{
	void initialize();
	void terminate();
	
	void removeAll();
	void remove(String underlyingRIC);
	
	void suspendAll();
	void suspend(String underlyingRIC);
	
	void awakenAll();
	void awaken(String underlyingRIC);
	
	void add(String underlyingRIC);
}
