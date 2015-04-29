package com.leon.rfq.underlying;

import java.util.List;

public interface UnderlyingManagerDao
{
	UnderlyingDetailImpl insert(String ric, String description, boolean isValid, String savedBy);
	
	UnderlyingDetailImpl update(String ric, String description, boolean isValid, String savedBy);

	boolean updateValidity(String ric, boolean isValid, String updatedBy);

	List<UnderlyingDetailImpl> getAll();

	UnderlyingDetailImpl get(String ric);
}
