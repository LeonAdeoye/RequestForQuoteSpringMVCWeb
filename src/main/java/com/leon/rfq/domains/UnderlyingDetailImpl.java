package com.leon.rfq.domains;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.ibatis.type.Alias;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement
@Alias("UnderlyingDetailImpl")
public class UnderlyingDetailImpl
{
	private static final Logger logger = LoggerFactory.getLogger(UnderlyingDetailImpl.class);
	
	@NotNull(message="{underlying.validation.ric.notNull}")
	@Size(min=1, max=10, message="{underlying.validation.ric.size}")
	private String ric;
	
	@NotNull(message="{underlying.validation.description.notNull}")
	@Size(min=1, max=45, message="{underlying.validation.description.size}")
	private String description;
	
	private BigDecimal spread;
	private BigDecimal referencePrice;
	private BigDecimal simulationPriceVariance;
	private BigDecimal dividendYield;
	
	private boolean isValid;
	
	private String lastUpdatedBy;

	public String getLastUpdatedBy()
	{
		return this.lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy)
	{
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public UnderlyingDetailImpl()
	{
		this.ric = "";
		this.description = "";
		this.isValid = true;
		this.lastUpdatedBy = "";
		this.referencePrice = BigDecimal.TEN;
		this.simulationPriceVariance = BigDecimal.ONE;
		this.spread = BigDecimal.ONE;
	}
	
	public UnderlyingDetailImpl(String ric, String description, BigDecimal referencePrice,
			BigDecimal simulationPriceVariance, BigDecimal spread, Boolean isValid, String lastUpdatedBy)
	{
		this.ric = ric;
		this.description = description;
		this.isValid = isValid;
		this.lastUpdatedBy = lastUpdatedBy;
		this.referencePrice = referencePrice;
		this.simulationPriceVariance = simulationPriceVariance;
		this.spread = spread;
	}

	public String getRic()
	{
		return this.ric;
	}

	public void setRic(String ric)
	{
		this.ric = ric;
	}

	public String getDescription()
	{
		return this.description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public boolean getIsValid()
	{
		return this.isValid;
	}

	public void setIsValid(boolean isValid)
	{
		this.isValid = isValid;
	}

	public BigDecimal getSpread()
	{
		return this.spread;
	}

	public void setSpread(BigDecimal spread)
	{
		this.spread = spread;
	}

	public BigDecimal getReferencePrice()
	{
		return this.referencePrice;
	}

	public void setReferencePrice(BigDecimal referencePrice)
	{
		this.referencePrice = referencePrice;
	}

	public BigDecimal getSimulationPriceVariance()
	{
		return this.simulationPriceVariance;
	}

	public void setSimulationPriceVariance(BigDecimal simulationPriceVariance)
	{
		this.simulationPriceVariance = simulationPriceVariance;
	}

	public BigDecimal getDividendYield()
	{
		return this.dividendYield;
	}

	public void setDividendYield(BigDecimal dividendYield)
	{
		this.dividendYield = dividendYield;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("UnderlyingDetailImpl [ric=");
		builder.append(this.ric);
		builder.append(", description=");
		builder.append(this.description);
		builder.append(", spread=");
		builder.append(this.spread);
		builder.append(", referencePrice=");
		builder.append(this.referencePrice);
		builder.append(", simulationPriceVariance=");
		builder.append(this.simulationPriceVariance);
		builder.append(", dividendYield=");
		builder.append(this.dividendYield);
		builder.append(", isValid=");
		builder.append(this.isValid);
		builder.append(", lastUpdatedBy=");
		builder.append(this.lastUpdatedBy);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = (prime * result)
				+ ((this.description == null) ? 0 : this.description.hashCode());
		result = (prime * result)
				+ ((this.dividendYield == null) ? 0 : this.dividendYield.hashCode());
		result = (prime * result) + (this.isValid ? 1231 : 1237);
		result = (prime * result)
				+ ((this.lastUpdatedBy == null) ? 0 : this.lastUpdatedBy.hashCode());
		result = (prime * result)
				+ ((this.referencePrice == null) ? 0 : this.referencePrice.hashCode());
		result = (prime * result)
				+ ((this.simulationPriceVariance == null) ? 0 : this.simulationPriceVariance.hashCode());
		result = (prime * result) + ((this.ric == null) ? 0 : this.ric.hashCode());
		result = (prime * result) + ((this.spread == null) ? 0 : this.spread.hashCode());
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
		if (!(obj instanceof UnderlyingDetailImpl))
		{
			return false;
		}
		UnderlyingDetailImpl other = (UnderlyingDetailImpl) obj;
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
		if (this.dividendYield == null)
		{
			if (other.dividendYield != null)
			{
				return false;
			}
		} else if (!this.dividendYield.equals(other.dividendYield))
		{
			return false;
		}
		if (this.isValid != other.isValid)
		{
			return false;
		}
		if (this.lastUpdatedBy == null)
		{
			if (other.lastUpdatedBy != null)
			{
				return false;
			}
		} else if (!this.lastUpdatedBy.equals(other.lastUpdatedBy))
		{
			return false;
		}
		if (this.referencePrice == null)
		{
			if (other.referencePrice != null)
			{
				return false;
			}
		} else if (!this.referencePrice.equals(other.referencePrice))
		{
			return false;
		}
		if (this.simulationPriceVariance == null)
		{
			if (other.simulationPriceVariance != null)
			{
				return false;
			}
		} else if (!this.simulationPriceVariance.equals(other.simulationPriceVariance))
		{
			return false;
		}
		if (this.ric == null)
		{
			if (other.ric != null)
			{
				return false;
			}
		} else if (!this.ric.equals(other.ric))
		{
			return false;
		}
		if (this.spread == null)
		{
			if (other.spread != null)
			{
				return false;
			}
		} else if (!this.spread.equals(other.spread))
		{
			return false;
		}
		return true;
	}
	
}
