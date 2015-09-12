package com.leon.rfq.repositories;

import java.util.Set;

import com.leon.rfq.domains.GroupDetailImpl;

public interface GroupDao
{
	boolean insert(String name,
			String description,
			boolean isValid,
			String savedByUser);
	
	boolean update(String name,
			String description,
			boolean isValid,
			String updatedByUser);

	Set<GroupDetailImpl> getAll();
	 
	GroupDetailImpl get(String name);

	boolean groupExistsWithName(String name);
}
