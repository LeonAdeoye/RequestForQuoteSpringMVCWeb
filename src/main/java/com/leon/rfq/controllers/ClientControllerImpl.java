package com.leon.rfq.controllers;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
	
	@RequestMapping(value="/all", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Set<ClientDetailImpl> getAllClient()
	{
		return this.clientService.getAllFromCacheOnly();
	}
		
	@RequestMapping()
	public String getAll(Model model)
	{
		model.addAttribute("clients", this.clientService.getAll());
		return "clients";
	}
	
	@RequestMapping("/client")
	public String get(@RequestParam String ric, Model model)
	{
		model.addAttribute("client", this.clientService.get(ric));
		return "client";
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String getNewClientForm(Model model)
	{
		ClientDetailImpl client = new ClientDetailImpl();
		model.addAttribute("newClient", client);
		
		return "addClient";
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String processNewClientForm(@ModelAttribute("newClient") @Valid ClientDetailImpl newClient,
			BindingResult result, HttpServletRequest request)
	{
		String[] suppressedFields = result.getSuppressedFields();
		if(suppressedFields.length > 0)
		{
			throw new RuntimeException("Attempting to bind disallowed fields: " +
					StringUtils.arrayToCommaDelimitedString(suppressedFields));
		}
		
		if(result.hasErrors())
			return "addClient";
		
		this.clientService.insert(newClient.getName(), newClient.getTier(), newClient.getIsValid(), "ladeoye"); //TODO
		
		return "redirect:/clients";
	}
	
	@RequestMapping("/delete")
	public String delete(@RequestParam String ric, Model model)
	{
		this.clientService.delete(ric);
		
		return "redirect:/clients";
	}

	@RequestMapping("/update")
	public String update(@RequestParam int clientId, @RequestParam String name, @RequestParam String tier, @RequestParam boolean isValid, @RequestParam String updatedByUser, Model model)
	{
		this.clientService.update(clientId, name, ClientTierEnum.valueOf(tier), isValid, updatedByUser);
		
		return "redirect:/clients";
	}
	
	@RequestMapping(value = "/matchingClientTags", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody Object getClientMatches(@RequestParam String pattern)
	{
		return this.clientService.getMatchingClientTags(pattern.toUpperCase());
	}
}
