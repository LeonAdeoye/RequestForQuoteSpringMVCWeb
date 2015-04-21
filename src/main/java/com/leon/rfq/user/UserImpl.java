package com.leon.rfq.user;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.ibatis.type.Alias;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement
@Alias("UserImpl")
public class UserImpl
{
	private static final Logger logger = LoggerFactory.getLogger(UserImpl.class);
	@NotNull(message="{user.validation.userId.notNull}")
	private String userId;
	@Pattern(regexp="^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}$", message="{user.validation.emailAddress.pattern}")
	private String emailAddress;
	@NotNull(message="{user.validation.firstName.notNull}")
	private String firstName;
	@NotNull(message="{user.validation.lastName.notNull}")
	private String lastName;
	private String locationName;
	private String groupName;
	private boolean isValid;
	private String lastUpdatedBy;
	
	public UserImpl()
	{
		
	}
	
	public UserImpl(String userId, String emailAddress, String firstName,
			String lastName, String locationName, String groupName, boolean isValid, String lastUpdatedBy)
	{
		this.userId = userId;
		this.emailAddress = emailAddress;
		this.firstName = firstName;
		this.lastName = lastName;
		this.locationName = locationName;
		this.groupName = groupName;
		this.isValid = isValid;
		this.lastUpdatedBy = lastUpdatedBy;
		
		if(logger.isDebugEnabled())
		{
			logger.debug("Constructor instantiation of UserImpl: " + this.toString());
		}
	}

	public String getUserId()
	{
		return this.userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getEmailAddress()
	{
		return this.emailAddress;
	}

	public void setEmailAddress(String emailAddress)
	{
		this.emailAddress = emailAddress;
	}

	public String getFirstName()
	{
		return this.firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public String getLastName()
	{
		return this.lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public String getLocationName()
	{
		return this.locationName;
	}

	public void setLocationName(String locationName)
	{
		this.locationName = locationName;
	}
	
	public String getGroupName()
	{
		return this.groupName;
	}

	public void setGroupName(String groupName)
	{
		this.groupName = groupName;
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
		StringBuilder builder = new StringBuilder("User [userId=");
		builder.append(this.userId);
		builder.append(", emailAddress=");
		builder.append(this.emailAddress);
		builder.append(", firstName=");
		builder.append(this.firstName);
		builder.append(", lastName=");
		builder.append(this.lastName);
		builder.append(", locationName=");
		builder.append(this.locationName);
		builder.append(", groupName=");
		builder.append(this.groupName);
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
		result = (prime * result)
				+ ((this.emailAddress == null) ? 0 : this.emailAddress.hashCode());
		result = (prime * result)
				+ ((this.firstName == null) ? 0 : this.firstName.hashCode());
		result = (prime * result)
				+ ((this.groupName == null) ? 0 : this.groupName.hashCode());
		result = (prime * result) + (this.isValid ? 1231 : 1237);
		result = (prime * result)
				+ ((this.lastName == null) ? 0 : this.lastName.hashCode());
		result = (prime
				* result)
				+ ((this.lastUpdatedBy == null) ? 0 : this.lastUpdatedBy.hashCode());
		result = (prime * result)
				+ ((this.locationName == null) ? 0 : this.locationName.hashCode());
		result = (prime * result) + ((this.userId == null) ? 0 : this.userId.hashCode());
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
		if (!(obj instanceof UserImpl))
		{
			return false;
		}
		UserImpl other = (UserImpl) obj;
		if (this.emailAddress == null)
		{
			if (other.emailAddress != null)
			{
				return false;
			}
		} else if (!this.emailAddress.equals(other.emailAddress))
		{
			return false;
		}
		if (this.firstName == null)
		{
			if (other.firstName != null)
			{
				return false;
			}
		} else if (!this.firstName.equals(other.firstName))
		{
			return false;
		}
		if (!this.groupName.equals(other.groupName))
		{
			return false;
		}
		if (this.isValid != other.isValid)
		{
			return false;
		}
		if (this.lastName == null)
		{
			if (other.lastName != null)
			{
				return false;
			}
		} else if (!this.lastName.equals(other.lastName))
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
		if (this.locationName == null)
		{
			if (other.locationName != null)
			{
				return false;
			}
		} else if (!this.locationName.equals(other.locationName))
		{
			return false;
		}
		if (this.userId == null)
		{
			if (other.userId != null)
			{
				return false;
			}
		} else if (!this.userId.equals(other.userId))
		{
			return false;
		}
		return true;
	}
	
}
