package com.leon.rfq.user;

import java.util.List;

public interface UserService
{
	UserImpl get(String userId);

	List<UserImpl> getAll();
	
	boolean save(String userId, String firstName, String lastName, String emailAddress,
			String locationName, String groupName, boolean isValid, String savedByUser);
	
	boolean delete(String userId);
	
	boolean updateValidity(String userId, boolean isValid, String updatedByUser);

	void setUserDao(UserDao userDao);
}