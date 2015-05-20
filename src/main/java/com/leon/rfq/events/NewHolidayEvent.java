package com.leon.rfq.events;

import org.springframework.context.ApplicationEvent;

import com.leon.rfq.domains.HolidayDetailImpl;


@SuppressWarnings("serial")
public class NewHolidayEvent extends ApplicationEvent
{
	private final HolidayDetailImpl holiday;

	public NewHolidayEvent(Object source, HolidayDetailImpl holiday)
	{
		super(source);
		this.holiday = holiday;
	}

	@Override
	public String toString()
	{
		return "New holiday event: " + this.holiday;
	}

	public HolidayDetailImpl getHoliday()
	{
		return this.holiday;
	}
}
