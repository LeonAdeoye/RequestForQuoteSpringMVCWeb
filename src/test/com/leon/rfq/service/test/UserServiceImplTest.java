package com.leon.rfq.service.test;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
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
public class UserServiceImplTest extends AbstractJUnit4SpringContextTests
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
    public void get_NullUserId_ThrowsInvalidArgumentException()
	{
		catchException(this.userService).get(null);
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "userId argument is invalid");
	}
	
	@Test
    public void get_EmptyStringUserId_ThrowsInvalidArgumentException()
	{
		catchException(this.userService).get("");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "userId argument is invalid");
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
		verify(userDaoMock, never()).delete("userToBeDeleted");
	}
	
	@Test
    public void updateValidity_NonExistentUserId_DoesNotCallDaoUpdateValidityMethod()
	{
		// Arrange
		UserService userService = new UserServiceImpl();
		UserDao userDaoMock = mock(UserDaoImpl.class);
		userService.setUserDao(userDaoMock);
		// Act
		userService.updateValidity("userToBeUpdated", true, "tester");
		// Assert
		verify(userDaoMock, never()).updateValidity("userToBeUpdated", true, "tester");
	}
	
	@Test
    public void updateValidity_NonExistentUserId_ReturnFalse()
	{
		// Arrange
		UserService userService = new UserServiceImpl();
		UserDao userDaoMock = mock(UserDaoImpl.class);
		userService.setUserDao(userDaoMock);
		// Act and Assert
		assertFalse("updateValidity should return false if user does not exist.", userService.updateValidity("userToBeUpdated", true, "tester"));
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
		verify(userDaoMock, never()).updateValidity("userToBeUpdated", true, "tester");
	}
	
	@Test
    public void insert_validParameters_CallsDaoSaveMethod()
	{
		// Arrange
		UserService userService = new UserServiceImpl();
		UserDao userDaoMock = mock(UserDaoImpl.class);
		userService.setUserDao(userDaoMock);
		// Act
		userService.insert("userToBeSaved", "firstName", "lastName", "emailAddress", "location", "group", true, "tester");
		// Assert
		verify(userDaoMock).insert("userToBeSaved", "firstName", "lastName", "emailAddress", "location", "group", true, "tester");
	}
	
	@Test
    public void insert_NullUserId_ThrowsIllegalArgumentException()
	{
		catchException(this.userService).insert(null, "firstName", "lastName", "emailAddress", "location", "group", true, "tester");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "userId argument is invalid");
	}
	
	@Test
    public void insert_EmptyStringUserId_ThrowsIllegalArgumentException()
	{
		catchException(this.userService).insert("", "firstName", "lastName", "emailAddress", "location", "group", true, "tester");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "userId argument is invalid");
	}
	
	@Test
    public void insert_NullFirstName_ThrowsIllegalArgumentException()
	{
		catchException(this.userService).insert("userId", null, "lastName", "emailAddress", "location", "group", true, "tester");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "firstName argument is invalid");
	}
	
	@Test
    public void insert_EmptyStringFirstName_ThrowsIllegalArgumentException()
	{
		catchException(this.userService).insert("userId", "", "lastName", "emailAddress", "location", "group", true, "tester");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "firstName argument is invalid");
	}
	
	@Test
    public void insert_NullLastName_ThrowsIllegalArgumentException()
	{
		catchException(this.userService).insert("userId", "firstName", null, "emailAddress", "location", "group", true, "tester");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "lastName argument is invalid");
	}
	
	@Test
    public void insert_EmptyStringLastName_ThrowsInvalidArgumentException()
	{
		catchException(this.userService).insert("userId", "firstName", "", "emailAddress", "location", "group", true, "tester");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "lastName argument is invalid");
	}
	
	@Test
    public void insert_NullEmailAddress_ThrowsIllegalArgumentException()
	{
		catchException(this.userService).insert("userId", "firstName", "lastName", null, "location", "group", true, "tester");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "emailAddress argument is invalid");
	}
	
	@Test
    public void insert_EmptyStringEmailAddress_ThrowsInvalidArgumentException()
	{
		catchException(this.userService).insert("userId", "firstName", "lastName", "", "location", "group", true, "tester");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "emailAddress argument is invalid");
	}
	
	@Test
    public void insert_NullLocation_ThrowsIllegalArgumentException()
	{
		catchException(this.userService).insert("userId", "firstName", "lastName", "emailAddress", null, "group", true, "tester");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "locationName argument is invalid");
	}
	
	@Test
    public void insert_EmptyStringLocation_ThrowsInvalidArgumentException()
	{
		catchException(this.userService).insert("userId", "firstName", "lastName", "emailAddress", "", "group", true, "tester");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "locationName argument is invalid");
	}
	
	@Test
    public void insert_NullSavedByUser_ThrowsIllegalArgumentException()
	{
		catchException(this.userService).insert("userId", "firstName", "lastName", "emailAddress", "location", "group", true, null);
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "savedByUser argument is invalid");
	}
	
	@Test
    public void insert_EmptyStringSavedByUser_ThrowsInvalidArgumentException()
	{
		catchException(this.userService).insert("userId", "firstName", "lastName", "emailAddress", "location", "group", true, "");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "savedByUser argument is invalid");
	}
	
	@Test
    public void delete_NullParameter_ThrowsIllegalArgumentException()
	{
		catchException(this.userService).delete(null);
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "userId argument is invalid");
	}
	
	@Test
    public void delete_EmptyStringParameter_ThrowsInvalidArgumentException()
	{
		catchException(this.userService).delete("");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "userId argument is invalid");
	}
	
	@Test
    public void updateValidity_NullUserId_ThrowsIllegalArgumentException()
	{
		catchException(this.userService).updateValidity(null, true, "tester");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "userId argument is invalid");
	}
	
	@Test
    public void updateValidity_EmptyStringUserId_ThrowsInvalidArgumentException()
	{
		catchException(this.userService).updateValidity("", true, "tester");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "userId argument is invalid");
	}
	
	@Test
    public void updateValidity_NullUpdatedByUser_ThrowsIllegalArgumentException()
	{
		catchException(this.userService).updateValidity("userId", true, null);
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "updatedByUser argument is invalid");
	}
	
	@Test
    public void updateValidity_EmptyStringUpdatedByUser_ThrowsInvalidArgumentException()
	{
		catchException(this.userService).updateValidity("userId", true, "");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "updatedByUser argument is invalid");
	}
	
	@Test
    public void userExistsWithUserId_EmptyUserId_ThrowsInvalidArgumentException()
	{
		catchException(this.userService).userExistsWithUserId("");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "userId argument is invalid");
	}
	
	@Test
    public void userExistsWithUserId_NullUserId_ThrowsIllegalArgumentException()
	{
		catchException(this.userService).userExistsWithUserId(null);
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "userId argument is invalid");
	}
	
	@Test
    public void userExistsWithEmailAddress_EmptyEmailAddress_ThrowsInvalidArgumentException()
	{
		catchException(this.userService).userExistsWithEmailAddress("");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "emailAddress argument is invalid");
	}
	
	@Test
    public void userExistsWithEmailAddress_NullEmailAddress_ThrowsIllegalArgumentException()
	{
		catchException(this.userService).userExistsWithEmailAddress(null);
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "emailAddress argument is invalid");
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
	
	@Test
    public void update_NullUserId_ThrowsIllegalArgumentException()
	{
		catchException(this.userService).update(null, "firstName", "lastName", "emailAddress", "location", "group", true, "tester");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "userId argument is invalid");
	}
	
	@Test
    public void update_EmptyStringUserId_ThrowsInvalidArgumentException()
	{
		catchException(this.userService).update("", "firstName", "lastName", "emailAddress", "location", "group", true, "tester");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "userId argument is invalid");
	}
	
	@Test
    public void update_NullFirstName_ThrowsIllegalArgumentException()
	{
		catchException(this.userService).update("userId", null, "lastName", "emailAddress", "location", "group", true, "tester");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "firstName argument is invalid");
	}
	
	@Test
    public void update_EmptyStringFirstName_ThrowsInvalidArgumentException()
	{
		catchException(this.userService).update("userId", "", "lastName", "emailAddress", "location", "group", true, "tester");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "firstName argument is invalid");
	}
	
	@Test
    public void update_NullLastName_ThrowsIllegalArgumentException()
	{
		catchException(this.userService).update("userId", "firstName", null, "emailAddress", "location", "group", true, "tester");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "lastName argument is invalid");
	}
	
	@Test
    public void update_EmptyStringLastName_ThrowsInvalidArgumentException()
	{
		catchException(this.userService).update("userId", "firstName", "", "emailAddress", "location", "group", true, "tester");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "lastName argument is invalid");
	}
	
	@Test
    public void update_NullEmailAddress_ThrowsIllegalArgumentException()
	{
		catchException(this.userService).update("userId", "firstName", "lastName", null, "location", "group", true, "tester");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "emailAddress argument is invalid");
	}
	
	@Test
    public void update_EmptyStringEmailAddress_ThrowsInvalidArgumentException()
	{
		catchException(this.userService).update("userId", "firstName", "lastName", "", "location", "group", true, "tester");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "emailAddress argument is invalid");
	}
	
	@Test
    public void update_NullLocation_ThrowsIllegalArgumentException()
	{
		catchException(this.userService).update("userId", "firstName", "lastName", "emailAddress", null, "group", true, "tester");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "locationName argument is invalid");
	}
	
	@Test
    public void update_EmptyStringLocation_ThrowsInvalidArgumentException()
	{
		catchException(this.userService).update("userId", "firstName", "lastName", "emailAddress", "", "group", true, "tester");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "locationName argument is invalid");
	}
	
	@Test
    public void update_NullUpdatedByUser_ThrowsIllegalArgumentException()
	{
		catchException(this.userService).update("userId", "firstName", "lastName", "emailAddress", "location", "group", true, null);
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "updatedByUser argument is invalid");
	}
	
	@Test
    public void update_EmptyStringUpdatedByUser_ThrowsInvalidArgumentException()
	{
		catchException(this.userService).update("userId", "firstName", "lastName", "emailAddress", "location", "group", true, "");
		
		assertTrue(caughtException() instanceof IllegalArgumentException);
		assertEquals(caughtException().getMessage(), "updatedByUser argument is invalid");
	}
	
}
