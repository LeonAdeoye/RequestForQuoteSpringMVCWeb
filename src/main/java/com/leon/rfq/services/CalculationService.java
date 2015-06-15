package com.leon.rfq.services;

import com.leon.rfq.domains.RequestDetailImpl;
import com.leon.rfq.products.PricingModel;

public interface CalculationService
{
	void calculate(RequestDetailImpl request, PricingModel model);
}
