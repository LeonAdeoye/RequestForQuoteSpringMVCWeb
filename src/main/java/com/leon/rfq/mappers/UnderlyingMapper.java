package com.leon.rfq.mappers;

import java.util.Set;

import com.leon.rfq.domains.UnderlyingDetailImpl;

public interface UnderlyingMapper
{
	Set<UnderlyingDetailImpl> getAll();
	
	UnderlyingDetailImpl get(String ric);
	
	int delete(String ric);
	
	int insert(UnderlyingDetailImpl underlying);
	
	int update(UnderlyingDetailImpl underlying);

	int updateValidity(UnderlyingDetailImpl underlying);
	
	UnderlyingDetailImpl underlyingExistsWithRic(String ric);
}
