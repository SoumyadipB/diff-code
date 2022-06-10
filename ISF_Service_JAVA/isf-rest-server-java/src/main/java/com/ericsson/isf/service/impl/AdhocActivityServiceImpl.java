package com.ericsson.isf.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.PropertyValueException;
import org.hibernate.SessionFactory;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ericsson.isf.config.ApplicationConfigurations;
import com.ericsson.isf.dao.AdhocActivityDao;
import com.ericsson.isf.model.HibernateDateDB;
import com.ericsson.isf.model.SignalrModel;
import com.ericsson.isf.model.botstore.RpaApiResponse;
import com.ericsson.isf.model.botstore.TblAdhocBooking;
import com.ericsson.isf.model.botstore.TblAdhocBookingActivity;
import com.ericsson.isf.model.botstore.TblAdhocBookingActivityForIcons;
import com.ericsson.isf.model.botstore.TblProjects;
import com.ericsson.isf.service.ActivityMasterService;
import com.ericsson.isf.service.AdhocActivityService;
import com.ericsson.isf.service.AppService;
import com.ericsson.isf.service.ValidationUtilityService;
import com.ericsson.isf.service.WOExecutionService;
import com.ericsson.isf.util.AppConstants;
import com.ericsson.isf.util.AppUtil;
import com.ericsson.isf.util.ConfigurationFilesConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import org.json.JSONObject;
/**
 *
 * @author esaabeh
 */

@Service
@Transactional("txManager")
public class AdhocActivityServiceImpl implements AdhocActivityService{

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(AdhocActivityServiceImpl.class);

    @Autowired
    private AdhocActivityDao adhocActivityDao;
    
	@Autowired
	private ApplicationConfigurations configurations;
	
	@Autowired
	private WOExecutionService woExecutionService;
	
	@Autowired
	private ActivityMasterService activityMasterService;
    
	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	private AppService appService;
	
	@Autowired
	private ValidationUtilityService validationUtilityService;
	
	private static final String FORMAT="%02d:%02d:%02d";


	@Override
	public List<TblAdhocBookingActivityForIcons> getAdhocActivities() {
		
    	List<TblAdhocBookingActivityForIcons> actListUi= new ArrayList<TblAdhocBookingActivityForIcons>();
    	TblAdhocBookingActivityForIcons tblAdhocBookingActivityUi= null;

    	List<Object[]> data= adhocActivityDao.getAdhocActivities();
    	for(Object[] st: data)
    	{
    		tblAdhocBookingActivityUi= new TblAdhocBookingActivityForIcons();
  
    		tblAdhocBookingActivityUi.setActivity((String)st[0]);
    		tblAdhocBookingActivityUi.setName((String)st[1]);
    		tblAdhocBookingActivityUi.setAbbreviation((String)st[2]);
    		tblAdhocBookingActivityUi.setSequence((Byte)st[3]);
    		
    		actListUi.add(tblAdhocBookingActivityUi);
    	}

		return actListUi;
	}
	
