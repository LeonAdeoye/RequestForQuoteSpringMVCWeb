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

import com.leon.rfq.domains.RequestDetailImpl;
import com.leon.rfq.services.RequestService;
import com.leon.rfq.validators.RequestValidator;

@Controller
@RequestMapping("/requests")
public class RequestControllerImpl
{
	private static final Logger logger = LoggerFactory.getLogger(RequestControllerImpl.class);
	
	@Autowired
	RequestService requestService;
	
	@Autowired
	private RequestValidator requestValidator;
	
	@InitBinder
	public void initialiseBinder(WebDataBinder binder)
	{
		binder.setAllowedFields("requestSnippet", "bookName", "clientId");
		binder.setValidator(this.requestValidator);
	}
		
	@RequestMapping()
	public String getAll(Model model)
	{
		model.addAttribute("requests", this.requestService.getAll());
		return "requests";
	}
	
	@RequestMapping("/request")
	public String get(@RequestParam String requestId, Model model)
	{
		model.addAttribute("request", this.requestService.get(requestId));
		return "request";
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String getNewUserForm(Model model)
	{
		RequestDetailImpl request = new RequestDetailImpl();
		model.addAttribute("newRequest", request);
		
		return "addRequest";
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
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
	
	@RequestMapping("/delete")
	public String delete(@RequestParam String requestId, Model model)
	{
		if(!this.requestService.delete(requestId))
			model.addAttribute("error", "Failed to delete request with requestId: " + requestId);
			
		return "redirect:/requests";
	}
}
