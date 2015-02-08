package com.leon.rfq.controllers;

import java.util.List;

import com.leon.rfq.user.UserDao;
import com.leon.rfq.user.UserImpl;

public interface UserController
{
	void setUserDao(UserDao userDao);
	
	UserImpl get(String userId);

	List<UserImpl> getAll();
	
	boolean save(String userId, String firstName, String lastName, String emailAddress,
			String locationName, int groupId, boolean isValid, String savedByUser);
	
	boolean delete(String userId);
	
	boolean updateValidity(String userId, boolean isValid, String updatedByUser);
}