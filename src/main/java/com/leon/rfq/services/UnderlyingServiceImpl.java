package com.leon.rfq.services;

import java.util.Collections;
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
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

import com.leon.rfq.domains.UnderlyingDetailImpl;
import com.leon.rfq.events.NewUnderlyingEvent;
import com.leon.rfq.repositories.UnderlyingDao;

@Service
public final class UnderlyingServiceImpl implements UnderlyingService, ApplicationEventPublisherAware
{
	private static Logger logger = LoggerFactory.getLogger(UnderlyingServiceImpl.class);
	private ApplicationEventPublisher applicationEventPublisher;
	private final Map<String, UnderlyingDetailImpl> underlyings = new ConcurrentSkipListMap<>();
			
	@Autowired(required=true)
	private UnderlyingDao underlyingDao;
	
	public UnderlyingServiceImpl() {}
	
	@Override
	@PostConstruct
	public void initialise()
	{
		if(logger.isDebugEnabled())
			logger.debug("Initializing underlying service by getting all existing underlyings...");
		
		this.getAll();
	}
	
	/**
	 * Returns true if underlying is cached
	 * 
	 * @param ric	the ric that is searched for
	 */
	@Override
	public boolean isUnderlyingCached(String ric)
	{
		return this.underlyings.containsKey(ric);
	}

	/**
	 * Sets the Underlying DAO object reference property.
	 * 
	 * @param underlyingDao				the underlying DAO for saving to the database.
	 * @throws NullPointerException 	if the underlyingDao parameter is null.
	 */
	@Override
	public void setUnderlyingDao(UnderlyingDao underlyingDao)
	{
		if(underlyingDao == null)
		{
			if(logger.isErrorEnabled())
				logger.error("underlyingDao argument cannot be null");
			
			throw new NullPointerException("underlyingDao argument cannot be null");
		}

		this.underlyingDao = underlyingDao;
	}

	/**
	 * Inserts the underlying to the database and publishes an event.
	 * 
	 * @param ric 							the RIC of the underlying to be saved.
	 * @param description					the description of the underlying to be saved.
	 * @param isValid						the validity flag
	 * @param savedByUser					the user who is saving the underlying.
	 * @returns	true if the save was successful; false otherwise.
	 * @throws IllegalArgumentException 	if the RIC or description or savedByUser parameter is an empty string.
	 */
	@Override
	public boolean insert(String ric, String description, boolean isValid, String savedByUser)
	{
		if((ric == null) || ric.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("ric argument is invalid");
			
			throw new IllegalArgumentException("ric argument is invalid");
		}

		if((description == null) || description.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("description argument is invalid");
			
			throw new IllegalArgumentException("description argument is invalid");
		}

		if((savedByUser == null) || savedByUser.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("savedByUser argument is invalid");
			
			throw new IllegalArgumentException("savedByUser argument is invalid");
		}

		if(logger.isDebugEnabled())
			logger.debug("Received request from user [" + savedByUser + "] to save underlying with RIC [" + ric + "].");
		
		ReentrantLock lock = new ReentrantLock();
				
		try
		{
			lock.lock();
			
			if(!isUnderlyingCached(ric))
			{
				this.underlyings.putIfAbsent(ric, new UnderlyingDetailImpl(ric, description, isValid, savedByUser));
				
				UnderlyingDetailImpl newUnderlying = this.underlyingDao.insert(ric, description, isValid, savedByUser);
				
				if(newUnderlying != null)
					this.applicationEventPublisher.publishEvent(new NewUnderlyingEvent(this, newUnderlying));
				
				return newUnderlying != null;
			}
	
			return false;
		}
		finally
		{
			lock.unlock();
		}
	}
	
	/**
	 * Updates the underlying in the database and publishes an event.
	 * 
	 * @param ric 							the RIC of the underlying to be saved.
	 * @param description					the description of the underlying to be saved.
	 * @param savedBy						the user who is saving the underlying.
	 * @returns	true if the save was successful; false otherwise.
	 * @throws IllegalArgumentException 	if the RIC or description or savedBy parameter is an empty string.
	 */
	@Override
	public boolean update(String ric, String description, boolean isValid, String updatedByUser)
	{
		if((ric == null) || ric.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("ric argument is invalid");
			
			throw new IllegalArgumentException("ric argument is invalid");
		}

		if((description == null) || description.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("description argument is invalid");
			
			throw new IllegalArgumentException("description argument is invalid");
		}

		if((updatedByUser == null) || updatedByUser.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("updatedByUser argument is invalid");
			
			throw new IllegalArgumentException("updatedByUser argument is invalid");
		}
		
		if(logger.isDebugEnabled())
			logger.debug("Received request from user [" + updatedByUser + "] to update underlying with RIC [" + ric + "].");
		
		ReentrantLock lock = new ReentrantLock();
		
		try
		{
			lock.lock();
			
			if(isUnderlyingCached(ric))
			{
				this.underlyings.remove(ric);
			
				this.underlyings.putIfAbsent(ric, new UnderlyingDetailImpl(ric, description, isValid,updatedByUser));
				
				UnderlyingDetailImpl updatedUnderlying = this.underlyingDao.update(ric, description, isValid, updatedByUser);
				
				if(updatedUnderlying != null)
					this.applicationEventPublisher.publishEvent(new NewUnderlyingEvent(this, updatedUnderlying));
	
				return updatedUnderlying != null;
			}
			
			return false;
		}
		finally
		{
			lock.unlock();
		}
	}

