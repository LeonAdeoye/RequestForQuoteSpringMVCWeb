package com.leon.rfq.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.leon.rfq.common.CalculationConstants;
import com.leon.rfq.common.EnumTypes.SideEnum;
import com.leon.rfq.common.OptionConstants;
import com.leon.rfq.common.TriFunction;
import com.leon.rfq.domains.OptionDetailImpl;
import com.leon.rfq.domains.RequestDetailImpl;
import com.leon.rfq.products.BlackScholesModelImpl;
import com.leon.rfq.products.PricingModel;
import com.leon.rfq.products.RangeParameters;

@Component
public class CalculationServiceImpl implements CalculationService
{
	private static final Logger logger = LoggerFactory.getLogger(CalculationServiceImpl.class);
	
	private CalculationServiceImpl() {}
	
	private static final int ALL_REQUIRED_OUTPUT_COUNT = 7;
	
	private static final List<String> ALL_REQUIRED_OUTPUT = new ArrayList<>(ALL_REQUIRED_OUTPUT_COUNT);
	
	static
	{
		ALL_REQUIRED_OUTPUT.add(OptionConstants.PNL);
		ALL_REQUIRED_OUTPUT.add(OptionConstants.DELTA);
		ALL_REQUIRED_OUTPUT.add(OptionConstants.GAMMA);
		ALL_REQUIRED_OUTPUT.add(OptionConstants.THETA);
		ALL_REQUIRED_OUTPUT.add(OptionConstants.RHO);
		ALL_REQUIRED_OUTPUT.add(OptionConstants.VEGA);
		ALL_REQUIRED_OUTPUT.add(OptionConstants.THEORETICAL_VALUE);
	}
	
	// TODO - should all of these methods be synchronized? Definitely NOT!!!
	// Stage 2: Need to revisit and optimize/limit all synchronization blocks here
	@Override
	public synchronized Map<String, BigDecimal> calculate(PricingModel model, Map<String, BigDecimal> inputs)
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
		
		TriFunction<OptionDetailImpl, BigDecimal, BigDecimal, BigDecimal> sideAggregator =
				(leg, AggregatedValue, calculationResult) -> (leg.getSide() == SideEnum.BUY) ?
						AggregatedValue.add(calculationResult) : AggregatedValue.subtract(calculationResult);
				
