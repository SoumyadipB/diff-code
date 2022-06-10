package com.ericsson.isf.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

public class Value {
	
	public Value(String id, String val) {
		super();
		this.id = id;
		this.val = val;
	}

	private String id;
	private String val;
	
	@XmlValue
	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}

	@XmlAttribute 
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
