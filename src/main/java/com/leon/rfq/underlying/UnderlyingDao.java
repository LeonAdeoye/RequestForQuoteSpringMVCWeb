package com.leon.rfq.underlying;

import java.util.List;

public interface UnderlyingDao
{
	UnderlyingDetailImpl insert(String ric, String description, boolean isValid, String savedBy);
	
	UnderlyingDetailImpl update(String ric, String description, boolean isValid, String savedBy);

	boolean updateValidity(String ric, boolean isValid, String updatedBy);
	
	boolean delete(String ric);

	List<UnderlyingDetailImpl> getAll();

	UnderlyingDetailImpl get(String ric);
}
