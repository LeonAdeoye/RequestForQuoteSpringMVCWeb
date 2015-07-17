package com.leon.rfq.services;

import java.time.LocalDateTime;
import java.util.Set;

import com.leon.rfq.domains.ChatMessageImpl;
import com.leon.rfq.domains.UserDetailImpl;
import com.leon.rfq.repositories.UserDao;

public interface UserService
{
	UserDetailImpl get(String userId);

	Set<UserDetailImpl> getAll();
	
	boolean insert(String userId, String firstName, String lastName, String emailAddress,
			String locationName, String groupName, boolean isValid, String savedByUser);
	
	boolean delete(String userId);
	
	boolean updateValidity(String userId, boolean isValid, String updatedByUser);

	void setUserDao(UserDao userDao);
	
	boolean isUserCached(String userId);
	
	boolean userExistsWithUserId(String userId);
	
	boolean userExistsWithEmailAddress(String emailAddress);
	
	boolean update(String userId, String firstName, String lastName, String emailAddress,
			String locationName, String groupName, boolean isValid, String savedByUser);

	void initialise();

	Set<UserDetailImpl> getAllFromCacheOnly();

	Set<ChatMessageImpl> getMessages(String userId, int requestId, LocalDateTime fromTimeStamp);
}
