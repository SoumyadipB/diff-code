package com.ericsson.isf.dao;

import java.util.List;

import org.hibernate.HibernateException;

import com.ericsson.isf.model.ProjectScope;
import com.ericsson.isf.model.botstore.TblAccessRole;
import com.ericsson.isf.model.botstore.TblCapability;
import com.ericsson.isf.model.botstore.TblCapabilityId;
import com.ericsson.isf.model.botstore.TblRoleCapability;
import com.ericsson.isf.model.botstore.TblRoleCapabilityId;

public interface AccessManagementHibDao {

	public List<TblAccessRole> getAllAccessRoles();
	
	public void createAccessRole(TblAccessRole accRole) throws HibernateException;
	public void createAccessRole(String role, String alias) throws HibernateException;

	public List<Object[]> getProjectScopes(String signumID);

	public List<TblCapability> getAllCapabilities();
	public List<Object[]> getCapabilities();

	public void addCapability(String PageName, String PageGroup, String GroupTitle, String SubMenuTitle, String GroupIcon, String SubMenuHref, String GroupHref, Integer Priority) throws HibernateException;
	public void updateCapability(Integer pageId, String PageName, String PageGroup, String GroupTitle, String SubMenuTitle, String GroupIcon, String SubMenuHref, String GroupHref, Integer Priority) throws HibernateException;

	public void deleteCapability(Integer pageId) throws HibernateException;

	public List<TblRoleCapability> getAllRoleCapabilities();

	public void addRoleCapability(TblRoleCapabilityId cap) throws HibernateException;
	public void updateRoleCapability(TblRoleCapabilityId cap) throws HibernateException;


	public int checkRoleCapability(TblRoleCapabilityId cap)throws HibernateException;

}
