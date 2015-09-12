package com.leon.rfq.services;

import java.util.List;
import java.util.Set;

import com.leon.rfq.common.Tag;
import com.leon.rfq.domains.GroupDetailImpl;
import com.leon.rfq.repositories.GroupDao;

public interface GroupService
{
	GroupDetailImpl get(String name);

	Set<GroupDetailImpl> getAll();
	
	Set<GroupDetailImpl> getAllFromCacheOnly();
	
	boolean insert(String name, String description, boolean isValid, String savedByUser);
	
	boolean update(String name, String description, boolean isValid, String updatedByUser);

	void setGroupDao(GroupDao groupDao);
	
	boolean groupExistsWithName(String name);

	void initialise();

	List<Tag> getMatchingGroupTags(String groupPattern);
}
