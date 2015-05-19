package com.leon.rfq.simulation;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.ApplicationListener;

import com.leon.rfq.domains.OptionDetailImpl;

public final class PriceSimulatorImpl extends Thread implements PriceSimulator,
ApplicationListener, ApplicationEventPublisherAware
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
		private boolean isAwake = true;
		private final Random priceGenerator = new Random();
		private final Random changeGenerator = new Random();

		/**
		 * Constructor
		 * 
		 * @param priceMean							the mean price used for the ND random number generator
		 * @param priceVariance						the price variance used for the ND random number generator
		 * @throws IllegalArgumentException			if priceMean <= 0 || priceVariance <= 0.
		 */
		private PriceGenerator(double priceMean, double priceVariance)
		{
			if(priceMean <= 0.0)
				throw new IllegalArgumentException("priceMean");

			if(priceVariance <= 0.0)
				throw new IllegalArgumentException("priceVariance");

			this.priceMean = priceMean;
			this.priceVariance = priceVariance;
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

		private double getLatestPrice()
		{
			return this.priceMean + (this.priceGenerator.nextGaussian() * this.priceVariance);
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
		this.sleepDurationMin = sleepDurationMin;
		this.sleepDurationIncrement = sleepDurationIncrement;
	}

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher)
	{
		this.applicationEventPublisher = applicationEventPublisher;

	}

	@Override
	public void initialize()
	{
		this.start();
	}

	@Override
	public void onApplicationEvent(ApplicationEvent event)
	{
		if((event instanceof PriceSimulatorRequestEvent))
		{
			PriceSimulatorRequestEvent requestEvent = (PriceSimulatorRequestEvent) event;

			switch(requestEvent.getRequestType())
			{
			case ADD_UNDERLYING:
				add(requestEvent.getUnderlyingRIC(), requestEvent.getPriceMean(), requestEvent.getPriceVariance());
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
		else if(event instanceof TaggedRequestEvent)
		{
			TaggedRequestEvent taggedRequestEvent = (TaggedRequestEvent) event;

			if(logger.isDebugEnabled())
				logger.debug("Tagged request event received for request: " + taggedRequestEvent.getRequest());

			// TODO fix exception thrown here
			for(OptionDetailImpl optionLeg : taggedRequestEvent.getRequest().getLegs().getOptionDetailList())
				add(optionLeg.getUnderlyingRIC(), optionLeg.getUnderlyingPrice(), 0.5);
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
	 * @throws					IllegalArgumentException if underlyingRIC parameter is an empty string ||
	 * 							priceMean <= 0 || priceVariance <= 0.
	 */
	@Override
	public void add(String underlyingRIC, double priceMean, double priceVariance)
	{
		if(underlyingRIC.isEmpty())
			throw new IllegalArgumentException("underlyingRIC");

		if(priceMean <= 0.0)
			throw new IllegalArgumentException("priceMean");

		if(priceVariance <= 0.0)
			throw new IllegalArgumentException("priceVariance");

		if(this.priceMap.containsKey(underlyingRIC))
			return;

		this.priceMap.put(underlyingRIC, new PriceGenerator(priceMean, priceVariance));

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
		if(underlyingRIC.isEmpty())
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
		if(underlyingRIC.isEmpty())
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
		this.isSuspended = false;

		if(logger.isInfoEnabled())
			logger.info("All underlying have been awoken.");
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
		if(underlyingRIC.isEmpty())
			throw new IllegalArgumentException("underlyingRIC");

		if(!this.priceMap.containsKey(underlyingRIC))
			return;

		this.priceMap.get(underlyingRIC).awaken();

		if(logger.isInfoEnabled())
			logger.info("Underlying " + underlyingRIC + " has been awoken.");
	}

	@Override
	public void run()
	{
		if(logger.isInfoEnabled())
			logger.info("Price simulator starting continuous publishing...");

		while(this.isRunning)
		{
			if(!this.isSuspended)
			{
				for(Map.Entry<String, PriceGenerator> item : this.priceMap.entrySet())
				{
					PriceGenerator underlying = item.getValue();

					if(underlying.isAwake() && underlying.hasChanged())
					{
						double price = underlying.getLatestPrice();

						if(logger.isDebugEnabled())
							logger.debug("Publishing price: " + price + " for underlying: " + item.getKey());

						this.applicationEventPublisher.publishEvent(new PriceUpdateEvent(this, item.getKey(), price));
					}
				}
			}

			try
			{
				sleep(this.getNextSleepDuration());
			}
			catch(InterruptedException ie)
			{
				if(logger.isInfoEnabled())
					logger.info("Interruption exception raised. Terminating price simulator...");

				this.isRunning = false;
			}
		}

		if(logger.isInfoEnabled())
			logger.info("Price simulator activity terminated!");
	}

}
