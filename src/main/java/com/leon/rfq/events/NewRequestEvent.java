package com.leon.rfq.events;

import org.springframework.context.ApplicationEvent;

import com.leon.rfq.domains.RequestDetailImpl;

@SuppressWarnings("serial")
public final class NewRequestEvent extends ApplicationEvent
{
	private final RequestDetailImpl newRequest;

	public NewRequestEvent(Object source, RequestDetailImpl newRequest)
	{
		super(source);
		this.newRequest = newRequest;
	}

	@Override
	public String toString()
	{
		return "New request event: " + this.newRequest;
	}

	public RequestDetailImpl getNewRequest()
	{
		return this.newRequest;
	}
}
