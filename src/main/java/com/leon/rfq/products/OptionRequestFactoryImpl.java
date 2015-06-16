package com.leon.rfq.products;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.leon.rfq.common.EnumTypes.SideEnum;
import com.leon.rfq.common.EnumTypes.StatusEnum;
import com.leon.rfq.common.RegexConstants;
import com.leon.rfq.domains.OptionDetailImpl;
import com.leon.rfq.domains.RequestDetailImpl;
import com.leon.rfq.services.BankHolidayService;
import com.leon.rfq.services.CalculationServiceImpl;
import com.leon.rfq.services.DefaultConfigurationService;
import com.leon.rfq.services.InterestRateService;
import com.leon.rfq.services.PriceService;
import com.leon.rfq.services.VolatilityService;

@Component
public class OptionRequestFactoryImpl implements OptionRequestFactory
{
	private static final Logger logger = LoggerFactory.getLogger(OptionRequestFactoryImpl.class);
	
	@Autowired(required=true)
	private BankHolidayService bankHolidayService;
	
	@Autowired(required=true)
	private DefaultConfigurationService defaultConfigurationService;
	
	@Autowired(required=true)
	private VolatilityService volatilityService;
	
	@Autowired(required=true)
	private  PriceService priceService;
	
	@Autowired(required=true)
	private InterestRateService interestRateService;
	
	public OptionRequestFactoryImpl() {}
	
	/**
	 * Creates an instance of RequestDetailImpl using the parameters passed.
	 * 
	 * @param requestSnippet 	the snippet to be used to create the RequestDetailImpl instance.
	 * @param clientId			the client ID of the RequestDetailImpl instance to be created.
	 * @param bookCode			the book code of the RequestDetailImpl instance to be created.
	 * @param savedByUser		the user saving the new instance.
	 * @return	the new instance.
	 */
	@Override
	public RequestDetailImpl getNewInstance(String requestSnippet, int clientId, String bookCode, String savedByUser)
	{
		RequestDetailImpl newRequest = new RequestDetailImpl();
		
		if(!isValidOptionRequestSnippet(requestSnippet))
		{
			if(logger.isErrorEnabled())
				logger.error("requestSnippet argument is invalid");
			
			throw new IllegalArgumentException("requestSnippet argument is invalid");
		}
		
		if(!parseRequest(requestSnippet, newRequest))
		{
			if(logger.isErrorEnabled())
				logger.error("requestSnippet argument is invalid");
			
			throw new IllegalArgumentException("requestSnippet argument is invalid");
		}
		
		newRequest.setBookCode(bookCode);
		newRequest.setClientId(clientId);
		newRequest.setLastUpdatedBy(savedByUser);
        newRequest.setRequest(requestSnippet);
        newRequest.setStatus(StatusEnum.PENDING);
        newRequest.setIdentifier(-1);
        newRequest.setClientId(clientId);
        newRequest.setTradeDate(LocalDate.now());
        
        if(newRequest.getLegs() !=null)
            newRequest.setExpiryDate(newRequest.getLegs().get(0).getMaturityDate());
        
        newRequest.setLotSize(100);
        newRequest.setMultiplier(10);
        newRequest.setContracts(100);
        newRequest.setNotionalFXRate(new BigDecimal("1"));
        newRequest.setNotionalMillions(new BigDecimal("1"));
        newRequest.setBookCode(bookCode);

        if(newRequest.getLegs() !=null)
            newRequest.setDayCountConvention(newRequest.getLegs().get(0).getDayCountConvention());

        newRequest.setPremiumSettlementFXRate(new BigDecimal("1"));
        newRequest.setSalesCreditFXRate(new BigDecimal("1"));
        newRequest.setIsOTC(true);
        newRequest.setSalesCreditPercentage(new BigDecimal("2"));
        newRequest.setPremiumSettlementDaysOverride(1);
        newRequest.setPremiumSettlementDate(LocalDate.now().plusDays(newRequest.getPremiumSettlementDaysOverride()));
        
        // TODO select model depending on certain criteria
        CalculationServiceImpl.calculate(new BlackScholesModelImpl(), newRequest);
		
        return newRequest;
	}
	
