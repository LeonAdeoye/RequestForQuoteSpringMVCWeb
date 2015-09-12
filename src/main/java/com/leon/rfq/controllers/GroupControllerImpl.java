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

import com.leon.rfq.domains.GroupDetailImpl;
import com.leon.rfq.services.GroupService;
import com.leon.rfq.validators.GroupValidatorImpl;

@Controller
@RequestMapping("/groups")
public class GroupControllerImpl
{
	private static final Logger logger = LoggerFactory.getLogger(GroupControllerImpl.class);
	
	@Autowired(required=true)
	GroupService groupService;
	
	
	@Autowired(required=true)
	private GroupValidatorImpl groupValidator;
	
	@InitBinder
	public void initialiseBinder(WebDataBinder binder)
	{
		binder.setAllowedFields("name", "description", "isValid");
		binder.setValidator(this.groupValidator);
	}
	
	@RequestMapping()
	public String getAll(Model model)
	{
		return "groups";
	}
	
	@RequestMapping(value="/ajaxGetListOfAllGroups", method=RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Set<GroupDetailImpl> ajaxGetListOfAllGroups()
	{
		return this.groupService.getAllFromCacheOnly();
	}
	
	@RequestMapping(value = "/ajaxUpdateGroup", method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Object ajaxUpdateGroup(@RequestBody GroupDetailImpl groupToUpdate)
	{
		return this.groupService.update(groupToUpdate.getName(), groupToUpdate.getDescription(),
				groupToUpdate.getIsValid(), groupToUpdate.getLastUpdatedBy());
	}
	
	@RequestMapping(value = "/ajaxAddNewGroup", method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Object ajaxAddNewGroup(@RequestBody GroupDetailImpl groupToAdd)
	{
		return this.groupService.insert(groupToAdd.getName(), groupToAdd.getDescription(),
				groupToAdd.getIsValid(), groupToAdd.getLastUpdatedBy());
	}
	
	@RequestMapping(value = "/matchingGroupTags", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Object getGroupMatches(@RequestParam String pattern)
	{
		return this.groupService.getMatchingGroupTags(pattern.toUpperCase());
	}
}
