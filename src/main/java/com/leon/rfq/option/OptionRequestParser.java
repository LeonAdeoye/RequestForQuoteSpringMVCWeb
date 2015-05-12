package com.leon.rfq.option;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leon.rfq.domains.OptionDetailImpl;
import com.leon.rfq.services.BankHolidayMaintenanceService;
import com.leon.rfq.services.PriceService;
import com.leon.rfq.services.ProductConfigurationService;
import com.leon.rfq.services.VolatilityService;
import com.leon.rfq.utilities.UtilityMethods;

@Service
public class OptionRequestParser
{
	@Autowired
	BankHolidayMaintenanceService bankHolidayMaintenanceService;
	
	@Autowired
	ProductConfigurationService productConfigurationService;
	
	@Autowired
	VolatilityService volatilityService;
	
	@Autowired
	PriceService priceService;
	

    public final BigDecimal DAY_COUNT_CONVENTION_255 = new BigDecimal("255");
    public final BigDecimal DAY_COUNT_CONVENTION_250 = new BigDecimal("250");
    public final BigDecimal DAY_COUNT_CONVENTION_265 = new BigDecimal("265");
	
    public boolean isValidOptionRequestSnippet(String snippet)
    {
        Pattern pattern = Pattern.compile("^([+-]?[\\d]*[CP]{1}){1}([-+]{1}[\\d]*[CP]{1})* ([\\d]+){1}(,{1}[\\d]+)* "
        		+ "[\\d]{1,2}(JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC)20[\\d]{2}(,{1}[\\d]{1,2}(JAN|FEB|MAR"
        		+ "|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC)20[\\d]{2})* (\\w){4,7}\\.[A-Z]{1,2}(,{1}(\\w){4,7}\\.[A-Z]{1,2})*$");
        Matcher matcher = pattern.matcher(snippet.toUpperCase());
       
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
                		this.productConfigurationService.getLocation())));
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
                		this.productConfigurationService.getLocation())));
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
                //TODO
                optionLeg.setIsEuropean(true);
                optionLeg.setInterestRate(new BigDecimal("0.1"));
                optionLeg.setDayCountConvention(this.DAY_COUNT_CONVENTION_250);
            }
        }
        else
        {
            int count = 0;
            for (OptionDetailImpl optionLeg : optionLegs)
            {
                ric = underlyings[count++];
                // TO-DO Add underlying manager
                optionLeg.setUnderlyingRIC(ric);
                optionLeg.setVolatility(this.volatilityService.getVolatility(ric));
                optionLeg.setUnderlyingPrice(this.priceService.getMidPrice(ric));
                //TODO
                optionLeg.setIsEuropean(true);
                optionLeg.setInterestRate(new BigDecimal("0.1"));
                optionLeg.setDayCountConvention(this.DAY_COUNT_CONVENTION_250);
            }
        }
    }

/*    public List<OptionDetailImpl> ParseRequest(string request, IRequestForQuote parent)
    {
        var partsOfTheRequest  = request.Split(new char[] {' '}, StringSplitOptions.RemoveEmptyEntries);
        var optionLegs = ParseOptionTypes(partsOfTheRequest[0], parent);
        ParseOptionStrikes(partsOfTheRequest[1], optionLegs);
        ParseOptionMaturityDates(partsOfTheRequest[2], optionLegs);
        ParseOptionUnderlyings(partsOfTheRequest[3], optionLegs);
        return optionLegs;
    }*/

/*    public List<OptionDetailImpl> ParseOptionTypes(string request, IRequestForQuote parent)
    {
        var optionTypes = new List<OptionDetailImpl>();
        var optionDetailReg = new Regex(@"^(?<side>[+-])?(?<quantity>[1-9])?(?<type>[CP]{1})+");
        var optionLegReg = new Regex(@"^(?<leg>[+-]?[1-9]?[CP]{1})+");
        var matchedLegs = optionLegReg.Match(request);
        var legCount = 0;

        while (matchedLegs.Success)
        {
            var leg = matchedLegs.Groups["leg"].ToString();
            var matchedDetails = optionDetailReg.Match(leg);

            var side = matchedDetails.Groups["side"].Value == "-" ? SideEnum.SELL : SideEnum.BUY;
            var quantity = matchedDetails.Groups["quantity"].Value == "" ? 1 : Convert.ToInt32(matchedDetails.Groups["quantity"].Value);
            var isCall = matchedDetails.Groups["type"].Value == "C";

            optionTypes.Add(new OptionDetailImpl()
            {
                Side = side,
                Quantity = quantity,
                IsCall = isCall,
                LegId = ++legCount,
                ParentRequest = parent
            });

            if (log.IsDebugEnabled)
                log.Debug("leg #" + optionTypes.Count + ": " + leg + ", leg side: " + side + ", quantity: " + quantity + ", Type: " + (isCall ? "CALL" : "PUT"));

            request = request.Replace(leg, "");
            matchedLegs = optionLegReg.Match(request);
        }
        return optionTypes;
    }*/
	
}
