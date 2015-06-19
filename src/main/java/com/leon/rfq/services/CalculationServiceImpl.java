package com.leon.rfq.services;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;

import com.leon.rfq.common.OptionConstants;
import com.leon.rfq.domains.OptionDetailImpl;
import com.leon.rfq.domains.RequestDetailImpl;
import com.leon.rfq.events.PriceUpdateEvent;
import com.leon.rfq.products.PricingModel;
import com.leon.rfq.products.RangeParameters;

public class CalculationServiceImpl implements ApplicationListener<PriceUpdateEvent>
{
	private static final Logger logger = LoggerFactory.getLogger(CalculationServiceImpl.class);
	
	private CalculationServiceImpl() {}
	
	public synchronized static Map<String, BigDecimal> calculate(PricingModel model, Map<String, BigDecimal> inputs)
	{
		model.configure(inputs);
		return model.calculate();
	}
	
	public synchronized static void calculate(PricingModel model, RequestDetailImpl request)
	{
		if((request == null) || (request.getLegs() == null))
			return;
		
		for(OptionDetailImpl leg : request.getLegs())
		{
			model.configure(extractModelInputs(leg));
			extractModelOutputs(model.calculate(), leg);
		}
		
		if(logger.isDebugEnabled())
			logger.debug(model.toString());
	}
	
	public synchronized static void calculate(PricingModel model, OptionDetailImpl leg)
	{
		model.configure(extractModelInputs(leg));
		extractModelOutputs(model.calculate(), leg);
	}
	
	public synchronized static Map<String, BigDecimal> extractModelInputs(OptionDetailImpl leg)
	{
		Map<String, BigDecimal> inputs = new HashMap<>();
		inputs.put(OptionConstants.UNDERLYING_PRICE, leg.getUnderlyingPrice());
		inputs.put(OptionConstants.STRIKE, leg.getStrike());
		inputs.put(OptionConstants.VOLATILITY, leg.getVolatility());
		inputs.put(OptionConstants.INTEREST_RATE, leg.getInterestRate());
		inputs.put(OptionConstants.TIME_TO_EXPIRY, leg.getYearsToExpiry());
		inputs.put(OptionConstants.IS_CALL_OPTION, leg.getIsCall() ? BigDecimal.valueOf(1) : BigDecimal.valueOf(0));
		inputs.put(OptionConstants.IS_EUROPEAN_OPTION, leg.getIsEuropean() ? BigDecimal.valueOf(1) : BigDecimal.valueOf(0));
		
		if(logger.isDebugEnabled())
			logger.debug(inputs.toString());
		
		return inputs;
	}
	
	public synchronized static void extractModelOutputs(Map<String, BigDecimal> outputs, OptionDetailImpl leg)
	{
		leg.setPremium(outputs.get(OptionConstants.THEORETICAL_VALUE));
		leg.setDelta(outputs.get(OptionConstants.DELTA));
		leg.setGamma(outputs.get(OptionConstants.GAMMA));
		leg.setVega(outputs.get(OptionConstants.VEGA));
		leg.setTheta(outputs.get(OptionConstants.THETA));
		leg.setRho(outputs.get(OptionConstants.RHO));
		leg.setIntrinsicValue(outputs.get(OptionConstants.INTRINSIC_VALUE));
		leg.setLambda(outputs.get(OptionConstants.LAMBDA));
		leg.setTimeValue(outputs.get(OptionConstants.TIME_VALUE));
		
		if(logger.isDebugEnabled())
			logger.debug(outputs.toString());
		
	}
	
	
	public synchronized static Map<BigDecimal, Map<String, BigDecimal>> calculateRange(PricingModel model, Map<String, BigDecimal> inputs,
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

	@Override
	public void onApplicationEvent(PriceUpdateEvent arg0)
	{
		
	}
}
