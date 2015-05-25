package com.leon.rfq.services;

import java.util.List;

import com.leon.rfq.domains.UserDetailImpl;
import com.leon.rfq.repositories.UserDao;

public interface UserService
{
	UserDetailImpl get(String userId);

	List<UserDetailImpl> getAll();
	
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

	List<UserDetailImpl> getAllFromCacheOnly();
}
