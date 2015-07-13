package com.leon.rfq.domains;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SearchCriterionImpl
{
	private static final Logger logger = LoggerFactory.getLogger(SearchCriterionImpl.class);
	private String name;
	private String value;
	private String owner;
	private Boolean isPrivate;
	private String searchKey;

	public SearchCriterionImpl(String owner, String key, String name, String value, Boolean isPrivate)
	{
		this.owner = owner;
		this.searchKey = key;
		this.name = name;
		this.value = value;
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

	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getValue()
	{
		return this.value;
	}

	public void setValue(String value)
	{
		this.value = value;
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
		buf.append(", Name: ");
		buf.append(this.name);
		buf.append(", Value: ");
		buf.append(this.value);
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
				+ ((this.name == null) ? 0 : this.name.hashCode());
		result = (prime * result)
				+ ((this.value == null) ? 0 : this.value.hashCode());
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
		if (this.value == null)
		{
			if (other.value != null)
			{
				return false;
			}
		} else if (!this.value.equals(other.value))
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
