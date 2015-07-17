package com.leon.rfq.services;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.leon.rfq.domains.ChatMessageImpl;
import com.leon.rfq.domains.UserDetailImpl;
import com.leon.rfq.repositories.UserDao;

@Service
public final class UserServiceImpl implements UserService
{
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	private ApplicationEventPublisher applicationEventPublisher;
	private final Map<String, UserDetailImpl> users = new ConcurrentSkipListMap<>();
	
	@Autowired(required=true)
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
		
		if(logger.isDebugEnabled())
			logger.debug("Getting the user with user ID" + userId);
		
		ReentrantLock lock = new ReentrantLock();
		
		try
		{
			lock.lock();
					
			UserDetailImpl user;
			
			if(isUserCached(userId))
				user = this.users.get(userId);
			else
			{
				user = this.userDao.get(userId);
				if(user != null)
					this.users.putIfAbsent(userId, user);
			}
			
			return user;
		}
		finally
		{
			lock.unlock();
		}
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
	public Set<UserDetailImpl> getAll()
	{
		if(logger.isDebugEnabled())
			logger.debug("Getting all previously saved users.");
		
		ReentrantLock lock = new ReentrantLock();
		
		try
		{
			lock.lock();
						
			Set<UserDetailImpl> users = this.userDao.getAll();
			
			if(users != null)
			{
				this.users.clear();
				
				// Could use a more complicated lambda expression here but below is far simpler
				for(UserDetailImpl user : users)
					this.users.putIfAbsent(user.getUserId(), user);
				
				return users;
			}
			else
				return new HashSet<UserDetailImpl>();
		}
		finally
		{
			lock.unlock();
		}
	}
	
	@Override
	public Set<UserDetailImpl> getAllFromCacheOnly()
	{
		return new HashSet<UserDetailImpl>(this.users.values());
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
		
		if(logger.isDebugEnabled())
			logger.debug("Inserting the user with user ID" + userId);
		
		ReentrantLock lock = new ReentrantLock();
		
		try
		{
			lock.lock();
			
			if(!isUserCached(userId))
			{
				this.users.putIfAbsent(userId, new UserDetailImpl(userId, emailAddress, firstName, lastName, locationName,
						groupName, isValid, savedByUser));
				
				return this.userDao.insert(userId, firstName, lastName, emailAddress, locationName, groupName, isValid, savedByUser);
			}
			
			return true;
		}
		finally
		{
			lock.unlock();
		}
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
		
		if(logger.isDebugEnabled())
			logger.debug("Deleting the user with user ID" + userId);
		
		ReentrantLock lock = new ReentrantLock();
		
		try
		{
			lock.lock();
					
			if(isUserCached(userId))
			{
				this.users.remove(userId);
				
				return this.userDao.delete(userId);
			}
			
			return false;
		}
		finally
		{
			lock.unlock();
		}
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
		
		if(logger.isDebugEnabled())
			logger.debug("Updating the validity of user with user ID" + userId);
		
		ReentrantLock lock = new ReentrantLock();
		
		try
		{
			lock.lock();
							
			if(isUserCached(userId))
			{
				UserDetailImpl user = this.users.get(userId);
				
				user.setIsValid(isValid);
				
				return this.userDao.updateValidity(userId, isValid, updatedByUser);
			}
			
			return false;
		}
		finally
		{
			lock.unlock();
		}
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
		
		if(logger.isDebugEnabled())
			logger.debug("Updating user with user ID" + userId);
		
		ReentrantLock lock = new ReentrantLock();
		
		try
		{
			lock.lock();
							
			if(isUserCached(userId))
			{
				this.users.remove(userId);
			
				this.users.putIfAbsent(userId, new UserDetailImpl(userId, emailAddress, firstName, lastName, locationName,
					groupName, isValid, updatedByUser));
				
				return this.userDao.update(userId, firstName, lastName, emailAddress, locationName, groupName, isValid, updatedByUser);
			}
			
			return false;
		}
		finally
		{
			lock.unlock();
		}
	}

	@Override
	public Set<ChatMessageImpl> getMessages(String userId, int requestId, LocalDateTime fromTimeStamp)
	{
		if((userId == null) || userId.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("userId argument is invalid");
			
			throw new IllegalArgumentException("userId argument is invalid");
		}
		
		if(this.users.containsKey(userId))
			return this.users.get(userId).getMessagesForRequest(requestId, fromTimeStamp);
		
		return new HashSet<>();
	}
}
