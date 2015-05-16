package com.leon.rfq.service.test;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.leon.rfq.domains.RequestDetailImpl;
import com.leon.rfq.repositories.RequestDao;
import com.leon.rfq.repositories.RequestDaoImpl;
import com.leon.rfq.services.RequestService;
import com.leon.rfq.services.RequestServiceImpl;

@ContextConfiguration(locations = { "classpath: **/applicationContext.xml" })
public class RequestServiceImplTest extends AbstractJUnit4SpringContextTests
{
	@Autowired(required=true)
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
    public void insert_ValidSnippet_CallsDaoInsertMethod()
	{
		// Arrange
		RequestService requestService = new RequestServiceImpl();
		RequestDao requestDaoMock = mock(RequestDaoImpl.class);
		requestService.setRequestDao(requestDaoMock);
		// Act
		requestService.insert("C 100 20Jan2016 0001.HK", Integer.MAX_VALUE, "testBook", "tester");
		// Assert
		verify(requestDaoMock).insert(any(RequestDetailImpl.class));
	}
	
	@Test
    public void insert_InvalidSnippet_ThrowsIllegalArgumentException()
	{
		// Arrange
		RequestService requestService = new RequestServiceImpl();
		RequestDao requestDaoMock = mock(RequestDaoImpl.class);
		requestService.setRequestDao(requestDaoMock);
		// Act
		catchException(this.requestService).insert("testSnippet", Integer.MAX_VALUE, "testBook", "tester");
		// Assert
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "requestSnippet argument is invalid");
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
}
