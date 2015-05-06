package com.leon.rfq.services;

import java.util.List;

import com.leon.rfq.dao.UnderlyingDao;
import com.leon.rfq.domains.UnderlyingDetailImpl;

public interface UnderlyingService
{
	void setUnderlyingDao(UnderlyingDao underlyingDao);

	boolean insert(String RIC, String description, boolean isValid,	String savedBy);
	boolean update(String RIC, String description, boolean isValid,	String savedBy);

	List<UnderlyingDetailImpl> getAll();
	
	UnderlyingDetailImpl get(String ric);
	
	boolean delete(String ric);
	
	boolean underlyingExistsWithRic(String ric);
	
	boolean isUnderlyingCached(String ric);
}
