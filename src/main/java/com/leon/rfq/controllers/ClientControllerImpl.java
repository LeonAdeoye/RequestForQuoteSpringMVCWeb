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
		return "clients";
	}
		
	@RequestMapping(value="/ajaxGetListOfAllClients", method=RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Set<ClientDetailImpl> ajaxGetListOfAllClients()
	{
		return this.clientService.getAllFromCacheOnly();
	}
	
	@RequestMapping(value = "/ajaxUpdateClient", method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Object ajaxUpdateClient(@RequestBody ClientDetailImpl clientToUpdate)
	{
		return this.clientService.update(clientToUpdate.getClientId(), clientToUpdate.getName(),
				clientToUpdate.getTier(), clientToUpdate.getIsValid(), clientToUpdate.getLastUpdatedBy());
	}
	
	@RequestMapping(value = "/ajaxAddNewClient", method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Object ajaxAddNewBook(@RequestBody ClientDetailImpl newClient)
	{
		return this.clientService.insert(newClient.getName(), newClient.getTier(), true, newClient.getLastUpdatedBy());
	}
	
	@RequestMapping(value = "/matchingClientTags", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody Object getClientMatches(@RequestParam String pattern)
	{
		return this.clientService.getMatchingClientTags(pattern.toUpperCase());
	}
}
