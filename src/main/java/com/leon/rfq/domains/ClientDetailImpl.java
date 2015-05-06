package com.leon.rfq.domains;

import javax.xml.bind.annotation.XmlRootElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement(name="ClientDetailImpl")
public final class ClientDetailImpl
{
	private static final Logger logger = LoggerFactory.getLogger(ClientDetailImpl.class);
	private String name;
	private int identifier;
	private boolean isValid;
	private String tier;

	public ClientDetailImpl() {}

	public ClientDetailImpl(String name, int identifier, String tier, boolean isValid)
	{
		this.name = name;
		this.identifier = identifier;
		this.tier = tier;
		this.isValid = isValid;

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

	public int getIdentifier()
	{
		return this.identifier;
	}

	public void setIdentifier(int identifier)
	{
		this.identifier = identifier;
	}

	public String getTier()
	{
		return this.tier;
	}

	public void setTier(String tier)
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

	@Override
	public String toString()
	{
		StringBuilder buf = new StringBuilder("Name: ");
		buf.append(this.name);
		buf.append(", Identifier: ");
		buf.append(this.identifier);
		buf.append(", Tier: ");
		buf.append(this.tier);
		buf.append(", Is Valid: ");
		buf.append(this.isValid ? "TRUE" : "FALSE");
		return buf.toString();
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = (prime * result) + this.identifier;
		result = (prime * result) + (this.isValid ? 1231 : 1237);
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
		if (this.tier == null)
		{
			if (other.tier != null)
			{
				return false;
			}
		} else if (!this.tier.equals(other.tier))
		{
			return false;
		}
		return true;
	}
	
}