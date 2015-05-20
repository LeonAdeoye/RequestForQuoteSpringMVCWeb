package com.leon.rfq.events;

import org.springframework.context.ApplicationEvent;

import com.leon.rfq.domains.ClientDetailImpl;

@SuppressWarnings("serial")
public class NewClientEvent extends ApplicationEvent
{
	private final ClientDetailImpl newClient;

	public NewClientEvent(Object source, ClientDetailImpl newClient)
	{
		super(source);
		this.newClient = newClient;
	}

	@Override
	public String toString()
	{
		return "New client event: " + this.newClient;
	}

	public ClientDetailImpl getNewClient()
	{
		return this.newClient;
	}

}
