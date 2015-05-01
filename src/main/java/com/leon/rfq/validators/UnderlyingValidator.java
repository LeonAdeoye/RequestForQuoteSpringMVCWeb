package com.leon.rfq.validators;

import java.util.Set;

import javax.validation.ConstraintViolation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.leon.rfq.underlying.UnderlyingDetailImpl;
import com.leon.rfq.underlying.UnderlyingService;

@Component
public class UnderlyingValidator implements Validator
{
	@Autowired
	private UnderlyingService underlyingService;
	
	@Autowired
	private javax.validation.Validator beanValidator;
	
	@Override
	public boolean supports(Class<?> clazz)
	{
		return UnderlyingDetailImpl.class.isAssignableFrom(clazz);
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
		
		UnderlyingDetailImpl underlying = (UnderlyingDetailImpl) target;
		
		if(this.underlyingService.underlyingExistsWithRic(underlying.getRic()))
			errors.rejectValue("ric", "underlying.validation.ric.unique");
	}

}
