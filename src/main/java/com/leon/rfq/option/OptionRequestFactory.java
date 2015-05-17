package com.leon.rfq.option;

import com.leon.rfq.domains.RequestDetailImpl;

public interface OptionRequestFactory
{
	RequestDetailImpl getNewInstance(String requestSnippet, int clientId, String bookCode, String savedByUser);
	
	boolean isValidOptionRequestSnippet(String snippet);
}