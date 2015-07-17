package com.leon.rfq.repositories;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.WriteResultChecking;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.leon.rfq.domains.ChatMessageImpl;

@Repository
public class ChatDaoImpl implements ChatDao
{
	private static final Logger logger = LoggerFactory.getLogger(ChatDaoImpl.class);
	
	@Autowired(required=true)
	private MongoTemplate chatMongoDBTemplate;
	
	private final String collectionName;
	
	public ChatDaoImpl(String collectionName)
	{
		if((collectionName == null) || collectionName.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("collectionName is an invalid argument");
			
			throw new IllegalArgumentException("collectionName is an invalid argument");
		}
		
		this.collectionName = collectionName;
	}
	
	@Override
	public void initialize()
	{
		// Also by default the WriteConcern is ACKNOWLEDGED so no need to set it.
		this.chatMongoDBTemplate.setWriteResultChecking(WriteResultChecking.EXCEPTION);
	}

	@Override
	public List<ChatMessageImpl> get(int requestId, LocalDateTime fromTimeStamp)
	{
		Query query = new Query(where("requestId").is(requestId))
			.addCriteria(where("timeStamp").gte(fromTimeStamp));
		
		try
		{
			return this.chatMongoDBTemplate.find(query, ChatMessageImpl.class, this.collectionName);
		}
		catch(Exception e)
		{
			if(logger.isErrorEnabled())
				logger.error("Failed to get chat messages with request ID: " + requestId +
						" and from time stamp: " + fromTimeStamp + " due to exception: " + e);
			
			return new ArrayList<>();
		}
	}

	@Override
	public List<ChatMessageImpl> get(int requestId)
	{
		Query query = new Query(where("requestId").is(requestId));
		
		try
		{
			return this.chatMongoDBTemplate.find(query, ChatMessageImpl.class, this.collectionName);
		}
		catch(Exception e)
		{
			if(logger.isErrorEnabled())
				logger.error("Failed to get chat messages with request ID: " + requestId + " due to exception: " + e);
			
			return new ArrayList<>();
		}
	}

	@Override
	public boolean insert(ChatMessageImpl message)
	{
		try
		{
			this.chatMongoDBTemplate.insert(message, this.collectionName);
			
			return true;
		}
		catch(Exception e)
		{
			if(logger.isErrorEnabled())
				logger.error("Failed to insert chat message: " + message + " due to exception: " + e);
			
			return false;
		}
	}
	
	@Override
	public boolean delete(int requestId)
	{
		Query query = new Query(where("requestId").is(requestId));
		
		try
		{
			this.chatMongoDBTemplate.remove(query, ChatMessageImpl.class, this.collectionName);
			
			return true;
		}
		catch(Exception e)
		{
			if(logger.isErrorEnabled())
				logger.error("Failed to delete chat messages with request ID: " + requestId + " due to exception: " + e);
			
			return false;
		}
	}
	
	@Override
	public boolean delete(int requestId, LocalDateTime timeStamp)
	{
		Query query = new Query(where("requestId").is(requestId))
		.addCriteria(where("timeStamp").is(timeStamp));
		
		try
		{
			this.chatMongoDBTemplate.remove(query, ChatMessageImpl.class, this.collectionName);
			
			return true;
		}
		catch(Exception e)
		{
			if(logger.isErrorEnabled())
				logger.error("Failed to delete chat messages with request ID: " + requestId + " and timestamp: "
						+ timeStamp + " due to exception: " + e);
			
			return false;
		}
	}
}
