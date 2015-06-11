package com.leon.rfq.products;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.leon.rfq.common.OptionConstants;
 
public final class BlackScholesModelImpl implements PricingModel
{
	private static final Logger logger = LoggerFactory.getLogger(BlackScholesModelImpl.class);
	
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
        private BigDecimal timeToExpiryInYears;
        private BigDecimal expInterestRate;
        private BigDecimal interestRate;
        private BigDecimal underlyingPrice;
        private BigDecimal strike;
        private BigDecimal volatility;
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
        	return BigDecimal.valueOf(Math.exp(((negate(absolute)).multiply(absolute)).divide(TWO, this.scale, RoundingMode.HALF_UP).doubleValue()));
        }
                       
        private BigDecimal calculateTheoreticalValue() throws Exception
        {
            if (this.isCallOption)
            	this.theoreticalValue = this.underlyingPrice.multiply(cummulativeNormalDensity(this.d1))
            	.subtract(this.strike.multiply(this.expInterestRate.multiply(cummulativeNormalDensity(this.d2))));
            else
            	this.theoreticalValue = this.strike.multiply(this.expInterestRate).multiply(cummulativeNormalDensity(negate(this.d2)))
            	.subtract(this.underlyingPrice.multiply(cummulativeNormalDensity(negate(this.d1))));
            
            return scale(this.theoreticalValue);
        }
       
        private BigDecimal calculateDelta() throws Exception
        {
        	this.delta = this.isCallOption ? cummulativeNormalDensity(this.d1) : negate(cummulativeNormalDensity(negate(this.d1)));
        	
        	return scale(this.delta);
        }
       
        private BigDecimal calculateGamma() throws Exception
        {
        	this.gamma = normalDensity(this.d1).divide(this.underlyingPrice.multiply(this.volatility)
        			.multiply(this.timeToExpirySquareRoot), this.scale, RoundingMode.HALF_UP);
        	
        	return this.gamma;
        }
       
        private BigDecimal calculateVega() throws Exception
        {
        	this.vega = (normalDensity(this.d1).multiply(this.underlyingPrice
        			.multiply(this.timeToExpirySquareRoot)).divide(HUNDRED, this.scale, RoundingMode.HALF_UP));
        	
        	return this.vega;
        }
       
        private BigDecimal calculateTheta() throws Exception
        {
            BigDecimal left = negate((this.underlyingPrice
            		.multiply(this.volatility).multiply(normalDensity(this.d1)))
            		.divide(TWO.multiply(this.timeToExpirySquareRoot), this.scale, RoundingMode.HALF_UP));
            
            BigDecimal right = (this.interestRate.multiply(this.strike).multiply(this.expInterestRate)
            		.multiply(cummulativeNormalDensity(this.isCallOption ? this.d2 : negate(this.d2))));
            
            if(this.isCallOption)
            	this.theta = left.subtract(right).divide(HUNDRED, this.scale, RoundingMode.HALF_UP);
            else
            	this.theta = left.add(right).divide(HUNDRED, this.scale, RoundingMode.HALF_UP);
            
            return this.theta;
        }
       
        private BigDecimal calculateRho() throws Exception
        {
            if (this.isCallOption)
            	this.rho = this.timeToExpiryInYears.multiply(this.strike).multiply(this.expInterestRate)
            		.multiply(cummulativeNormalDensity(this.d2)).divide(HUNDRED, this.scale, RoundingMode.HALF_UP);
            else
            	this.rho = negate(this.timeToExpiryInYears).multiply(this.strike).multiply(this.expInterestRate)
                	.multiply(cummulativeNormalDensity(negate(this.d2))).divide(HUNDRED, this.scale, RoundingMode.HALF_UP);
                    
            return this.rho;
        }
        
        private BigDecimal calculateLambda() throws Exception
        {
        	this.lambda = this.underlyingPrice.multiply(this.delta).divide(this.theoreticalValue, this.scale, RoundingMode.HALF_DOWN);
        	return scale(this.lambda);
        }
        
        private BigDecimal calculateIntrinsicValue() throws Exception
        {
        	this.intrinsicValue = this.isCallOption ? this.underlyingPrice.subtract(this.strike) : this.strike.subtract(this.underlyingPrice);
        	return scale(this.intrinsicValue.compareTo(BigDecimal.ZERO) > 0 ? this.intrinsicValue : BigDecimal.ZERO);
        }
        
        private BigDecimal calculateTimeValue() throws Exception
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
        
        private static BigDecimal squareRoot(BigDecimal value)
        {
            BigDecimal x = new BigDecimal(Math.sqrt(value.doubleValue()));
            return x.add(new BigDecimal(value.subtract(x.multiply(x)).doubleValue() / (x.doubleValue() * 2.0)));
        }
        
        private BigDecimal normalDensity(BigDecimal initialValue)
        {
            return this.piCalcVal.multiply(expCalculatedValue(initialValue));
        }
        
        private BigDecimal cummulativeNormalDensity(BigDecimal initialValue)
        {
        	BigDecimal absolute =  initialValue.abs();
        	BigDecimal k = BigDecimal.ONE.divide(BigDecimal.ONE.add(A0.multiply(absolute)), this.scale, RoundingMode.HALF_UP);
        	BigDecimal kPowers = ((A1.multiply(k)).add(A2.multiply(k.pow(2))).add(A3.multiply(k.pow(3)))).add(A4.multiply(k.pow(4)).add(A5.multiply(k.pow(5))));
        	BigDecimal result = BigDecimal.ONE.subtract(normalDensity(initialValue).multiply(kPowers));
        	
        	if(initialValue.compareTo(BigDecimal.ZERO) == -1)
        		return BigDecimal.ONE.subtract(result);
        	
        	return result;
        }
        
		@Override
		public void configure(Map<String, BigDecimal> inputs)
		{
			this.volatility = inputs.get(OptionConstants.VOLATILITY);
			this.strike = inputs.get(OptionConstants.STRIKE);
			this.underlyingPrice = inputs.get(OptionConstants.UNDERLYING_PRICE);
			this.interestRate = inputs.get(OptionConstants.INTEREST_RATE);
			this.timeToExpiryInYears = inputs.get(OptionConstants.TIME_TO_EXPIRY);
			this.isCallOption = inputs.get(OptionConstants.IS_CALL_OPTION).equals(BigDecimal.ONE);
		}

		@Override
		public Map<String, BigDecimal> calculate()
		{
			try
			{
				Map<String, BigDecimal> result = new HashMap<>();
				
				calculateSeedValues();
				
				result.put(OptionConstants.THEORETICAL_VALUE, calculateTheoreticalValue());
				result.put(OptionConstants.INTRINSIC_VALUE, calculateIntrinsicValue());
				result.put(OptionConstants.TIME_VALUE, calculateTimeValue());
				result.put(OptionConstants.DELTA, calculateDelta());
				result.put(OptionConstants.GAMMA, calculateGamma());
				result.put(OptionConstants.VEGA, calculateVega());
				result.put(OptionConstants.THETA, calculateTheta());
				result.put(OptionConstants.RHO, calculateRho());
				result.put(OptionConstants.LAMBDA, calculateLambda());
				
				return result;
			}
			catch(Exception e)
			{
				logger.isErrorEnabled();
					logger.error("Failed to complete Black and Scholes calculation using following inputs: " + this);
				
				return new HashMap<String, BigDecimal>();
			}
		}

		@Override
		public Map<String, BigDecimal> calculate(List<String> listOfRequiredOutputs)
		{
			try
			{
				Map<String, BigDecimal> result = new HashMap<>();
				
				calculateSeedValues();
				
				for(String output : listOfRequiredOutputs)
				{
					if(output.equals(OptionConstants.THEORETICAL_VALUE))
					{
						result.put(OptionConstants.THEORETICAL_VALUE, calculateTheoreticalValue());
						continue;
					}
					
					if(output.equals(OptionConstants.INTRINSIC_VALUE))
					{
						result.put(OptionConstants.INTRINSIC_VALUE, calculateIntrinsicValue());
						continue;
					}
					
					if(output.equals(OptionConstants.TIME_VALUE))
					{
						result.put(OptionConstants.TIME_VALUE, calculateTimeValue());
						continue;
					}
					
					if(output.equals(OptionConstants.DELTA))
					{
						result.put(OptionConstants.DELTA, calculateDelta());
						continue;
					}

					if(output.equals(OptionConstants.GAMMA))
					{
						result.put(OptionConstants.GAMMA, calculateGamma());
						continue;
					}
					
					if(output.equals(OptionConstants.VEGA))
					{
						result.put(OptionConstants.VEGA, calculateVega());
						continue;
					}
					
					if(output.equals(OptionConstants.THETA))
					{
						result.put(OptionConstants.THETA, calculateTheta());
						continue;
					}
					
					if(output.equals(OptionConstants.RHO))
					{
						result.put(OptionConstants.RHO, calculateRho());
						continue;
					}
										
					if(output.equals(OptionConstants.LAMBDA))
					{
						result.put(OptionConstants.LAMBDA, calculateLambda());
						continue;
					}
				}
				
				return result;
			}
			catch(Exception e)
			{
				logger.isErrorEnabled();
					logger.error("Failed to complete Black and Scholes calculation using following inputs: " + this);
				
				return new HashMap<String, BigDecimal>();
			}
		}

		@Override
		public String toString()
		{
			StringBuilder builder = new StringBuilder();
			builder.append("BlackScholesModelImpl [isCallOption=");
			builder.append(this.isCallOption);
			builder.append(", timeToExpiryInYears=");
			builder.append(this.timeToExpiryInYears);
			builder.append(", interestRate=");
			builder.append(this.interestRate);
			builder.append(", underlyingPrice=");
			builder.append(this.underlyingPrice);
			builder.append(", strike=");
			builder.append(this.strike);
			builder.append(", volatility=");
			builder.append(this.volatility);
			builder.append("]");
			return builder.toString();
		}
		
		
}