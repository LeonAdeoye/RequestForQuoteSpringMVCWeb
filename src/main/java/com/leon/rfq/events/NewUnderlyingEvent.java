package com.leon.rfq.events;

import org.springframework.context.ApplicationEvent;

import com.leon.rfq.domains.UnderlyingDetailImpl;

@SuppressWarnings("serial")
public final class NewUnderlyingEvent extends ApplicationEvent
{
	private final UnderlyingDetailImpl newUnderlying;

	public NewUnderlyingEvent(Object source, UnderlyingDetailImpl newUnderlying)
	{
		super(source);
		this.newUnderlying = newUnderlying;
	}

	@Override
	public String toString()
	{
		return "New underlying event: " + this.newUnderlying;
	}

	public UnderlyingDetailImpl getNewUnderlying()
	{
		return this.newUnderlying;
	}
}
