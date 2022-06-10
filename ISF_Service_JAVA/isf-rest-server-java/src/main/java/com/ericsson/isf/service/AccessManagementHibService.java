/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.service;


import java.util.List;

import com.ericsson.isf.model.ProjectScope;
import com.ericsson.isf.model.botstore.RpaApiResponse;
import com.ericsson.isf.model.botstore.TblAccessRole;
import com.ericsson.isf.model.botstore.TblCapabilityId;
import com.ericsson.isf.model.botstore.TblRoleCapabilityId;

/**
 *
 * @author esaabeh
 */

public interface AccessManagementHibService {
    
	public List<TblAccessRole> getAllAccessRoles();
	public RpaApiResponse addAccessRole(String role, String alias);
	
	public List<ProjectScope> getProjectScopes(String signumID);
	
	public List<TblCapabilityId> getAllCapabilities();

	public RpaApiResponse addCapability(TblCapabilityId cap);

	public RpaApiResponse deleteCapability(Integer pageId);

	public List<TblRoleCapabilityId> getAllRoleCapabilities();

	public RpaApiResponse addRoleCapability(TblRoleCapabilityId cap);
	public List<TblCapabilityId> getCapabilities();

}
