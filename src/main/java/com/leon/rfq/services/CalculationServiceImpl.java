package com.leon.rfq.services;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.leon.rfq.common.OptionConstants;
import com.leon.rfq.domains.OptionDetailImpl;
import com.leon.rfq.domains.RequestDetailImpl;
import com.leon.rfq.events.PriceUpdateEvent;
import com.leon.rfq.products.PricingModel;
import com.leon.rfq.products.RangeParameters;

@Component
public class CalculationServiceImpl implements ApplicationListener<PriceUpdateEvent>
{
	private static final Logger logger = LoggerFactory.getLogger(CalculationServiceImpl.class);
	
	private CalculationServiceImpl() {}
	
	// TODO - should all of these methods be synchronized
	public synchronized static Map<String, Optional<BigDecimal>> calculate(PricingModel model, Map<String, BigDecimal> inputs)
	{
		if(model == null)
		{
			if(logger.isErrorEnabled())
				logger.error("model is an invalid argument");
			
			throw new NullPointerException("model is an invalid argument");
		}
		
		if(inputs == null)
		{
			if(logger.isErrorEnabled())
				logger.error("inputs is an invalid argument");
			
			throw new NullPointerException("inputs is an invalid argument");
		}
		
		model.configure(inputs);
		return model.calculate();
	}
	
	public synchronized static void calculate(PricingModel model, RequestDetailImpl request)
	{
		if(request == null)
		{
			if(logger.isErrorEnabled())
				logger.error("request is an invalid argument");
			
			throw new NullPointerException("request is an invalid argument");
		}
		
		if(model == null)
		{
			if(logger.isErrorEnabled())
				logger.error("model is an invalid argument");
			
			throw new IllegalArgumentException("model is an invalid argument");
		}
		
		for(OptionDetailImpl leg : request.getLegs())
		{
			model.configure(extractModelInputs(leg));
			extractModelOutputs(model.calculate(), leg);
		}
		
		aggregate(request);
	}
	
	public synchronized static void aggregate(RequestDetailImpl request)
	{
		if(request == null)
		{
			if(logger.isErrorEnabled())
				logger.error("request is an invalid argument");
			
			throw new NullPointerException("request is an invalid argument");
		}
		
		if((request.getLegs() != null) || (request.getLegs().size() != 0))
		{
			request.setDelta(request.getLegs().stream().map(OptionDetailImpl::getDelta)
			.reduce(BigDecimal.ZERO, BigDecimal::add));
			
			request.setGamma(request.getLegs().stream().map(OptionDetailImpl::getGamma)
			.reduce(BigDecimal.ZERO, BigDecimal::add));
			
			request.setVega(request.getLegs().stream().map(OptionDetailImpl::getVega)
			.reduce(BigDecimal.ZERO, BigDecimal::add));
			
			request.setTheta(request.getLegs().stream().map(OptionDetailImpl::getTheta)
			.reduce(BigDecimal.ZERO, BigDecimal::add));
			
			request.setRho(request.getLegs().stream().map(OptionDetailImpl::getRho)
			.reduce(BigDecimal.ZERO, BigDecimal::add));
			
			request.setIntrinsicValue(request.getLegs().stream().map(OptionDetailImpl::getIntrinsicValue)
			.reduce(BigDecimal.ZERO, BigDecimal::add));
			
			request.setTimeValue(request.getLegs().stream().map(OptionDetailImpl::getTimeValue)
			.reduce(BigDecimal.ZERO, BigDecimal::add));
			
			request.setPremiumAmount(request.getLegs().stream().map(OptionDetailImpl::getPremium)
			.reduce(BigDecimal.ZERO, BigDecimal::add));
		}
		
		if((request.getLegs() != null) && (request.getLegs().size() == 1))
		{
			request.setLambda(request.getLegs().get(0).getLambda());
		}
	}
	
	
	public synchronized static void calculate(PricingModel model, OptionDetailImpl leg)
	{
		if(model == null)
		{
			if(logger.isErrorEnabled())
				logger.error("model is an invalid argument");
			
			throw new NullPointerException("model is an invalid argument");
		}
		
		if(leg == null)
		{
			if(logger.isErrorEnabled())
				logger.error("leg is an invalid argument");
			
			throw new NullPointerException("leg is an invalid argument");
		}
		
		model.configure(extractModelInputs(leg));
		extractModelOutputs(model.calculate(), leg);
	}
	
