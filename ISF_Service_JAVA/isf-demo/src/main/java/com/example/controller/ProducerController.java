/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.service.ExternalInterfaceManagmentService;


@RestController
@RequestMapping("/externalInterface")
public class ProducerController {
	@Autowired
	private ExternalInterfaceManagmentService eriSiteManagmentService;
	
    @RequestMapping("/checkIsfHealth")
    public List<Map<String,Object>> checkIsfHealth() {
        return eriSiteManagmentService.checkIsfHealth();
    }
}
