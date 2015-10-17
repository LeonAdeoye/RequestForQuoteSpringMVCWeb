package com.leon.rfq.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.leon.rfq.domains.ChatMessageImpl;
import com.leon.rfq.domains.ChatroomDetailImpl;
import com.leon.rfq.domains.UserDetailImpl;
import com.leon.rfq.repositories.ChatDao;


public interface ChatMediatorService
{
	void setDao(ChatDao chatDao);
	
	boolean unregisterParticipant(int requestForQuote, UserDetailImpl participant);

	void registerParticipant(int requestForQuoteId, UserDetailImpl participant);

	boolean isParticipantRegistered(int requestForQuoteId, UserDetailImpl participant);

	List<ChatMessageImpl> getChatMessages(int requestId, LocalDateTime fromTimeStamp);

	Set<ChatroomDetailImpl> getAllChatRooms(String userId);

	ChatMessageImpl sendMessage(ChatMessageImpl chatMessage);
}