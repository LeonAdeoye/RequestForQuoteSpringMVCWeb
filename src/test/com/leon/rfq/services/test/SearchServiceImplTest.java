package com.leon.rfq.services.test;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.leon.rfq.repositories.SearchDao;
import com.leon.rfq.repositories.SearchDaoImpl;
import com.leon.rfq.services.SearchService;
import com.leon.rfq.services.SearchServiceImpl;

@ContextConfiguration(locations = { "classpath: **/applicationContext.xml" })
public class SearchServiceImplTest extends AbstractJUnit4SpringContextTests
{
	@Test
    public void get_ValidScenario_ReturnsValidListOfAllSearches()
	{
		// Arrange
		SearchService searchService = new SearchServiceImpl();
		SearchDao searchDaoMock = mock(SearchDaoImpl.class);
		searchService.setSearchDao(searchDaoMock);
		assertTrue("get method should return a non-null list of searchs", searchService.get().isEmpty());
	}
	
	@Test
    public void get_NullOwner_ThrowsInvalidArgumentException()
	{
		// Arrange
		SearchService searchService = new SearchServiceImpl();
		// Act
		catchException(searchService).get(null);
		// Assert
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "owner argument is invalid");
	}
	
	@Test
    public void get_EmptyOwnerString_ThrowsInvalidArgumentException()
	{
		// Arrange
		SearchService searchService = new SearchServiceImpl();
		// Act
		catchException(searchService).get("");
		// Assert
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "owner argument is invalid");
	}
	
	@Test
    public void get_EmptyStringSearchKey_ThrowsInvalidArgumentException()
	{
		// Arrange
		SearchService searchService = new SearchServiceImpl();
		// Act
		catchException(searchService).get("testOwner", "");
		// Assert
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "searchKey argument is invalid");
	}
	
	@Test
    public void get_NullSearchKey_ThrowsInvalidArgumentException()
	{
		// Arrange
		SearchService searchService = new SearchServiceImpl();
		// Act
		catchException(searchService).get("testOwner", null);
		// Assert
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "searchKey argument is invalid");
	}
	
	@Test
    public void get_EmptyOwnerStringAndValidSearchKey_ThrowsInvalidArgumentException()
	{
		// Arrange
		SearchService searchService = new SearchServiceImpl();
		// Act
		catchException(searchService).get("", "testKey");
		// Assert
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "owner argument is invalid");
	}
	
	@Test
    public void get_NullOwnerAndValidSearchKey_ThrowsInvalidArgumentException()
	{
		// Arrange
		SearchService searchService = new SearchServiceImpl();
		// Act
		catchException(searchService).get(null, "testKey");
		// Assert
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "owner argument is invalid");
	}
	
	@Test
    public void get_ValidOwner_CallsDaoGetMethod()
	{
		// Arrange
		SearchService searchService = new SearchServiceImpl();
		SearchDao searchDaoMock = mock(SearchDaoImpl.class);
		searchService.setSearchDao(searchDaoMock);
		// Act
		searchService.get("testOwner");
		// Assert
		verify(searchDaoMock).get("testOwner");
	}
	
	@Test
    public void get_NoParameters_CallsDaoGetMethod()
	{
		// Arrange
		SearchService searchService = new SearchServiceImpl();
		SearchDao searchDaoMock = mock(SearchDaoImpl.class);
		searchService.setSearchDao(searchDaoMock);
		// Act
		searchService.get();
		// Assert
		verify(searchDaoMock).get();
	}
	
	@Test
    public void get_ValidOwnerAndKey_NeverCallsDaoGetMethod()
	{
		// Arrange
		SearchService searchService = new SearchServiceImpl();
		SearchDao searchDaoMock = mock(SearchDaoImpl.class);
		searchService.setSearchDao(searchDaoMock);
		// Act
		searchService.get("testOwner", "testKey");
		// Assert
		verify(searchDaoMock).get("testOwner", "testKey");
	}
	
	@Test
    public void delete_NonExistentOwnerAndKey_DoesNotCallDaoDeleteMethod()
	{
		// Arrange
		SearchService searchService = new SearchServiceImpl();
		SearchDao searchDaoMock = mock(SearchDaoImpl.class);
		searchService.setSearchDao(searchDaoMock);
		// Act
		searchService.delete("testOwner", "testKey");
		// Assert
		verify(searchDaoMock, never()).delete("testOwner", "testKey");
	}
	
	@Test
    public void delete_NonExistentOwner_DoesNotCallDaoDeleteMethod()
	{
		// Arrange
		SearchService searchService = new SearchServiceImpl();
		SearchDao searchDaoMock = mock(SearchDaoImpl.class);
		searchService.setSearchDao(searchDaoMock);
		// Act
		searchService.delete("testOwner");
		// Assert
		verify(searchDaoMock, never()).delete("testOwner");
	}
	
	@Test
    public void insert_validParameters_CallsDaoInsertMethod()
	{
		// Arrange
		SearchService searchService = new SearchServiceImpl();
		SearchDao searchDaoMock = mock(SearchDaoImpl.class);
		searchService.setSearchDao(searchDaoMock);
		// Act
		searchService.insert("testOwner", "testKey", "name", "value", true);
		// Assert
		verify(searchDaoMock).insert("testOwner", "testKey", "name", "value", true);
	}
	
	@Test
    public void insert_NullOwner_ThrowsIllegalArgumentException()
	{
		// Arrange
		SearchService searchService = new SearchServiceImpl();
		// Act
		catchException(searchService).insert(null, "testKey", "name", "value", true);
		// Assert
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "owner argument is invalid");
	}
	
	@Test
    public void insert_EmptyOwnerString_ThrowsIllegalArgumentException()
	{
		// Arrange
		SearchService searchService = new SearchServiceImpl();
		// Act
		catchException(searchService).insert("", "testKey", "name", "value", true);
		// Assert
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "owner argument is invalid");
	}
	
	@Test
    public void insert_NullSearchKey_ThrowsIllegalArgumentException()
	{
		// Arrange
		SearchService searchService = new SearchServiceImpl();
		// Act
		catchException(searchService).insert("testOwner", null, "name", "value", true);
		// Assert
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "searchKey argument is invalid");
	}
	
	@Test
    public void insert_EmptySearchKeyString_ThrowsIllegalArgumentException()
	{
		// Arrange
		SearchService searchService = new SearchServiceImpl();
		// Act
		catchException(searchService).insert("testOwner", "", "name", "value", true);
		// Assert
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "searchKey argument is invalid");
	}
	
	@Test
    public void insert_NullCriterionName_ThrowsIllegalArgumentException()
	{
		// Arrange
		SearchService searchService = new SearchServiceImpl();
		// Act
		catchException(searchService).insert("testOwner", "testKey", null, "value", true);
		// Assert
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "name argument is invalid");
	}
	
	@Test
    public void insert_EmptyCriterionNameString_ThrowsInvalidArgumentException()
	{
		// Arrange
		SearchService searchService = new SearchServiceImpl();
		// Act
		catchException(searchService).insert("testOwner", "testKey", "", "value", true);
		// Assert
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "name argument is invalid");
	}
	
	@Test
    public void insert_NullCriterionValue_ThrowsInvalidArgumentException()
	{
		// Arrange
		SearchService searchService = new SearchServiceImpl();
		// Act
		catchException(searchService).insert("testOwner", "testKey", "name", null, true);
		// Assert
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "value argument is invalid");
	}
	
	@Test
    public void insert_EmptyCriterionValueString_ThrowsInvalidArgumentException()
	{
		// Arrange
		SearchService searchService = new SearchServiceImpl();
		// Act
		catchException(searchService).insert("testOwner", "testKey", "name", "", true);
		// Assert
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "value argument is invalid");
	}
	
	@Test
    public void delete_NullOwner_ThrowsIllegalArgumentException()
	{
		// Arrange
		SearchService searchService = new SearchServiceImpl();
		// Act
		catchException(searchService).delete(null);
		// Assert
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "owner argument is invalid");
	}
	
	@Test
    public void delete_EmptyOwnerString_ThrowsInvalidArgumentException()
	{
		// Arrange
		SearchService searchService = new SearchServiceImpl();
		// Act
		catchException(searchService).delete("");
		// Assert
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "owner argument is invalid");
	}
	
	@Test
    public void delete_NullOwnerValidSearchKey_ThrowsIllegalArgumentException()
	{
		// Arrange
		SearchService searchService = new SearchServiceImpl();
		// Act
		catchException(searchService).delete(null, "testKey");
		// Assert
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "owner argument is invalid");
	}
	
	@Test
    public void delete_EmptyOwnerStringValidSearchKey_ThrowsInvalidArgumentException()
	{
		// Arrange
		SearchService searchService = new SearchServiceImpl();
		// Act
		catchException(searchService).delete("", "testKey");
		// Assert
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "owner argument is invalid");
	}
	
	@Test
    public void delete_ValidOwnerNullSearcKey_ThrowsIllegalArgumentException()
	{
		// Arrange
		SearchService searchService = new SearchServiceImpl();
		// Act
		catchException(searchService).delete("testOwner", null);
		// Assert
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "searchKey argument is invalid");
	}
	
	@Test
    public void delete_ValidOwnerEmptySearchKeyString_ThrowsInvalidArgumentException()
	{
		// Arrange
		SearchService searchService = new SearchServiceImpl();
		// Act
		catchException(searchService).delete("testOwner", "");
		// Assert
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "searchKey argument is invalid");
	}
}
