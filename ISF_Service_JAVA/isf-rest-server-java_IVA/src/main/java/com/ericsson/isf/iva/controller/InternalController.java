package com.ericsson.isf.iva.controller;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ericsson.isf.iva.jwt.security.JoseResponseGenerator;
import com.nimbusds.jose.JOSEException;

@RestController
@RequestMapping("/internal")
public class InternalController {

	@Autowired
	private JoseResponseGenerator jwtResponse;
	
	@GetMapping("/parseSignedAndEncrypted")
    public String parseSignedAndEncrypted(@RequestBody String encryptedMessage){

		try {
			String decoded=jwtResponse.parse(encryptedMessage);
			return decoded;
		} catch (ParseException | JOSEException e) {
			e.printStackTrace();
			return "not able to parse";
		}
    }
}
