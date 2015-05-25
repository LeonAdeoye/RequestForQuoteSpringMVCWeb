package com.leon.rfq.controllers;

import javax.servlet.http.HttpServletRequest;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.leon.rfq.domains.RequestDetailImpl;
import com.leon.rfq.services.BookService;
import com.leon.rfq.services.ClientService;
import com.leon.rfq.services.RequestService;
import com.leon.rfq.validators.RequestValidator;

@Controller
public class RequestControllerImpl
{
	private static final Logger logger = LoggerFactory.getLogger(RequestControllerImpl.class);
	
	@Autowired
	RequestService requestService;
	
	@Autowired
	BookService bookService;
	
	@Autowired
	ClientService clientService;
	
	@Autowired
	private RequestValidator requestValidator;
	
	@InitBinder
	public void initialiseBinder(WebDataBinder binder)
	{
		binder.setAllowedFields("request", "bookCode", "clientId", "language");
		binder.setValidator(this.requestValidator);
	}
		
	@RequestMapping(value = "/requests", method = RequestMethod.GET)
	public String getAll(Model model)
	{
		model.addAttribute("requests", this.requestService.getAllFromTodayOnly());
		model.addAttribute("newRequest", new RequestDetailImpl());
		model.addAttribute("books", this.bookService.getAllFromCacheOnly());
		model.addAttribute("clients", this.clientService.getAllFromCacheOnly());
		
		return "requests";
	}
	
	@RequestMapping(value = "/requests", method = RequestMethod.POST)
	public String getAll(@ModelAttribute("newRequest") @Valid RequestDetailImpl newRequest,
			BindingResult result, HttpServletRequest request)
	{
		String[] suppressedFields = result.getSuppressedFields();
		if(suppressedFields.length > 0)
		{
			throw new RuntimeException("Attempting to bind disallowed fields: " +
					StringUtils.arrayToCommaDelimitedString(suppressedFields));
		}
		
		if(result.hasErrors())
			return "requests";
		
		this.requestService.insert(newRequest.getRequest(), newRequest.getClientId(), newRequest.getBookCode(),  "ladeoye"); //TODO
		
		return "redirect:/requests";
	}
	
	@RequestMapping("/requests/request")
	public String get(@RequestParam int requestId, Model model)
	{
		model.addAttribute("request", this.requestService.get(requestId));
		return "request";
	}

	@RequestMapping(value = "/requests/add", method = RequestMethod.GET)
	public String getNewUserForm(Model model)
	{
		model.addAttribute("newRequest", new RequestDetailImpl());
		
		return "addRequest";
	}
	
	@RequestMapping(value = "/requests/add", method = RequestMethod.POST)
	public String processNewRequestForm(@ModelAttribute("newRequest") @Valid RequestDetailImpl newRequest,
			BindingResult result, HttpServletRequest request)
	{
		String[] suppressedFields = result.getSuppressedFields();
		if(suppressedFields.length > 0)
		{
			throw new RuntimeException("Attempting to bind disallowed fields: " +
					StringUtils.arrayToCommaDelimitedString(suppressedFields));
		}
		
		if(result.hasErrors())
			return "requestUser";
		
		this.requestService.insert(newRequest.getRequest(), newRequest.getClientId(), newRequest.getBookCode(),  "ladeoye"); //TODO
		
		return "redirect:/requests";
	}
	
	@RequestMapping("/requests/delete")
	public String delete(@RequestParam int requestId, Model model)
	{
		if(!this.requestService.delete(requestId))
			model.addAttribute("error", "Failed to delete request with requestId: " + requestId);
			
		return "redirect:/requests";
	}
	
	@RequestMapping(value = "/requests/matchingBookTags", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody Object getBookMatches(@RequestParam String pattern)
	{
		return this.bookService.getMatchingBookTags(pattern);
	}
	
	@RequestMapping(value = "/requests/matchingClientTags", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody Object getClientMatches(@RequestParam String pattern)
	{
		return this.clientService.getMatchingClientTags(pattern);
	}
}
