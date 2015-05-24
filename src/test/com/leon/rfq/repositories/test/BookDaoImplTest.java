package com.leon.rfq.repositories.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.leon.rfq.domains.BookDetailImpl;
import com.leon.rfq.repositories.BookDaoImpl;


@ContextConfiguration(locations = { "classpath: **/applicationContext.xml" })
public class BookDaoImplTest extends AbstractJUnit4SpringContextTests
{
	@Autowired
	private DataSourceTransactionManager transactionManager;
	
	private TransactionStatus status;
	
	@Autowired
	private BookDaoImpl bookDaoImpl;
	
	@BeforeClass
	public static void setup()
	{
	}
	
	@AfterClass
	public static void teardown()
	{
	}
	
	@Before
	public void setUp()
	{
		assertNotNull("autowired bookDaoImpl should not be null", this.bookDaoImpl);
		assertNotNull("autowired transaction manager should not be null", this.transactionManager);
		
		this.status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
	}
	
	@After
	public void tearDown()
	{
		this.transactionManager.rollback(this.status);
	}
	
	@Test
    public void getAll_ValidScenario_ReturnsValidListOfAllBooks()
	{
		int beforeCount = this.bookDaoImpl.getAll().size();
		this.bookDaoImpl.insert("testbook", "testEntity", true, "testUSer");
		assertNotNull("getAll method should return a non-null list of books", this.bookDaoImpl.getAll());
		assertEquals("count of books should have been incremented ", beforeCount + 1, this.bookDaoImpl.getAll().size());
	}
	
	@Test
    public void get_ValidBookCode_ReturnsValidBookMatchingBookCode()
	{
		this.bookDaoImpl.insert("testbook", "testEntity", true, "testUSer");
		assertEquals("get method should return the book when a valid bookCode is provided", "testbook", this.bookDaoImpl.get("testbook").getBookCode());
	}
	
	@Test
    public void get_NonExistantBookCode_ReturnsNulls()
	{
		assertNull("get method should return null when a non-existant bookCode is provided", this.bookDaoImpl.get("testBook"));
	}
	
	@Test
    public void updateValidity_ValidBookCode_UpdatesValidityReturnsTrue()
	{
		this.bookDaoImpl.insert("testbook", "testEntity", true, "testUser");
		assertTrue("previously saved book should exist", this.bookDaoImpl.get("testbook").getBookCode().equals("testbook"));
		assertTrue("updateValidity method should update validity to the provided value for the saved book", this.bookDaoImpl.updateValidity("testbook", false, "testUser"));
		assertFalse("updated book validity should have changed", this.bookDaoImpl.get("testbook").getIsValid());
	}
	
	@Test
    public void updateValidity_NonExistantBookCode_ReturnsFalse()
	{
		assertFalse("updateValidity method should return false for a non-existant book", this.bookDaoImpl.updateValidity("testBook", false, "testUser"));
	}
	
	@Test
    public void insert_ValidParameters_SavesBookAndReturnsTrue()
	{
		assertTrue("Insert method should save a valid book and return true", this.bookDaoImpl.insert("testBook", "testEntity", true, "testUser"));
		assertEquals("previously saved book should exist", "testBook", this.bookDaoImpl.get("testBook").getBookCode());
	}
	
	@Test
    public void insert_duplicatedBookCode_SaveFailsAndReturnsFalse()
	{
		this.bookDaoImpl.insert("testbook", "testEntity", true, "testUser");
		assertFalse("second save method should return false because bookCode already exists", this.bookDaoImpl.insert("testbook", "testEntity", true, "testUser"));
	}
	
	@Test
    public void update_ValidEntity_EntityUpdated()
	{
		this.bookDaoImpl.insert("testbook", "testEntity", true, "testUser");
		assertTrue("Update should return true", this.bookDaoImpl.update("testbook", "newEntity", true, "testUser"));
		BookDetailImpl updatedBook = this.bookDaoImpl.get("testbook");
		assertEquals("Updated book's entity should be updated to new value", "newEntity", updatedBook.getEntity());
	}
	
	@Test
    public void delete_ValidBookCode_DeleteSucceedsAndReturnsTrue()
	{
		this.bookDaoImpl.insert("testBook", "testEntity", true, "testUser");
		assertEquals("previously saved book should exist", "testBook", this.bookDaoImpl.get("testBook").getBookCode());
		assertTrue("delete method should delete book and return true", this.bookDaoImpl.delete("testBook"));
		assertNull("deleted book should not longer exist and get method should return null", this.bookDaoImpl.get("testBook"));
	}
	
	@Test
    public void delete_nonexistentBookCode_DeleteFailsAndReturnsFalse()
	{
		assertFalse("delete method should return false because the book does not exist", this.bookDaoImpl.delete("nonExistantBookCode"));
	}
		
	@Test
    public void bookExistsWithBookCode_ExistingBookCode_ReturnsTrue()
	{
		this.bookDaoImpl.insert("testbook", "testEntity", true, "testUser");
		
		assertTrue("bookExistsWithBookCode should return true because bookCode exists", this.bookDaoImpl.bookExistsWithBookCode("testbook"));
	}
	
	@Test
    public void bookExistsWithBookCode_NonexistentBookCode_ReturnsFalse()
	{
		assertFalse("bookExistsWithBookCode should return false because bookCode does not exists", this.bookDaoImpl.bookExistsWithBookCode("testbook"));
	}
}
