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

import com.leon.rfq.domains.UnderlyingDetailImpl;
import com.leon.rfq.services.UnderlyingService;
import com.leon.rfq.validators.UnderlyingValidatorImpl;

@Controller
@RequestMapping("/underlyings")
public class UnderlyingControllerImpl
{
	private static final Logger logger = LoggerFactory.getLogger(UnderlyingControllerImpl.class);
	
	@Autowired(required=true)
	UnderlyingService underlyingService;
	
	@Autowired(required=true)
	private UnderlyingValidatorImpl underlyingValidator;
	
	@InitBinder
	public void initialiseBinder(WebDataBinder binder)
	{
		binder.setAllowedFields("ric", "description", "isValid");
		binder.setValidator(this.underlyingValidator);
	}
	
	@RequestMapping(value="/all", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Set<UnderlyingDetailImpl> getAllUnderlying()
	{
		return this.underlyingService.getAllFromCacheOnly();
	}
		
	@RequestMapping()
	public String getAll(Model model)
	{
		model.addAttribute("underlyings", this.underlyingService.getAll());
		return "underlyings";
	}
	
	@RequestMapping(value="/ajaxGetListOfAllUnderlyings", method=RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Set<UnderlyingDetailImpl> ajaxGetListOfAllUnderlyings()
	{
		return this.underlyingService.getAllFromCacheOnly();
	}
	
	@RequestMapping(value = "/ajaxUpdateUnderlying", method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Object ajaxUpdateUnderlying(@RequestBody UnderlyingDetailImpl underlyingToUpdate)
	{
		return this.underlyingService.update(underlyingToUpdate.getRic(), underlyingToUpdate.getDescription(),
				underlyingToUpdate.getReferencePrice(), underlyingToUpdate.getSimulationPriceVariance(),
				underlyingToUpdate.getSpread(), underlyingToUpdate.getIsValid(), underlyingToUpdate.getLastUpdatedBy());
	}
		
	@RequestMapping(value = "/ajaxAddNewUnderlying", method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Object ajaxAddNewBook(@RequestBody UnderlyingDetailImpl newUnderlying)
	{
		// TODO: add dividendYield
		return this.underlyingService.insert(newUnderlying.getRic(), newUnderlying.getDescription(),
				newUnderlying.getReferencePrice(), newUnderlying.getSimulationPriceVariance(),
				newUnderlying.getSpread(), newUnderlying.getIsValid() , newUnderlying.getLastUpdatedBy());
	}
	
	@RequestMapping(value = "/matchingUnderlyingTags", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody Object getUnderlyingMatches(@RequestParam String pattern)
	{
		return this.underlyingService.getMatchingUnderlyingTags(pattern.toUpperCase());
	}
}
