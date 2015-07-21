package com.leon.rfq.products;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.leon.rfq.common.CalculationConstants;
import com.leon.rfq.common.OptionConstants;
 
public class BlackScholesModelImpl implements PricingModel
{
	private static final Logger logger = LoggerFactory.getLogger(BlackScholesModelImpl.class);
	
        private boolean isCallOption = true;
        
        private static BigDecimal A0 = new BigDecimal("0.2316419");
        private static BigDecimal A1 = new BigDecimal("0.31938153");
        private static BigDecimal A2 = new BigDecimal("-0.356563782");
        private static BigDecimal A3 = new BigDecimal("1.781477937");
        private static BigDecimal A4 = new BigDecimal("-1.821255978");
        private static BigDecimal A5 = new BigDecimal("1.330274429");
        
        private static BigDecimal piCalcVal = BigDecimal.ONE.divide(squareRoot(CalculationConstants.TWO
        		.multiply(BigDecimal.valueOf(Math.PI))), CalculationConstants.SCALE_OF_FOUR, RoundingMode.HALF_UP);
                
        private BigDecimal timeToExpirySquareRoot;
        private BigDecimal timeToExpiryInYears;
        private BigDecimal expInterestRate;
        private BigDecimal interestRate;
        private BigDecimal underlyingPrice;
        private BigDecimal strike;
        private BigDecimal volatility;
        private BigDecimal d1;
        private BigDecimal d2;
        private BigDecimal sideMultiplier;
        private BigDecimal qtyMultiplier;
        
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
        	piCalcVal = BigDecimal.ONE.divide(squareRoot(CalculationConstants.TWO.multiply(BigDecimal.valueOf(Math.PI))),
        		CalculationConstants.SCALE_OF_FOUR, RoundingMode.HALF_UP);
        	
        	this.timeToExpirySquareRoot = scale(squareRoot(this.timeToExpiryInYears));
        	
        	this.expInterestRate = scale(BigDecimal.valueOf(Math.exp((negate(this.interestRate)
        		.multiply(this.timeToExpiryInYears)).doubleValue())));
        	
        	BigDecimal logCalc = BigDecimal.valueOf(Math.log(this.underlyingPrice.divide(this.strike,
        		CalculationConstants.SCALE_OF_FOUR, RoundingMode.HALF_UP).doubleValue()));
        	
        	BigDecimal volSq = this.volatility.multiply(this.timeToExpirySquareRoot);
        	
        	BigDecimal volPow = this.volatility.pow(2).divide(CalculationConstants.TWO,
        		CalculationConstants.SCALE_OF_FOUR, RoundingMode.HALF_UP);
        	
        	this.d1 = (logCalc.add(this.interestRate.add(volPow.multiply(this.timeToExpiryInYears))))
        		.divide(volSq, CalculationConstants.SCALE_OF_FOUR, RoundingMode.HALF_UP);
        	
