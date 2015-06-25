package com.leon.rfq.simulators;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import com.leon.rfq.domains.PriceDetailImpl;
import com.leon.rfq.events.PriceSimulatorRequestEvent;
import com.leon.rfq.services.UnderlyingService;

@Service
public final class PriceSimulatorImpl implements PriceSimulator, ApplicationListener<PriceSimulatorRequestEvent>
{
	private static final Logger logger = LoggerFactory.getLogger(PriceSimulatorImpl.class);
	// Only two threads should be using this concurrent map
	private final Map<String, PriceGeneratorImpl> priceMap  = new ConcurrentHashMap<>(20, 0.9f, 2);
	
	private final int sleepDurationMin;
	private final int sleepDurationIncrement;
	private final Random sleepDurationGenerator = new Random();
	private boolean isRunning = true;
	private boolean isSuspended = false;
	private final ExecutorService executorService = Executors.newSingleThreadExecutor();
	
	@Resource(name="priceUpdateBlockingQueue")
	private BlockingQueue<PriceDetailImpl> priceUpdateBlockingQueue;
	
	@Autowired(required=true)
	private UnderlyingService underlyingService;
	
	/**
	 * Returns the next sleeping duration.
	 *
	 * @returns	the randomly generated sleep duration
	 */
	private int getNextSleepDuration()
	{
		return this.sleepDurationMin + this.sleepDurationGenerator.nextInt(this.sleepDurationIncrement);
	}
	
	public PriceSimulatorImpl()
	{
		this.sleepDurationMin = 500;
		this.sleepDurationIncrement = 1000;
	}

	/**
	 * Constructor.
	 *
	 * @param  sleepDurationMin 		the minimum sleep duration in between each set of publishing events.
	 * @param  sleepDurationIncrement	the sleep duration increment between each set of publishing events.
	 */
	public PriceSimulatorImpl(int sleepDurationMin, int sleepDurationIncrement)
	{
		if(sleepDurationMin <= 0.0)
		{
			if(logger.isErrorEnabled())
				logger.error("sleepDurationMin argument is invalid");
			
			throw new IllegalArgumentException("sleepDurationMin argument is invalid");
		}

		if(sleepDurationIncrement <= 0.0)
		{
			if(logger.isErrorEnabled())
				logger.error("sleepDurationIncrement argument is invalid");
			
			throw new IllegalArgumentException("sleepDurationIncrement argument is invalid");
		}
		
		this.sleepDurationMin = sleepDurationMin;
		this.sleepDurationIncrement = sleepDurationIncrement;
	}
	
	private void prime()
	{

		if(logger.isInfoEnabled())
			logger.info("Priming price simulator cache...");
		
		this.underlyingService.getAllFromCacheOnly().forEach(underlying -> add(underlying.getRic(),
				underlying.getReferencePrice(), underlying.getSimulationPriceVariance(), underlying.getSpread()));
	}

	@Override
	@PostConstruct
	public final void initialize()
	{
		if(logger.isInfoEnabled())
			logger.info("Starting price simulator...");
		
		if(this.priceUpdateBlockingQueue == null)
		{
			if(logger.isErrorEnabled())
				logger.error("The resource blocking queue has NOT been initialised properly. Terminating price simulator.");
			
			return;
		}
		
		if(logger.isDebugEnabled())
			logger.debug("The resource blocking queue has been initialised properly.");
		
		prime();
		
		this.executorService.submit(() ->
		{
			try
			{
				while(this.isRunning)
				{
					if(!this.isSuspended)
					{
						this.priceMap.forEach((underlyingRIC, priceGenerator) ->
						{
							if(priceGenerator.isAwake() && priceGenerator.hasPriceChanged())
							{
								try
								{
									this.priceUpdateBlockingQueue.put(priceGenerator.generate(underlyingRIC));
									
									Thread.sleep(PriceSimulatorImpl.this.getNextSleepDuration());
								}
								catch (Exception e)
								{
									if(logger.isErrorEnabled())
										logger.error("Exception raised: " + e + ", message: " + e.getLocalizedMessage()
												+ ", stack trace: " + Arrays.toString(e.getStackTrace()));
								}
							}
						});
					}
					
					Thread.sleep(PriceSimulatorImpl.this.getNextSleepDuration());
				}
			}
			catch(InterruptedException ie)
			{
				if(logger.isErrorEnabled())
					logger.error("Terminating simulation because an interruption exception has been raised.");
			}
		});
	}

	/**
	 * Dispatches event to appropriate handler method.
	 * @param  requestEvent			the event to be dispatched.
	 */
	@Override
	public void onApplicationEvent(PriceSimulatorRequestEvent requestEvent)
	{
		switch(requestEvent.getRequestType())
		{
			case ADD_UNDERLYING:
				add(requestEvent.getUnderlyingRIC(), requestEvent.getPriceMean(), requestEvent.getPriceVariance(), requestEvent.getPriceSpread());
				break;
			case REMOVE_UNDERLYING:
				remove(requestEvent.getUnderlyingRIC());
				break;
			case SUSPEND_UNDERLYING:
				suspend(requestEvent.getUnderlyingRIC());
				break;
			case AWAKEN_UNDERLYING:
				awaken(requestEvent.getUnderlyingRIC());
				break;
			case SUSPEND_ALL:
				suspendAll();
				break;
			case REMOVE_ALL:
				removeAll();
				break;
			case AWAKEN_ALL:
				awakenAll();
				break;
		}
	}

	/**
	 * Terminates the price simulator stopping all price generation.
	 */
	@PreDestroy
	@Override
	public void terminate()
	{
		this.isRunning = false;
		this.priceMap.clear();
		this.priceUpdateBlockingQueue.clear();
		this.priceUpdateBlockingQueue = null;
		this.executorService.shutdownNow();
		
		if(logger.isInfoEnabled())
			logger.info("Termination of price simulator has been completed successfully.");
	}

