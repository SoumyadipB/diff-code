package com.ericsson.isf.controller;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ericsson.isf.model.SignalR;
import com.ericsson.isf.model.SignalrModel;
import com.ericsson.isf.service.SignalRService;

@RestController
@RequestMapping("/signalrService")
public class SignalRController {
	@Autowired
    SignalRService signalRService;
	
	@RequestMapping(value= "/sendSignalrMessage", method= RequestMethod.POST,produces="application/json")
    public SignalR sendAndReceiveMessage(@RequestBody SignalrModel signalrModel) throws InterruptedException, ExecutionException {
		return signalRService.sendAndReceiveMessageToSignalR(signalrModel);
        
        		
    }

}
