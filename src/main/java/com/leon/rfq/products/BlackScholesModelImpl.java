package com.leon.rfq.products;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
 
public final class BlackScholesModelImpl implements OptionPricingModel
{
        // Variables for intermediate calculations
        private boolean isCallOption = true;
        
        private static BigDecimal TWO = BigDecimal.valueOf(2.0);
        private static BigDecimal NEGATIVE_ONE = BigDecimal.valueOf(-1);
        private static BigDecimal HUNDRED = BigDecimal.valueOf(100);
        
        private static BigDecimal A0 = new BigDecimal("0.2316419");
        private static BigDecimal A1 = new BigDecimal("0.31938153");
        private static BigDecimal A2 = new BigDecimal("-0.356563782");
        private static BigDecimal A3 = new BigDecimal("1.781477937");
        private static BigDecimal A4 = new BigDecimal("-1.821255978");
        private static BigDecimal A5 = new BigDecimal("1.330274429");
        
        private int scale = 4;
        
        private BigDecimal piCalcVal;
        private BigDecimal timeToExpirySquareRoot;
        private final BigDecimal timeToExpiryInYears;
        private BigDecimal expInterestRate;
        private final BigDecimal interestRate;
        private final BigDecimal underlyingPrice;
        private final BigDecimal strike;
        private final BigDecimal volatility;
        private BigDecimal d1;
        private BigDecimal d2;
        
        private BigDecimal theoreticalValue;
        private BigDecimal intrinsicValue;
        private BigDecimal timeValue;
        private BigDecimal delta;
        private BigDecimal gamma;
        private BigDecimal vega;
        private BigDecimal theta;
        private BigDecimal rho;
        private BigDecimal lambda;

        
        public BlackScholesModelImpl(boolean isCallOption, BigDecimal timeToExpiryInYears, BigDecimal volatility,
        		BigDecimal strike, BigDecimal underlyingPrice, BigDecimal interestRate)
        {
        	this.isCallOption = isCallOption;
        	this.timeToExpiryInYears = timeToExpiryInYears;
        	this.volatility = volatility;
        	this.strike = strike;
        	this.interestRate = interestRate;
        	this.underlyingPrice = underlyingPrice;
        }
        
        public void calculateSeedValues()
        {
        	this.piCalcVal = BigDecimal.ONE.divide(squareRoot(TWO.multiply(BigDecimal.valueOf(Math.PI))), this.scale, RoundingMode.HALF_UP);
        	this.timeToExpirySquareRoot = squareRoot(this.timeToExpiryInYears);
        	this.expInterestRate = BigDecimal.valueOf(Math.exp((negate(this.interestRate).multiply(this.timeToExpiryInYears)).doubleValue()));
        	BigDecimal logCalc = BigDecimal.valueOf(Math.log(this.underlyingPrice.divide(this.strike, this.scale, RoundingMode.HALF_UP).doubleValue()));
        	BigDecimal volSq = this.volatility.multiply(this.timeToExpirySquareRoot);
        	BigDecimal volPow = this.volatility.pow(2).divide(TWO, this.scale, RoundingMode.HALF_UP);
        	this.d1 = (logCalc.add(this.interestRate.add(volPow.multiply(this.timeToExpiryInYears)))).divide(volSq, this.scale, RoundingMode.HALF_UP);
        	this.d2 = this.d1.subtract(volSq);
        }
        
        public void setScale(int scale)
        {
        	this.scale = scale;
        }
        
        private BigDecimal expCalculatedValue(BigDecimal initialValue)
        {
        	BigDecimal absolute =  initialValue.abs();
        	return BigDecimal.valueOf(Math.exp(((NEGATIVE_ONE.multiply(absolute)).multiply(absolute)).divide(TWO, this.scale, RoundingMode.HALF_UP).doubleValue()));
        }
               
        @Override
		public void setToCall(boolean isCallOption)
        {
            this.isCallOption = isCallOption;
        }
       
        @Override
		public void setToEuropean(boolean isEuropeanOption) throws UnsupportedOperationException
        {
        	throw new UnsupportedOperationException("Black and scholes model can only be used to price European options");
        }
       
