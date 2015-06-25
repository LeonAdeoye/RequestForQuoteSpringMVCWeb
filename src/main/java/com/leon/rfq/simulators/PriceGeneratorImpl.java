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
	private final BigDecimal priceMean;
	private final BigDecimal priceVariance;
	private final BigDecimal halfOfSpread;
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
	public PriceGeneratorImpl(BigDecimal priceMean, BigDecimal priceVariance, BigDecimal priceSpread)
	{
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
		
		this.priceMean = priceMean;
		this.priceVariance = priceVariance;
		this.halfOfSpread = priceSpread.divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);
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
		return this.lastPriceGenerator.nextInt(2) == 0;
	}
	
	public boolean hasPriceChanged()
	{
		return this.changeGenerator.nextInt(3) == 2;
	}
	
	public PriceDetailImpl generate(String underlyingRIC)
	{
		if((underlyingRIC == null) || underlyingRIC.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("underlyingRIC is an invalid argument");
			
			throw new IllegalArgumentException("underlyingRIC is an invalid argument");
		}
		
		this.midPrice = BigDecimal.valueOf(this.priceMean.doubleValue()
				+ (this.priceGenerator.nextGaussian()
				* this.priceVariance.doubleValue()));
			
		return new PriceDetailImpl(underlyingRIC,
			this.midPrice.add(this.halfOfSpread).setScale(2, RoundingMode.HALF_UP),
			this.midPrice.subtract(this.halfOfSpread).setScale(2, RoundingMode.HALF_UP),
			this.midPrice.setScale(2, RoundingMode.HALF_UP),
			isAskPriceLast() ?	this.midPrice.add(this.halfOfSpread).setScale(2, RoundingMode.HALF_UP)
			: this.midPrice.subtract(this.halfOfSpread).setScale(2, RoundingMode.HALF_UP));
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("PriceGeneratorImpl [priceMean=");
		builder.append(this.priceMean);
		builder.append(", priceVariance=");
		builder.append(this.priceVariance);
		builder.append(", halfOfSpread=");
		builder.append(this.halfOfSpread);
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
		result = (prime * result)
				+ ((this.halfOfSpread == null) ? 0 : this.halfOfSpread.hashCode());
		result = (prime * result) + (this.isAwake ? 1231 : 1237);
		result = (prime * result)
				+ ((this.midPrice == null) ? 0 : this.midPrice.hashCode());
		result = (prime * result)
				+ ((this.priceMean == null) ? 0 : this.priceMean.hashCode());
		result = (prime * result)
				+ ((this.priceVariance == null) ? 0 : this.priceVariance.hashCode());
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
		if (this.halfOfSpread == null)
		{
			if (other.halfOfSpread != null)
			{
				return false;
			}
		} else if (!this.halfOfSpread.equals(other.halfOfSpread))
		{
			return false;
		}
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
		if (this.priceMean == null)
		{
			if (other.priceMean != null)
			{
				return false;
			}
		} else if (!this.priceMean.equals(other.priceMean))
		{
			return false;
		}
		if (this.priceVariance == null)
		{
			if (other.priceVariance != null)
			{
				return false;
			}
		} else if (!this.priceVariance.equals(other.priceVariance))
		{
			return false;
		}
		return true;
	}
}

