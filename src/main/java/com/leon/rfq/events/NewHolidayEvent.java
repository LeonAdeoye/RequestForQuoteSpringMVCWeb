package com.leon.rfq.events;

import org.springframework.context.ApplicationEvent;

import com.leon.rfq.domains.BankHolidayDetailImpl;


@SuppressWarnings("serial")
public class NewHolidayEvent extends ApplicationEvent
{
	private final BankHolidayDetailImpl holiday;

	public NewHolidayEvent(Object source, BankHolidayDetailImpl holiday)
	{
		super(source);
		this.holiday = holiday;
	}

	@Override
	public String toString()
	{
		return "New holiday event: " + this.holiday;
	}

	public BankHolidayDetailImpl getHoliday()
	{
		return this.holiday;
	}
}
