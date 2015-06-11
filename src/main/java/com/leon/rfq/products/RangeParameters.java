package com.leon.rfq.products;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public final class RangeParameters
{
	private final BigDecimal startValue;
	private final BigDecimal endValue;
	private final BigDecimal increment;
	private final String rangeVariableName;
	private final List<String> listOfRequiredOutput;
	
	public RangeParameters(BigDecimal startValue, BigDecimal endValue, BigDecimal increment,
			String rangeVariableName, List<String> listOfReuqiredOutput)
	{
		this.startValue = startValue;
		this.endValue = endValue;
		this.increment = increment;
		this.rangeVariableName = rangeVariableName;
		this.listOfRequiredOutput = listOfReuqiredOutput;
	}
	
	public RangeParameters(BigDecimal startValue, BigDecimal endValue, BigDecimal increment,
			String rangeVariableName, String onlyOutput)
	{
		this.startValue = startValue;
		this.endValue = endValue;
		this.increment = increment;
		this.rangeVariableName = rangeVariableName;
		this.listOfRequiredOutput = new ArrayList<>(1);
		this.listOfRequiredOutput.add(onlyOutput);
	}

	public BigDecimal getStartValue()
	{
		return this.startValue;
	}

	public BigDecimal getEndValue()
	{
		return this.endValue;
	}

	public BigDecimal getIncrement()
	{
		return this.increment;
	}

	public String getRangeVariableName()
	{
		return this.rangeVariableName;
	}

	public List<String> getListOfRequiredOutput()
	{
		return this.listOfRequiredOutput;
	}
}
