package com.leon.rfq.repositories;

import java.util.Set;

import com.leon.rfq.domains.UnderlyingDetailImpl;

public interface UnderlyingDao
{
	UnderlyingDetailImpl insert(String ric, String description, boolean isValid, String savedByUser);
	
	UnderlyingDetailImpl update(String ric, String description, boolean isValid, String updatedByUser);
	
	boolean delete(String ric);

	Set<UnderlyingDetailImpl> getAll();

	UnderlyingDetailImpl get(String ric);
	
	boolean underlyingExistsWithRic(String ric);
}
