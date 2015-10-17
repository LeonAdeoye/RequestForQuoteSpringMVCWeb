package com.leon.rfq.controllers;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.leon.rfq.domains.ChatMessageImpl;
import com.leon.rfq.services.ChatMediatorService;

@Controller
@RequestMapping("/chatroom")
public class ChatroomControllerImpl
{
	private static final Logger logger = LoggerFactory.getLogger(ChatroomControllerImpl.class);
	
	@Autowired(required=true)
	ChatMediatorService chatMediatorService;
	
	@RequestMapping()
	public String getAll(Model model)
	{
		return "chatroom";
	}
	
	@RequestMapping(value="/ajaxGetListOfChatrooms", method=RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Set<Integer> ajaxGetListOfAllChatrooms(@RequestParam String userId)
	{
		return this.chatMediatorService.getAllChatRooms(userId);
	}
		
	@RequestMapping(value = "/ajaxSendChatMessage", method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Object ajaxSendChatMessage(@RequestBody ChatMessageImpl chatMessage)
	{
		return this.chatMediatorService.sendMessage(chatMessage);
	}
}
