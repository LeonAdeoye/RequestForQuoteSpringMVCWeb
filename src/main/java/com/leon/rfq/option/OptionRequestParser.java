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
import org.springframework.stereotype.Component;

import com.leon.rfq.domains.EnumTypes.SideEnum;
import com.leon.rfq.domains.OptionDetailImpl;
import com.leon.rfq.domains.RequestDetailImpl;
import com.leon.rfq.services.BankHolidayMaintenanceService;
import com.leon.rfq.services.DefaultConfigurationService;
import com.leon.rfq.services.InterestRateService;
import com.leon.rfq.services.PriceService;
import com.leon.rfq.services.VolatilityService;
import com.leon.rfq.utilities.UtilityMethods;

@Component
public final class OptionRequestParser
{
	private static final Logger logger = LoggerFactory.getLogger(OptionRequestParser.class);
	
	@Autowired(required=true)
	BankHolidayMaintenanceService bankHolidayMaintenanceService;
	
	@Autowired(required=true)
	DefaultConfigurationService defaultConfigurationService;
	
	@Autowired(required=true)
	VolatilityService volatilityService;
	
	@Autowired(required=true)
	PriceService priceService;
	
	@Autowired(required=true)
	InterestRateService interestRateService;
	
	
	/**
	 * Determines if the snippet is valid for an option request.
	 * 
	 * @param snippet 		the snippet to be pattern matched.
	 * @returns	true if the snippet is a valid option request snippet.
	 */
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
    private boolean isEuropeanOption(String snippet)
    {
        Pattern regexp = Pattern.compile("^([+-]?[\\d]*[CP]{1}){1}([-+]{1}[\\d]*[CP]{1})*");
        Matcher matcher = regexp.matcher(snippet);
       
        return matcher.matches();
    }

	/**
	 * Parses the delimited string containing options strikes and assigns them to each option leg.
	 * 
	 * @param delimitedStrikes 	the delimited string containing the strikes.
	 * @param optionLegs 		the list of option legs that the strikes are to be applied to.
	 */
    private void parseOptionStrikes(String delimitedStrikes, List<OptionDetailImpl> optionLegs)
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

	/**
	 * Parses the delimited string containing options maturity dates and assigns them to each option leg.
	 * 
	 * @param delimitedDates 	the delimited string containing the maturity dates.
	 * @param optionLegs 		the list of option legs that the maturity dates are to be applied to.
	 */
    private void parseOptionMaturityDates(String delimitedDates, List<OptionDetailImpl> optionLegs)
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

	/**
	 * Parses the delimited string containing underlying RICs and assigns them to each option leg.
	 * 
	 * @param delimitedUnderlyings 	the delimited string containing the underlying RICs.
	 * @param optionLegs 		the list of option legs that the underlying RICs are to be applied to.
	 */
    private void parseOptionUnderlyings(String delimitedUnderlyings, List<OptionDetailImpl> optionLegs)
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
                // TODO Add to underlying manager new RICs if they don't exist.
                optionLeg.setUnderlyingRIC(ric);
                optionLeg.setVolatility(this.volatilityService.getVolatility(ric));
                optionLeg.setUnderlyingPrice(this.priceService.getMidPrice(ric));
                optionLeg.setInterestRate(this.interestRateService.getInterestRate(ric));
                optionLeg.setDayCountConvention(this.defaultConfigurationService.getDefaultDayCountConvention());
            }
        }
    }

	/**
	 * Parses the request snippet containing other option characteristics and assigns them to each option leg.
	 * This method calls the other private parsing methods.
	 * 
	 * @param snippet 	the option request snippet containing all of option details to be parsed.
	 * @param parent 	the parent request that these option details belong to.
	 */
    public void parseRequest(String snippet, RequestDetailImpl parent)
    {
    	String[] partsOfTheRequest = snippet.split(" ");
    	List<OptionDetailImpl> optionLegs = parseOptionTypes(partsOfTheRequest[0], parent);
        parseOptionStrikes(partsOfTheRequest[1], optionLegs);
        parseOptionMaturityDates(partsOfTheRequest[2], optionLegs);
        parseOptionUnderlyings(partsOfTheRequest[3], optionLegs);
        parent.setLegs(optionLegs);
    }

	/**
	 * Parses the request snippet containing other option characteristics and assigns them to each option leg.
	 * 
	 * @param snippet 	the option request snippet containing the other option details.
	 * @param parent 	the parent request that these option details belong to.
	 * @Return List<OptionDetailImpl> the list of options with the characteristics populated.
	 */
    private List<OptionDetailImpl> parseOptionTypes(String snippet, RequestDetailImpl parent)
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
