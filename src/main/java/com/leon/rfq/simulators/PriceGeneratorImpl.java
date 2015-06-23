package com.leon.rfq.simulators;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.leon.rfq.domains.PriceDetailImpl;

// Package private
class PriceGeneratorImpl
{
	private static final Logger logger = LoggerFactory.getLogger(PriceGeneratorImpl.class);
	private final double priceMean;
	private final double priceVariance;
	private final double priceSpread;
	private boolean isAwake = true;
	private BigDecimal midPrice;
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
	
	private boolean isAskPriceLast()
	{
		return this.lastPriceGenerator.nextInt(1) == 0;
	}
	
	public PriceDetailImpl generate(String underlyingRIC)
	{
		// Generate the mid price only if the change random number generator returns 2.
		if(this.changeGenerator.nextInt(3) == 2)
			this.midPrice = BigDecimal.valueOf(this.priceMean + (this.priceGenerator.nextGaussian() * this.priceVariance));
		
		BigDecimal halfOfSpread = BigDecimal.valueOf(this.priceSpread)
				.divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);
		
		return new PriceDetailImpl(underlyingRIC, this.midPrice.add(halfOfSpread),
				this.midPrice.subtract(halfOfSpread), this.midPrice,
				isAskPriceLast() ?	this.midPrice.add(halfOfSpread) : this.midPrice.subtract(halfOfSpread));
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("PriceGeneratorImpl [priceMean=");
		builder.append(this.priceMean);
		builder.append(", priceVariance=");
		builder.append(this.priceVariance);
		builder.append(", priceSpread=");
		builder.append(this.priceSpread);
		builder.append(", isAwake=");
		builder.append(this.isAwake);
		builder.append(", midPrice=");
		builder.append(this.midPrice);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = (prime * result) + (this.isAwake ? 1231 : 1237);
		result = (prime * result)
				+ ((this.midPrice == null) ? 0 : this.midPrice.hashCode());
		long temp;
		temp = Double.doubleToLongBits(this.priceMean);
		result = (prime * result) + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(this.priceSpread);
		result = (prime * result) + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(this.priceVariance);
		result = (prime * result) + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (!(obj instanceof PriceGeneratorImpl))
		{
			return false;
		}
		PriceGeneratorImpl other = (PriceGeneratorImpl) obj;
		if (this.isAwake != other.isAwake)
		{
			return false;
		}
		if (this.midPrice == null)
		{
			if (other.midPrice != null)
			{
				return false;
			}
		} else if (!this.midPrice.equals(other.midPrice))
		{
			return false;
		}
		if (Double.doubleToLongBits(this.priceMean) != Double
				.doubleToLongBits(other.priceMean))
		{
			return false;
		}
		if (Double.doubleToLongBits(this.priceSpread) != Double
				.doubleToLongBits(other.priceSpread))
		{
			return false;
		}
		if (Double.doubleToLongBits(this.priceVariance) != Double
				.doubleToLongBits(other.priceVariance))
		{
			return false;
		}
		return true;
	}
	
	
}

