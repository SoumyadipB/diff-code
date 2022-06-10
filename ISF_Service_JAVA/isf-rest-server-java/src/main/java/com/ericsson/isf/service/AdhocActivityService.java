package com.ericsson.isf.service;

import java.util.List;
import java.util.Map;

import com.ericsson.isf.model.botstore.RpaApiResponse;
import com.ericsson.isf.model.botstore.TblAdhocBooking;
import com.ericsson.isf.model.botstore.TblAdhocBookingActivity;
import com.ericsson.isf.model.botstore.TblAdhocBookingActivityForIcons;
import com.ericsson.isf.model.botstore.TblProjects;

public interface AdhocActivityService {

	public List<TblAdhocBookingActivityForIcons> getAdhocActivities();
	public RpaApiResponse saveAdhocBooking(TblAdhocBooking tblAdhocBooking);
	public List<TblProjects> getProjectsOfUser(String signum);
	public Map<String, String> getAdhocBooking(String signum);
	public boolean saveAdhocBookingForOutlook(TblAdhocBooking tblAdhocBooking);
	public TblAdhocBookingActivity getBookingActivityByActivityAndType(String type,String activity);
	public int updateAdhocActivityActiveByMeetingId(String outlookMeetingId,boolean isActive); 
	public String getInternalActivities();
	public TblAdhocBooking getAdhocBookingForSignum(String signumID);
	public TblAdhocBooking getAdhocBookingForId(int adhocBookingId);
}
