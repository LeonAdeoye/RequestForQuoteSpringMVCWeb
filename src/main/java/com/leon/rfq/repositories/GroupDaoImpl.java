package com.leon.rfq.repositories;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.leon.rfq.domains.GroupDetailImpl;
import com.leon.rfq.mappers.GroupMapper;

@Repository
public class GroupDaoImpl implements GroupDao
{
	private static final Logger logger = LoggerFactory.getLogger(GroupDaoImpl.class);
	
	@Autowired
	private GroupMapper groupMapper;


	@Override
	public boolean insert(String name, String description, boolean isValid,
			String savedByUser)
	{
		if(logger.isDebugEnabled())
			logger.debug("Insert the group with name " + name);
		
		try
		{
			return this.groupMapper.insert(new GroupDetailImpl(name, description, isValid, savedByUser)) == 1;
		}
		catch(Exception e)
		{
			if(logger.isErrorEnabled())
				logger.error("Failed to insert the group with name " + name + " because of exception: " + e);
			
			return false;
		}
	}
	
	@Override
	public boolean update(String name, String description, boolean isValid, String updatedByUser)
	{
		if(logger.isDebugEnabled())
			logger.debug("Update the group with name " + name);
		
		try
		{
			return this.groupMapper.update(new GroupDetailImpl(name, description, isValid, updatedByUser)) == 1;
		}
		catch(Exception e)
		{
			if(logger.isErrorEnabled())
				logger.error("Failed to update the group with name " + name + " because of exception: " + e);
			
			return false;
		}
	}

	@Override
	public Set<GroupDetailImpl> getAll()
	{
		if(logger.isDebugEnabled())
			logger.debug("Request to get all groups");
		
		logger.debug(String.format("Size of groups to be returned: %d", this.groupMapper.getAll().size()));

		return this.groupMapper.getAll();
	}

	@Override
	public GroupDetailImpl get(String name)
	{
		if(logger.isDebugEnabled())
			logger.debug("Request to get group with name: " + name);
		
		return this.groupMapper.get(name);
	}
	
	@Override
	public boolean groupExistsWithName(String name)
	{
		if(logger.isDebugEnabled())
			logger.debug("Request to check if group exists with name: " + name);
		
		return this.groupMapper.groupExistsWithName(name) != null;
	}
}

