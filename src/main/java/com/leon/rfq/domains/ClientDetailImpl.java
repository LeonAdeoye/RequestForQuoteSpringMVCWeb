package com.leon.rfq.domains;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.leon.rfq.common.EnumTypes.ClientTierEnum;

@XmlRootElement(name="ClientDetailImpl")
public final class ClientDetailImpl
{
	private static final Logger logger = LoggerFactory.getLogger(ClientDetailImpl.class);
	@NotNull(message="{client.validation.clientName.notNull}")
	@Size(min=1, max=45, message="{client.validation.clientName.size}")
	private String name;
	private int identifier;  // Using clientId does not work despite using ALL of the recommended MyBatis attributes.
	private boolean isValid;
	private ClientTierEnum tier;
	private String lastUpdatedBy;

	public ClientDetailImpl() {}

	public ClientDetailImpl(String name, int clientId, ClientTierEnum tier, boolean isValid, String createdByUser)
	{
		this.name = name;
		this.identifier = clientId;
		this.tier = tier;
		this.isValid = isValid;
		this.lastUpdatedBy = createdByUser;

		logger.debug("ClientDetailImpl object instantiated = > " +  this);
	}
	
	public ClientDetailImpl(String name, ClientTierEnum tier, boolean isValid, String createdByUser)
	{
		this.name = name;
		this.tier = tier;
		this.isValid = isValid;
		this.lastUpdatedBy = createdByUser;

		logger.debug("ClientDetailImpl object instantiated = > " +  this);
	}

	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getClientId()
	{
		return this.identifier;
	}

	public void setClientId(int clientId)
	{
		this.identifier = clientId;
	}

	public ClientTierEnum getTier()
	{
		return this.tier;
	}

	public void setTier(ClientTierEnum tier)
	{
		this.tier = tier;
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

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("ClientDetailImpl [name=");
		builder.append(this.name);
		builder.append(", identifier=");
		builder.append(this.identifier);
		builder.append(", isValid=");
		builder.append(this.isValid);
		builder.append(", tier=");
		builder.append(this.tier);
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
		result = (prime * result) + this.identifier;
		result = (prime * result) + (this.isValid ? 1231 : 1237);
		result = (prime * result)	+ ((this.lastUpdatedBy == null) ? 0 : this.lastUpdatedBy.hashCode());
		result = (prime * result) + ((this.name == null) ? 0 : this.name.hashCode());
		result = (prime * result) + ((this.tier == null) ? 0 : this.tier.hashCode());
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
		if (!(obj instanceof ClientDetailImpl))
		{
			return false;
		}
		ClientDetailImpl other = (ClientDetailImpl) obj;
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
		if (this.tier != other.tier)
		{
			return false;
		}
		return true;
	}
}