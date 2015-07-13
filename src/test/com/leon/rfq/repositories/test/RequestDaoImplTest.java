package com.leon.rfq.repositories.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.leon.rfq.common.EnumTypes.StatusEnum;
import com.leon.rfq.domains.RequestDetailImpl;
import com.leon.rfq.domains.SearchCriterionImpl;
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
		RequestDetailImpl newRequest = this.optionRequestFactory.getNewInstance("C 100 20Jan2020 0001.HK", 1, "TNG", "testUser");
		this.requestDaoImpl.insert(newRequest);
		// Act and Assert
		assertEquals("count of request should have been incremented ", beforeCount + 1, this.requestDaoImpl.getAll().size());
	}
	
	@Test
    public void get_ValidRequestId_ReturnsValidRequestMatchingRequestId()
	{
		// Arrange
		RequestDetailImpl newRequest = this.optionRequestFactory.getNewInstance("C 100 20Jan2020 0001.HK", 1, "TNG", "testUser");
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
		RequestDetailImpl newRequest = this.optionRequestFactory.getNewInstance("C 100 20Jan2020 0001.HK", 1, "TNG", "testUser");
		// Act and Assert
		assertTrue("Should insert the request and return true", this.requestDaoImpl.insert(newRequest));
		// Assert
		assertNotNull("Inserted request should now exist and return non-null", this.requestDaoImpl.get(newRequest.getIdentifier()));
	}
	
	@Test
    public void delete_ValidRequestId_DeleteSucceedsAndReturnsTrue()
	{
		// Arrange
		RequestDetailImpl newRequest = this.optionRequestFactory.getNewInstance("C 100 20Jan2020 0001.HK", 1, "TNG", "testUser");
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
		RequestDetailImpl newRequest = this.optionRequestFactory.getNewInstance("C 100 20Jan2020 0001.HK", 1, "TNG", "testUser");
		this.requestDaoImpl.insert(newRequest);
		// Act and Assert
		assertTrue("requestExistsWithRequestId should return true because requestId exists", this.requestDaoImpl.requestExistsWithRequestId(newRequest.getIdentifier()));
	}
	
	
	@Test
    public void requestExistsWithRequestId_NonexistentRequestId_ReturnsFalse()
	{
		assertFalse("requestExistsWithRequestId should return false because requestId does not exists", this.requestDaoImpl.requestExistsWithRequestId(Integer.MAX_VALUE));
	}
	
	@Test
    public void updateStatus_ValidStatusUpdate_StatusIsUpdated()
	{
		// Arrange
		RequestDetailImpl requestToUpdate = this.optionRequestFactory.getNewInstance("C 100 20Jan2020 0001.HK", 1, "TNG", "testUser");
		this.requestDaoImpl.insert(requestToUpdate);
		// Act
		requestToUpdate.setStatus(StatusEnum.INVALID);
		boolean result = this.requestDaoImpl.updateStatus(requestToUpdate);
		// Assert
		assertTrue("updateStatus method should return true", result);
		assertEquals("status should be updated to INVALID after persistance", StatusEnum.INVALID,
				this.requestDaoImpl.get(requestToUpdate.getIdentifier()).getStatus());
		assertNull("pickedUpBy should NOT be updated after persistance",
				this.requestDaoImpl.get(requestToUpdate.getIdentifier()).getPickedUpBy());
	}
	
	@Test
    public void updateStatus_PickedUpStatusUpdate_PickedUpByIsAlsoUpdated()
	{
		// Arrange
		RequestDetailImpl requestToUpdate = this.optionRequestFactory.getNewInstance("C 100 20Jan2020 0001.HK", 1, "TNG", "testUser");
		this.requestDaoImpl.insert(requestToUpdate);
		// Act
		requestToUpdate.setStatus(StatusEnum.PICKED_UP);
		boolean result = this.requestDaoImpl.updateStatus(requestToUpdate);
		// Assert
		assertTrue("updateStatus method should return true", result);
		assertEquals("status should be updated to PICKED_UP after persistance", StatusEnum.PICKED_UP,
				this.requestDaoImpl.get(requestToUpdate.getIdentifier()).getStatus());
		assertEquals("pickedUpBy should be updated to testUser after persistance", "testUser",
				this.requestDaoImpl.get(requestToUpdate.getIdentifier()).getPickedUpBy());
	}
	
	@Test
    public void updateStatus_PendingStatusUpdate_PickedUpByAlsoRevertsBackToNull()
	{
		// Arrange
		RequestDetailImpl requestToUpdate = this.optionRequestFactory.getNewInstance("C 100 20Jan2020 0001.HK", 1, "TNG", "testUser");
		this.requestDaoImpl.insert(requestToUpdate);
		// Act
		requestToUpdate.setStatus(StatusEnum.PICKED_UP);
		boolean result = this.requestDaoImpl.updateStatus(requestToUpdate);
				
		// Assert
		assertTrue("updateStatus method should return true", result);
		assertEquals("status should be updated to PICKED_UP after persistance", StatusEnum.PICKED_UP,
				this.requestDaoImpl.get(requestToUpdate.getIdentifier()).getStatus());
		assertEquals("pickedUpBy should be updated to testUser after persistance", "testUser",
				this.requestDaoImpl.get(requestToUpdate.getIdentifier()).getPickedUpBy());
		
		requestToUpdate.setStatus(StatusEnum.PENDING);
		result = this.requestDaoImpl.updateStatus(requestToUpdate);
		
		// Assert
		assertTrue("updateStatus method should return true", result);
		assertEquals("status should be updated to PENDING after persistance", StatusEnum.PENDING,
				this.requestDaoImpl.get(requestToUpdate.getIdentifier()).getStatus());
		assertNull("pickedUpBy should revert back to NULL",
				this.requestDaoImpl.get(requestToUpdate.getIdentifier()).getPickedUpBy());
	}
	
	@Test
    public void updateStatus_NonexistentRequestId_StatusIsNotUpdated()
	{
		// Arrange
		RequestDetailImpl nonexistantRequest = new RequestDetailImpl();
		nonexistantRequest.setIdentifier(Integer.MAX_VALUE);
		nonexistantRequest.setStatus(StatusEnum.INVALID);
		// Act
		boolean result = this.requestDaoImpl.updateStatus(nonexistantRequest);
		// Assert
		assertFalse("updateStatus method should return false", result);
	}
	
	@Test
    public void update_NonexistentRequestId_BookCodeIsNotUpdated()
	{
		// Arrange
		RequestDetailImpl nonexistantRequest = new RequestDetailImpl();
		nonexistantRequest.setIdentifier(Integer.MAX_VALUE);
		nonexistantRequest.setBookCode("testBook");
		// Act
		boolean result = this.requestDaoImpl.update(nonexistantRequest);
		// Assert
		assertFalse("update method should return false", result);
	}
	
	@Test
    public void update_ValidUpdate_UpdatedCorrectly()
	{
		// Arrange
		RequestDetailImpl requestToUpdate = this.optionRequestFactory.getNewInstance("C 100 20Jan2020 0001.HK", 1, "TNG", "testUser");
		this.requestDaoImpl.insert(requestToUpdate);
		// Act
		requestToUpdate.setClientId(Integer.MAX_VALUE);
		requestToUpdate.setBookCode("testBook");
		requestToUpdate.setTraderComment("traderComment");
		requestToUpdate.setClientComment("clientComment");
		requestToUpdate.setSalesComment("salesComment");
		boolean result = this.requestDaoImpl.update(requestToUpdate);
		RequestDetailImpl updatedRequest = this.requestDaoImpl.get(requestToUpdate.getIdentifier());
		// Assert
		assertTrue("update method should return true", result);
		assertEquals("Client ID should be updated", Integer.MAX_VALUE, updatedRequest.getClientId());
		assertEquals("Book code should be updated", "testBook", updatedRequest.getBookCode());
		assertEquals("Trader comment should be updated", "traderComment", updatedRequest.getTraderComment());
		assertEquals("Client comment should be updated", "clientComment", updatedRequest.getClientComment());
		assertEquals("Sales comment should be updated", "salesComment", updatedRequest.getSalesComment());
	}
	
	@Test
    public void search_ValidBookCriteria_ReturnsCorrectSingleResult()
	{
		// Arrange
		RequestDetailImpl request = this.optionRequestFactory.getNewInstance("C 100 20Jan2020 0001.HK", 1, "TNG", "testUser");
		this.requestDaoImpl.insert(request);
		Set<SearchCriterionImpl> criteria = new HashSet<>();
		criteria.add(new SearchCriterionImpl("testOwner", "testKey", "bookCode", "TNG", true));
		// Act
		Set<RequestDetailImpl> result = this.requestDaoImpl.search(criteria);
		assertEquals("Should return newly inserted request", 1, result.size());
	}
	
	@Test
    public void search_ValidClientCriteria_ReturnsSingleCorrectResult()
	{
		// Arrange
		RequestDetailImpl request = this.optionRequestFactory.getNewInstance("C 100 20Jan2020 0001.HK", Integer.MAX_VALUE, "TNG", "testUser");
		this.requestDaoImpl.insert(request);
		Set<SearchCriterionImpl> criteria = new HashSet<>();
		criteria.add(new SearchCriterionImpl("testOwner", "testKey", "clientId", String.valueOf(Integer.MAX_VALUE), true));
		// Act
		Set<RequestDetailImpl> results = this.requestDaoImpl.search(criteria);
		assertEquals("Should return newly inserted request", 1, results.size());
		for(RequestDetailImpl result : results)
		{
			assertEquals(request.getIdentifier(), result.getIdentifier());
			assertEquals(request.getClientId(), result.getClientId());
			assertEquals(request.getBookCode(), result.getBookCode());
			assertEquals(request.getUnderlyingRIC(), result.getUnderlyingRIC());
		}
	}
	
	@Test
    public void search_ValidBookAndClientCriteria_ReturnsSingleCorrectResult()
	{
		// Arrange
		RequestDetailImpl request = this.optionRequestFactory.getNewInstance("C 100 20Jan2020 0001.HK", Integer.MAX_VALUE, "TNG", "testUser");
		this.requestDaoImpl.insert(request);
		Set<SearchCriterionImpl> criteria = new HashSet<>();
		criteria.add(new SearchCriterionImpl("testOwner", "testKey1", "clientId", String.valueOf(Integer.MAX_VALUE), true));
		criteria.add(new SearchCriterionImpl("testOwner", "testKey2", "bookCode", "TNG", true));
		// Act
		Set<RequestDetailImpl> results = this.requestDaoImpl.search(criteria);
		// Assert
		assertEquals("Should return newly inserted request", 1, results.size());
		for(RequestDetailImpl result : results)
		{
			assertEquals(request.getIdentifier(), result.getIdentifier());
			assertEquals(request.getClientId(), result.getClientId());
			assertEquals(request.getBookCode(), result.getBookCode());
			assertEquals(request.getUnderlyingRIC(), result.getUnderlyingRIC());
		}
	}
	
	@Test
    public void search_ValidBookAndClientCriteria_ReturnsZeroResults()
	{
		// Arrange
		RequestDetailImpl requestA = this.optionRequestFactory.getNewInstance("C 100 20Jan2020 0001.HK", 1, "TNG1", "testUser");
		this.requestDaoImpl.insert(requestA);
		RequestDetailImpl requestB = this.optionRequestFactory.getNewInstance("C 100 20Jan2020 0001.HK", 2, "TNG2", "testUser");
		this.requestDaoImpl.insert(requestB);
		Set<SearchCriterionImpl> criteria = new HashSet<>();
		criteria.add(new SearchCriterionImpl("testOwner", "testKey1", "clientId", String.valueOf(1), true));
		criteria.add(new SearchCriterionImpl("testOwner", "testKey2", "bookCode", "TNG2", true));
		// Act
		Set<RequestDetailImpl> result = this.requestDaoImpl.search(criteria);
		// Assert
		assertEquals("Should return both newly inserted requests", 0, result.size());
	}
	
	@Test
    public void search_ValidBookAndClientCriteria_ReturnsTwoCorrectResults()
	{
		// Arrange
		RequestDetailImpl requestA = this.optionRequestFactory.getNewInstance("C 100 20Jan2020 0001.HK", 1, "TNG1", "testUser");
		this.requestDaoImpl.insert(requestA);
		RequestDetailImpl requestB = this.optionRequestFactory.getNewInstance("C 100 20Jan2020 0005.HK", 2, "TNG1", "testUser");
		this.requestDaoImpl.insert(requestB);
		Set<SearchCriterionImpl> criteria = new HashSet<>();
		criteria.add(new SearchCriterionImpl("testOwner", "testKey1", "clientId", String.valueOf(1), true));
		criteria.add(new SearchCriterionImpl("testOwner", "testKey2", "bookCode", "TNG1", true));
		// Act
		Set<RequestDetailImpl> results = this.requestDaoImpl.search(criteria);
		// Assert
		assertEquals("Should return both newly inserted requests", 1, results.size());
		for(RequestDetailImpl result : results)
		{
			assertEquals(requestA.getIdentifier(), result.getIdentifier());
			assertEquals(requestA.getClientId(), result.getClientId());
			assertEquals(requestA.getBookCode(), result.getBookCode());
			assertEquals(requestA.getUnderlyingRIC(), result.getUnderlyingRIC());
		}
	}
	
	@Test
    public void search_ValidBookAndClientCriteria_ReturnsTwoCorrectResultsOutOfThree()
	{
		// Arrange
		RequestDetailImpl requestA = this.optionRequestFactory.getNewInstance("C 100 20Jan2020 0001.HK", 1, "TNG1", "testUser");
		this.requestDaoImpl.insert(requestA);
		RequestDetailImpl requestB = this.optionRequestFactory.getNewInstance("C 100 20Jan2020 0001.HK", 2, "TNG1", "testUser");
		this.requestDaoImpl.insert(requestB);
		RequestDetailImpl requestC = this.optionRequestFactory.getNewInstance("C 100 20Jan2020 0001.HK", 2, "TNG1", "testUser");
		this.requestDaoImpl.insert(requestC);
		Set<SearchCriterionImpl> criteria = new HashSet<>();
		criteria.add(new SearchCriterionImpl("testOwner", "testKey1", "clientId", String.valueOf(2), true));
		criteria.add(new SearchCriterionImpl("testOwner", "testKey2", "bookCode", "TNG1", true));
		// Act
		Set<RequestDetailImpl> result = this.requestDaoImpl.search(criteria);
		// Assert
		assertEquals("Should return both newly inserted requests", 2, result.size());
	}
	
	@Test
    public void search_ValidBookAndClientAndUnderlyingCriteria_ReturnsOneCorrectResultsOutOfThree()
	{
		// Arrange
		RequestDetailImpl requestA = this.optionRequestFactory.getNewInstance("C 100 20Jan2020 0001.HK", 1, "TNG1", "testUser");
		this.requestDaoImpl.insert(requestA);
		RequestDetailImpl requestB = this.optionRequestFactory.getNewInstance("C 100 20Jan2020 0001.HK", 2, "TNG1", "testUser");
		this.requestDaoImpl.insert(requestB);
		RequestDetailImpl requestC = this.optionRequestFactory.getNewInstance("C 100 20Jan2020 0005.HK", 2, "TNG1", "testUser");
		this.requestDaoImpl.insert(requestC);
		Set<SearchCriterionImpl> criteria = new HashSet<>();
		criteria.add(new SearchCriterionImpl("testOwner", "testKey1", "clientId", String.valueOf(2), true));
		criteria.add(new SearchCriterionImpl("testOwner", "testKey2", "bookCode", "TNG1", true));
		criteria.add(new SearchCriterionImpl("testOwner", "testKey3", "underlyingRIC", "0005.HK", true));
		// Act
		Set<RequestDetailImpl> results = this.requestDaoImpl.search(criteria);
		// Assert
		assertEquals("Should return both newly inserted requests", 1, results.size());
		
		for(RequestDetailImpl result : results)
		{
			assertEquals(requestC.getIdentifier(), result.getIdentifier());
			assertEquals(requestC.getClientId(), result.getClientId());
			assertEquals(requestC.getBookCode(), result.getBookCode());
			assertEquals(requestC.getUnderlyingRIC(), result.getUnderlyingRIC());
		}
	}
}
