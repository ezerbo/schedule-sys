package com.rj.sys.utils;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ObjectValidator<T> {
	
	private @Autowired ValidatorFactory validatorFactory;
	
	public void validate(T objectToBeValidated){
		Validator validator = validatorFactory.getValidator();
		StringBuilder message  = new StringBuilder();
		try{
			Set<ConstraintViolation<T>> constraintViolations = validator.validate(objectToBeValidated);
			if(!constraintViolations.isEmpty()){
				for (ConstraintViolation<T> constraintViolation : constraintViolations) {
					message.append("'")
							.append(constraintViolation.getPropertyPath().toString()+" : "+constraintViolation.getInvalidValue())
							.append("'")
							.append(constraintViolation.getMessage()+",")
					;
				}
				log.debug("The data {} could not be validated due to {} ", objectToBeValidated.toString(), message);
				throw new RuntimeException(message.toString());
			}
		}catch(ValidationException e){
			log.debug("Errors {} occured while validating {} ",e.getMessage(), objectToBeValidated);
			throw new RuntimeException(e.getMessage());
		}
	}
}
