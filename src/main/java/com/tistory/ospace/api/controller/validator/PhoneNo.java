package com.tistory.ospace.api.controller.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;


@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneNoValidator.class)
@Documented
public @interface PhoneNo {
	String message() default "PhoneNo is invalid.";
	
	Class<?>[] groups() default { };
	
	Class<? extends Payload>[] payload() default { };
	
	@Target({ ElementType.METHOD, ElementType.FIELD })
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@interface List {

		NotNull[] value();
	}
}
