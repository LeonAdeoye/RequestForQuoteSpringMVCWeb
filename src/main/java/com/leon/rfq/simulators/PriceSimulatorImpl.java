package com.leon.rfq.simulators;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.ApplicationListener;

import com.leon.rfq.events.PriceSimulatorRequestEvent;
import com.leon.rfq.events.PriceUpdateEvent;

public final class PriceSimulatorImpl implements PriceSimulator,
ApplicationListener<PriceSimulatorRequestEvent>, ApplicationEventPublisherAware
{
	private static final Logger logger = LoggerFactory.getLogger(PriceSimulatorImpl.class);
	private final Map<String, PriceGenerator> priceMap  = new HashMap<>();
	private ApplicationEventPublisher applicationEventPublisher;
	private final int sleepDurationMin;
	private final int sleepDurationIncrement;
	private final Random sleepDurationGenerator = new Random();
	private boolean isRunning = true;
	private boolean isSuspended = false;

	private class PriceGenerator
	{
		private final double priceMean;
		private final double priceVariance;
		private final double priceSpread;
		private boolean isAwake = true;
		private double midPrice;
		private final Random priceGenerator = new Random();
		private final Random changeGenerator = new Random();
		private final Random lastPriceGenerator = new Random();

		/**
		 * Constructor
		 * 
		 * @param priceMean							the mean price used for the ND random number generator
		 * @param priceVariance						the price variance used for the ND random number generator
		 * @throws IllegalArgumentException			if priceMean <= 0 || priceVariance <= 0.
		 */
		private PriceGenerator(double priceMean, double priceVariance, double priceSpread)
		{
			if(priceMean <= 0.0)
			{
				if(logger.isErrorEnabled())
					logger.error("priceMean argument is invalid");
				
				throw new IllegalArgumentException("priceMean argument is invalid");
			}

			if(priceVariance <= 0.0)
			{
				if(logger.isErrorEnabled())
					logger.error("priceVariance argument is invalid");
				
				throw new IllegalArgumentException("priceVariance argument is invalid");
			}
			
			if(priceSpread <= 0.0)
			{
				if(logger.isErrorEnabled())
					logger.error("priceSpread argument is invalid");
				
				throw new IllegalArgumentException("priceSpread argument is invalid");
			}

			this.priceMean = priceMean;
			this.priceVariance = priceVariance;
			this.priceSpread = priceSpread;
		}

		private void suspend()
		{
			this.isAwake = false;
		}

		private void awaken()
		{
			this.isAwake = true;
		}

		private boolean isAwake()
		{
			return this.isAwake;
		}

		private double getLastPrice()
		{
			if(this.lastPriceGenerator.nextInt(1) == 0)
				return getAskPrice();
			else
				return getBidPrice();
		}
		
		private void generate()
		{
			this.midPrice = this.priceMean + (this.priceGenerator.nextGaussian() * this.priceVariance);
		}
		
		private double getMidPrice()
		{
			// TODO test
			if(this.midPrice == 0)
				generate();
			
			return this.midPrice;
		}
		
		private double getAskPrice()
		{
			return getMidPrice() + (this.priceSpread/2);
		}
		
		private double getBidPrice()
		{
			return getMidPrice() - (this.priceSpread/2);
		}
		
		private boolean hasChanged()
		{
			return this.changeGenerator.nextInt(3) == 2;
		}
	}

	/**
	 * Returns the next sleeping duration.
	 *
	 * @returns	the randomly generated sleep duration
	 */
	private int getNextSleepDuration()
	{
		return this.sleepDurationMin + this.sleepDurationGenerator.nextInt(this.sleepDurationIncrement);
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

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher)
	{
		this.applicationEventPublisher = applicationEventPublisher;
	}

	@Override
	@PostConstruct
	public void initialize()
	{
		if(logger.isInfoEnabled())
			logger.info("Price simulator starting continuous publishing...");
		
		try
		{
			while(this.isRunning)
			{
				if(!this.isSuspended)
				{
					for(Map.Entry<String, PriceGenerator> item : this.priceMap.entrySet())
					{
						PriceGenerator priceGenerator = item.getValue();
	
						if(priceGenerator.isAwake() && priceGenerator.hasChanged())
						{
							priceGenerator.generate();
							
							//TODO mid, ask, bid
							double price = priceGenerator.getLastPrice();
	
							if(logger.isDebugEnabled())
								logger.debug("Publishing price: " + price + " for underlying: " + item.getKey());
	
							this.applicationEventPublisher.publishEvent(new PriceUpdateEvent(this, item.getKey(), price));
							
							Thread.sleep(this.getNextSleepDuration());
						}
					}
				}
				Thread.sleep(this.getNextSleepDuration());
			}
		}
		catch(InterruptedException ie)
		{
			if(logger.isInfoEnabled())
				logger.info("Interruption exception raised. Terminating price simulator...");

			this.isRunning = false;
		}
		
		if(logger.isInfoEnabled())
			logger.info("Price simulator activity terminated!");
	}

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
		case AWAKEN_ALL:
			awakenAll();
			break;
		}
	}

	/**
	 * Terminates the price simulator stopping all price generation.
	 */
	@Override
	public void terminate()
	{
		if(logger.isInfoEnabled())
			logger.info("Terminating price simulator...");

		this.isRunning = false;
	}

	/**
	 * Adds the underlying product using the underlying RIC as the lookup key to the price publishing map.
	 *
	 * @param  underlyingRIC	the RIC of the underlying product used as key to add it.
	 * @param  priceMean		the mean price used random price generator with normal distribution.
	 * @param  priceVariance	the variance used random price generator with normal distribution.
	 * @throws					IllegalArgumentException if underlyingRIC parameter is an empty or null string ||
	 * 							priceMean <= 0 || priceVariance <= 0.
	 */
	@Override
	public void add(String underlyingRIC, double priceMean, double priceVariance, double priceSpread)
	{
		if((underlyingRIC == null) || underlyingRIC.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("underlyingRIC argument is invalid");
			
			throw new IllegalArgumentException("underlyingRIC argument is invalid");
		}

		if(priceMean <= 0.0)
		{
			if(logger.isErrorEnabled())
				logger.error("priceMean argument is invalid");
			
			throw new IllegalArgumentException("priceMean argument is invalid");
		}

		if(priceVariance <= 0.0)
			throw new IllegalArgumentException("priceVariance");
		
		if(priceSpread <= 0.0)
			throw new IllegalArgumentException("priceVariance");

		if(this.priceMap.containsKey(underlyingRIC))
		{
			if(!this.priceMap.get(underlyingRIC).isAwake())
				this.priceMap.get(underlyingRIC).awaken();
			
			return;
		}

		this.priceMap.put(underlyingRIC, new PriceGenerator(priceMean, priceVariance, priceSpread));

		if(logger.isInfoEnabled())
			logger.info("Added underlying " + underlyingRIC + " to the price publishing map");
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
			throw new IllegalArgumentException("underlyingRIC");

		if(!this.priceMap.containsKey(underlyingRIC))
			return;

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
			throw new IllegalArgumentException("underlyingRIC");

		if(!this.priceMap.containsKey(underlyingRIC))
			return;

		this.priceMap.get(underlyingRIC).suspend();

		if(logger.isInfoEnabled())
			logger.info("Underlying " + underlyingRIC + " has been suspended.");
	}

	/**
	 * Restart all price generation by all underlyings.
	 */
	@Override
	public void awakenAll()
	{
		for(Map.Entry<String, PriceGenerator> entry : this.priceMap.entrySet())
			entry.getValue().awaken();
		
		this.isSuspended = false;

		if(logger.isInfoEnabled())
			logger.info("All underlyings have been awoken.");
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
			throw new IllegalArgumentException("underlyingRIC");

		if(!this.priceMap.containsKey(underlyingRIC))
			return;

		this.priceMap.get(underlyingRIC).awaken();

		if(logger.isInfoEnabled())
			logger.info("Underlying " + underlyingRIC + " has been awoken.");
	}
}
