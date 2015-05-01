package com.leon.rfq.underlying;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnderlyingDetailImpl
{
	private static final Logger logger = LoggerFactory.getLogger(UnderlyingDetailImpl.class);
	private String ric;
	private String description;
	private boolean isValid;

	public UnderlyingDetailImpl()
	{
		this.ric = "";
		this.description = "";
		this.isValid = true;

		logger.debug("underlying instantiated = > " +  this);
	}
	
	public UnderlyingDetailImpl(String ric, String description, Boolean isValid)
	{
		this.ric = ric;
		this.description = description;
		this.isValid = isValid;

		logger.debug("underlying instantiated = > " +  this);
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

	@Override
	public String toString()
	{
		StringBuilder buf = new StringBuilder("RIC: ");
		buf.append(this.ric);
		buf.append(", Description: ");
		buf.append(this.description);
		buf.append(", Is valid: ");
		buf.append(this.isValid);

		return buf.toString();
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o)
			return true;

		if(!(o instanceof UnderlyingDetailImpl))
			return false;

		UnderlyingDetailImpl underlying = (UnderlyingDetailImpl) o;

		return this.ric.equals(underlying.ric) && this.description.equals(underlying.description)
				&& (this.isValid == underlying.isValid);
	}

	@Override
	public int hashCode()
	{
		int result = 17;
		result = (37 * result) + (this.ric == null ? 0 : this.ric.hashCode());
		result = (37 * result) + (this.description == null ? 0 : this.description.hashCode());
		result = (37 * result) + (this.isValid ? 0 : 1);
		return result;
	}
}