	/**
	 * Determines if the snippet is valid for an option request.
	 * 
	 * @param snippet 		the snippet to be pattern matched.
	 * @return	true if the snippet is a valid option request snippet.
	 */
	public static boolean isValidOptionRequestSnippet(String snippet)
    {
    	if(logger.isDebugEnabled())
    		logger.debug("Validating option request snippet: " + snippet);
    	
        Pattern regexp = Pattern.compile(RegexConstants.REQUEST_PATTERN, Pattern.CASE_INSENSITIVE);
        
        Matcher matcher = regexp.matcher(snippet);
       
        return matcher.matches();
    }
    
	/**
	 * Determines if an option is American or European depending on the capitalization of the initial part of snippet.
	 * 
	 * @param snippet 							the snippet to be pattern matched.
	 * @return	true for European options if the initial C/P part of the snippet is in upper case.
	 */
    public static boolean isEuropeanOption(String snippet)
    {
        Pattern euRegex = Pattern.compile(RegexConstants.REQUEST_PATTERN);
        Matcher euMatcher = euRegex.matcher(snippet);
       
        return euMatcher.matches();
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMMyyyy");
        
        if (dates.length == 1)
        {
            for (OptionDetailImpl optionLeg : optionLegs)
            {
            	optionLeg.setMaturityDate(LocalDate.parse(dates[0], formatter));
            	optionLeg.setTradeDate(LocalDate.now());
                
                optionLeg.setDaysToExpiry(new BigDecimal(this.bankHolidayService.calculateBusinessDaysToExpiry(
                		optionLeg.getTradeDate(),
                		optionLeg.getMaturityDate(),
                		this.defaultConfigurationService.getDefaultLocation())));
            }
        }
        else
        {
            int count = 0;
            for (OptionDetailImpl optionLeg : optionLegs)
            {
            	optionLeg.setMaturityDate(LocalDate.parse(dates[count++], formatter));
            	optionLeg.setTradeDate(LocalDate.now());
            	
                optionLeg.setDaysToExpiry(new BigDecimal(this.bankHolidayService.calculateBusinessDaysToExpiry(
                		optionLeg.getTradeDate(),
                		optionLeg.getMaturityDate(),
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
	 * @return boolean	true if the option is parsed successfully otherwise false;
	 */
    @Override
	public boolean parseRequest(String snippet, RequestDetailImpl parent)
    {
    	try
    	{
	    	String[] partsOfTheRequest = snippet.split(" ");
	    	List<OptionDetailImpl> optionLegs = parseOptionTypes(partsOfTheRequest[0], parent);
	        parseOptionStrikes(partsOfTheRequest[1], optionLegs);
	        parseOptionMaturityDates(partsOfTheRequest[2], optionLegs);
	        parseOptionUnderlyings(partsOfTheRequest[3], optionLegs);
	        parent.setLegs(optionLegs);
	        return true;
    	}
    	catch(Exception e)
    	{
    		if(logger.isErrorEnabled())
    			logger.error("Failed to parse option request. Exception thrown: "+ e);
    		return false;
    	}
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
    	
        Pattern optionDetailRegex = Pattern.compile(RegexConstants.DETAIL_PATTERN);
        Matcher detailMatcher = optionDetailRegex.matcher(snippet);
        
        Pattern optionLegRegex = Pattern.compile(RegexConstants.LEG_PATTERN);
        Matcher matcher = optionLegRegex.matcher(snippet);
        int legCount = 0;

        while (matcher.matches())
        {
            if (logger.isDebugEnabled())
            	logger.debug("Snippet before: " + snippet);
        	        	
        	String leg = matcher.group("leg");
        	
        	detailMatcher.find();
        	String sideGroup = detailMatcher.group("side");
            SideEnum side = ((sideGroup != null) && sideGroup.equals("-")) ? SideEnum.SELL : SideEnum.BUY;
            
            String quantityGroup = detailMatcher.group("quantity");
            int quantity = (quantityGroup != null) ? Integer.parseInt(detailMatcher.group("quantity")) : 1;
            
            boolean isCall = detailMatcher.group("type").equals("C");

            optionTypes.add(new OptionDetailImpl(side, quantity, isCall, ++legCount, isEuropean, parent));

            if (logger.isDebugEnabled())
                logger.debug("leg #" + legCount + ": " + leg + ", side: " + side + ", quantity: " + quantity + ", Type: " + (isCall ? "CALL" : "PUT"));

            snippet = snippet.replaceFirst(leg, "");
            
            if (logger.isDebugEnabled())
                logger.debug("Remaining snippet after processing leg: " + (legCount++) + "is: "+ snippet);
            
            matcher = optionLegRegex.matcher(snippet);
        }
        
        return optionTypes;
    }
	
}
