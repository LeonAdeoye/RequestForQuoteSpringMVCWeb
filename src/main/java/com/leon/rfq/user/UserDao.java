package com.leon.rfq.user;

import java.util.List;

public interface UserDao
{
	boolean delete(String userId);

	boolean save(String userId,
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
}
