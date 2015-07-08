package com.leon.rfq.services;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.locks.ReentrantLock;
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
	
	@Autowired(required=true)
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
	
	private boolean isBookCached(String bookCode)
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
		
		if(logger.isDebugEnabled())
			logger.debug("Getting the book with book code" + bookCode);
		
		ReentrantLock lock = new ReentrantLock();
		
		try
		{
			lock.lock();
									
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
		finally
		{
			lock.unlock();
		}
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
	public Set<BookDetailImpl> getAll()
	{
		if(logger.isDebugEnabled())
			logger.debug("Getting all the books");
		
		ReentrantLock lock = new ReentrantLock();
		
		try
		{
			lock.lock();
		
			Set<BookDetailImpl> result = this.bookDao.getAll();
			
			if(result!= null)
			{
				this.books.clear();
				
				// Could use a more complicated lambda expression here but below is far simpler
				for(BookDetailImpl book : result)
					this.books.putIfAbsent(book.getBookCode(), book);
				
				return result;
			}
			else
				return new HashSet<BookDetailImpl>();
		}
		finally
		{
			lock.unlock();
		}
	}
	
	@Override
	public Set<BookDetailImpl> getAllFromCacheOnly()
	{
		return this.books.values().stream().collect(Collectors.toSet());
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
				
		if(logger.isDebugEnabled())
			logger.debug("Inserting the book with book code" + bookCode);
		
		ReentrantLock lock = new ReentrantLock();
		
		try
		{
			lock.lock();
		
			if(!isBookCached(bookCode))
			{
				this.books.putIfAbsent(bookCode, new BookDetailImpl(bookCode, entity, isValid, savedByUser));
				
				return this.bookDao.insert(bookCode, entity, isValid, savedByUser);
			}
			
			return true;
		}
		finally
		{
			lock.unlock();
		}
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
		
		if(logger.isDebugEnabled())
			logger.debug("Deleting the book with book code" + bookCode);
		
		ReentrantLock lock = new ReentrantLock();
		
		try
		{
			lock.lock();
				
			if(isBookCached(bookCode))
			{
				this.books.remove(bookCode);
				
				return this.bookDao.delete(bookCode);
			}
			
			return false;
		}
		finally
		{
			lock.unlock();
		}
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
		
		if(logger.isDebugEnabled())
			logger.debug("Updating the validity of the book with book code" + bookCode);
		
		ReentrantLock lock = new ReentrantLock();
		
		try
		{
			lock.lock();
		
			if(isBookCached(bookCode))
			{
				BookDetailImpl book = this.books.get(bookCode);
				
				book.setIsValid(isValid);
				
				return this.bookDao.updateValidity(bookCode, isValid, updatedByUser);
			}
			
			return false;
		}
		finally
		{
			lock.unlock();
		}
	}
	
	@Override
	public List<Tag> getMatchingBookTags(String pattern)
	{
		return this.getAllFromCacheOnly().stream().filter(book -> book.getBookCode().contains(pattern))
				.map(book -> new Tag(book.getBookCode())).collect(Collectors.toList());
	}
}
