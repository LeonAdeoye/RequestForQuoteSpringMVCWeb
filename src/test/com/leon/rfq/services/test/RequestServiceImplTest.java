package com.leon.rfq.services.test;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.leon.rfq.domains.OptionDetailImpl;
import com.leon.rfq.domains.PriceDetailImpl;
import com.leon.rfq.domains.RequestDetailImpl;
import com.leon.rfq.products.BlackScholesModelImpl;
import com.leon.rfq.products.OptionRequestFactory;
import com.leon.rfq.products.OptionRequestFactoryImpl;
import com.leon.rfq.products.PricingModel;
import com.leon.rfq.repositories.RequestDao;
import com.leon.rfq.repositories.RequestDaoImpl;
import com.leon.rfq.services.CalculationService;
import com.leon.rfq.services.CalculationServiceImpl;
import com.leon.rfq.services.PriceService;
import com.leon.rfq.services.PriceServiceImpl;
import com.leon.rfq.services.RequestService;
import com.leon.rfq.services.RequestServiceImpl;

@ContextConfiguration(locations = { "classpath: **/applicationContext.xml" })
public final class RequestServiceImplTest extends AbstractJUnit4SpringContextTests
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
		RequestDao requestDaoMock = mock(RequestDaoImpl.class);
		this.requestService.setRequestDao(requestDaoMock);
		// Act
		this.requestService.get(Integer.MAX_VALUE);
		// Assert
		verify(requestDaoMock).get(Integer.MAX_VALUE);
	}
	
	@Test
    public void getAll_NoParameters_CallsDaoGetAllMethod()
	{
		// Arrange
		RequestDao requestDaoMock = mock(RequestDaoImpl.class);
		this.requestService.setRequestDao(requestDaoMock);
		// Act
		this.requestService.getAll();
		// Assert
		verify(requestDaoMock).getAll();
	}
	
	@Test
    public void delete_NonExistantRequestId_DoesNotCallDaoDeleteMethod()
	{
		// Arrange
		RequestDao requestDaoMock = mock(RequestDaoImpl.class);
		this.requestService.setRequestDao(requestDaoMock);
		// Act
		this.requestService.delete(Integer.MAX_VALUE);
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
		OptionRequestFactory factoryMock = mock(OptionRequestFactoryImpl.class);
		requestService.setOptionRequestFactory(factoryMock);
		
		RequestDetailImpl request = new RequestDetailImpl();
		List<OptionDetailImpl> legs = new ArrayList<>(2);
		request.setLegs(legs);
		
		CalculationService calculationServiceMock = mock(CalculationServiceImpl.class);
		requestService.setCalculationService(calculationServiceMock);
		PricingModel modelMock = mock(BlackScholesModelImpl.class);
		Mockito.doNothing().when(calculationServiceMock).calculate(modelMock, request);
		
		when(factoryMock.getNewInstance("C 100 20Jan2016 0001.HK", Integer.MAX_VALUE, "testBook", "tester")).thenReturn(request);
		// Act
		requestService.insert("C 100 20Jan2016 0001.HK", Integer.MAX_VALUE, "testBook", "tester");
		// Assert
		verify(requestDaoMock).insert(any(RequestDetailImpl.class));
	}
	
	@Test
    public void insert_FactoryReturnsNullRequest_NeverCallsDaoInsertMethod()
	{
		// Arrange
		RequestService requestService = new RequestServiceImpl();
		RequestDao requestDaoMock = mock(RequestDaoImpl.class);
		OptionRequestFactory optionRequestFactoryMock = mock(OptionRequestFactoryImpl.class);
		requestService.setRequestDao(requestDaoMock);
		requestService.setOptionRequestFactory(optionRequestFactoryMock);
		when(optionRequestFactoryMock.getNewInstance("C 100 20Jan2016 0001.HK", Integer.MAX_VALUE, "testBook", "tester")).thenReturn(null);
		// Act
		boolean result = requestService.insert("C 100 20Jan2016 0001.HK", Integer.MAX_VALUE, "testBook", "tester");
		// Assert
		verify(requestDaoMock, never()).insert(any(RequestDetailImpl.class));
		assertFalse("insert should return false", result);

	}
	
	@Test
    public void insert_InvalidSnippet_ThrowsIllegalArgumentException()
	{
		// Arrange
		RequestDao requestDaoMock = mock(RequestDaoImpl.class);
		this.requestService.setRequestDao(requestDaoMock);
		// Act
		catchException(this.requestService).insert("testSnippet", Integer.MAX_VALUE, "testBook", "tester");
		// Assert
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "snippet argument is invalid");
	}
	
	@Test
    public void insert_EmptySnippet_ThrowsIllegalArgumentException()
	{
		// Arrange
		RequestService requestService = new RequestServiceImpl();
		RequestDao requestDaoMock = mock(RequestDaoImpl.class);
		requestService.setRequestDao(requestDaoMock);
		// Act
		catchException(requestService).insert("", Integer.MAX_VALUE, "testBook", "tester");
		// Assert
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "requestSnippet argument is invalid");
	}
	
	@Test
    public void insert_NullSnippet_ThrowsIllegalArgumentException()
	{
		// Arrange
		RequestDao requestDaoMock = mock(RequestDaoImpl.class);
		this.requestService.setRequestDao(requestDaoMock);
		// Act
		catchException(this.requestService).insert(null, Integer.MAX_VALUE, "testBook", "tester");
		// Assert
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "requestSnippet argument is invalid");
	}
	
	@Test
    public void insert_EmptyBookCode_ThrowsIllegalArgumentException()
	{
		// Arrange
		RequestDao requestDaoMock = mock(RequestDaoImpl.class);
		this.requestService.setRequestDao(requestDaoMock);
		// Act
		catchException(this.requestService).insert("C 100 20Jan2016 0001.HK", Integer.MAX_VALUE, "", "tester");
		// Assert
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "bookCode argument is invalid");
	}
	
	@Test
    public void insert_NullBookCode_ThrowsIllegalArgumentException()
	{
		// Arrange
		RequestDao requestDaoMock = mock(RequestDaoImpl.class);
		this.requestService.setRequestDao(requestDaoMock);
		// Act
		catchException(this.requestService).insert("C 100 20Jan2016 0001.HK", Integer.MAX_VALUE, null, "tester");
		// Assert
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "bookCode argument is invalid");
	}
	
	@Test
    public void insert_EmptySavedByUser_ThrowsIllegalArgumentException()
	{
		// Arrange
		RequestDao requestDaoMock = mock(RequestDaoImpl.class);
		this.requestService.setRequestDao(requestDaoMock);
		// Act
		catchException(this.requestService).insert("C 100 20Jan2016 0001.HK", Integer.MAX_VALUE, "testBook", "");
		// Assert
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "savedByUser argument is invalid");
	}
	
	@Test
    public void insert_NullSavedByUser_ThrowsIllegalArgumentException()
	{
		// Arrange
		RequestDao requestDaoMock = mock(RequestDaoImpl.class);
		this.requestService.setRequestDao(requestDaoMock);
		// Act
		catchException(this.requestService).insert("C 100 20Jan2016 0001.HK", Integer.MAX_VALUE, "testBook", null);
		// Assert
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "savedByUser argument is invalid");
	}
		
	@Test
    public void requestExistsWithRequestId_ExistingRequestId_CallsCorrectDaoMethod()
	{
		// Arrange
		RequestDao requestDaoMock = mock(RequestDaoImpl.class);
		this.requestService.setRequestDao(requestDaoMock);
		// Act
		this.requestService.requestExistsWithRequestId(Integer.MAX_VALUE);
		// Assert
		verify(requestDaoMock).requestExistsWithRequestId(Integer.MAX_VALUE);
	}
	
	@Test
    public void pricesUpdates_ValidRequestsWithFiveDifferentUnderlyingsInTotal_FivePriceDetailsAreReturned()
	{
		// Arrange
		RequestService requestService = new RequestServiceImpl();
		
		PriceService priceServiceMock = mock(PriceServiceImpl.class);
		requestService.setPriceService(priceServiceMock);
		when(priceServiceMock.getAllPrices("0001.HK")).thenReturn(new PriceDetailImpl("0001.HK"));
		when(priceServiceMock.getAllPrices("0002.HK")).thenReturn(new PriceDetailImpl("0002.HK"));
		when(priceServiceMock.getAllPrices("0003.HK")).thenReturn(new PriceDetailImpl("0003.HK"));
		when(priceServiceMock.getAllPrices("0004.HK")).thenReturn(new PriceDetailImpl("0004.HK"));
		when(priceServiceMock.getAllPrices("0005.HK")).thenReturn(new PriceDetailImpl("0005.HK"));
		
		RequestDetailImpl requestA = new RequestDetailImpl();
		List<OptionDetailImpl> legsA = new ArrayList<>(3);
		OptionDetailImpl legA1 = new OptionDetailImpl();
		legA1.setUnderlyingRIC("0001.HK");
		OptionDetailImpl legA2 = new OptionDetailImpl();
		legA2.setUnderlyingRIC("0002.HK");
		OptionDetailImpl legA3 = new OptionDetailImpl();
		legA3.setUnderlyingRIC("0003.HK");
		legsA.add(legA1);
		legsA.add(legA2);
		legsA.add(legA3);
		requestA.setLegs(legsA);
		requestA.setIdentifier(Integer.MIN_VALUE);
		
		RequestDetailImpl requestB = new RequestDetailImpl();
		List<OptionDetailImpl> legsB = new ArrayList<>(3);
		OptionDetailImpl legB1 = new OptionDetailImpl();
		legB1.setUnderlyingRIC("0001.HK");
		OptionDetailImpl legB2 = new OptionDetailImpl();
		legB2.setUnderlyingRIC("0001.HK");
		OptionDetailImpl legB3 = new OptionDetailImpl();
		legB3.setUnderlyingRIC("0004.HK");
		OptionDetailImpl legB4 = new OptionDetailImpl();
		legB4.setUnderlyingRIC("0005.HK");
		legsB.add(legB1);
		legsB.add(legB2);
		legsB.add(legB3);
		legsB.add(legB4);
		requestB.setLegs(legsB);
		requestB.setIdentifier(Integer.MAX_VALUE);
		
		OptionRequestFactory factoryMock = mock(OptionRequestFactoryImpl.class);
		requestService.setOptionRequestFactory(factoryMock);
		when(factoryMock.getNewInstance("C 100 20Jan2016 0001.HK", Integer.MAX_VALUE, "testBook", "tester")).thenReturn(requestA);
		when(factoryMock.getNewInstance("P 100 20Jan2016 0001.HK", Integer.MAX_VALUE, "testBook", "tester")).thenReturn(requestB);
		
		RequestDao requestDaoMock = mock(RequestDaoImpl.class);
		requestService.setRequestDao(requestDaoMock);
		when(requestDaoMock.insert(requestA)).thenReturn(true);
		when(requestDaoMock.insert(requestB)).thenReturn(true);
		
		CalculationService calculationServiceMock = mock(CalculationServiceImpl.class);
		requestService.setCalculationService(calculationServiceMock);
		PricingModel modelMock = mock(BlackScholesModelImpl.class);
		Mockito.doNothing().when(calculationServiceMock).calculate(modelMock, requestA);
		Mockito.doNothing().when(calculationServiceMock).calculate(modelMock, requestB);
		
		// Act
		requestService.insert("C 100 20Jan2016 0001.HK", Integer.MAX_VALUE, "testBook", "tester");
		requestService.insert("P 100 20Jan2016 0001.HK", Integer.MAX_VALUE, "testBook", "tester");
		
		Map<String, PriceDetailImpl> prices = requestService.getPriceUpdates();
		assertEquals(5, prices.keySet().size());
	}
	
	@Test
    public void pricesUpdates_NoRequestsExist_NoPricesAreReturned()
	{
		// Arrange
		RequestService requestService = new RequestServiceImpl();
		
		PriceService priceServiceMock = mock(PriceServiceImpl.class);
		requestService.setPriceService(priceServiceMock);
		
		// Act
		Map<String, PriceDetailImpl> prices = requestService.getPriceUpdates();
		assertEquals(0, prices.keySet().size());
	}
}
