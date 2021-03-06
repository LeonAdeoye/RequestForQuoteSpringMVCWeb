package com.leon.rfq.mappers;

import java.util.Set;

import com.leon.rfq.domains.UserDetailImpl;

public interface UserMapper
{
	Set<UserDetailImpl> getAll();
	
	UserDetailImpl get(String userId);
	
	int delete(String userId);
	
	int insert(UserDetailImpl userDetailImpl);
	
	int update(UserDetailImpl userDetailImpl);

	int updateValidity(UserDetailImpl userDetailImpl);
	
	UserDetailImpl userExistsWithEmailAddress(String emailAddress);
	
	UserDetailImpl userExistsWithUserId(String userId);
}
