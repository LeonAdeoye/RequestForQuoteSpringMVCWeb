package com.leon.rfq.domains;

import javax.xml.bind.annotation.XmlRootElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement(name="HolidayImpl")
public final class HolidayDetailImpl
{
	private static final Logger logger = LoggerFactory.getLogger(HolidayDetailImpl.class);
	private String location;
	private String holidayDate;

	public HolidayDetailImpl(String location, String holidayDate)
	{
		this.location = location;
		this.holidayDate = holidayDate;

		logger.debug("HolidayDetailImpl object instantiated = > " +  this);
	}

	public HolidayDetailImpl() {}

	/**
	 * @return the location
	 */
	public String getLocation()
	{
		return this.location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(String location)
	{
		this.location = location;
	}

	/**
	 * @return the holidayDate
	 */
	public String getHolidayDate()
	{
		return this.holidayDate;
	}

	/**
	 * @param holidayDate the holidayDate to set
	 */
	public void setHolidayDate(String holidayDate)
	{
		this.holidayDate = holidayDate;
	}

	/**
	 * @return the logger
	 */
	public static Logger getLogger()
	{
		return logger;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("HolidayImpl [location=");
		builder.append(this.location);
		builder.append(", holidayDate=");
		builder.append(this.holidayDate);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = (prime * result)
				+ ((this.holidayDate == null) ? 0 : this.holidayDate.hashCode());
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
		if (!(obj instanceof HolidayDetailImpl))
		{
			return false;
		}
		HolidayDetailImpl other = (HolidayDetailImpl) obj;
		if (this.holidayDate == null)
		{
			if (other.holidayDate != null)
			{
				return false;
			}
		} else if (!this.holidayDate.equals(other.holidayDate))
		{
			return false;
		}
		if (this.location == null)
		{
			if (other.location != null)
			{
				return false;
			}
		} else if (!this.location.equals(other.location))
		{
			return false;
		}
		return true;
	}

}