	/**
	 * Adds the underlying product using the underlying RIC as the lookup key to the price publishing map.
	 *
	 * @param  underlyingRIC	the RIC of the underlying product used as key to add it.
	 * @param  priceMean		the mean price used random price generator with normal distribution.
	 * @param  priceVariance	the variance used random price generator with normal distribution.
	 * @param  priceSpread		the spread between the ask and bid prices.
	 * @throws					IllegalArgumentException if underlyingRIC parameter is an empty or null string ||
	 * 							priceMean <= 0 || priceVariance <= 0 || priceSpread <= 0.
	 */
	@Override
	public void add(String underlyingRIC, BigDecimal priceMean, BigDecimal priceVariance, BigDecimal priceSpread)
	{
		if((underlyingRIC == null) || underlyingRIC.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("underlyingRIC argument is invalid");
			
			throw new IllegalArgumentException("underlyingRIC argument is invalid");
		}

		if(priceMean.compareTo(BigDecimal.ZERO) <= 0)
		{
			if(logger.isErrorEnabled())
				logger.error("priceMean argument is invalid");
			
			throw new IllegalArgumentException("priceMean argument is invalid");
		}

		if(priceVariance.compareTo(BigDecimal.ZERO) <= 0)
		{
			if(logger.isErrorEnabled())
				logger.error("priceVariance argument is invalid");
			
			throw new IllegalArgumentException("priceVariance argument is invalid");
		}
		
		if(priceSpread.compareTo(BigDecimal.ZERO) <= 0)
		{
			if(logger.isErrorEnabled())
				logger.error("priceSpread argument is invalid");
			
			throw new IllegalArgumentException("priceSpread argument is invalid");
		}
		
		this.priceMap.put(underlyingRIC, new PriceGeneratorImpl(priceMean, priceVariance, priceSpread));

		if(logger.isInfoEnabled())
			logger.info("Added underlying " + underlyingRIC + " to the price publishing map with price mean: " +
					priceMean + ", priceVariance: " + priceVariance + ", and priceSpread: " + priceSpread);
	}

	/**
	 * Removes the underlying product using the underlying RIC as the lookup key from the price publisher map.
	 *
	 * @param  underlyingRIC	the RIC of the underlying product used as key to remove it.
	 * @throws					IllegalArgumentException if underlyingRIC parameter is an empty string.
	 */
	@Override
	public void remove(String underlyingRIC)
	{
		if((underlyingRIC == null) || underlyingRIC.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("underlyingRIC argument is invalid");
			
			throw new IllegalArgumentException("underlyingRIC argument is invalid");
		}
		
		this.priceMap.remove(underlyingRIC);
		
		if(logger.isInfoEnabled())
			logger.info("Removed underlying " + underlyingRIC + " from the price publishing map");
	}

	/**
	 * Suspends all price generation by all underlyings.
	 */
	@Override
	public void suspendAll()
	{
		this.isSuspended = true;

		if(logger.isInfoEnabled())
			logger.info("All underlying have been suspended.");
	}

	/**
	 * Suspends the underlying product from generating prices using the underlying RIC as the lookup key.
	 *
	 * @param  underlyingRIC	the RIC of the underlying product used as key to suspend it.
	 * @throws					IllegalArgumentException if underlyingRIC parameter is an empty string.
	 */
	@Override
	public void suspend(String underlyingRIC)
	{
		if((underlyingRIC == null) || underlyingRIC.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("underlyingRIC argument is invalid");
			
			throw new IllegalArgumentException("underlyingRIC argument is invalid");
		}
		
		ReentrantLock lock = new ReentrantLock();
		
		try
		{
			lock.lock();

			if(this.priceMap.containsKey(underlyingRIC))
				this.priceMap.get(underlyingRIC).suspend();
		}
		finally
		{
			lock.unlock();
		}

		if(logger.isInfoEnabled())
			logger.info("Underlying " + underlyingRIC + " has been suspended.");
	}
	
	/**
	 * Removes all underlyings from the price generation map.
	 */
	@Override
	public void removeAll()
	{
		this.isSuspended = true;
		
		this.priceMap.clear();
		
		this.isSuspended = false;
		
		if(logger.isInfoEnabled())
			logger.info("All underlyings have been removed.");
	}

	/**
	 * Restart all price generation by all underlyings.
	 */
	@Override
	public void awakenAll()
	{
		ReentrantLock lock = new ReentrantLock();
		
		try
		{
			lock.lock();
		
			this.priceMap.values().stream().forEach(PriceGeneratorImpl::awaken);
			
			this.isSuspended = false;
		}
		finally
		{
			lock.unlock();
		}

		if(logger.isInfoEnabled())
			logger.info("All underlyings in the price generation map have been awoken.");
	}

	/**
	 * Restart the price generation of the specified underlying product.
	 *
	 * @param  underlyingRIC	the RIC of the underlying product used as key to awaken it.
	 * @throws					IllegalArgumentException if underlyingRIC parameter is an empty string.
	 */
	@Override
	public void awaken(String underlyingRIC)
	{
		if((underlyingRIC == null) || underlyingRIC.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("underlyingRIC argument is invalid");
			
			throw new IllegalArgumentException("underlyingRIC argument is invalid");
		}
		
		ReentrantLock lock = new ReentrantLock();
		
		try
		{
			lock.lock();

			if(this.priceMap.containsKey(underlyingRIC))
				this.priceMap.get(underlyingRIC).awaken();
		}
		finally
		{
			lock.unlock();
		}

		if(logger.isInfoEnabled())
			logger.info("Underlying " + underlyingRIC + " has been awoken.");
	}
}
