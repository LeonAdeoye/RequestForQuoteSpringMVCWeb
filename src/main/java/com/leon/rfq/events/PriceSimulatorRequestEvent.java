package com.leon.rfq.events;

import org.springframework.context.ApplicationEvent;

import com.leon.rfq.common.EnumTypes.PriceSimulatorRequestEnum;

@SuppressWarnings("serial")
public final class PriceSimulatorRequestEvent extends ApplicationEvent
{
	private final PriceSimulatorRequestEnum requestType;
	private double priceMean = 0.0;
	private double priceVariance = 0.0;
	private String underlyingRIC = "";

	/**
	 * Constructor.
	 *
	 * @param  source			the object publishing this event to the price simulator.
	 * @param  requestType		the type of request to be sent to the price simulator.
	 * @throws					IllegalStateException if requestType != SUSPEND_ALL && requestType != AWAKEN_ALL.
	 */
	public PriceSimulatorRequestEvent(Object source, PriceSimulatorRequestEnum requestType)
	{
		super(source);

		if((requestType != PriceSimulatorRequestEnum.SUSPEND_ALL) && (requestType != PriceSimulatorRequestEnum.AWAKEN_ALL))
			throw new IllegalStateException();

		this.requestType = requestType;
	}

	/**
	 * Constructor.
	 *
	 * @param  source			the object publishing this event to the price simulator.
	 * @param  requestType		the type of request to be sent to the price simulator.
	 * @param  underlyingRIC	the underlyingRIC associated with the request.
	 * @throws					IllegalStateException if requestType == ADD_UNDERLYING.
	 */
	public PriceSimulatorRequestEvent(Object source, PriceSimulatorRequestEnum requestType, String underlyingRIC)
	{
		super(source);

		if(requestType == PriceSimulatorRequestEnum.ADD_UNDERLYING)
			throw new IllegalStateException();

		this.requestType = requestType;
		this.underlyingRIC = underlyingRIC;
	}

	/**
	 * Constructor.
	 *
	 * @param  source			the object publishing this event to the price simulator.
	 * @param  requestType		the type of request to be sent to the price simulator.
	 * @param  underlyingRIC	the underlyingRIC associated with the request.
	 * @param  priceMean		the normal distribution mean price to be used by price generator.
	 * @param  priceVariance	the normal distribution price variance to be used by price generator.
	 */
	public PriceSimulatorRequestEvent(Object source, PriceSimulatorRequestEnum requestType,
			String underlyingRIC, double priceMean, double priceVariance)
	{
		super(source);

		this.requestType = requestType;
		this.underlyingRIC = underlyingRIC;
		this.priceMean = priceMean;
		this.priceVariance = priceVariance;
	}

	public PriceSimulatorRequestEnum getRequestType()
	{
		return this.requestType;
	}

	public String getUnderlyingRIC()
	{
		return this.underlyingRIC;
	}

	public double getPriceMean()
	{
		return this.priceMean;
	}

	public double getPriceVariance()
	{
		return this.priceVariance;
	}

	@Override
	public String toString()
	{
		StringBuilder buffer = new StringBuilder("Price Simulator Request Event => Request type: ");
		buffer.append(this.requestType);
		buffer.append(", underlying RIC: ");
		buffer.append(this.underlyingRIC);
		buffer.append(", mean price: ");
		buffer.append(this.priceMean);
		buffer.append(", price variance: ");
		buffer.append(this.priceVariance);
		return buffer.toString();
	}
}
