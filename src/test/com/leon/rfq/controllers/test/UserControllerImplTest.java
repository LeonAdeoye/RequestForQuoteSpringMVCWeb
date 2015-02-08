package com.leon.rfq.controllers.test;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.leon.rfq.controllers.UserController;
import com.leon.rfq.controllers.UserControllerImpl;
import com.leon.rfq.user.UserDao;
import com.leon.rfq.user.UserDaoImpl;

@ContextConfiguration(locations = { "classpath:/applicationContext.xml" })
public class UserControllerImplTest extends AbstractJUnit4SpringContextTests
{
	@Autowired
	private UserControllerImpl controller;
	
	@Before
	public void setUp()
	{
		assertNotNull("autowired userDaoImpl should not be null", this.controller);
	}
	
	@Test
    public void getAll_ValidScenario_ReturnsValidListOfAllUsers()
	{
		assertNotNull("getAll method should return a non-null list of users", this.controller.getAll());
	}
	
	@Test
    public void get_NullParameter_ThrowsNullPointerException()
	{
		catchException(this.controller).get(null);
		
		assertTrue(caughtException() instanceof NullPointerException);
		assertEquals(caughtException().getMessage(), "userId parameter cannot be null");
	}
	
	@Test
    public void get_EmptyStringParameter_ThrowsInvalidArgumentException()
	{
		catchException(this.controller).get("");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "userId parameter cannot be an empty string");
	}
	
	@Test
    public void get_ValidParameter_CallsDaoGetMethod()
	{
		// Arrange
		UserController controller = new UserControllerImpl();
		UserDao userDaoMock = mock(UserDaoImpl.class);
		controller.setUserDao(userDaoMock);
		// Act
		controller.get("testUser");
		// Assert
		verify(userDaoMock).get("testUser");
	}
	
	@Test
    public void getAll_NoParameters_CallsDaoGetAllMethod()
	{
		// Arrange
		UserController controller = new UserControllerImpl();
		UserDao userDaoMock = mock(UserDaoImpl.class);
		controller.setUserDao(userDaoMock);
		// Act
		controller.getAll();
		// Assert
		verify(userDaoMock).getAll();
	}
	
	@Test
    public void delete_validUserId_CallsDaoDeleteMethod()
	{
		// Arrange
		UserController controller = new UserControllerImpl();
		UserDao userDaoMock = mock(UserDaoImpl.class);
		controller.setUserDao(userDaoMock);
		// Act
		controller.delete("userToBeDeleted");
		// Assert
		verify(userDaoMock).delete("userToBeDeleted");
	}
	
	@Test
    public void updateValidity_validUserId_CallsDaoUpdateValidityMethod()
	{
		// Arrange
		UserController controller = new UserControllerImpl();
		UserDao userDaoMock = mock(UserDaoImpl.class);
		controller.setUserDao(userDaoMock);
		// Act
		controller.updateValidity("userToBeUpdated", true, "tester");
		// Assert
		verify(userDaoMock).updateValidity("userToBeUpdated", true, "tester");
	}
	
	@Test
    public void save_validParameters_CallsDaoSaveMethod()
	{
		// Arrange
		UserController controller = new UserControllerImpl();
		UserDao userDaoMock = mock(UserDaoImpl.class);
		controller.setUserDao(userDaoMock);
		// Act
		controller.save("userToBeSaved", "firstName", "lastName", "emailAddress", "location", 0, true, "tester");
		// Assert
		verify(userDaoMock).save("userToBeSaved", "firstName", "lastName", "emailAddress", "location", 0, true, "tester");
	}
	
	@Test
    public void save_NullUserId_ThrowsNullPointerException()
	{
		catchException(this.controller).save(null, "firstName", "lastName", "emailAddress", "location", 0, true, "tester");
		
		assertTrue(caughtException() instanceof NullPointerException);
		assertEquals(caughtException().getMessage(), "userId parameter cannot be null");
	}
	
	@Test
    public void save_EmptyStringUserId_ThrowsInvalidArgumentException()
	{
		catchException(this.controller).save("", "firstName", "lastName", "emailAddress", "location", 0, true, "tester");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "userId parameter cannot be an empty string");
	}
	
	@Test
    public void save_NullFirstName_ThrowsNullPointerException()
	{
		catchException(this.controller).save("userId", null, "lastName", "emailAddress", "location", 0, true, "tester");
		
		assertTrue(caughtException() instanceof NullPointerException);
		assertEquals(caughtException().getMessage(), "firstName parameter cannot be null");
	}
	
	@Test
    public void save_EmptyStringFirstName_ThrowsInvalidArgumentException()
	{
		catchException(this.controller).save("userId", "", "lastName", "emailAddress", "location", 0, true, "tester");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "firstName parameter cannot be an empty string");
	}
	
	@Test
    public void save_NullLastName_ThrowsNullPointerException()
	{
		catchException(this.controller).save("userId", "firstName", null, "emailAddress", "location", 0, true, "tester");
		
		assertTrue(caughtException() instanceof NullPointerException);
		assertEquals(caughtException().getMessage(), "lastName parameter cannot be null");
	}
	
	@Test
    public void save_EmptyStringLastName_ThrowsInvalidArgumentException()
	{
		catchException(this.controller).save("userId", "firstName", "", "emailAddress", "location", 0, true, "tester");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "lastName parameter cannot be an empty string");
	}
	
	@Test
    public void save_NullEmailAddress_ThrowsNullPointerException()
	{
		catchException(this.controller).save("userId", "firstName", "lastName", null, "location", 0, true, "tester");
		
		assertTrue(caughtException() instanceof NullPointerException);
		assertEquals(caughtException().getMessage(), "emailAddress parameter cannot be null");
	}
	
	@Test
    public void save_EmptyStringEmailAddress_ThrowsInvalidArgumentException()
	{
		catchException(this.controller).save("userId", "firstName", "lastName", "", "location", 0, true, "tester");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "emailAddress parameter cannot be an empty string");
	}
	
	@Test
    public void save_NullLocation_ThrowsNullPointerException()
	{
		catchException(this.controller).save("userId", "firstName", "lastName", "emailAddress", null, 0, true, "tester");
		
		assertTrue(caughtException() instanceof NullPointerException);
		assertEquals(caughtException().getMessage(), "locationName parameter cannot be null");
	}
	
	@Test
    public void save_EmptyStringLocation_ThrowsInvalidArgumentException()
	{
		catchException(this.controller).save("userId", "firstName", "lastName", "emailAddress", "", 0, true, "tester");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "locationName parameter cannot be an empty string");
	}
	
	@Test
    public void save_NullSavedByUser_ThrowsNullPointerException()
	{
		catchException(this.controller).save("userId", "firstName", "lastName", "emailAddress", "location", 0, true, null);
		
		assertTrue(caughtException() instanceof NullPointerException);
		assertEquals(caughtException().getMessage(), "savedByUser parameter cannot be null");
	}
	
	@Test
    public void save_EmptyStringSavedByUser_ThrowsInvalidArgumentException()
	{
		catchException(this.controller).save("userId", "firstName", "lastName", "emailAddress", "location", 0, true, "");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "savedByUser parameter cannot be an empty string");
	}
	
	@Test
    public void delete_NullParameter_ThrowsNullPointerException()
	{
		catchException(this.controller).delete(null);
		
		assertTrue(caughtException() instanceof NullPointerException);
		assertEquals(caughtException().getMessage(), "userId parameter cannot be null");
	}
	
	@Test
    public void delete_EmptyStringParameter_ThrowsInvalidArgumentException()
	{
		catchException(this.controller).delete("");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "userId parameter cannot be an empty string");
	}
	
	@Test
    public void updateValidity_NullUserId_ThrowsNullPointerException()
	{
		catchException(this.controller).updateValidity(null, true, "tester");
		
		assertTrue(caughtException() instanceof NullPointerException);
		assertEquals(caughtException().getMessage(), "userId parameter cannot be null");
	}
	
	@Test
    public void updateValidity_EmptyStringUserId_ThrowsInvalidArgumentException()
	{
		catchException(this.controller).updateValidity("", true, "tester");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "userId parameter cannot be an empty string");
	}
	
	@Test
    public void updateValidity_NullUpdatedByUser_ThrowsNullPointerException()
	{
		catchException(this.controller).updateValidity("userId", true, null);
		
		assertTrue(caughtException() instanceof NullPointerException);
		assertEquals(caughtException().getMessage(), "updatedByUser parameter cannot be null");
	}
	
	@Test
    public void updateValidity_EmptyStringUpdatedByUser_ThrowsInvalidArgumentException()
	{
		catchException(this.controller).updateValidity("userId", true, "");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "updatedByUser parameter cannot be an empty string");
	}
}
