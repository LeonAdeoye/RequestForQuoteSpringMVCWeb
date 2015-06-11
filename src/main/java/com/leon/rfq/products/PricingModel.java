package com.leon.rfq.products;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
 
public interface PricingModel
{
	void configure(Map<String, BigDecimal> inputs);
	Map<String, BigDecimal> calculate();
	Map<String, BigDecimal> calculate(List<String> listOfRequiredOutputs);
}