package com.leon.rfq.services;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.leon.rfq.domains.PriceDetailImpl;

@Service
public final class PriceServiceImpl implements PriceService
{
	private static final Logger logger = LoggerFactory.getLogger(PriceServiceImpl.class);
	private final Map<String, PriceDetailImpl> priceMap = new ConcurrentHashMap<>(20, 0.9f, 5);
	private final boolean isRunning = true;
	
	@Resource(name="priceUpdateBlockingQueue")
	private BlockingQueue<PriceDetailImpl> priceUpdateBlockingQueue;
	
	@Override
	@PostConstruct
	public void initialize()
	{
		if(logger.isInfoEnabled())
			logger.info("Starting price service..");
		
		Executors.newSingleThreadExecutor().submit(() ->
		{
			try
			{
				while(this.isRunning)
				{
					PriceDetailImpl priceUpdate = this.priceUpdateBlockingQueue.take();
					
					if(logger.isDebugEnabled())
						logger.debug("New price update: "  + priceUpdate);
					
					this.priceMap.put(priceUpdate.getUnderlyingRIC(), priceUpdate);
				}
			}
			catch(InterruptedException ie)
			{
				if(logger.isInfoEnabled())
					logger.info("Interruption exception raised. Terminating price service...");
				
				this.priceMap.clear();
			}
		});
	}
	
	@Override
	public BigDecimal getLastPrice(String ric)
	{
		return this.priceMap.get(ric).getLastPrice();
	}

	@Override
	public BigDecimal getMidPrice(String ric)
	{
		return this.priceMap.get(ric).getMidPrice();
	}

	@Override
	public BigDecimal getAskPrice(String ric)
	{
		return this.priceMap.get(ric).getAskPrice();
	}

	@Override
	public BigDecimal getBidPrice(String ric)
	{
		return this.priceMap.get(ric).getBidPrice();
	}
}
