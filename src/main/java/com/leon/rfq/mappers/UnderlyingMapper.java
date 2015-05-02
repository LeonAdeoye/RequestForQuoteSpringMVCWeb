package com.leon.rfq.mappers;

import java.util.List;

import com.leon.rfq.underlying.UnderlyingDetailImpl;

public interface UnderlyingMapper
{
	List<UnderlyingDetailImpl> getAll();
	
	UnderlyingDetailImpl get(String ric);
	
	int delete(String ric);
	
	int insert(UnderlyingDetailImpl underlying);
	
	int update(UnderlyingDetailImpl underlying);

	int updateValidity(UnderlyingDetailImpl underlying);
	
	UnderlyingDetailImpl underlyingExistsWithRic(String ric);
}
