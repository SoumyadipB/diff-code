package com.ericsson.isf.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

public class EmptyStringValidator implements ConstraintValidator<NotBlankString, String> {

	@Override
	public void initialize(NotBlankString validClassName) {
	}

	@Override
	public boolean isValid(String str, ConstraintValidatorContext context) {

		return StringUtils.isNotBlank(str);

	}

}
