package com.leon.rfq.option;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leon.rfq.domains.EnumTypes.SideEnum;
import com.leon.rfq.domains.OptionDetailImpl;
import com.leon.rfq.domains.RequestDetailImpl;
import com.leon.rfq.services.BankHolidayMaintenanceService;
import com.leon.rfq.services.DefaultConfigurationService;
import com.leon.rfq.services.InterestRateService;
import com.leon.rfq.services.PriceService;
import com.leon.rfq.services.VolatilityService;
import com.leon.rfq.utilities.UtilityMethods;

@Service
public class OptionRequestParser
{
	private static final Logger logger = LoggerFactory.getLogger(OptionRequestParser.class);
	
	@Autowired
	BankHolidayMaintenanceService bankHolidayMaintenanceService;
	
	@Autowired
	DefaultConfigurationService defaultConfigurationService;
	
	@Autowired
	VolatilityService volatilityService;
	
	@Autowired
	PriceService priceService;
	
	@Autowired
	InterestRateService interestRateService;
	
    public boolean isValidOptionRequestSnippet(String snippet)
    {
    	if(logger.isDebugEnabled())
    		logger.debug("Validating option request snippet: " + snippet);
    	
        Pattern regexp = Pattern.compile("^([+-]?[\\d]*[CP]{1}){1}([-+]{1}[\\d]*[CP]{1})* ([\\d]+){1}(,{1}[\\d]+)* "
        		+ "[\\d]{1,2}(JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC)20[\\d]{2}(,{1}[\\d]{1,2}(JAN|FEB|MAR"
        		+ "|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC)20[\\d]{2})* (\\w){4,7}\\.[A-Z]{1,2}(,{1}(\\w){4,7}\\.[A-Z]{1,2})*$",
        		Pattern.CASE_INSENSITIVE);
        
        Matcher matcher = regexp.matcher(snippet);
       
        return matcher.matches();
    }
    
	/**
	 * Determines if an option is American or European depending on the capitalization of the initial part of snippet.
	 * 
	 * @param snippet 							the snippet to be pattern matched.
	 * @returns	true for European options if the initial C/P part of the snippet is in upper case.
	 */
    public boolean isEuropeanOption(String snippet)
    {
        Pattern regexp = Pattern.compile("^([+-]?[\\d]*[CP]{1}){1}([-+]{1}[\\d]*[CP]{1})*");
        Matcher matcher = regexp.matcher(snippet);
       
        return matcher.matches();
    }

    public void parseOptionStrikes(String delimitedStrikes, List<OptionDetailImpl> optionLegs)
    {
    	String[] strikes = delimitedStrikes.split(",");
    	// TODO - need to remove empty entries.
        
        if (strikes.length == 1)
        {
            for (OptionDetailImpl optionLeg : optionLegs)
                optionLeg.setStrike(new BigDecimal(strikes[0]));
        }
        else
        {
            int count = strikes.length - 1;
            for (OptionDetailImpl optionLeg : optionLegs)
            	optionLeg.setStrike(new BigDecimal(strikes[count--]));
        }
    }

    public void parseOptionMaturityDates(String delimitedDates, List<OptionDetailImpl> optionLegs)
    {
        String[] dates = delimitedDates.split(",");
        
        if (dates.length == 1)
        {
            for (OptionDetailImpl optionLeg : optionLegs)
            {
                optionLeg.setMaturityDate(dates[0]);
                optionLeg.setTradeDate(new Date().toString());
                
                optionLeg.setDaysToExpiry(new BigDecimal(this.bankHolidayMaintenanceService.CalculateBusinessDaysToExpiry(
                		UtilityMethods.convertToDate(optionLeg.getTradeDate()),
                		UtilityMethods.convertToDate(optionLeg.getMaturityDate()),
                		this.defaultConfigurationService.getDefaultLocation())));
            }
        }
        else
        {
            int count = 0;
            for (OptionDetailImpl optionLeg : optionLegs)
            {
            	optionLeg.setMaturityDate(dates[count++]);
            	optionLeg.setTradeDate(new Date().toString());
            	
                optionLeg.setDaysToExpiry(new BigDecimal(this.bankHolidayMaintenanceService.CalculateBusinessDaysToExpiry(
                		UtilityMethods.convertToDate(optionLeg.getTradeDate()),
                		UtilityMethods.convertToDate(optionLeg.getMaturityDate()),
                		this.defaultConfigurationService.getDefaultLocation())));
            }
        }
    }

