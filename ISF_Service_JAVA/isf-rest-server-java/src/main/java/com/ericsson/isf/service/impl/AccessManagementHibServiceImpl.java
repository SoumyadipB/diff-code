package com.ericsson.isf.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ericsson.isf.dao.AccessManagementHibDao;
import com.ericsson.isf.model.Child;
import com.ericsson.isf.model.ProjectScope;
import com.ericsson.isf.model.botstore.RpaApiResponse;
import com.ericsson.isf.model.botstore.TblAccessRole;
import com.ericsson.isf.model.botstore.TblAccessRoleId;
import com.ericsson.isf.model.botstore.TblCapability;
import com.ericsson.isf.model.botstore.TblCapabilityId;
import com.ericsson.isf.model.botstore.TblRoleCapability;
import com.ericsson.isf.model.botstore.TblRoleCapabilityId;
import com.ericsson.isf.service.AccessManagementHibService;
import com.ericsson.isf.util.AppConstants;

/**
 *
 * @author esaabeh
 */

@Service
@Transactional("txManager")
public class AccessManagementHibServiceImpl implements AccessManagementHibService{

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(AccessManagementHibServiceImpl.class);

    @Autowired
    private AccessManagementHibDao accessManagementHibDao;
    
	@Autowired
	SessionFactory sessionFactory;

	public List<TblAccessRole> getAllAccessRoles() {
    	
		List<TblAccessRole> accessRoles= null;
		List<TblAccessRole> accessRolesForClient=null;
		
    	try
    	{
    		accessRoles= accessManagementHibDao.getAllAccessRoles();
    		
            if (accessRoles== null || accessRoles.isEmpty()) {
            	return new ArrayList<>();
            } else {            	
            	accessRolesForClient= new ArrayList<>();
            	TblAccessRole accessRoleForClient=null;
            	TblAccessRoleId roleIdForClient= null; 
            	
            	for(TblAccessRole role: accessRoles)
            	{
            		accessRoleForClient= new TblAccessRole();
            		roleIdForClient= new TblAccessRoleId();
            		
            		roleIdForClient.setAccessRoleId(role.getId().getAccessRoleId());
            		roleIdForClient.setRole(role.getId().getRole());
            		roleIdForClient.setAlias(role.getId().getAlias());
            		roleIdForClient.setActive(role.getId().getActive());
            		
            		accessRoleForClient.setId(roleIdForClient);
            		
            		accessRolesForClient.add(accessRoleForClient);
            	}
            }
    	}
    	catch (Exception e) {
			LOG.info("Error while getAllAccessRoles(): {}",e.getMessage());
			e.printStackTrace();
		}

    	return accessRolesForClient;
    }

    @Transactional("txManager")
    public RpaApiResponse addAccessRole(String role, String alias)
    {
    	RpaApiResponse res= new RpaApiResponse();
    	try
    	{
            LOG.info("Role--Alias:: {} -- {}",role,alias);

    		accessManagementHibDao.createAccessRole(role, alias);
        	res.setApiSuccess(true);
        	res.setResponseMsg("Access Role Created Successfully.");
    	}
    	catch (HibernateException he) {
			LOG.info("Error while Creating Access Role: {}",he.getMessage());
			he.printStackTrace(); 
        	res.setApiSuccess(false);
            res.setResponseMsg("Error in creating Access Role:: "+he.getStackTrace());
		}
    	catch (Exception e) {
			LOG.info("Error while Creating Access Role: {}",e.getMessage());
        	res.setApiSuccess(false);
        	res.setResponseMsg("Error while Creating Access Role: "+e.getMessage());
			e.printStackTrace();
		}
    	
    	return res;
    }


	public List<ProjectScope> getProjectScopes(String signumID) {
    	
		Map<String, List<String>> dataMap= new HashMap<>();
		List<String> list= null;
		
		List<Object[]> projectScopesObj= null;
		List<ProjectScope> projectList= new ArrayList<>();
		ProjectScope project= null;
		List<Child> scopeList=null;
		Child scope=null;
		
    	try
    	{
    		projectScopesObj= accessManagementHibDao.getProjectScopes(signumID);
    	
    		String projIdName=null;
    		String scopeIdName=null;

    		if (projectScopesObj== null || projectScopesObj.isEmpty()) {
            	return new ArrayList<ProjectScope>();
            } else {
            	for(Object[] st: projectScopesObj)
	        	{
	            	projIdName= (Integer)st[0]+"####"+(String)st[1];
	            	scopeIdName= (Integer)st[3]+"####"+(String)st[2];
	        		
	            	if(dataMap.containsKey(projIdName))
	            	{
	            		dataMap.get(projIdName).add(scopeIdName);
	            	}
	            	else
	            	{
	            		list= new ArrayList<String>();
	            		list.add(scopeIdName);
	            		dataMap.put(projIdName, list);
	            	}
	        	}
            	
            	for(Map.Entry<String,List<String>> p: dataMap.entrySet())
            	{
            		project= new ProjectScope();
            		project.setId(Integer.parseInt(p.getKey().split("####")[0]));
            		project.setName(p.getKey().split("####")[1]);

            		scopeList= new ArrayList<Child>();
            		for(String s: dataMap.get(p.getKey()))
            		{
            			scope= new Child();
            			scope.setId(Integer.parseInt(s.split("####")[0]));
            			scope.setName(s.split("####")[1]);
            			
            			scopeList.add(scope);
            		}
            		
            		project.setChildren(scopeList);
            		projectList.add(project);
            	}
            }
    	}
    	catch (Exception e) {
			LOG.info("Error while getAllAccessRoles(): {}",e.getMessage());
			e.printStackTrace();
		}

    	return projectList;
    }

