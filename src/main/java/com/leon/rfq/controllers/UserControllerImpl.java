package com.leon.rfq.controllers;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.leon.rfq.user.UserImpl;
import com.leon.rfq.user.UserService;
import com.leon.rfq.validators.UserValidator;

@Controller
@RequestMapping("/users")
public class UserControllerImpl
{
	private static final Logger logger = LoggerFactory.getLogger(UserControllerImpl.class);
	
	@Autowired
	UserService userService;
	
	@Autowired
	private UserValidator userValidator;
	
	@InitBinder
	public void initialiseBinder(WebDataBinder binder)
	{
		binder.setAllowedFields("userId", "lastName", "firstName", "emailAddress", "locationName", "groupName", "isValid", "language");
		binder.setValidator(this.userValidator);
	}
		
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
	public String processNewUserForm(@Valid @ModelAttribute("newUser") UserImpl newUser, BindingResult result)
	{
		String[] suppressedFields = result.getSuppressedFields();
		if(suppressedFields.length > 0)
		{
			throw new RuntimeException("Attempting to bind dsiallowed fields: " +
					StringUtils.arrayToCommaDelimitedString(suppressedFields));
		}
		
		if(result.hasErrors())
			return "addUser";
		
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
