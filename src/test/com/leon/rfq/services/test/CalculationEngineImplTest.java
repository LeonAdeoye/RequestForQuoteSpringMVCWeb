package com.leon.rfq.services.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.leon.rfq.common.OptionConstants;
import com.leon.rfq.domains.RequestDetailImpl;
import com.leon.rfq.products.BlackScholesModelImpl;
import com.leon.rfq.products.OptionRequestFactory;
import com.leon.rfq.products.PricingModel;
import com.leon.rfq.products.RangeParameters;
import com.leon.rfq.services.CalculationService;
import com.leon.rfq.services.PriceService;
import com.leon.rfq.services.PriceServiceImpl;

@ContextConfiguration(locations = { "classpath: **/applicationContext.xml" })
public class CalculationEngineImplTest extends AbstractJUnit4SpringContextTests
{
	@Autowired(required=true)
	private CalculationService calculationService;
	
	@Autowired(required=true)
	private OptionRequestFactory optionRequestFactory;
	
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

	@Test
	public void calculateProfitAndLossPoints_ValidInputs_CorrectResultReturned() throws Exception
	{
		PriceService priceServiceMock = mock(PriceServiceImpl.class);
		this.optionRequestFactory.setPriceService(priceServiceMock);
		when(priceServiceMock.getLastPrice("0001.HK")).thenReturn(BigDecimal.valueOf(90));
		
		RequestDetailImpl request = this.optionRequestFactory.getNewInstance("C+P 100,110 20Jan2020 0001.HK",
				Integer.MAX_VALUE, "testBook", "testUser");
		
		PricingModel model = new BlackScholesModelImpl();
		
		this.calculationService.calculate(model, request);
		
		List<BigDecimal> result = this.calculationService.calculateProfitAndLossPoints(model, request);
		
		assertEquals("Should return two points", 2, result.size());
	}
}
