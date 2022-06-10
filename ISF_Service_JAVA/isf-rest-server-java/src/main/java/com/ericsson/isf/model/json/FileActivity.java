package com.ericsson.isf.model.json;

import java.util.Arrays;

public class FileActivity extends Activity{

	private Observer observer;
	
	public FileActivity(){
		this.observer = new Observer();
	}

	public Observer getObserver() {
		return observer;
	}

	public void setObserver(Observer observer) {
		this.observer = observer;
	}

	@Override
	public String toString() {
		return "FileActivity [observer=" + observer + ", getEvent_module()=" + getEvent_module()
				+ ", getScanner_combinator()=" + getScanner_combinator() + ", getValid_apps()="
				+ Arrays.toString(getValid_apps()) + ", getChild()=" + getChild() + ", getConditions()="
				+ getConditions() + ", toString()=" + super.toString() + ", getClass()=" + getClass() + ", hashCode()="
				+ hashCode() + "]";
	}


}
