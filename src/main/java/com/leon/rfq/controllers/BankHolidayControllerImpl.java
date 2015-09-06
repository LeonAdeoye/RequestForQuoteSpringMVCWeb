package com.leon.rfq.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.leon.rfq.common.EnumTypes.LocationEnum;
import com.leon.rfq.common.Tag;
import com.leon.rfq.domains.BankHolidayDetailImpl;
import com.leon.rfq.services.BankHolidayService;

@Controller
@RequestMapping("/bankHolidays")
public class BankHolidayControllerImpl
{
	@Autowired(required=true)
	BankHolidayService bankHolidayService;
	
	private final List<LocationEnum> listOfLocations = new ArrayList<LocationEnum>(Arrays.asList(LocationEnum.values()));
		
	@RequestMapping()
	public String getAll(Model model)
	{
		return "bankHolidays";
	}
		
	@RequestMapping(value="/ajaxGetListOfAllBankHolidays", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Object ajaxGetListOfAllBankHolidays()
	{
		return this.bankHolidayService.getAllFromCacheOnly();
	}
	
	@RequestMapping(value = "/ajaxUpdateBankHoliday", method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Object ajaxUpdateBankHoliday(@RequestBody BankHolidayDetailImpl bankHolidayToUpdate)
	{
		return this.bankHolidayService.updateValidity(bankHolidayToUpdate.getLocation(), bankHolidayToUpdate.getBankHolidayDate(),
				bankHolidayToUpdate.getIsValid(), bankHolidayToUpdate.getLastUpdatedBy());
	}
	
	@RequestMapping(value = "/ajaxAddNewBankHoliday", method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Object ajaxAddNewBankHoliday(@RequestBody BankHolidayDetailImpl bankHolidayToAdd)
	{
		return this.bankHolidayService.insert(bankHolidayToAdd.getLocation(),
				bankHolidayToAdd.getBankHolidayDate(), bankHolidayToAdd.getLastUpdatedBy());
	}
	
	@RequestMapping(value = "/matchingLocationTags", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Object getLocationMatches(@RequestParam String pattern)
	{
		return this.listOfLocations.stream().filter(location -> location.getLocation().toUpperCase().contains(pattern.toUpperCase()))
				.map(location -> new Tag(String.valueOf(location), location.getLocation())).collect(Collectors.toList());
	}
	
	@RequestMapping(value = "ajaxGetLocations", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Object ajaxGetLocations()
	{
		return this.listOfLocations.stream().collect(Collectors.toMap(location -> location, location -> location.getLocation()));
	}
}