    @Transactional("txManager")
    public RpaApiResponse saveAdhocBooking(TblAdhocBooking tblAdhocBooking) {
    	RpaApiResponse res= new RpaApiResponse();
    	try {
    		//converted to jsonString 
    		String finalRuleMapString = AppUtil.convertClassObjectToJson(tblAdhocBooking);
    		//converted to jsonstring to jsonobject
    		JSONObject newObject = new JSONObject(finalRuleMapString);
    		
    		if(tblAdhocBooking.getTblProjects()!=null) 
    		{
    			if( tblAdhocBooking.getTblProjects().getProjectId()==0) 
    	    	{
    	    		 ObjectMapper objectMapper = new ObjectMapper();
    		    	 newObject.remove("tblProjects"); 
    		    	 tblAdhocBooking = objectMapper.readValue(newObject.toString(), TblAdhocBooking.class);	
    				}
    		}
    	 }
			catch ( IOException e) {
				e.printStackTrace();
			}
          
    	
//    	TblAdhocBooking adBooking= this.getAdhocBookingForSignum(tblAdhocBooking.getSignumID());
    	TblAdhocBooking adBooking= activityMasterService.getAdhocBookingForSignum(tblAdhocBooking.getSignumID(),  tblAdhocBooking.getStatus());
    	
    	TblAdhocBooking adBookingForId= this.getAdhocBookingForId(tblAdhocBooking.getAdhocBookingId());
    	
    	TblAdhocBooking booking= null;
    	long diffInMillies=0L;
    	String duration= null;
    	try
    	{
    		if(adBooking != null && adBooking.getStatus().equalsIgnoreCase("STARTED") && !tblAdhocBooking.getComment().equalsIgnoreCase("STOP"))
    		{
			    LOG.info("Adhoc is already Running !!!");
            	res.setApiSuccess(false);
            	res.setResponseMsg("Adhoc is already Running !!!");
            	return res;
    		}
    		else if(adBookingForId != null 
    				&& adBookingForId.getStatus().equalsIgnoreCase("COMPLETED") 
    				&& tblAdhocBooking.getComment().equalsIgnoreCase("STOP")
    				)
    		{
			    LOG.info("Adhoc task is already stopped from floating window !");
            	res.setApiSuccess(false);
            	res.setResponseMsg("Adhoc task is already stopped from floating window !");
            	return res;
    		}
    		else
    		{
        		if(tblAdhocBooking.getAdhocBookingId() > 0)
        		{
        			HibernateDateDB currDbTime = adhocActivityDao.getSystemDate();
        			
        			booking= adhocActivityDao.getAdhocBooking(tblAdhocBooking.getAdhocBookingId());
        			if(tblAdhocBooking.getBookedDuration() != null && tblAdhocBooking.getBookedDuration().equalsIgnoreCase("planned"))
        			{
        				diffInMillies = Math.abs(booking.getPlannedEndDate().getTime() - booking.getStartDate().getTime());
        				duration= String.format(FORMAT, 
        			    		TimeUnit.MILLISECONDS.toHours(diffInMillies),
        			    		TimeUnit.MILLISECONDS.toMinutes(diffInMillies) -  
        			    		TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(diffInMillies)), // The change is in this line
        			    		TimeUnit.MILLISECONDS.toSeconds(diffInMillies) - 
        			    		TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(diffInMillies)));
        			    LOG.info("Planned Date diff : {}", duration);
        			    booking.setPlannedDuration(duration);
        			    booking.setBookedDuration(duration);
        			}
        			else if(tblAdhocBooking.getBookedDuration() != null && tblAdhocBooking.getBookedDuration().equalsIgnoreCase("actual"))
        			{
        				diffInMillies = Math.abs(currDbTime.getDate().getTime() - booking.getStartDate().getTime());
        				duration= String.format(FORMAT, 
        			    		TimeUnit.MILLISECONDS.toHours(diffInMillies),
        			    		TimeUnit.MILLISECONDS.toMinutes(diffInMillies) -  
        			    		TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(diffInMillies)), // The change is in this line
        			    		TimeUnit.MILLISECONDS.toSeconds(diffInMillies) - 
        			    		TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(diffInMillies)));
        			    LOG.info("Actual Date diff : {}", duration);
        			    booking.setBookedDuration(duration);
        			}
        			booking.setStatus("COMPLETED");
        			booking.setComment(tblAdhocBooking.getComment());
    			    booking.setActualEndDate(currDbTime.getDate());
        			booking.setLastModifiedOn(currDbTime.getDate());
        			booking.setLastModifiedBy(tblAdhocBooking.getLastModifiedBy());
                	
                	adhocActivityDao.saveAdhocBooking(booking);
                	
                	LOG.info("Adhoc Booking Updated Successfully for Adhoc Booking id : {}", tblAdhocBooking.getAdhocBookingId());
                	res.setData(String.valueOf(tblAdhocBooking.getAdhocBookingId()));
                	res.setApiSuccess(true);
                	res.setResponseMsg("Adhoc Booking Updated Successfully for Adhoc Booking id : "+ tblAdhocBooking.getAdhocBookingId());
        			
