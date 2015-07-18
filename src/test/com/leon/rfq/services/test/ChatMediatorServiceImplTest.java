package com.leon.rfq.services.test;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.leon.rfq.domains.UserDetailImpl;
import com.leon.rfq.repositories.ChatDao;
import com.leon.rfq.repositories.ChatDaoImpl;
import com.leon.rfq.services.ChatMediatorService;

@ContextConfiguration(locations = { "classpath: **/applicationContext.xml" })
public class ChatMediatorServiceImplTest extends AbstractJUnit4SpringContextTests
{
	@Autowired(required=true)
	private ChatMediatorService chatMediatorService;
	
	@Before
	public void setUp()
	{
		assertNotNull("autowired chatDaoImpl should not be null", this.chatMediatorService);
	}
	
	@Test
    public void isParticipantRegistered_NullParticipant_ThrowsNullPointerException()
	{
		catchException(this.chatMediatorService).isParticipantRegistered(Integer.MAX_VALUE, null);
		
		assertTrue("Exception should be an instance of NullPointerException", caughtException() instanceof NullPointerException);
		assertEquals("Exception message should match", caughtException().getMessage(), "participant is an invalid argument");
	}
	
	@Test
    public void unregisterParticipant_NullParticipant_ThrowsNullPointerException()
	{
		catchException(this.chatMediatorService).unregisterParticipant(Integer.MAX_VALUE, null);
		
		assertTrue("Exception should be an instance of NullPointerException", caughtException() instanceof NullPointerException);
		assertEquals("Exception message should match", caughtException().getMessage(), "participant is an invalid argument");
	}
	
	@Test
    public void sendMessage_NullSender_ThrowsNullPointerException()
	{
		catchException(this.chatMediatorService).sendMessage(Integer.MAX_VALUE, null, "test content");
		
		assertTrue("Exception should be an instance of NullPointerException", caughtException() instanceof NullPointerException);
		assertEquals("Exception message should match", caughtException().getMessage(), "sender is an invalid argument");
	}
	
	@Test
    public void sendMessage_NullContent_ThrowsIllegalArgumentException()
	{
		catchException(this.chatMediatorService).sendMessage(Integer.MAX_VALUE, new UserDetailImpl(), null);

		assertTrue("Exception should be an instance of NullPointerException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "content is an invalid argument");
	}
	
	@Test
    public void sendMessage_EmptyStringContent_ThrowsIllegalArgumentException()
	{
		catchException(this.chatMediatorService).sendMessage(Integer.MAX_VALUE, new UserDetailImpl(), "");
		
		assertTrue("Exception should be an instance of NullPointerException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "content is an invalid argument");
	}
		
	@Test
    public void getChatMessages_ValidParameter_CallsDaoGetMethod()
	{
		// Arrange
		ChatDao chatDaoMock = mock(ChatDaoImpl.class);
		this.chatMediatorService.setDao(chatDaoMock);
		LocalDateTime timeStamp = LocalDateTime.now();
		// Act
		this.chatMediatorService.getChatMessages(Integer.MAX_VALUE, timeStamp);
		// Assert
		verify(chatDaoMock).get(Integer.MAX_VALUE, timeStamp);
	}
}