	public List<TblCapabilityId> getAllCapabilities() {
    	
		List<TblCapability> capList= null;
		
		List<TblCapabilityId> capbListClient=null;
		TblCapabilityId capId= null;
		
    	try
    	{
    		capList= accessManagementHibDao.getAllCapabilities();
    		
    		if (capList== null || capList.isEmpty()) {
    			return new ArrayList<TblCapabilityId>();
    		} else {
    			capbListClient= new ArrayList<>();

    			for(TblCapability c: capList)
    			{
    				capId= new TblCapabilityId();

    				if(null!= c)
    				{
    					capId.setCapabilityPageId(c.getId().getCapabilityPageId());
    					capId.setCapabilityPageGroup(c.getId().getCapabilityPageGroup());
    					capId.setCapabilityPageName(c.getId().getCapabilityPageName());
    					capId.setActive(c.getId().getActive());
    					capId.setGroupTitle(c.getId().getGroupTitle());
    					capId.setSubMenuTitle(c.getId().getSubMenuTitle());
    					capId.setGroupIcon(c.getId().getGroupIcon());
    					capId.setSubMenuHref(c.getId().getSubMenuHref());
    					capId.setOnClick(c.getId().getOnClick());
    					capId.setGroupHref(c.getId().getGroupHref());
    					capId.setPriority(c.getId().getPriority());
    					capId.setGroupOnClick(c.getId().getGroupOnClick());

    					capbListClient.add(capId);
    				}
    			}
    		}
    	}
    	catch (Exception e) {
			LOG.info("Error while getAllCapabilities(): "+e.getMessage());
			e.printStackTrace();
		}

    	return capbListClient;
    }
    
    

	public List<TblCapabilityId> getCapabilities() {
    	
		List<TblCapability> capList= null;
		List<Object[]> capbList= null;
		
		List<TblCapabilityId> capbListClient=null;
		TblCapabilityId capId= null;
		
    	try
    	{
    		capbList= accessManagementHibDao.getCapabilities();
    		
            if (capbList== null || capbList.isEmpty()) {
            	return new ArrayList<TblCapabilityId>();
            } else {            	
            	capbListClient= new ArrayList<>();
            	for(Object[] st: capbList)
	        	{
                	capId= new TblCapabilityId();

                	capId.setCapabilityPageId((Integer)st[0]);
                	capId.setCapabilityPageGroup((String)st[1]);
                	capId.setCapabilityPageName((String)st[2]);
                	capId.setActive((Integer)st[3]);
                	capId.setGroupTitle((String)st[4]);
                	capId.setSubMenuTitle((String)st[5]);
                	capId.setGroupIcon((String)st[6]);
                	capId.setSubMenuHref((String)st[7]);
                	capId.setOnClick((Boolean)st[8]);
                	capId.setGroupHref((String)st[9]);
                	capId.setPriority((Integer)st[10]);
                	capId.setGroupOnClick((Boolean)st[11]);
            		
                	capbListClient.add(capId);

	        	}
            }
    	}
    	catch (Exception e) {
			LOG.info("Error while getCapabilities(): "+e.getMessage());
			e.printStackTrace();
		}

    	return capbListClient;
    }

    @Transactional("txManager")
    public RpaApiResponse addCapability(TblCapabilityId cap)
    {
    	RpaApiResponse res= new RpaApiResponse();
    	try
    	{
    		
    		if(cap.getCapabilityPageId()!=0)
    			accessManagementHibDao.updateCapability(cap.getCapabilityPageId(), cap.getCapabilityPageName(), cap.getCapabilityPageGroup(), cap.getGroupTitle(), cap.getSubMenuTitle(), cap.getGroupIcon(), cap.getSubMenuHref(), cap.getGroupHref(), cap.getPriority());
    		else
    			accessManagementHibDao.addCapability(cap.getCapabilityPageName(), cap.getCapabilityPageGroup(), cap.getGroupTitle(), cap.getSubMenuTitle(), cap.getGroupIcon(), cap.getSubMenuHref(), cap.getGroupHref(), cap.getPriority());    			
    		
        	res.setApiSuccess(true);
        	res.setResponseMsg("Capability Updated Successfully.");
			LOG.info("Capability Updated Successfully.");
    	}
    	catch (HibernateException he) {
			LOG.info(AppConstants.ERROR_IN_UPDATING_CAPABILITIES,he.getMessage());
			he.printStackTrace(); 
        	res.setApiSuccess(false);
            res.setResponseMsg(AppConstants.ERROR_IN_UPDATING_CAPABILITIES+he.getStackTrace());
		}
    	catch (Exception e) {
			LOG.info(AppConstants.ERROR_IN_UPDATING_CAPABILITIES,e.getMessage());
        	res.setApiSuccess(false);
        	res.setResponseMsg(AppConstants.ERROR_IN_UPDATING_CAPABILITIES+e.getMessage());
			e.printStackTrace();
		}
    	
    	return res;
    }

