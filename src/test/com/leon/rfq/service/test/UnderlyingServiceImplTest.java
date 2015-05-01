package com.leon.rfq.service.test;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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

import com.leon.rfq.underlying.UnderlyingDao;
import com.leon.rfq.underlying.UnderlyingDaoImpl;
import com.leon.rfq.underlying.UnderlyingService;
import com.leon.rfq.underlying.UnderlyingServiceImpl;

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
    public void get_NullParameter_ThrowsInvalidArgumentException()
	{
		catchException(this.underlyingService).get(null);
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "ric argument is invalid");
	}
	
	@Test
    public void get_EmptyStringParameter_ThrowsInvalidArgumentException()
	{
		catchException(this.underlyingService).get("");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "ric argument is invalid");
	}
	
	@Test
    public void get_ValidParameter_CallsDaoGetMethod()
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
    public void updateValidity_NonExistentRic_DoesNotCallDaoUpdateValidityMethod()
	{
		// Arrange
		UnderlyingService underlyingService = new UnderlyingServiceImpl();
		UnderlyingDao underlyingDaoMock = mock(UnderlyingDaoImpl.class);
		underlyingService.setUnderlyingDao(underlyingDaoMock);
		// Act
		underlyingService.update("testRIC", "test description", true, "tester");
		// Assert
		verify(underlyingDaoMock, never()).update("testRIC", "test description", true, "tester");
	}
	
	@Test
    public void updateValidity_NonExistentRic_ReturnFalse()
	{
		// Arrange
		UnderlyingService underlyingService = new UnderlyingServiceImpl();
		UnderlyingDao underlyingDaoMock = mock(UnderlyingDaoImpl.class);
		underlyingService.setUnderlyingDao(underlyingDaoMock);
		// Act and Assert
		assertFalse("updateValidity should return false if underlying does not exist.",
				underlyingService.update("testRIC", "test descriptin", true, "tester"));
	}
	
	@Test
    public void updateValidity_validRic_CallsDaoUpdateValidityMethod()
	{
		// Arrange
		UnderlyingService underlyingService = new UnderlyingServiceImpl();
		UnderlyingDao underlyingDaoMock = mock(UnderlyingDaoImpl.class);
		underlyingService.setUnderlyingDao(underlyingDaoMock);
		// Act
		underlyingService.update("testRIC", "test description", true, "tester");
		// Assert
		verify(underlyingDaoMock, never()).update("testRIC", "test description", true, "tester");
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
    public void insert_NullRic_ThrowsInvalidArgumentException()
	{
		catchException(this.underlyingService).insert(null, "description", true, "tester");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "ric argument is invalid");
	}
	
	@Test
    public void insert_EmptyStringRic_ThrowsInvalidArgumentException()
	{
		catchException(this.underlyingService).insert("", "description", true, "tester");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "ric argument is invalid");
	}
	
	@Test
    public void insert_NullDescription_ThrowsInvalidArgumentExceptionn()
	{
		catchException(this.underlyingService).insert("ric", null, true, "tester");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "description argument is invalid");
	}
	
	@Test
    public void insert_EmptyStringDescription_ThrowsInvalidArgumentException()
	{
		catchException(this.underlyingService).insert("ric", "", true, "tester");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "description argument is invalid");
	}
		
	@Test
    public void insert_NullSavedByUser_ThrowsInvalidArgumentException()
	{
		catchException(this.underlyingService).insert("ric", "description", true, null);
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "savedByUser argument is invalid");
	}
	
	@Test
    public void insert_EmptyStringSavedByUser_ThrowsInvalidArgumentException()
	{
		catchException(this.underlyingService).insert("ric", "description", true, "");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "savedByUser argument is invalid");
	}
	
	@Test
    public void delete_NullRic_ThrowsInvalidArgumentException()
	{
		catchException(this.underlyingService).delete(null);
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "ric argument is invalid");
	}
	
	@Test
    public void delete_EmptyStringRic_ThrowsInvalidArgumentException()
	{
		catchException(this.underlyingService).delete("");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "ric argument is invalid");
	}
	
	@Test
    public void underlyingExistsWithRic_EmptyRic_ThrowsInvalidArgumentException()
	{
		catchException(this.underlyingService).underlyingExistsWithRic("");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "ric argument is invalid");
	}
	
	@Test
    public void underlyingExistsWithRic_NullRic_ThrowsInvalidArgumentException()
	{
		catchException(this.underlyingService).underlyingExistsWithRic(null);
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "ric argument is invalid");
	}
	
	@Test
    public void underlyingExistsWithRic_existingRic_ReturnsTrue()
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
    public void update_NullRic_ThrowsInvalidArgumentException()
	{
		catchException(this.underlyingService).update(null, "description", true, "tester");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "ric argument is invalid");
	}
	
	@Test
    public void update_EmptyStringRic_ThrowsInvalidArgumentException()
	{
		catchException(this.underlyingService).update("", "description", true, "tester");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "ric argument is invalid");
	}
	
	@Test
    public void update_NullDescription_ThrowsInvalidArgumentException()
	{
		catchException(this.underlyingService).update("ric", null, true, "tester");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "description argument is invalid");
	}
	
	@Test
    public void update_EmptyStringDescription_ThrowsInvalidArgumentException()
	{
		catchException(this.underlyingService).update("ric", "", true, "tester");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "description argument is invalid");
	}
		
	@Test
    public void update_NullUpdatedByUser_ThrowsInvalidArgumentException()
	{
		catchException(this.underlyingService).update("ric", "description", true, null);
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "updatedByUser argument is invalid");
	}
	
	@Test
    public void update_EmptyStringUpdatedByUser_ThrowsInvalidArgumentException()
	{
		catchException(this.underlyingService).update("ric", "description", true, "");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "updatedByUser argument is invalid");
	}
	
}
