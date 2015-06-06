package com.leon.rfq.repositories;

import java.util.Set;

import com.leon.rfq.domains.UserDetailImpl;

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

	Set<UserDetailImpl> getAll();
	 
	UserDetailImpl get(String userId);

	boolean userExistsWithEmailAddress(String emailAddress);
	
	boolean userExistsWithUserId(String userId);
}
