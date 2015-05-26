package com.leon.rfq.services.test;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.leon.rfq.repositories.BookDao;
import com.leon.rfq.repositories.BookDaoImpl;
import com.leon.rfq.services.BookService;
import com.leon.rfq.services.BookServiceImpl;

@ContextConfiguration(locations = { "classpath: **/applicationContext.xml" })
public class BookServiceImplTest extends AbstractJUnit4SpringContextTests
{
	@Autowired
	private BookService bookService;
	
	@Before
	public void setUp()
	{
		assertNotNull("autowired bookDaoImpl should not be null", this.bookService);
	}
	
	@Test
    public void getAll_ValidScenario_ReturnsValidListOfAllBooks()
	{
		assertNotNull("getAll method should return a non-null list of books", this.bookService.getAll());
	}
	
	@Test
    public void get_NullBookCode_ThrowsInvalidArgumentException()
	{
		catchException(this.bookService).get(null);
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "bookCode argument is invalid");
	}
	
	@Test
    public void get_EmptyStringBookCode_ThrowsInvalidArgumentException()
	{
		catchException(this.bookService).get("");
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "bookCode argument is invalid");
	}
	
	@Test
    public void get_ValidParameter_CallsDaoGetMethod()
	{
		// Arrange
		BookService bookService = new BookServiceImpl();
		BookDao bookDaoMock = mock(BookDaoImpl.class);
		bookService.setBookDao(bookDaoMock);
		// Act
		bookService.get("testBook");
		// Assert
		verify(bookDaoMock).get("testBook");
	}
	
	@Test
    public void getAll_NoParameters_CallsDaoGetAllMethod()
	{
		// Arrange
		BookService bookService = new BookServiceImpl();
		BookDao bookDaoMock = mock(BookDaoImpl.class);
		bookService.setBookDao(bookDaoMock);
		// Act
		bookService.getAll();
		// Assert
		verify(bookDaoMock).getAll();
	}
	
	@Test
    public void delete_validBookCode_CallsDaoDeleteMethod()
	{
		// Arrange
		BookService bookService = new BookServiceImpl();
		BookDao bookDaoMock = mock(BookDaoImpl.class);
		bookService.setBookDao(bookDaoMock);
		// Act
		bookService.delete("bookToBeDeleted");
		// Assert
		verify(bookDaoMock, never()).delete("bookToBeDeleted");
	}
	
	@Test
    public void updateValidity_NonExistentBookCode_DoesNotCallDaoUpdateValidityMethod()
	{
		// Arrange
		BookService bookService = new BookServiceImpl();
		BookDao bookDaoMock = mock(BookDaoImpl.class);
		bookService.setBookDao(bookDaoMock);
		// Act
		bookService.updateValidity("bookToBeUpdated", true, "tester");
		// Assert
		verify(bookDaoMock, never()).updateValidity("bookToBeUpdated", true, "tester");
	}
	
	@Test
    public void updateValidity_NonExistentBookCode_ReturnFalse()
	{
		// Arrange
		BookService bookService = new BookServiceImpl();
		BookDao bookDaoMock = mock(BookDaoImpl.class);
		bookService.setBookDao(bookDaoMock);
		// Act and Assert
		assertFalse("updateValidity should return false if book does not exist.", bookService.updateValidity("bookToBeUpdated", true, "tester"));
	}
	
	@Test
    public void updateValidity_validBookCode_CallsDaoUpdateValidityMethod()
	{
		// Arrange
		BookService bookService = new BookServiceImpl();
		BookDao bookDaoMock = mock(BookDaoImpl.class);
		bookService.setBookDao(bookDaoMock);
		// Act
		bookService.updateValidity("bookToBeUpdated", true, "tester");
		// Assert
		verify(bookDaoMock, never()).updateValidity("bookToBeUpdated", true, "tester");
	}
	
	@Test
    public void insert_validParameters_CallsDaoSaveMethod()
	{
		// Arrange
		BookService bookService = new BookServiceImpl();
		BookDao bookDaoMock = mock(BookDaoImpl.class);
		bookService.setBookDao(bookDaoMock);
		// Act
		bookService.insert("testBook", "entity", true, "tester");
		// Assert
		verify(bookDaoMock).insert("testBook", "entity", true, "tester");
	}
	
	@Test
    public void insert_NullBookCode_ThrowsIllegalArgumentException()
	{
		catchException(this.bookService).insert(null, "entity", true, "tester");
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "bookCode argument is invalid");
	}
	
	@Test
    public void insert_EmptyStringBookCode_ThrowsIllegalArgumentException()
	{
		catchException(this.bookService).insert("", "entity", true, "tester");
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "bookCode argument is invalid");
	}
	
	@Test
    public void insert_NullEntity_ThrowsIllegalArgumentException()
	{
		catchException(this.bookService).insert("bookCode", null, true, "tester");
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "entity argument is invalid");
	}
	
	@Test
    public void insert_EmptyStringEntity_ThrowsIllegalArgumentException()
	{
		catchException(this.bookService).insert("bookCode", "", true, "tester");
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "entity argument is invalid");
	}
	
	@Test
    public void insert_NullSavedByUser_ThrowsIllegalArgumentException()
	{
		catchException(this.bookService).insert("bookCode", "entity", true, null);
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "savedByUser argument is invalid");
	}
	
	@Test
    public void insert_EmptyStringSavedByUser_ThrowsInvalidArgumentException()
	{
		catchException(this.bookService).insert("bookCode", "entity", true, "");
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "savedByUser argument is invalid");
	}
	
	@Test
    public void delete_NullParameter_ThrowsIllegalArgumentException()
	{
		catchException(this.bookService).delete(null);
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "bookCode argument is invalid");
	}
	
	@Test
    public void delete_EmptyStringParameter_ThrowsInvalidArgumentException()
	{
		catchException(this.bookService).delete("");
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "bookCode argument is invalid");
	}
	
	@Test
    public void updateValidity_NullBookCode_ThrowsIllegalArgumentException()
	{
		catchException(this.bookService).updateValidity(null, true, "tester");
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "bookCode argument is invalid");
	}
	
	@Test
    public void updateValidity_EmptyStringBookCode_ThrowsInvalidArgumentException()
	{
		catchException(this.bookService).updateValidity("", true, "tester");
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "bookCode argument is invalid");
	}
	
	@Test
    public void updateValidity_NullUpdatedByUser_ThrowsIllegalArgumentException()
	{
		catchException(this.bookService).updateValidity("bookCode", true, null);
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "updatedByUser argument is invalid");
	}
	
	@Test
    public void updateValidity_EmptyStringUpdatedByUser_ThrowsInvalidArgumentException()
	{
		catchException(this.bookService).updateValidity("bookCode", true, "");
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "updatedByUser argument is invalid");
	}
	
	@Test
    public void bookExistsWithBookCode_EmptyBookCode_ThrowsInvalidArgumentException()
	{
		catchException(this.bookService).bookExistsWithBookCode("");
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "bookCode argument is invalid");
	}
	
	@Test
    public void bookExistsWithBookCode_NullBookCode_ThrowsIllegalArgumentException()
	{
		catchException(this.bookService).bookExistsWithBookCode(null);
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "bookCode argument is invalid");
	}
	
	@Test
    public void bookExistsWithBookCode_existingBookCode__CallsCorrectDaoMethod()
	{
		// Arrange
		BookService bookService = new BookServiceImpl();
		BookDao bookDaoMock = mock(BookDaoImpl.class);
		bookService.setBookDao(bookDaoMock);
		// Act
		bookService.bookExistsWithBookCode("bookCode");
		// Assert
		verify(bookDaoMock).bookExistsWithBookCode("bookCode");
	}
}
