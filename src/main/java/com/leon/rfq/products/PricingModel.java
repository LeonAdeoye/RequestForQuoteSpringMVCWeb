package com.leon.rfq.products;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
 
public interface PricingModel
{
	void configure(Map<String, BigDecimal> inputs);
	Map<String, Optional<BigDecimal>> calculate();
	Map<String, BigDecimal> calculate(List<String> listOfRequiredOutputs);
	Optional<BigDecimal> calculate(String requiredOutput);
}