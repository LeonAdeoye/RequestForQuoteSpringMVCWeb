package com.leon.rfq.controllers;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.leon.rfq.domains.UserDetailImpl;
import com.leon.rfq.services.UserService;
import com.leon.rfq.validators.UserValidatorImpl;

@Controller
@RequestMapping("/users")
public class UserControllerImpl
{
	private static final Logger logger = LoggerFactory.getLogger(UserControllerImpl.class);
	
	@Autowired
	UserService userService;
	
	@Autowired
	private UserValidatorImpl userValidator;
	
	@InitBinder
	public void initialiseBinder(WebDataBinder binder)
	{
		binder.setAllowedFields("userId", "lastName", "firstName", "emailAddress", "locationName", "groupName", "isValid", "language");
		binder.setValidator(this.userValidator);
	}
		
	@RequestMapping()
	public String getAll(Model model)
	{
		return "users";
	}
	
	@RequestMapping(value = "/ajaxAddNewUser", method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Object ajaxAddNewUser(@RequestBody UserDetailImpl newUser)
	{
		return this.userService.insert(newUser.getUserId(), newUser.getFirstName(), newUser.getLastName(), newUser.getEmailAddress(),
				newUser.getLocationName(), newUser.getGroupName(), newUser.getIsValid(), newUser.getLastUpdatedBy()); //TODO
	}
	
	@RequestMapping(value = "/ajaxUpdateUser", method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Object ajaxUpdateClient(@RequestBody UserDetailImpl userToUpdate)
	{
		return this.userService.updateValidity(userToUpdate.getUserId(),
				userToUpdate.getIsValid(), userToUpdate.getLastUpdatedBy());
	}
	
	@RequestMapping(value = "/ajaxGetMessages", method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Object getChatMessages(@RequestBody String userId, @RequestBody int requestId, LocalDateTime fromTimeStamp)
	{
		return this.userService.getMessages(userId, requestId, fromTimeStamp);
	}
	
	@RequestMapping(value="/ajaxGetListOfAllUsers", method=RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Object ajaxGetListOfAllUsers()
	{
		return this.userService.getAll();
	}
}