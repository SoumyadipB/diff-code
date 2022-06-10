package com.ericsson.isf.model.json;

public class Observer {

	private String reference;
	private String path;
	private String recursive;

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getRecursive() {
		return recursive;
	}

	public void setRecursive(String recursive) {
		this.recursive = recursive;
	}

	@Override
	public String toString() {
		return "Observer [reference=" + reference + ", path=" + path + ", recursive=" + recursive + ", getClass()="
				+ getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}

}
