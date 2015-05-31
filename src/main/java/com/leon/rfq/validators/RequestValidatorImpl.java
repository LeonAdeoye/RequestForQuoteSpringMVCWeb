package com.leon.rfq.validators;

import java.util.Set;

import javax.validation.ConstraintViolation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.leon.rfq.domains.RequestDetailImpl;
import com.leon.rfq.services.RequestService;

@Component
public class RequestValidatorImpl implements Validator
{
	@Autowired
	private RequestService requestService;
	
	@Autowired
	private javax.validation.Validator beanValidator;
	
	@Override
	public boolean supports(Class<?> clazz)
	{
		return RequestDetailImpl.class.isAssignableFrom(clazz);
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
		
		RequestDetailImpl request = (RequestDetailImpl) target;
		// TODO
	}
}
