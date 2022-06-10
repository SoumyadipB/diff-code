package com.ericsson.isf.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ericsson.isf.configServer.ConfigServer;

@RestController
public class RestartController {
     
    @PostMapping("/restart")
    public void restart() {
    	ConfigServer.restart();
    } 
}