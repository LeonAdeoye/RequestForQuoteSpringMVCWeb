package com.leon.rfq.events;

import org.springframework.context.ApplicationEvent;

import com.leon.rfq.domains.BookDetailImpl;

@SuppressWarnings("serial")
public final class NewBookEvent extends ApplicationEvent
{
	private final BookDetailImpl newBook;

	public NewBookEvent(Object source, BookDetailImpl newBook)
	{
		super(source);
		this.newBook = newBook;
	}

	@Override
	public String toString()
	{
		return "New book event: " + this.newBook;
	}

	public BookDetailImpl getNewBook()
	{
		return this.newBook;
	}
}
