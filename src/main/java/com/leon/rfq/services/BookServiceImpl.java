package com.leon.rfq.services;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.leon.rfq.common.Tag;
import com.leon.rfq.domains.BookDetailImpl;
import com.leon.rfq.repositories.BookDao;

@Service
public final class BookServiceImpl implements BookService
{
	private static final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);
	private ApplicationEventPublisher applicationEventPublisher;
	private final Map<String, BookDetailImpl> books = new ConcurrentSkipListMap<>();
	
	@Autowired
	private BookDao bookDao;
	
	// For unit testing mocking framework.
	@Override
	public void setBookDao(BookDao bookDao)
	{
		this.bookDao = bookDao;
	}
	
	@Override
	@PostConstruct
	public void initialise()
	{
		if(logger.isDebugEnabled())
			logger.debug("Initializing book service by getting all existing books...");
		
		this.getAll();
	}
	
	public BookServiceImpl() {}
	
	@Override
	public boolean isBookCached(String bookCode)
	{
		return this.books.containsKey(bookCode);
	}
	
	@Override
	public BookDetailImpl get(String bookCode)
	{
		if((bookCode == null) || bookCode.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("bookCode argument is invalid");
			
			throw new IllegalArgumentException("bookCode argument is invalid");
		}
		
		BookDetailImpl book;
		
		if(isBookCached(bookCode))
			book = this.books.get(bookCode);
		else
		{
			book = this.bookDao.get(bookCode);
			if(book != null)
				this.books.putIfAbsent(bookCode, book);
		}
		
		return book;
	}
	
	@Override
	public boolean bookExistsWithBookCode(String bookCode)
	{
		if((bookCode == null) || bookCode.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("bookCode argument is invalid");
			
			throw new IllegalArgumentException("bookCode argument is invalid");
		}
		
		return isBookCached(bookCode) ? true : this.bookDao.bookExistsWithBookCode(bookCode);
	}
		
	@Override
	public List<BookDetailImpl> getAll()
	{
		List<BookDetailImpl> result = this.bookDao.getAll();
		
		if(result!= null)
		{
			this.books.clear();
			
			// Could use a more complicated lambda expression here but below is far simpler
			for(BookDetailImpl book : result)
				this.books.putIfAbsent(book.getBookCode(), book);
			
			return result;
		}
		else
			return new LinkedList<BookDetailImpl>();
	}
	
	@Override
	public List<BookDetailImpl> getAllFromCacheOnly()
	{
		return Collections.synchronizedList(this.books.values().stream().collect(Collectors.toList()));
	}

	@Override
	public boolean insert(String bookCode, String entity, boolean isValid, String savedByUser)
	{
		if((bookCode == null) || bookCode.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("bookCode argument is invalid");
			
			throw new IllegalArgumentException("bookCode argument is invalid");
		}

		if((entity == null) || entity.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("entity argument is invalid");
			
			throw new IllegalArgumentException("entity argument is invalid");
		}
		
		if((savedByUser == null) || savedByUser.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("savedByUser argument is invalid");
			
			throw new IllegalArgumentException("savedByUser argument is invalid");
		}
		
		
		if(!isBookCached(bookCode))
		{
			this.books.putIfAbsent(bookCode, new BookDetailImpl(bookCode, entity, isValid, savedByUser));
			
			return this.bookDao.insert(bookCode, entity, isValid, savedByUser);
		}
		
		return true;
	}

	@Override
	public boolean delete(String bookCode)
	{
		if((bookCode == null) || bookCode.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("bookCode argument is invalid");
			
			throw new IllegalArgumentException("bookCode argument is invalid");
		}
		
		if(isBookCached(bookCode))
		{
			this.books.remove(bookCode);
			
			return this.bookDao.delete(bookCode);
		}
		
		return false;
	}

	@Override
	public boolean updateValidity(String bookCode, boolean isValid, String updatedByUser)
	{
		if((bookCode == null) || bookCode.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("bookCode argument is invalid");
			
			throw new IllegalArgumentException("bookCode argument is invalid");
		}
		
		if((updatedByUser == null) || updatedByUser.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("updatedByUser argument is invalid");
			
			throw new IllegalArgumentException("updatedByUser argument is invalid");
		}
		
		if(isBookCached(bookCode))
		{
			BookDetailImpl book = this.books.get(bookCode);
			
			book.setIsValid(isValid);
			
			return this.bookDao.updateValidity(bookCode, isValid, updatedByUser);
		}
		
		return false;
	}
	
	@Override
	public List<Tag> getMatchingBookTags(String pattern)
	{
		return Collections.synchronizedList(this.getAllFromCacheOnly().stream().filter(book -> book.getBookCode().contains(pattern))
				.map(book -> new Tag(book.getBookCode())).collect(Collectors.toList()));
	}
}
