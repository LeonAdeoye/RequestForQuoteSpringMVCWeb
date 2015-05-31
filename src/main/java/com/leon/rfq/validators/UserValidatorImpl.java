package com.leon.rfq.validators;

import java.util.Set;

import javax.validation.ConstraintViolation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.leon.rfq.domains.UserDetailImpl;
import com.leon.rfq.services.UserService;

@Component
public class UserValidatorImpl implements Validator
{
	@Autowired
	private UserService userService;
	
	@Autowired
	private javax.validation.Validator beanValidator;
	
	@Override
	public boolean supports(Class<?> clazz)
	{
		return UserDetailImpl.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors)
	{
		Set<ConstraintViolation<Object>> constraintViolations = this.beanValidator.validate(target);
		
		for(ConstraintViolation<Object> constraintViolation : constraintViolations)
		{
			String propertyPath = constraintViolation.getPropertyPath().toString();
			String message = constraintViolation.getMessage();
			errors.rejectValue(propertyPath, "", message);
		}
		
		UserDetailImpl user = (UserDetailImpl) target;
		
		if(this.userService.userExistsWithUserId(user.getUserId()))
			errors.rejectValue("userId", "user.validation.userId.unique");
		
		if(this.userService.userExistsWithEmailAddress(user.getEmailAddress()))
			errors.rejectValue("emailAddress", "user.validation.emailAddress.unique");
	}

}
