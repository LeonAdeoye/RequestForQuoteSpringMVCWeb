package com.leon.rfq.services.test;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.leon.rfq.repositories.ChatDao;
import com.leon.rfq.repositories.ChatDaoImpl;
import com.leon.rfq.services.ChatMediatorService;
import com.leon.rfq.services.ChatMediatorServiceImpl;
import com.leon.rfq.services.UserService;
import com.leon.rfq.services.UserServiceImpl;

@ContextConfiguration(locations = { "classpath: **/applicationContext.xml" })
public class ChatMediatorServiceImplTest extends AbstractJUnit4SpringContextTests
{
	@Test
    public void isParticipantRegistered_NullParticipant_ThrowsNullPointerException()
	{
		// Arrange
		ChatDao chatDaoMock = mock(ChatDaoImpl.class);
		UserService userServiceMock = mock(UserServiceImpl.class);
		ChatMediatorService chatMediatorService = new ChatMediatorServiceImpl(chatDaoMock, userServiceMock);
		// Act
		catchException(chatMediatorService).isParticipantRegistered(Integer.MAX_VALUE, null);
		// Assert
		assertTrue("Exception should be an instance of NullPointerException", caughtException() instanceof NullPointerException);
		assertEquals("Exception message should match", caughtException().getMessage(), "participant is an invalid argument");
	}
	
	@Test
    public void unregisterParticipant_NullParticipant_ThrowsNullPointerException()
	{
		// Arrange
		ChatDao chatDaoMock = mock(ChatDaoImpl.class);
		UserService userServiceMock = mock(UserServiceImpl.class);
		ChatMediatorService chatMediatorService = new ChatMediatorServiceImpl(chatDaoMock, userServiceMock);
		// Act
		catchException(chatMediatorService).unregisterParticipant(Integer.MAX_VALUE, null);
		// Assert
		assertTrue("Exception should be an instance of NullPointerException", caughtException() instanceof NullPointerException);
		assertEquals("Exception message should match", caughtException().getMessage(), "participant is an invalid argument");
	}

	@Test
    public void sendMessage_nullChatMessage_ThrowsNullPointerException()
	{
		// Arrange
		ChatDao chatDaoMock = mock(ChatDaoImpl.class);
		UserService userServiceMock = mock(UserServiceImpl.class);
		ChatMediatorService chatMediatorService = new ChatMediatorServiceImpl(chatDaoMock, userServiceMock);
		// Act
		catchException(chatMediatorService).sendMessage(null);
		// Assert
		assertTrue("Exception should be an instance of NullPointerException", caughtException() instanceof NullPointerException);
		assertEquals("Exception message should match", caughtException().getMessage(), "chatMesasage is an invalid argument");
	}
				
	@Test
    public void getChatMessages_ValidParameter_CallsDaoGetMethod()
	{
		// Arrange
		ChatDao chatDaoMock = mock(ChatDaoImpl.class);
		UserService userServiceMock = mock(UserServiceImpl.class);
		ChatMediatorService chatMediatorService = new ChatMediatorServiceImpl(chatDaoMock, userServiceMock);
		LocalDateTime timeStamp = LocalDateTime.now();
		// Act
		chatMediatorService.getChatMessages(Integer.MAX_VALUE, timeStamp);
		// Assert
		verify(chatDaoMock).get(Integer.MAX_VALUE, timeStamp);
	}
}
