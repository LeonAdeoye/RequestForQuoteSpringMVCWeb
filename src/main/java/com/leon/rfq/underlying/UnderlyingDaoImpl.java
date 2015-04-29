package com.leon.rfq.underlying;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.leon.rfq.mappers.UnderlyingMapper;

@Repository
public class UnderlyingDaoImpl implements UnderlyingDao
{
	private static final Logger logger = LoggerFactory.getLogger(UnderlyingDaoImpl.class);
	
	@Autowired
	private UnderlyingMapper underlyingMapper;
	
	public UnderlyingDaoImpl() {}

	@Override
	public UnderlyingDetailImpl insert(String ric, String description, boolean isValid, String savedBy)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UnderlyingDetailImpl update(String ric, String description, boolean isValid, String savedBy)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateValidity(String ric, boolean isValid, String updatedBy)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<UnderlyingDetailImpl> getAll()
	{
		if(logger.isDebugEnabled())
			logger.debug("Request to get all underlyings");

		return this.underlyingMapper.getAll();
	}

	@Override
	public UnderlyingDetailImpl get(String ric)
	{
		// TODO Auto-generated method stub
		return null;
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

}
