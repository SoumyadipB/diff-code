/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mapper.ExternalInterfaceManagmentMapper;
/**
 *
 * @author esanpup
 */
@Service
public class ExternalInterfaceManagmentService {
    
	@Autowired
	ExternalInterfaceManagmentMapper externalInterfaceManagmentMapper;
	
	public List<Map<String,Object>> checkIsfHealth() {
		 return externalInterfaceManagmentMapper.checkIsfHealth();
	}
}
