package com.leon.rfq.simulators;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Package private
class PriceGeneratorImpl
{
	private static final Logger logger = LoggerFactory.getLogger(PriceGeneratorImpl.class);
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
	 * @param priceSpread						the price spread between the ask and bid price.
	 * @throws IllegalArgumentException			if priceMean <= 0 || priceVariance <= 0.
	 */
	public PriceGeneratorImpl(double priceMean, double priceVariance, double priceSpread)
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

	public void suspend()
	{
		this.isAwake = false;
	}

	public void awaken()
	{
		this.isAwake = true;
	}

	public boolean isAwake()
	{
		return this.isAwake;
	}

	public double getLastPrice()
	{
		// Use a random number generator to decide between the ask and bid.
		if(this.lastPriceGenerator.nextInt(1) == 0)
			return getAskPrice();
		else
			return getBidPrice();
	}
	
	public void generate()
	{
		// Generate the mid price only if change random number generator returns 2.
		if(this.changeGenerator.nextInt(3) == 2)
			this.midPrice = this.priceMean + (this.priceGenerator.nextGaussian() * this.priceVariance);
	}
	
	public double getMidPrice()
	{
		return this.midPrice;
	}
	
	public double getAskPrice()
	{
		// The Ask price is the price being asked or being offered to BUY something.
		// You buy something at higher price than you sell something.
		// ASK price > MID price
		return getMidPrice() + (this.priceSpread/2);
	}
	
	public double getBidPrice()
	{
		// BID price < MID price
		return getMidPrice() - (this.priceSpread/2);
	}
}

