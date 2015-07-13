package com.leon.rfq.repositories;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.leon.rfq.domains.SearchCriterionImpl;
import com.leon.rfq.mappers.SearchMapper;

@Repository
public class SearchDaoImpl implements SearchDao
{
	private static final Logger logger = LoggerFactory.getLogger(SearchDaoImpl.class);
	
	@Autowired(required=true)
	private SearchMapper searchMapper;
	
	public SearchDaoImpl() {}
	
	@Override
	public Set<SearchCriterionImpl> get(String owner, String searchKey)
	{
		Set<SearchCriterionImpl> result = this.searchMapper.get(new SearchCriterionImpl(owner, searchKey));
		
		if(result != null)
			return result;
		else
			return new HashSet<>();
	}

	@Override
	public Map<String, Set<SearchCriterionImpl>> get(String owner)
	{
		Map<String, Set<SearchCriterionImpl>> result = this.searchMapper.getForOwner(owner).stream().collect(Collectors.groupingBy(
			SearchCriterionImpl::getSearchKey, Collectors.toSet()));
		
		if(result != null)
			return result;
		else
			return new HashMap<>();
	}

	@Override
	public Map<String, Map<String, Set<SearchCriterionImpl>>> get()
	{
		Map<String, Map<String, Set<SearchCriterionImpl>>> result = this.searchMapper.getAll().stream().collect(Collectors.groupingBy(
				SearchCriterionImpl::getOwner, Collectors.groupingBy(SearchCriterionImpl::getSearchKey, Collectors.toSet())));
		
		if(result != null)
			return result;
		else
			return new HashMap<>();
	}

	@Override
	public boolean delete(String owner, String searchKey)
	{
		if(logger.isDebugEnabled())
			logger.debug("Deleting the search with owner: " + owner + " and key: " + searchKey);
		
		try
		{
			return this.searchMapper.delete(new SearchCriterionImpl(owner, searchKey)) == 1;
		}
		catch(Exception e)
		{
			if(logger.isErrorEnabled())
				logger.error("Failed to delete the search with owner: " + owner + " and key: " + searchKey + " because of exception: " + e);
			return false;
		}
	}

	@Override
	public boolean delete(String owner)
	{
		if(logger.isDebugEnabled())
			logger.debug("Deleting the search with owner: " + owner);
		
		try
		{
			return this.searchMapper.deleteForOwner(owner) == 1;
		}
		catch(Exception e)
		{
			if(logger.isErrorEnabled())
				logger.error("Failed to delete the search with owner: " + owner);
			
			return false;
		}
	}

	@Override
	public boolean insert(String owner, String searchKey, String name, String value, Boolean isPrivate)
	{
		SearchCriterionImpl searchToBeInserted = new SearchCriterionImpl(owner, searchKey, name,
				value, isPrivate);
		
		if(logger.isDebugEnabled())
			logger.debug("Inserting the search: " + searchToBeInserted);
		
		try
		{
			return this.searchMapper.insert(searchToBeInserted) == 1;
		}
		catch(Exception e)
		{
			if(logger.isErrorEnabled())
				logger.error("Failed to insert the search: " + searchToBeInserted);
			
			return false;
		}
	}
}
