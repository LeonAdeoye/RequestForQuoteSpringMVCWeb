package com.leon.rfq.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.leon.rfq.domains.BankHolidayDetailImpl;
import com.leon.rfq.services.BankHolidayService;


@Controller
@RequestMapping("/bankHolidays")
public class BankHolidayControllerImpl
{
	private static final Logger logger = LoggerFactory.getLogger(BankHolidayControllerImpl.class);
	
	@Autowired(required=true)
	BankHolidayService bankHolidayService;
		
	@RequestMapping()
	public String getAll(Model model)
	{
		model.addAttribute("bankHolidays", null /*this.bankHolidayService.getAll()*/);
		return "bankHolidays";
	}
		
	@RequestMapping(value="/ajaxGetListOfAllBankHolidays", method=RequestMethod.GET,
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
		return this.bankHolidayService.insert(bankHolidayToAdd.getLocation(), bankHolidayToAdd.getBankHolidayDate(), bankHolidayToAdd.getLastUpdatedBy());
	}
}
