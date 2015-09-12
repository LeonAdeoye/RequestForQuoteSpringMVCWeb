package com.leon.rfq.services;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.leon.rfq.common.Tag;
import com.leon.rfq.domains.GroupDetailImpl;
import com.leon.rfq.repositories.GroupDao;

@Service
public final class GroupServiceImpl implements GroupService
{
	private static final Logger logger = LoggerFactory.getLogger(GroupServiceImpl.class);
	private ApplicationEventPublisher applicationEventPublisher;
	private final Map<String, GroupDetailImpl> groups = new ConcurrentSkipListMap<>();
	
	@Autowired(required=true)
	private GroupDao groupDao;
	
	// For unit testing mocking framework.
	@Override
	public void setGroupDao(GroupDao groupDao)
	{
		this.groupDao = groupDao;
	}
	
	@Override
	@PostConstruct
	public void initialise()
	{
		if(logger.isDebugEnabled())
			logger.debug("Initializing group service by getting all existing groups...");
		
		this.getAll();
	}
	
	public GroupServiceImpl() {}
	
	private boolean isGroupCached(String name)
	{
		return this.groups.containsKey(name);
	}
	
	@Override
	public GroupDetailImpl get(String name)
	{
		if((name == null) || name.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("name argument is invalid");
			
			throw new IllegalArgumentException("name argument is invalid");
		}
		
		if(logger.isDebugEnabled())
			logger.debug("Getting the group with name" + name);
		
		ReentrantLock lock = new ReentrantLock();
		
		try
		{
			lock.lock();
									
			GroupDetailImpl group;
			
			if(isGroupCached(name))
				group = this.groups.get(name);
			else
			{
				group = this.groupDao.get(name);
				if(group != null)
					this.groups.putIfAbsent(name, group);
			}
			
			return group;
		}
		finally
		{
			lock.unlock();
		}
	}
	
	@Override
	public boolean groupExistsWithName(String name)
	{
		if((name == null) || name.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("name argument is invalid");
			
			throw new IllegalArgumentException("name argument is invalid");
		}
		
		return isGroupCached(name) ? true : this.groupDao.groupExistsWithName(name);
	}
		
	@Override
	public Set<GroupDetailImpl> getAll()
	{
		if(logger.isDebugEnabled())
			logger.debug("Getting all the groups");
		
		ReentrantLock lock = new ReentrantLock();
		
		try
		{
			lock.lock();
		
			Set<GroupDetailImpl> result = this.groupDao.getAll();
			
			if(result!= null)
			{
				this.groups.clear();
				
				// Could use a more complicated lambda expression here but below is far simpler
				for(GroupDetailImpl group : result)
					this.groups.putIfAbsent(group.getName(), group);
				
				return result;
			}
			else
				return new HashSet<GroupDetailImpl>();
		}
		finally
		{
			lock.unlock();
		}
	}
	
	@Override
	public Set<GroupDetailImpl> getAllFromCacheOnly()
	{
		return this.groups.values().stream().collect(Collectors.toSet());
	}

	@Override
	public boolean insert(String name, String description, boolean isValid, String savedByUser)
	{
		if((name == null) || name.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("name argument is invalid");
			
			throw new IllegalArgumentException("name argument is invalid");
		}

		if((description == null) || description.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("description argument is invalid");
			
			throw new IllegalArgumentException("description argument is invalid");
		}
		
		if((savedByUser == null) || savedByUser.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("savedByUser argument is invalid");
			
			throw new IllegalArgumentException("savedByUser argument is invalid");
		}
				
		if(logger.isDebugEnabled())
			logger.debug("Inserting the group with name" + name);
		
		ReentrantLock lock = new ReentrantLock();
		
		try
		{
			lock.lock();
		
			if(!isGroupCached(name))
			{
				this.groups.putIfAbsent(name, new GroupDetailImpl(name, description, isValid, savedByUser));
				
				return this.groupDao.insert(name, description, isValid, savedByUser);
			}
			
			return true;
		}
		finally
		{
			lock.unlock();
		}
	}
	
	@Override
	public boolean update(String name, String description, boolean isValid, String savedByUser)
	{
		if((name == null) || name.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("name argument is invalid");
			
			throw new IllegalArgumentException("name argument is invalid");
		}

		if((description == null) || description.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("description argument is invalid");
			
			throw new IllegalArgumentException("description argument is invalid");
		}
		
		if((savedByUser == null) || savedByUser.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("savedByUser argument is invalid");
			
			throw new IllegalArgumentException("savedByUser argument is invalid");
		}
				
		if(logger.isDebugEnabled())
			logger.debug("Inserting the group with name" + name);
		
		ReentrantLock lock = new ReentrantLock();
		
		try
		{
			lock.lock();
		
			if(!isGroupCached(name))
			{
				this.groups.putIfAbsent(name, new GroupDetailImpl(name, description, isValid, savedByUser));
				
				return this.groupDao.update(name, description, isValid, savedByUser);
			}
			
			return true;
		}
		finally
		{
			lock.unlock();
		}
	}
	
	
	@Override
	public List<Tag> getMatchingGroupTags(String pattern)
	{
		return this.getAllFromCacheOnly().stream().filter(group -> group.getName().contains(pattern))
				.map(group -> new Tag(group.getName())).collect(Collectors.toList());
	}
}
