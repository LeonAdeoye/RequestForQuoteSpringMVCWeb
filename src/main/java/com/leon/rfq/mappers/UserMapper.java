package com.leon.rfq.mappers;

import java.util.List;

import com.leon.rfq.user.UserImpl;

public interface UserMapper
{
	List<UserImpl> getAll();
	
	UserImpl get(String userId);
	
	int delete(String userId);
	
	int insert(UserImpl userImpl);
	
	int update(UserImpl userImpl);

	int updateValidity(UserImpl userImpl);
	
	UserImpl userExistsWithEmailAddress(String emailAddress);
	
	UserImpl userExistsWithUserId(String userId);
}
