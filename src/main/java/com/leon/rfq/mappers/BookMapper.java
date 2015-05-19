package com.leon.rfq.mappers;

import java.util.List;

import com.leon.rfq.domains.BookDetailImpl;

public interface BookMapper
{
	List<BookDetailImpl> getAll();
	
	BookDetailImpl get(String bookCode);
	
	int delete(String bookCode);
	
	int insert(BookDetailImpl bookDetailImpl);
	
	int update(BookDetailImpl bookDetailImpl);

	int updateValidity(BookDetailImpl bookDetailImpl);
	
	BookDetailImpl bookExistsWithBookcode(String bookCode);
}
