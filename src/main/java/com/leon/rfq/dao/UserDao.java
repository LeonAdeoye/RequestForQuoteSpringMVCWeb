package com.leon.rfq.dao;

import java.util.List;

import com.leon.rfq.domains.UserImpl;

public interface UserDao
{
	boolean delete(String userId);

	boolean insert(String userId,
			String firstName,
			String lastName,
			String emailAddress,
			String locationName,
			String groupName,
			boolean isValid,
			String savedByUser);
	
	boolean update(String userId,
			String firstName,
			String lastName,
			String emailAddress,
			String locationName,
			String groupName,
			boolean isValid,
			String savedByUser);

	boolean updateValidity(String userId, boolean isValid, String updatedByUser);

	List<UserImpl> getAll();
	 
	UserImpl get(String userId);

	boolean userExistsWithEmailAddress(String emailAddress);
	
	boolean userExistsWithUserId(String userId);
}
