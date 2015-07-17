package com.leon.rfq.domains;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;

public final class ChatMessageImpl
{
	private final String owner;
	private final Set<String> recipients;
	private final String content;
	private final LocalDateTime timeStamp;
	private final int requestId;
	
	@Id
	private String id;

	public ChatMessageImpl(String owner, Set<String> recipients, String content, int requestId)
	{
		this.owner = owner;
		this.recipients = new HashSet<>(recipients);
		this.content = content;
		this.timeStamp = LocalDateTime.now();
		this.requestId = requestId;
	}

	public String getOwner()
	{
		return this.owner;
	}


	public Set<String> getRecipients()
	{
		return this.recipients;
	}


	public String getContent()
	{
		return this.content;
	}


	public LocalDateTime getTimeStamp()
	{
		return this.timeStamp;
	}


	public int getRequestId()
	{
		return this.requestId;
	}


	public String getId()
	{
		return this.id;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("ChatMessageImpl [owner=");
		builder.append(this.owner);
		builder.append(", recipients=");
		builder.append(this.recipients);
		builder.append(", content=");
		builder.append(this.content);
		builder.append(", timeStamp=");
		builder.append(this.timeStamp);
		builder.append(", requestId=");
		builder.append(this.requestId);
		builder.append(", id=");
		builder.append(this.id);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((this.content == null) ? 0 : this.content.hashCode());
		result = (prime * result) + ((this.id == null) ? 0 : this.id.hashCode());
		result = (prime * result) + ((this.owner == null) ? 0 : this.owner.hashCode());
		result = (prime * result)
				+ ((this.recipients == null) ? 0 : this.recipients.hashCode());
		result = (prime * result) + this.requestId;
		result = (prime * result)
				+ ((this.timeStamp == null) ? 0 : this.timeStamp.hashCode());
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
		if (!(obj instanceof ChatMessageImpl))
		{
			return false;
		}
		ChatMessageImpl other = (ChatMessageImpl) obj;
		if (this.content == null)
		{
			if (other.content != null)
			{
				return false;
			}
		} else if (!this.content.equals(other.content))
		{
			return false;
		}
		if (this.id == null)
		{
			if (other.id != null)
			{
				return false;
			}
		} else if (!this.id.equals(other.id))
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
		if (this.recipients == null)
		{
			if (other.recipients != null)
			{
				return false;
			}
		} else if (!this.recipients.equals(other.recipients))
		{
			return false;
		}
		if (this.requestId != other.requestId)
		{
			return false;
		}
		if (this.timeStamp == null)
		{
			if (other.timeStamp != null)
			{
				return false;
			}
		} else if (!this.timeStamp.equals(other.timeStamp))
		{
			return false;
		}
		return true;
	}
	
}