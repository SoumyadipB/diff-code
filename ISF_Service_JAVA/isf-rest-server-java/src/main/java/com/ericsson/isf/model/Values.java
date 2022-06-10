package com.ericsson.isf.model;

import javax.xml.bind.annotation.XmlElement;

public class Values {
	
	public Values(Value value) {
		super();
		this.value = value;
	}

	private Value value;
	@XmlElement
	public Value getValue() {
		return value;
	}

	public void setValue(Value value) {
		this.value = value;
	}
	

}
