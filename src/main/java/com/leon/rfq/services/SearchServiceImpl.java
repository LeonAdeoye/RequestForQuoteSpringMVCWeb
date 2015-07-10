package com.leon.rfq.services;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leon.rfq.domains.SearchCriterionImpl;
import com.leon.rfq.repositories.SearchDao;

@Service
public final class SearchServiceImpl implements SearchService
{
	private static final Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class);
	private final Map<String, SearchCriterionImpl> savedSearches = new ConcurrentSkipListMap<>();
	
	@Autowired(required=true)
	private SearchDao searchDao;
	
	// For unit testing mocking framework.
	@Override
	public void setSearchDao(SearchDao searchDao)
	{
		this.searchDao = searchDao;
	}
	
	@Override
	@PostConstruct
	public void initialise()
	{
		if(logger.isDebugEnabled())
			logger.debug("Initializing search service by getting all existing searches...");
		
		this.getAll();
	}
	
	public SearchServiceImpl() {}
	
	@Override
	public boolean isSearchCached(String searchId)
	{
		return this.savedSearches.containsKey(searchId);
	}
	
	@Override
	public SearchCriterionImpl get(String owner, String searchKey)
	{
		if((owner == null) || owner.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("owner argument is invalid");
			
			throw new IllegalArgumentException("owner argument is invalid");
		}
		
		if((searchKey == null) || searchKey.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("searchId argument is invalid");
			
			throw new IllegalArgumentException("searchId argument is invalid");
		}
		
		if(logger.isDebugEnabled())
			logger.debug("Getting the search with search ID" + searchKey);
		
		ReentrantLock lock = new ReentrantLock();
		
		try
		{
			lock.lock();
					
			SearchCriterionImpl search;
			
			if(isSearchCached(owner + searchKey))
				search = this.savedSearches.get(owner + searchKey);
			else
			{
				search = this.searchDao.get(owner + searchKey);
				
				if(search != null)
					this.savedSearches.putIfAbsent(owner + searchKey, search);
			}
			
			return search;
		}
		finally
		{
			lock.unlock();
		}
	}
		
	@Override
	public Set<SearchCriterionImpl> getAll()
	{
		if(logger.isDebugEnabled())
			logger.debug("Getting all previously saved searches.");
		
		ReentrantLock lock = new ReentrantLock();
		
		try
		{
			lock.lock();
						
			Set<SearchCriterionImpl> searches = this.searchDao.getAll();
			
			if(searches != null)
			{
				this.savedSearches.clear();
				
				for(SearchCriterionImpl search : searches)
					this.savedSearches.putIfAbsent(search.getOwner() + search.getKey(), search);
				
				return searches;
			}
			else
				return new HashSet<SearchCriterionImpl>();
		}
		finally
		{
			lock.unlock();
		}
	}

	@Override
	public boolean insert(String owner, String searchKey, String controlName, String controlValue, Boolean isPrivate, Boolean isFilter)
	{
		if((owner == null) || owner.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("owner argument is invalid");
			
			throw new IllegalArgumentException("owner argument is invalid");
		}
		
		if((searchKey == null) || searchKey.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("searchkey argument is invalid");
			
			throw new IllegalArgumentException("searchkey argument is invalid");
		}
		
		if((controlName == null) || controlName.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("controlName argument is invalid");
			
			throw new IllegalArgumentException("controlName argument is invalid");
		}
		
		if((controlValue == null) || controlValue.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("controlValue argument is invalid");
			
			throw new IllegalArgumentException("controlValue argument is invalid");
		}
		
		if(logger.isDebugEnabled())
			logger.debug("Inserting the search with search ID" + searchKey);
		
		ReentrantLock lock = new ReentrantLock();
		
		try
		{
			lock.lock();
			
			if(!isSearchCached(searchKey))
			{
				this.savedSearches.putIfAbsent(owner + searchKey, new SearchCriterionImpl(owner, searchKey,
					controlName, controlValue, isPrivate, isFilter));
				
				return this.searchDao.insert(owner, searchKey, controlName, controlValue, isPrivate, isFilter);
			}
			
			return true;
		}
		finally
		{
			lock.unlock();
		}
	}

	@Override
	public boolean delete(String owner, String searchKey)
	{
		if((searchKey == null) || searchKey.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("searchKey argument is invalid");
			
			throw new IllegalArgumentException("searchKey argument is invalid");
		}
		
		if((owner == null) || owner.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("owner argument is invalid");
			
			throw new IllegalArgumentException("owner argument is invalid");
		}
		
		if(logger.isDebugEnabled())
			logger.debug("Deleting the search criteria with owner: " + owner + " and key: " + searchKey);
		
		ReentrantLock lock = new ReentrantLock();
		
		try
		{
			lock.lock();
					
			if(isSearchCached(owner + searchKey))
			{
				this.savedSearches.remove(owner + searchKey);
				
				return this.searchDao.delete(owner + searchKey);
			}
			
			return false;
		}
		finally
		{
			lock.unlock();
		}
	}
}
