package com.leon.rfq.user.test;

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

import com.leon.rfq.user.UserDaoImpl;


@ContextConfiguration(locations = { "classpath: **/applicationContext.xml" })
public class UserDaoImplTest extends AbstractJUnit4SpringContextTests
{
	@Autowired
	private DataSourceTransactionManager transactionManager;
	
	private TransactionStatus status;
	
	@Autowired
	private UserDaoImpl userDaoImpl;
	
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
		assertNotNull("autowired userDaoImpl should not be null", this.userDaoImpl);
		assertNotNull("autowired transaction manager should not be null", this.transactionManager);
		
		this.status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
	}
	
	@After
	public void tearDown()
	{
		this.transactionManager.rollback(this.status);
	}
	
	@Test
    public void getAll_ValidScenario_ReturnsValidListOfAllUsers()
	{
		int beforeCount = this.userDaoImpl.getAll().size();
		this.userDaoImpl.save("userIdToBeUpdated", "ethan", "adeoye", "horatio.adeoye", "hong kong", "myGroup", true, "me");
		assertNotNull("getAll method should return a non-null list of users", this.userDaoImpl.getAll());
		assertEquals("count of user should have been incremented ", beforeCount + 1, this.userDaoImpl.getAll().size());
	}
	
	@Test
    public void get_ValidUserId_ReturnsValidUserMatchingUserId()
	{
		assertEquals("get method should return the user when a valid userId is provided", "leon.adeoye", this.userDaoImpl.get("leon.adeoye").getUserId());
	}
	
	@Test
    public void get_NonExistantUserId_ReturnsValidUserMatchingUserId()
	{
		assertNull("get method should return null when a non-existant userId is provided", this.userDaoImpl.get("nonExistantUserId"));
	}
	
	@Test
    public void updateValidity_ValidUserId_UpdatesValidityReturnsTrue()
	{
		this.userDaoImpl.save("userIdToBeUpdated", "ethan", "adeoye", "horatio.adeoye", "hong kong", "myGroup", true, "me");
		assertTrue("previously saved user should exist", this.userDaoImpl.get("userIdToBeUpdated").getUserId().equals("userIdToBeUpdated"));
		assertTrue("updateValidity method should update validity to the provided value for the saved user", this.userDaoImpl.updateValidity("userIdToBeUpdated", false, "leon.adeoye"));
		assertTrue("updated user validity should have changed", !this.userDaoImpl.get("userIdToBeUpdated").getIsValid());
	}
	
	@Test
    public void updateValidity_NonExistantUserId_ReturnsFalse()
	{
		assertFalse("updateValidity method should return false for a non-existant user", this.userDaoImpl.updateValidity("nonExistantUserId", false, "leon.adeoye"));
	}
	
	@Test
    public void save_ValidParameters_SavedUserAndReturnsTrue()
	{
		assertTrue("save method should save a valid user and returns true", this.userDaoImpl.save("testUserId", "ethan", "adeoye", "horatio.adeoye", "hong kong", "myGroup", true, "me"));
		assertTrue("previously saved user should exist", this.userDaoImpl.get("testUserId").getUserId().equals("testUserId"));
	}
	
	@Test
    public void save_duplicatedUserId_SaveFailsAndReturnsFalse()
	{
		this.userDaoImpl.save("duplicatedUserId", "ethan", "adeoye", "horatio.adeoye", "hong kong", "myGroup", true, "me");
		assertFalse("second save method should return false because userId already exists", this.userDaoImpl.save("duplicatedUserId", "ethan", "adeoye", "horatio.adeoye", "hong kong", "myGroup", true, "me"));
	}
	
	@Test
    public void delete_ValidUserId_DeleteSucceedsAndReturnsTrue()
	{
		this.userDaoImpl.save("userIdToBeDeleted", "ethan", "adeoye", "horatio.adeoye", "hong kong", "myGroup", true, "me");
		assertTrue("previously saved user should exist", this.userDaoImpl.get("userIdToBeDeleted").getUserId().equals("userIdToBeDeleted"));
		assertTrue("delete method should return true", this.userDaoImpl.delete("userIdToBeDeleted"));
		assertNull("deleted user should not longer exist", this.userDaoImpl.get("userIdToBeDeleted"));
	}
	
	@Test
    public void delete_duplicatedUserId_DeleteFailsAndReturnsFalse()
	{
		assertFalse("delete method should return false because the user does not exist", this.userDaoImpl.delete("nonExistantUserId"));
	}
}
