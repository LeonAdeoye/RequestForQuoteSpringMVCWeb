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
		Map<String, BigDecimal> inputs = new HashMap<>();
		inputs.put(OptionConstants.UNDERLYING_PRICE, BigDecimal.valueOf(90));
		inputs.put(OptionConstants.STRIKE, BigDecimal.valueOf(100));
		inputs.put(OptionConstants.VOLATILITY, BigDecimal.valueOf(0.2));
		inputs.put(OptionConstants.INTEREST_RATE, BigDecimal.valueOf(0.05));
		inputs.put(OptionConstants.TIME_TO_EXPIRY, BigDecimal.valueOf(1));
		inputs.put(OptionConstants.IS_CALL_OPTION, BigDecimal.valueOf(1));
		
		Map<String, BigDecimal> outputs = new HashMap<>();
		outputs.put(OptionConstants.DELTA, BigDecimal.valueOf(0.4299));
		outputs.put(OptionConstants.GAMMA, BigDecimal.valueOf(0.0218));
		outputs.put(OptionConstants.VEGA, BigDecimal.valueOf(0.3535));
		outputs.put(OptionConstants.TIME_VALUE, BigDecimal.valueOf(5.1007));
		outputs.put(OptionConstants.THEORETICAL_VALUE, BigDecimal.valueOf(5.1007));
		outputs.put(OptionConstants.INTRINSIC_VALUE, BigDecimal.valueOf(0.0000).setScale(4));
		outputs.put(OptionConstants.THETA, BigDecimal.valueOf(-0.0521));
		outputs.put(OptionConstants.RHO, BigDecimal.valueOf(0.3359));
		outputs.put(OptionConstants.LAMBDA, BigDecimal.valueOf(7.5848));
				
		PricingModel model = new BlackScholesModelImpl();
		model.configure(inputs);
		
		assertEquals("Output should match expectations", outputs, model.calculate());
	}
	
	@Test
	public void calculate_InvalidInputs_InvalidOutputs() throws Exception
	{
		Map<String, BigDecimal> inputs = new HashMap<>();
		inputs.put(OptionConstants.UNDERLYING_PRICE, BigDecimal.valueOf(0));
		inputs.put(OptionConstants.STRIKE, BigDecimal.valueOf(0));
		inputs.put(OptionConstants.VOLATILITY, BigDecimal.valueOf(0));
		inputs.put(OptionConstants.INTEREST_RATE, BigDecimal.valueOf(0));
		inputs.put(OptionConstants.TIME_TO_EXPIRY, BigDecimal.valueOf(0));
		inputs.put(OptionConstants.IS_CALL_OPTION, BigDecimal.valueOf(1));
		
		Map<String, BigDecimal> outputs = new HashMap<>();
		PricingModel model = new BlackScholesModelImpl();
		model.configure(inputs);
		
		assertEquals("Output should match expectations", outputs, model.calculate());
	}
}
