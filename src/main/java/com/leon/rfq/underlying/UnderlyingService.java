package com.leon.rfq.underlying;

import java.util.List;

public interface UnderlyingService
{
	void setUnderlyingManagerDao(UnderlyingManagerDao dao);

	boolean insert(String RIC, String description, boolean isValid,	String savedBy);
	boolean update(String RIC, String description, boolean isValid,	String savedBy);

	boolean updateValidity(String RIC, boolean isValid,	String updatedBy);

	List<UnderlyingDetailImpl> getAll();
}
