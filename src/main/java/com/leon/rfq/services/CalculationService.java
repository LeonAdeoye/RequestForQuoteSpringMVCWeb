package com.leon.rfq.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.function.Function;

import com.leon.rfq.common.TriFunction;
import com.leon.rfq.domains.OptionDetailImpl;
import com.leon.rfq.domains.RequestDetailImpl;
import com.leon.rfq.products.PricingModel;
import com.leon.rfq.products.RangeParameters;

public interface CalculationService
{
	Map<String, BigDecimal> calculate(PricingModel model,	Map<String, BigDecimal> inputs);

	void calculate(PricingModel model, RequestDetailImpl request);

	void aggregate(RequestDetailImpl request);

	void calculate(PricingModel model, OptionDetailImpl leg);

	Map<String, List<BigDecimal>> calculateRange(
			PricingModel model, Map<String, BigDecimal> inputs,	RangeParameters rangeParameters);

	void calculateProfitAndLossPoints(PricingModel model, RequestDetailImpl request);

	List<BigDecimal> calculatePointsOfInterest(PricingModel model, RequestDetailImpl request,
			SortedSet<BigDecimal> pointsOfInterest, String input, String output,
			TriFunction<OptionDetailImpl, BigDecimal, BigDecimal, BigDecimal> sideAggregator,
			Function<BigDecimal, BigDecimal> massageFunction);

	Map<String, Map<String, List<BigDecimal>>> chartData(RequestDetailImpl request);

	Map<String, BigDecimal> extractModelInputs(OptionDetailImpl leg);

	void extractModelOutputs(Map<String, BigDecimal> outputs, OptionDetailImpl leg);

	void aggregate(Map<String, List<BigDecimal>> rangeResult,
			Map<String, List<BigDecimal>> calculatedRange);

	Map<String, List<BigDecimal>> chartDataForSpecificRangeVariable(
			RequestDetailImpl request, BigDecimal rangeSeed, String rangeVar);
}