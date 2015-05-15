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

import com.leon.rfq.repositories.RequestDaoImpl;


@ContextConfiguration(locations = { "classpath: **/applicationContext.xml" })
public class RequestDaoImplTest extends AbstractJUnit4SpringContextTests
{
	@Autowired
	private DataSourceTransactionManager transactionManager;
	
	private TransactionStatus status;
	
	@Autowired
	private RequestDaoImpl requestDaoImpl;
	
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
		assertNotNull("autowired requestDaoImpl should not be null", this.requestDaoImpl);
		assertNotNull("autowired transaction manager should not be null", this.transactionManager);
		
		this.status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
	}
	
	@After
	public void tearDown()
	{
		this.transactionManager.rollback(this.status);
	}
	
	/*@Test
    public void getAll_ValidScenario_ReturnsValidListOfAllRequests()
	{
		int beforeCount = this.requestDaoImpl.getAll().size();
		this.requestDaoImpl.insert("requestIdToBeUpdated", "ethan", "adeoye", "horatio.adeoye", "hong kong", "myGroup", true, "me");
		assertNotNull("getAll method should return a non-null list of requests", this.requestDaoImpl.getAll());
		assertEquals("count of request should have been incremented ", beforeCount + 1, this.requestDaoImpl.getAll().size());
	}*/
	
	@Test
    public void get_ValidRequestId_ReturnsValidRequestMatchingRequestId()
	{
		assertEquals("get method should return the request when a valid requestId is provided", 1, this.requestDaoImpl.get(1).getIdentifier());
	}
	
	@Test
    public void get_NonExistantRequestId_ReturnsValidRequestMatchingRequestId()
	{
		assertNull("get method should return null when a non-existant requestId is provided", this.requestDaoImpl.get(Integer.MAX_VALUE));
	}
	
	/*@Test
    public void updateValidity_ValidRequestId_UpdatesValidityReturnsTrue()
	{
		this.requestDaoImpl.insert("requestIdToBeUpdated", "ethan", "adeoye", "horatio.adeoye", "hong kong", "myGroup", true, "me");
		assertTrue("previously saved request should exist", this.requestDaoImpl.get("requestIdToBeUpdated").getRequestId().equals("requestIdToBeUpdated"));
		assertTrue("updateValidity method should update validity to the provided value for the saved request", this.requestDaoImpl.updateValidity("requestIdToBeUpdated", false, "leon.adeoye"));
		assertTrue("updated request validity should have changed", !this.requestDaoImpl.get("requestIdToBeUpdated").getIsValid());
	}
	
	@Test
    public void updateValidity_NonExistantRequestId_ReturnsFalse()
	{
		assertFalse("updateValidity method should return false for a non-existant request", this.requestDaoImpl.updateValidity("nonExistantRequestId", false, "leon.adeoye"));
	}
	
	@Test
    public void insert_ValidParameters_SavedRequestAndReturnsTrue()
	{
		assertTrue("save method should save a valid request and returns true", this.requestDaoImpl.insert("testRequestId", "ethan", "adeoye", "horatio.adeoye", "hong kong", "myGroup", true, "me"));
		assertTrue("previously saved request should exist", this.requestDaoImpl.get("testRequestId").getRequestId().equals("testRequestId"));
	}
	
	@Test
    public void insert_duplicatedRequestId_SaveFailsAndReturnsFalse()
	{
		this.requestDaoImpl.insert("duplicatedRequestId", "ethan", "adeoye", "horatio.adeoye", "hong kong", "myGroup", true, "me");
		assertFalse("second save method should return false because requestId already exists", this.requestDaoImpl.insert("duplicatedRequestId", "ethan", "adeoye", "horatio.adeoye", "hong kong", "myGroup", true, "me"));
	}
	
	@Test
    public void update_updatewithValidEmailAddress_EmailAddressUpdated()
	{
		this.requestDaoImpl.insert("testRequestId", "ethan", "adeoye", "horatio.adeoye", "hong kong", "myGroup", true, "me");
		assertTrue("Update should return true", this.requestDaoImpl.update("testRequestId", "ethan", "adeoye", "horatio@adeoye.com", "hong kong", "myGroup", true, "me"));
		RequestDetailImpl updatedRequest = this.requestDaoImpl.get("testRequestId");
		assertEquals("Updated request's email address should be updated to new value", updatedRequest.getEmailAddress(), "horatio@adeoye.com");
	}
	
	@Test
    public void delete_ValidRequestId_DeleteSucceedsAndReturnsTrue()
	{
		this.requestDaoImpl.insert("100000", "C+P 100", "TESTBOOK", "client", "me");
		assertTrue("previously saved request should exist", this.requestDaoImpl.get(100000).getIdentifier() == 100000);
		assertTrue("delete method should return true", this.requestDaoImpl.delete("100000"));
		assertNull("deleted request should not longer exist", this.requestDaoImpl.get("100000"));
	}*/
	
	@Test
    public void delete_nonExistentRequestId_DeleteFailsAndReturnsFalse()
	{
		assertFalse("delete method should return false because the request does not exist", this.requestDaoImpl.delete(Integer.MAX_VALUE));
	}
	
	@Test
    public void requestExistsWithRequestId_ExistingRequestId_ReturnsTrue()
	{
		//this.requestDaoImpl.insert("100000", "C+P 100", "TESTBOOK", "client", "me");
		
		assertTrue("requestExistsWithRequestId should return true because requestId exists", this.requestDaoImpl.requestExistsWithRequestId(1));
	}
	
	
	@Test
    public void requestExistsWithRequestId_NonexistentRequestId_ReturnsFalse()
	{
		assertFalse("requestExistsWithRequestId should return false because requestId does not exists", this.requestDaoImpl.requestExistsWithRequestId(Integer.MAX_VALUE));
	}
}