        	this.d2 = this.d1.subtract(volSq);
        }
                       
        private BigDecimal calculateTheoreticalValue() throws Exception
        {
        	this.theoreticalValue = BigDecimal.ZERO;
        	
            if (this.isCallOption)
            	this.theoreticalValue = scale(this.underlyingPrice.multiply(cummulativeNormalDensity(this.d1))
            		.subtract(this.strike.multiply(this.expInterestRate.multiply(cummulativeNormalDensity(this.d2)))));
            else
            	this.theoreticalValue = scale(this.strike.multiply(this.expInterestRate)
            		.multiply(cummulativeNormalDensity(negate(this.d2)))
            		.subtract(this.underlyingPrice.multiply(cummulativeNormalDensity(negate(this.d1)))));
            
        	this.theoreticalValue =  negate(this.theoreticalValue.multiply(this.sideMultiplier).multiply(this.qtyMultiplier));
        	
        	return this.theoreticalValue;

        }
       
        private BigDecimal calculateDelta() throws Exception
        {
        	this.delta = BigDecimal.ZERO;
        	
        	this.delta = scale(this.isCallOption ? cummulativeNormalDensity(this.d1) : negate(cummulativeNormalDensity(negate(this.d1))));
        	
        	this.delta = this.delta.multiply(this.sideMultiplier).multiply(this.qtyMultiplier);
        	
        	return this.delta;
        }
       
        private BigDecimal calculateGamma() throws Exception
        {
        	this.gamma = BigDecimal.ZERO;
        	
        	this.gamma = normalDensity(this.d1).divide(this.underlyingPrice.multiply(this.volatility)
        			.multiply(this.timeToExpirySquareRoot), CalculationConstants.SCALE_OF_FOUR, RoundingMode.HALF_UP);
        	
        	this.gamma = this.gamma.multiply(this.sideMultiplier).multiply(this.qtyMultiplier);
        	
        	return this.gamma;
        }
       
        private BigDecimal calculateVega() throws Exception
        {
        	this.vega = BigDecimal.ZERO;
        	
        	this.vega = (normalDensity(this.d1).multiply(this.underlyingPrice
        			.multiply(this.timeToExpirySquareRoot)).divide(CalculationConstants.ONE_HUNDRED,
        			CalculationConstants.SCALE_OF_FOUR, RoundingMode.HALF_UP));
        	
        	this.vega = this.vega.multiply(this.sideMultiplier).multiply(this.qtyMultiplier);
        	
        	return this.vega;
        }
       
        private BigDecimal calculateTheta() throws Exception
        {
        	this.theta = BigDecimal.ZERO;
        	
            BigDecimal left = negate((this.underlyingPrice.multiply(this.volatility).multiply(normalDensity(this.d1)))
            	.divide(CalculationConstants.TWO.multiply(this.timeToExpirySquareRoot),
            	CalculationConstants.SCALE_OF_FOUR, RoundingMode.HALF_UP));
            
            BigDecimal right = (this.interestRate.multiply(this.strike).multiply(this.expInterestRate)
            	.multiply(cummulativeNormalDensity(this.isCallOption ? this.d2 : negate(this.d2))));
            
            if(this.isCallOption)
            	this.theta = left.subtract(right).divide(CalculationConstants.ONE_HUNDRED,
            		CalculationConstants.SCALE_OF_FOUR, RoundingMode.HALF_UP);
            else
            	this.theta = left.add(right).divide(CalculationConstants.ONE_HUNDRED,
            		CalculationConstants.SCALE_OF_FOUR, RoundingMode.HALF_UP);
            
            this.theta = this.theta.multiply(this.sideMultiplier).multiply(this.qtyMultiplier);
            
            return this.theta;
        }
       
        private BigDecimal calculateRho() throws Exception
        {
        	this.rho = BigDecimal.ZERO;
        	
            if (this.isCallOption)
            	this.rho = this.timeToExpiryInYears.multiply(this.strike).multiply(this.expInterestRate)
            		.multiply(cummulativeNormalDensity(this.d2))
            		.divide(CalculationConstants.ONE_HUNDRED, CalculationConstants.SCALE_OF_FOUR, RoundingMode.HALF_UP);
            else
            	this.rho = negate(this.timeToExpiryInYears).multiply(this.strike).multiply(this.expInterestRate)
                	.multiply(cummulativeNormalDensity(negate(this.d2)))
                	.divide(CalculationConstants.ONE_HUNDRED, CalculationConstants.SCALE_OF_FOUR, RoundingMode.HALF_UP);
                    
            this.rho = this.rho.multiply(this.sideMultiplier).multiply(this.qtyMultiplier);
            
            return this.rho;
        }
        
        private BigDecimal calculateLambda() throws Exception
        {
        	this.lambda = BigDecimal.ZERO;
        	
        	this.lambda = this.underlyingPrice.multiply(this.delta).divide(negate(this.theoreticalValue),
        		CalculationConstants.SCALE_OF_FOUR, RoundingMode.HALF_DOWN);
        	
        	return this.lambda;
        }
        
        private BigDecimal calculateIntrinsicValue()
        {
        	this.intrinsicValue = scale(calculateIntrinsicValue(this.isCallOption, this.underlyingPrice,
        			this.strike, this.qtyMultiplier));
        	
        	return this.intrinsicValue;
        }
        
        public static BigDecimal calculateIntrinsicValue(boolean isCallOption, BigDecimal underlyingPrice,
        		BigDecimal strike, BigDecimal qtyMultiplier)
        {
        	BigDecimal intrinsicValue = BigDecimal.ZERO;
        	
        	intrinsicValue = isCallOption ? underlyingPrice.subtract(strike).multiply(qtyMultiplier)
        			: strike.subtract(underlyingPrice).multiply(qtyMultiplier);

        	intrinsicValue = intrinsicValue.compareTo(BigDecimal.ZERO) > 0 ? intrinsicValue : BigDecimal.ZERO;
        	
        	return intrinsicValue;
        }
        
        private BigDecimal calculateTimeValue() throws Exception
        {
        	this.timeValue = BigDecimal.ZERO;
        	
        	this.timeValue = negate(calculateTheoreticalValue().subtract(calculateIntrinsicValue()));
        	this.timeValue = scale(this.timeValue.compareTo(BigDecimal.ZERO) > 0 ? this.timeValue : BigDecimal.ZERO);
        	        	
        	return this.timeValue;
        }
        
        private static BigDecimal expCalculatedValue(BigDecimal initialValue)
        {
        	BigDecimal absolute =  initialValue.abs();
        	return BigDecimal.valueOf(Math.exp(((negate(absolute)).multiply(absolute))
        		.divide(CalculationConstants.TWO, CalculationConstants.SCALE_OF_FOUR, RoundingMode.HALF_UP).doubleValue()));
        }
                
        private static BigDecimal scale(BigDecimal initialValue)
        {
        	return initialValue.setScale(CalculationConstants.SCALE_OF_FOUR, RoundingMode.HALF_UP);
        }
        
        private static BigDecimal negate(BigDecimal initialValue)
        {
        	return initialValue.multiply(CalculationConstants.NEGATIVE_ONE);
        }
        
        private static BigDecimal squareRoot(BigDecimal value)
        {
            BigDecimal x = new BigDecimal(Math.sqrt(value.doubleValue()));
            return x.add(new BigDecimal(value.subtract(x.multiply(x)).doubleValue() / (x.doubleValue() * 2.0)));
        }
        
        private static BigDecimal normalDensity(BigDecimal initialValue)
        {
            return piCalcVal.multiply(expCalculatedValue(initialValue));
        }
        
        private static BigDecimal cummulativeNormalDensity(BigDecimal initialValue)
        {
        	BigDecimal absolute =  initialValue.abs();
        	
        	BigDecimal k = BigDecimal.ONE.divide(BigDecimal.ONE.add(A0.multiply(absolute)),
        		CalculationConstants.SCALE_OF_FOUR, RoundingMode.HALF_UP);
        	
        	BigDecimal kPowers = ((A1.multiply(k)).add(A2.multiply(k.pow(2))).add(A3.multiply(k.pow(3))))
        		.add(A4.multiply(k.pow(4)).add(A5.multiply(k.pow(5))));
        	
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
			this.sideMultiplier = inputs.get(OptionConstants.SIDE_MULTIPLIER);
			this.qtyMultiplier = inputs.get(OptionConstants.QTY_MULTIPLIER);
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
		public BigDecimal calculate(String requiredOutput)
		{
			try
			{
				calculateSeedValues();
				
				switch(requiredOutput)
				{
					case OptionConstants.THEORETICAL_VALUE:
						return calculateTheoreticalValue();
					case OptionConstants.INTRINSIC_VALUE:
						return calculateIntrinsicValue();
					case OptionConstants.TIME_VALUE:
						return calculateTimeValue();
					case OptionConstants.DELTA:
						return calculateDelta();
					case OptionConstants.GAMMA:
						return calculateGamma();
					case OptionConstants.VEGA:
						return calculateVega();
					case OptionConstants.THETA:
						return calculateTheta();
					case OptionConstants.RHO:
						return calculateRho();
					case OptionConstants.LAMBDA:
						return calculateLambda();
				}
			}
			catch(Exception e)
			{
				logger.isErrorEnabled();
					logger.error("Failed to complete Black and Scholes calculation using following inputs: " + this);
			}
			
			return BigDecimal.ZERO;
		}
		

		@Override
		public Map<String, BigDecimal> calculate(List<String> listOfRequiredOutputs)
		{
			try
			{
				Map<String, BigDecimal> result = new HashMap<>();
				
				calculateSeedValues();
				
				for(String requiredOutput : listOfRequiredOutputs)
				{
					switch(requiredOutput)
					{
						case OptionConstants.THEORETICAL_VALUE:
							result.put(OptionConstants.THEORETICAL_VALUE, calculateTheoreticalValue());
							continue;
						case OptionConstants.INTRINSIC_VALUE:
							result.put(OptionConstants.INTRINSIC_VALUE, calculateIntrinsicValue());
							continue;
						case OptionConstants.TIME_VALUE:
							result.put(OptionConstants.TIME_VALUE, calculateTimeValue());
							continue;
						case OptionConstants.DELTA:
							result.put(OptionConstants.DELTA, calculateDelta());
							continue;
						case OptionConstants.GAMMA:
							result.put(OptionConstants.GAMMA, calculateGamma());
							continue;
						case OptionConstants.VEGA:
							result.put(OptionConstants.VEGA, calculateVega());
							continue;
						case OptionConstants.THETA:
							result.put(OptionConstants.THETA, calculateTheta());
							continue;
						case OptionConstants.RHO:
							result.put(OptionConstants.RHO, calculateRho());
							continue;
						case OptionConstants.LAMBDA:
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
			builder.append(", piCalcVal=");
			builder.append(piCalcVal);
			builder.append(", timeToExpirySquareRoot=");
			builder.append(this.timeToExpirySquareRoot);
			builder.append(", timeToExpiryInYears=");
			builder.append(this.timeToExpiryInYears);
			builder.append(", expInterestRate=");
			builder.append(this.expInterestRate);
			builder.append(", interestRate=");
			builder.append(this.interestRate);
			builder.append(", underlyingPrice=");
			builder.append(this.underlyingPrice);
			builder.append(", strike=");
			builder.append(this.strike);
			builder.append(", volatility=");
			builder.append(this.volatility);
			builder.append(", d1=");
			builder.append(this.d1);
			builder.append(", d2=");
			builder.append(this.d2);
			builder.append(", theoreticalValue=");
			builder.append(this.theoreticalValue);
			builder.append(", intrinsicValue=");
			builder.append(this.intrinsicValue);
			builder.append(", timeValue=");
			builder.append(this.timeValue);
			builder.append(", delta=");
			builder.append(this.delta);
			builder.append(", gamma=");
			builder.append(this.gamma);
			builder.append(", vega=");
			builder.append(this.vega);
			builder.append(", theta=");
			builder.append(this.theta);
			builder.append(", rho=");
			builder.append(this.rho);
			builder.append(", lambda=");
			builder.append(this.lambda);
			builder.append("]");
			return builder.toString();
		}
				
}