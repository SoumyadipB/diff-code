package com.ericsson.isf.model.json;

import java.util.Arrays;

public class AppActivity extends Activity{
	
	private String trigger_on_close;
	
	public String getTrigger_on_close() {
		return trigger_on_close;
	}

	public void setTrigger_on_close(String trigger_on_close) {
		this.trigger_on_close = trigger_on_close;
	}

	@Override
	public String toString() {
		return "AppActivity [getEvent_module()=" + getEvent_module() + ", getScanner_combinator()="
				+ getScanner_combinator() + ", getValid_apps()=" + Arrays.toString(getValid_apps())
				+ ", getTrigger_on_close()=" + getTrigger_on_close() + ", getChild()=" + getChild() + ", getConditions()="
				+ getConditions() + ", toString()=" + super.toString() + ", getClass()=" + getClass() + ", hashCode()="
				+ hashCode() + "]";
	}

	
	
}
