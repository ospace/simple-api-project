package com.tistory.ospace.api.controller.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneNoValidator implements ConstraintValidator<PhoneNo, String>{

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		
		if(null == value || value.isEmpty()) return false;
		
		if(11 != value.length()) return false;
		
		return true;
	}

}
