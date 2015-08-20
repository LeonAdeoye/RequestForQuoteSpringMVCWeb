package com.leon.rfq.controllers;

import java.util.Set;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.leon.rfq.common.EnumTypes.ClientTierEnum;
import com.leon.rfq.domains.ClientDetailImpl;
import com.leon.rfq.services.ClientService;
import com.leon.rfq.validators.ClientValidatorImpl;

@Controller
@RequestMapping("/clients")
public class ClientControllerImpl
{
	private static final Logger logger = LoggerFactory.getLogger(ClientControllerImpl.class);
	
	@Autowired(required=true)
	ClientService clientService;
	
	@Autowired(required=true)
	private ClientValidatorImpl clientValidator;
	
	@InitBinder
	public void initialiseBinder(WebDataBinder binder)
	{
		binder.setAllowedFields("ric", "description", "isValid");
		binder.setValidator(this.clientValidator);
	}
		
	@RequestMapping()
	public String getAll(Model model)
	{
		model.addAttribute("clients", this.clientService.getAll());
		return "clients";
	}
		
	@RequestMapping(value="/ajaxGetListOfAllClients", method=RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Set<ClientDetailImpl> ajaxGetListOfAllClients()
	{
		return this.clientService.getAllFromCacheOnly();
	}
	
	@RequestMapping(value = "/ajaxUpdateValidity", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Object ajaxUpdateValidity(@RequestBody int clientId, @RequestBody String clientName,
			@RequestBody boolean isValid, @RequestBody String tier, @RequestBody String updatedByUser)
	{
		return this.clientService.update(clientId, clientName, ClientTierEnum.valueOf(tier),
				true, updatedByUser);
	}
	
	@RequestMapping(value = "/ajaxAddNewClient", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Object ajaxAddNewBook(@RequestBody String name,
			@RequestBody String tier, @RequestBody String updatedByUser)
	{
		return this.clientService.insert(name, ClientTierEnum.valueOf(tier), true, updatedByUser);
	}
	
	@RequestMapping(value = "/matchingClientTags", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody Object getClientMatches(@RequestParam String pattern)
	{
		return this.clientService.getMatchingClientTags(pattern.toUpperCase());
	}
}
