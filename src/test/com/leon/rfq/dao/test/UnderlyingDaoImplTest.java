package com.leon.rfq.dao.test;

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

import com.leon.rfq.underlying.UnderlyingDao;
import com.leon.rfq.underlying.UnderlyingDetailImpl;


@ContextConfiguration(locations = { "classpath: **/applicationContext.xml" })
public class UnderlyingDaoImplTest extends AbstractJUnit4SpringContextTests
{
	@Autowired
	private DataSourceTransactionManager transactionManager;
	
	private TransactionStatus status;
	
	@Autowired
	private UnderlyingDao underlyingDao;
	
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
		assertNotNull("autowired underlyingDao should not be null", this.underlyingDao);
		assertNotNull("autowired transaction manager should not be null", this.transactionManager);
		
		this.status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
	}
	
	@After
	public void tearDown()
	{
		this.transactionManager.rollback(this.status);
	}
	
	@Test
    public void getAll_ValidScenario_ReturnsValidListOfAllUnderlyings()
	{
		int beforeCount = this.underlyingDao.getAll().size();
		this.underlyingDao.insert("testRic", "description", true, "me");
		assertNotNull("getAll method should return a non-null list of underlyings", this.underlyingDao.getAll());
		assertEquals("count of underlyings should have been incremented ", beforeCount + 1, this.underlyingDao.getAll().size());
	}
	
	@Test
    public void get_ValidRic_ReturnsValidUnderlyingMatchingRic()
	{
		this.underlyingDao.insert("testRic", "description", true, "me");
		assertEquals("get method should return the underlying when a valid ric is provided", "testRic", this.underlyingDao.get("testRic").getRic());
	}
	
	@Test
    public void get_NonExistanRic_ReturnsNull()
	{
		assertNull("get method should return null when a non-existant ric is provided", this.underlyingDao.get("nonExistantRic"));
	}
	
	@Test
    public void insert_ValidParameters_InsertsUnderlyingAndReturnsIt()
	{
		assertNotNull("insert method should insert a valid underlying and return it", this.underlyingDao.insert("testRic", "description", true, "me"));
		assertTrue("previously saved underlying should exist", this.underlyingDao.get("testRic").getRic().equals("testRic"));
	}
	
	@Test
    public void insert_duplicatedRic_InsertFailsAndReturnsNull()
	{
		this.underlyingDao.insert("testRic", "description", true, "me");
		assertNull("second insert should return false because ric already exists", this.underlyingDao.insert("testRic", "description", true, "me"));
	}
	
	@Test
    public void update_updateWithValidDescription_DescriptionUpdated()
	{
		this.underlyingDao.insert("testRic", "description", true, "me");
		assertNotNull("Update should return true", this.underlyingDao.update("testRic", "updated description", true, "me"));
		UnderlyingDetailImpl updatedUnderlying = this.underlyingDao.get("testRic");
		assertEquals("Updated underlying's description should be updated to new value", updatedUnderlying.getDescription(), "updated description");
	}
	
	@Test
    public void delete_ValidRic_DeleteSucceedsAndReturnsTrue()
	{
		this.underlyingDao.insert("testRic", "description", true, "me");
		assertTrue("previously saved underlying should exist", this.underlyingDao.get("testRic").getRic().equals("testRic"));
		assertTrue("delete method should return true", this.underlyingDao.delete("testRic"));
		assertNull("deleted unerlying should not longer exist", this.underlyingDao.get("testRic"));
	}
	
	@Test
    public void delete_nonExistentRic_DeleteFailsAndReturnsFalse()
	{
		assertFalse("delete method should return false because the underlying does not exist", this.underlyingDao.delete("testRic"));
	}
	
	@Test
    public void underlyingExistsWithRic_ExistingRic_ReturnsTrue()
	{
		this.underlyingDao.insert("testRic", "description", true, "me");
		
		assertTrue("underlyingExistsWithRic should return true because ric exists", this.underlyingDao.underlyingExistsWithRic("testRic"));
	}
	
	@Test
    public void underlyingExistsWithRic_NonExistentRic_ReturnsFalse()
	{
		assertFalse("underlyingExistsWithRic should return false because ric does not exists", this.underlyingDao.underlyingExistsWithRic("testRic"));
	}
}
