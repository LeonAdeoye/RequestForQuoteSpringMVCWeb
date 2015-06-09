package com.leon.rfq.products;
import static java.lang.Math.exp;
import static java.lang.Math.log;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
 
public final class BlackScholesModelImpl implements OptionPricingModel
{
        // Variables for intermediate calculations
        //private double d1 = 0.0;
        //private double d2 = 0.0;
        private double e = 0.0;
        private double t = 0.0;
        private boolean isCallOption = true;
        private boolean isEuropeanOption = true;
        
        private static BigDecimal TWO = BigDecimal.valueOf(2.0);
        private static BigDecimal NEGATIVE_ONE = BigDecimal.valueOf(-1);
        private static BigDecimal HUNDRED = BigDecimal.valueOf(100);
        
        private static BigDecimal A0 = new BigDecimal("0.2316419");
        private static BigDecimal A1 = new BigDecimal("0.31938153");
        private static BigDecimal A2 = new BigDecimal("-0.356563782");
        private static BigDecimal A3 = new BigDecimal("1.781477937");
        private static BigDecimal A4 = new BigDecimal("-1.821255978");
        private static BigDecimal A5 = new BigDecimal("1.330274429");
        
        private static final int SCALE = 4;
        
        private BigDecimal piCalcVal;
        private BigDecimal timeToExpirySquareRoot;
        private BigDecimal timeToExpiryInYears;
        private BigDecimal expInterestRate;
        private BigDecimal interestRate;
        private BigDecimal underlyingPrice;
        private BigDecimal strike;
        private BigDecimal volatility;
        private BigDecimal d1;
        private BigDecimal d2;
        
        public BlackScholesModelImpl()
        {
        }
        
        public void initialization()
        {
        	this.piCalcVal = BigDecimal.ONE.divide(squareRoot(TWO.multiply(BigDecimal.valueOf(Math.PI))), SCALE, RoundingMode.HALF_UP);
        	this.timeToExpirySquareRoot = squareRoot(this.timeToExpiryInYears);
        	this.expInterestRate = BigDecimal.valueOf(Math.exp((NEGATIVE_ONE.multiply(this.interestRate).multiply(this.timeToExpiryInYears)).doubleValue()));
        	BigDecimal logCalc = BigDecimal.valueOf(Math.log(this.underlyingPrice.divide(this.strike, SCALE, RoundingMode.HALF_UP).doubleValue()));
        	BigDecimal volSq = this.volatility.multiply(this.timeToExpirySquareRoot);
        	BigDecimal volPow = this.volatility.pow(2).divide(TWO, SCALE, RoundingMode.HALF_UP);
        	this.d1 = (logCalc.add(this.interestRate.add(volPow.multiply(this.timeToExpiryInYears)))).divide(volSq, SCALE, RoundingMode.HALF_UP);
        	this.d2 = this.d1.subtract(volSq);
        }
        
        private static BigDecimal expCalculatedValue(BigDecimal initialValue)
        {
        	BigDecimal absolute =  initialValue.abs();
        	return BigDecimal.valueOf(Math.exp(((NEGATIVE_ONE.multiply(absolute)).multiply(absolute)).divide(TWO, SCALE, RoundingMode.HALF_UP).doubleValue()));
        }
               
        @Override
		public void setToCall(boolean isCallOption)
        {
            this.isCallOption = isCallOption;
        }
       
        @Override
		public void setToEuropean(boolean isEuropeanOption)
        {
            this.isEuropeanOption = isEuropeanOption;
        }
       
        @Override
		public OptionPriceResult calculate(Map<String, Double> input)
        {
    		OptionPriceResult optionResult = new OptionPriceResult();
            try
            {
                double volatility = input.get(VOLATILITY);
                double interestRate = input.get(INTEREST_RATE);
                double strike = input.get(STRIKE);
                double underlyingPrice = input.get(UNDERLYING_PRICE);
                double timeToExpiryInYears = input.get(TIME_TO_EXPIRY);
               
                // Calculate these two intermediate calculations and reuse
                this.d1 = (log(underlyingPrice/strike)+((interestRate + ((volatility * volatility)/2))* timeToExpiryInYears))/(volatility * Math.sqrt(timeToExpiryInYears));
                this.d2 = this.d1 - (volatility * Math.sqrt(timeToExpiryInYears));
                this.e = exp(-interestRate * timeToExpiryInYears);
                this.t = Math.sqrt(timeToExpiryInYears);
                                             
        		optionResult.setPrice(this.calculateOptionPrice(underlyingPrice, strike, timeToExpiryInYears, interestRate));
        		optionResult.setDelta(this.calculateOptionDelta());
        		optionResult.setGamma(this.calculateOptionGamma(underlyingPrice, volatility));
        		optionResult.setVega(this.calculateOptionVega(underlyingPrice));
        		optionResult.setRho(this.calculateOptionRho(strike, timeToExpiryInYears, interestRate));
        		optionResult.setTheta(this.calculateOptionTheta(underlyingPrice, strike, interestRate, volatility));
        		
                return optionResult;
            }
            catch(Exception e)
            {
                throw new RuntimeException(this.toString() + " calculation error: " + e.getMessage());
            }
        }
        