        @Override
		public OptionPriceResult calculate(Map<String, Double> input)
        {
    		OptionPriceResult optionResult = new OptionPriceResult();
            try
            {
/*                double volatility = input.get(VOLATILITY);
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
        		optionResult.setTheta(this.calculateOptionTheta(underlyingPrice, strike, interestRate, volatility));*/
        		
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
                       
        public BigDecimal calculateTheoreticalValue() throws Exception
        {
            if (this.isCallOption)
            	this.theoreticalValue = this.underlyingPrice.multiply(cummulativeNormalDensity(this.d1))
            	.subtract(this.strike.multiply(this.expInterestRate.multiply(cummulativeNormalDensity(this.d2))));
            else
            	this.theoreticalValue = this.strike.multiply(this.expInterestRate).multiply(cummulativeNormalDensity(negate(this.d2)))
            	.subtract(this.underlyingPrice.multiply(cummulativeNormalDensity(negate(this.d1))));
            
            return scale(this.theoreticalValue);
        }
       
        public BigDecimal calculateDelta() throws Exception
        {
        	this.delta = this.isCallOption ? cummulativeNormalDensity(this.d1) : negate(cummulativeNormalDensity(negate(this.d1)));
        	
        	return scale(this.delta);
        }
       
        public BigDecimal calculateGamma() throws Exception
        {
        	this.gamma = normalDensity(this.d1).divide(this.underlyingPrice.multiply(this.volatility)
        			.multiply(this.timeToExpirySquareRoot), this.scale, RoundingMode.HALF_UP);
        	
        	return this.gamma;
        }
       
        public BigDecimal calculateVega() throws Exception
        {
        	this.vega = (normalDensity(this.d1).multiply(this.underlyingPrice
        			.multiply(this.timeToExpirySquareRoot)).divide(HUNDRED, this.scale, RoundingMode.HALF_UP));
        	
        	return this.vega;
        }
       
        public BigDecimal calculateTheta() throws Exception
        {
            BigDecimal left = (this.underlyingPrice.multiply(this.volatility).multiply(normalDensity(this.d1)))
            		.divide(TWO.multiply(this.timeToExpirySquareRoot), this.scale, RoundingMode.HALF_UP);
            
            BigDecimal right = (this.interestRate.multiply(this.strike).multiply(this.expInterestRate)
            		.multiply(cummulativeNormalDensity(this.isCallOption ? this.d2 : negate(this.d2))));
            
            this.theta = negate(left.add(right)).divide(HUNDRED, this.scale, RoundingMode.HALF_UP);
            
            return this.theta;
        }
       
        public BigDecimal calculateRho() throws Exception
        {
            if (this.isCallOption)
            	this.rho = this.timeToExpiryInYears.multiply(this.strike).multiply(this.expInterestRate)
            		.multiply(cummulativeNormalDensity(this.d2)).divide(HUNDRED, this.scale, RoundingMode.HALF_UP);
            else
            	this.rho = negate(this.timeToExpiryInYears).multiply(this.strike).multiply(this.expInterestRate)
                	.multiply(cummulativeNormalDensity(negate(this.d2))).divide(HUNDRED, this.scale, RoundingMode.HALF_UP);
                    
            return this.rho;
        }
        
        public BigDecimal calculateLambda() throws Exception
        {
        	this.lambda = this.underlyingPrice.multiply(this.delta).divide(this.theoreticalValue, this.scale, RoundingMode.HALF_DOWN);
        	return scale(this.lambda);
        }
        
        public BigDecimal calculateIntrinsicValue() throws Exception
        {
        	this.intrinsicValue = this.isCallOption ? this.underlyingPrice.subtract(this.strike) : this.strike.subtract(this.underlyingPrice);
        	return scale(this.intrinsicValue.compareTo(BigDecimal.ZERO) > 0 ? this.intrinsicValue : BigDecimal.ZERO);
        }
        
        public BigDecimal calculateTimeValue() throws Exception
        {
        	this.timeValue = calculateTheoreticalValue().subtract(calculateIntrinsicValue());
        	return scale(this.timeValue.compareTo(BigDecimal.ZERO) > 0 ? this.timeValue : BigDecimal.ZERO);
        }
        
        private BigDecimal scale(BigDecimal initialValue)
        {
        	return initialValue.setScale(this.scale, RoundingMode.HALF_UP);
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
        
        public BigDecimal normalDensity(BigDecimal initialValue)
        {
            return this.piCalcVal.multiply(expCalculatedValue(initialValue));
        }
        
        public BigDecimal cummulativeNormalDensity(BigDecimal initialValue)
        {
        	BigDecimal absolute =  initialValue.abs();
        	BigDecimal k = BigDecimal.ONE.divide(BigDecimal.ONE.add(A0.multiply(absolute)), this.scale, RoundingMode.HALF_UP);
        	BigDecimal kPowers = ((A1.multiply(k)).add(A2.multiply(k.pow(2))).add(A3.multiply(k.pow(3)))).add(A4.multiply(k.pow(4)).add(A5.multiply(k.pow(5))));
        	BigDecimal result = BigDecimal.ONE.subtract(normalDensity(initialValue).multiply(kPowers));
        	
        	if(initialValue.compareTo(BigDecimal.ZERO) == -1)
        		return BigDecimal.ONE.subtract(result);
        	
        	return result;
        }
        
        public static void main(String... args)
        {
        	try
        	{
	        	BlackScholesModelImpl model = new BlackScholesModelImpl(false, BigDecimal.ONE, BigDecimal.valueOf(0.2),
	            		BigDecimal.valueOf(100), BigDecimal.valueOf(90), BigDecimal.valueOf(0.05));
	        	
	        	model.calculateSeedValues();
	        	
	        	System.out.println("Theorectical value: " + model.calculateTheoreticalValue());
	        	System.out.println("Intrinsic value: " + model.calculateIntrinsicValue());
	        	System.out.println("Time value: " + model.calculateTimeValue());
	        	System.out.println("Delta: " + model.calculateDelta());
	        	System.out.println("Gamma: " + model.calculateGamma());
	        	System.out.println("Vega: " + model.calculateVega());
	        	System.out.println("Theta: " + model.calculateTheta());
	        	System.out.println("Rho: " + model.calculateRho());
	        	System.out.println("Lambda: " + model.calculateLambda());
        	}
        	catch(Exception e)
        	{
        		e.printStackTrace();
        	}
       }
}