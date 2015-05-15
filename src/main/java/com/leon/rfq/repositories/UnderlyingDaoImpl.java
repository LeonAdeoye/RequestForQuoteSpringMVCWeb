package com.leon.rfq.repositories;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.leon.rfq.domains.UnderlyingDetailImpl;
import com.leon.rfq.mappers.UnderlyingMapper;

@Repository
public class UnderlyingDaoImpl implements UnderlyingDao
{
	private static final Logger logger = LoggerFactory.getLogger(UnderlyingDaoImpl.class);
	
	@Autowired
	private UnderlyingMapper underlyingMapper;
	
	public UnderlyingDaoImpl() {}

	@Override
	public UnderlyingDetailImpl insert(String ric, String description, boolean isValid, String savedByUser)
	{
		if(logger.isDebugEnabled())
			logger.debug("Inserting underlying with ric " + ric);
		
		try
		{
			UnderlyingDetailImpl underlying = new UnderlyingDetailImpl(ric, description, isValid, savedByUser);
			
			if(this.underlyingMapper.insert(underlying) == 1)
				return underlying;
		}
		catch(Exception e)
		{
			if(logger.isErrorEnabled())
				logger.error("Failed to insert the underlying with ric " + ric + " because of exception: " + e);
		}
		
		return null;
	}

	@Override
	public UnderlyingDetailImpl update(String ric, String description, boolean isValid, String updatedByUser)
	{
		if(logger.isDebugEnabled())
			logger.debug("Updating underlying with ric " + ric);
		
		try
		{
			UnderlyingDetailImpl underlying = new UnderlyingDetailImpl(ric, description, isValid, updatedByUser);
			
			if(this.underlyingMapper.update(underlying) == 1)
				return underlying;
		}
		catch(Exception e)
		{
			if(logger.isErrorEnabled())
				logger.error("Failed to update the underlying with ric " + ric + " because of exception: " + e);
		}
		
		return null;
	}

	@Override
	public List<UnderlyingDetailImpl> getAll()
	{
		if(logger.isDebugEnabled())
			logger.debug("Request to get all underlyings");

		try
		{
			return this.underlyingMapper.getAll();
		}
		catch(Exception e)
		{
			if(logger.isErrorEnabled())
				logger.error("Failed to get all underlyings because of exception: " + e);
			
			return new LinkedList<UnderlyingDetailImpl>();
		}
	}

	@Override
	public UnderlyingDetailImpl get(String ric)
	{
		if(logger.isDebugEnabled())
			logger.debug("Get the underlying with ric " + ric);
		
		try
		{
			return this.underlyingMapper.get(ric);
		}
		catch(Exception e)
		{
			if(logger.isErrorEnabled())
				logger.error("Failed to get the underlying with ric " + ric + " because of exception: " + e);
			
			return null;
		}
	}

	@Override
	public boolean delete(String ric)
	{
		if(logger.isDebugEnabled())
			logger.debug("Delete the underlying with ric " + ric);
		
		try
		{
			return this.underlyingMapper.delete(ric) == 1;
		}
		catch(Exception e)
		{
			if(logger.isErrorEnabled())
				logger.error("Failed to delete the underlying with ric " + ric + " because of exception: " + e);
			
			return false;
		}
	}

	@Override
	public boolean underlyingExistsWithRic(String ric)
	{
		if(logger.isDebugEnabled())
			logger.debug("check if the underlying exists with ric " + ric);
		
		try
		{
			return this.underlyingMapper.underlyingExistsWithRic(ric) != null;
		}
		catch(Exception e)
		{
			if(logger.isErrorEnabled())
				logger.error("Failed to check if the underlying exists with ric " + ric + " because of exception: " + e);
			
			return false;
		}
	}

}
