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
	
	public BankHolidayDetailImpl() {}
		
	public BankHolidayDetailImpl(LocationEnum location,
			LocalDate bankHolidayDate, boolean isValid, String lastUpdatedBy)
	{
		super();
		this.location = location;
		this.bankHolidayDate = bankHolidayDate;
		this.isValid = isValid;
		this.lastUpdatedBy = lastUpdatedBy;
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

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = (prime * result)
				+ ((this.bankHolidayDate == null) ? 0 : this.bankHolidayDate.hashCode());
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
}
