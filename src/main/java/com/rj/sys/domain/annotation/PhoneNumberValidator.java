package com.rj.sys.domain.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {
	
	private String pattern = "^[0-9]{3}-[0-9]{3}-[0-9]{4}$";
	
	@Override
	public void initialize(PhoneNumber constraintAnnotation) {
	}

	@Override
	public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
		return this.validate(phoneNumber);
	}
	
	private boolean validate(String phoneNumber){
		if(phoneNumber == null){
			return true;
		}
		
		return phoneNumber.matches(pattern);
	}
	
}
