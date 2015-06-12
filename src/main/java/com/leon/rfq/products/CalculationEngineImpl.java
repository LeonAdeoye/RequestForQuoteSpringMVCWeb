package com.leon.rfq.products;

import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;

public class CalculationEngineImpl
{
	private CalculationEngineImpl() {}
	
	public static Map<String, BigDecimal> calculate(PricingModel model, Map<String, BigDecimal> inputs)
	{
		model.configure(inputs);
		return model.calculate();
	}
	
	public static Map<BigDecimal, Map<String, BigDecimal>> calculateRange(PricingModel model, Map<String, BigDecimal> inputs,
			RangeParameters params)
	{
		Map<BigDecimal, Map<String, BigDecimal>> result = new TreeMap<>();
		
		for(BigDecimal value = params.getStartValue();
				value.compareTo(params.getEndValue()) <= 0;
				value = value.add(params.getIncrement()))
		{
			inputs.put(params.getRangeVariableName(), value);
			model.configure(inputs);
			result.put(value, model.calculate(params.getListOfRequiredOutput()));
		}
		return result;
	}
}
