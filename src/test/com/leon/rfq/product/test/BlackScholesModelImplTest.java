package com.leon.rfq.product.test;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.leon.rfq.common.OptionConstants;
import com.leon.rfq.products.BlackScholesModelImpl;
import com.leon.rfq.products.PricingModel;

@ContextConfiguration(locations = { "classpath: **/applicationContext.xml" })
public class BlackScholesModelImplTest extends AbstractJUnit4SpringContextTests
{
	@Test
	public void calculate_ValidInputs_ValidOutputs() throws Exception
	{
		// Arrange
		Map<String, BigDecimal> inputs = new HashMap<>();
		inputs.put(OptionConstants.UNDERLYING_PRICE, BigDecimal.valueOf(90));
		inputs.put(OptionConstants.STRIKE, BigDecimal.valueOf(100));
		inputs.put(OptionConstants.VOLATILITY, BigDecimal.valueOf(0.2));
		inputs.put(OptionConstants.INTEREST_RATE, BigDecimal.valueOf(0.05));
		inputs.put(OptionConstants.TIME_TO_EXPIRY, BigDecimal.valueOf(1));
		inputs.put(OptionConstants.IS_CALL_OPTION, BigDecimal.valueOf(1));
		inputs.put(OptionConstants.SIDE_MULTIPLIER, BigDecimal.valueOf(1));
		inputs.put(OptionConstants.QTY_MULTIPLIER, BigDecimal.valueOf(1));
		
		Map<String, BigDecimal> expectedOutput = new HashMap<>();
		expectedOutput.put(OptionConstants.DELTA, BigDecimal.valueOf(0.4299));
		expectedOutput.put(OptionConstants.GAMMA, BigDecimal.valueOf(0.0218));
		expectedOutput.put(OptionConstants.VEGA, BigDecimal.valueOf(0.3535));
		expectedOutput.put(OptionConstants.TIME_VALUE, BigDecimal.valueOf(5.1017));
		expectedOutput.put(OptionConstants.THEORETICAL_VALUE, BigDecimal.valueOf(-5.1017));
		expectedOutput.put(OptionConstants.INTRINSIC_VALUE, BigDecimal.valueOf(0).setScale(4));
		expectedOutput.put(OptionConstants.THETA, BigDecimal.valueOf(-0.0521));
		expectedOutput.put(OptionConstants.RHO, BigDecimal.valueOf(0.3359));
		expectedOutput.put(OptionConstants.LAMBDA, BigDecimal.valueOf(7.5839));
		
		PricingModel model = new BlackScholesModelImpl();
		
		//Act
		model.configure(inputs);
		Map<String, BigDecimal> actualOutput = model.calculate();
		
		// Assert
		assertEquals("Delta calculated from model should match expectations", expectedOutput.get(OptionConstants.DELTA), actualOutput.get(OptionConstants.DELTA));
		assertEquals("Gamma calculated from model should match expectations", expectedOutput.get(OptionConstants.GAMMA), actualOutput.get(OptionConstants.GAMMA));
		assertEquals("Vega calculated from model should match expectations", expectedOutput.get(OptionConstants.VEGA), actualOutput.get(OptionConstants.VEGA));
		assertEquals("Theta calculated from model should match expectations", expectedOutput.get(OptionConstants.THETA), actualOutput.get(OptionConstants.THETA));
		assertEquals("Rho calculated from model should match expectations", expectedOutput.get(OptionConstants.RHO), actualOutput.get(OptionConstants.RHO));
		assertEquals("Theoretical value calculated from model should match expectations", expectedOutput.get(OptionConstants.THEORETICAL_VALUE), actualOutput.get(OptionConstants.THEORETICAL_VALUE));
		assertEquals("Time value calculated from model should match expectations", expectedOutput.get(OptionConstants.TIME_VALUE), actualOutput.get(OptionConstants.TIME_VALUE));
		assertEquals("Intrinsic value calculated from model should match expectations", expectedOutput.get(OptionConstants.INTRINSIC_VALUE), actualOutput.get(OptionConstants.INTRINSIC_VALUE));
		assertEquals("Lambda calculated from model should match expectations", expectedOutput.get(OptionConstants.LAMBDA), actualOutput.get(OptionConstants.LAMBDA));
		assertEquals("Output calculated from model should match expectations", expectedOutput, actualOutput);
	}
	
	@Test
	public void calculate_InvalidInputs_InvalidOutputs() throws Exception
	{
		// Arrange
		Map<String, BigDecimal> inputs = new HashMap<>();
		inputs.put(OptionConstants.UNDERLYING_PRICE, BigDecimal.valueOf(0));
		inputs.put(OptionConstants.STRIKE, BigDecimal.valueOf(0));
		inputs.put(OptionConstants.VOLATILITY, BigDecimal.valueOf(0));
		inputs.put(OptionConstants.INTEREST_RATE, BigDecimal.valueOf(0));
		inputs.put(OptionConstants.TIME_TO_EXPIRY, BigDecimal.valueOf(0));
		inputs.put(OptionConstants.IS_CALL_OPTION, BigDecimal.valueOf(1));
		inputs.put(OptionConstants.SIDE_MULTIPLIER, BigDecimal.valueOf(-1));
		inputs.put(OptionConstants.QTY_MULTIPLIER, BigDecimal.valueOf(1));
		
		
		Map<String, BigDecimal> outputs = new HashMap<>();
		PricingModel model = new BlackScholesModelImpl();
		// Act and Assert
		model.configure(inputs);
		assertEquals("Output should match expectations", outputs, model.calculate());
	}
}
