package com.leon.rfq.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.leon.rfq.common.EnumTypes.SideEnum;
import com.leon.rfq.common.OptionConstants;
import com.leon.rfq.domains.OptionDetailImpl;
import com.leon.rfq.domains.RequestDetailImpl;
import com.leon.rfq.events.PriceUpdateEvent;
import com.leon.rfq.products.BlackScholesModelImpl;
import com.leon.rfq.products.PricingModel;
import com.leon.rfq.products.RangeParameters;

@Component
public class CalculationServiceImpl implements CalculationService, ApplicationListener<PriceUpdateEvent>
{
	private static final Logger logger = LoggerFactory.getLogger(CalculationServiceImpl.class);
	
	private CalculationServiceImpl() {}
	
	// TODO - should all of these methods be synchronized
	@Override
	public synchronized Map<String, Optional<BigDecimal>> calculate(PricingModel model, Map<String, BigDecimal> inputs)
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
	
	@Override
	public synchronized void calculate(PricingModel model, RequestDetailImpl request)
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
		calculateProfitAndLossPoints(model, request);
	}
	
	@Override
	public synchronized void calculateProfitAndLossPoints(PricingModel model, RequestDetailImpl request)
	{
		if(model == null)
		{
			if(logger.isErrorEnabled())
				logger.error("model is an invalid argument");
			
			throw new NullPointerException("model is an invalid argument");
		}
		
		if(request == null)
		{
			if(logger.isErrorEnabled())
				logger.error("request is an invalid argument");
			
			throw new NullPointerException("request is an invalid argument");
		}
		
		SortedSet<BigDecimal> pointsOfInterest = new TreeSet<>();
		pointsOfInterest.addAll(request.getLegs().stream().map(OptionDetailImpl::getStrike)
				.collect(Collectors.toSet()));
				
		pointsOfInterest.add(Collections.min(pointsOfInterest).multiply(BigDecimal.valueOf(0.9)));
		pointsOfInterest.add(Collections.max(pointsOfInterest).multiply(BigDecimal.valueOf(1.1)));
				
		request.setProfitAndLossPoints(calculatePointsOfInterest(request, pointsOfInterest,
				OptionConstants.UNDERLYING_PRICE, OptionConstants.THEORETICAL_VALUE,
				(theoreticalValue) -> theoreticalValue.add(request.getPremiumAmount())));
	}

	@Override
	public synchronized List<BigDecimal> calculatePointsOfInterest(RequestDetailImpl request,
			SortedSet<BigDecimal> pointsOfInterest, String input, String output,
			Function<BigDecimal, BigDecimal> massageFunction)
	{
		if(request == null)
		{
			if(logger.isErrorEnabled())
				logger.error("request is an invalid argument");
			
			throw new NullPointerException("request is an invalid argument");
		}
		
		if(pointsOfInterest == null)
		{
			if(logger.isErrorEnabled())
				logger.error("pointsOfInterest is an invalid argument");
			
			throw new NullPointerException("pointsOfInterest is an invalid argument");
		}
		
		if(massageFunction == null)
		{
			if(logger.isErrorEnabled())
				logger.error("massageFunction is an invalid argument");
			
			throw new NullPointerException("massageFunction is an invalid argument");
		}
		
		if((input == null) || input.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("input is an invalid argument");
			
			throw new IllegalArgumentException("input is an invalid argument");
		}
		
		if((output == null) || output.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("output is an invalid argument");
			
			throw new IllegalArgumentException("output is an invalid argument");
		}
		
		List<BigDecimal> result = new ArrayList<>();
		
		for(BigDecimal pointOfInterest : pointsOfInterest)
		{
			BigDecimal sumOfOutputAtPoint = BigDecimal.ZERO;
			for(OptionDetailImpl leg : request.getLegs())
			{
				BigDecimal intrinsicValue = BlackScholesModelImpl.calculateIntrinsicValue(leg.getIsCall(), pointOfInterest, leg.getStrike())
						.multiply(BigDecimal.valueOf(leg.getQuantity()));
				
				if(leg.getSide() == SideEnum.BUY)
					sumOfOutputAtPoint = sumOfOutputAtPoint.add(intrinsicValue);
				else
					sumOfOutputAtPoint = sumOfOutputAtPoint.subtract(intrinsicValue);
			}
			result.add(massageFunction.apply(sumOfOutputAtPoint));
		}
		
		return result;
	}
	
	@Override
	public synchronized void aggregate(RequestDetailImpl request)
	{
		if(request == null)
		{
			if(logger.isErrorEnabled())
				logger.error("request is an invalid argument");
			
			throw new NullPointerException("request is an invalid argument");
		}
		
		if((request.getLegs() != null) || (request.getLegs().size() != 0))
		{
			request.setDelta(request.getLegs().stream().map(leg ->
				(leg.getDelta().multiply(leg.getSide() == SideEnum.BUY ? BigDecimal.ONE : BigDecimal.valueOf(-1))
				.multiply(BigDecimal.valueOf(leg.getQuantity()))))
				.reduce(BigDecimal.ZERO, BigDecimal::add));
			
			request.setGamma(request.getLegs().stream().map(leg ->
				(leg.getGamma().multiply(leg.getSide() == SideEnum.BUY ? BigDecimal.ONE : BigDecimal.valueOf(-1))
				.multiply(BigDecimal.valueOf(leg.getQuantity()))))
				.reduce(BigDecimal.ZERO, BigDecimal::add));
			
			request.setVega(request.getLegs().stream().map(leg ->
				(leg.getVega().multiply(leg.getSide() == SideEnum.BUY ? BigDecimal.ONE : BigDecimal.valueOf(-1))
				.multiply(BigDecimal.valueOf(leg.getQuantity()))))
				.reduce(BigDecimal.ZERO, BigDecimal::add));
			
			request.setTheta(request.getLegs().stream().map(leg ->
				(leg.getTheta().multiply(leg.getSide() == SideEnum.BUY ? BigDecimal.ONE : BigDecimal.valueOf(-1))
				.multiply(BigDecimal.valueOf(leg.getQuantity()))))
				.reduce(BigDecimal.ZERO, BigDecimal::add));
			
			request.setRho(request.getLegs().stream().map(leg ->
				(leg.getRho().multiply(leg.getSide() == SideEnum.BUY ? BigDecimal.ONE : BigDecimal.valueOf(-1))
				.multiply(BigDecimal.valueOf(leg.getQuantity()))))
				.reduce(BigDecimal.ZERO, BigDecimal::add));
			
			request.setIntrinsicValue(request.getLegs().stream().map(leg ->
				(leg.getIntrinsicValue().multiply(leg.getSide() == SideEnum.BUY ? BigDecimal.valueOf(-1) : BigDecimal.ONE)
				.multiply(BigDecimal.valueOf(leg.getQuantity()))))
				.reduce(BigDecimal.ZERO, BigDecimal::add));
			
			request.setTimeValue(request.getLegs().stream().map(leg ->
				(leg.getTimeValue().multiply(leg.getSide() == SideEnum.BUY ?  BigDecimal.valueOf(-1) : BigDecimal.ONE)
				.multiply(BigDecimal.valueOf(leg.getQuantity()))))
				.reduce(BigDecimal.ZERO, BigDecimal::add));
			
			request.setPremiumAmount(request.getLegs().stream().map(leg ->
				(leg.getPremium().multiply(leg.getSide() == SideEnum.BUY ? BigDecimal.valueOf(-1) : BigDecimal.ONE)
				.multiply(BigDecimal.valueOf(leg.getQuantity()))))
				.reduce(BigDecimal.ZERO, BigDecimal::add));
		}
		
		if((request.getLegs() != null) && (request.getLegs().size() == 1))
		{
			request.setLambda(request.getLegs().get(0).getLambda());
		}
	}
	
	
	@Override
	public synchronized void calculate(PricingModel model, OptionDetailImpl leg)
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
	
	@Override
	public synchronized Map<String, BigDecimal> extractModelInputs(OptionDetailImpl leg)
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
	
	@Override
	public synchronized void extractModelOutputs(Map<String, Optional<BigDecimal>> outputs, OptionDetailImpl leg)
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
	
	
	@Override
	public synchronized Map<BigDecimal, Map<String, Optional<BigDecimal>>> calculateRange(PricingModel model,
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
