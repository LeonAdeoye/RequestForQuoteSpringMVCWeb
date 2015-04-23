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

import com.leon.rfq.user.UserDao;
import com.leon.rfq.user.UserDaoImpl;
import com.leon.rfq.user.UserService;
import com.leon.rfq.user.UserServiceImpl;

@ContextConfiguration(locations = { "classpath: **/applicationContext.xml" })
public class UserControllerImplTest extends AbstractJUnit4SpringContextTests
{
	@Autowired
	private UserService userService;
	
	@Before
	public void setUp()
	{
		assertNotNull("autowired userDaoImpl should not be null", this.userService);
	}
	
	@Test
    public void getAll_ValidScenario_ReturnsValidListOfAllUsers()
	{
		assertNotNull("getAll method should return a non-null list of users", this.userService.getAll());
	}
	
	@Test
    public void get_NullParameter_ThrowsNullPointerException()
	{
		catchException(this.userService).get(null);
		
		assertTrue(caughtException() instanceof NullPointerException);
		assertEquals(caughtException().getMessage(), "userId parameter cannot be null");
	}
	
	@Test
    public void get_EmptyStringParameter_ThrowsInvalidArgumentException()
	{
		catchException(this.userService).get("");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "userId parameter cannot be an empty string");
	}
	
	@Test
    public void get_ValidParameter_CallsDaoGetMethod()
	{
		// Arrange
		UserService userService = new UserServiceImpl();
		UserDao userDaoMock = mock(UserDaoImpl.class);
		userService.setUserDao(userDaoMock);
		// Act
		userService.get("testUser");
		// Assert
		verify(userDaoMock).get("testUser");
	}
	
	@Test
    public void getAll_NoParameters_CallsDaoGetAllMethod()
	{
		// Arrange
		UserService userService = new UserServiceImpl();
		UserDao userDaoMock = mock(UserDaoImpl.class);
		userService.setUserDao(userDaoMock);
		// Act
		userService.getAll();
		// Assert
		verify(userDaoMock).getAll();
	}
	
	@Test
    public void delete_validUserId_CallsDaoDeleteMethod()
	{
		// Arrange
		UserService userService = new UserServiceImpl();
		UserDao userDaoMock = mock(UserDaoImpl.class);
		userService.setUserDao(userDaoMock);
		// Act
		userService.delete("userToBeDeleted");
		// Assert
		verify(userDaoMock).delete("userToBeDeleted");
	}
	
	@Test
    public void updateValidity_validUserId_CallsDaoUpdateValidityMethod()
	{
		// Arrange
		UserService userService = new UserServiceImpl();
		UserDao userDaoMock = mock(UserDaoImpl.class);
		userService.setUserDao(userDaoMock);
		// Act
		userService.updateValidity("userToBeUpdated", true, "tester");
		// Assert
		verify(userDaoMock).updateValidity("userToBeUpdated", true, "tester");
	}
	
	@Test
    public void save_validParameters_CallsDaoSaveMethod()
	{
		// Arrange
		UserService userService = new UserServiceImpl();
		UserDao userDaoMock = mock(UserDaoImpl.class);
		userService.setUserDao(userDaoMock);
		// Act
		userService.save("userToBeSaved", "firstName", "lastName", "emailAddress", "location", "group", true, "tester");
		// Assert
		verify(userDaoMock).save("userToBeSaved", "firstName", "lastName", "emailAddress", "location", "group", true, "tester");
	}
	
	@Test
    public void save_NullUserId_ThrowsNullPointerException()
	{
		catchException(this.userService).save(null, "firstName", "lastName", "emailAddress", "location", "group", true, "tester");
		
		assertTrue(caughtException() instanceof NullPointerException);
		assertEquals(caughtException().getMessage(), "userId parameter cannot be null");
	}
	
	@Test
    public void save_EmptyStringUserId_ThrowsInvalidArgumentException()
	{
		catchException(this.userService).save("", "firstName", "lastName", "emailAddress", "location", "group", true, "tester");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "userId parameter cannot be an empty string");
	}
	
	@Test
    public void save_NullFirstName_ThrowsNullPointerException()
	{
		catchException(this.userService).save("userId", null, "lastName", "emailAddress", "location", "group", true, "tester");
		
		assertTrue(caughtException() instanceof NullPointerException);
		assertEquals(caughtException().getMessage(), "firstName parameter cannot be null");
	}
	
	@Test
    public void save_EmptyStringFirstName_ThrowsInvalidArgumentException()
	{
		catchException(this.userService).save("userId", "", "lastName", "emailAddress", "location", "group", true, "tester");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "firstName parameter cannot be an empty string");
	}
	
	@Test
    public void save_NullLastName_ThrowsNullPointerException()
	{
		catchException(this.userService).save("userId", "firstName", null, "emailAddress", "location", "group", true, "tester");
		
		assertTrue(caughtException() instanceof NullPointerException);
		assertEquals(caughtException().getMessage(), "lastName parameter cannot be null");
	}
	
	@Test
    public void save_EmptyStringLastName_ThrowsInvalidArgumentException()
	{
		catchException(this.userService).save("userId", "firstName", "", "emailAddress", "location", "group", true, "tester");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "lastName parameter cannot be an empty string");
	}
	
	@Test
    public void save_NullEmailAddress_ThrowsNullPointerException()
	{
		catchException(this.userService).save("userId", "firstName", "lastName", null, "location", "group", true, "tester");
		
		assertTrue(caughtException() instanceof NullPointerException);
		assertEquals(caughtException().getMessage(), "emailAddress parameter cannot be null");
	}
	
	@Test
    public void save_EmptyStringEmailAddress_ThrowsInvalidArgumentException()
	{
		catchException(this.userService).save("userId", "firstName", "lastName", "", "location", "group", true, "tester");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "emailAddress parameter cannot be an empty string");
	}
	
	@Test
    public void save_NullLocation_ThrowsNullPointerException()
	{
		catchException(this.userService).save("userId", "firstName", "lastName", "emailAddress", null, "group", true, "tester");
		
		assertTrue(caughtException() instanceof NullPointerException);
		assertEquals(caughtException().getMessage(), "locationName parameter cannot be null");
	}
	
	@Test
    public void save_EmptyStringLocation_ThrowsInvalidArgumentException()
	{
		catchException(this.userService).save("userId", "firstName", "lastName", "emailAddress", "", "group", true, "tester");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "locationName parameter cannot be an empty string");
	}
	
	@Test
    public void save_NullSavedByUser_ThrowsNullPointerException()
	{
		catchException(this.userService).save("userId", "firstName", "lastName", "emailAddress", "location", "group", true, null);
		
		assertTrue(caughtException() instanceof NullPointerException);
		assertEquals(caughtException().getMessage(), "savedByUser parameter cannot be null");
	}
	
	@Test
    public void save_EmptyStringSavedByUser_ThrowsInvalidArgumentException()
	{
		catchException(this.userService).save("userId", "firstName", "lastName", "emailAddress", "location", "group", true, "");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "savedByUser parameter cannot be an empty string");
	}
	
	@Test
    public void delete_NullParameter_ThrowsNullPointerException()
	{
		catchException(this.userService).delete(null);
		
		assertTrue(caughtException() instanceof NullPointerException);
		assertEquals(caughtException().getMessage(), "userId parameter cannot be null");
	}
	
	@Test
    public void delete_EmptyStringParameter_ThrowsInvalidArgumentException()
	{
		catchException(this.userService).delete("");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "userId parameter cannot be an empty string");
	}
	
	@Test
    public void updateValidity_NullUserId_ThrowsNullPointerException()
	{
		catchException(this.userService).updateValidity(null, true, "tester");
		
		assertTrue(caughtException() instanceof NullPointerException);
		assertEquals(caughtException().getMessage(), "userId parameter cannot be null");
	}
	
	@Test
    public void updateValidity_EmptyStringUserId_ThrowsInvalidArgumentException()
	{
		catchException(this.userService).updateValidity("", true, "tester");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "userId parameter cannot be an empty string");
	}
	
	@Test
    public void updateValidity_NullUpdatedByUser_ThrowsNullPointerException()
	{
		catchException(this.userService).updateValidity("userId", true, null);
		
		assertTrue(caughtException() instanceof NullPointerException);
		assertEquals(caughtException().getMessage(), "updatedByUser parameter cannot be null");
	}
	
	@Test
    public void updateValidity_EmptyStringUpdatedByUser_ThrowsInvalidArgumentException()
	{
		catchException(this.userService).updateValidity("userId", true, "");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "updatedByUser parameter cannot be an empty string");
	}
	
	@Test
    public void userExistsWithUserId_EmptyUserId_ThrowsInvalidArgumentException()
	{
		catchException(this.userService).userExistsWithUserId("");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "userId parameter cannot be an empty string");
	}
	
	@Test
    public void userExistsWithUserId_NullUserId_ThrowsNullPointerException()
	{
		catchException(this.userService).userExistsWithUserId(null);
		
		assertTrue(caughtException() instanceof NullPointerException);
		assertEquals(caughtException().getMessage(), "userId parameter cannot be null");
	}
	
	@Test
    public void userExistsWithEmailAddress_EmptyEmailAddress_ThrowsInvalidArgumentException()
	{
		catchException(this.userService).userExistsWithEmailAddress("");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "emailAddress parameter cannot be an empty string");
	}
	
	@Test
    public void userExistsWithEmailAddress_NullEmailAddress_ThrowsNullPointerException()
	{
		catchException(this.userService).userExistsWithEmailAddress(null);
		
		assertTrue(caughtException() instanceof NullPointerException);
		assertEquals(caughtException().getMessage(), "emailAddress parameter cannot be null");
	}
	
	@Test
    public void userExistsWithEmailAddress_existingEmailAddress_ReturnsTrue()
	{
		// Arrange
		UserService userService = new UserServiceImpl();
		UserDao userDaoMock = mock(UserDaoImpl.class);
		userService.setUserDao(userDaoMock);
		// Act
		userService.userExistsWithEmailAddress("emailAddress");
		// Assert
		verify(userDaoMock).userExistsWithEmailAddress("emailAddress");
	}
	
	@Test
    public void userExistsWithUserId_existingUserId_ReturnsTrue()
	{
		// Arrange
		UserService userService = new UserServiceImpl();
		UserDao userDaoMock = mock(UserDaoImpl.class);
		userService.setUserDao(userDaoMock);
		// Act
		userService.userExistsWithUserId("userId");
		// Assert
		verify(userDaoMock).userExistsWithUserId("userId");
	}
}
