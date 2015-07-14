package com.leon.rfq.domains;

import javax.xml.bind.annotation.XmlRootElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement(name="GroupDetailImpl")
public class GroupDetailImpl
{
	private static final Logger logger = LoggerFactory.getLogger(GroupDetailImpl.class);
	private int groupId;
	private String groupName;
	private boolean isValid;
	
	public GroupDetailImpl() {}
		
	public GroupDetailImpl(int groupId, String groupName, boolean isValid)
	{
		this.groupId = groupId;
		this.groupName = groupName;
		this.isValid = isValid;
	}

	/**
	 * @return the groupId
	 */
	public int getGroupId()
	{
		return this.groupId;
	}

	/**
	 * @param groupId the groupId to set
	 */
	public void setGroupId(int groupId)
	{
		this.groupId = groupId;
	}

	/**
	 * @return the groupName
	 */
	public String getGroupName()
	{
		return this.groupName;
	}

	/**
	 * @param groupName the groupName to set
	 */
	public void setGroupName(String groupName)
	{
		this.groupName = groupName;
	}

	/**
	 * @return the isValid
	 */
	public boolean getIsValid()
	{
		return this.isValid;
	}

	/**
	 * @param isValid the isValid to set
	 */
	public void setIsValid(boolean isValid)
	{
		this.isValid = isValid;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("GroupDetailImpl [groupId=");
		builder.append(this.groupId);
		builder.append(", groupName=");
		builder.append(this.groupName);
		builder.append(", isValid=");
		builder.append(this.isValid);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = (prime * result) + this.groupId;
		result = (prime * result)
				+ ((this.groupName == null) ? 0 : this.groupName.hashCode());
		result = (prime * result) + (this.isValid ? 1231 : 1237);
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
		if (this.groupId != other.groupId)
		{
			return false;
		}
		if (this.groupName == null)
		{
			if (other.groupName != null)
			{
				return false;
			}
		} else if (!this.groupName.equals(other.groupName))
		{
			return false;
		}
		if (this.isValid != other.isValid)
		{
			return false;
		}
		return true;
	}
	
}
