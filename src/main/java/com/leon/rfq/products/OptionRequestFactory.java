package com.leon.rfq.products;

import com.leon.rfq.domains.RequestDetailImpl;
import com.leon.rfq.services.PriceService;

public interface OptionRequestFactory
{
	RequestDetailImpl getNewInstance(String snippet, int clientId,
			String bookCode, String savedByUser);
	
	boolean doesUnderlyingExist(String snippet);

	boolean parseRequest(RequestDetailImpl parent);

	void setPriceService(PriceService priceService);
}