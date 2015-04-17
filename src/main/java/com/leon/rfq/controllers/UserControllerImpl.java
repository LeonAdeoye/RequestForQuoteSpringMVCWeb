package com.leon.rfq.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.leon.rfq.user.UserImpl;
import com.leon.rfq.user.UserService;

@Controller
@RequestMapping("/users")
public class UserControllerImpl
{
	private static final Logger logger = LoggerFactory.getLogger(UserControllerImpl.class);
	
	@Autowired
	private UserService userService;
		
	@RequestMapping()
	public String getAll(Model model)
	{
		model.addAttribute("users", this.userService.getAll());
		return "users";
	}
	
	@RequestMapping("/user")
	public String get(@RequestParam String userId, Model model)
	{
		model.addAttribute("user", this.userService.get(userId));
		return "user";
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String getNewUserForm(Model model)
	{
		UserImpl user = new UserImpl();
		model.addAttribute("newUser", user);
		
		return "addUser";
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String processNewUserForm(@ModelAttribute("newUser") UserImpl newUser)
	{
		this.userService.save(newUser.getUserId(), newUser.getFirstName(), newUser.getLastName(), newUser.getEmailAddress(),
				newUser.getLocationName(), newUser.getGroupName(), newUser.getIsValid(), "ladeoye"); //TODO
		
		return "redirect:/users";
	}

	@RequestMapping("/delete")
	public String delete(@RequestParam String userId, Model model)
	{
		this.userService.delete(userId);
		return "redirect:/users";
	}

	@RequestMapping("/updateValidity")
	public String updateValidity(@RequestParam String userId, @RequestParam boolean isValid, @RequestParam String updatedByUser, Model model)
	{
		this.userService.updateValidity(userId, isValid, updatedByUser);
		return "redirect:/users";
	}
}