    public void parseOptionUnderlyings(String delimitedUnderlyings, List<OptionDetailImpl> optionLegs)
    {
    	String[] underlyings = delimitedUnderlyings.split(",");
    	String ric;
    	
        if (underlyings.length == 1)
        {
            for (OptionDetailImpl optionLeg : optionLegs)
            {
            	ric = underlyings[0];
                optionLeg.setUnderlyingRIC(ric);
                optionLeg.setVolatility(this.volatilityService.getVolatility(ric));
                optionLeg.setUnderlyingPrice(this.priceService.getMidPrice(ric));
                optionLeg.setInterestRate(this.interestRateService.getInterestRate(ric));
                optionLeg.setDayCountConvention(this.defaultConfigurationService.getDefaultDayCountConvention());
            }
        }
        else
        {
            int count = 0;
            for (OptionDetailImpl optionLeg : optionLegs)
            {
                ric = underlyings[count++];
                // TODO Add underlying manager
                optionLeg.setUnderlyingRIC(ric);
                optionLeg.setVolatility(this.volatilityService.getVolatility(ric));
                optionLeg.setUnderlyingPrice(this.priceService.getMidPrice(ric));
                optionLeg.setInterestRate(this.interestRateService.getInterestRate(ric));
                optionLeg.setDayCountConvention(this.defaultConfigurationService.getDefaultDayCountConvention());
            }
        }
    }

    public List<OptionDetailImpl> ParseRequest(String request, RequestDetailImpl parent)
    {
    	String[] partsOfTheRequest = request.split(" ");
    	
    	List<OptionDetailImpl> optionLegs = parseOptionTypes(partsOfTheRequest[0], parent);
        parseOptionStrikes(partsOfTheRequest[1], optionLegs);
        parseOptionMaturityDates(partsOfTheRequest[2], optionLegs);
        parseOptionUnderlyings(partsOfTheRequest[3], optionLegs);
        
        return optionLegs;
    }

    public List<OptionDetailImpl> parseOptionTypes(String snippet, RequestDetailImpl parent)
    {
    	List<OptionDetailImpl> optionTypes = new LinkedList<>();
    	boolean isEuropean = isEuropeanOption(snippet);
        Pattern optionDetailRegex = Pattern.compile("^(?<side>[+-])?(?<quantity>[1-9])?(?<type>[CP]{1})+");
        Matcher detailMatcher = optionDetailRegex.matcher(snippet);
        
        Pattern optionLegRegex = Pattern.compile("^(?<leg>[+-]?[1-9]?[CP]{1})+",1);
        Matcher matcher = optionLegRegex.matcher(snippet);
        int legCount = 0;

        while (matcher.matches())
        {
            if (logger.isDebugEnabled())
            	logger.debug("Snippet before: " + snippet);
        	        	
        	String leg = matcher.group("leg");
            SideEnum side = detailMatcher.group("side").equals("-") ? SideEnum.SELL : SideEnum.BUY;
            int quantity = detailMatcher.group("quantity").isEmpty() ? 1 : Integer.parseInt(detailMatcher.group("quantity"));
            boolean isCall = detailMatcher.group("type").equals("C");

            optionTypes.add(new OptionDetailImpl(side, quantity, isCall, ++legCount, isEuropean, parent));

            if (logger.isDebugEnabled())
                logger.debug("leg #" + legCount + ": " + leg + ", side: " + side + ", quantity: " + quantity + ", Type: " + (isCall ? "CALL" : "PUT") + ", Type: " + (isEuropean ? "European" : "American"));

            snippet = snippet.replaceFirst(leg, "");
            ++legCount;
            
            if (logger.isDebugEnabled())
                logger.debug("Leg count incremented to: " + legCount + "Snippet after: " + snippet);
        }
        
        return optionTypes;
    }
	
}
