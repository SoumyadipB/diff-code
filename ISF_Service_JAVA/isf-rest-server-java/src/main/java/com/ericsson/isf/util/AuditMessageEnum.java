package com.ericsson.isf.util;

public enum AuditMessageEnum {
	DEFAULT("{filedName} changed from {oldValue} to {newValue}"),
	MSG_FIELDNAME_NEWVALUE("{filedName} changed to {newValue}");
	
	private String auditMessage;
	
	AuditMessageEnum(final String auditMessage){
		this.auditMessage = auditMessage;
	}
	
	public String getAuditMessage() {
		return auditMessage;
	}

}
