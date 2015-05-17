package com.leon.rfq.services.test;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.leon.rfq.repositories.UnderlyingDao;
import com.leon.rfq.repositories.UnderlyingDaoImpl;
import com.leon.rfq.services.UnderlyingService;
import com.leon.rfq.services.UnderlyingServiceImpl;

@ContextConfiguration(locations = { "classpath: **/applicationContext.xml" })
public class UnderlyingServiceImplTest extends AbstractJUnit4SpringContextTests
{
	@Autowired
	private UnderlyingService underlyingService;
	
	@Before
	public void setUp()
	{
		assertNotNull("autowired underlyingDaoImpl should not be null", this.underlyingService);
	}
	
	@Test
    public void getAll_ValidScenario_ReturnsValidListOfAllUsers()
	{
		assertNotNull("getAll method should return a non-null list of underlyings", this.underlyingService.getAll());
	}
	
	@Test
    public void get_NullRic_ThrowsIllegalArgumentException()
	{
		catchException(this.underlyingService).get(null);
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "ric argument is invalid");
	}
	
	@Test
    public void get_EmptyStringRic_ThrowsIllegalArgumentException()
	{
		catchException(this.underlyingService).get("");
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "ric argument is invalid");
	}
	
	@Test
    public void get_ValidRic_CallsDaoGetMethod()
	{
		// Arrange
		UnderlyingService underlyingService = new UnderlyingServiceImpl();
		UnderlyingDao underlyingDaoMock = mock(UnderlyingDaoImpl.class);
		underlyingService.setUnderlyingDao(underlyingDaoMock);
		// Act
		underlyingService.get("testRIC");
		// Assert
		verify(underlyingDaoMock).get("testRIC");
	}
	
	@Test
    public void getAll_NoParameters_CallsDaoGetAllMethod()
	{
		// Arrange
		UnderlyingService underlyingService = new UnderlyingServiceImpl();
		UnderlyingDao underlyingDaoMock = mock(UnderlyingDaoImpl.class);
		underlyingService.setUnderlyingDao(underlyingDaoMock);
		// Act
		underlyingService.getAll();
		// Assert
		verify(underlyingDaoMock).getAll();
	}
	
	@Test
    public void delete_validRic_CallsDaoDeleteMethod()
	{
		// Arrange
		UnderlyingService underlyingService = new UnderlyingServiceImpl();
		UnderlyingDao underlyingDaoMock = mock(UnderlyingDaoImpl.class);
		underlyingService.setUnderlyingDao(underlyingDaoMock);
		// Act
		underlyingService.delete("testRIC");
		// Assert
		verify(underlyingDaoMock, never()).delete("testRIC");
	}
		
	@Test
    public void insert_validParameters_CallsDaoSaveMethod()
	{
		// Arrange
		UnderlyingService underlyingService = new UnderlyingServiceImpl();
		UnderlyingDao underlyingDaoMock = mock(UnderlyingDaoImpl.class);
		underlyingService.setUnderlyingDao(underlyingDaoMock);
		// Act
		underlyingService.insert("testRIC", "description", true, "tester");
		// Assert
		verify(underlyingDaoMock).insert("testRIC", "description", true, "tester");
	}
	
	@Test
    public void insert_NullRic_ThrowsIllegalArgumentException()
	{
		catchException(this.underlyingService).insert(null, "description", true, "tester");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "ric argument is invalid");
	}
	
	@Test
    public void insert_EmptyStringRic_ThrowsIllegalArgumentException()
	{
		catchException(this.underlyingService).insert("", "description", true, "tester");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "ric argument is invalid");
	}
	
	@Test
    public void insert_NullDescription_ThrowsIllegalArgumentExceptionn()
	{
		catchException(this.underlyingService).insert("ric", null, true, "tester");
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "description argument is invalid");
	}
	
	@Test
    public void insert_EmptyStringDescription_ThrowsIllegalArgumentException()
	{
		catchException(this.underlyingService).insert("ric", "", true, "tester");
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "description argument is invalid");
	}
		
	@Test
    public void insert_NullSavedByUser_ThrowsIllegalArgumentException()
	{
		catchException(this.underlyingService).insert("ric", "description", true, null);
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "savedByUser argument is invalid");
	}
	
	@Test
    public void insert_EmptyStringSavedByUser_ThrowsIllegalArgumentException()
	{
		catchException(this.underlyingService).insert("ric", "description", true, "");
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "savedByUser argument is invalid");
	}
	
	@Test
    public void delete_NullRic_ThrowsIllegalArgumentException()
	{
		catchException(this.underlyingService).delete(null);
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "ric argument is invalid");
	}
	
	@Test
    public void delete_EmptyStringRic_ThrowsIllegalArgumentException()
	{
		catchException(this.underlyingService).delete("");
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "ric argument is invalid");
	}
	
	@Test
    public void underlyingExistsWithRic_EmptyRic_ThrowsIllegalArgumentException()
	{
		catchException(this.underlyingService).underlyingExistsWithRic("");
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "ric argument is invalid");
	}
	
	@Test
    public void underlyingExistsWithRic_NullRic_ThrowsIllegalArgumentException()
	{
		catchException(this.underlyingService).underlyingExistsWithRic(null);
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "ric argument is invalid");
	}
	
	@Test
    public void underlyingExistsWithRic_existingRic__CallsCorrectDaoMethod()
	{
		// Arrange
		UnderlyingService underlyingService = new UnderlyingServiceImpl();
		UnderlyingDao underlyingDaoMock = mock(UnderlyingDaoImpl.class);
		underlyingService.setUnderlyingDao(underlyingDaoMock);
		// Act
		underlyingService.underlyingExistsWithRic("ric");
		// Assert
		verify(underlyingDaoMock).underlyingExistsWithRic("ric");
	}
	
	@Test
    public void update_NullRic_ThrowsIllegalArgumentException()
	{
		catchException(this.underlyingService).update(null, "description", true, "tester");
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "ric argument is invalid");
	}
	
	@Test
    public void update_EmptyStringRic_ThrowsIllegalArgumentException()
	{
		catchException(this.underlyingService).update("", "description", true, "tester");
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "ric argument is invalid");
	}
	
	@Test
    public void update_NullDescription_ThrowsIllegalArgumentException()
	{
		catchException(this.underlyingService).update("ric", null, true, "tester");
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "description argument is invalid");
	}
	
	@Test
    public void update_EmptyStringDescription_ThrowsIllegalArgumentException()
	{
		catchException(this.underlyingService).update("ric", "", true, "tester");
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "description argument is invalid");
	}
		
	@Test
    public void update_NullUpdatedByUser_ThrowsIllegalArgumentException()
	{
		catchException(this.underlyingService).update("ric", "description", true, null);
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "updatedByUser argument is invalid");
	}
	
	@Test
    public void update_EmptyStringUpdatedByUser_ThrowsIllegalArgumentException()
	{
		catchException(this.underlyingService).update("ric", "description", true, "");
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "updatedByUser argument is invalid");
	}
	
}
