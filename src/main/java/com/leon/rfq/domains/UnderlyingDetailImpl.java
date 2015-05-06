package com.leon.rfq.domains;

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

		logger.debug("underlying instantiated = > " +  this);
	}
	
	public UnderlyingDetailImpl(String ric, String description, Boolean isValid, String lastUpdatedBy)
	{
		this.ric = ric;
		this.description = description;
		this.isValid = isValid;
		this.lastUpdatedBy = lastUpdatedBy;

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
		buf.append(", LastUpdatedBy: ");
		buf.append(this.lastUpdatedBy);
		
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
				&& this.lastUpdatedBy.equals(underlying.lastUpdatedBy) && (this.isValid == underlying.isValid);
	}

	@Override
	public int hashCode()
	{
		int result = 17;
		result = (37 * result) + (this.ric == null ? 0 : this.ric.hashCode());
		result = (37 * result) + (this.description == null ? 0 : this.description.hashCode());
		result = (37 * result) + (this.lastUpdatedBy == null ? 0 : this.lastUpdatedBy.hashCode());
		result = (37 * result) + (this.isValid ? 0 : 1);
		return result;
	}
}
