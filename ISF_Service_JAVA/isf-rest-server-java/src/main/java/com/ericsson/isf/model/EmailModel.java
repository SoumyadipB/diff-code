/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

/**
 *
 * @author edhhklu
 */
public class EmailModel {

	private String senderSignum;
	private String body;
	private String receiverSignum;
	private String templateName;
	private String templateText;
	private String subject;
	private String to;
	private String cc;
	private String bcc;
	private int id;
	private String status;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getTemplateText() {
		return templateText;
	}

	public void setTemplateText(String templateText) {
		this.templateText = templateText;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getSenderSignum() {
		return senderSignum;
	}

	public void setSenderSignum(String senderSignum) {
		this.senderSignum = senderSignum;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getReceiverSignum() {
		return receiverSignum;
	}

	public void setReceiverSignum(String receiverSignum) {
		this.receiverSignum = receiverSignum;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public EmailModel() {

	}

	public EmailModel(String senderSignum, String body, String receiverSignum, String subject, String to, String cc,String bcc) {
		super();
		this.senderSignum = senderSignum;
		this.body = body;
		this.receiverSignum = receiverSignum;
		this.subject = subject;
		this.to = to;
		this.cc = cc;
		this.bcc = bcc;
	}

	@Override
	public String toString() {
		return "EmailModel [senderSignum=" + senderSignum + ", body=" + body + ", receiverSignum=" + receiverSignum
				+ ", templateName=" + templateName + ", templateText=" + templateText + ", subject=" + subject + ", to="
				+ to + ", cc=" + cc + ", bcc=" + bcc +", id=" + id +"]";
	}

	public String getBcc() {
		return bcc;
	}

	public void setBcc(String bcc) {
		this.bcc = bcc;
	}

}
