package com.leon.rfq.domains;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.leon.rfq.domains.EnumTypes.SideEnum;

public class OptionDetailImpl
{
	private static final Logger logger = LoggerFactory.getLogger(OptionDetailImpl.class);
	
	private int legId;
	private boolean isCall = true;
	private boolean isEuropean = true;

	private BigDecimal delta;
	private BigDecimal gamma;
	private BigDecimal theta;
	private BigDecimal vega;
	private BigDecimal rho;
	private BigDecimal premium;

	private String underlyingRIC;
	private BigDecimal underlyingPrice;
	private BigDecimal strike;

	private BigDecimal strikePercentage;
	private String description;
	private BigDecimal premiumPercentage;
	private int quantity;
	private BigDecimal yearsToExpiry;
	private LocalDate tradeDate;
	private LocalDate maturityDate;

	private BigDecimal daysToExpiry;
	private BigDecimal dayCountConvention;
	private BigDecimal volatility;
	private BigDecimal interestRate;
	private SideEnum side;
	private RequestDetailImpl parentRequest;

	public OptionDetailImpl() {}
	
	public OptionDetailImpl(SideEnum side, int quantity, boolean isCall, int legId, boolean isEuropean, RequestDetailImpl parentRequest)
	{
		this.side = side;
		this.parentRequest = parentRequest;
		this.legId = legId;
		this.isCall = isCall;
		this.quantity = quantity;
		this.isEuropean = isEuropean;
		
		if(logger.isDebugEnabled())
			logger.debug("Constructor instantiation of OptionDetailImpl: " + this.toString());
	}
	
	RequestDetailImpl getParent()
	{
		return this.parentRequest;
	}

	public BigDecimal getStrikePercentage()
	{
		return this.strikePercentage;
	}

	public void setStrikePercentage(BigDecimal strikePercentage)
	{
		this.strikePercentage = strikePercentage;
	}

	public LocalDate getMaturityDate()
	{
		return this.maturityDate;
	}

	public void setMaturityDate(LocalDate maturityDate)
	{
		this.maturityDate = maturityDate;
	}
	
	public LocalDate getTradeDate()
	{
		return this.tradeDate;
	}

	public void setTradeDate(LocalDate tradeDate)
	{
		this.tradeDate = tradeDate;
	}

	public BigDecimal getPremiumPercentage()
	{
		return this.premiumPercentage;
	}

	public void setPremiumPercentage(BigDecimal premiumPercentage)
	{
		this.premiumPercentage = premiumPercentage;
	}

	public BigDecimal getYearsToExpiry()
	{
		return this.yearsToExpiry;
	}

	public void setYearsToExpiry(BigDecimal yearsToExpiry)
	{
		this.yearsToExpiry = yearsToExpiry;
	}

	public int getQuantity()
	{
		return this.quantity;
	}

	public void setQuantity(int quantity)
	{
		this.quantity = quantity;
	}

