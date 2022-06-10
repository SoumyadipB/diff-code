package com.ericsson.isf.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ericsson.isf.dao.FlowChartDAO;
import com.ericsson.isf.exception.ApplicationException;
import com.ericsson.isf.model.DownloadTemplateModel;
import com.ericsson.isf.model.TestProject;
import com.ericsson.isf.service.FlowChartService;
import com.ericsson.isf.service.TestService;

@RestController
@RequestMapping("/test")
public class TestController {
	
	@Autowired   /*Bind to bean/pojo  */
	private TestService testService;
        
        @Autowired   /*Bind to bean/pojo  */
	    private FlowChartService flowChartService;
        
            @Autowired
    private FlowChartDAO flowChartDao;
	
	@RequestMapping(value = "/getProjects/{testParameter1}/{testParameter2}", method = RequestMethod.GET)
	public List<TestProject> getProjects(@PathVariable("testParameter1") Optional<String> testParameter1, 
					     @PathVariable("testParameter2") Optional<String> testParameter2) {
		String str1="";
		String str2="";
		if(testParameter1.isPresent())
			str1 = testParameter1.get();
		if(testParameter2.isPresent())
			str2 = testParameter2.get();
			
		return this.testService.getProjects(str1, str2);
	}
	@RequestMapping(value = "/testException", method = RequestMethod.POST)
	public void updateProject(@RequestBody TestProject testProject) {
		this.testService.updateProject(testProject);
	}
        
//        @RequestMapping(value = "/mapTaskTool/{taskID}/{toolID}", method = RequestMethod.POST)
//	public void mapTaskTool(@PathVariable("taskID") int taskID,
//                                @PathVariable("toolID") int toolID) {
//		this.testService.mapTaskTool(taskID,toolID);
//	}
        
        
        public String generateMailBody(String senderID, String senderName, String receiverID, String receiverName, String status, int woID) {

        String mailBody = "";
        if (woID != 0) {
            mailBody = "<html>\n"
                    + "<head>\n"
                    + "<meta charset=\"UTF-8\">\n"
                    + "<title>ISF Alert</title>\n"
                    + "</head>\n"
                    + "<body style=\"background:#e1e1e1;\">\n"
                    + "<table cellpadding=\"0\" cellspacing=\"0\" width=\"650\" align=\"center\" style=\"background:#fff;border:1px solid #c1c1c1; font-size:12px;font-family:arial; padding: 40px;color:#58595B;\">\n"
                    + "<tr>\n"
                    + "<td colspan=\"3\" style=\"padding-bottom: 0px;border-bottom: 1px solid #c1c1c1;\">\n"
                    + "<img alt=\"Ericsson\" src=\"http://www.ericsson.com/shared/eipa/images/elogo.png\" />\n"
                    + "<p style =\"text-align:right;margin:0px\">ISF ALERTS </p>\n"
                    + "</td> \n"
                    + "</tr> \n"
                    + "<tr>\n"
                    + "<td colspan=\"3\" style=\"padding-bottom: 10px;padding-top: 10px;text-align:left;font-size:14px;\">\n"
                    + "<pre>Dear <b>" + senderName + "</b>,<br> "
                    + "<br>"
                    + "Work Order with <b>WorkOrderID= " + woID + "</b> has been <b> " + status + "</b>  by <b> " + receiverName + "(" + receiverID + ")</b><br>"
                    + "<br>"
                    + "<b>Please refresh your Work Order page to view the updated Work Orders.</b>"
                    + "<br>"
                    + "Delivery Execution --> Planned WorkOrder"
                    + "<br>"
                    + "<br>"
                    + "This is an auto-generated message. Please do not reply.<br>"
                    + "<br>"
                    + "Have a good day.<br>"
                    + "<br>"
                    + "Best Regards,<br>"
                    + "ISF Team<br>"
                    + "</pre><br>"
                    + " </td>\n"
                    + " </tr>\n"
                    + " \n"
                    + " <tr>\n"
                    + " <td colspan=\"3\" style=\"padding-top:10px;padding-left:10px;padding-right:10px;padding-bottom:10px;\">\n"
                    + " &nbsp;<br /><br /> \n"    
                    + " </td>\n" 
                    + " </tr>\n"
                    + " </table>\n"
                    + "</body>\n"
                    + "</html>";
        } else {
            mailBody = "<html>\n"
                    + "<head>\n"
                    + "<meta charset=\"UTF-8\">\n"
                    + "<title>ISF Alert</title>\n"
                    + "</head>\n"
                    + "<body style=\"background:#e1e1e1;\">\n"
                    + "<table cellpadding=\"0\" cellspacing=\"0\" width=\"650\" align=\"center\" style=\"background:#fff;border:1px solid #c1c1c1; font-size:12px;font-family:arial; padding: 40px;color:#58595B;\">\n"
                    + "<tr>\n"
                    + "<td colspan=\"3\" style=\"padding-bottom: 0px;border-bottom: 1px solid #c1c1c1;\">\n"
                    + "<img alt=\"Ericsson\" src=\"http://www.ericsson.com/shared/eipa/images/elogo.png\" />\n"
                    + "<p style =\"text-align:right;margin:0px\">ISF ALERTS </p>\n"
                    + "</td> \n"
                    + "</tr> \n"
                    + "<tr>\n"
                    + "<td colspan=\"3\" style=\"padding-bottom: 10px;padding-top: 10px;text-align:left;font-size:14px;\">\n"
                    + "<pre>Dear User,<br> "
                    + "<br>"
                    + "A Work Order has been <b> " + status + "</b>  by <b> " + senderName + "(" + senderID + ")</b><br>"
                    + "<br>"
                    + "<b>Please refresh your Work Order page to view the updated Work Orders.</b>"
                    + "<br>"
                    + "<br>"
                    + "This is an auto-generated message. Please do not reply.<br>"
                    + "<br>"
                    + "Have a good day.<br>"
                    + "<br>"
                    + "Best Regards,<br>"
                    + "ISF Team<br>"
                    + "</pre><br>"
                    + " </td>\n"
                    + "</tr>\n"
                    + " \n"
                    + " <tr>\n"
                    + " <td colspan=\"3\" style=\"padding-top:10px;padding-left:10px;padding-right:10px;padding-bottom:10px;\">\n"
                    + " &nbsp;<br /><br /> \n"    
                    + " </td>\n" 
                    + " </tr>\n"
                    + " </table>\n"
                    + "</body>\n"
                    + "</html>";

        }
        return mailBody;
    }
        
        
    @RequestMapping(value = "/updateFlowChartJSON/{projectID}", method = RequestMethod.POST)
	public void updateFlowChartJSON(@PathVariable("projectID") int projectID) {
		this.testService.updateFlowChartJSON(projectID);
	}   
        
