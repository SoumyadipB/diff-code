package com.ericsson.isf.model.json;

public class JSONDataFormat {
	
	
   private String work_order_id="";
   private String step_id="";
   private String  signum_id="";
   private String task_id="";
   private String sub_activity_flowchart_id="";
   private String  override_action="";
	
	private ParentRule rule;

	private Trigger trigger;

	public Trigger getTrigger() {
		return trigger;
	}
	public void setTrigger(Trigger trigger) {
		this.trigger = trigger;
	}
	public ParentRule getRule() {
		return rule;
	}
	public void setRule(ParentRule rule) {
		this.rule = rule;
	}
	public String getWork_order_id() {
		return work_order_id;
	}
	public void setWork_order_id(String work_order_id) {
		this.work_order_id = work_order_id;
	}
	public String getStep_id() {
		return step_id;
	}
	public void setStep_id(String step_id) {
		this.step_id = step_id;
	}
	public String getSignum_id() {
		return signum_id;
	}
	public void setSignum_id(String signum_id) {
		this.signum_id = signum_id;
	}
	public String getTask_id() {
		return task_id;
	}
	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}
	public String getSub_activity_flowchart_id() {
		return sub_activity_flowchart_id;
	}
	public void setSub_activity_flowchart_id(String sub_activity_flowchart_id) {
		this.sub_activity_flowchart_id = sub_activity_flowchart_id;
	}
	public String getOverride_action() {
		return override_action;
	}
	public void setOverride_action(String override_action) {
		this.override_action = override_action;
	}	
}
