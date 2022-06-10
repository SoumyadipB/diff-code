package com.ericsson.isf.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ericsson.isf.exception.ApplicationExceptionHandler;
import com.ericsson.isf.model.botstore.RpaApiResponse;
import com.ericsson.isf.model.botstore.TblAdhocBooking;
import com.ericsson.isf.model.botstore.TblAdhocBookingActivity;
import com.ericsson.isf.model.botstore.TblAdhocBookingActivityForIcons;
import com.ericsson.isf.model.botstore.TblProjects;
import com.ericsson.isf.service.AdhocActivityService;

/**
 *
 * @author esaabeh
 */
@RestController
@RequestMapping("/adhocActivity")
public class AdhocActivityController {
    
    private static final Logger LOG = LoggerFactory.getLogger(ApplicationExceptionHandler.class);
    
    @Autowired
    private AdhocActivityService adhocActivityService;

    @RequestMapping(value = "/getAdhocActivities", method = RequestMethod.GET)
    public List<TblAdhocBookingActivityForIcons> getAdhocActivities() throws IOException, SQLException
    {
    	List<TblAdhocBookingActivityForIcons> actList= null;
    	actList= adhocActivityService.getAdhocActivities();
        
        return actList;
    }

    @RequestMapping(value = "/saveAdhocBooking", method = RequestMethod.POST)
    public RpaApiResponse saveAdhocBooking(@RequestBody TblAdhocBooking tblAdhocBooking) throws IOException, SQLException
    {
    	RpaApiResponse res= new RpaApiResponse();

        if(null == tblAdhocBooking) {
        	res.setApiSuccess(false);
            res.setResponseMsg("Provided data cannot be Null/blank for saving details!!");
        }

        LOG.info("saveAdhocBooking Request: SUCCESS");
    	return adhocActivityService.saveAdhocBooking(tblAdhocBooking);
    }

    @RequestMapping(value = "/getProjectsOfUser/{signum}", method = RequestMethod.GET)
    public List<TblProjects> getProjectsOfUser(@PathVariable("signum") String signum) throws IOException, SQLException
    {
    	List<TblProjects> projList= null;
    	projList= adhocActivityService.getProjectsOfUser(signum);
        
        return projList;
    }

    @RequestMapping(value = "/getAdhocBooking/{signum}", method = RequestMethod.GET)
    public Map<String, String> getAdhocBooking(@PathVariable("signum") String signum) throws IOException, SQLException
    {
    	return adhocActivityService.getAdhocBooking(signum);
    }
    
    @RequestMapping(value = "/getInternalActivities", method = RequestMethod.GET)
    public String getInternalActivities() throws IOException, SQLException
    {
    	return adhocActivityService.getInternalActivities();
    }

}
