package com.leon.rfq.services;

import java.util.List;
import java.util.Set;

import com.leon.rfq.common.Tag;
import com.leon.rfq.domains.BookDetailImpl;
import com.leon.rfq.repositories.BookDao;

public interface BookService
{
	BookDetailImpl get(String bookCode);

	Set<BookDetailImpl> getAll();
	
	Set<BookDetailImpl> getAllFromCacheOnly();
	
	boolean insert(String bookCode, String entity, boolean isValid, String savedByUser);
	
	boolean delete(String bookCode);
	
	boolean updateValidity(String bookCode, boolean isValid, String updatedByUser);

	void setBookDao(BookDao bookDao);
	
	boolean bookExistsWithBookCode(String bookCode);

	void initialise();

	List<Tag> getMatchingBookTags(String bookPattern);
}
