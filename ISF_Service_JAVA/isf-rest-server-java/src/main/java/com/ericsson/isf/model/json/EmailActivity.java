package com.ericsson.isf.model.json;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EmailActivity extends Activity{

	@JsonProperty("look_back")
	private String look_back;
	
	@JsonProperty("look_back_time")
	private int look_back_time;

	public String getLook_back() {
		return look_back;
	}

	public void setLook_back(String look_back) {
		this.look_back = look_back;
	}

	public int getLook_back_time() {
		return look_back_time;
	}

	public void setLook_back_time(int look_back_time) {
		this.look_back_time = look_back_time;
	}

	@Override
	public String toString() {
		return "EmailActivity [look_back=" + look_back + ", look_back_time=" + look_back_time + ", getEvent_module()="
				+ getEvent_module() + ", getScanner_combinator()=" + getScanner_combinator() + ", getValid_apps()="
				+ Arrays.toString(getValid_apps()) + ", getChild()=" + getChild() + ", getConditions()="
				+ getConditions() + ", toString()=" + super.toString() + ", getClass()=" + getClass() + ", hashCode()="
				+ hashCode() + "]";
	}


	
}
