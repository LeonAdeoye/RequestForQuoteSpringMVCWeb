package com.leon.rfq.services.test;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;

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
	@Autowired(required=true)
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
		assertEquals("Exception message should match", "ric argument is invalid", caughtException().getMessage());
	}
	
	@Test
    public void get_EmptyStringRic_ThrowsIllegalArgumentException()
	{
		catchException(this.underlyingService).get("");
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", "ric argument is invalid", caughtException().getMessage());
	}
	
	@Test
    public void get_ValidRic_CallsDaoGetMethod()
	{
		// Arrange
		UnderlyingService underlyingService = new UnderlyingServiceImpl(); // Need local instance to clear cache
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
		UnderlyingDao underlyingDaoMock = mock(UnderlyingDaoImpl.class);
		this.underlyingService.setUnderlyingDao(underlyingDaoMock);
		// Act
		this.underlyingService.getAll();
		// Assert
		verify(underlyingDaoMock).getAll();
	}
	
	@Test
    public void delete_validRic_CallsDaoDeleteMethod()
	{
		// Arrange
		UnderlyingDao underlyingDaoMock = mock(UnderlyingDaoImpl.class);
		this.underlyingService.setUnderlyingDao(underlyingDaoMock);
		// Act
		this.underlyingService.delete("testRIC");
		// Assert
		verify(underlyingDaoMock, never()).delete("testRIC");
	}
		
	@Test
    public void insert_validParameters_CallsDaoSaveMethod()
	{
		// Arrange
		UnderlyingDao underlyingDaoMock = mock(UnderlyingDaoImpl.class);
		this.underlyingService.setUnderlyingDao(underlyingDaoMock);
		// Act
		this.underlyingService.insert("testRIC", "description", BigDecimal.TEN,  BigDecimal.ONE, true, "tester");
		// Assert
		verify(underlyingDaoMock).insert("testRIC", "description", BigDecimal.TEN,  BigDecimal.ONE, true, "tester");
	}
	
	@Test
    public void insert_NullRic_ThrowsIllegalArgumentException()
	{
		catchException(this.underlyingService).insert(null, "description", BigDecimal.TEN,  BigDecimal.ONE, true, "tester");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", "ric argument is invalid", caughtException().getMessage());
	}
	
	@Test
    public void insert_EmptyStringRic_ThrowsIllegalArgumentException()
	{
		catchException(this.underlyingService).insert("", "description" , BigDecimal.TEN,  BigDecimal.ONE, true, "tester");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", "ric argument is invalid", caughtException().getMessage());
	}
	
	@Test
    public void insert_NullDescription_ThrowsIllegalArgumentExceptionn()
	{
		catchException(this.underlyingService).insert("ric", null , BigDecimal.TEN,  BigDecimal.ONE, true, "tester");
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", "description argument is invalid", caughtException().getMessage());
	}
	
	@Test
    public void insert_EmptyStringDescription_ThrowsIllegalArgumentException()
	{
		catchException(this.underlyingService).insert("ric", "", BigDecimal.TEN,  BigDecimal.ONE, true, "tester");
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", "description argument is invalid", caughtException().getMessage());
	}
		
	@Test
    public void insert_NullSavedByUser_ThrowsIllegalArgumentException()
	{
		catchException(this.underlyingService).insert("ric", "description", BigDecimal.TEN,  BigDecimal.ONE, true, null);
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", "savedByUser argument is invalid", caughtException().getMessage());
	}
	
	@Test
    public void insert_EmptyStringSavedByUser_ThrowsIllegalArgumentException()
	{
		catchException(this.underlyingService).insert("ric", "description", BigDecimal.TEN,  BigDecimal.ONE, true, "");
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", "savedByUser argument is invalid", caughtException().getMessage());
	}
	
	@Test
    public void insert_InvalidReferencePrice_ThrowsIllegalArgumentException()
	{
		catchException(this.underlyingService).insert("ric", "description", BigDecimal.ZERO,  BigDecimal.ONE, true, "tester");
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", "referencePrice argument is invalid", caughtException().getMessage());
	}
	
	@Test
    public void insert_InvalidSimulationPriceVariance_ThrowsIllegalArgumentException()
	{
		catchException(this.underlyingService).insert("ric", "description", BigDecimal.TEN,  BigDecimal.ZERO, true, "tester");
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", "simulationPriceVariance argument is invalid", caughtException().getMessage());
	}
	
	@Test
    public void delete_NullRic_ThrowsIllegalArgumentException()
	{
		catchException(this.underlyingService).delete(null);
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", "ric argument is invalid", caughtException().getMessage());
	}
	
	@Test
    public void delete_EmptyStringRic_ThrowsIllegalArgumentException()
	{
		catchException(this.underlyingService).delete("");
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", "ric argument is invalid", caughtException().getMessage());
	}
	
	@Test
    public void underlyingExistsWithRic_EmptyRic_ThrowsIllegalArgumentException()
	{
		catchException(this.underlyingService).underlyingExistsWithRic("");
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", "ric argument is invalid", caughtException().getMessage());
	}
	
	@Test
    public void underlyingExistsWithRic_NullRic_ThrowsIllegalArgumentException()
	{
		catchException(this.underlyingService).underlyingExistsWithRic(null);
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", "ric argument is invalid", caughtException().getMessage());
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
    public void update_InvalidReferencePrice_ThrowsIllegalArgumentException()
	{
		catchException(this.underlyingService).update("ric", "description", BigDecimal.ZERO,  BigDecimal.ONE, true, "tester");
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", "referencePrice argument is invalid", caughtException().getMessage());
	}
	
	@Test
    public void update_InvalidSimulationPriceVariance_ThrowsIllegalArgumentException()
	{
		catchException(this.underlyingService).update("ric", "description", BigDecimal.TEN,  BigDecimal.ZERO, true, "tester");
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", "simulationPriceVariance argument is invalid", caughtException().getMessage());
	}
	
	@Test
    public void update_NullRic_ThrowsIllegalArgumentException()
	{
		catchException(this.underlyingService).update(null, "description", BigDecimal.TEN,  BigDecimal.ONE, true, "tester");
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", "ric argument is invalid", caughtException().getMessage());
	}
	
	@Test
    public void update_EmptyStringRic_ThrowsIllegalArgumentException()
	{
		catchException(this.underlyingService).update("", "description", BigDecimal.TEN,  BigDecimal.ONE, true, "tester");
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", "ric argument is invalid", caughtException().getMessage());
	}
	
	@Test
    public void update_NullDescription_ThrowsIllegalArgumentException()
	{
		catchException(this.underlyingService).update("ric", null, BigDecimal.TEN,  BigDecimal.ONE, true, "tester");
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", "description argument is invalid", caughtException().getMessage());
	}
	
	@Test
    public void update_EmptyStringDescription_ThrowsIllegalArgumentException()
	{
		catchException(this.underlyingService).update("ric", "", BigDecimal.TEN,  BigDecimal.ONE, true, "tester");
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", "description argument is invalid", caughtException().getMessage());
	}
		
	@Test
    public void update_NullUpdatedByUser_ThrowsIllegalArgumentException()
	{
		catchException(this.underlyingService).update("ric", "description", BigDecimal.TEN,  BigDecimal.ONE, true, null);
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", "updatedByUser argument is invalid", caughtException().getMessage());
	}
	
	@Test
    public void update_EmptyStringUpdatedByUser_ThrowsIllegalArgumentException()
	{
		catchException(this.underlyingService).update("ric", "description", BigDecimal.TEN,  BigDecimal.ONE, true, "");
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", "updatedByUser argument is invalid", caughtException().getMessage());
	}
	
}
