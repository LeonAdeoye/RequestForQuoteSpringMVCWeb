package com.leon.rfq.mappers;

import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.leon.rfq.common.EnumTypes.StatusEnum;
import com.leon.rfq.domains.RequestDetailImpl;
import com.leon.rfq.domains.SearchCriterionImpl;

public interface RequestMapper
{
	Set<RequestDetailImpl> getAll();
	
	RequestDetailImpl get(int requestId);
	
	int delete(int requestId);
	
	int insert(RequestDetailImpl newRequest);
	
	int update(RequestDetailImpl newRequest);
	
	int updateStatus(RequestDetailImpl newRequest);
	
	RequestDetailImpl requestExistsWithRequestId(int requestId);
	
	int test(@Param("status") StatusEnum status);

	Set<RequestDetailImpl> search(@Param("criteria") Set<SearchCriterionImpl> criteria);
}
