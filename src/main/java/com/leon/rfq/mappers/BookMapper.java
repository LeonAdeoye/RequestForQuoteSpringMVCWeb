package com.leon.rfq.mappers;

import java.util.Set;

import com.leon.rfq.domains.BookDetailImpl;

public interface BookMapper
{
	Set<BookDetailImpl> getAll();
	
	BookDetailImpl get(String bookCode);
	
	int delete(String bookCode);
	
	int insert(BookDetailImpl bookDetailImpl);
	
	int update(BookDetailImpl bookDetailImpl);

	int updateValidity(BookDetailImpl bookDetailImpl);
	
	BookDetailImpl bookExistsWithBookCode(String bookCode);
}
