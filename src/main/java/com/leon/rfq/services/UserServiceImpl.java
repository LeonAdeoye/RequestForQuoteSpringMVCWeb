package com.leon.rfq.services;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.leon.rfq.domains.UserDetailImpl;
import com.leon.rfq.repositories.UserDao;

@Service
public final class UserServiceImpl implements UserService
{
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	private ApplicationEventPublisher applicationEventPublisher;
	private final Map<String, UserDetailImpl> users = new HashMap<>();
	
	@Autowired
	private UserDao userDao;
	
	// For unit testing mocking framework.
	@Override
	public void setUserDao(UserDao userDao)
	{
		this.userDao = userDao;
	}
	
	@Override
	@PostConstruct
	public void initialise()
	{
		if(logger.isDebugEnabled())
			logger.debug("Initializing user service by getting all existing users...");
		
		this.getAll();
	}
	
	public UserServiceImpl() {}
	
	@Override
	public boolean isUserCached(String userId)
	{
		return this.users.containsKey(userId);
	}
	
	@Override
	public UserDetailImpl get(String userId)
	{
		if((userId == null) || userId.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("userId argument is invalid");
			
			throw new IllegalArgumentException("userId argument is invalid");
		}
		
		UserDetailImpl user;
		
		if(isUserCached(userId))
			user = this.users.get(userId);
		else
		{
			user = this.userDao.get(userId);
			if(user != null)
				this.users.put(userId, user);
		}
		
		return user;
	}
	
	@Override
	public boolean userExistsWithUserId(String userId)
	{
		if((userId == null) || userId.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("userId argument is invalid");
			
			throw new IllegalArgumentException("userId argument is invalid");
		}
		
		return isUserCached(userId) ? true : this.userDao.userExistsWithUserId(userId);
	}
	
	@Override
	public boolean userExistsWithEmailAddress(String emailAddress)
	{
		if((emailAddress == null) || emailAddress.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("emailAddress argument is invalid");
			
			throw new IllegalArgumentException("emailAddress argument is invalid");
		}
			
		if(this.users.values().stream().anyMatch(user -> user.getEmailAddress().equals(emailAddress)))
			return true;
				
		return this.userDao.userExistsWithEmailAddress(emailAddress);
	}
	
	@Override
	public List<UserDetailImpl> getAll()
	{
		List<UserDetailImpl> result = this.userDao.getAll();
		
		if(result!= null)
		{
			this.users.clear();
			
			// Could use a more complicated lambda expression here but below is far simpler
			for(UserDetailImpl user : result)
				this.users.put(user.getUserId(), user);
			
			return result;
		}
		else
			return new LinkedList<UserDetailImpl>();
	}
	
	@Override
	public List<UserDetailImpl> getAllFromCacheOnly()
	{
		return new LinkedList<UserDetailImpl>(this.users.values());
	}

	@Override
	public boolean insert(String userId, String firstName, String lastName,
			String emailAddress, String locationName, String groupName,
			boolean isValid, String savedByUser)
	{
		if((userId == null) || userId.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("userId argument is invalid");
			
			throw new IllegalArgumentException("userId argument is invalid");
		}
		
		if((savedByUser == null) || savedByUser.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("savedByUser argument is invalid");
			
			throw new IllegalArgumentException("savedByUser argument is invalid");
		}
		
		if((firstName == null) || firstName.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("firstName argument is invalid");
			
			throw new IllegalArgumentException("firstName argument is invalid");
		}
		
		if((lastName == null) || lastName.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("lastName argument is invalid");
			
			throw new IllegalArgumentException("lastName argument is invalid");
		}
		
		if((emailAddress == null) ||emailAddress.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("emailAddress argument is invalid");
			
			throw new IllegalArgumentException("emailAddress argument is invalid");
		}
		
		if((locationName == null) || locationName.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("locationName argument is invalid");
			
			throw new IllegalArgumentException("locationName argument is invalid");
		}
		
		if((groupName == null)|| groupName.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("groupName argument is invalid");
			
			throw new IllegalArgumentException("groupName argument is invalid");
		}
		
		if(!isUserCached(userId))
		{
			this.users.put(userId, new UserDetailImpl(userId, emailAddress, firstName, lastName, locationName,
					groupName, isValid, savedByUser));
			
			return this.userDao.insert(userId, firstName, lastName, emailAddress, locationName, groupName, isValid, savedByUser);
		}
		
		return true;
	}

	@Override
	public boolean delete(String userId)
	{
		if((userId == null) || userId.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("userId argument is invalid");
			
			throw new IllegalArgumentException("userId argument is invalid");
		}
		
		if(isUserCached(userId))
		{
			this.users.remove(userId);
			
			return this.userDao.delete(userId);
		}
		
		return false;
	}

	@Override
	public boolean updateValidity(String userId, boolean isValid, String updatedByUser)
	{
		if((userId == null) || userId.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("userId argument is invalid");
			
			throw new IllegalArgumentException("userId argument is invalid");
		}
		
		if((updatedByUser == null) || updatedByUser.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("updatedByUser argument is invalid");
			
			throw new IllegalArgumentException("updatedByUser argument is invalid");
		}
		
		if(isUserCached(userId))
		{
			UserDetailImpl user = this.users.get(userId);
			
			user.setIsValid(isValid);
			
			return this.userDao.updateValidity(userId, isValid, updatedByUser);
		}
		
		return false;
	}

	@Override
	public boolean update(String userId, String firstName, String lastName,
			String emailAddress, String locationName, String groupName,
			boolean isValid, String updatedByUser)
	{
		if((userId == null) || userId.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("userId argument is invalid");
			
			throw new IllegalArgumentException("userId argument is invalid");
		}
		
		if((updatedByUser == null) || updatedByUser.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("updatedByuser argument is invalid");
			
			throw new IllegalArgumentException("updatedByUser argument is invalid");
		}
		
		if((firstName == null) || firstName.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("firstName argument is invalid");
			
			throw new IllegalArgumentException("firstName argument is invalid");
		}
		
		if((lastName == null) || lastName.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("lastName argument is invalid");
			
			throw new IllegalArgumentException("lastName argument is invalid");
		}
		
		if((emailAddress == null) || emailAddress.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("emailAddress argument is invalid");
			
			throw new IllegalArgumentException("emailAddress argument is invalid");
		}
		
		if((locationName == null) || locationName.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("locationName argument is invalid");
			
			throw new IllegalArgumentException("locationName argument is invalid");
		}
		
		if((groupName == null)|| groupName.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("groupName argument is invalid");
			
			throw new IllegalArgumentException("groupName argument is invalid");
		}
		
		if(isUserCached(userId))
		{
			this.users.remove(userId);
		
			this.users.put(userId, new UserDetailImpl(userId, emailAddress, firstName, lastName, locationName,
				groupName, isValid, updatedByUser));
			
			return this.userDao.update(userId, firstName, lastName, emailAddress, locationName, groupName, isValid, updatedByUser);
		}
		
		return false;
	}
	
	
}
