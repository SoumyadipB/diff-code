package com.ericsson.isf.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class Answer {
private String constrained;
private String type;
private Values values;
public Answer(String constrained, String type, Values values) {
	super();
	this.constrained = constrained;
	this.type = type;
	this.values = values;
}
@XmlAttribute 
public String getConstrained() {
	return constrained;
}
public void setConstrained(String constrained) {
	this.constrained = constrained;
}
@XmlAttribute 
public String getType() {
	return type;
}
public void setType(String type) {
	this.type = type;
}
@XmlElement
public Values getValues() {
	return values;
}
public void setValues(Values values) {
	this.values = values;
}
}
