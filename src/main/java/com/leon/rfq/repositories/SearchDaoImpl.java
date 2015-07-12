package com.leon.rfq.repositories;

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
		return this.searchMapper.get(new SearchCriterionImpl(owner, searchKey));
	}

	@Override
	public Map<String, Set<SearchCriterionImpl>> get(String owner)
	{
		return this.searchMapper.getForOwner(owner).stream().collect(Collectors.groupingBy(
			SearchCriterionImpl::getSearchKey, Collectors.toSet()));
	}

	@Override
	public Map<String, Map<String, Set<SearchCriterionImpl>>> get()
	{
		return this.searchMapper.getAll().stream().collect(Collectors.groupingBy(
				SearchCriterionImpl::getOwner, Collectors.groupingBy(
						SearchCriterionImpl::getSearchKey, Collectors.toSet())));
	}

	@Override
	public boolean delete(String owner, String searchKey)
	{
		return this.searchMapper.delete(new SearchCriterionImpl(owner, searchKey)) == 1;
	}

	@Override
	public boolean delete(String owner)
	{
		return this.searchMapper.deleteForOwner(owner) == 1;
	}

	@Override
	public boolean insert(String owner, String searchKey, String controlName,
			String controlValue, Boolean isPrivate, Boolean isFilter)
	{
		return this.searchMapper.insert(new SearchCriterionImpl(owner, searchKey, controlName,
				controlValue, isPrivate, isFilter)) == 1;
	}

}
