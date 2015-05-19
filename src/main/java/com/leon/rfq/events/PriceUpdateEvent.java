package com.leon.rfq.events;

import org.springframework.context.ApplicationEvent;

@SuppressWarnings("serial")
public final class PriceUpdateEvent extends ApplicationEvent
{
	private final double priceUpdate;
	private final String underlyingRIC;

	public PriceUpdateEvent(Object source, String underlyingRIC, double priceUpdate)
	{
		super(source);
		this.underlyingRIC = underlyingRIC;
		this.priceUpdate = priceUpdate;
	}

	public double getPriceUpdate()
	{
		return this.priceUpdate;
	}

	public String getUnderlyingRIC()
	{
		return this.underlyingRIC;
	}

	@Override
	public String toString()
	{
		StringBuilder buffer = new StringBuilder("Price Simulator Request Event => underlying RIC: ");
		buffer.append(this.underlyingRIC);
		buffer.append(", price update: ");
		buffer.append(this.priceUpdate);
		return buffer.toString();
	}
}
