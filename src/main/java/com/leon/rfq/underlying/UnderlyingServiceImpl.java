package com.leon.rfq.underlying;

import java.util.List;

import javax.jws.WebMethod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

import com.leon.rfq.events.NewUnderlyingEvent;

@Service
public class UnderlyingServiceImpl implements UnderlyingService, ApplicationEventPublisherAware
{
	private static Logger logger = LoggerFactory.getLogger(UnderlyingServiceImpl.class);
	private ApplicationEventPublisher applicationEventPublisher;
	private UnderlyingDao dao;

	/**
	 * Sets the Underlying Manager DAO object reference property.
	 * 
	 * @param dao 						the underlying manager dao for saving to the database.
	 * @throws NullPointerException 	if the dao parameter is null.
	 */
	@Override
	@WebMethod(exclude = true)
	public void setUnderlyingManagerDao(UnderlyingDao dao)
	{
		if(dao == null)
		{
			if(logger.isErrorEnabled())
				logger.error("dao argument cannot be null");
			
			throw new NullPointerException("dao argument cannot be null");
		}

		this.dao = dao;
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
		if(ric.isEmpty() || (ric == null))
		{
			if(logger.isErrorEnabled())
				logger.error("ric argument is invalid");
			
			throw new IllegalArgumentException("ric argument is invalid");
		}

		if(description.isEmpty() || (description == null))
		{
			if(logger.isErrorEnabled())
				logger.error("description argument is invalid");
			
			throw new IllegalArgumentException("description argument is invalid");
		}

		if(savedByUser.isEmpty() || (savedByUser == null))
		{
			if(logger.isErrorEnabled())
				logger.error("savedByUser argument is invalid");
			
			throw new IllegalArgumentException("savedByUser argument is invalid");
		}

		if(logger.isDebugEnabled())
			logger.debug("Received request from user [" + savedByUser + "] to save underlying with RIC [" + ric + "].");

		UnderlyingDetailImpl newUnderlying = this.dao.insert(ric, description, isValid, savedByUser);

		if(newUnderlying != null)
			this.applicationEventPublisher.publishEvent(new NewUnderlyingEvent(this, newUnderlying));

		return newUnderlying != null;
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
		if(ric.isEmpty() || (ric == null))
		{
			if(logger.isErrorEnabled())
				logger.error("ric argument is invalid");
			
			throw new IllegalArgumentException("ric argument is invalid");
		}

		if(description.isEmpty() || (description == null))
		{
			if(logger.isErrorEnabled())
				logger.error("description argument is invalid");
			
			throw new IllegalArgumentException("description argument is invalid");
		}

		if(updatedByUser.isEmpty() || (updatedByUser == null))
		{
			if(logger.isErrorEnabled())
				logger.error("updatedByUser argument is invalid");
			
			throw new IllegalArgumentException("updatedByUser argument is invalid");
		}
		
		if(logger.isDebugEnabled())
			logger.debug("Received request from user [" + updatedByUser + "] to update underlying with RIC [" + ric + "].");

		UnderlyingDetailImpl newUnderlying = this.dao.update(ric, description, isValid, updatedByUser);

		if(newUnderlying != null)
			this.applicationEventPublisher.publishEvent(new NewUnderlyingEvent(this, newUnderlying));

		return newUnderlying != null;
	}

	/**
	 * Updates the validity of the underlying in the database.
	 * 
	 * @param ric 							the RIC of the underlying to be updated.
	 * @param isValid						the validity of the underlying to be updated.
	 * @returns	true if the update was successful; false otherwise.
	 * @throws IllegalArgumentException 	if the underlying's RIC parameter is an empty string.
	 */
	@Override
	@WebMethod
	public boolean updateValidity(String ric, boolean isValid, String updatedByUser)
	{
		if(ric.isEmpty() || (ric == null))
		{
			if(logger.isErrorEnabled())
				logger.error("ric argument is invalid");
			
			throw new IllegalArgumentException("ric argument is invalid");
		}

		if(updatedByUser.isEmpty() || (updatedByUser == null))
		{
			if(logger.isErrorEnabled())
				logger.error("updatedByUser argument is invalid");
			
			throw new IllegalArgumentException("updatedByUser argument is invalid");
		}
		
		if(logger.isDebugEnabled())
			logger.debug("Received request from user [" + updatedByUser + "] to update the validity of the underlying with RIC [" + ric + "].");

		return this.dao.updateValidity(ric, isValid, updatedByUser);
	}

	/**
	 * Sets the application event publisher.
	 * 
	 * @param applicationEventPublisher 	the applicationEventPublisher for publishing events.
	 * @throws NullPointerException 		if the applicationEventPublisher parameter is null.
	 */
	@Override
	@WebMethod
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher)
	{
		if(applicationEventPublisher == null)
			throw new NullPointerException("applicationEventPublisher");

		this.applicationEventPublisher = applicationEventPublisher;
	}

	/**
	 * Gets all underlyings previously saved to the database.
	 * @returns a list of underlyings that were previously saved in the database.
	 */
	@Override
	@WebMethod
	public List<UnderlyingDetailImpl> getAll()
	{
		if(logger.isDebugEnabled())
			logger.debug("Received request to get all previously saved underlyings.");

		return this.dao.getAll();
	}
}
