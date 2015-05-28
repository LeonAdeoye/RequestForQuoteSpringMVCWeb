package com.leon.rfq.validators;

import java.util.Set;

import javax.validation.ConstraintViolation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.leon.rfq.domains.ClientDetailImpl;
import com.leon.rfq.services.ClientService;

@Component
public class ClientValidator implements Validator
{
	@Autowired
	private ClientService clientService;
	
	@Autowired
	private javax.validation.Validator beanValidator;
	
	@Override
	public boolean supports(Class<?> clazz)
	{
		return ClientDetailImpl.class.isAssignableFrom(clazz);
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
		
		ClientDetailImpl client = (ClientDetailImpl) target;
		
		if(this.clientService.clientExistsWithClientName(client.getName()))
			errors.rejectValue("clientId", "client.validation.clientName.unique");
	}

}
