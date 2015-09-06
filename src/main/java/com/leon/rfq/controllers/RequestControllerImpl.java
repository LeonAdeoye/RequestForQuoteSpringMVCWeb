package com.leon.rfq.controllers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.leon.rfq.common.EnumTypes.StatusEnum;
import com.leon.rfq.common.Tag;
import com.leon.rfq.domains.PriceDetailImpl;
import com.leon.rfq.domains.RequestDetailImpl;
import com.leon.rfq.services.BookService;
import com.leon.rfq.services.CalculationService;
import com.leon.rfq.services.ClientService;
import com.leon.rfq.services.RequestService;
import com.leon.rfq.validators.RequestValidatorImpl;

@Controller
public class RequestControllerImpl
{
	private static final Logger logger = LoggerFactory.getLogger(RequestControllerImpl.class);
	
	@Autowired(required=true)
	RequestService requestService;
	
	@Autowired(required=true)
	BookService bookService;
	
	@Autowired(required=true)
	ClientService clientService;
	
	@Autowired(required=true)
	private CalculationService calculationService;
	
	@Autowired(required=true)
	private RequestValidatorImpl requestValidator;
	
	private final List<StatusEnum> listOfStatus = new ArrayList<StatusEnum>(Arrays.asList(StatusEnum.values()));
	
	@InitBinder
	public void initialiseBinder(WebDataBinder binder)
	{
		binder.setAllowedFields("request", "bookCode", "clientId", "language");
		//binder.setValidator(this.requestValidator);
	}
	
	@RequestMapping(value="/requests/today",
		method=RequestMethod.GET,
		produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Set<RequestDetailImpl> getAllRequestsFromTodayOnly()
	{
		return this.requestService.getAllFromTodayOnly();
	}
	 
	@RequestMapping(value="/requests/priceUpdates",
		method=RequestMethod.GET,
		produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Map<String, PriceDetailImpl> getPriceUpdates()
	{
		return this.requestService.getPriceUpdates();
	}
	
	@RequestMapping(value="/requests/statusUpdates",
		method=RequestMethod.GET,
		produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Set<RequestDetailImpl> getStatusUpdates()
	{
		return this.requestService.getStatusUpdates();
	}
	
	@RequestMapping(value="/requests/statusUpdatesFromSet",
			method=RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Set<RequestDetailImpl> getStatusUpdates(@RequestBody Set<RequestDetailImpl> setOfRequests)
	{
		return this.requestService.getStatusUpdates(setOfRequests);
	}
	
	@RequestMapping(value="/requests/calculationUpdates",
		method=RequestMethod.GET,
		produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Map<Integer, Map<String, BigDecimal>> getCalculationUpdates()
	{
		return this.requestService.getCalculationUpdates();
	}
	 
	@RequestMapping(value="/requests/createNewRequest",
			method=RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody RequestDetailImpl createNewRequest(@RequestBody RequestDetailImpl newRequest)
	{
		RequestDetailImpl createdRequest = this.requestService.insert(newRequest.getRequest(),
				newRequest.getClientId(), newRequest.getBookCode(), newRequest.getLastUpdatedBy());
	 
		if(createdRequest != null)
		{
			if(logger.isDebugEnabled())
				logger.debug("Created new request: " + createdRequest);
		}
		else
		{
			if(logger.isErrorEnabled())
				logger.error("Failed to create new request: " + newRequest);
		}
	
		return createdRequest;
	}
	 
	@RequestMapping(value="/requests/updateStatus",
			method=RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody RequestDetailImpl updateStatus(@RequestBody RequestDetailImpl requestToUpdate)
	{
		if(this.requestService.updateStatus(requestToUpdate))
		{
			if(logger.isDebugEnabled())
				logger.debug("Updated request with ID: " + requestToUpdate.getIdentifier()
						 + " to status: " + requestToUpdate.getStatus());
			
			return this.requestService.get(requestToUpdate.getIdentifier());
		}
		else
		{
			if(logger.isErrorEnabled())
				logger.error("Failed to Update request with ID: " + requestToUpdate.getIdentifier()
				+ " to status: " + requestToUpdate.getStatus());
			
			return null;
		}
	}
	 
	@RequestMapping(value="/requests/update",
			method=RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody RequestDetailImpl update(@RequestBody RequestDetailImpl requestToUpdate)
	{
		if(this.requestService.update(requestToUpdate))
		{
			if(logger.isDebugEnabled())
				logger.debug("Updated request with ID: " + requestToUpdate.getIdentifier());
			
			return this.requestService.get(requestToUpdate.getIdentifier());
		}
		else
		{
			if(logger.isErrorEnabled())
				logger.error("Failed to Update request with ID: " + requestToUpdate.getIdentifier());
			
			return null;
		}
	}
		
	@RequestMapping(value = "/requests", method = RequestMethod.GET)
	public String getAll(Model model)
	{
		return "requests";
	}
	
	@RequestMapping("/requests/request")
	public String get(@RequestParam int requestId, Model model)
	{
		model.addAttribute("request", this.requestService.get(requestId));
		return "request";
	}

	@RequestMapping(value = "/requests/add", method = RequestMethod.GET)
	public String getNewRequestForm(Model model)
	{
		model.addAttribute("newRequest", new RequestDetailImpl());
		
		return "addRequest";
	}
	
	@RequestMapping(value = "/requests/add", method = RequestMethod.POST)
	public String processNewRequestForm(@ModelAttribute("newRequest") @Valid RequestDetailImpl newRequest,
			BindingResult result, HttpServletRequest request, Model model)
	{
		String[] suppressedFields = result.getSuppressedFields();
		if(suppressedFields.length > 0)
		{
			throw new RuntimeException("Attempting to bind disallowed fields: " +
					StringUtils.arrayToCommaDelimitedString(suppressedFields));
		}
		
		if(result.hasErrors())
			return "requestsError";
	
		//TODO ladeoye - should get correct user name
		RequestDetailImpl createdRequest = this.requestService.insert(newRequest.getRequest(),
				newRequest.getClientId(), newRequest.getBookCode(),  "ladeoye");
		
		if(createdRequest != null)
		{
			 if(logger.isDebugEnabled())
				 logger.debug("Created new request: " + createdRequest);
		}
		else
		{
			if(logger.isErrorEnabled())
				logger.error("Failed to create new request: " + newRequest);
			
			model.addAttribute("error", "Failed to insert new request");
		}

		return "redirect:/requests";
	}
	
	@RequestMapping("/requests/delete")
	public String delete(@RequestParam int requestId, Model model)
	{
		if(!this.requestService.delete(requestId))
			model.addAttribute("error", "Failed to delete request with requestId: " + requestId);
			
		return "redirect:/requests";
	}
	
	@RequestMapping(value = "requests/matchingStatusTags", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Object getStatusMatches(@RequestParam String pattern)
	{
		return this.listOfStatus.stream().filter(status -> status.getDescription().toUpperCase().contains(pattern.toUpperCase()))
				.map(status -> new Tag(String.valueOf(status), status.getDescription())).collect(Collectors.toList());
	}
	
	@RequestMapping(value = "requests/statuses", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Object getStatuses()
	{
		return this.listOfStatus.stream().collect(Collectors.toMap(status -> status, status -> status.getDescription()));
	}
	
	@RequestMapping(value = "requests/ajaxGetChartData", method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Object ajaxGetChartData(@RequestBody int requestId)
	{
		if(logger.isDebugEnabled())
			logger.debug("Received chart data for request ID: " + requestId);
		
		return this.calculationService.chartData(this.requestService.get(requestId));
	}
}
