package com.leon.rfq.underlying;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

import com.leon.rfq.events.NewUnderlyingEvent;

@Service
public class UnderlyingServiceImpl implements UnderlyingService, ApplicationEventPublisherAware
{
	private static Logger logger = LoggerFactory.getLogger(UnderlyingServiceImpl.class);
	private ApplicationEventPublisher applicationEventPublisher;
	
	@Autowired
	private UnderlyingDao underlyingDao;
	
	private final Map<String, UnderlyingDetailImpl> underlyings = new HashMap<>();
	
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
		
		if(!isUnderlyingCached(ric))
		{
			this.underlyings.put(ric, new UnderlyingDetailImpl(ric, description, isValid, savedByUser));
			
			UnderlyingDetailImpl newUnderlying = this.underlyingDao.insert(ric, description, isValid, savedByUser);
			
			if(newUnderlying != null)
				this.applicationEventPublisher.publishEvent(new NewUnderlyingEvent(this, newUnderlying));
			
			return newUnderlying != null;
		}

		return false;
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
		
		if(isUnderlyingCached(ric))
		{
			this.underlyings.remove(ric);
		
			this.underlyings.put(ric, new UnderlyingDetailImpl(ric, description, isValid,updatedByUser));
			
			UnderlyingDetailImpl updatedUnderlying = this.underlyingDao.update(ric, description, isValid, updatedByUser);
			
			if(updatedUnderlying != null)
				this.applicationEventPublisher.publishEvent(new NewUnderlyingEvent(this, updatedUnderlying));

			return updatedUnderlying != null;
		}
		
		return false;
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
	 * Gets all underlyings previously saved to the database.
	 * @returns a list of underlyings that were previously saved in the database.
	 */
	@Override
	public List<UnderlyingDetailImpl> getAll()
	{
		if(logger.isDebugEnabled())
			logger.debug("Received request to get all previously saved underlyings.");
		
		
		List<UnderlyingDetailImpl> result = this.underlyingDao.getAll();
		
		if(result!= null)
		{
			this.underlyings.clear();
			
			for(UnderlyingDetailImpl underlying : result)
				this.underlyings.put(underlying.getRic(), underlying);
			
			return result;
		}
		else
			return new LinkedList<UnderlyingDetailImpl>();
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
		
		UnderlyingDetailImpl underlying;
		
		if(this.isUnderlyingCached(ric))
			underlying = this.underlyings.get(ric);
		else
		{
			underlying = this.underlyingDao.get(ric);
			if(underlying != null)
				this.underlyings.put(ric, underlying);
		}
		
		return underlying;
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
		
		if(logger.isDebugEnabled())
			logger.debug("Delete underlying with RIC" + ric);
		
		if(isUnderlyingCached(ric))
		{
			this.underlyings.remove(ric);
			
			return this.underlyingDao.delete(ric);
		}
		
		return false;
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
