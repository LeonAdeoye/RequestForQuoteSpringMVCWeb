package com.leon.rfq.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.SortedSet;
import java.util.function.Function;

import com.leon.rfq.common.TriFunction;
import com.leon.rfq.domains.OptionDetailImpl;
import com.leon.rfq.domains.RequestDetailImpl;
import com.leon.rfq.products.PricingModel;
import com.leon.rfq.products.RangeParameters;

public interface CalculationService
{
	Map<String, Optional<BigDecimal>> calculate(PricingModel model,	Map<String, BigDecimal> inputs);

	void calculate(PricingModel model, RequestDetailImpl request);

	void aggregate(RequestDetailImpl request);

	void calculate(PricingModel model, OptionDetailImpl leg);

	Map<String, BigDecimal> extractModelInputs(OptionDetailImpl leg);

	void extractModelOutputs(Map<String, Optional<BigDecimal>> outputs,	OptionDetailImpl leg);

	Map<BigDecimal, Map<String, BigDecimal>> calculateRange(
			PricingModel model, Map<String, BigDecimal> inputs,	RangeParameters rangeParameters);

	void calculateProfitAndLossPoints(PricingModel model, RequestDetailImpl request);

	List<BigDecimal> calculatePointsOfInterest(PricingModel model, RequestDetailImpl request,
			SortedSet<BigDecimal> pointsOfInterest, String input, String output,
			TriFunction<OptionDetailImpl, BigDecimal, BigDecimal, BigDecimal> sideAggregator,
			Function<BigDecimal, BigDecimal> massageFunction);

	Map<BigDecimal, Map<String, BigDecimal>> chartData(RequestDetailImpl request);
}