package com.leon.rfq.products;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class CalculationResult
{
	private final String identifier;
	
	private final Map<String, BigDecimal> resultValues = new HashMap<>();

	public CalculationResult(String identifer, Map<String, BigDecimal> resultValues)
	{
		this.identifier = identifer;
		this.resultValues.putAll(resultValues);
	}

	public String getIdentifer()
	{
		return this.identifier;
	}

	public Map<String, BigDecimal> getResultValue()
	{
		return this.resultValues;
	}
}