	public synchronized static Map<String, BigDecimal> extractModelInputs(OptionDetailImpl leg)
	{
		if(leg == null)
		{
			if(logger.isErrorEnabled())
				logger.error("leg is an invalid argument");
			
			throw new NullPointerException("leg is an invalid argument");
		}
		
		Map<String, BigDecimal> inputs = new HashMap<>();
		inputs.put(OptionConstants.UNDERLYING_PRICE, leg.getUnderlyingPrice());
		inputs.put(OptionConstants.STRIKE, leg.getStrike());
		inputs.put(OptionConstants.VOLATILITY, leg.getVolatility());
		inputs.put(OptionConstants.INTEREST_RATE, leg.getInterestRate());
		inputs.put(OptionConstants.TIME_TO_EXPIRY, leg.getYearsToExpiry());
		inputs.put(OptionConstants.IS_CALL_OPTION, leg.getIsCall() ? BigDecimal.valueOf(1) : BigDecimal.valueOf(0));
		inputs.put(OptionConstants.IS_EUROPEAN_OPTION, leg.getIsEuropean() ? BigDecimal.valueOf(1) : BigDecimal.valueOf(0));
		
		if(logger.isDebugEnabled())
			logger.debug("Option model input: " + inputs.toString());
		
		return inputs;
	}
	
	public synchronized static void extractModelOutputs(Map<String, Optional<BigDecimal>> outputs, OptionDetailImpl leg)
	{
		if(leg == null)
		{
			if(logger.isErrorEnabled())
				logger.error("leg is an invalid argument");
			
			throw new NullPointerException("leg is an invalid argument");
		}
		
		if(outputs == null)
		{
			if(logger.isErrorEnabled())
				logger.error("outputs is an invalid argument");
			
			throw new NullPointerException("outputs is an invalid argument");
		}
		
		leg.setPremium(outputs.get(OptionConstants.THEORETICAL_VALUE).orElse(BigDecimal.ZERO));
		leg.setDelta(outputs.get(OptionConstants.DELTA).orElse(BigDecimal.ZERO));
		leg.setGamma(outputs.get(OptionConstants.GAMMA).orElse(BigDecimal.ZERO));
		leg.setVega(outputs.get(OptionConstants.VEGA).orElse(BigDecimal.ZERO));
		leg.setTheta(outputs.get(OptionConstants.THETA).orElse(BigDecimal.ZERO));
		leg.setRho(outputs.get(OptionConstants.RHO).orElse(BigDecimal.ZERO));
		leg.setIntrinsicValue(outputs.get(OptionConstants.INTRINSIC_VALUE).orElse(BigDecimal.ZERO));
		leg.setLambda(outputs.get(OptionConstants.LAMBDA).orElse(BigDecimal.ZERO));
		leg.setTimeValue(outputs.get(OptionConstants.TIME_VALUE).orElse(BigDecimal.ZERO));
		
		if(logger.isDebugEnabled())
			logger.debug("Option model outputs: " + outputs.toString());
	}
	
	
	public synchronized static Map<BigDecimal, Map<String, Optional<BigDecimal>>> calculateRange(PricingModel model,
			Map<String, BigDecimal> inputs, RangeParameters rangeParameters)
	{
		if(model == null)
		{
			if(logger.isErrorEnabled())
				logger.error("model is an invalid argument");
			
			throw new NullPointerException("model is an invalid argument");
		}
		
		if(inputs == null)
		{
			if(logger.isErrorEnabled())
				logger.error("inputs is an invalid argument");
			
			throw new NullPointerException("inputs is an invalid argument");
		}
		
		if(rangeParameters == null)
		{
			if(logger.isErrorEnabled())
				logger.error("rangeParameters is an invalid argument");
			
			throw new NullPointerException("rangeParameters is an invalid argument");
		}
		
		Map<BigDecimal, Map<String, Optional<BigDecimal>>> result = new TreeMap<>();
		
		for(BigDecimal rangeValue = rangeParameters.getStartValue();
				rangeValue.compareTo(rangeParameters.getEndValue()) <= 0;
				rangeValue = rangeValue.add(rangeParameters.getIncrement()))
		{
			inputs.put(rangeParameters.getRangeVariableName(), rangeValue);
			model.configure(inputs);
			result.put(rangeValue, model.calculate(rangeParameters.getListOfRequiredOutput()));
		}
		return result;
	}

	@Override
	public void onApplicationEvent(PriceUpdateEvent arg0)
	{
		
	}
}
