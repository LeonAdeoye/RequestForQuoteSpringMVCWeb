package com.leon.rfq.events;

import org.springframework.context.ApplicationEvent;

import com.leon.rfq.domains.UserDetailImpl;

@SuppressWarnings("serial")
public class NewUserEvent extends ApplicationEvent
{
	private final UserDetailImpl newUser;

	public NewUserEvent(Object source, UserDetailImpl newUser)
	{
		super(source);
		this.newUser = newUser;
	}

	@Override
	public String toString()
	{
		return "New user event: " + this.newUser;
	}

	public UserDetailImpl getNewUser()
	{
		return this.newUser;
	}
}
