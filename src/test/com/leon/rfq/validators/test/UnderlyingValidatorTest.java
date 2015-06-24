package com.leon.rfq.validators.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.validation.BindException;
import org.springframework.validation.ValidationUtils;

import com.leon.rfq.domains.UnderlyingDetailImpl;
import com.leon.rfq.validators.UnderlyingValidatorImpl;

@ContextConfiguration(locations = { "classpath: **/applicationContext.xml" })
public class UnderlyingValidatorTest extends AbstractJUnit4SpringContextTests
{
	@Autowired
	private UnderlyingValidatorImpl underlyingValidator;
	
	@Test
	public void Underlying_ValidParameters_ShouldBeValidated()
	{
		// Arrange
		UnderlyingDetailImpl underlying = new UnderlyingDetailImpl("testRic", "test description", BigDecimal.TEN, BigDecimal.ONE, BigDecimal.ONE, true, "testUser");
		
		BindException bindException = new BindException(underlying, "underlying");
		
		// Act
		ValidationUtils.invokeValidator(this.underlyingValidator, underlying, bindException);
		
		// Assert
		assertEquals("Error count should be zero if all parameters are valid", 0, bindException.getErrorCount());
	}
	
	@Test
	public void Underlying_EmptyDescription_ShouldBeInvalidated()
	{
		// Arrange
		UnderlyingDetailImpl underlying = new UnderlyingDetailImpl("testRic", "", BigDecimal.TEN,  BigDecimal.ONE, BigDecimal.ONE, true, "testUser");
		
		BindException bindException = new BindException(underlying, "underlying");
		
		// Act
		ValidationUtils.invokeValidator(this.underlyingValidator, underlying, bindException);
		
		// Assert
		assertEquals("Error count should be one if the description is empty", 1, bindException.getErrorCount());
		assertTrue("Error message should match if the description is empty", bindException.getLocalizedMessage().contains("Description must be 1 to 45 characters in length"));
	}
	
	@Test
	public void Underlying_RicTooLong_ShouldBeInvalidated()
	{
		// Arrange
		UnderlyingDetailImpl underlying = new UnderlyingDetailImpl("012345678901234567890", "test description" , BigDecimal.TEN, BigDecimal.ONE, BigDecimal.ONE, true, "testUser");
		
		BindException bindException = new BindException(underlying, "underlying");
		
		// Act
		ValidationUtils.invokeValidator(this.underlyingValidator, underlying, bindException);
		
		// Assert
		assertEquals("Error count should be one if the ric is too long", 1, bindException.getErrorCount());
		assertTrue("Error message should match if the ric is too long", bindException.getLocalizedMessage().contains("RIC must be 1 to 10 characters in length"));
	}
	
	@Test
	public void Underlying_DescriptionTooLong_ShouldBeInvalidated()
	{
		// Arrange
		UnderlyingDetailImpl underlying = new UnderlyingDetailImpl("testRic",
				"012345678901234567890123456789012345678901234567890", BigDecimal.TEN,  BigDecimal.ONE, BigDecimal.ONE, true, "testUser");
		
		BindException bindException = new BindException(underlying, "underlying");
		
		// Act
		ValidationUtils.invokeValidator(this.underlyingValidator, underlying, bindException);
		// Assert
		assertEquals("Error count should be one if the description is too long", 1, bindException.getErrorCount());
		assertTrue("Error message should match if the description is too long", bindException.getLocalizedMessage().contains("Description must be 1 to 45 characters in length"));
	}
}
