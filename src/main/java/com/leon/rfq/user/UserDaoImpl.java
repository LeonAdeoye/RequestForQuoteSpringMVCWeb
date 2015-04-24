package com.leon.rfq.user;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.leon.rfq.mappers.UserMapper;

@Repository
public class UserDaoImpl implements UserDao
{
	private static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);
	
	@Autowired
	private UserMapper userMapper;

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
				logger.error("Failed to delete the user with userId " + userId + " because of exception: " + e);
			return false;
		}
	}

	@Override
	public boolean insert(String userId, String firstName, String lastName,
			String emailAddress, String locationName, String groupName, boolean isValid,
			String savedByUser)
	{
		if(logger.isDebugEnabled())
			logger.debug("Insert the user with userId " + userId);
		
		try
		{
			return this.userMapper.insert(new UserImpl(userId, emailAddress, firstName, lastName, locationName, groupName, isValid, savedByUser)) == 1;
		}
		catch(Exception e)
		{
			if(logger.isErrorEnabled())
				logger.error("Failed to insert the user with userId " + userId + " because of exception: " + e);
			
			return false;
		}
	}
	
	@Override
	public boolean update(String userId, String firstName, String lastName,
			String emailAddress, String locationName, String groupName, boolean isValid,
			String savedByUser)
	{
		if(logger.isDebugEnabled())
			logger.debug("Update the user with userId " + userId);
		
		try
		{
			return this.userMapper.update(new UserImpl(userId, emailAddress, firstName, lastName, locationName, groupName, isValid, savedByUser)) == 1;
		}
		catch(Exception e)
		{
			if(logger.isErrorEnabled())
				logger.error("Failed to update the user with userId " + userId + " because of exception: " + e);
			
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
			return this.userMapper.updateValidity(new UserImpl(userId, "", "", "", "", "", isValid, updatedByUser)) == 1;
		}
		catch(Exception e)
		{
			if(logger.isErrorEnabled())
				logger.error("Failed to update the validity of user with userId " + userId + " to " + isValid + " because of exception: " + e);
			
			return false;
		}
	}

	@Override
	public List<UserImpl> getAll()
	{
		if(logger.isDebugEnabled())
			logger.debug("Request to get all users");
		
		logger.debug(String.format("Size of users to be returned: %d", this.userMapper.getAll().size()));

		return this.userMapper.getAll();
	}

	@Override
	public UserImpl get(String userId)
	{
		if(logger.isDebugEnabled())
			logger.debug("Request to get user with userId: " + userId);
		
		return this.userMapper.get(userId);
	}

	@Override
	public boolean userExistsWithEmailAddress(String emailAddress)
	{
		if(logger.isDebugEnabled())
			logger.debug("Request to check if user exists with emailAddress: " + emailAddress);
		
		return this.userMapper.userExistsWithEmailAddress(emailAddress) != null;
	}
	
	@Override
	public boolean userExistsWithUserId(String userId)
	{
		if(logger.isDebugEnabled())
			logger.debug("Request to check if user exists with userId: " + userId);
		
		return this.userMapper.userExistsWithUserId(userId) != null;
	}
}

