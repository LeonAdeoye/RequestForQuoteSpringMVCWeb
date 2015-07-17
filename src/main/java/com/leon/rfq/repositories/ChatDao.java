package com.leon.rfq.repositories;

import java.time.LocalDateTime;
import java.util.List;

import com.leon.rfq.domains.ChatMessageImpl;


public interface ChatDao
{
	List<ChatMessageImpl> get(int requestId, LocalDateTime fromTimestamp);
	
	List<ChatMessageImpl> get(int requestId);
	
	boolean insert(ChatMessageImpl message);
	
	boolean delete(int requestId);

	boolean delete(int requestId, LocalDateTime fromTimeStamp);

	void initialize();
}
