package com.leon.rfq.repositories;

import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.leon.rfq.domains.ChatMessageImpl;
import com.mongodb.async.client.MongoClient;
import com.mongodb.async.client.MongoCollection;
import com.mongodb.async.client.MongoDatabase;

public class ChatDaoImpl implements ChatDao
{
	private static final Logger logger = LoggerFactory.getLogger(ChatDaoImpl.class);
	private MongoClient client;
	private MongoDatabase database;
	private MongoCollection<Document> chatCollection;
	
	public ChatDaoImpl()
	{
		
	}
	
	@Override
	@PostConstruct
	public final void initialize()
	{
		this.client = com.mongodb.async.client.MongoClients.create("mongodb://localhost:27017");
		this.database = this.client.getDatabase("rfq_chat");
		this.chatCollection = this.database.getCollection("chats");
	}
	
	@PreDestroy
	@Override
	public final void terminate()
	{
		this.client.close();
	}

	@Override
	public Set<ChatMessageImpl> get(int requestForQuoteId,
			int fromThisSequenceId)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<ChatMessageImpl> get(int requestForQuoteId)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(ChatMessageImpl message)
	{
		Document doc = new Document("content", message.getContent())
        .append("sequenceId", message.getSequenceId())
        .append("requestId", message.getRequestId())
        .append("timestamp", message.getTimeStamp())
        .append("owner", message.getOwner().getUserId())
		.append("recipients", message.getRecipients());
		
		this.chatCollection.insertOne(doc, (Void result, final Throwable t) ->
		{
			if(logger.isDebugEnabled())
			{
				logger.debug("inserted" + message);
			}
		});
	}
}
