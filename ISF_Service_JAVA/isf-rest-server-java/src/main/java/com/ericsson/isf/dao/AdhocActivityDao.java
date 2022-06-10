package com.ericsson.isf.dao;

import java.util.List;

import org.hibernate.HibernateException;

import com.ericsson.isf.model.HibernateDateDB;
import com.ericsson.isf.model.botstore.TblAdhocBooking;
import com.ericsson.isf.model.botstore.TblAdhocBookingActivity;

public interface AdhocActivityDao {

	public List<Object[]> getAdhocActivities();
	public void saveAdhocBooking(TblAdhocBooking tblAdhocBooking) throws HibernateException;
	
	public List<Object[]> getProjectsOfUser(String signum);

	public TblAdhocBooking getAdhocBooking(int bookingId);

	public TblAdhocBooking getAdhocBookingForSignum(String signum);

	public TblAdhocBookingActivity getAdhocBookingAct(int id);

	public TblAdhocBookingActivity getBookingActivityByActivityAndType(String type,String activity);
	
	public int updateAdhocActivityActiveByMeetingId(String outlookMeetingId,boolean isActive); 
	
	public String getInternalActivities();
	
	public HibernateDateDB getSystemDate();
	
	public TblAdhocBooking getAdhocBookingForId(int adhocBookingId);
}
