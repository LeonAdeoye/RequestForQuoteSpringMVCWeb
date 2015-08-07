package com.leon.rfq.services;

import java.util.HashMap;
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
	private final Map<String, Map<String, Set<SearchCriterionImpl>>> savedSearches = new ConcurrentSkipListMap<>();
	
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
		
		this.get();
	}
	
	public SearchServiceImpl() {}
	
	private void clearCache(String owner)
	{
		this.savedSearches.get(owner).clear();
		this.savedSearches.remove(owner);
	}
	
	private void clearCache(String owner, String searchKey)
	{
		this.savedSearches.get(owner).get(searchKey).clear();
		this.savedSearches.get(owner).remove(searchKey);
	}
	
	private boolean areSearchCriteriaCached(String owner)
	{
		return this.savedSearches.containsKey(owner);
	}
	
	private boolean areSearchCriteriaCached(String owner, String searchKey)
	{
		if(this.savedSearches.containsKey(owner))
			return this.savedSearches.get(owner).containsKey(searchKey);
		
		return false;
	}
	
	private Set<SearchCriterionImpl> getCriteriaFromCache(String owner, String searchKey)
	{
		return this.savedSearches.get(owner).get(searchKey);
	}
	
	private Map<String, Set<SearchCriterionImpl>> getCriteriaFromCache(String owner)
	{
		return this.savedSearches.get(owner);
	}
	
	private void addCriteriaToCache(String owner, String searchKey, String name, String value, Boolean isPrivate)
	{
		SearchCriterionImpl criterionToBeInserted = new SearchCriterionImpl(owner, searchKey, name, value, isPrivate);
		Set<SearchCriterionImpl> setToBeInserted = new HashSet<>();
		
		if(areSearchCriteriaCached(owner))
		{
			if(!areSearchCriteriaCached(owner, searchKey))
			{
				setToBeInserted.add(criterionToBeInserted);
				this.savedSearches.get(owner).put(searchKey, setToBeInserted);
			}
			else
				this.savedSearches.get(owner).get(searchKey).add(new SearchCriterionImpl(owner, searchKey, name, value, isPrivate));
		}
		else
		{
			setToBeInserted.add(criterionToBeInserted);
			Map<String, Set<SearchCriterionImpl>> mapToBeInserted = new HashMap<>();
			mapToBeInserted.put(searchKey, setToBeInserted);
			this.savedSearches.put(owner, mapToBeInserted);
		}
	}
	
	@Override
	public Set<SearchCriterionImpl> get(String owner, String searchKey)
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
				logger.error("searchKey argument is invalid");
			
			throw new IllegalArgumentException("searchKey argument is invalid");
		}
		
		if(logger.isDebugEnabled())
			logger.debug("Getting the search with search ID" + searchKey);
		
		ReentrantLock lock = new ReentrantLock();
		
		try
		{
			lock.lock();
					
			Set<SearchCriterionImpl> criteria;
			
			if(areSearchCriteriaCached(owner, searchKey))
				criteria = getCriteriaFromCache(owner, searchKey);
			else
			{
				criteria = this.searchDao.get(owner, searchKey);
				
				criteria.forEach(criterion -> addCriteriaToCache(owner, searchKey, criterion.getName(),
					criterion.getValue(), criterion.getIsPrivate()));
			}
			
			return criteria;
		}
		finally
		{
			lock.unlock();
		}
	}
		
	@Override
	public Map<String, Set<SearchCriterionImpl>> get(String owner)
	{
		if((owner == null) || owner.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("owner argument is invalid");
			
			throw new IllegalArgumentException("owner argument is invalid");
		}
		
		if(logger.isDebugEnabled())
			logger.debug("Getting all previously saved searches for owner:" + owner);
		
		Map<String, Set<SearchCriterionImpl>> criteria;
		
		ReentrantLock lock = new ReentrantLock();
		
		try
		{
			lock.lock();
						
			if(!areSearchCriteriaCached(owner))
			{
				criteria = this.searchDao.get(owner);

				for(Set<SearchCriterionImpl> set : criteria.values())
				{
					set.forEach(criterion ->addCriteriaToCache(owner, criterion.getSearchKey(),
						criterion.getName(), criterion.getValue(), criterion.getIsPrivate()));
				}
			}
			else
				criteria = getCriteriaFromCache(owner);
			
			return criteria;
		}
		finally
		{
			lock.unlock();
		}
	}
	
	@Override
	public Map<String, Map<String, Set<SearchCriterionImpl>>> get()
	{
		if(logger.isDebugEnabled())
			logger.debug("Getting all previously saved searches for all owners");
		
		Map<String, Map<String, Set<SearchCriterionImpl>>> criteria;
		
		ReentrantLock lock = new ReentrantLock();
		
		try
		{
			lock.lock();
						
			criteria = this.searchDao.get();

			if(criteria != null)
			{
				this.savedSearches.clear();
				this.savedSearches.putAll(criteria);
				return criteria;
			}
			else
				return new HashMap<>();
		}
		finally
		{
			lock.unlock();
		}
	}

	@Override
	public boolean insert(String owner, String searchKey, String name, String value, Boolean isPrivate)
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
			
			throw new IllegalArgumentException("searchKey argument is invalid");
		}
		
		if((name == null) || name.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("name argument is invalid");
			
			throw new IllegalArgumentException("name argument is invalid");
		}
		
		if((value == null) || value.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("value argument is invalid");
			
			throw new IllegalArgumentException("value argument is invalid");
		}
		
		if(logger.isDebugEnabled())
			logger.debug("Inserting the search with search ID: " + searchKey);
		
		ReentrantLock lock = new ReentrantLock();
		
		try
		{
			lock.lock();
			
			addCriteriaToCache(owner, searchKey, name, value, isPrivate);
				
			return this.searchDao.insert(owner, searchKey, name, value, isPrivate);
			
		}
		finally
		{
			lock.unlock();
		}
	}

	@Override
	public boolean delete(String owner, String searchKey)
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
				logger.error("searchKey argument is invalid");
			
			throw new IllegalArgumentException("searchKey argument is invalid");
		}
		
		if(logger.isDebugEnabled())
			logger.debug("Deleting the search criteria with owner: " + owner + " and key: " + searchKey);
		
		ReentrantLock lock = new ReentrantLock();
		
		try
		{
			lock.lock();
					
			if(areSearchCriteriaCached(owner, searchKey))
			{
				clearCache(owner, searchKey);
				
				return this.searchDao.delete(owner, searchKey);
			}
			
			return false;
		}
		finally
		{
			lock.unlock();
		}
	}
	
	@Override
	public boolean delete(String owner)
	{
		if((owner == null) || owner.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("owner argument is invalid");
			
			throw new IllegalArgumentException("owner argument is invalid");
		}
		
		if(logger.isDebugEnabled())
			logger.debug("Deleting the search criteria with owner: " + owner);
		
		ReentrantLock lock = new ReentrantLock();
		
		try
		{
			lock.lock();
					
			if(areSearchCriteriaCached(owner))
			{
				clearCache(owner);
				
				return this.searchDao.delete(owner);
			}
			
			return false;
		}
		finally
		{
			lock.unlock();
		}
	}
}
