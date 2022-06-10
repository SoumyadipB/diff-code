package com.ericsson.isf.model;
public class LinkModel{
		long source;
		long target;
		int type;
		long id;
		public long getSource() {
			return source;
		}
		public void setSource(long source) {
			this.source = source;
		}
		public long getTarget() {
			return target;
		}
		public void setTarget(long target) {
			this.target = target;
		}
		public int getType() {
			return type;
		}
		public void setType(int type) {
			this.type = type;
		}
		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		}
		@Override
		public String toString() {
			return "{\"source\":" + source + ", \"target\":" + target + ",\" type\":" + type + ", \"id\":" + id + "}";
		}
		
		
		
		
		
	}