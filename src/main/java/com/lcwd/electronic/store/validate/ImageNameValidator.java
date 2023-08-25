package com.lcwd.electronic.store.validate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;

public class ImageNameValidator implements ConstraintValidator<ImageNameValid, String> {

	 private Logger logger= org.slf4j.LoggerFactory.getLogger(ImageNameValidator.class);
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		logger.info("message from valid"+value);
		if(value.isBlank()) {
			return false;
		}else {
			return true;
		}
		
	}

}
