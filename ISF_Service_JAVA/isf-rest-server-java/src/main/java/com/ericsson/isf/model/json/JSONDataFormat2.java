package com.ericsson.isf.model.json;

public class JSONDataFormat2 {
	
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
}
