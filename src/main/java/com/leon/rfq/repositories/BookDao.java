package com.leon.rfq.repositories;

import java.util.Set;

import com.leon.rfq.domains.BookDetailImpl;

public interface BookDao
{
	boolean delete(String bookCode);

	boolean insert(String bookCode,
			String entity,
			boolean isValid,
			String savedByUser);
	
	boolean update(String bookCode,
			String entity,
			boolean isValid,
			String savedByUser);

	boolean updateValidity(String bookCode, boolean isValid, String updatedByUser);

	Set<BookDetailImpl> getAll();
	 
	BookDetailImpl get(String bookCode);

	boolean bookExistsWithBookCode(String bookCode);
}