                	if(StringUtils.equalsIgnoreCase(AppConstants.DESKTOP,tblAdhocBooking.getRefferer())) {
                		Map<String, Object> obj = new HashMap<>();
                		obj.put("SignumId",tblAdhocBooking.getLastModifiedBy());
            			SignalrModel signalRModel = adhocSignalrConfiguration(obj);
            			appService.callSignalrApplicationToCallSignalRHub(signalRModel);
                	}
        		
        		}
        		
        		else
        		{
        			HibernateDateDB currDbTime = adhocActivityDao.getSystemDate();
        			
        			TblAdhocBookingActivity bookingAct= adhocActivityDao.getAdhocBookingAct(tblAdhocBooking.getTblAdhocBookingActivity().getAdhocBookingActivityId());
            		tblAdhocBooking.setStartDate(currDbTime.getDate());
            		tblAdhocBooking.setCreatedOn(currDbTime.getDate());
            		tblAdhocBooking.setLastModifiedOn(currDbTime.getDate());
                	
    				diffInMillies= Math.abs(tblAdhocBooking.getPlannedEndDate().getTime() - currDbTime.getDate().getTime());
    				duration= String.format(FORMAT, 
    			    		TimeUnit.MILLISECONDS.toHours(diffInMillies),
    			    		TimeUnit.MILLISECONDS.toMinutes(diffInMillies) -  
    			    		TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(diffInMillies)), // The change is in this line
    			    		TimeUnit.MILLISECONDS.toSeconds(diffInMillies) - 
    			    		TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(diffInMillies)));
    				
    				tblAdhocBooking.setPlannedDuration(duration);
    				tblAdhocBooking.setActive(true);
    				
    				tblAdhocBooking.setTblAdhocBookingActivity(bookingAct);
    				adhocActivityDao.saveAdhocBooking(tblAdhocBooking);

    				//Set extra response data for UI-->
    				Map<String, String> dataMap= new HashMap<>();
    		    	String plannedDuration= null;
    		    	String actualDuration= null;
    				diffInMillies = Math.abs(tblAdhocBooking.getPlannedEndDate().getTime() - tblAdhocBooking.getStartDate().getTime());
    				plannedDuration= diffInMillies/1000+"";
    				diffInMillies = Math.abs(currDbTime.getDate().getTime() - tblAdhocBooking.getStartDate().getTime());
    				actualDuration= diffInMillies/1000+"";

    				String act= bookingAct.getActivity();
    				String type= bookingAct.getType();
    		    	dataMap.put("id", tblAdhocBooking.getAdhocBookingId()+"");
    				dataMap.put("actType", act+"-"+type);
    				dataMap.put("plannedDuration", plannedDuration);
    				dataMap.put("actualDuration", actualDuration);

                	LOG.info("Adhoc Booking Created Successfully. Adhoc Booking id is : {}", tblAdhocBooking.getAdhocBookingId());
                	res.setApiSuccess(true);
                	res.setData(tblAdhocBooking.getAdhocBookingId()+"");
                	res.setDataMap(dataMap);
                	res.setResponseMsg("Adhoc Booking Created Successfully. Adhoc Booking id is : "+ tblAdhocBooking.getAdhocBookingId());
                	
                	if(StringUtils.equalsIgnoreCase(AppConstants.DESKTOP,tblAdhocBooking.getRefferer())) {
                		Map<String, Object> obj = new HashMap<>();
                		obj.put("SignumId",tblAdhocBooking.getSignumID());
            			SignalrModel signalRModel = adhocSignalrConfiguration(obj);
            			appService.callSignalrApplicationToCallSignalRHub(signalRModel);
                	}

        		}
    		}
    		
		} 
    	catch (PropertyValueException he) {
			LOG.info(AppConstants.ERROR_CREATING_ADHOC_BOOKING,he.getMessage());
        	res.setApiSuccess(false);
            res.setResponseMsg(String.format(AppConstants.ERROR_CREATING_ADHOC_BOOKING, he.getMessage()));
		}
    	catch (HibernateException he) {
			LOG.info(AppConstants.ERROR_CREATING_ADHOC_BOOKING,he.getMessage());
        	res.setApiSuccess(false);
            res.setResponseMsg(String.format(AppConstants.ERROR_CREATING_ADHOC_BOOKING, he.getMessage()));
		}
    	catch (Exception e) {
			LOG.info(AppConstants.ERROR_CREATING_ADHOC_BOOKING,e.getMessage());
        	res.setApiSuccess(false);
            res.setResponseMsg(String.format(AppConstants.ERROR_CREATING_ADHOC_BOOKING, e.getMessage()));
		}
    	
    	return res;
    }

    public TblAdhocBooking getAdhocBookingForId(int adhocBookingId) {
		return adhocActivityDao.getAdhocBookingForId(adhocBookingId);
	}

	public TblAdhocBooking getAdhocBookingForSignum(String signumID) {
		return adhocActivityDao.getAdhocBookingForSignum(signumID);
	}

	public SignalrModel adhocSignalrConfiguration(Map<String,Object> signumObj) {
		SignalrModel signalRModel = new SignalrModel();
		signalRModel.setHubName(configurations.getStringProperty(ConfigurationFilesConstants.HUB_NAME));
		//signalRModel.setHubUrl(configurations.getStringProperty(ConfigurationFilesConstants.HUB_URL));
		//signalRModel.setHubUrl(validationUtilityService.getCurrentEnvironment("BaseUrl"));
		signalRModel.setHubUrl(System.getenv("CONFIG_BASE_URL"));
		signalRModel.setMethodName(configurations.getStringProperty(ConfigurationFilesConstants.ADHOC_HUB_METHOD_NAME));
		signalRModel.setExecutionType(AppConstants.SIGNALR_EXECUTION_TYPE);
		signalRModel.setPayload(signumObj);
		return signalRModel;
	}

    
	@Override
	public List<TblProjects> getProjectsOfUser(String signum) {
		
    	List<TblProjects> projListUi= new ArrayList<TblProjects>();
    	TblProjects tblProjectsUi= null;

    	List<Object[]> data= adhocActivityDao.getProjectsOfUser(signum);
    	for(Object[] proj: data)
    	{
    		tblProjectsUi= new TblProjects();
    		
    		tblProjectsUi.setProjectId((Integer)proj[0]);
    		tblProjectsUi.setProjectName((String)proj[1]);
    		
    		projListUi.add(tblProjectsUi);
    	}

		return projListUi;
	}

	@Override
	public Map<String, String> getAdhocBooking(String signum) {
		Map<String, String> dataMap= new HashMap<String, String>();
		TblAdhocBooking booking= adhocActivityDao.getAdhocBookingForSignum(signum);
    	long diffInMillies;
    	String plannedDuration= null;
    	String actualDuration= null;
		if(booking != null)
		{
			diffInMillies = Math.abs(booking.getPlannedEndDate().getTime() - booking.getStartDate().getTime());
			plannedDuration= diffInMillies/1000+"";
			diffInMillies = Math.abs(new Date().getTime() - booking.getStartDate().getTime());
			actualDuration= diffInMillies/1000+"";
			
			dataMap.put("id", booking.getAdhocBookingId()+"");
			dataMap.put("actType", booking.getTblAdhocBookingActivity().getActivity()+"-"+booking.getTblAdhocBookingActivity().getType());
			dataMap.put("plannedDuration", plannedDuration);
			dataMap.put("actualDuration", actualDuration);
		}
		return dataMap;
	}

    @Transactional("txManager")
    public boolean saveAdhocBookingForOutlook(TblAdhocBooking tblAdhocBooking) {
    	long diffInMillies;
    	String duration= null;
    	diffInMillies = Math.abs(tblAdhocBooking.getActualEndDate().getTime() - tblAdhocBooking.getStartDate().getTime());
		duration= String.format(FORMAT, 
	    		TimeUnit.MILLISECONDS.toHours(diffInMillies),
	    		TimeUnit.MILLISECONDS.toMinutes(diffInMillies) -  
	    		TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(diffInMillies)), // The change is in this line
	    		TimeUnit.MILLISECONDS.toSeconds(diffInMillies) - 
	    		TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(diffInMillies)));
		tblAdhocBooking.setBookedDuration(duration);
    	adhocActivityDao.saveAdhocBooking(tblAdhocBooking);
    	return true;
    }

    public TblAdhocBookingActivity getBookingActivityByActivityAndType(String type,String activity) {
    	return adhocActivityDao.getBookingActivityByActivityAndType(type, activity);
    }
    
    public int updateAdhocActivityActiveByMeetingId(String outlookMeetingId,boolean isActive){
    	return adhocActivityDao.updateAdhocActivityActiveByMeetingId(outlookMeetingId,isActive);
    }
    public String getInternalActivities(){
    	return adhocActivityDao.getInternalActivities();
    }
    
}
