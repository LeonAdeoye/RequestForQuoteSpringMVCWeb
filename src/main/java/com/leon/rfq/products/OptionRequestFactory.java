package com.leon.rfq.products;

import com.leon.rfq.domains.RequestDetailImpl;

public interface OptionRequestFactory
{
	RequestDetailImpl getNewInstance(String snippet, int clientId,
			String bookCode, String savedByUser);

	boolean parseRequest(String snippet, RequestDetailImpl parent);
	
	boolean doesUnderlyingExist(String snippet);
}