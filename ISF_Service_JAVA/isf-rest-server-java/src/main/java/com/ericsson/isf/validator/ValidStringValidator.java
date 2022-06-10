package com.ericsson.isf.validator;

import java.util.Arrays;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

public class ValidStringValidator implements ConstraintValidator<ValidString, String> {

	private boolean isRequired;
	
	@Override
	public void initialize(ValidString validStringAnnotation) {
		this.isRequired = validStringAnnotation.isRequired();
	}

	@Override
	public boolean isValid(String stringValue, ConstraintValidatorContext context) {

		if (!isRequired && StringUtils.isBlank(stringValue)) {
				return true;
		}
		stringValue = StringUtils.trimToEmpty(stringValue);
		if (StringUtils.isBlank(stringValue)) {
			return false;
		}

		String emptyStringArray[] = new String[30];
		Arrays.fill(emptyStringArray, StringUtils.EMPTY);

		stringValue = StringUtils
				.replaceEach(stringValue,
						new String[] { ",", ";", "/", "-", "_", "!", "@", "$", "#", "^", "&", "(", ")", ":", "'", "\"",
								"?", ">", "<", "?", "/", "=", "+", "`", "~", "{", "}", "[", "]", "|" },
						emptyStringArray);

		return StringUtils.isNotBlank(stringValue);

	}

}
