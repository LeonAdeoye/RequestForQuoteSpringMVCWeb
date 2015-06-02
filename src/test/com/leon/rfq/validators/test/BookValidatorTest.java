package com.leon.rfq.validators.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.validation.BindException;
import org.springframework.validation.ValidationUtils;

import com.leon.rfq.domains.BookDetailImpl;
import com.leon.rfq.validators.BookValidatorImpl;

@ContextConfiguration(locations = { "classpath: **/applicationContext.xml" })
public class BookValidatorTest extends AbstractJUnit4SpringContextTests
{
	@Autowired
	BookValidatorImpl bookValidator;
	
	@Test
	public void Book_ValidParameters_ShouldBeValidated()
	{
		// Arrange
		BookDetailImpl book = new BookDetailImpl("GENESIS", "entity", true, "testUser");
		BindException bindException = new BindException(book, "book");
		
		// Act
		ValidationUtils.invokeValidator(this.bookValidator, book, bindException);
		
		// Assert
		assertEquals("Error count should be zero if all parameters are valid", 0, bindException.getErrorCount());
	}
	
	@Test
	public void Book_EmptyEntity_ShouldBeInvalidated()
	{
		// Arrange
		BookDetailImpl book = new BookDetailImpl("GENESIS", "", true, "testUser");
		BindException bindException = new BindException(book, "book");
		
		// Act
		ValidationUtils.invokeValidator(this.bookValidator, book, bindException);
		
		// Assert
		assertEquals("Error count should be one if the entity is empty", 1, bindException.getErrorCount());
		assertTrue("Error message should match if the entity is empty", bindException.getLocalizedMessage().contains("Entity must be 1 to 10 characters in length"));
	}
	
	@Test
	public void Book_NullEntity_ShouldBeInvalidated()
	{
		// Arrange
		BookDetailImpl book = new BookDetailImpl("GENESIS", null, true, "testUser");
		BindException bindException = new BindException(book, "book");
		
		// Act
		ValidationUtils.invokeValidator(this.bookValidator, book, bindException);
		
		// Assert
		assertEquals("Error count should be one if the entity is null", 1, bindException.getErrorCount());
		assertTrue("Error message should match if the entity is null", bindException.getLocalizedMessage().contains("Entity cannot be null"));
	}
	
	@Test
	public void Book_BookCodeTooLong_ShouldBeInvalidated()
	{
		// Arrange
		BookDetailImpl book = new BookDetailImpl("123456789012345678901234567890", "entity", true, "testBook");
		BindException bindException = new BindException(book, "book");
		
		// Act
		ValidationUtils.invokeValidator(this.bookValidator, book, bindException);
		
		// Assert
		assertEquals("Error count should be one if the book code is too long", 1, bindException.getErrorCount());
		assertTrue("Error message should match if the book code is too long", bindException.getLocalizedMessage().contains("Book code must be 1 to 10 characters in length"));
	}
	
	@Test
	public void Book_EntityTooLong_ShouldBeInvalidated()
	{
		// Arrange
		BookDetailImpl book = new BookDetailImpl("GENESIS", "123456789012345678901234567890", true, "testBook");
		BindException bindException = new BindException(book, "book");
		
		// Act
		ValidationUtils.invokeValidator(this.bookValidator, book, bindException);
		
		// Assert
		assertEquals("Error count should be one if the entity is too long", 1, bindException.getErrorCount());
		assertTrue("Error message should match if the entity is too long", bindException.getLocalizedMessage().contains("Entity must be 1 to 10 characters in length"));
	}
}
