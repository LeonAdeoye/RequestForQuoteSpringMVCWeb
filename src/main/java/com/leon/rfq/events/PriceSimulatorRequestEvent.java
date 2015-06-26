package com.leon.rfq.events;

import org.springframework.context.ApplicationEvent;

import com.leon.rfq.common.EnumTypes.PriceSimulatorRequestEnum;

@SuppressWarnings("serial")
public final class PriceSimulatorRequestEvent extends ApplicationEvent
{
	private final PriceSimulatorRequestEnum requestType;
	private String underlyingRIC = "";
	
	/**
	 * Constructor.
	 *
	 * @param  source			the object publishing this event to the price simulator.
	 * @param  requestType		the type of request to be sent to the price simulator.
	 * @throws					IllegalStateException if requestType != SUSPEND_ALL && requestType != AWAKEN_ALL && requestType != REMOVE_ALL.
	 */
	public PriceSimulatorRequestEvent(Object source, PriceSimulatorRequestEnum requestType)
	{
		super(source);

		if((requestType != PriceSimulatorRequestEnum.REMOVE_ALL) && (requestType != PriceSimulatorRequestEnum.SUSPEND_ALL) && (requestType != PriceSimulatorRequestEnum.AWAKEN_ALL))
			throw new IllegalArgumentException("requestType is an invalid argument");

		this.requestType = requestType;
	}

	/**
	 * Constructor.
	 *
	 * @param  source			the object publishing this event to the price simulator.
	 * @param  requestType		the type of request to be sent to the price simulator.
	 * @param  underlyingRIC	the underlyingRIC associated with the request.
	 * @throws					IllegalStateException if requestType == SUSPEND_ALL/AWAKEN_ALL/REMOVE_ALL.
	 */
	public PriceSimulatorRequestEvent(Object source, PriceSimulatorRequestEnum requestType, String underlyingRIC)
	{
		super(source);

		if((requestType == PriceSimulatorRequestEnum.REMOVE_ALL) || (requestType == PriceSimulatorRequestEnum.SUSPEND_ALL) || (requestType == PriceSimulatorRequestEnum.AWAKEN_ALL))
			throw new IllegalArgumentException("requestType is an invalid argument");

		this.requestType = requestType;
		this.underlyingRIC = underlyingRIC;
	}

	public PriceSimulatorRequestEnum getRequestType()
	{
		return this.requestType;
	}

	public String getUnderlyingRIC()
	{
		return this.underlyingRIC;
	}

	@Override
	public String toString()
	{
		StringBuilder buffer = new StringBuilder("Price Simulator Request Event => Request type: ");
		buffer.append(this.requestType);
		buffer.append(", underlying RIC: ");
		buffer.append(this.underlyingRIC);
		return buffer.toString();
	}
}
