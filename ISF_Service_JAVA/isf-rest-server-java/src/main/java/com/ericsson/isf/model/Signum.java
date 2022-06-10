package com.ericsson.isf.model;

import org.apache.commons.lang.StringUtils;

import com.ericsson.isf.exception.ApplicationException;

public class Signum {

	private String signum;

	public Signum(String signum) {
		if (StringUtils.isBlank(signum) || signum.length() < 6 || signum.length() > 7) {
			throw new ApplicationException(500, "Supplied Signum is not valid");
		} else {
			this.signum = signum;
		}
	}
	public String getSignum() {
		return signum;
	}

}
