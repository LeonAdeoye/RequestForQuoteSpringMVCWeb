package com.leon.rfq.products;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.leon.rfq.common.OptionConstants;
import com.leon.rfq.domains.OptionDetailImpl;

public class CalculationEngineImpl
{
	private CalculationEngineImpl() {}
	
	public static Map<String, BigDecimal> calculate(PricingModel model, Map<String, BigDecimal> inputs)
	{
		model.configure(inputs);
		return model.calculate();
	}
	
	public static CalculationResult calculate(PricingModel model, OptionDetailImpl leg)
	{
		model.configure(mapInputs(leg));
		return new CalculationResult(String.valueOf(leg.getLegId()), model.calculate());
	}
	
	public static Map<String, BigDecimal> mapInputs(OptionDetailImpl leg)
	{
		Map<String, BigDecimal> inputs = new HashMap<>();
		inputs.put(OptionConstants.UNDERLYING_PRICE, leg.getUnderlyingPrice());
		inputs.put(OptionConstants.STRIKE, leg.getStrike());
		inputs.put(OptionConstants.VOLATILITY, leg.getVolatility());
		inputs.put(OptionConstants.INTEREST_RATE, leg.getInterestRate());
		inputs.put(OptionConstants.TIME_TO_EXPIRY, leg.getYearsToExpiry());
		inputs.put(OptionConstants.IS_CALL_OPTION, leg.getIsCall() ? BigDecimal.valueOf(1) : BigDecimal.valueOf(0));
		inputs.put(OptionConstants.IS_EUROPEAN_OPTION, leg.getIsEuropean() ? BigDecimal.valueOf(1) : BigDecimal.valueOf(0));
		return inputs;
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
