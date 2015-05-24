package com.leon.rfq.domains;

import javax.xml.bind.annotation.XmlRootElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement(name="BookDetailImpl")
public final class BookDetailImpl
{
	private static final Logger logger = LoggerFactory.getLogger(BookDetailImpl.class);
	private String bookCode;
	private boolean isValid;
	private String entity;
	private String lastUpdatedByUser;

	public BookDetailImpl() {}

	public BookDetailImpl(String bookCode, String entity, boolean isValid, String lastUpdatedByUser)
	{
		this.bookCode = bookCode;
		this.entity = entity;
		this.isValid = isValid;
		this.lastUpdatedByUser = lastUpdatedByUser;

		logger.debug("BookDetailImpl object instantiated => " +  this);
	}

	public String getBookCode()
	{
		return this.bookCode;
	}

	public void setBookCode(String bookCode)
	{
		this.bookCode = bookCode;
	}
	
	public String getLastUpdatedBy()
	{
		return this.lastUpdatedByUser;
	}

	public void setLastUpdatedBy(String lastUpdatedByUser)
	{
		this.lastUpdatedByUser = lastUpdatedByUser;
	}

	public String getEntity()
	{
		return this.entity;
	}

	public void setEntity(String entity)
	{
		this.entity = entity;
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
		StringBuilder buf = new StringBuilder("Book Code: ");
		buf.append(this.bookCode);
		buf.append(", Entity: ");
		buf.append(this.entity);
		buf.append(", LastUpdatedByUser: ");
		buf.append(this.lastUpdatedByUser);
		buf.append(", Is Valid: ");
		buf.append(this.isValid ? "TRUE" : "FALSE");
		return buf.toString();
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = (prime * result)
				+ ((this.bookCode == null) ? 0 : this.bookCode.hashCode());
		result = (prime * result) + ((this.entity == null) ? 0 : this.entity.hashCode());
		result = (prime * result) + (this.isValid ? 1231 : 1237);
		result = (prime * result)	+ ((this.lastUpdatedByUser == null) ? 0 : this.lastUpdatedByUser.hashCode());
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
		if (!(obj instanceof BookDetailImpl))
		{
			return false;
		}
		BookDetailImpl other = (BookDetailImpl) obj;
		if (this.bookCode == null)
		{
			if (other.bookCode != null)
			{
				return false;
			}
		} else if (!this.bookCode.equals(other.bookCode))
		{
			return false;
		}
		if (this.entity == null)
		{
			if (other.entity != null)
			{
				return false;
			}
		} else if (!this.entity.equals(other.entity))
		{
			return false;
		}
		if (this.isValid != other.isValid)
		{
			return false;
		}
		if (this.lastUpdatedByUser == null)
		{
			if (other.lastUpdatedByUser != null)
			{
				return false;
			}
		} else if (!this.lastUpdatedByUser.equals(other.lastUpdatedByUser))
		{
			return false;
		}
		return true;
	}
		
}