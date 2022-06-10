package com.ericsson.isf.dao.impl;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ericsson.isf.dao.AccessManagementHibDao;
import com.ericsson.isf.model.botstore.TblAccessRole;
import com.ericsson.isf.model.botstore.TblCapability;
import com.ericsson.isf.model.botstore.TblRoleCapability;
import com.ericsson.isf.model.botstore.TblRoleCapabilityId;

/**
 *
 * @author esaabeh
 */
@Repository
public class AccessManagementHibDaoImpl implements AccessManagementHibDao{

	@Autowired
	SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	public List<TblAccessRole> getAllAccessRoles() {
        return (List<TblAccessRole>) sessionFactory.getCurrentSession().createQuery("from TblAccessRole").getResultList();
    }
	
	@SuppressWarnings("unchecked")
	public void createAccessRole(TblAccessRole accRole) throws HibernateException{
		sessionFactory.getCurrentSession().saveOrUpdate(accRole);
    }

	@SuppressWarnings("unchecked")
	public void createAccessRole(String role, String alias) throws HibernateException{
		sessionFactory.getCurrentSession().createSQLQuery("insert into [refData].[TBL_ACCESS_ROLE] " + 
				"(Role, Alias, Active) values ('"+role+"', '"+alias+"', 1)").executeUpdate();
    }

	@SuppressWarnings("unchecked")
    public List<Object[]> getProjectScopes(String signumID) {
    	return sessionFactory.getCurrentSession()
    			.createSQLQuery("SELECT distinct p.ProjectID , p.ProjectName, pj.ScopeName, pj.ProjectScopeID "
    			+ "FROM [transactionalData].[TBL_PROJECTS] p "
    			+ "join transactionalData.TBL_PROJECTSCOPE pj on pj.ProjectID = p.ProjectID "
    			+ "where IsDeleted ="+0
    			+ " and ( ProjectCreator ='"+signumID+"'"
    			+ " OR CPM ='"+signumID+"'"
    			+ " OR OperationalManager ='"+signumID+"'"
    			+ " ) "
    			+ "union "
    			+ "SELECT distinct dr.ProjectID, po.ProjectName, pj.ScopeName, pj.ProjectScopeID "
    			+ "from [transactionalData].[TBL_DELIVERY_RESPONSIBLE] dr "
    			+ "join [transactionalData].[TBL_PROJECTS] po on dr.ProjectID = po.ProjectID "
    			+ "join transactionalData.TBL_PROJECTSCOPE pj on pj.ProjectID = po.ProjectID "
    			+ "where dr.DeliveryResponsible like '"+ signumID+"'"
    			+ " and po.IsDeleted = "+0
    			+ " and dr.active="+1
    			).list();
    }

    
	@SuppressWarnings("unchecked")
	public List<TblCapability> getAllCapabilities() {
        return (List<TblCapability>) sessionFactory.getCurrentSession().createQuery("from TblCapability").getResultList();
    }

	@SuppressWarnings("unchecked")
	public List<Object[]> getCapabilities() {
        return (List<Object[]>) sessionFactory.getCurrentSession().createSQLQuery("Select [CapabilityPageID],[CapabilityPageGroup],[CapabilityPageName],[Active],[GroupTitle],[SubMenuTitle],[GroupIcon],[SubMenuHref],[OnClick],[GroupHref],[Priority],[GroupOnClick]\r\n" + 
        		" from [refData].[TBL_CAPABILITY] where active=1").list();
    }

	@SuppressWarnings("unchecked")
	public void addCapability(String PageName, String PageGroup, String GroupTitle, String SubMenuTitle, String GroupIcon, String SubMenuHref, String GroupHref, Integer Priority) throws HibernateException{
		sessionFactory.getCurrentSession().createSQLQuery("insert into [refData].[TBL_CAPABILITY] " + 
				"(CapabilityPageName, CapabilityPageGroup, GroupTitle, SubMenuTitle, GroupIcon, SubMenuHref, GroupHref, Priority, Active, OnClick, GroupOnClick) values ('"+PageName+"', '"+PageGroup+"', '"+GroupTitle+"', '"+SubMenuTitle+"', '"+GroupIcon+"', '"+SubMenuHref+"', '"+GroupHref+"',"+Priority+", 1, 1, 1)").executeUpdate();
    }

	@SuppressWarnings("unchecked")
	public void updateCapability(Integer pageId, String PageName, String PageGroup, String GroupTitle, String SubMenuTitle, String GroupIcon, String SubMenuHref, String GroupHref, Integer Priority) throws HibernateException{
		sessionFactory.getCurrentSession().createSQLQuery("UPDATE [refData].[TBL_CAPABILITY] " + 
				"SET CapabilityPageName='"+PageName+"', "
				+"CapabilityPageGroup='"+PageGroup+"', "
				+"GroupTitle='"+GroupTitle+"', "
				+"SubMenuTitle='"+SubMenuTitle+"', "
				+"GroupIcon='"+GroupIcon+"', "
				+"SubMenuHref='"+SubMenuHref+"', "
				+"GroupHref='"+GroupHref+"', "
				+"Priority='"+Priority+"' "
				+"WHERE CapabilityPageID="+pageId).executeUpdate();
    }

	@SuppressWarnings("unchecked")
	public void deleteCapability(Integer pageId) throws HibernateException{
		sessionFactory.getCurrentSession().createSQLQuery("DELETE FROM [refData].[TBL_CAPABILITY]"
				+ " WHERE CapabilityPageID="+pageId).executeUpdate();
    }

	
	@SuppressWarnings("unchecked")
	public List<TblRoleCapability> getAllRoleCapabilities() {
        return (List<TblRoleCapability>) sessionFactory.getCurrentSession().createQuery("from TblRoleCapability").getResultList();
    }
	
	@SuppressWarnings("unchecked")
	public void addRoleCapability(TblRoleCapabilityId cap) throws HibernateException{
		sessionFactory.getCurrentSession().createSQLQuery("insert into [refData].[TBL_ROLE_CAPABILITY] " + 
				"(RoleID, CapabilityPageID, permission, Active) values ('"+cap.getRoleId()+"', '"+cap.getCapabilityPageId()+"', '"+cap.getPermission()+"', 1)").executeUpdate();
    }

	@SuppressWarnings("unchecked")
	public void updateRoleCapability(TblRoleCapabilityId cap) throws HibernateException{
		sessionFactory.getCurrentSession().createSQLQuery("UPDATE [refData].[TBL_ROLE_CAPABILITY] " + 
				"SET RoleID='"+cap.getRoleId()+"', "
				+"CapabilityPageID='"+cap.getCapabilityPageId()+"', "
				+"permission='"+cap.getPermission()+"' "
				+"WHERE RoleCapabilityID="+cap.getRoleCapabilityId()).executeUpdate();
    }

	@SuppressWarnings("unchecked")
	public int checkRoleCapability(TblRoleCapabilityId cap) {
		
		return	(int) sessionFactory.getCurrentSession().createSQLQuery("SELECT count(*) FROM [refData].[TBL_ROLE_CAPABILITY] " + 
				"WHERE RoleCapabilityID="+cap.getRoleCapabilityId()+
				" AND RoleID="+cap.getRoleId()+
				" AND CapabilityPageID="+cap.getCapabilityPageId()).uniqueResult();
	}
}
