package com.leon.rfq.services;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.leon.rfq.domains.PriceDetailImpl;

@Service
public class PriceServiceImpl implements PriceService
{
	private static final Logger logger = LoggerFactory.getLogger(PriceServiceImpl.class);
	private final Map<String, PriceDetailImpl> priceMap = new ConcurrentHashMap<>(20, 0.9f, 5);
	private boolean isRunning = true;
	private ExecutorService executorService;
	
	@Resource(name="priceUpdateBlockingQueue")
	private BlockingQueue<PriceDetailImpl> priceUpdateBlockingQueue;
	private boolean cleanUpDone = false;
	
	@Override
	@PostConstruct
	public void initialize()
	{
		if(logger.isInfoEnabled())
			logger.info("Starting price service...");
		
		if(this.priceUpdateBlockingQueue == null)
		{
			if(logger.isErrorEnabled())
				logger.error("The resource blocking queue has NOT been initialised properly. Terminating price service.");
			
			return;
		}
		
		if(logger.isDebugEnabled())
			logger.debug("The resource blocking queue has been initialised properly.");
		
		this.executorService = Executors.newSingleThreadExecutor();
		this.cleanUpDone = false;
		this.isRunning = true;
		
		this.executorService.submit(() ->
		{
			int count = 0;
			try
			{
				while(this.isRunning)
				{
					PriceDetailImpl priceUpdate = this.priceUpdateBlockingQueue.take();
			
					if(logger.isDebugEnabled() && ((++count%200)==0))
						logger.debug("Mudulo 200th price update taken from head of blocking queue: "  + priceUpdate);
					
					this.priceMap.put(priceUpdate.getUnderlyingRIC(), priceUpdate);
				}
			}
			catch(InterruptedException ie)
			{
				if(logger.isErrorEnabled())
					logger.error("Terminating price service because an interruption exception has been raised");
			}
		});
	}
	
	/**
	 * Terminates the price service.
	 */
	@PreDestroy
	@Override
	public void terminate()
	{
		this.cleanUpDone = true;
		this.isRunning = false;
		this.priceMap.clear();
		this.priceUpdateBlockingQueue.clear();
		this.executorService.shutdownNow();
		
		if(logger.isInfoEnabled())
			logger.info("Termination of price service has been completed successfully.");
	}
	
	/**
	 * Calls terminate method if not called previously.
	 */
	@Override
	protected void finalize() throws Exception
	{
		if(!this.cleanUpDone )
			this.terminate();
	}
	
	@Override
	public BigDecimal getLastPrice(String ric)
	{
		if((ric == null) || ric.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("ric argument is invalid");
			
			throw new IllegalArgumentException("ric argument is invalid");
		}
		
		ReentrantLock lock = new ReentrantLock();
		try
		{
			lock.lock();
			
			if(this.priceMap.containsKey(ric))
				return this.priceMap.get(ric).getLastPrice();
			
			return BigDecimal.ZERO;
		}
		finally
		{
			lock.unlock();
		}
	}

	@Override
	public BigDecimal getMidPrice(String ric)
	{
		if((ric == null) || ric.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("ric argument is invalid");
			
			throw new IllegalArgumentException("ric argument is invalid");
		}
		
		ReentrantLock lock = new ReentrantLock();
		try
		{
			lock.lock();
			
			if(this.priceMap.containsKey(ric))
				return this.priceMap.get(ric).getMidPrice();
			
			return BigDecimal.ZERO;
		}
		finally
		{
			lock.unlock();
		}
	}

	@Override
	public BigDecimal getAskPrice(String ric)
	{
		if((ric == null) || ric.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("ric argument is invalid");
			
			throw new IllegalArgumentException("ric argument is invalid");
		}
		
		ReentrantLock lock = new ReentrantLock();
		try
		{
			lock.lock();
			
			if(this.priceMap.containsKey(ric))
				return this.priceMap.get(ric).getAskPrice();
			
			return BigDecimal.ZERO;
		}
		finally
		{
			lock.unlock();
		}
	}

	@Override
	public BigDecimal getBidPrice(String ric)
	{
		if((ric == null) || ric.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("ric argument is invalid");
			
			throw new IllegalArgumentException("ric argument is invalid");
		}
		
		ReentrantLock lock = new ReentrantLock();
		try
		{
			lock.lock();
			
			if(this.priceMap.containsKey(ric))
				return this.priceMap.get(ric).getBidPrice();
			
			return BigDecimal.ZERO;
		}
		finally
		{
			lock.unlock();
		}
	}

	@Override
	public PriceDetailImpl getAllPrices(String ric)
	{
		if((ric == null) || ric.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("ric argument is invalid");
			
			throw new IllegalArgumentException("ric argument is invalid");
		}
		
		ReentrantLock lock = new ReentrantLock();
		try
		{
			lock.lock();
			
			if(this.priceMap.containsKey(ric))
				return this.priceMap.get(ric);
			
			return new PriceDetailImpl(ric);
		}
		finally
		{
			lock.unlock();
		}
	}
}
