package com.leon.rfq.services;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leon.rfq.domains.ChatMessageImpl;
import com.leon.rfq.domains.ChatroomDetailImpl;
import com.leon.rfq.domains.UserDetailImpl;
import com.leon.rfq.repositories.ChatDao;

@Service
public final class ChatMediatorServiceImpl implements ChatMediatorService
{
	private static final Logger logger = LoggerFactory.getLogger(ChatMediatorServiceImpl.class);
	private final Map<Integer, Set<UserDetailImpl>> chatRooms = new ConcurrentSkipListMap<>();
	
	@Autowired(required=true)
	private ChatDao chatDao;
	
	@Autowired(required=true)
	private UserService userService;
	
	public ChatMediatorServiceImpl() {}

	@Override
	public void setDao(ChatDao chatDao)
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
	public ChatMessageImpl sendMessage(ChatMessageImpl chatMessage)
	{
		if(chatMessage == null)
		{
			if(logger.isErrorEnabled())
				logger.error("chatMessage is an invalid argument");
			
			throw new NullPointerException("chatMessage is an invalid argument");
		}
		
		ReentrantLock lock = new ReentrantLock();
		
		try
		{
			lock.lock();
			String senderId = chatMessage.getSender();
			int requestId = chatMessage.getRequestId();
			UserDetailImpl sender = this.userService.get(senderId);
			
			if(sender == null)
			{
				if(logger.isErrorEnabled())
					logger.error("User {} does not exist", sender);
				
				throw new NullPointerException("User " + senderId + " does not exist");
			}
			
			if(!isParticipantRegistered(requestId, sender))
				registerParticipant(requestId, sender);
			
			Set<UserDetailImpl> recipients = this.chatRooms.get(requestId);
			Set<String> recipientIds = recipients.stream().map(UserDetailImpl::getUserId).collect(Collectors.toSet());
						
			chatMessage.setRecipients(recipientIds);
			chatMessage.setTimeStamp(LocalDateTime.now());
			
			recipients.stream().filter(recipient -> !recipient.equals(senderId))
				.forEach(recipient -> recipient.receive(chatMessage));
			
			if(this.chatDao.insert(chatMessage))
				return chatMessage;
		}
		finally
		{
			lock.unlock();
		}
		
		return null;
	}

	@Override
	public void registerParticipant(int requestId, UserDetailImpl participant)
	{
		if(participant == null)
		{
			if(logger.isErrorEnabled())
				logger.error("participant is an invalid argument");
			
			throw new NullPointerException("participant is an invalid argument");
		}

		if(this.chatRooms.containsKey(requestId))
		{
			Set<UserDetailImpl> participants = this.chatRooms.get(requestId);
			
			if(!participants.contains(participant))
				participants.add(participant);
		}
		else
		{
			Set<UserDetailImpl> participants = new HashSet<>();
			participants.add(participant);
			
			this.chatRooms.put(requestId, participants);
		}
	}

	@Override
	public boolean isParticipantRegistered(int requestId, UserDetailImpl participant)
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
			
			if(this.chatRooms.containsKey(requestId))
				return this.chatRooms.get(requestId).contains(participant);
			
			return false;
		}
		finally
		{
			lock.unlock();
		}
	}

	@Override
	public List<ChatMessageImpl> getChatMessages(int requestId, LocalDateTime fromTimeStamp)
	{
		return this.chatDao.get(requestId, fromTimeStamp);
	}

	@Override
	public boolean unregisterParticipant(int requestId, UserDetailImpl participant)
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
			
			if(this.chatRooms.containsKey(requestId))
				return this.chatRooms.get(requestId).remove(participant);
					
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

	public int getChatRoomSize(int requestId)
	{
		ReentrantLock lock = new ReentrantLock();
		
		try
		{
			lock.lock();
			
			if(this.chatRooms.containsKey(requestId))
				return this.chatRooms.get(requestId).size();
	
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

	@Override
	public Set<ChatroomDetailImpl> getAllChatRooms(String userId)
	{
		// TODO Auto-generated method stub
		return null;
	}
}
