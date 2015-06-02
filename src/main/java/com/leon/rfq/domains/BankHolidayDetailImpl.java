package com.leon.rfq.domains;

import java.time.LocalDate;

import javax.xml.bind.annotation.XmlRootElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.leon.rfq.common.EnumTypes.LocationEnum;

@XmlRootElement(name="BankHolidayDetailImpl")
public final class BankHolidayDetailImpl
{
	private static final Logger logger = LoggerFactory.getLogger(BankHolidayDetailImpl.class);
	
	private LocationEnum location;
	private LocalDate bankHolidayDate;
	private boolean isValid;
	private String lastUpdatedBy;
	private int identifier;
	
	public BankHolidayDetailImpl() {}

	public BankHolidayDetailImpl(LocationEnum location,	LocalDate bankHolidayDate, boolean isValid,
			String lastUpdatedBy, int identifier)
	{
		this.location = location;
		this.bankHolidayDate = bankHolidayDate;
		this.isValid = isValid;
		this.lastUpdatedBy = lastUpdatedBy;
		this.identifier = identifier;
	}
	
	public BankHolidayDetailImpl(LocationEnum location,	LocalDate bankHolidayDate, boolean isValid,
			String lastUpdatedBy)
	{
		this.location = location;
		this.bankHolidayDate = bankHolidayDate;
		this.isValid = isValid;
		this.lastUpdatedBy = lastUpdatedBy;
		this.identifier = -1;
	}

	public LocationEnum getLocation()
	{
		return this.location;
	}

	public void setLocation(LocationEnum location)
	{
		this.location = location;
	}

	public LocalDate getBankHolidayDate()
	{
		return this.bankHolidayDate;
	}

	public void setBankHolidayDate(LocalDate bankHolidayDate)
	{
		this.bankHolidayDate = bankHolidayDate;
	}

	public boolean isValid()
	{
		return this.isValid;
	}

	public void setValid(boolean isValid)
	{
		this.isValid = isValid;
	}

	public String getLastUpdatedBy()
	{
		return this.lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy)
	{
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public int getIdentifier()
	{
		return this.identifier;
	}

	public void setIdentifier(int identifier)
	{
		this.identifier = identifier;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = (prime * result)
				+ ((this.bankHolidayDate == null) ? 0 : this.bankHolidayDate.hashCode());
		result = (prime * result) + this.identifier;
		result = (prime * result) + (this.isValid ? 1231 : 1237);
		result = (prime * result)
				+ ((this.lastUpdatedBy == null) ? 0 : this.lastUpdatedBy.hashCode());
		result = (prime * result)
				+ ((this.location == null) ? 0 : this.location.hashCode());
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
		if (!(obj instanceof BankHolidayDetailImpl))
		{
			return false;
		}
		BankHolidayDetailImpl other = (BankHolidayDetailImpl) obj;
		if (this.bankHolidayDate == null)
		{
			if (other.bankHolidayDate != null)
			{
				return false;
			}
		} else if (!this.bankHolidayDate.equals(other.bankHolidayDate))
		{
			return false;
		}
		if (this.identifier != other.identifier)
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
		if (this.location != other.location)
		{
			return false;
		}
		return true;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("BankHolidayDetailImpl [location=");
		builder.append(this.location);
		builder.append(", bankHolidayDate=");
		builder.append(this.bankHolidayDate);
		builder.append(", isValid=");
		builder.append(this.isValid);
		builder.append(", lastUpdatedBy=");
		builder.append(this.lastUpdatedBy);
		builder.append(", identifier=");
		builder.append(this.identifier);
		builder.append("]");
		return builder.toString();
	}
	
		
}
