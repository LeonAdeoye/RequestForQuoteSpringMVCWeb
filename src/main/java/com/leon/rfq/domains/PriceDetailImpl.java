package com.leon.rfq.domains;

import java.math.BigDecimal;
import java.util.Optional;

public class PriceDetailImpl
{
	private Optional<BigDecimal> askPrice;
	private Optional<BigDecimal> bidPrice;
	private Optional<BigDecimal> midPrice;
	private Optional<BigDecimal> closePrice;
	private Optional<BigDecimal> openPrice;
	
	public PriceDetailImpl() {}
	
	public Optional<BigDecimal> getAskPrice()
	{
		return this.askPrice;
	}

	public void setAskPrice(Optional<BigDecimal> askPrice)
	{
		this.askPrice = askPrice;
	}
	
	public Optional<BigDecimal> getBidPrice()
	{
		return this.bidPrice;
	}

	public void setBidPrice(Optional<BigDecimal> bidPrice)
	{
		this.bidPrice = bidPrice;
	}

	public Optional<BigDecimal> getMidPrice()
	{
		return this.midPrice;
	}

	public void setMidPrice(Optional<BigDecimal> midPrice)
	{
		this.midPrice = midPrice;
	}

	public Optional<BigDecimal> getClosePrice()
	{
		return this.closePrice;
	}

	public void setClosePrice(Optional<BigDecimal> closePrice)
	{
		this.closePrice = closePrice;
	}

	public Optional<BigDecimal> getOpenPrice()
	{
		return this.openPrice;
	}

	public void setOpenPrice(Optional<BigDecimal> openPrice)
	{
		this.openPrice = openPrice;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("PriceDetailImpl [askPrice=");
		builder.append(this.askPrice);
		builder.append(", bidPrice=");
		builder.append(this.bidPrice);
		builder.append(", midPrice=");
		builder.append(this.midPrice);
		builder.append(", closePrice=");
		builder.append(this.closePrice);
		builder.append(", openPrice=");
		builder.append(this.openPrice);
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
				+ ((this.closePrice == null) ? 0 : this.closePrice.hashCode());
		result = (prime * result)
				+ ((this.midPrice == null) ? 0 : this.midPrice.hashCode());
		result = (prime * result)
				+ ((this.openPrice == null) ? 0 : this.openPrice.hashCode());
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
		if (this.closePrice == null)
		{
			if (other.closePrice != null)
			{
				return false;
			}
		} else if (!this.closePrice.equals(other.closePrice))
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
		if (this.openPrice == null)
		{
			if (other.openPrice != null)
			{
				return false;
			}
		} else if (!this.openPrice.equals(other.openPrice))
		{
			return false;
		}
		return true;
	};
	
	
}
