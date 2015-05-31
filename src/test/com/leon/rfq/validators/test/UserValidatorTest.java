package com.leon.rfq.validators.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.validation.BindException;
import org.springframework.validation.ValidationUtils;

import com.leon.rfq.domains.UserDetailImpl;
import com.leon.rfq.validators.UserValidatorImpl;

@ContextConfiguration(locations = { "classpath: **/applicationContext.xml" })
public class UserValidatorTest extends AbstractJUnit4SpringContextTests
{
	@Autowired
	private UserValidatorImpl userValidator;
	
	@Test
	public void User_EmptylastName_ShouldBeInvalidated()
	{
		// Arrange
		UserDetailImpl user = new UserDetailImpl("testUser", "emailAddress@test.com", "firstName",
			"", "locationName", "groupName", true, "testUser");
		
		BindException bindException = new BindException(user, "user");
		
		// Act
		ValidationUtils.invokeValidator(this.userValidator, user, bindException);
		
		// Assert
		assertEquals("Error count should be one if the lastName is empty", 1, bindException.getErrorCount());
		assertTrue("Error message should match if the lastName is empty", bindException.getLocalizedMessage().contains("Last name must be 1 to 20 characters in length"));
	}
	
	@Test
	public void User_ValidParameters_ShouldBeValidated()
	{
		// Arrange
		UserDetailImpl user = new UserDetailImpl("testUser", "emailAddress@test.com", "firstName",
			"lastName", "locationName", "groupName", true, "testUser");
		
		BindException bindException = new BindException(user, "user");
		
		// Act
		ValidationUtils.invokeValidator(this.userValidator, user, bindException);
		
		// Assert
		assertEquals("Error count should be zero if all parameters are valid", 0, bindException.getErrorCount());
	}
	
	@Test
	public void User_EmptyFirstName_ShouldBeInvalidated()
	{
		// Arrange
		UserDetailImpl user = new UserDetailImpl("testUser", "emailAddress@test.com", "",
			"lastName", "locationName", "groupName", true, "testUser");
		
		BindException bindException = new BindException(user, "user");
		
		// Act
		ValidationUtils.invokeValidator(this.userValidator, user, bindException);
		
		// Assert
		assertEquals("Error count should be one if the firstName is empty", 1, bindException.getErrorCount());
		assertTrue("Error message should match if the firstName is empty", bindException.getLocalizedMessage().contains("First name must be 1 to 20 characters in length"));
	}
	
	@Test
	public void User_UserIdTooLong_ShouldBeInvalidated()
	{
		// Arrange
		UserDetailImpl user = new UserDetailImpl("123456789012345678901234567890", "emailAddress@test.com", "firstName",
			"lastName", "locationName", "groupName", true, "testUser");
		
		BindException bindException = new BindException(user, "user");
		
		// Act
		ValidationUtils.invokeValidator(this.userValidator, user, bindException);
		
		// Assert
		assertEquals("Error count should be one if the userId is too long", 1, bindException.getErrorCount());
		assertTrue("Error message should match if the userId is too long", bindException.getLocalizedMessage().contains("User ID must be 1 to 20 characters in length"));
	}
	
	@Test
	public void User_FirstNameTooLong_ShouldBeInvalidated()
	{
		// Arrange
		UserDetailImpl user = new UserDetailImpl("testUser", "emailAddress@test.com", "123456789012345678901234567890",
			"lastName", "locationName", "groupName", true, "testUser");
		
		BindException bindException = new BindException(user, "user");
		
		// Act
		ValidationUtils.invokeValidator(this.userValidator, user, bindException);
		
		// Assert
		assertEquals("Error count should be one if the first name is too long", 1, bindException.getErrorCount());
		assertTrue("Error message should match if the first name is too long", bindException.getLocalizedMessage().contains("First name must be 1 to 20 characters in length"));
	}
	
	@Test
	public void User_LastNameTooLong_ShouldBeInvalidated()
	{
		// Arrange
		UserDetailImpl user = new UserDetailImpl("testuser", "emailAddress@test.com", "firstName",
			"123456789012345678901234567890", "locationName", "groupName", true, "testUser");
		
		BindException bindException = new BindException(user, "user");
		
		// Act
		ValidationUtils.invokeValidator(this.userValidator, user, bindException);
		
		// Assert
		assertEquals("Error count should be one if the last name is too long", 1, bindException.getErrorCount());
		assertTrue("Error message should match if the last name is too long", bindException.getLocalizedMessage().contains("Last name must be 1 to 20 characters in length"));
	}
	
	@Test
	public void User_EmailAddressMissingAT_ShouldBeInvalidated()
	{
		// Arrange
		UserDetailImpl user = new UserDetailImpl("testUser", "test.com", "firstName",
			"lastName", "locationName", "groupName", true, "testUser");
		
		BindException bindException = new BindException(user, "user");
		
		// Act
		ValidationUtils.invokeValidator(this.userValidator, user, bindException);
		
		// Assert
		assertEquals("Error count should be one if the email address is missing an @ character", 1, bindException.getErrorCount());
		assertTrue("Error message should match if the email address is missing an @ character", bindException.getLocalizedMessage().contains("Email address must contain both a . and an @ character"));
	}
	
	@Test
	public void User_EmailAddressMissingPeriod_ShouldBeInvalidated()
	{
		// Arrange
		UserDetailImpl user = new UserDetailImpl("testUser", "test@com", "firstName",
			"lastName", "locationName", "groupName", true, "testUser");
		
		BindException bindException = new BindException(user, "user");
		
		// Act
		ValidationUtils.invokeValidator(this.userValidator, user, bindException);
		
		// Assert
		assertEquals("Error count should be one if the email address is missing a period", 1, bindException.getErrorCount());
		assertTrue("Error message should match if the email address is missing a period", bindException.getLocalizedMessage().contains("Email address must contain both a . and an @ character"));
	}
	
}