	/**
	 * Sets the application event publisher.
	 * 
	 * @param applicationEventPublisher 	the applicationEventPublisher for publishing events.
	 * @throws NullPointerException 		if the applicationEventPublisher parameter is null.
	 */
	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher)
	{
		if(applicationEventPublisher == null)
		{
			if(logger.isErrorEnabled())
				logger.error("applicationEventPublisher argument cannot be null");
			
			throw new NullPointerException("applicationEventPublisher");
		}

		this.applicationEventPublisher = applicationEventPublisher;
	}
	
	/**
	 * Gets all underlyings previously saved to the cache only. Does not retrieve from the database.
	 * @returns a list of underlyings that were previously saved in the cache.
	 */
	@Override
	public Set<UnderlyingDetailImpl> getAllFromCacheOnly()
	{
		return new HashSet<UnderlyingDetailImpl>(this.underlyings.values());
	}

	/**
	 * Gets all underlyings previously saved to the database.
	 * @returns a list of underlyings that were previously saved in the database.
	 */
	@Override
	public Set<UnderlyingDetailImpl> getAll()
	{
		if(logger.isDebugEnabled())
			logger.debug("Getting all previously saved underlyings.");
		
		ReentrantLock lock = new ReentrantLock();
		
		try
		{
			lock.lock();
							
			Set<UnderlyingDetailImpl> underlyings =  Collections.synchronizedSet(this.underlyingDao.getAll());
			
			if(underlyings!= null)
			{
				this.underlyings.clear();
				
				for(UnderlyingDetailImpl underlying : underlyings)
					this.underlyings.putIfAbsent(underlying.getRic(), underlying);
				
				return underlyings;
			}
			else
				return new HashSet<UnderlyingDetailImpl>();
		}
		finally
		{
			lock.unlock();
		}
	}

	/**
	 * Gets the underlying with the RIC passed as a parameter
	 * 
	 * @param 	ric 						the RIC of the underlying that needs to be retrieved.
	 * @throws 	IllegalArgumentException 	if RIC parameter is null or empty.
	 */
	@Override
	public UnderlyingDetailImpl get(String ric)
	{
		if((ric == null) || ric.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("ric argument is invalid");
			
			throw new IllegalArgumentException("ric argument is invalid");
		}
		
		if(logger.isDebugEnabled())
			logger.debug("Get underlying with RIC" + ric);
		
		ReentrantLock lock = new ReentrantLock();
		
		try
		{
			lock.lock();
							
			UnderlyingDetailImpl underlying;
			
			if(this.isUnderlyingCached(ric))
				underlying = this.underlyings.get(ric);
			else
			{
				underlying = this.underlyingDao.get(ric);
				if(underlying != null)
					this.underlyings.putIfAbsent(ric, underlying);
			}
			
			return underlying;
		}
		finally
		{
			lock.unlock();
		}
	}

	/**
	 * Delete the underlying with the RIC passed as a parameter
	 * 
	 * @param 	ric 						the RIC of the underlying that needs to be deleted.
	 * @throws 	IllegalArgumentException 	if RIC parameter is null or empty.
	 */
	@Override
	public boolean delete(String ric)
	{
		if((ric == null) || ric.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("ric argument is invalid");
			
			throw new IllegalArgumentException("ric argument is invalid");
		}

		ReentrantLock lock = new ReentrantLock();
		
		if(logger.isDebugEnabled())
			logger.debug("Delete underlying with RIC" + ric);
		
		try
		{
			lock.lock();
							
			if(isUnderlyingCached(ric))
			{
				this.underlyings.remove(ric);
				
				return this.underlyingDao.delete(ric);
			}
			
			return false;
		}
		finally
		{
			lock.unlock();
		}
	}

	/**
	 * Check if the underlying exists with the RIC passed as a parameter
	 * 
	 * @param 	ric 						the RIC of the underlying that needs to be checked.
	 * @throws 	IllegalArgumentException 	if RIC parameter is null or empty.
	 */
	@Override
	public boolean underlyingExistsWithRic(String ric)
	{
		if((ric == null) || ric.isEmpty())
		{
			if(logger.isErrorEnabled())
				logger.error("ric argument is invalid");
			
			throw new IllegalArgumentException("ric argument is invalid");
		}
		
		if(logger.isDebugEnabled())
			logger.debug("Check if underlying exists with RIC " + ric);
		
		return (isUnderlyingCached(ric) ? true : this.underlyingDao.underlyingExistsWithRic(ric));
	}
	
}