		request.setProfitAndLossPoints(calculatePointsOfInterest(model, request, pointsOfInterest,
				OptionConstants.UNDERLYING_PRICE, OptionConstants.INTRINSIC_VALUE, sideAggregator,
				(calculatedValue) -> calculatedValue.add(request.getPremiumAmount())));
	}

	@Override
	public synchronized List<BigDecimal> calculatePointsOfInterest(PricingModel model, RequestDetailImpl request,
			SortedSet<BigDecimal> pointsOfInterest, String input, String output,
			TriFunction<OptionDetailImpl, BigDecimal, BigDecimal, BigDecimal> sideAggregator,
			Function<BigDecimal, BigDecimal> massageFunction)
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
		
		if(pointsOfInterest == null)
		{
			if(logger.isErrorEnabled())
				logger.error("pointsOfInterest is an invalid argument");
			
			throw new NullPointerException("pointsOfInterest is an invalid argument");
		}
		
		if(sideAggregator == null)
		{
			if(logger.isErrorEnabled())
				logger.error("sideAggregator is an invalid argument");
			
			throw new NullPointerException("sideAggregator is an invalid argument");
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
				Map<String, BigDecimal> inputs = extractModelInputs(leg);
				inputs.put(input, pointOfInterest);
				model.configure(inputs);
				
				BigDecimal requiredResult = model.calculate(output).multiply(BigDecimal.valueOf(leg.getQuantity()));
				
				sumOfOutputAtPoint = sideAggregator.apply(leg, sumOfOutputAtPoint, requiredResult);
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
			request.setDelta(request.getLegs().stream().map(leg -> leg.getDelta())
				.reduce(BigDecimal.ZERO, BigDecimal::add));
			
			request.setGamma(request.getLegs().stream().map(leg -> leg.getGamma())
				.reduce(BigDecimal.ZERO, BigDecimal::add));
			
			request.setVega(request.getLegs().stream().map(leg -> leg.getVega())
				.reduce(BigDecimal.ZERO, BigDecimal::add));
			
			request.setTheta(request.getLegs().stream().map(leg -> leg.getTheta())
				.reduce(BigDecimal.ZERO, BigDecimal::add));
			
			request.setRho(request.getLegs().stream().map(leg -> leg.getRho())
				.reduce(BigDecimal.ZERO, BigDecimal::add));
			
			request.setIntrinsicValue(request.getLegs().stream().map(leg ->	leg.getIntrinsicValue())
				.reduce(BigDecimal.ZERO, BigDecimal::add));
			
			request.setTimeValue(request.getLegs().stream().map(leg -> leg.getTimeValue())
				.reduce(BigDecimal.ZERO, BigDecimal::add));
			
			request.setPremiumAmount(request.getLegs().stream().map(leg -> leg.getPremium())
				.reduce(BigDecimal.ZERO, BigDecimal::add));
		}
		
		// Lambda may not be additive
		if((request.getLegs() != null) && (request.getLegs().size() == 1))
			request.setLambda(request.getLegs().get(0).getLambda());
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
		inputs.put(OptionConstants.SIDE_MULTIPLIER, leg.getSide() == SideEnum.BUY ? BigDecimal.valueOf(1) : BigDecimal.valueOf(-1));
		inputs.put(OptionConstants.QTY_MULTIPLIER, BigDecimal.valueOf(leg.getQuantity()));
		
		if(logger.isDebugEnabled())
			logger.debug("Option model input: " + inputs.toString());
		
		return inputs;
	}
	
	@Override
	public synchronized void extractModelOutputs(Map<String, BigDecimal> outputs, OptionDetailImpl leg)
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
			logger.debug("Option model outputs: " + outputs.toString());
	}
	
	
	@Override
	public synchronized Map<String, List<BigDecimal>> calculateRange(PricingModel model,
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
		
		Map<String, List<BigDecimal>> range = new HashMap<>();
				
		rangeParameters.getListOfRequiredOutput().forEach(output -> range.put(output, new ArrayList<>()));
		range.put(rangeParameters.getRangeVariableName(), new ArrayList<>());
		
		// TODO Convert this to a parallel calculation and validate optimization by measuring time taken
		for(BigDecimal rangeValue = rangeParameters.getStartValue();
				rangeValue.compareTo(rangeParameters.getEndValue()) <= 0;
				rangeValue = rangeValue.add(rangeParameters.getIncrement()))
		{
			inputs.put(rangeParameters.getRangeVariableName(), rangeValue);
			model.configure(inputs);

			range.get(rangeParameters.getRangeVariableName()).add(rangeValue);
						
			model.calculate(rangeParameters.getListOfRequiredOutput()).entrySet()
				.forEach(entry -> range.get(entry.getKey()).add(entry.getValue()));
		}
		
		return range;
	}
	
	@Override
	public Map<String, Map<String, List<BigDecimal>>> chartData(RequestDetailImpl request)
	{
		if(request == null)
		{
			if(logger.isErrorEnabled())
				logger.error("request is an invalid argument");
			
			throw new NullPointerException("request is an invalid argument");
		}
		
		Map<String, Map<String, List<BigDecimal>>> resultRange = new HashMap<>();
						
		resultRange.put(OptionConstants.TIME_TO_EXPIRY, chartDataForSpecificRangeVariable(request,
				request.getLegs().get(0).getYearsToExpiry(),OptionConstants.TIME_TO_EXPIRY));
		
		resultRange.put(OptionConstants.UNDERLYING_PRICE, chartDataForSpecificRangeVariable(request,
				request.getUnderlyingPrice(), OptionConstants.UNDERLYING_PRICE));
		
		resultRange.put(OptionConstants.VOLATILITY, chartDataForSpecificRangeVariable(request,
				request.getLegs().get(0).getVolatility(), OptionConstants.VOLATILITY));
		
		return resultRange;
	}
	
	@Override
	public Map<String, List<BigDecimal>> chartDataForSpecificRangeVariable(RequestDetailImpl request,
			BigDecimal rangeSeed, String rangeVar)
	{
		if(rangeSeed == null)
		{
			if(logger.isErrorEnabled())
				logger.error("rangeSeed is an invalid argument");
			
			throw new NullPointerException("rangeSeed is an invalid argument");
		}
				
		if((rangeVar == null) || rangeVar.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("rangeVar is an invalid argument");
			
			throw new IllegalArgumentException("rangeVar is an invalid argument");
		}
		
		BigDecimal incr = rangeSeed.divide(CalculationConstants.ONE_HUNDRED,
				CalculationConstants.SCALE_OF_FOUR, RoundingMode.HALF_UP);
		
		BigDecimal max = rangeSeed.multiply(CalculationConstants.TWO);
		
		BigDecimal min = BigDecimal.ZERO.add(incr);
				
		RangeParameters rangeParameters = new RangeParameters(min, max,
				incr, rangeVar, ALL_REQUIRED_OUTPUT);
		
		PricingModel model = new BlackScholesModelImpl();
		
		Map<String, List<BigDecimal>> rangeResult = new HashMap<>();
		
		for(OptionDetailImpl leg : request.getLegs())
			aggregate(rangeResult, calculateRange(model, extractModelInputs(leg), rangeParameters));
		
		return rangeResult;
	}

	@Override
	public void aggregate(Map<String, List<BigDecimal>> rangeResult,
			Map<String, List<BigDecimal>> calculatedRange)
	{
		if(rangeResult.size() == 0)
		{
			rangeResult.putAll(calculatedRange);
			return;
		}
		
		for(Map.Entry<String, List<BigDecimal>> rangeValueListEntry : calculatedRange.entrySet())
		{
			int numberOfElements = rangeValueListEntry.getValue().size();
			BigDecimal[] sumOfTwoRanges = new BigDecimal[numberOfElements];
			BigDecimal[] first = rangeResult.get(rangeValueListEntry.getKey()).toArray(new BigDecimal[numberOfElements]);
			BigDecimal[] second = rangeValueListEntry.getValue().toArray(new BigDecimal[numberOfElements]);
			
			Arrays.setAll(sumOfTwoRanges, i -> first[i].add(second[i]));
			
			rangeResult.put(rangeValueListEntry.getKey(), Arrays.asList(sumOfTwoRanges));
		}
	}
}

