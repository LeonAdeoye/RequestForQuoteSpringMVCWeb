package com.leon.rfq.controllers;

import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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

import com.leon.rfq.domains.SearchCriterionImpl;
import com.leon.rfq.services.SearchService;
import com.leon.rfq.validators.SearchValidatorImpl;

@Controller
@RequestMapping("/searches")
public class SearchControllerImpl
{
	@Autowired(required=true)
	SearchService searchService;
	
	@Autowired(required=true)
	private SearchValidatorImpl searchValidator;
	
	@InitBinder
	public void initialiseBinder(WebDataBinder binder)
	{
		binder.setAllowedFields("searchCode", "description", "isValid");
		binder.setValidator(this.searchValidator);
	}
	
	@RequestMapping(value="/ajaxForOwner", method=RequestMethod.GET,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Map<String,Set<SearchCriterionImpl>> get(@RequestParam String owner)
	{
		return this.searchService.get(owner);
	}
	
	@RequestMapping(value="/ajaxForKey", method=RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Set<SearchCriterionImpl> get(@RequestParam String owner, @RequestParam String searchKey)
	{
		return this.searchService.get(owner, searchKey);
	}
		
	@RequestMapping("/forAll")
	public String get(Model model)
	{
		model.addAttribute("searches", this.searchService.get());
		return "searches";
	}
	
	@RequestMapping("/forKey")
	public String get(@RequestParam String owner, @RequestParam String searchKey, Model model)
	{
		model.addAttribute("search", this.searchService.get(owner, searchKey));
		return "search";
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String getNewSearchForm(Model model)
	{
		SearchCriterionImpl search = new SearchCriterionImpl();
		model.addAttribute("newSearch", search);
		
		return "addSearch";
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String processNewSearchForm(@ModelAttribute("newSearch") @Valid SearchCriterionImpl newSearch,
			BindingResult result, HttpServletRequest request)
	{
		String[] suppressedFields = result.getSuppressedFields();
		if(suppressedFields.length > 0)
		{
			throw new RuntimeException("Attempting to bind disallowed fields: " +
					StringUtils.arrayToCommaDelimitedString(suppressedFields));
		}
		
		if(result.hasErrors())
			return "addSearch";
		
		this.searchService.insert(newSearch.getOwner(), newSearch.getSearchKey(), newSearch.getName(),
				newSearch.getValue(), newSearch.getIsPrivate());
		
		return "redirect:/searches";
	}
	
	@RequestMapping(value="/ajaxInsert", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody boolean insert(@RequestParam String owner, @RequestParam String searchKey,
			@RequestParam String name, @RequestParam String value, @RequestParam boolean isPrivate)
	{
		return this.searchService.insert(owner, searchKey, name, value, isPrivate);
	}
	
	@RequestMapping("/deleteForOwner")
	public String delete(@RequestParam String owner, Model model)
	{
		this.searchService.delete(owner);
		
		return "redirect:/searches";
	}
	
	@RequestMapping("/deleteForKey")
	public String delete(@RequestParam String owner, @RequestParam String searchKey, Model model)
	{
		this.searchService.delete(owner, searchKey);
		
		return "redirect:/searches";
	}
	
	@RequestMapping(value="/ajaxDeleteForOwner", method=RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody boolean delete(@RequestParam String owner)
	{
		return this.searchService.delete(owner);
	}
	
	@RequestMapping(value="/ajaxDeleteForKey", method=RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody boolean delete(@RequestParam String owner, @RequestParam String searchKey)
	{
		return this.searchService.delete(owner, searchKey);
	}
}
