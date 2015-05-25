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

import com.leon.rfq.domains.RequestDetailImpl;
import com.leon.rfq.products.OptionRequestFactory;
import com.leon.rfq.repositories.RequestDaoImpl;


@ContextConfiguration(locations = { "classpath: **/applicationContext.xml" })
public class RequestDaoImplTest extends AbstractJUnit4SpringContextTests
{
	@Autowired
	private DataSourceTransactionManager transactionManager;
	
	@Autowired(required=true)
	private OptionRequestFactory optionRequestFactory;
	
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
	
	@Test
    public void getAll_ValidScenario_ReturnsValidListOfAllRequests()
	{
		// Arrange
		int beforeCount = this.requestDaoImpl.getAll().size();
		RequestDetailImpl newRequest = this.optionRequestFactory.getNewInstance("C 100 20Jan2016 0001.HK", 1, "TNG", "testuser");
		this.requestDaoImpl.insert(newRequest);
		// Act and Assert
		assertEquals("count of request should have been incremented ", beforeCount + 1, this.requestDaoImpl.getAll().size());
	}
	
	@Test
    public void get_ValidRequestId_ReturnsValidRequestMatchingRequestId()
	{
		// Arrange
		RequestDetailImpl newRequest = this.optionRequestFactory.getNewInstance("C 100 20Jan2016 0001.HK", 1, "TNG", "testuser");
		this.requestDaoImpl.insert(newRequest);
		// Act and Assert
		assertEquals("get method should return the request when a valid requestId is provided", newRequest.getIdentifier(), this.requestDaoImpl.get(newRequest.getIdentifier()).getIdentifier());
	}
	
	@Test
    public void get_NonExistantRequestId_ReturnsValidRequestMatchingRequestId()
	{
		assertNull("get method should return null when a non-existant requestId is provided", this.requestDaoImpl.get(Integer.MAX_VALUE));
	}
	
	@Test
    public void insert_ValidParameters_InsertsRequestAndReturnsTrue()
	{
		// Arrange
		RequestDetailImpl newRequest = this.optionRequestFactory.getNewInstance("C 100 20Jan2016 0001.HK", 1, "TNG", "testuser");
		// Act and Assert
		assertTrue("Should insert the request and return true", this.requestDaoImpl.insert(newRequest));
		// Assert
		assertNotNull("Inserted request should now exist and return non-null", this.requestDaoImpl.get(newRequest.getIdentifier()));
	}
	
	@Test
    public void delete_ValidRequestId_DeleteSucceedsAndReturnsTrue()
	{
		// Arrange
		RequestDetailImpl newRequest = this.optionRequestFactory.getNewInstance("C 100 20Jan2016 0001.HK", 1, "TNG", "testuser");
		this.requestDaoImpl.insert(newRequest);
		// Act and Assert
		assertTrue("delete method should return true", this.requestDaoImpl.delete(newRequest.getIdentifier()));
		// Assert
		assertNull("deleted request should not longer exist", this.requestDaoImpl.get(newRequest.getIdentifier()));
	}
	
	@Test
    public void delete_nonExistentRequestId_DeleteFailsAndReturnsFalse()
	{
		assertFalse("delete method should return false because the request does not exist", this.requestDaoImpl.delete(Integer.MAX_VALUE));
	}
	
	@Test
    public void requestExistsWithRequestId_ExistingRequestId_ReturnsTrue()
	{
		// Arrange
		RequestDetailImpl newRequest = this.optionRequestFactory.getNewInstance("C 100 20Jan2016 0001.HK", 1, "TNG", "testuser");
		this.requestDaoImpl.insert(newRequest);
		// Act and Assert
		assertTrue("requestExistsWithRequestId should return true because requestId exists", this.requestDaoImpl.requestExistsWithRequestId(newRequest.getIdentifier()));
	}
	
	
	@Test
    public void requestExistsWithRequestId_NonexistentRequestId_ReturnsFalse()
	{
		assertFalse("requestExistsWithRequestId should return false because requestId does not exists", this.requestDaoImpl.requestExistsWithRequestId(Integer.MAX_VALUE));
	}
}
