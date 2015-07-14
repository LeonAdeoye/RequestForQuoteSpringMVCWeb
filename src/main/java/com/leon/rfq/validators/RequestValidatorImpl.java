package com.leon.rfq.validators;

import java.util.Set;

import javax.validation.ConstraintViolation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.leon.rfq.domains.RequestDetailImpl;
import com.leon.rfq.domains.SearchCriterionImpl;
import com.leon.rfq.products.OptionRequestFactory;
import com.leon.rfq.products.OptionRequestFactoryImpl;
import com.leon.rfq.services.RequestService;

@Component
public class RequestValidatorImpl implements Validator
{
	@Autowired(required=true)
	private RequestService requestService;
	
	@Autowired(required=true)
	private OptionRequestFactory factory;
	
	@Autowired(required=true)
	private javax.validation.Validator beanValidator;
	
	@Override
	public boolean supports(Class<?> clazz)
	{
		return RequestDetailImpl.class.isAssignableFrom(clazz) || SearchCriterionImpl.class.isAssignableFrom(clazz);
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
		
		if(target instanceof RequestDetailImpl)
		{
			RequestDetailImpl request = (RequestDetailImpl) target;
			
			if(!OptionRequestFactoryImpl.isValidOptionRequestSnippet(request.getRequest()))
			{
				errors.rejectValue("request", "request.validation.snippet.pattern");
				return;
			}
			
			if(!this.factory.doesUnderlyingExist(request.getRequest()))
				errors.rejectValue("request", "request.validation.underlying.absent");
		}
	}
}
