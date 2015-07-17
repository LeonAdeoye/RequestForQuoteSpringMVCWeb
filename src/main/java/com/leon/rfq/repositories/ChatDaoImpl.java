package com.leon.rfq.repositories;

import java.util.Set;

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
	
	private final String connectionString;
	private final String databaseName;
	private final String collectionName;
	
	public ChatDaoImpl(String databaseName, String collectionName, String connectionString)
	{
		if((databaseName == null) || databaseName.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("databaseName is an invalid argument");
			
			throw new IllegalArgumentException("databaseName is an invalid argument");
		}
		
		if((collectionName == null) || collectionName.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("collectionName is an invalid argument");
			
			throw new IllegalArgumentException("collectionName is an invalid argument");
		}
		
		if((connectionString == null) || connectionString.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("connectionString is an invalid argument");
			
			throw new IllegalArgumentException("connectionString is an invalid argument");
		}
		
		this.databaseName = databaseName;
		this.collectionName = collectionName;
		this.connectionString = connectionString;
	}
	
	@Override
	public final void initialize()
	{
		this.client = com.mongodb.async.client.MongoClients.create(this.connectionString);
		this.database = this.client.getDatabase(this.databaseName);
		this.chatCollection = this.database.getCollection(this.collectionName);
	}
	
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
