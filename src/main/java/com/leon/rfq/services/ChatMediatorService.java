package com.leon.rfq.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.leon.rfq.domains.ChatMessageImpl;
import com.leon.rfq.domains.UserDetailImpl;


public interface ChatMediatorService
{
	boolean unregisterParticipant(int requestForQuote, UserDetailImpl participant);

	void registerParticipant(int requestForQuoteId, UserDetailImpl participant);

	boolean isParticipantRegistered(int requestForQuoteId, UserDetailImpl participant);

	List<ChatMessageImpl> getChatMessages(int requestId, LocalDateTime fromTimeStamp);

	Set<Integer> getAllChatRooms(String userId);

	ChatMessageImpl sendMessage(ChatMessageImpl chatMessage);
}