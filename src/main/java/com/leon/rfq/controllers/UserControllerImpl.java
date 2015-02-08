package com.leon.rfq.controllers;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.leon.rfq.user.UserDao;
import com.leon.rfq.user.UserImpl;

public class UserControllerImpl implements UserController
{
	private static final Logger logger = LoggerFactory.getLogger(UserControllerImpl.class);
	
	private UserDao userDao;
	
	@Override
	public void setUserDao(UserDao userDao)
	{
		this.userDao = userDao;
	}
	
	@Override
	public UserImpl get(String userId)
	{
		if(userId == null)
		{
			if(logger.isErrorEnabled())
				logger.error("userId parameter cannot be null");
			
			throw new NullPointerException("userId parameter cannot be null");
		}
		
		if(userId.equals(""))
		{
			if(logger.isErrorEnabled())
				logger.error("userId parameter cannot be null");
			
			throw new IllegalArgumentException("userId parameter cannot be an empty string");
		}
		
		return this.userDao.get(userId);
	}
	
	@Override
	public List<UserImpl> getAll()
	{
		List<UserImpl> result = this.userDao.getAll();
		
		if(result!= null)
			return result;
		else
			return new LinkedList<UserImpl>();
	}

	@Override
	public boolean save(String userId, String firstName, String lastName,
			String emailAddress, String locationName, int groupId,
			boolean isValid, String savedByUser)
	{
		if(userId == null)
		{
			if(logger.isErrorEnabled())
				logger.error("userId parameter cannot be null");
			
			throw new NullPointerException("userId parameter cannot be null");
		}
		
		if(userId.equals(""))
		{
			if(logger.isErrorEnabled())
				logger.error("userId parameter cannot be null");
			
			throw new IllegalArgumentException("userId parameter cannot be an empty string");
		}
		
		if(savedByUser == null)
		{
			if(logger.isErrorEnabled())
				logger.error("savedByUser parameter cannot be null");
			
			throw new NullPointerException("savedByUser parameter cannot be null");
		}
		
		if(savedByUser.equals(""))
		{
			if(logger.isErrorEnabled())
				logger.error("savedByUser parameter cannot be null");
			
			throw new IllegalArgumentException("savedByUser parameter cannot be an empty string");
		}
		
		if(firstName == null)
		{
			if(logger.isErrorEnabled())
				logger.error("firstName parameter cannot be null");
			
			throw new NullPointerException("firstName parameter cannot be null");
		}
		
		if(firstName.equals(""))
		{
			if(logger.isErrorEnabled())
				logger.error("firstName parameter cannot be null");
			
			throw new IllegalArgumentException("firstName parameter cannot be an empty string");
		}

		if(lastName == null)
		{
			if(logger.isErrorEnabled())
				logger.error("lastName parameter cannot be null");
			
			throw new NullPointerException("lastName parameter cannot be null");
		}
		
		if(lastName.equals(""))
		{
			if(logger.isErrorEnabled())
				logger.error("lastName parameter cannot be null");
			
			throw new IllegalArgumentException("lastName parameter cannot be an empty string");
		}

		if(emailAddress == null)
		{
			if(logger.isErrorEnabled())
				logger.error("emailAddress parameter cannot be null");
			
			throw new NullPointerException("emailAddress parameter cannot be null");
		}
		
		if(emailAddress.equals(""))
		{
			if(logger.isErrorEnabled())
				logger.error("emailAddress parameter cannot be null");
			
			throw new IllegalArgumentException("emailAddress parameter cannot be an empty string");
		}
		
		if(locationName == null)
		{
			if(logger.isErrorEnabled())
				logger.error("locationName parameter cannot be null");
			
			throw new NullPointerException("locationName parameter cannot be null");
		}
		
		if(locationName.equals(""))
		{
			if(logger.isErrorEnabled())
				logger.error("locationName parameter cannot be null");
			
			throw new IllegalArgumentException("locationName parameter cannot be an empty string");
		}
		
		return this.userDao.save(userId, firstName, lastName, emailAddress, locationName, groupId, isValid, savedByUser);
	}

	@Override
	public boolean delete(String userId)
	{
		if(userId == null)
		{
			if(logger.isErrorEnabled())
				logger.error("userId parameter cannot be null");
			
			throw new NullPointerException("userId parameter cannot be null");
		}
		
		if(userId.equals(""))
		{
			if(logger.isErrorEnabled())
				logger.error("userId parameter cannot be null");
			
			throw new IllegalArgumentException("userId parameter cannot be an empty string");
		}
		
		return this.userDao.delete(userId);
	}

	@Override
	public boolean updateValidity(String userId, boolean isValid, String updatedByUser)
	{
		if(userId == null)
		{
			if(logger.isErrorEnabled())
				logger.error("userId parameter cannot be null");
			
			throw new NullPointerException("userId parameter cannot be null");
		}
		
		if(userId.equals(""))
		{
			if(logger.isErrorEnabled())
				logger.error("userId parameter cannot be null");
			
			throw new IllegalArgumentException("userId parameter cannot be an empty string");
		}
		
		if(updatedByUser == null)
		{
			if(logger.isErrorEnabled())
				logger.error("updatedByUser parameter cannot be null");
			
			throw new NullPointerException("updatedByUser parameter cannot be null");
		}
		
		if(updatedByUser.equals(""))
		{
			if(logger.isErrorEnabled())
				logger.error("updatedByUser parameter cannot be null");
			
			throw new IllegalArgumentException("updatedByUser parameter cannot be an empty string");
		}
		
		return this.userDao.updateValidity(userId, isValid, updatedByUser);
	}
}
