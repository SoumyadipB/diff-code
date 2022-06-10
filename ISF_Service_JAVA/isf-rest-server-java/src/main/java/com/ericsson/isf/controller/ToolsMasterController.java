
package com.ericsson.isf.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.ericsson.isf.exception.ApplicationException;
import com.ericsson.isf.model.ExternalActivitySubactivityModel;
import com.ericsson.isf.model.StandardToolsModel;
import com.ericsson.isf.model.ToolsModel;
import com.ericsson.isf.model.UserFeedbackModel;
import com.ericsson.isf.service.ToolsMasterService;
import com.ericsson.isf.util.AppConstants;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 *
 * @author esanpup
 */
@RestController
@RequestMapping("/toolInventory")
public class ToolsMasterController {
    
	private static final Logger LOG = LoggerFactory.getLogger(ToolsMasterController.class);
    
    @Autowired 
    private ToolsMasterService toolsMasterService;
    
    @RequestMapping(value = "/saveToolInventory", method = RequestMethod.POST)
	public void saveToolInventory(@RequestBody ToolsModel toolModel) {
		this.toolsMasterService.saveToolInventory(toolModel);
                LOG.info(AppConstants.TBL_TOOL_INVENTORY);
	}
        
    @RequestMapping(value = "/updateToolInventory", method = RequestMethod.POST)
	public void updateToolInventory(@RequestBody ToolsModel toolModel) {
		this.toolsMasterService.updateToolInventory(toolModel);
                LOG.info(AppConstants.TBL_TOOL_INVENTORY);
	}
        
    @RequestMapping(value = "/deleteToolInventory/{toolID}/{signumID}/{activeStatus}", method = RequestMethod.GET)
	public void deleteToolInventory(@PathVariable("toolID") int toolID,
                                        @PathVariable("signumID") String signumID,
                                        	@PathVariable("activeStatus") String activeStatus) {
            if(toolID == 0 || (signumID.equalsIgnoreCase("Null") || signumID.equals(""))){
                throw new ApplicationException(500, "Invalid input... ToolID cannot be 0 !!!");
            }
            else{
                this.toolsMasterService.deleteToolInventory(toolID,signumID,activeStatus);
                LOG.info(AppConstants.TBL_TOOL_INVENTORY);
                
            }
	}
        
    
    @RequestMapping(value = "/getToolInventoryDetails", method = RequestMethod.GET)
  	public List<ToolsModel> getToolInventoryDetails(@RequestParam(value="flag",required=false) boolean flag) {
    	if(flag==true){
              List<ToolsModel> toolList= this.toolsMasterService.getToolInventoryDetails();
              return toolList;
    	}
    	else
    	{
    		return this.toolsMasterService.getActiveToolInventoryDetails();
    	}
  	}     
        
    @RequestMapping(value="/getToolInventoryDetailsByID/{toolID}/{signumID}", method=RequestMethod.GET)
        public List<ToolsModel> getToolInventoryDetailsByID(@PathVariable("toolID") int toolID,
                                                            @PathVariable("signumID") String signumID){
            if(toolID !=0){
	            List<ToolsModel> toolListByID= this.toolsMasterService.getToolInventoryDetailsByID(toolID, signumID);
	            return toolListByID;
            }
            else{
                throw new ApplicationException(500, "Invalid input... ToolID cannot be 0 !!!");
            }
    }
        
    @RequestMapping(value = "/getStandardToolDetailsByID/{toolID}", method = RequestMethod.GET)
	public List<StandardToolsModel> getStandardToolDetailsByID(int toolID) {
            List<StandardToolsModel> toolList= this.toolsMasterService.getStandardToolDetailsByID(toolID);
            //LOG.info("TBL_TOOL_INVENTORY: TOOLS LIST: " +toolList);
            return toolList;
	}
    @RequestMapping(value = "/saveUserFeedback", method = RequestMethod.POST , consumes="multipart/form-data")
    @ResponseBody
   	public void saveUserFeedback(@RequestParam("userFeedbackModel") String userFeedbackModel,@RequestParam(value ="userfeedbackfile", required=false) MultipartFile userfeedbackfile) throws IOException {
    	Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonObject object = (JsonObject) parser.parse(userFeedbackModel);// response will be the json String
        UserFeedbackModel userFeedbackModel1= gson.fromJson(object, UserFeedbackModel.class); 
    	this.toolsMasterService.saveUserFeedback(userFeedbackModel1,userfeedbackfile);
                   LOG.info("TBL_User_Feedback : SUCCESS");
   	}
    @RequestMapping(value = "/getUserFeedback", method = RequestMethod.GET)
   	public List<UserFeedbackModel> getUserFeedback() {
               List<UserFeedbackModel> userFeedback= this.toolsMasterService.getUserFeedback();
               return userFeedback;
   	}
    
    @RequestMapping(value = "/saveExternalActivity", method = RequestMethod.POST)
   	public void saveExternalActivity(@RequestBody ExternalActivitySubactivityModel externalActivitySubactivityModel) {
   		this.toolsMasterService.saveExternalActivity(externalActivitySubactivityModel);
                   LOG.info(AppConstants.TBL_EXTERNAL_ACTIVITY_SUBACTIVITY_MAPPING);
   	}
    
    @RequestMapping(value = "/updateExternalActivity", method = RequestMethod.POST)
   	public void updateExternalActivity(@RequestBody ExternalActivitySubactivityModel externalActivitySubactivityModel) {
   		this.toolsMasterService.updateExternalActivity(externalActivitySubactivityModel);
   		LOG.info(AppConstants.TBL_EXTERNAL_ACTIVITY_SUBACTIVITY_MAPPING);
   	}
    
    @RequestMapping(value = "/deleteExternalActivity/{activityID}", method = RequestMethod.GET)
   	public void deleteExternalActivity(@PathVariable("activityID") int activityID) {
               if(activityID == 0){
                   throw new ApplicationException(500, "Invalid input... ActivityID cannot be 0 !!!");
               }
               else{
                   this.toolsMasterService.deleteExternalActivity(activityID);
                   LOG.info(AppConstants.TBL_EXTERNAL_ACTIVITY_SUBACTIVITY_MAPPING);
                   
               }
   	}
    @RequestMapping(value = "/getExternalActivity", method = RequestMethod.GET)
	public List<ExternalActivitySubactivityModel> getExternalActivity() {
            List<ExternalActivitySubactivityModel> externalActivityList= this.toolsMasterService.getExternalActivity();
            return externalActivityList;
	}
    
    @RequestMapping(value = "/getToolType", method = RequestMethod.GET)
   	public List<Map<String, Object>> getToolType() {
    	return this.toolsMasterService.getToolType();
   	}

}
