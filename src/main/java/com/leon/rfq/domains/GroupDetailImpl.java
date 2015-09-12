package com.leon.rfq.domains;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GroupDetailImpl
{
	private static final Logger logger = LoggerFactory.getLogger(GroupDetailImpl.class);
	private String name;
	private String description;
	private boolean isValid;
	private String lastUpdatedBy;
	
	public String getName()
	{
		return this.name;
	}
	
	public void setName(String name)
	{
		this.name = name;
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
	
	public String getLastUpdatedBy()
	{
		return this.lastUpdatedBy;
	}
	
	public void setLastUpdatedBy(String lastUpdatedBy)
	{
		this.lastUpdatedBy = lastUpdatedBy;
	}
	
	public GroupDetailImpl() {}

	public GroupDetailImpl(String name, String description, boolean isValid, String lastUpdatedBy)
	{
		super();
		this.name = name;
		this.description = description;
		this.isValid = isValid;
		this.lastUpdatedBy = lastUpdatedBy;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("GroupDetailImpl [name=");
		builder.append(this.name);
		builder.append(", description=");
		builder.append(this.description);
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
		result = (prime * result)	+ ((this.description == null) ? 0 : this.description.hashCode());
		result = (prime * result) + (this.isValid ? 1231 : 1237);
		result = (prime * result)	+ ((this.lastUpdatedBy == null) ? 0 : this.lastUpdatedBy.hashCode());
		result = (prime * result) + ((this.name == null) ? 0 : this.name.hashCode());
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
		if (!(obj instanceof GroupDetailImpl))
		{
			return false;
		}
		GroupDetailImpl other = (GroupDetailImpl) obj;
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
		if (this.name == null)
		{
			if (other.name != null)
			{
				return false;
			}
		} else if (!this.name.equals(other.name))
		{
			return false;
		}
		return true;
	}
}
