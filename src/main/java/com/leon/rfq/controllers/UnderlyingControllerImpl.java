package com.leon.rfq.controllers;

import java.math.BigDecimal;
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
	
	@RequestMapping("/underlying")
	public String get(@RequestParam String ric, Model model)
	{
		model.addAttribute("underlying", this.underlyingService.get(ric));
		return "underlying";
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String getNewUnderlyingForm(Model model)
	{
		UnderlyingDetailImpl underlying = new UnderlyingDetailImpl();
		model.addAttribute("newUnderlying", underlying);
		
		return "addUnderlying";
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String processNewUnderlyingForm(@ModelAttribute("newUnderlying") @Valid UnderlyingDetailImpl newUnderlying,
			BindingResult result, HttpServletRequest request)
	{
		String[] suppressedFields = result.getSuppressedFields();
		if(suppressedFields.length > 0)
		{
			throw new RuntimeException("Attempting to bind disallowed fields: " +
					StringUtils.arrayToCommaDelimitedString(suppressedFields));
		}
		
		if(result.hasErrors())
			return "addUnderlying";
		
		this.underlyingService.insert(newUnderlying.getRic(), newUnderlying.getDescription(), newUnderlying.getReferencePrice(),
				newUnderlying.getSimulationPriceVariance(), newUnderlying.getSpread(), newUnderlying.getIsValid(), "ladeoye"); //TODO
		
		return "redirect:/underlyings";
	}
	
	@RequestMapping("/delete")
	public String delete(@RequestParam String ric, Model model)
	{
		this.underlyingService.delete(ric);
		
		return "redirect:/underlyings";
	}

	@RequestMapping("/update")
	public String update(@RequestParam String ric, @RequestParam String description, @RequestParam double referencePrice, @RequestParam double simulationPriceVariance, @RequestParam double spread, @RequestParam boolean isValid, @RequestParam String updatedByUser, Model model)
	{
		this.underlyingService.update(ric, description, BigDecimal.valueOf(referencePrice),
				BigDecimal.valueOf(simulationPriceVariance), BigDecimal.valueOf(spread), isValid, updatedByUser);
		
		return "redirect:/underlyings";
	}

	@RequestMapping(value = "/matchingUnderlyingTags", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody Object getUnderlyingMatches(@RequestParam String pattern)
	{
		return this.underlyingService.getMatchingUnderlyingTags(pattern.toUpperCase());
	}
}
