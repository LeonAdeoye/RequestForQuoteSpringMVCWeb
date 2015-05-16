package com.leon.rfq.service.test;

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
import com.leon.rfq.option.OptionRequestFactory;

@ContextConfiguration(locations = { "classpath: **/applicationContext.xml" })
public class OptionRequestFactoryImplTest  extends AbstractJUnit4SpringContextTests
{
	@Autowired(required=true)
	private OptionRequestFactory optionRequestFactory;
	
	@Test
	public void isEuropeanOption_validEuropeanSnippet_ReturnsTrue() throws Exception
	{
		assertTrue("valid European option should return true", Whitebox.invokeMethod(this.optionRequestFactory, "isEuropeanOption", "C 100 20Jan2015 0001.HK"));
	}
	
	@Test
	public void isValidOptionRequestSnippet_validRequestSnippet_ReturnsTrue() throws Exception
	{
		assertTrue("should return true if the snippet is valid", Whitebox.invokeMethod(this.optionRequestFactory, "isValidOptionRequestSnippet", "C 100 20Jan2015 0001.HK"));
	}
	
	@Test
	public void parseRequest_validRequestSnippet_ReturnsTrue() throws Exception
	{
		RequestDetailImpl newRequest = new RequestDetailImpl();
		Whitebox.invokeMethod(this.optionRequestFactory, "parseRequest", "C 100 20Jan2015 0001.HK", newRequest );
		assertEquals(newRequest.getLegs().size(), 1);
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
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "requestSnippet argument is invalid");
	}
}


