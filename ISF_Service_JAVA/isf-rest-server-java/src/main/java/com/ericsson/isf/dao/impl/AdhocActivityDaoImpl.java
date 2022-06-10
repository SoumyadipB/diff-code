package com.ericsson.isf.dao.impl;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ericsson.isf.dao.AdhocActivityDao;
import com.ericsson.isf.model.HibernateDateDB;
import com.ericsson.isf.model.botstore.TblAdhocBooking;
import com.ericsson.isf.model.botstore.TblAdhocBookingActivity;

/**
 *
 * @author esaabeh
 */
@Repository
public class AdhocActivityDaoImpl implements AdhocActivityDao{

	@Autowired
	SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	public List<Object[]> getAdhocActivities() {
        return (List<Object[]>) sessionFactory.getCurrentSession()
        		.createSQLQuery("Select distinct act.Activity,act.Name,act.Abbreviation,act.Sequence\r\n" + 
        				"from [refData].[TBL_ADHOC_BOOKING_ACTIVITY] act \r\n" + 
        				"where act.Status = 'Active' and act.Permission='{\"CRUD Permissions\": [\"displayOnWEB\"]}' \r\n" + 
        				"order by Sequence asc").list();
    }

	@SuppressWarnings("unchecked")
	public void saveAdhocBooking(TblAdhocBooking tblAdhocBooking) throws HibernateException{
        sessionFactory.getCurrentSession().saveOrUpdate(tblAdhocBooking);
    }


	@SuppressWarnings("unchecked")
	public List<Object[]> getProjectsOfUser(String signum) {
		return (List<Object[]>) sessionFactory
				.getCurrentSession()
				.createSQLQuery(
						"SELECT distinct p.ProjectID, p.ProjectName FROM transactionalData.TBL_PROJECTS p join transactionalData.TBL_ResourceRequests rr on p.ProjectID = rr.PROJECTID  "
						+ "join transactionalData.TBL_ResourcePosition rp on rr.ResourceRequestID = rp.ResourceRequestID "
						+ "join transactionalData.TBL_WorkEffort wf on wf.ResourcePositionID = rp.ResourcePositionID"
						+ " where wf.Signum='" + signum + "' and rp.PositionStatus in ('Position Completion','Deployed','Resource Allocated') and wf.IsActive = 1 and wf.EndDate >= CONVERT(DATE,dbo.GetDateIST())"
						/*+ "Union "
						+ "SELECT distinct p.ProjectID, p.ProjectName"
        				+ " FROM transactionalData.TBL_WORK_ORDER wo inner join transactionalData.TBL_PROJECTS p on p.ProjectID = wo.PROJECTID"
        				+ " where SignumID='"+signum+"'"*/).list();
	}

	@SuppressWarnings("unchecked")
    public TblAdhocBooking getAdhocBooking(int bookingId) {
        return (TblAdhocBooking) sessionFactory.getCurrentSession()
        		.createQuery("from TblAdhocBooking where adhocBookingId = :bookingId")
        		.setParameter("bookingId", bookingId).getSingleResult();
    }

	@SuppressWarnings("unchecked")
    public TblAdhocBooking getAdhocBookingForSignum(String signum) {
        
		List<TblAdhocBooking > list= sessionFactory.getCurrentSession()
        		.createQuery("from TblAdhocBooking where status = :status and signumID = :signum")
        		.setParameter("status", "Started")
        		.setParameter("signum", signum).getResultList();
		
		if(list.size()>0)
			return list.get(0);
		else
			return null;
    }

	@SuppressWarnings("unchecked")
    public TblAdhocBookingActivity getAdhocBookingAct(int id) {
        return (TblAdhocBookingActivity) sessionFactory.getCurrentSession()
        		.createQuery("from TblAdhocBookingActivity where adhocBookingActivityId = :adhocBookingActivityId")
        		.setParameter("adhocBookingActivityId", id).getSingleResult();
    }
	
	@SuppressWarnings("unchecked")
    public TblAdhocBookingActivity getBookingActivityByActivityAndType(String type,String activity) {
        return (TblAdhocBookingActivity) sessionFactory.getCurrentSession()
        		.createQuery("from TblAdhocBookingActivity where type = :type and activity = :activity ")
        		.setParameter("type", type)
        		.setParameter("activity", activity)
        		.getSingleResult();
    }
	
	@SuppressWarnings("unchecked")
    public String getInternalActivities() {
        return (String) sessionFactory.getCurrentSession()
        		.createQuery("select substring(        						"
        				+ "(select ',' +activity "
        				+"from TblAdhocBookingActivity"
        				+" where type='internal'"
        				+"for xml path('')"
        				+"), 2, 1000"
        				+") as n ")
        		
        		.getSingleResult();
    }
	
	  
	
	
	@SuppressWarnings("unchecked")
    public int updateAdhocActivityActiveByMeetingId(String outlookMeetingId,boolean isActive) {
        return sessionFactory.getCurrentSession()
        		.createQuery("update TblAdhocBooking set active=:active where outlookMeetingId = :outlookMeetingId")
        		.setParameter("active", isActive)
        		.setParameter("outlookMeetingId", outlookMeetingId)
        		.executeUpdate();
    }
	
	

	/**
	 * @return System date on DB server
	 */
	public HibernateDateDB getSystemDate() {
		
	    NativeQuery<HibernateDateDB> query = sessionFactory.getCurrentSession().createNativeQuery(
	            "SELECT dbo.GetDateIST() AS DATE_VALUE", HibernateDateDB.class);
	    HibernateDateDB dateItem = (HibernateDateDB) query.getSingleResult();
	    return dateItem;
	}
	
	@SuppressWarnings("unchecked")
    public TblAdhocBooking getAdhocBookingForId(int adhocBookingId) {
        
		List<TblAdhocBooking > list= sessionFactory.getCurrentSession()
        		.createQuery("from TblAdhocBooking where adhocBookingId = :adhocBookingId")
        		.setParameter("adhocBookingId", adhocBookingId)
        		.getResultList();
		
		if(list.size()>0)
			return list.get(0);
		else
			return null;
    }


}
