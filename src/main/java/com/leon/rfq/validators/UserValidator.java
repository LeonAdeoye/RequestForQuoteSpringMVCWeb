package com.leon.rfq.validators;

import java.util.Set;
import javax.validation.ConstraintViolation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.leon.rfq.user.UserImpl;
import com.leon.rfq.user.UserService;

@Component
public class UserValidator implements Validator
{
	@Autowired
	private UserService userService;
	
	@Autowired
	private javax.validation.Validator beanValidator;
	
	@Override
	public boolean supports(Class<?> clazz)
	{
		return UserImpl.class.isAssignableFrom(clazz);
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
		
		UserImpl user = (UserImpl) target;
		
		if(this.userService.get(user.getUserId()) != null)
		{
			errors.rejectValue("userId", "user.validation.userId.unique");
		}
		
		//TODO
/*		if(userService.get(user.getEmailAddress()) == null)
		{
			errors.rejectValue("emailAddress", "validator.emailAddressUniqueness.message");
		}*/
		
	}

}