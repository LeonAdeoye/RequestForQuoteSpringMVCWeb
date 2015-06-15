package com.leon.rfq.services;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.leon.rfq.domains.RequestDetailImpl;
import com.leon.rfq.products.CalculationEngineImpl;
import com.leon.rfq.products.CalculationResult;
import com.leon.rfq.products.PricingModel;

@Service
public final class CalculationServiceImpl implements CalculationService
{
	private static final Logger logger = LoggerFactory.getLogger(CalculationServiceImpl.class);
	
	public CalculationServiceImpl() {}
	
	@Override
	public void calculate(RequestDetailImpl request, PricingModel model)
	{
		if((request == null) || (request.getLegs() == null) || (request.getLegs().size() == 0))
			throw new IllegalArgumentException("request argument is invalid");

		if(model == null)
			throw new IllegalArgumentException("model argument is invalid");
		
		List<CompletableFuture<CalculationResult>> listOfFutures =
				request.getLegs().stream().map(leg -> CompletableFuture.supplyAsync(() ->
				CalculationEngineImpl.calculate(model, leg)))
				.collect(Collectors.<CompletableFuture<CalculationResult>>toList());
		
		CompletableFuture<List<CalculationResult>> allDone = sequence(listOfFutures);
		
		allDone.handle(this::processCalculationResult);
	}
	
	private static <T> CompletableFuture<List<T>> sequence(List<CompletableFuture<T>> futures)
	{
	    CompletableFuture<Void> allDoneFuture = CompletableFuture
	    		.allOf(futures.toArray(new CompletableFuture[futures.size()]));
	    
	    return allDoneFuture.thenApply(v -> futures.stream()
	    		.map(future -> future.join()).collect(Collectors.<T>toList()));
	}
	
	public boolean processCalculationResult(List<CalculationResult> result, Throwable ex)
	{
		if(result != null)
		{
			return true;
		}
		else
		{
			if(logger.isErrorEnabled())
				logger.error(ex.getMessage());
			
			return false;
		}
	}
	
}
