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

import com.leon.rfq.dao.UserDaoImpl;
import com.leon.rfq.domains.UserImpl;


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
		this.userDaoImpl.insert("userIdToBeUpdated", "ethan", "adeoye", "horatio.adeoye", "hong kong", "myGroup", true, "me");
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
		this.userDaoImpl.insert("userIdToBeUpdated", "ethan", "adeoye", "horatio.adeoye", "hong kong", "myGroup", true, "me");
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
    public void insert_ValidParameters_SavedUserAndReturnsTrue()
	{
		assertTrue("save method should save a valid user and returns true", this.userDaoImpl.insert("testUserId", "ethan", "adeoye", "horatio.adeoye", "hong kong", "myGroup", true, "me"));
		assertTrue("previously saved user should exist", this.userDaoImpl.get("testUserId").getUserId().equals("testUserId"));
	}
	
	@Test
    public void insert_duplicatedUserId_SaveFailsAndReturnsFalse()
	{
		this.userDaoImpl.insert("duplicatedUserId", "ethan", "adeoye", "horatio.adeoye", "hong kong", "myGroup", true, "me");
		assertFalse("second save method should return false because userId already exists", this.userDaoImpl.insert("duplicatedUserId", "ethan", "adeoye", "horatio.adeoye", "hong kong", "myGroup", true, "me"));
	}
	
	@Test
    public void update_updatewithValidEmailAddress_EmailAddressUpdated()
	{
		this.userDaoImpl.insert("testUserId", "ethan", "adeoye", "horatio.adeoye", "hong kong", "myGroup", true, "me");
		assertTrue("Update should return true", this.userDaoImpl.update("testUserId", "ethan", "adeoye", "horatio@adeoye.com", "hong kong", "myGroup", true, "me"));
		UserImpl updatedUser = this.userDaoImpl.get("testUserId");
		assertEquals("Updated user's email address should be updated to new value", updatedUser.getEmailAddress(), "horatio@adeoye.com");
	}
	
	@Test
    public void delete_ValidUserId_DeleteSucceedsAndReturnsTrue()
	{
		this.userDaoImpl.insert("userIdToBeDeleted", "ethan", "adeoye", "horatio.adeoye", "hong kong", "myGroup", true, "me");
		assertTrue("previously saved user should exist", this.userDaoImpl.get("userIdToBeDeleted").getUserId().equals("userIdToBeDeleted"));
		assertTrue("delete method should return true", this.userDaoImpl.delete("userIdToBeDeleted"));
		assertNull("deleted user should not longer exist", this.userDaoImpl.get("userIdToBeDeleted"));
	}
	
	@Test
    public void delete_duplicatedUserId_DeleteFailsAndReturnsFalse()
	{
		assertFalse("delete method should return false because the user does not exist", this.userDaoImpl.delete("nonExistantUserId"));
	}
	
	@Test
    public void userExistsWithEmailAddress_ExistingEmailAddress_ReturnsTrue()
	{
		this.userDaoImpl.insert("userId", "ethan", "adeoye", "horatio.adeoye@test.com", "hong kong", "myGroup", true, "me");
		
		assertTrue("userExistsWithEmailAddress should return true because email address exists", this.userDaoImpl.userExistsWithEmailAddress("horatio.adeoye@test.com"));
	}
	
	@Test
    public void userExistsWithEmailAddress_NonexistentEmailAddress_ReturnsFalse()
	{
		assertFalse("userExistsWithEmailAddress should return false because email address does not exists", this.userDaoImpl.userExistsWithEmailAddress("peppa@test.com"));
	}
	
	@Test
    public void userExistsWithUserId_ExistingUserId_ReturnsTrue()
	{
		this.userDaoImpl.insert("peppa_pig", "ethan", "adeoye", "horatio.adeoye@home.com", "hong kong", "myGroup", true, "me");
		
		assertTrue("userExistsWithUserId should return true because userId exists", this.userDaoImpl.userExistsWithUserId("peppa_pig"));
	}
	
	@Test
    public void userExistsWithUserId_NonexistentUserId_ReturnsFalse()
	{
		assertFalse("userExistsWithUserId should return false because userId does not exists", this.userDaoImpl.userExistsWithUserId("papa_pig"));
	}
}
