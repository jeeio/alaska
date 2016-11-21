package io.jee.alaska.validator;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import io.jee.alaska.util.Result;

public class ValidatorUtils {
	
	public static <T> Result<?> validator(T object, Class<?>... groups){
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = validatorFactory.getValidator();
		Set<ConstraintViolation<T>> violations = validator.validate(object, groups);
		for (ConstraintViolation<T> constraintViolation : violations) {
			return Result.error(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage());
		}
		return Result.success();
	}

}
