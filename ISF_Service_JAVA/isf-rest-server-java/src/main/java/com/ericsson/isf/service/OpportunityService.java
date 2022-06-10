/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.service;

import com.ericsson.isf.dao.OpportunityDao;
import com.ericsson.isf.exception.ApplicationException;
import com.ericsson.isf.model.OpportunityModel;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author eabhmoj
 */
@Service
public class OpportunityService {
    
    @Autowired
    private OpportunityDao opportunityDao;

    /* //TODO code cleanup 
     public List<OpportunityModel> getOpportunityDetails() {
        List<OpportunityModel> opportunityDetails = new ArrayList<>();
        opportunityDetails = this.opportunityDao.getOpportunityDetails();
        return opportunityDetails;
    }*/
}
