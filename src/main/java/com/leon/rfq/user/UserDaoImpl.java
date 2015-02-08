package com.leon.rfq.user;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.leon.rfq.mappers.UserMapper;

public class UserDaoImpl implements UserDao
{
	private static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);
	
	private UserMapper userMapper;
	
	public void setUserMapper(UserMapper userMapper)
	{
		this.userMapper = userMapper;
	}

	@Override
	public boolean delete(String userId)
	{
		if(logger.isDebugEnabled())
			logger.debug("Delete the user with userId " + userId);
		
		try
		{
			return this.userMapper.delete(userId) == 1;
		}
		catch(Exception e)
		{
			if(logger.isErrorEnabled())
				logger.error("Failed to delete the user with userId " + userId + " because of exception " + e);
			
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean save(String userId, String firstName, String lastName,
			String emailAddress, String locationName, int groupId, boolean isValid,
			String savedByUser)
	{
		if(logger.isDebugEnabled())
			logger.debug("Save the user with userId " + userId);
		
		try
		{
			return this.userMapper.save(new UserImpl(userId, firstName, lastName, emailAddress, locationName, groupId, isValid, savedByUser)) == 1;
		}
		catch(Exception e)
		{
			if(logger.isErrorEnabled())
				logger.error("Failed to save the user with userId " + userId + " because of exception " + e);
			
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean updateValidity(String userId, boolean isValid, String updatedByUser)
	{
		if(logger.isDebugEnabled())
			logger.debug("Update the validity of user with userId " + userId + " to " + isValid);

		try
		{
			return this.userMapper.updateValidity(new UserImpl(userId, "", "", "", "", 0, isValid, updatedByUser)) == 1;
		}
		catch(Exception e)
		{
			if(logger.isErrorEnabled())
				logger.error("Failed to update the validity of user with userId " + userId + " to " + isValid + " because of exception " + e);
			
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<UserImpl> getAll()
	{
		if(logger.isDebugEnabled())
			logger.debug("Request to get all users");

		return this.userMapper.getAll();
	}

	@Override
	public UserImpl get(String userId)
	{
		if(logger.isDebugEnabled())
			logger.debug("Request to get user with userId: " + userId);
		
		return this.userMapper.get(userId);
	}
}

