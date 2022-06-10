package com.ericsson.isf.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class Parameter {
private String optional;
private String type;
private int id;
private String name;
private Answer answer;

public Parameter(String optional, String type, int id, String name, Answer answer) {
	super();
	this.optional = optional;
	this.type = type;
	this.id = id;
	this.name = name;
	this.answer = answer;
}
@XmlAttribute 
public String getOptional() {
	return optional;
}
public void setOptional(String optional) {
	this.optional = optional;
}
@XmlAttribute 
public String getType() {
	return type;
}
public void setType(String type) {
	this.type = type;
}
@XmlElement
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
@XmlElement
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
@XmlElement
public Answer getAnswer() {
	return answer;
}
public void setAnswer(Answer answer) {
	this.answer = answer;
}
}
