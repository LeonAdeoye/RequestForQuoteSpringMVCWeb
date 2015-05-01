package com.leon.rfq.underlying;

import java.util.List;

public interface UnderlyingDao
{
	UnderlyingDetailImpl insert(String ric, String description, boolean isValid, String savedByUser);
	
	UnderlyingDetailImpl update(String ric, String description, boolean isValid, String updatedByUser);
	
	boolean delete(String ric);

	List<UnderlyingDetailImpl> getAll();

	UnderlyingDetailImpl get(String ric);
	
	boolean underlyingExistsWithRic(String ric);
}
