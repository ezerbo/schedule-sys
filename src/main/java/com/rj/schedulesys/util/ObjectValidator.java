package com.rj.schedulesys.util;

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
	
	
	/**
	 * @param objectToBeValidated
	 * @throws RuntimeException : When a validation error occurs
	 */
	public void validate(T objectToBeValidated){
		
		Validator validator = validatorFactory.getValidator();
		 
		StringBuilder message  = new StringBuilder();
		try{
			Set<ConstraintViolation<T>> constraintViolations = validator.validate(objectToBeValidated);
			if(!constraintViolations.isEmpty()){
				for (ConstraintViolation<T> constraintViolation : constraintViolations) {
					message.append(constraintViolation.getPropertyPath())
							.append(" ")
							.append(constraintViolation.getMessage())
							.append(",");
				}
				
				log.debug("{} is not valid due to {} ", objectToBeValidated, message);
				
				throw new RuntimeException(message.toString());
			}
		}catch(ValidationException e){
			log.debug("{} occured while validating {} ",e.getMessage(), objectToBeValidated);
			throw new RuntimeException(e.getMessage());
		}
	}
}