    @RequestMapping(value = "/updateFlowChartJSONTools", method = RequestMethod.GET)
	public void updateFlowChartJSON() {
		this.testService.updateFlowChartJSONTools();
	}    
        
//        @RequestMapping(value = "/testJSON", method = RequestMethod.POST)
//	public void testJSON(@RequestBody String json) {
//		this.testService.testJSON(json);
//	}  
        
        @RequestMapping(value = "/populateWFLinkDetails", method = RequestMethod.POST)
	public void populateWFLinkDetails() {
		this.testService.populateWFLinkDetails();
	} 
        
    @RequestMapping(value = "/populateWFStepDetails", method = RequestMethod.POST)
	public void populateWFStepDetails() {
		this.testService.populateWFStepDetails();
	} 
    
    @RequestMapping(value = "/generateExcelFromJson/{subActivityFlowChartDefID}", method = RequestMethod.GET)
	public void generateExcelFromJson(HttpServletResponse response, @PathVariable("subActivityFlowChartDefID") int subActivityFlowChartDefID) throws ParseException {
		this.testService.generateExcelFromJson(response, subActivityFlowChartDefID);
	}
    
    @RequestMapping(value = "/generateNewExcelFromJson/{subActivityFlowChartDefID}", method = RequestMethod.GET)
	public DownloadTemplateModel generateNewExcelFromJson(HttpServletResponse response, @PathVariable("subActivityFlowChartDefID") int subActivityFlowChartDefID) throws ParseException {
		
    	DownloadTemplateModel downloadTemplateModel=new DownloadTemplateModel();
    	String fName=subActivityFlowChartDefID +".xlsx";
    	
    	byte[] file=this.testService.generateNewExcelFromJson(response, subActivityFlowChartDefID);
    	
    	downloadTemplateModel.setpFileContent(file);
   		downloadTemplateModel.setpFileName(fName);
        return downloadTemplateModel;
	}
    
    @RequestMapping(value = "/generateExcelFromJsonAll/{MA}", method = RequestMethod.GET)
	public void generateExcelFromJsonAll(@PathVariable("MA") String MA) throws ParseException {
		this.testService.generateExcelFromJsonAll(MA);
	}

                
//        @RequestMapping(value = "/generateJSON", method = RequestMethod.POST)
//	public void generateJSON() {
//            try {
//                List<FlowChartStepModel> flowChartStepModel = this.flowChartDao.getFlowChartStepDetails(5, 1018);
//                this.flowChartService.generateJSON(5,1018,flowChartStepModel);
//            } catch (Exception ex) {
//                Logger.getLogger(TestController.class.getName()).log(Level.SEVERE, null, ex);
//            }
//	}
        

        @RequestMapping(value = "/generateNewJSONFromFile/{flowChartDefID}", method = RequestMethod.POST, consumes = "multipart/form-data")
        public void generateNewJSONFromFile(
                @PathVariable("flowChartDefID") String flowChartDefID,
                @RequestParam("file") MultipartFile file) throws IOException, SQLException {
            try{
               this.testService.generateNewJSONFromFile(file,flowChartDefID);
            } catch(Exception ex) {
                throw new ApplicationException(500, "Error while uploading the Workflow due to : "+ex.getMessage());
            }
        }

        
        @RequestMapping(value = "/generateNewJSONFromFile", method = RequestMethod.GET)
        public String generateNewJSONFromFile() throws IOException, SQLException {
            try{
              return this.testService.generateNewJSONFromFile();
            } catch(Exception ex) {
                throw new ApplicationException(500, "Error while uploading the Workflow due to : "+ex.getMessage());
            }
        }
        
        @RequestMapping(value = "/viewWorkFlow/{flowChartDefID}", method = RequestMethod.GET)
    	public String viewWorkFlow(@PathVariable ("flowChartDefID") int flowChartDefID) {
    		return this.testService.viewWorkFlow(flowChartDefID);
    	}  
        
        @RequestMapping(value = "/getWorkFlowInformation/{flowChartDefID}", method = RequestMethod.GET)
    	public Map<String,Object> getWorkFlowInformation(@PathVariable ("flowChartDefID") int flowChartDefID) {
    		return this.testService.getWorkFlowInformation(flowChartDefID);
    	}  
        
}

