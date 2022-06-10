package com.ericsson.isf.iva.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ericsson.isf.iva.IsfVoiceAssistantApplication;
import com.ericsson.isf.iva.profiles.configuration.AppConfig;

@RefreshScope
@RestController
@RequestMapping("/")
class RestartController {

	@Autowired
	AppConfig appconfig;

	@GetMapping("/getAppMessage")
	public String getAppMessage() {
		return appconfig.getAppMessage();
	}

	@PostMapping("/restart")
	public void restart() {
		IsfVoiceAssistantApplication.restart();
	}
	//TODO runtime properties change
}