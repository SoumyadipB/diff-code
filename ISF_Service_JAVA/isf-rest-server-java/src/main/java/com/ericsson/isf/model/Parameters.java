package com.ericsson.isf.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement  
public class Parameters {
private Parameter parameter;

public Parameters() {}  
public Parameters(Parameter parameter) {
	super();
	this.parameter = parameter;
}

@XmlElement  
public Parameter getParameter() {
	return parameter;
}

public void setParameter(Parameter parameter) {
	this.parameter = parameter;
}
}
