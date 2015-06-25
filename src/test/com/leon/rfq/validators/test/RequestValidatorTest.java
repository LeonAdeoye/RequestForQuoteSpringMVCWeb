package com.leon.rfq.validators.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.validation.BindException;
import org.springframework.validation.ValidationUtils;

import com.leon.rfq.domains.RequestDetailImpl;
import com.leon.rfq.validators.RequestValidatorImpl;

@ContextConfiguration(locations = { "classpath: **/applicationContext.xml" })
public class RequestValidatorTest extends AbstractJUnit4SpringContextTests
{
	@Autowired
	RequestValidatorImpl requestValidator;
	
	@Test
	public void Request_UnderlyingExists_ShouldBeValidated()
	{
		// Arrange
		RequestDetailImpl request = new RequestDetailImpl();
		request.setRequest("C 100 20Jan2017 0001.HK");
		request.setBookCode("testBook");
		request.setSalesComment("test comment");
		request.setTraderComment("test comment");
		request.setClientComment("test comment");
		request.setPickedUpBy("test user");
		BindException bindException = new BindException(request, "request");
		
		// Act
		ValidationUtils.invokeValidator(this.requestValidator, request, bindException);
		
		// Assert
		assertEquals("Underlying does exist and should be validated. Errors: [" + bindException.getAllErrors() + "]", 1, bindException.getErrorCount());
	}
	
	@Test
	public void Request_UnderlyingDoesNotExist_ShouldBeInvalidated()
	{
		// Arrange
		RequestDetailImpl request = new RequestDetailImpl();
		request.setRequest("C 100 20Jan2017 0001.TP");
		request.setBookCode("testBook");
		request.setSalesComment("test comment");
		request.setTraderComment("test comment");
		request.setClientComment("test comment");
		request.setPickedUpBy("test user");
		BindException bindException = new BindException(request, "request");
		
		// Act
		ValidationUtils.invokeValidator(this.requestValidator, request, bindException);
		
		// Assert
		assertEquals("Underlying does NOT exist and should be invalidated. Errors: [" + bindException.getAllErrors() + "]", 1, bindException.getErrorCount());
	}
	
	@Test
	public void Request_ValidSnippet_ShouldBeValidated()
	{
		// Arrange
		RequestDetailImpl request = new RequestDetailImpl();
		request.setRequest("p 100 20Jan2017 0001.HK");
		request.setBookCode("testBook");
		request.setSalesComment("test comment");
		request.setTraderComment("test comment");
		request.setClientComment("test comment");
		request.setPickedUpBy("test user");
		BindException bindException = new BindException(request, "request");
		
		// Act
		ValidationUtils.invokeValidator(this.requestValidator, request, bindException);
		
		// Assert
		assertEquals("Request snippet is in expected format and should be validated. Errors: [" + bindException.getAllErrors() + "]", 1, bindException.getErrorCount());
	}
	
	@Test
	public void Request_InvalidSnippet_ShouldBeInvalidated()
	{
		// Arrange
		RequestDetailImpl request = new RequestDetailImpl();
		request.setRequest("X 100 20Jan2017 0001.HK");
		request.setBookCode("testBook");
		request.setSalesComment("test comment");
		request.setTraderComment("test comment");
		request.setClientComment("test comment");
		request.setPickedUpBy("test user");
		BindException bindException = new BindException(request, "request");
		
		// Act
		ValidationUtils.invokeValidator(this.requestValidator, request, bindException);
		
		// Assert
		assertEquals("Request snippet is NOT in expected format and should be invalidated. Errors: [" + bindException.getAllErrors() + "]", 2, bindException.getErrorCount());
	}
}
