package com.leon.rfq.services.test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.leon.rfq.common.OptionConstants;
import com.leon.rfq.products.BlackScholesModelImpl;
import com.leon.rfq.products.PricingModel;
import com.leon.rfq.products.RangeParameters;
import com.leon.rfq.services.CalculationService;

@ContextConfiguration(locations = { "classpath: **/applicationContext.xml" })
public class CalculationEngineImplTest extends AbstractJUnit4SpringContextTests
{
	@Autowired(required=true)
	CalculationService calculationService;
	@Test
	public void calculate_ValidInputs_CorrectModelMethodsCalled() throws Exception
	{
		Map<String, BigDecimal> inputs = new HashMap<>();
		// Arrange
		PricingModel modelMock = mock(BlackScholesModelImpl.class);
		// Acts
		this.calculationService.calculate(modelMock, inputs);
		// Assert
		verify(modelMock).configure(inputs);
		verify(modelMock).calculate();
	}
	
	@Test
	public void calculateRange_ValidInputs_CorrectModelMethodsCalled() throws Exception
	{
		Map<String, BigDecimal> inputs = new HashMap<>();
		RangeParameters params = new RangeParameters(BigDecimal.valueOf(0.1), BigDecimal.valueOf(0.2),
				BigDecimal.valueOf(0.01), OptionConstants.UNDERLYING_PRICE, OptionConstants.DELTA);
		// Arrange
		PricingModel modelMock = mock(BlackScholesModelImpl.class);
		// Acts
		this.calculationService.calculateRange(modelMock, inputs, params);
		// Assert
		verify(modelMock, times(11)).configure(inputs);
		verify(modelMock, times(11)).calculate(params.getListOfRequiredOutput());
	}
}