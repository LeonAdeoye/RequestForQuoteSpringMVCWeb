package com.leon.rfq.services.test;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.powermock.reflect.Whitebox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.leon.rfq.domains.RequestDetailImpl;
import com.leon.rfq.products.OptionRequestFactory;


@ContextConfiguration(locations = { "classpath: **/applicationContext.xml" })
//@RunWith(JUnitParamsRunner.class)
//@RunWith(PowerMockRunner.class)
//@PowerMockRunnerDelegate(JUnitParamsRunner.class)
//@PrepareForTest(OptionRequestFactoryImpl.class)

public class OptionRequestFactoryImplTest  extends AbstractJUnit4SpringContextTests
{
	//@Rule
    //public PowerMockRule rule = new PowerMockRule();
	
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
	
	@Test
	public void isEuropeanOption_validEuropeanSnippet_ReturnsTrue() throws Exception
	{
		assertTrue("valid European option should return true", Whitebox.invokeMethod(this.optionRequestFactory, "isEuropeanOption", "C 100 20Jan2015 0001.HK"));
	}
	
	/*@Test
	@Ignore
	@Parameters(method="getRequestSnippet")
	public void isValidOptionRequestSnippet_validRequestSnippet_ReturnsTrue(String requestSnippet) throws Exception
	{
		assertTrue("should return true if the snippet is valid", this.optionRequestFactory.isValidOptionRequestSnippet(requestSnippet));
	}*/
	
	@Test
	public void parseRequest_validRequestSnippet_ReturnsTrue() throws Exception
	{
		RequestDetailImpl newRequest = new RequestDetailImpl();
		Whitebox.invokeMethod(this.optionRequestFactory, "parseRequest", "C 100 20Jan2015 0001.HK", newRequest );
		assertEquals("Number of legs for new request should be 1", newRequest.getLegs().size(), 1);
	}
	
	@Test
	public void getNewInstance_validRequestSnippet_ReturnsValidRequestNewInstance() throws Exception
	{
		RequestDetailImpl request = this.optionRequestFactory.getNewInstance("C 100 20Jan2015 0001.HK", 1, "AB01", "testuser");
		assertEquals("should create one leg", request.getLegs().size(), 1);
	}
	
	@Test
	public void getNewInstance_InvalidRequestSnippet_ThrowsIllegalArgumentException()
	{
		catchException(this.optionRequestFactory).getNewInstance("invalidSnippet", 1, "AB01", "testuser");
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "requestSnippet argument is invalid");
	}
}


