package com.leon.rfq.repositories.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.leon.rfq.domains.ChatMessageImpl;
import com.leon.rfq.repositories.ChatDao;


@ContextConfiguration(locations = { "classpath: **/applicationContext.xml" })
public class ChatDaoImplTest extends AbstractJUnit4SpringContextTests
{
	@Autowired(required=true)
	private ChatDao chatDao;
		
	@Test
    public void insert_validMessage_ShouldBeInsertedAndFound()
	{
		// Arrange
		Set<String> recipients = new HashSet<>();
		recipients.add("testRecipient");
		ChatMessageImpl chatMessageToBeSaved = new ChatMessageImpl("testSender", recipients,"test", Integer.MAX_VALUE);
		// Act
		boolean result = this.chatDao.insert(chatMessageToBeSaved);
		// Assert
		assertTrue("insert should return true", result);
		
		// Act
		List<ChatMessageImpl> messages = this.chatDao.get(Integer.MAX_VALUE);
		// Assert
		assertEquals("count should should be 1", 1, messages.size());
		assertTrue("saved message should be contained in list", messages.contains(chatMessageToBeSaved));
		
		// Act
		this.chatDao.delete(Integer.MAX_VALUE);
		messages = this.chatDao.get(Integer.MAX_VALUE);
		// Assert
		assertEquals("after delete the count should should be 0", 0, messages.size());
	}
	
	@Test
    public void get_NonExistentRequestId_ShouldReturnEmptyList()
	{
		// Act
		List<ChatMessageImpl> messages = this.chatDao.get(Integer.MIN_VALUE);
		// Assert
		assertEquals("count should should be 0", 0, messages.size());
	}
	
	@Test
    public void insert_ValidMessage_ShouldGetWithTimeStamp()
	{
		// Arrange
		Set<String> recipients = new HashSet<>();
		recipients.add("testRecipient");
		ChatMessageImpl chatMessageToBeSaved = new ChatMessageImpl("testSender", recipients, "test", Integer.MAX_VALUE);
		LocalDateTime timeStamp = chatMessageToBeSaved.getTimeStamp();
		// Act
		boolean result = this.chatDao.insert(chatMessageToBeSaved);
		// Assert
		assertTrue("insert should return true", result);
		
		// Act
		List<ChatMessageImpl> messages = this.chatDao.get(Integer.MAX_VALUE, timeStamp);
		// Assert
		assertEquals("count should should be 1", 1, messages.size());
		assertTrue("saved message should be contained in list", messages.contains(chatMessageToBeSaved));
		
		// Act
		this.chatDao.delete(Integer.MAX_VALUE, timeStamp);
		messages = this.chatDao.get(Integer.MAX_VALUE);
		// Assert
		assertEquals("after delete the count should should be 0", 0, messages.size());
	}
}
