package com.leon.rfq.products;

import java.util.HashMap;
import java.util.Map;
 
final class  OptionPricingModelContext
{
        private OptionPricingModel model = null;
        private final Map<String, Double> input = new HashMap<String, Double>();
        private double dayCountConvention = 250.0;
               
        OptionPricingModelContext(OptionPricingModel model)
        {
                this.model = model;
        }
       
        OptionPricingModelContext(OptionPricingModel model, double volatility, double interestRate,
                        double strike, double underlyingPrice, double timeToExpiry)
        {
                this.model = model;
                this.input.put(OptionPricingModel.VOLATILITY , volatility);
                this.input.put(OptionPricingModel.INTEREST_RATE , interestRate);
                this.input.put(OptionPricingModel.STRIKE , strike);
                this.input.put(OptionPricingModel.UNDERLYING_PRICE , underlyingPrice);
                this.input.put(OptionPricingModel.TIME_TO_EXPIRY , timeToExpiry);
        }
       
        void setModel(OptionPricingModel model)
        {
                this.model = model;
        }
        
        OptionPricingModel getModel()
        {
        	return this.model;
        }
        
        void setToCall(boolean isCallOption)
        {
                if(this.model != null)
                        this.model.setToCall(isCallOption);
        }
       
        void setToEuropean(boolean isEuropeanOption)
        {
                if(this.model != null)
                        this.model.setToEuropean(isEuropeanOption);
        }
       
        void setDayCountConvention(double dayCountConvention)
        {
                this.dayCountConvention = dayCountConvention;
        }
       
        void setVolatility(double volatility)
        {
                this.input.put(OptionPricingModel.VOLATILITY , volatility);
        }
       
        void setInterestRate(double interestRate)
        {
                this.input.put(OptionPricingModel.INTEREST_RATE , interestRate);
        }
       
        void setSrike(double strike)
        {
                this.input.put(OptionPricingModel.STRIKE , strike);
        }
       
        void setUnderlyingPrice(double underlyingPrice)
        {
                this.input.put(OptionPricingModel.UNDERLYING_PRICE , underlyingPrice);
        }
        
        void setDaysToExpiry(double daysToExpiry)
        {
                this.input.put(OptionPricingModel.TIME_TO_EXPIRY , daysToExpiry / this.dayCountConvention);
        }
       
        void setYearsToExpiry(double yearsToExpiry)
        {
                this.input.put(OptionPricingModel.TIME_TO_EXPIRY , yearsToExpiry);
        }
               
        OptionPriceResult calculate() throws Exception
        {
                return this.model.calculate(this.input);
        }
       
        OptionPriceResultSet calculateRange(String rangeKey, double startValue, double endValue, double increment) throws Exception
        {
        	OptionPriceResultSet resultSet = new OptionPriceResultSet();
            this.model.calculateRange(resultSet, this.input, rangeKey, startValue, endValue, increment);
            return resultSet;
        }
}
