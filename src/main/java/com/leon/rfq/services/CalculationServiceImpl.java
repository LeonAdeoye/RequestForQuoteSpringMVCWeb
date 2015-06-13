package com.leon.rfq.services;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.leon.rfq.products.CalculationEngineImpl;
import com.leon.rfq.products.PricingModel;

@Service
public final class CalculationServiceImpl implements CalculationService
{
	private static final Logger logger = LoggerFactory.getLogger(CalculationServiceImpl.class);
	private ApplicationEventPublisher applicationEventPublisher;
	
	public CalculationServiceImpl() {}
	
	@Override
	public Future<Map<String, BigDecimal>>calculate(long identifier, PricingModel model, Map<String, BigDecimal> inputs)
	{
		return CompletableFuture.supplyAsync(() ->
		CalculationEngineImpl.calculate(model, inputs)).thenAccept(RequestServiceImpl::processCalculationResult());
	}
	
}
