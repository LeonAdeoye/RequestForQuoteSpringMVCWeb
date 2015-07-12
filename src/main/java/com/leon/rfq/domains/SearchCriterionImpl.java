package com.leon.rfq.domains;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SearchCriterionImpl
{
	private static final Logger logger = LoggerFactory.getLogger(SearchCriterionImpl.class);
	private String controlName;
	private String controlValue;
	private String owner;
	private Boolean isPrivate;
	private String searchKey;

	public SearchCriterionImpl(String owner, String key, String controlName, String controlValue, Boolean isPrivate, Boolean isFilter)
	{
		this.owner = owner;
		this.searchKey = key;
		this.controlName = controlName;
		this.controlValue = controlValue;
		this.isPrivate = isPrivate;

		if(logger.isDebugEnabled())
		logger.debug("Criterion instantiated = > " +  this);
	}

	public SearchCriterionImpl() {}

	public SearchCriterionImpl(String owner, String searchKey)
	{
		this.owner = owner;
		this.searchKey = searchKey;
	}

	public String getOwner()
	{
		return this.owner;
	}

	public void setOwner(String owner)
	{
		this.owner = owner;
	}

	public String getSearchKey()
	{
		return this.searchKey;
	}

	public void setSearchKey(String searchKey)
	{
		this.searchKey = searchKey;
	}

	public String getControlName()
	{
		return this.controlName;
	}

	public void setControlName(String controlName)
	{
		this.controlName = controlName;
	}

	public String getControlValue()
	{
		return this.controlValue;
	}

	public void setControlValue(String controlValue)
	{
		this.controlValue = controlValue;
	}

	public Boolean getIsPrivate()
	{
		return this.isPrivate;
	}

	public void setIsPrivate(Boolean isPrivate)
	{
		this.isPrivate = isPrivate;
	}

	@Override
	public String toString()
	{
		StringBuilder buf = new StringBuilder("Owner: ");
		buf.append(this.owner);
		buf.append(", searchKey: ");
		buf.append(this.searchKey);
		buf.append(", Criterion Key: ");
		buf.append(this.controlName);
		buf.append(", Criterion Value: ");
		buf.append(this.controlValue);
		buf.append(", IsPrivate: ");
		buf.append(this.isPrivate);
		return buf.toString();
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = (prime * result)
				+ ((this.controlName == null) ? 0 : this.controlName.hashCode());
		result = (prime * result)
				+ ((this.controlValue == null) ? 0 : this.controlValue.hashCode());
		result = (prime * result)
				+ ((this.isPrivate == null) ? 0 : this.isPrivate.hashCode());
		result = (prime * result) + ((this.searchKey == null) ? 0 : this.searchKey.hashCode());
		result = (prime * result) + ((this.owner == null) ? 0 : this.owner.hashCode());
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
		if (!(obj instanceof SearchCriterionImpl))
		{
			return false;
		}
		SearchCriterionImpl other = (SearchCriterionImpl) obj;
		if (this.controlName == null)
		{
			if (other.controlName != null)
			{
				return false;
			}
		} else if (!this.controlName.equals(other.controlName))
		{
			return false;
		}
		if (this.controlValue == null)
		{
			if (other.controlValue != null)
			{
				return false;
			}
		} else if (!this.controlValue.equals(other.controlValue))
		{
			return false;
		}
		if (this.isPrivate == null)
		{
			if (other.isPrivate != null)
			{
				return false;
			}
		} else if (!this.isPrivate.equals(other.isPrivate))
		{
			return false;
		}
		if (this.searchKey == null)
		{
			if (other.searchKey != null)
			{
				return false;
			}
		} else if (!this.searchKey.equals(other.searchKey))
		{
			return false;
		}
		if (this.owner == null)
		{
			if (other.owner != null)
			{
				return false;
			}
		} else if (!this.owner.equals(other.owner))
		{
			return false;
		}
		return true;
	}

	
}
