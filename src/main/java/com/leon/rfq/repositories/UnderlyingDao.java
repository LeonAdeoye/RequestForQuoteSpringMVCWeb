package com.leon.rfq.repositories;

import java.math.BigDecimal;
import java.util.Set;

import com.leon.rfq.domains.UnderlyingDetailImpl;

public interface UnderlyingDao
{
	boolean delete(String ric);

	Set<UnderlyingDetailImpl> getAll();

	UnderlyingDetailImpl get(String ric);
	
	boolean underlyingExistsWithRic(String ric);

	UnderlyingDetailImpl insert(String ric, String description,	BigDecimal referencePrice,
			BigDecimal simulationPriceVariance, BigDecimal spread, BigDecimal dividendYield, boolean isValid, String savedByUser);
	
	UnderlyingDetailImpl update(String ric, String description,	BigDecimal referencePrice,
			BigDecimal simulationPriceVariance, BigDecimal spread, BigDecimal dividendYield, boolean isValid, String updatedByUser);
	
}


