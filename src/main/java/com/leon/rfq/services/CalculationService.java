package com.leon.rfq.services;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

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

	Map<BigDecimal, Map<String, Optional<BigDecimal>>> calculateRange(
			PricingModel model, Map<String, BigDecimal> inputs,	RangeParameters rangeParameters);
}