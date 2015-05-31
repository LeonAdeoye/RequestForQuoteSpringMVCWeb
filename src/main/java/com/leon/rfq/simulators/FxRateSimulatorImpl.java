package com.leon.rfq.simulators;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.ApplicationListener;


public final class FxRateSimulatorImpl extends Thread implements FxRateSimulator, ApplicationListener, ApplicationEventPublisherAware
{
	private static final Logger logger = LoggerFactory.getLogger(FxRateSimulatorImpl.class);
	private final Map<String, FxRateGenerator> FxRateMap  = new HashMap<>();
	private ApplicationEventPublisher applicationEventPublisher;
	private final int sleepDurationMin;
	private final int sleepDurationIncrement;
	private final Random sleepDurationGenerator = new Random();
	private boolean isRunning = true;
	private boolean isSuspended = false;

	private class FxRateGenerator
	{
		private final double FxRateMean;
		private final double FxRateVariance;
		private boolean isAwake = true;
		private final Random FxRateGenerator = new Random();
		private final Random changeGenerator = new Random();

		private FxRateGenerator(double FxRateMean, double FxRateVariance)
		{
			this.FxRateMean = FxRateMean;
			this.FxRateVariance = FxRateVariance;
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

		private double getLatestFxRate()
		{
			return this.FxRateMean + (this.FxRateGenerator.nextGaussian() * this.FxRateVariance);
		}

		private boolean hasChanged()
		{
			return this.changeGenerator.nextInt(3) == 2;
		}
	}

	/**
	 * Returns the next sleeping duration.
	 *
	 * @return	the randomly generated sleep duration
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
	public FxRateSimulatorImpl(int sleepDurationMin, int sleepDurationIncrement)
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
		//TODO
	}

	/**
	 * Terminates the FxRate simulator stopping all FxRate generation.
	 */
	@Override
	public void terminate()
	{
		if(logger.isInfoEnabled())
			logger.info("Terminating FxRate simulator...");

		this.isRunning = false;
	}

	/**
	 * Adds the currency as the lookup key to the FxRate publishing map.
	 *
	 * @param  currency			the currency used as key to add it.
	 * @param  FxRateMean		the mean FxRate used random FxRate generator with normal distribution.
	 * @param  FxRateVariance	the variance used random FxRate generator with normal distribution.
	 * @throws					IllegalArgumentException if currency parameter is an empty string ||
	 * 							FxRateMean <= 0 || FxRateVariance <= 0.
	 */
	@Override
	public void add(String currency, double FxRateMean, double FxRateVariance)
	{
		if(currency.isEmpty())
			throw new IllegalArgumentException("currency");
		if(FxRateMean <= 0.0)
			throw new IllegalArgumentException("FxRateMean");
		if(FxRateVariance <= 0.0)
			throw new IllegalArgumentException("FxRateVariance");

		if(this.FxRateMap.containsKey(currency))
			return;

		this.FxRateMap.put(currency, new FxRateGenerator(FxRateMean, FxRateVariance));

		if(logger.isInfoEnabled())
			logger.info("Added underlying " + currency + " to the FxRate publishing map");
	}

	/**
	 * Removes the currency using it as the lookup key from the FxRate publisher map.
	 *
	 * @param  currency						the currency used as key to remove it.
	 * @throws IllegalArgumentException		if currency parameter is an empty string.
	 */
	@Override
	public void remove(String currency)
	{
		if(currency.isEmpty())
			throw new IllegalArgumentException("currency");

		if(!this.FxRateMap.containsKey(currency))
			return;

		this.FxRateMap.remove(currency);

		if(logger.isInfoEnabled())
			logger.info("Removed currency " + currency + " from the FxRate publishing map");
	}

	/**
	 * Suspends all FxRate generation by all currencies.
	 */
	@Override
	public void suspendAll()
	{
		this.isSuspended = true;

		if(logger.isInfoEnabled())
			logger.info("All currencies have been suspended.");
	}

	/**
	 * Suspends the underlying product from generating FxRates using the underlying RIC as the lookup key.
	 *
	 * @param	currency					the currency used as key to suspend it.
	 * @throws	IllegalArgumentException	if currency parameter is an empty string.
	 */
	@Override
	public void suspend(String currency)
	{
		if(currency.isEmpty())
			throw new IllegalArgumentException("currency");

		if(!this.FxRateMap.containsKey(currency))
			return;

		this.FxRateMap.get(currency).suspend();

		if(logger.isInfoEnabled())
			logger.info("Currency " + currency + " has been suspended.");
	}

	/**
	 * Restart all FxRate generation by all currencies.
	 */
	@Override
	public void awakenAll()
	{
		this.isSuspended = false;

		if(logger.isInfoEnabled())
			logger.info("All currencies have been awoken.");
	}

	/**
	 * Restart the FxRate generation of the specified currency.
	 *
	 * @param	currency					the currency used as key to awaken it.
	 * @throws	IllegalArgumentException 	if currency parameter is an empty string.
	 */
	@Override
	public void awaken(String currency)
	{
		if(currency.isEmpty())
			throw new IllegalArgumentException("currency");

		if(!this.FxRateMap.containsKey(currency))
			return;

		this.FxRateMap.get(currency).awaken();

		if(logger.isInfoEnabled())
			logger.info("Underlying " + currency + " has been awoken.");
	}

	@Override
	public void run()
	{
		if(logger.isInfoEnabled())
			logger.info("FxRate simulator starting continuous publishing...");

		while(this.isRunning)
		{
			if(!this.isSuspended)
			{
				for(Map.Entry<String, FxRateGenerator> item : this.FxRateMap.entrySet())
				{
					FxRateGenerator currency = item.getValue();

					if(currency.isAwake() && currency.hasChanged())
					{
						double FxRate = currency.getLatestFxRate();

						if(logger.isDebugEnabled())
							logger.debug("Publishing FxRate: " + FxRate + " for currency: " + item.getKey());

						//TODO
						//this.applicationEventPublisher.publishEvent();
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
					logger.info("Interruption exception raised. Terminating FxRate simulator...");

				this.isRunning = false;
			}
		}

		if(logger.isInfoEnabled())
			logger.info("FxRate simulator activity terminated!");
	}

}