	public String getDescription()
	{
		return this.description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public int getLegId()
	{
		return this.legId;
	}

	public void setLegId(int legId)
	{
		this.legId = legId;
	}

	public boolean getIsCall()
	{
		return this.isCall;
	}

	public void setIsCall(boolean isCall)
	{
		this.isCall = isCall;
	}

	public boolean getIsEuropean()
	{
		return this.isEuropean;
	}

	public void setIsEuropean(boolean isEuropean)
	{
		this.isEuropean = isEuropean;
	}

	public BigDecimal getDelta()
	{
		return this.delta;
	}

	public void setDelta(BigDecimal delta)
	{
		this.delta = delta;
	}

	public BigDecimal getGamma()
	{
		return this.gamma;
	}

	public void setGamma(BigDecimal gamma)
	{
		this.gamma = gamma;
	}

	public BigDecimal getTheta()
	{
		return this.theta;
	}

	public void setTheta(BigDecimal theta)
	{
		this.theta = theta;
	}

	public BigDecimal getVega()
	{
		return this.vega;
	}

	public void setVega(BigDecimal vega)
	{
		this.vega = vega;
	}

	public BigDecimal getRho()
	{
		return this.rho;
	}

	public void setRho(BigDecimal rho)
	{
		this.rho = rho;
	}

	public BigDecimal getPremium()
	{
		return this.premium;
	}

	public void setPremium(BigDecimal premium)
	{
		this.premium = premium;
	}

	public BigDecimal getInterestRate()
	{
		return this.interestRate;
	}

	public void setInterestRate(BigDecimal interestRate)
	{
		this.interestRate = interestRate;
	}

	public BigDecimal getVolatility()
	{
		return this.volatility;
	}

	public void setVolatility(BigDecimal volatility)
	{
		this.volatility = volatility;
	}

	public BigDecimal getUnderlyingPrice()
	{
		return this.underlyingPrice;
	}

	public void setUnderlyingPrice(BigDecimal underlyingPrice)
	{
		this.underlyingPrice = underlyingPrice;
	}

	public BigDecimal getStrike()
	{
		return this.strike;
	}

	public void setStrike(BigDecimal strike)
	{
		this.strike = strike;
	}

	public BigDecimal getDaysToExpiry()
	{
		return this.daysToExpiry;
	}

	public void setDaysToExpiry(BigDecimal daysToExpiry)
	{
		this.daysToExpiry = daysToExpiry;
	}

	public BigDecimal getDayCountConvention()
	{
		return this.dayCountConvention;
	}

	public void setDayCountConvention(BigDecimal dayCountConvention)
	{
		this.dayCountConvention = dayCountConvention;
	}

	public String getUnderlyingRIC()
	{
		return this.underlyingRIC;
	}

	public void setUnderlyingRIC(String underlyingRIC)
	{
		this.underlyingRIC = underlyingRIC;
	}

	public SideEnum getSide()
	{
		return this.side;
	}

	public void setSide(SideEnum side)
	{
		this.side = side;
	}

	@Override
	public String toString()
	{
		StringBuilder buf = new StringBuilder(" Leg ID: ");
		buf.append(this.legId);
		buf.append(", Is call: ");
		buf.append(this.isCall);
		buf.append(", Is european: ");
		buf.append(this.isEuropean);
		buf.append(", Underlying RIC: ");
		buf.append(this.underlyingRIC);
		buf.append(", Side: ");
		buf.append(this.side);
		buf.append(", Description: ");
		buf.append(this.description);
		buf.append(", Quantity: ");
		buf.append(this.quantity);

		buf.append(", Underlying price: ");
		buf.append(this.underlyingPrice);
		buf.append(", Interest rate: ");
		buf.append(this.interestRate);
		buf.append(", Volatility: ");
		buf.append(this.volatility);
		buf.append(", Days to expiry: ");
		buf.append(this.daysToExpiry);
		buf.append(", Years to expiry: ");
		buf.append(this.yearsToExpiry);
		buf.append(", Maturity date: ");
		buf.append(this.maturityDate);
		buf.append(", trade date: ");
		buf.append(this.tradeDate);
		buf.append(", Day count convention: ");
		buf.append(this.dayCountConvention);
		buf.append(", Strike: ");
		buf.append(this.strike);
		buf.append(", Strike Percentage: ");
		buf.append(this.strikePercentage);
		buf.append(", Strike Percentage: ");
		buf.append(this.strikePercentage);


		buf.append(", Delta: ");
		buf.append(this.delta);
		buf.append(", Gamma: ");
		buf.append(this.gamma);
		buf.append(", Vega: ");
		buf.append(this.vega);
		buf.append(", Theta: ");
		buf.append(this.theta);
		buf.append(", Rho: ");
		buf.append(this.rho);
		buf.append(", Premium: ");
		buf.append(this.premium);
		buf.append(", Premium Percentage: ");
		buf.append(this.premiumPercentage);


		return buf.toString();
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = (prime
				* result)
				+ ((this.dayCountConvention == null) ? 0 : this.dayCountConvention
						.hashCode());
		result = (prime * result)
				+ ((this.daysToExpiry == null) ? 0 : this.daysToExpiry.hashCode());
		result = (prime * result) + ((this.delta == null) ? 0 : this.delta.hashCode());
		result = (prime * result)
				+ ((this.description == null) ? 0 : this.description.hashCode());
		result = (prime * result) + ((this.gamma == null) ? 0 : this.gamma.hashCode());
		result = (prime * result)
				+ ((this.interestRate == null) ? 0 : this.interestRate.hashCode());
		result = (prime * result) + (this.isCall ? 1231 : 1237);
		result = (prime * result) + (this.isEuropean ? 1231 : 1237);
		result = (prime * result) + this.legId;
		result = (prime * result)
				+ ((this.maturityDate == null) ? 0 : this.maturityDate.hashCode());
		result = (prime * result) + ((this.premium == null) ? 0 : this.premium.hashCode());
		result = (prime
				* result)
				+ ((this.premiumPercentage == null) ? 0 : this.premiumPercentage
						.hashCode());
		result = (prime * result) + this.quantity;
		result = (prime * result) + ((this.rho == null) ? 0 : this.rho.hashCode());
		result = (prime * result) + ((this.side == null) ? 0 : this.side.hashCode());
		result = (prime * result) + ((this.strike == null) ? 0 : this.strike.hashCode());
		result = (prime
				* result)
				+ ((this.strikePercentage == null) ? 0 : this.strikePercentage.hashCode());
		result = (prime * result) + ((this.theta == null) ? 0 : this.theta.hashCode());
		result = (prime * result)
				+ ((this.tradeDate == null) ? 0 : this.tradeDate.hashCode());
		result = (prime * result)
				+ ((this.underlyingPrice == null) ? 0 : this.underlyingPrice.hashCode());
		result = (prime * result)
				+ ((this.underlyingRIC == null) ? 0 : this.underlyingRIC.hashCode());
		result = (prime * result) + ((this.vega == null) ? 0 : this.vega.hashCode());
		result = (prime * result)
				+ ((this.volatility == null) ? 0 : this.volatility.hashCode());
		result = (prime * result)
				+ ((this.yearsToExpiry == null) ? 0 : this.yearsToExpiry.hashCode());
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
		if (!(obj instanceof OptionDetailImpl))
		{
			return false;
		}
		OptionDetailImpl other = (OptionDetailImpl) obj;
		if (this.dayCountConvention == null)
		{
			if (other.dayCountConvention != null)
			{
				return false;
			}
		} else if (!this.dayCountConvention.equals(other.dayCountConvention))
		{
			return false;
		}
		if (this.daysToExpiry == null)
		{
			if (other.daysToExpiry != null)
			{
				return false;
			}
		} else if (!this.daysToExpiry.equals(other.daysToExpiry))
		{
			return false;
		}
		if (this.delta == null)
		{
			if (other.delta != null)
			{
				return false;
			}
		} else if (!this.delta.equals(other.delta))
		{
			return false;
		}
		if (this.description == null)
		{
			if (other.description != null)
			{
				return false;
			}
		} else if (!this.description.equals(other.description))
		{
			return false;
		}
		if (this.gamma == null)
		{
			if (other.gamma != null)
			{
				return false;
			}
		} else if (!this.gamma.equals(other.gamma))
		{
			return false;
		}
		if (this.interestRate == null)
		{
			if (other.interestRate != null)
			{
				return false;
			}
		} else if (!this.interestRate.equals(other.interestRate))
		{
			return false;
		}
		if (this.isCall != other.isCall)
		{
			return false;
		}
		if (this.isEuropean != other.isEuropean)
		{
			return false;
		}
		if (this.legId != other.legId)
		{
			return false;
		}
		if (this.maturityDate == null)
		{
			if (other.maturityDate != null)
			{
				return false;
			}
		} else if (!this.maturityDate.equals(other.maturityDate))
		{
			return false;
		}
		if (this.premium == null)
		{
			if (other.premium != null)
			{
				return false;
			}
		} else if (!this.premium.equals(other.premium))
		{
			return false;
		}
		if (this.premiumPercentage == null)
		{
			if (other.premiumPercentage != null)
			{
				return false;
			}
		} else if (!this.premiumPercentage.equals(other.premiumPercentage))
		{
			return false;
		}
		if (this.quantity != other.quantity)
		{
			return false;
		}
		if (this.rho == null)
		{
			if (other.rho != null)
			{
				return false;
			}
		} else if (!this.rho.equals(other.rho))
		{
			return false;
		}
		if (this.side == null)
		{
			if (other.side != null)
			{
				return false;
			}
		} else if (!this.side.equals(other.side))
		{
			return false;
		}
		if (this.strike == null)
		{
			if (other.strike != null)
			{
				return false;
			}
		} else if (!this.strike.equals(other.strike))
		{
			return false;
		}
		if (this.strikePercentage == null)
		{
			if (other.strikePercentage != null)
			{
				return false;
			}
		} else if (!this.strikePercentage.equals(other.strikePercentage))
		{
			return false;
		}
		if (this.theta == null)
		{
			if (other.theta != null)
			{
				return false;
			}
		} else if (!this.theta.equals(other.theta))
		{
			return false;
		}
		if (this.tradeDate == null)
		{
			if (other.tradeDate != null)
			{
				return false;
			}
		} else if (!this.tradeDate.equals(other.tradeDate))
		{
			return false;
		}
		if (this.underlyingPrice == null)
		{
			if (other.underlyingPrice != null)
			{
				return false;
			}
		} else if (!this.underlyingPrice.equals(other.underlyingPrice))
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
		if (this.vega == null)
		{
			if (other.vega != null)
			{
				return false;
			}
		} else if (!this.vega.equals(other.vega))
		{
			return false;
		}
		if (this.volatility == null)
		{
			if (other.volatility != null)
			{
				return false;
			}
		} else if (!this.volatility.equals(other.volatility))
		{
			return false;
		}
		if (this.yearsToExpiry == null)
		{
			if (other.yearsToExpiry != null)
			{
				return false;
			}
		} else if (!this.yearsToExpiry.equals(other.yearsToExpiry))
		{
			return false;
		}
		return true;
	}

	}
