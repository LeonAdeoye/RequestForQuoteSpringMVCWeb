package com.leon.rfq.products;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class CalculationRequest implements Callable<Map<String, BigDecimal>>
{
	private final PricingModel model;
	private final Map<String, BigDecimal> inputs = new HashMap<>();
	
	public CalculationRequest(PricingModel model, Map<String, BigDecimal> inputs)
	{
		this.model = model;
		this.inputs.putAll(inputs);
	}
	
	@Override
	public Map<String, BigDecimal> call() throws Exception
	{
		return CalculationEngineImpl.calculate(this.model, this.inputs);
	}

}
