package com.leon.rfq.product.test;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.powermock.reflect.Whitebox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.leon.rfq.domains.RequestDetailImpl;
import com.leon.rfq.products.OptionRequestFactory;


@ContextConfiguration(locations = { "classpath: **/applicationContext.xml" })
/*@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(JUnitParamsRunner.class)
@PrepareForTest(OptionRequestFactoryImpl.class)*/
public class OptionRequestFactoryImplTest extends AbstractJUnit4SpringContextTests
{
	@SuppressWarnings("unused")
	private static final Object[] getRequestSnippet()
	{
		return new Object[]
		{
			new Object[] {"C 100 10Jan2016 0001.HK"},
			new Object[] {"C+P 100 10Jan2016 0001.HK"},
			new Object[] {"C-P 100 10Jan2016 0001.HK"},
			new Object[] {"2C 100 10Jan2016 0001.HK"},
			new Object[] {"-C 100 10Jan2016 0001.HK"},
			new Object[] {"+C 100 10Jan2016 0001.HK"},
			new Object[] {"+C 10 10Nov2016 00001.HK"},
			new Object[] {"+C-2P 10 10Nov2016 00001.HK"},
			new Object[] {"+C-P 10 10Nov2016 00001.HK"},
			new Object[] {"-C-P 10 10Nov2016 00001.HK,1000.JP"},
			new Object[] {"-1P 10 10Nov2016 00001.HK"}
		};
	}
	
	@Autowired(required=true)
	private OptionRequestFactory optionRequestFactory;
	
/*	@Test
	public void isEuropeanOption_validEuropeanCallSnippet_ReturnsTrue() throws Exception
	{
		assertTrue("valid European call option should return true", Whitebox.invokeMethod(this.optionRequestFactory, "isEuropeanOption", "C 100 20Jan2020 0001.HK"));
	}
	
	@Test
	public void isEuropeanOption_validEuropeanPutSnippet_ReturnsTrue() throws Exception
	{
		assertTrue("valid European put option should return true", Whitebox.invokeMethod(this.optionRequestFactory, "isEuropeanOption", "P 100 20Jan2025 0001.HK"));
	}
	
	@Test
	public void isEuropeanOption_validAmericanCallSnippet_ReturnsTrue() throws Exception
	{
		assertFalse("valid American call option should return true", Whitebox.invokeMethod(this.optionRequestFactory, "isEuropeanOption", "c 100 20Jan2025 0001.HK"));
	}
	
	@Test
	public void isEuropeanOption_validAmericanPutSnippet_ReturnsTrue() throws Exception
	{
		assertFalse("valid American put option should return true", Whitebox.invokeMethod(this.optionRequestFactory, "isEuropeanOption", "p 100 20Jan2025 0001.HK"));
	}*/
	
/*	@Test
	@Parameters(method="getRequestSnippet")
	public void isValidOptionRequestSnippet_validRequestSnippet_ReturnsTrue(String requestSnippet) throws Exception
	{
		assertTrue("should return true if the snippet is valid", this.optionRequestFactory.isValidOptionRequestSnippet(requestSnippet));
	}*/
	
	@Test
	public void parseRequest_validCPlusPRequestSnippet_ReturnsTrue() throws Exception
	{
		RequestDetailImpl newRequest = new RequestDetailImpl();
		Whitebox.invokeMethod(this.optionRequestFactory, "parseRequest", "C+P 100 20Jan2020 0001.HK", newRequest );
		assertEquals("Number of legs for new request should be 2", 2, newRequest.getLegs().size());
	}
	
	@Test
	public void parseRequest_valid_c_RequestSnippet_ReturnsTrue() throws Exception
	{
		RequestDetailImpl newRequest = new RequestDetailImpl();
		Whitebox.invokeMethod(this.optionRequestFactory, "parseRequest", "c 100 20Jan2030 0001.HK", newRequest );
		assertEquals("Number of legs for new request should be 1", 1, newRequest.getLegs().size());
	}
	
	@Test
	public void parseRequest_valid_P_RequestSnippet_ReturnsTrue() throws Exception
	{
		RequestDetailImpl newRequest = new RequestDetailImpl();
		Whitebox.invokeMethod(this.optionRequestFactory, "parseRequest", "P 100 20Jan2030 0001.HK", newRequest );
		assertEquals("Number of legs for new request should be 1", 1, newRequest.getLegs().size());
	}
	
	@Test
	public void parseRequest_valid_p_RequestSnippet_ReturnsTrue() throws Exception
	{
		RequestDetailImpl newRequest = new RequestDetailImpl();
		Whitebox.invokeMethod(this.optionRequestFactory, "parseRequest", "p 100 20Jan2030 0001.HK", newRequest );
		assertEquals("Number of legs for new request should be 1", 1, newRequest.getLegs().size());
	}
	
	@Test
	public void parseRequest_InvalidMaturityDate_ReturnsFalse() throws Exception
	{
		RequestDetailImpl newRequest = new RequestDetailImpl();
		boolean result = Whitebox.invokeMethod(this.optionRequestFactory, "parseRequest", "C 100 20Jan2010 0001.HK", newRequest );
		assertFalse("should return false because maturity precedes today", result);
	}
	
	@Test
	public void getNewInstance_validRequestSnippet_ReturnsValidRequestNewInstance() throws Exception
	{
		RequestDetailImpl request = this.optionRequestFactory.getNewInstance("C 100 20Jan2030 0001.HK", 1, "AB01", "testuser");
		assertEquals("should create one leg", 1, request.getLegs().size());
	}
	
	@Test
	public void getNewInstance_InvalidUnderlyingsSnippet_ReturnsValidRequestNewInstance() throws Exception
	{
		// Arrange and Act
		catchException(this.optionRequestFactory).getNewInstance("C+P 100 20Jan2030 0001.HK,0005.HK", 1, "AB01", "testuser");
		// Assert
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", "snippet argument is invalid", caughtException().getMessage());
	}
	
	@Test
	public void getNewInstance_InvalidRequestSnippet_ThrowsIllegalArgumentException()
	{
		// Arrange and Act
		catchException(this.optionRequestFactory).getNewInstance("invalidSnippet", 1, "AB01", "testuser");
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", "snippet argument is invalid", caughtException().getMessage());
	}
}


