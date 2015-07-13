package com.leon.rfq.validators;

import java.util.Set;

import javax.validation.ConstraintViolation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.leon.rfq.domains.SearchCriterionImpl;
import com.leon.rfq.services.SearchService;

@Component
public class SearchValidatorImpl implements Validator
{
	@Autowired
	private SearchService searchService;
	
	@Autowired
	private javax.validation.Validator beanValidator;
	
	@Override
	public boolean supports(Class<?> clazz)
	{
		return SearchCriterionImpl.class.isAssignableFrom(clazz);
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
		
		SearchCriterionImpl search = (SearchCriterionImpl) target;
		// TODO
	}
}
