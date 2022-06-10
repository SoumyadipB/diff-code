/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.service;

import com.ericsson.isf.dao.ActivityScopeDao;
import com.ericsson.isf.model.ActivityScopeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author eabhmoj
 */
@Service
public class ActivityScopeService {
    @Autowired
    ActivityScopeDao activityScopeDao;
     public int saveActivityScope(ActivityScopeModel activityScopeModel)
     {
         return activityScopeDao.saveActivityScope(activityScopeModel);
     }
    /* //TODO code cleanup
     *  public void DeleteActivityScope(int activityScopeId,String lastModifiedBy)
     {
         this.activityScopeDao.DeleteActivityScope(activityScopeId,lastModifiedBy);
     }
     */
     public boolean checkDTRCASubActivityId(Integer subActivityId) {
    	return activityScopeDao.checkDTRCASubActivityId(subActivityId);
     }
}