    @Transactional("txManager")
    public RpaApiResponse deleteCapability(Integer pageId)
    {
    	RpaApiResponse res= new RpaApiResponse();
    	try
    	{   		
    		accessManagementHibDao.deleteCapability(pageId);
    		
        	res.setApiSuccess(true);
        	res.setResponseMsg("Capability Deleted Successfully.");
			LOG.info("Capability Deleted Successfully.");
    	}
    	catch (HibernateException he) {
			LOG.info("Error while Deleting Capability, may be it is mapped with a Role: {}",he.getMessage());
			he.printStackTrace(); 
        	res.setApiSuccess(false);
            res.setResponseMsg("Cannot delete Capability now as it is mapped with a Role");
		}
    	catch (Exception e) {
			LOG.info("Error while Deleting Capability, may be it is mapped with a Role: {}",e.getMessage());
        	res.setApiSuccess(false);
        	res.setResponseMsg("Cannot delete Capability now as it is mapped with a Role");
			e.printStackTrace();
		}
    	
    	return res;
    }

	public List<TblRoleCapabilityId> getAllRoleCapabilities() {
		List<TblRoleCapability> capList= null;
		List<TblRoleCapabilityId> capbListClient=null;
		TblRoleCapabilityId capId= null;
    	try
    	{
    		capList= accessManagementHibDao.getAllRoleCapabilities();
            if (capList== null || capList.isEmpty()) {
            	return new ArrayList<TblRoleCapabilityId>();
            } else {
            	capbListClient= new ArrayList<TblRoleCapabilityId>();
            	for(TblRoleCapability c: capList)
            	{
                	capId= new TblRoleCapabilityId();
                	if(null!= c)
                	{
                    	capId.setRoleCapabilityId(c.getId().getRoleCapabilityId());
                    	capId.setRoleId(c.getId().getRoleId());
                    	capId.setCapabilityPageId(c.getId().getCapabilityPageId());
                    	capId.setActive(c.getId().getActive());
                    	capId.setPermission(c.getId().getPermission());
                    	capbListClient.add(capId);
                	}
            	}
            }
    	}
    	catch (Exception e) {
			LOG.info("Error while getAllRoleCapabilities(): {}",e.getMessage());
			e.printStackTrace();
		}
    	return capbListClient;
    }

    @Transactional("txManager")
    public RpaApiResponse addRoleCapability(TblRoleCapabilityId cap)
    {
    	RpaApiResponse res= new RpaApiResponse();
    	
    	try
    	{
    		
    		int countRole=0;

    		if( cap.getPermission()!=null && cap.getPermission().trim().length()<=0) {
    				cap.setPermission("none");
    		}
    		if(cap.getRoleCapabilityId()!=0) {
    			countRole=(int) accessManagementHibDao.checkRoleCapability(cap);
    			
    			if(countRole==0) {
		    		LOG.info(AppConstants.ROLEID_AND_CAPABILITY_EXISTS,cap.getRoleCapabilityId());
		        	res.setApiSuccess(false);
		            res.setResponseMsg(AppConstants.ROLEID_AND_CAPABILITY_EXISTS+cap.getRoleCapabilityId());
    			}
    			else {
    				accessManagementHibDao.updateRoleCapability(cap);
    			
    				res.setApiSuccess(true);
    	        	res.setResponseMsg(AppConstants.ROLEID_AND_CAPABILITY_UPDATED);
    				LOG.info(AppConstants.ROLEID_AND_CAPABILITY_UPDATED);
    			}
    		}
    		else {
    			accessManagementHibDao.addRoleCapability(cap); 
    			res.setApiSuccess(true);
            	res.setResponseMsg(AppConstants.ROLEID_AND_CAPABILITY_UPDATED);
    			LOG.info(AppConstants.ROLEID_AND_CAPABILITY_UPDATED);
    		}

    	}
    	catch(ConstraintViolationException ce) {
    		LOG.info(AppConstants.ROLEID_AND_CAPABILITY_EXISTS,ce.getMessage());
    		ce.printStackTrace(); 
        	res.setApiSuccess(false);
            res.setResponseMsg(AppConstants.ROLEID_AND_CAPABILITY_EXISTS);
    	}
    	catch (HibernateException he) {
			LOG.info("Error while Updating Role Capability: {}",he.getMessage());
			he.printStackTrace(); 
        	res.setApiSuccess(false);
            res.setResponseMsg("Error in Updating Role Capability:: "+he.getStackTrace());
		}
    	catch (Exception e) {
			LOG.info("Error while Updating Role Capability: {}",e.getMessage());
        	res.setApiSuccess(false);
        	res.setResponseMsg("Error while Updating Role Capability: "+e.getMessage());
			e.printStackTrace();
		}
    	return res;
    }
}
