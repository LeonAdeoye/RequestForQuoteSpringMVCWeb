package com.leon.rfq.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import com.leon.rfq.common.Tag;
import com.leon.rfq.domains.UnderlyingDetailImpl;
import com.leon.rfq.repositories.UnderlyingDao;

public interface UnderlyingService
{
	void setUnderlyingDao(UnderlyingDao underlyingDao);
	
	Set<UnderlyingDetailImpl> getAll();
	
	UnderlyingDetailImpl get(String ric);
	
	boolean delete(String ric);
	
	boolean underlyingExistsWithRic(String ric);
	
	boolean isUnderlyingCached(String ric);

	void initialise();

	Set<UnderlyingDetailImpl> getAllFromCacheOnly();

	boolean insert(String ric, String description, BigDecimal referencePrice,
			BigDecimal simulationPriceVariance, BigDecimal spread, boolean isValid, String savedByUser);
	
	boolean update(String RIC, String description, BigDecimal referencePrice,
			BigDecimal simulationPriceVariance, BigDecimal spread, boolean isValid, String updatedByUser);

	List<Tag> getMatchingUnderlyingTags(String upperCase);
}
