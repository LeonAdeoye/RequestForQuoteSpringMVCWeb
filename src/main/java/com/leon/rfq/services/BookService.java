package com.leon.rfq.services;

import java.util.List;

import com.leon.rfq.domains.BookDetailImpl;
import com.leon.rfq.repositories.BookDao;

public interface BookService
{
	BookDetailImpl get(String bookCode);

	List<BookDetailImpl> getAll();
	
	boolean insert(String bookCode, String entity, boolean isValid, String savedByUser);
	
	boolean delete(String bookCode);
	
	boolean updateValidity(String bookCode, boolean isValid, String updatedByUser);

	void setBookDao(BookDao bookDao);
	
	boolean isBookCached(String bookCode);
	
	boolean bookExistsWithBookCode(String bookCode);

	void initialise();
}
