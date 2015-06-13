package com.leon.rfq.services;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.Future;

import com.leon.rfq.products.PricingModel;

public interface CalculationService
{
	Future<Map<String, BigDecimal>>calculate(long identifier, PricingModel model, Map<String, BigDecimal> inputs);
}
