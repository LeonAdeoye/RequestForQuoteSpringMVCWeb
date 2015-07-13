package com.leon.rfq.repositories.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
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

import com.leon.rfq.domains.SearchCriterionImpl;
import com.leon.rfq.repositories.SearchDaoImpl;


@ContextConfiguration(locations = { "classpath: **/applicationContext.xml" })
public class SearchDaoImplTest extends AbstractJUnit4SpringContextTests
{
	@Autowired(required=true)
	private DataSourceTransactionManager transactionManager;
	
	private TransactionStatus status;
	
	@Autowired(required=true)
	private SearchDaoImpl searchDaoImpl;
	
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
		assertNotNull("autowired searchDaoImpl should not be null", this.searchDaoImpl);
		assertNotNull("autowired transaction manager should not be null", this.transactionManager);
		
		this.status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
	}
	
	@After
	public void tearDown()
	{
		this.transactionManager.rollback(this.status);
	}
	
	@Test
    public void get_InsertValidSearch_PersistedSearchCountShouldBeIncremented()
	{
		// Arrange
		int beforeCount = this.searchDaoImpl.get().size();
		SearchCriterionImpl expected = new SearchCriterionImpl("testOwner", "testKey", "testControl", "testValue", true);
		// Act
		this.searchDaoImpl.insert("testOwner", "testKey", "testControl", "testValue", true);
		// Assert
		assertNotNull("get method should return a non-null list of searchs", this.searchDaoImpl.get());
		assertEquals("count of searches returned should be incremented ", beforeCount + 1, this.searchDaoImpl.get().size());
		assertTrue("get", this.searchDaoImpl.get().get("testOwner").get("testKey").contains(expected));
	}
	
	@Test
    public void get_InsertValidSearch_ReturnsMatchingSearch()
	{
		// Arrange
		SearchCriterionImpl expected = new SearchCriterionImpl("testOwner", "testKey", "testControl", "testValue", true);
		// Act
		this.searchDaoImpl.insert("testOwner", "testKey", "testControl", "testValue", true);
		// Assert
		assertTrue("get method should return a set with expected criterion",
				this.searchDaoImpl.get("testOwner", "testKey").contains(expected));
	}
	
	@Test
    public void get_NonExistantSearchCode_ReturnsEmptySet()
	{
		// AAA
		assertTrue("get method should return an empty set", this.searchDaoImpl.get("testOwner", "testKey").isEmpty());
	}
	
	@Test
    public void get_ValidOwner_ReturnsMatchingSearch()
	{
		// Arrange
		SearchCriterionImpl expected = new SearchCriterionImpl("testOwner", "testKey", "testControl", "testValue", true);
		// Act
		this.searchDaoImpl.insert("testOwner", "testKey", "testControl", "testValue", true);
		// Assert
		assertTrue("get method should return a set with expected criterion",
				this.searchDaoImpl.get("testOwner").get("testKey").contains(expected));
	}
	
	@Test
    public void get_NonExistantOwner_ReturnsEmptyMap()
	{
		// AAA
		assertTrue("get method should return an empty map", this.searchDaoImpl.get("testOwner").isEmpty());
	}
		
	@Test
    public void insert_duplicatedSearchCode_SaveFailsAndReturnsFalse()
	{
		// Arrange & Act
		this.searchDaoImpl.insert("tester", "testKey", "testControl", "testValue", true);
		boolean result = this.searchDaoImpl.insert("tester", "testKey", "testControl", "testValue", true);
		// Assert
		assertFalse("second save method should return false because search already exists", result);
	}
		
	@Test
    public void delete_ValidOwnerAndKey_DeleteShouldReturnTrue()
	{
		// Act
		this.searchDaoImpl.insert("tester", "testKey", "testControl", "testValue", true);
		// Assert
		assertTrue("delete method should delete search and return true", this.searchDaoImpl.delete("tester", "testKey"));
		assertTrue("After the delete the get method should return an empty set", this.searchDaoImpl.get("tester", "testKey").isEmpty());
	}
	
	@Test
    public void delete_NonexistentOwnerAndKey_DeleteFailsAndReturnsFalse()
	{
		// AAA
		assertFalse("delete method should return false", this.searchDaoImpl.delete("tester", "testKey"));
	}
	
	
	@Test
    public void delete_ValidOwner_DeleteShouldReturnTrue()
	{
		// Act
		this.searchDaoImpl.insert("tester", "testKey", "testControl", "testValue", true);
		// Assert
		assertTrue("delete method should delete search and return true", this.searchDaoImpl.delete("tester"));
		assertTrue("After delete the get method should return an empty set", this.searchDaoImpl.get("tester").isEmpty());
	}
	
	@Test
    public void delete_NonexistentOwner_DeleteFailsAndReturnsFalse()
	{
		// AAA
		assertFalse("delete method should return false", this.searchDaoImpl.delete("tester"));
	}
}
