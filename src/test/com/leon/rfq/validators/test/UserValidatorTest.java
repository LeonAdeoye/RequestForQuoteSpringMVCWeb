package com.leon.rfq.validators.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.validation.BindException;
import org.springframework.validation.ValidationUtils;

import com.leon.rfq.user.UserImpl;
import com.leon.rfq.validators.UserValidator;

@ContextConfiguration(locations = { "classpath: **/applicationContext.xml" })
public class UserValidatorTest extends AbstractJUnit4SpringContextTests
{
	@Autowired
	private UserValidator userValidator;
	
	@Test
	public void User_EmptylastName_ShouldBeInvalidated()
	{
		// Arrange
		UserImpl user = new UserImpl("testUser", "emailAddress@test.com", "firstName",
			"", "locationName", "groupName", true, "testUser");
		
		BindException bindException = new BindException(user, "user");
		
		// Act
		ValidationUtils.invokeValidator(this.userValidator, user, bindException);
		
		// Assert
		assertEquals(1, bindException.getErrorCount());
		assertTrue(bindException.getLocalizedMessage().contains("Last name must be 1 to 20 characters in length"));
	}
	
	@Test
	public void User_ValidParameters_ShouldBeValidated()
	{
		// Arrange
		UserImpl user = new UserImpl("testUser", "emailAddress@test.com", "firstName",
			"lastName", "locationName", "groupName", true, "testUser");
		
		BindException bindException = new BindException(user, "user");
		
		// Act
		ValidationUtils.invokeValidator(this.userValidator, user, bindException);
		
		// Assert
		assertEquals(0, bindException.getErrorCount());
	}
	
	@Test
	public void User_EmptyFirstName_ShouldBeInvalidated()
	{
		// Arrange
		UserImpl user = new UserImpl("testUser", "emailAddress@test.com", "",
			"lastName", "locationName", "groupName", true, "testUser");
		
		BindException bindException = new BindException(user, "user");
		
		// Act
		ValidationUtils.invokeValidator(this.userValidator, user, bindException);
		
		// Assert
		assertEquals(1, bindException.getErrorCount());
		assertTrue(bindException.getLocalizedMessage().contains("First name must be 1 to 20 characters in length"));
	}
	
	@Test
	public void User_InvalidUserId_ShouldBeInvalidated()
	{
		// Arrange
		UserImpl user = new UserImpl("123456789012345678901234567890", "emailAddress@test.com", "firstName",
			"lastName", "locationName", "groupName", true, "testUser");
		
		BindException bindException = new BindException(user, "user");
		
		// Act
		ValidationUtils.invokeValidator(this.userValidator, user, bindException);
		
		// Assert
		assertEquals(1, bindException.getErrorCount());
		assertTrue(bindException.getLocalizedMessage().contains("User ID must be 1 to 20 characters in length"));
	}
	
	@Test
	public void User_EmailAddressMissingAT_ShouldBeInvalidated()
	{
		// Arrange
		UserImpl user = new UserImpl("testUser", "test.com", "firstName",
			"lastName", "locationName", "groupName", true, "testUser");
		
		BindException bindException = new BindException(user, "user");
		
		// Act
		ValidationUtils.invokeValidator(this.userValidator, user, bindException);
		
		// Assert
		assertEquals(1, bindException.getErrorCount());
		assertTrue(bindException.getLocalizedMessage().contains("Email address must contain both a . and an @ character"));
	}
	
	@Test
	public void User_EmailAddressMissingDOT_ShouldBeInvalidated()
	{
		// Arrange
		UserImpl user = new UserImpl("testUser", "test@com", "firstName",
			"lastName", "locationName", "groupName", true, "testUser");
		
		BindException bindException = new BindException(user, "user");
		
		// Act
		ValidationUtils.invokeValidator(this.userValidator, user, bindException);
		
		// Assert
		assertEquals(1, bindException.getErrorCount());
		assertTrue(bindException.getLocalizedMessage().contains("Email address must contain both a . and an @ character"));
	}
	
}
