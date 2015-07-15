package com.leon.rfq.repositories;

import java.util.Set;

import com.leon.rfq.domains.ChatMessageImpl;


public interface ChatDao
{
	Set<ChatMessageImpl> get(int requestForQuoteId, int fromThisSequenceId);
	
	Set<ChatMessageImpl> get(int requestForQuoteId);

	boolean save(ChatMessageImpl message);
}
