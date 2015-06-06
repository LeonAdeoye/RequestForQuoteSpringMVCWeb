package com.leon.rfq.repositories;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.leon.rfq.domains.BookDetailImpl;
import com.leon.rfq.mappers.BookMapper;

@Repository
public class BookDaoImpl implements BookDao
{
	private static final Logger logger = LoggerFactory.getLogger(BookDaoImpl.class);
	
	@Autowired
	private BookMapper bookMapper;

	@Override
	public boolean delete(String bookCode)
	{
		if(logger.isDebugEnabled())
			logger.debug("Delete the book with bookCode " + bookCode);
		
		try
		{
			return this.bookMapper.delete(bookCode) == 1;
		}
		catch(Exception e)
		{
			if(logger.isErrorEnabled())
				logger.error("Failed to delete the book with bookCode " + bookCode + " because of exception: " + e);
			return false;
		}
	}

	@Override
	public boolean insert(String bookCode, String entity, boolean isValid,
			String savedByUser)
	{
		if(logger.isDebugEnabled())
			logger.debug("Insert the book with bookCode " + bookCode);
		
		try
		{
			return this.bookMapper.insert(new BookDetailImpl(bookCode, entity, isValid, savedByUser)) == 1;
		}
		catch(Exception e)
		{
			if(logger.isErrorEnabled())
				logger.error("Failed to insert the book with bookCode " + bookCode + " because of exception: " + e);
			
			return false;
		}
	}
	
	@Override
	public boolean update(String bookCode, String entity, boolean isValid, String savedByUser)
	{
		if(logger.isDebugEnabled())
			logger.debug("Update the book with bookCode " + bookCode);
		
		try
		{
			return this.bookMapper.update(new BookDetailImpl(bookCode, entity, isValid, savedByUser)) == 1;
		}
		catch(Exception e)
		{
			if(logger.isErrorEnabled())
				logger.error("Failed to update the book with bookCode " + bookCode + " because of exception: " + e);
			
			return false;
		}
	}

	@Override
	public boolean updateValidity(String bookCode, boolean isValid, String updatedByUser)
	{
		if(logger.isDebugEnabled())
			logger.debug("Update the validity of book with bookCode " + bookCode + " to " + isValid);

		try
		{
			return this.bookMapper.updateValidity(new BookDetailImpl(bookCode, "", isValid, updatedByUser)) == 1;
		}
		catch(Exception e)
		{
			if(logger.isErrorEnabled())
				logger.error("Failed to update the validity of book with bookCode " + bookCode + " to " + isValid + " because of exception: " + e);
			
			return false;
		}
	}

	@Override
	public Set<BookDetailImpl> getAll()
	{
		if(logger.isDebugEnabled())
			logger.debug("Request to get all books");
		
		logger.debug(String.format("Size of books to be returned: %d", this.bookMapper.getAll().size()));

		return this.bookMapper.getAll();
	}

	@Override
	public BookDetailImpl get(String bookCode)
	{
		if(logger.isDebugEnabled())
			logger.debug("Request to get book with bookCode: " + bookCode);
		
		return this.bookMapper.get(bookCode);
	}
	
	@Override
	public boolean bookExistsWithBookCode(String bookCode)
	{
		if(logger.isDebugEnabled())
			logger.debug("Request to check if book exists with bookCode: " + bookCode);
		
		return this.bookMapper.bookExistsWithBookCode(bookCode) != null;
	}
}

