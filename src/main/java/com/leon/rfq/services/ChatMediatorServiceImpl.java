package com.leon.rfq.services;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.leon.rfq.domains.ChatMessageImpl;
import com.leon.rfq.domains.UserDetailImpl;
import com.leon.rfq.repositories.ChatDao;

public final class ChatMediatorServiceImpl implements ChatMediatorService
{
	private static final Logger logger = LoggerFactory.getLogger(ChatMediatorServiceImpl.class);
	private final Map<Integer, Set<UserDetailImpl>> chatRooms = new ConcurrentSkipListMap<>();
	
	@Autowired(required=true)
	private final ChatDao chatDao;

	public ChatMediatorServiceImpl(ChatDao chatDao)
	{
		if(chatDao == null)
		{
			if(logger.isErrorEnabled())
				logger.error("chatDao is an invalid argument");
			
			throw new NullPointerException("chatDao is an invalid argument");
		}

		this.chatDao = chatDao;
	}

	@Override
	public boolean sendMessage(int requestForQuoteId, UserDetailImpl sender, String content)
	{
		if((sender == null))
		{
			if(logger.isErrorEnabled())
				logger.error("sender is an invalid argument");
			
			throw new NullPointerException("sender is an invalid argument");
		}
		
		if((content == null) || content.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("content is an invalid argument");
			
			throw new IllegalArgumentException("content is an invalid argument");
		}
		
		ReentrantLock lock = new ReentrantLock();
		
		try
		{
			lock.lock();
			
			Set<UserDetailImpl> recipients = this.chatRooms.get(requestForQuoteId);
			ChatMessageImpl message = new ChatMessageImpl(sender, recipients, content, requestForQuoteId);
			
			recipients.stream().filter(recipient -> !recipient.equals(sender))
				.forEach(recipient -> recipient.receive(message));
			
			this.chatDao.save(message);
			
			return true;
		}
		finally
		{
			lock.unlock();
		}
	}

	@Override
	public void registerParticipant(int requestForQuoteId, UserDetailImpl participant)
	{
		if(participant == null)
		{
			if(logger.isErrorEnabled())
				logger.error("participant is an invalid argument");
			
			throw new NullPointerException("participant is an invalid argument");
		}

		if(this.chatRooms.containsKey(requestForQuoteId))
		{
			Set<UserDetailImpl> participants = this.chatRooms.get(requestForQuoteId);
			
			if(!participants.contains(participant))
				participants.add(participant);
		}
		else
		{
			Set<UserDetailImpl> participants = new HashSet<>();
			participants.add(participant);
			
			this.chatRooms.put(requestForQuoteId, participants);
		}
	}

	@Override
	public boolean isParticipantRegistered(int requestForQuoteId, UserDetailImpl participant)
	{
		if(participant == null)
		{
			if(logger.isErrorEnabled())
				logger.error("participant is an invalid argument");
			
			throw new NullPointerException("participant is an invalid argument");
		}
		
		ReentrantLock lock = new ReentrantLock();
		
		try
		{
			lock.lock();
			
			if(this.chatRooms.containsKey(requestForQuoteId))
				return this.chatRooms.get(requestForQuoteId).contains(participant);
			
			return false;
		}
		finally
		{
			lock.unlock();
		}
	}

	@Override
	public Set<ChatMessageImpl> getChatMessages(int requestForQuoteId, int fromThisSequenceId)
	{
		return this.chatDao.get(requestForQuoteId, fromThisSequenceId);
	}

	@Override
	public boolean unregisterParticipant(int requestForQuoteId, UserDetailImpl participant)
	{
		if(participant == null)
		{
			if(logger.isErrorEnabled())
				logger.error("participant is an invalid argument");
			
			throw new IllegalArgumentException("participant is an invalid argument");
		}
		
		ReentrantLock lock = new ReentrantLock();
		
		try
		{
			lock.lock();
			
			if(this.chatRooms.containsKey(requestForQuoteId))
				return this.chatRooms.get(requestForQuoteId).remove(participant);
					
			return false;
		}
		finally
		{
			lock.unlock();
		}
	}

	public int getChatRoomCount()
	{
		return this.chatRooms.size();
	}

	public int getChatRoomSize(int requestForQuoteId)
	{
		ReentrantLock lock = new ReentrantLock();
		
		try
		{
			lock.lock();
			
			if(this.chatRooms.containsKey(requestForQuoteId))
				return this.chatRooms.get(requestForQuoteId).size();
	
			return 0;
		}
		finally
		{
			lock.unlock();
		}
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		for(Map.Entry<Integer, Set<UserDetailImpl>> chatroom : this.chatRooms.entrySet())
		{
			sb.append("\n[ Chatroom for requestforQuoteId=");
			sb.append(chatroom.getKey());
			for(UserDetailImpl participant : chatroom.getValue())
			{
				sb.append("\nParticipant=");
				sb.append(participant);
			}
			sb.append(" ]");
		}
		return sb.toString();
	}
}
