package com.leon.rfq.service.test;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.leon.rfq.dao.RequestDao;
import com.leon.rfq.dao.RequestDaoImpl;
import com.leon.rfq.domains.RequestDetailImpl;
import com.leon.rfq.services.RequestService;
import com.leon.rfq.services.RequestServiceImpl;

@ContextConfiguration(locations = { "classpath: **/applicationContext.xml" })
public class RequestServiceImplTest extends AbstractJUnit4SpringContextTests
{
	@Autowired
	private RequestService requestService;
	
	@Before
	public void setUp()
	{
		assertNotNull("autowired requestDaoImpl should not be null", this.requestService);
	}
	
	@Test
    public void getAll_ValidScenario_ReturnsValidListOfAllUsers()
	{
		assertNotNull("getAll method should return a non-null list of users", this.requestService.getAll());
	}

	@Test
    public void get_ValidParameter_CallsDaoGetMethod()
	{
		// Arrange
		RequestService requestService = new RequestServiceImpl();
		RequestDao requestDaoMock = mock(RequestDaoImpl.class);
		requestService.setRequestDao(requestDaoMock);
		// Act
		requestService.get(Integer.MAX_VALUE);
		// Assert
		verify(requestDaoMock).get(Integer.MAX_VALUE);
	}
	
	@Test
    public void getAll_NoParameters_CallsDaoGetAllMethod()
	{
		// Arrange
		RequestService requestService = new RequestServiceImpl();
		RequestDao requestDaoMock = mock(RequestDaoImpl.class);
		requestService.setRequestDao(requestDaoMock);
		// Act
		requestService.getAll();
		// Assert
		verify(requestDaoMock).getAll();
	}
	
	@Test
    public void delete_validRequestId_CallsDaoDeleteMethod()
	{
		// Arrange
		RequestService requestService = new RequestServiceImpl();
		RequestDao requestDaoMock = mock(RequestDaoImpl.class);
		requestService.setRequestDao(requestDaoMock);
		// Act
		requestService.delete(Integer.MAX_VALUE);
		// Assert
		verify(requestDaoMock, never()).delete(Integer.MAX_VALUE);
	}
	
	@Test
	@Ignore
    public void insert_ValidParameters_CallsDaoInsertMethod()
	{
		// Arrange
		RequestService requestService = new RequestServiceImpl();
		RequestDao requestDaoMock = mock(RequestDaoImpl.class);
		requestService.setRequestDao(requestDaoMock);
		// Act
		requestService.insert("testSnippet", Integer.MAX_VALUE, "testBook", "tester");
		
		verify(requestDaoMock).insert(any(RequestDetailImpl.class));
	}
		
	@Test
    public void requestExistsWithRequestId_ExistingRequestId_CallsCorrectDaoMethod()
	{
		// Arrange
		RequestService requestService = new RequestServiceImpl();
		RequestDao requestDaoMock = mock(RequestDaoImpl.class);
		requestService.setRequestDao(requestDaoMock);
		// Act
		requestService.requestExistsWithRequestId(Integer.MAX_VALUE);
		// Assert
		verify(requestDaoMock).requestExistsWithRequestId(Integer.MAX_VALUE);
	}
	
	/*
	@Test
    public void update_NullUpdatedByUser_ThrowsIllegalArgumentException()
	{
		catchException(this.requestService).update("userId", "firstName", "lastName", "emailAddress", "location", "group", true, null);
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "updatedByUser argument is invalid");
	}
	
	@Test
    public void update_EmptyStringUpdatedByUser_ThrowsInvalidArgumentException()
	{
		catchException(this.requestService).update("userId", "firstName", "lastName", "emailAddress", "location", "group", true, "");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "updatedByUser argument is invalid");
	}
	*/
	
}
