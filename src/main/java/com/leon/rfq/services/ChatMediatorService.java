package com.leon.rfq.services;

import java.util.Set;

import com.leon.rfq.domains.ChatMessageImpl;
import com.leon.rfq.domains.UserDetailImpl;


public interface ChatMediatorService
{
	Set<ChatMessageImpl> getChatMessages(int requestForQuote, int fromThisSequenceId);

	boolean unregisterParticipant(int requestForQuote, UserDetailImpl participant);

	boolean sendMessage(int requestForQuoteId, UserDetailImpl sender, String content);

	void registerParticipant(int requestForQuoteId, UserDetailImpl participant);

	boolean isParticipantRegistered(int requestForQuoteId, UserDetailImpl participant);
}