        @Override
		public void calculateRange(OptionPriceResultSet optionPriceResultSet, Map<String, Double> input, String rangeKey, double startValue, double endValue, double increment)
        {
            try
            {
                for(double value = startValue; value <= endValue; value += increment)
                {
                    input.put(rangeKey, value);
                    OptionPriceResult optionPriceResult = calculate(input);
                    optionPriceResult.setRangeVariable(value);
                    optionPriceResultSet.merge(optionPriceResult);
                }
            }
            catch(Exception e)
            {
            	throw new RuntimeException(this.toString() + " calculation range error: " + e.getMessage());
            }
        }
                       
        public BigDecimal calculateOptionPrice() throws Exception
        {
            if (this.isCallOption)
                return this.underlyingPrice.multiply(cummulativeNormalDistribution(this.d1)).subtract(this.strike.multiply(BigDecimal.ZERO.multiply(cummulativeNormalDistribution(this.d2))));
            else
                return this.strike.multiply(BigDecimal.ZERO).multiply(cummulativeNormalDistribution(negate(this.d2))).subtract(this.underlyingPrice.multiply(cummulativeNormalDistribution(negate(this.d1))));
        }
       
        public BigDecimal calculateOptionDelta() throws Exception
        {
            if(this.isCallOption)
            	return scale(cummulativeNormalDistribution(this.d1));
            else
                return scale(negate(cummulativeNormalDistribution(negate(this.d1))));
        }
       
        public BigDecimal calculateOptionGamma() throws Exception
        {
                return normalDistribution(this.d1).divide(this.underlyingPrice.multiply(this.volatility).multiply(BigDecimal.ZERO), SCALE, RoundingMode.HALF_UP);
        }
       
        public BigDecimal calculateOptionVega() throws Exception
        {
        	return (normalDistribution(this.d1).multiply(this.underlyingPrice.multiply(BigDecimal.ZERO)).divide(HUNDRED, SCALE, RoundingMode.HALF_UP));
        }
       
        public BigDecimal calculateOptionTheta() throws Exception
        {
            if (this.isCallOption)
                    return -1 * ((((this.underlyingPrice * this.volatility * ND(this.d1))/(2 * this.t))  + (this.interestRate * this.strike * this.e * CND(this.d2))) / 100.0);
            else
                    return -1 * ((((this.underlyingPrice * this.volatility * ND(this.d1))/(2 * this.t))  + (this.interestRate * this.strike * this.e * CND(-this.d2))) / 100.0);
        }
       
        public BigDecimal calculateOptionRho() throws Exception
        {
            if (this.isCallOption)
                    return (this.timeToExpiryInYears * this.strike * this.e * cummulativeNormalDistribution(this.d2)) .divide(HUNDRED, SCALE, RoundingMode.HALF_UP);
            else
                    return (negate(this.timeToExpiryInYears).multiply(this.strike).multiply * this.e * cummulativeNormalDistribution(negate(this.d2))).divide(HUNDRED, SCALE, RoundingMode.HALF_UP);
        }
        
        private BigDecimal scale(BigDecimal initialValue)
        {
        	return initialValue.setScale(SCALE, RoundingMode.HALF_UP);
        }
        
        private BigDecimal negate(BigDecimal initialValue)
        {
        	return initialValue.multiply(NEGATIVE_ONE);
        }
        
        public static BigDecimal squareRoot(BigDecimal value)
        {
            BigDecimal x = new BigDecimal(Math.sqrt(value.doubleValue()));
            return x.add(new BigDecimal(value.subtract(x.multiply(x)).doubleValue() / (x.doubleValue() * 2.0)));
        }
        
        public BigDecimal normalDistribution(BigDecimal initialValue)
        {
            return this.piCalcVal.multiply(expCalculatedValue(initialValue));
        }
        
        public BigDecimal cummulativeNormalDistribution(BigDecimal initialValue)
        {
        	BigDecimal absolute =  initialValue.abs();
        	BigDecimal k = BigDecimal.ONE.divide(BigDecimal.ONE.add(A0.multiply(absolute)), SCALE, RoundingMode.HALF_UP);
        	BigDecimal kPowers = ((A1.multiply(k)).add(A2.multiply(k.pow(2))).add(A3.multiply(k.pow(3)))).add(A4.multiply(k.pow(4)).add(A5.multiply(k.pow(5))));
        	BigDecimal result = BigDecimal.ONE.subtract(normalDistribution(initialValue).multiply(kPowers));
        	
        	if(initialValue.compareTo(BigDecimal.ZERO) == -1)
        		return BigDecimal.ONE.subtract(result);
        	
        	return result;
        }
        
        public static void main(String... args)
        {
        	double fred = ND(0.775);
        	System.out.println(fred);
			BigDecimal bon = normalDistribution(new BigDecimal("0.775"));
        	System.out.println(bon);
        	
        	// For testing...
        	@SuppressWarnings("unused")
			Map<String, Double> input = new HashMap<>();
       }
}