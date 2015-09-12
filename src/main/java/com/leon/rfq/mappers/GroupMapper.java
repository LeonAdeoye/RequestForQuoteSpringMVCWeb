package com.leon.rfq.mappers;

import java.util.Set;

import com.leon.rfq.domains.GroupDetailImpl;

public interface GroupMapper
{
	Set<GroupDetailImpl> getAll();
	
	GroupDetailImpl get(String name);
	
	int insert(GroupDetailImpl groupDetailImpl);
	
	int update(GroupDetailImpl groupDetailImpl);
	
	GroupDetailImpl groupExistsWithName(String name);
}
