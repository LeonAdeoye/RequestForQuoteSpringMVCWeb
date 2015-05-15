package com.leon.rfq.service.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.powermock.reflect.Whitebox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.leon.rfq.domains.RequestDetailImpl;
import com.leon.rfq.option.OptionRequestParser;

@ContextConfiguration(locations = { "classpath: **/applicationContext.xml" })
public class OptionRequestParserTest  extends AbstractJUnit4SpringContextTests
{
	@Autowired(required=true)
	private OptionRequestParser optionRequestParser;
	
	@Test
	public void isEuropeanOption_validEuropeanSnippet_ReturnsTrue() throws Exception
	{
		assertTrue("valid European option should return true", Whitebox.invokeMethod(this.optionRequestParser, "isEuropeanOption", "C 100 20JAN2015 0001.HK"));
	}
	
	@Test
	public void isValidOptionRequestSnippet_validRequestSnippet_ReturnsTrue() throws Exception
	{
		assertTrue("should return true if the snippet is valid", Whitebox.invokeMethod(this.optionRequestParser, "isValidOptionRequestSnippet", "C 100 20JAN2015 0001.HK"));
	}
	
	@Test
	public void parseRequest_validRequestSnippet_ReturnsTrue() throws Exception
	{
		RequestDetailImpl request = new RequestDetailImpl();
		this.optionRequestParser.parseRequest("C 100 20JAN2015 0001.HK", request);
		assertEquals(request.getLegs().size(), 1);
	}
}


