package com.leon.rfq.domains;

import java.math.BigDecimal;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PriceDetailImpl
{
	private static final Logger logger = LoggerFactory.getLogger(PriceDetailImpl.class);
	
	private final Optional<BigDecimal> askPrice;
	private final Optional<BigDecimal> bidPrice;
	private final Optional<BigDecimal> midPrice;
	private final Optional<BigDecimal> lastPrice;
	private final String underlyingRIC;
	
	public PriceDetailImpl(String underlyingRIC, BigDecimal askPrice, BigDecimal bidPrice, BigDecimal midPrice, BigDecimal lastPrice)
	{
		if((underlyingRIC == null) || underlyingRIC.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("underlyingRIC argument is invalid");
			
			throw new IllegalArgumentException("underlyingRIC argument is invalid");
		}
		
		this.underlyingRIC = underlyingRIC;
		this.askPrice = Optional.ofNullable(askPrice);
		this.bidPrice = Optional.ofNullable(bidPrice);
		this.midPrice = Optional.ofNullable(midPrice);
		this.lastPrice = Optional.ofNullable(lastPrice);
	}

	public BigDecimal getAskPrice()
	{
		return this.askPrice.orElse(BigDecimal.ZERO);
	}

	public BigDecimal getBidPrice()
	{
		return this.bidPrice.orElse(BigDecimal.ZERO);
	}

	public BigDecimal getMidPrice()
	{
		return this.midPrice.orElse(BigDecimal.ZERO);
	}

	public BigDecimal getLastPrice()
	{
		return this.lastPrice.orElse(BigDecimal.ZERO);
	}

	public String getUnderlyingRIC()
	{
		return this.underlyingRIC;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("PriceDetailImpl [askPrice=");
		builder.append(this.askPrice.orElse(BigDecimal.ZERO));
		builder.append(", bidPrice=");
		builder.append(this.bidPrice.orElse(BigDecimal.ZERO));
		builder.append(", midPrice=");
		builder.append(this.midPrice.orElse(BigDecimal.ZERO));
		builder.append(", lastPrice=");
		builder.append(this.lastPrice.orElse(BigDecimal.ZERO));
		builder.append(", underlyingRIC=");
		builder.append(this.underlyingRIC);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = (prime * result)
				+ ((this.askPrice == null) ? 0 : this.askPrice.hashCode());
		result = (prime * result)
				+ ((this.bidPrice == null) ? 0 : this.bidPrice.hashCode());
		result = (prime * result)
				+ ((this.lastPrice == null) ? 0 : this.lastPrice.hashCode());
		result = (prime * result)
				+ ((this.midPrice == null) ? 0 : this.midPrice.hashCode());
		result = (prime * result)
				+ ((this.underlyingRIC == null) ? 0 : this.underlyingRIC.hashCode());
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
		if (!(obj instanceof PriceDetailImpl))
		{
			return false;
		}
		PriceDetailImpl other = (PriceDetailImpl) obj;
		if (this.askPrice == null)
		{
			if (other.askPrice != null)
			{
				return false;
			}
		} else if (!this.askPrice.equals(other.askPrice))
		{
			return false;
		}
		if (this.bidPrice == null)
		{
			if (other.bidPrice != null)
			{
				return false;
			}
		} else if (!this.bidPrice.equals(other.bidPrice))
		{
			return false;
		}
		if (this.lastPrice == null)
		{
			if (other.lastPrice != null)
			{
				return false;
			}
		} else if (!this.lastPrice.equals(other.lastPrice))
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
		if (this.underlyingRIC == null)
		{
			if (other.underlyingRIC != null)
			{
				return false;
			}
		} else if (!this.underlyingRIC.equals(other.underlyingRIC))
		{
			return false;
		}
		return true;
	}
	
}
