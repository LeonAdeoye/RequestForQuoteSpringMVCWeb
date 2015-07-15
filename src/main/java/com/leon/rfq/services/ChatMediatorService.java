package com.leon.rfq.services;

import java.util.Set;

import com.leon.rfq.domains.ChatMessageImpl;
import com.leon.rfq.domains.UserDetail;


public interface ChatMediatorService
{
	Set<ChatMessageImpl> getChatMessages(int requestForQuote, int fromThisSequenceId);

	boolean unregisterParticipant(int requestForQuote, UserDetail participant);

	boolean sendMessage(int requestForQuoteId, UserDetail sender, String content);

	void registerParticipant(int requestForQuoteId, UserDetail participant);

	boolean isParticipantRegistered(int requestForQuoteId, UserDetail participant);
}