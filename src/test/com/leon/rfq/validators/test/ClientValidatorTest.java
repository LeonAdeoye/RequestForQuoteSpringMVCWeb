package com.leon.rfq.validators.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.validation.BindException;
import org.springframework.validation.ValidationUtils;

import com.leon.rfq.common.EnumTypes.ClientTierEnum;
import com.leon.rfq.domains.ClientDetailImpl;
import com.leon.rfq.validators.ClientValidator;

@ContextConfiguration(locations = { "classpath: **/applicationContext.xml" })
public class ClientValidatorTest extends AbstractJUnit4SpringContextTests
{
	@Autowired
	private ClientValidator clientValidator;
	
	@Test
	public void Client_ValidParameters_ShouldBeValidated()
	{
		// Arrange
		ClientDetailImpl client = new ClientDetailImpl("testClient", ClientTierEnum.Bottom, true, "tester");
		
		BindException bindException = new BindException(client, "client");
		
		// Act
		ValidationUtils.invokeValidator(this.clientValidator, client, bindException);
		
		// Assert
		assertEquals("Error count should be zero if all parameters are valid", 0, bindException.getErrorCount());
	}
	
	@Test
	public void Client_ClientNameTooLong_ShouldBeInvalidated()
	{
		// Arrange
		ClientDetailImpl client = new ClientDetailImpl("12345678901234567890123456789012345678901234567890", ClientTierEnum.Bottom, true, "tester");
		
		BindException bindException = new BindException(client, "client");
		
		// Act
		ValidationUtils.invokeValidator(this.clientValidator, client, bindException);
		
		// Assert
		assertEquals("Error count should be one if the client name is too long", 1, bindException.getErrorCount());
		assertTrue("Error message should match if the client is too long", bindException.getLocalizedMessage().contains("Client name must be 1 to 45 characters in length"));
	}
}
