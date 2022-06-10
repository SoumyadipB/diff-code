/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ericsson.isf.config.ApplicationConfigurations;
import com.ericsson.isf.service.EnvironmentPropertyService;

/**
 *
 * @author ekarath
 */
@Service
public class MailUtil {
		
	@Autowired
    private ApplicationConfigurations configurations;
	
	@Autowired /* Bind to bean/pojo */
	private EnvironmentPropertyService environmentPropertyService;
	    
	    private static final String SP = "Link & Steps  to be followed by <b>Network Engineer</b> :";
	    private static final String MAG = "Link & Steps to be followed by <b>Manager</b> :";
	    
	    private static final String SP_CLOSED = "<b>Steps:</b> Go To CLOSED WORK ORDER(S) Tab";
	    private static final String MAG_CLOSED ="<b>Steps:</b> Select Project>Select WO>Accept OR Reject the WO";
	    
	    private static final String SP_DEFFERED = "<b>Steps:</b> Go To CLOSED WORK ORDER(S) Tab to view Deffered WO List";
	    private static final String MAG_DEFFERED ="<b>Steps:</b> Search respective Project where your WO exists \r\n" +
	    											"Click on view scope details icon \r\n"+
	    											"Open the Work Order Plan tab \r\n"+ 
	    											"Choose Deferred filter";
	    
	    private static final String SP_ACCEPTED = "<b>Steps:</b> Go To CLOSED WORK ORDER(S) Tab";
	    private static final String MAG_ACCEPTED ="<b>Steps:</b> Under Accepted Work Orders,\r\n"+ 
	    										  "Select the Respective Project ID and list of Closed WO will appear";
	    
	    private static final String SP_REJECTED = "<b>Steps:</b> Select STATUS = REOPENED the list will appear OR \r\n"+
	    										  "Go To CLOSED WORK ORDER(S) Tab";
	    private static final String MAG_REJECTED ="<b>Steps:</b>No further action to be taken on Rejected WO by the manager";
	    
	    private static final String WF_SP ="<b>Steps</b> to view workflows :";
	    
	    private static final String WF_PM ="<b>Link & Steps</b> for PM/DR Follow :";
	    
	    private static final String WF_SP_MSG ="Under My Work list of Work orders will appear \r\n" + 
	    									   "Click on View Flow Chart icon the respective work flow will be loaded inside FLOW CHART (S) area";
	    
	    private static final String WF_PM_MSG ="Search respective Project on provided link	\r\n" + 
									    		"Click on View scope details	\r\n" + 
									    		"Go to Work Flow tab	\r\n" + 
									    		"Under Work Flow Approval you will find the list of Work Flows waiting for approval	\r\n" + 
									    		"Take required action on these work flows";
	 
        private static final String DELIVERY_EXE = "/DeliveryExecution/WorkorderAndTask";
	    
	    private static final String PROJECT = "/Project";
	    
	    private static final String DEPLOYED_ENV_KEY="BaseUrl";
        
        public String generateMailBodyForWorkOrders(String senderID, String senderName, String receiverID, String receiverName, String status, int woID,String plannedStart, String neID) {
        String mailBody = "";
        if(neID==null) {
        	neID="-";
        }
        if (woID != 0 && status.equalsIgnoreCase("CLOSED")) {
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
                    + "<p style =\"text-align:right;margin:0px;font-family:Ericsson Capital TT\">ISF ALERTS </p>\n"
                    + "</td> \n"
                    + "</tr> \n"
                    + "<tr>\n"
                    + "<td colspan=\"3\" style=\"padding-bottom: 10px;padding-top: 10px;text-align:left;font-size:14px;\">\n"
                    + "<pre>Dear <b>" + senderName + "</b>,<br> "
                    + "<br>"
                    + "Work Order with <b>WorkOrderID= " + woID + "</b> for <b>NEID= " + neID + "</b> has been <b> " + status + "</b>  by <b> " + receiverName + "(" + receiverID + ")</b><br>"
                    + "<br>"
                    + SP
                    + "<br>"
                    + "<br>"
                    + environmentPropertyService.getEnvironmentPropertyModelByKey(DEPLOYED_ENV_KEY).get(0).getValue()+DELIVERY_EXE
                    + "<br>"
                    + SP_CLOSED
                    + "<br>"
                    + "<br>"
                    + MAG
                    + "<br>"
                    + "<br>"
                    + environmentPropertyService.getEnvironmentPropertyModelByKey(DEPLOYED_ENV_KEY).get(0).getValue()+PROJECT+"/DeliveryAcceptance"
                    + "<br>"
                    + MAG_CLOSED
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
        } else if(woID != 0 && status.equalsIgnoreCase("DEFERRED")){
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
                     + "<p style =\"text-align:right;margin:0px;font-family:Ericsson Capital TT\">ISF ALERTS </p>\n"
                     + "</td> \n"
                     + "</tr> \n"
                     + "<tr>\n"
                     + "<td colspan=\"3\" style=\"padding-bottom: 10px;padding-top: 10px;text-align:left;font-size:14px;\">\n"
                     + "<pre>Dear <b>" + senderName + "</b>,<br> "
                     + "<br>"
                     + "Work Order with <b>WorkOrderID= " + woID + "</b> for <b>NEID= " + neID + "</b> has been <b> " + status + "</b>  by <b> " + receiverName + "(" + receiverID + ")</b><br>"
                     + "<br>"
                     + SP
                     + "<br>"
                     + "<br>"
                     + environmentPropertyService.getEnvironmentPropertyModelByKey(DEPLOYED_ENV_KEY).get(0).getValue()+DELIVERY_EXE
                     + "<br>"
                     + SP_DEFFERED
                     + "<br>"
                     + "<br>"
                     + MAG
                     + "<br>"
                     + "<br>"
                     + environmentPropertyService.getEnvironmentPropertyModelByKey(DEPLOYED_ENV_KEY).get(0).getValue()+PROJECT+"/Search"
                     + "<br>"
                     + MAG_DEFFERED
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
        }else if(woID != 0 && status.equalsIgnoreCase("ACCEPTED")){
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
                 + "<p style =\"text-align:right;margin:0px;font-family:Ericsson Capital TT\">ISF ALERTS </p>\n"
                 + "</td> \n"
                 + "</tr> \n"
                 + "<tr>\n"
                 + "<td colspan=\"3\" style=\"padding-bottom: 10px;padding-top: 10px;text-align:left;font-size:14px;\">\n"
                 + "<pre>Dear <b>" + senderName + "</b>,<br> "
                 + "<br>"
                 + "Work Order with <b>WorkOrderID= " + woID + "</b> for <b>NEID= " + neID + "</b> has been <b> " + status + "</b>  by <b> " + receiverName + "(" + receiverID + ")</b><br>"
                 + "<br>"
                 + SP
                 + "<br>"
                 + "<br>"
                 + environmentPropertyService.getEnvironmentPropertyModelByKey(DEPLOYED_ENV_KEY).get(0).getValue()+DELIVERY_EXE
                 + "<br>"
                 + SP_ACCEPTED
                 + "<br>"
                 + "<br>"
                 + MAG
                 + "<br>"
                 + "<br>"
                 + environmentPropertyService.getEnvironmentPropertyModelByKey(DEPLOYED_ENV_KEY).get(0).getValue()+PROJECT+"/DeliveryAcceptance"
                 + "<br>"
                 + MAG_ACCEPTED
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
        }else if(woID != 0 && status.equalsIgnoreCase("REJECTED")){
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
                    + "<p style =\"text-align:right;margin:0px;font-family:Ericsson Capital TT\">ISF ALERTS </p>\n"
                    + "</td> \n"
                    + "</tr> \n"
                    + "<tr>\n"
                    + "<td colspan=\"3\" style=\"padding-bottom: 10px;padding-top: 10px;text-align:left;font-size:14px;\">\n"
                    + "<pre>Dear <b>" + senderName + "</b>,<br> "
                    + "<br>"
                    + "Work Order with <b>WorkOrderID= " + woID + "</b> for <b>NEID= " + neID + "</b> has been <b> " + status + "</b>  by <b> " + receiverName + "(" + receiverID + ")</b><br>"
                    + "<br>"
                    + SP
                    + "<br>"
                    + "<br>"
                    + environmentPropertyService.getEnvironmentPropertyModelByKey(DEPLOYED_ENV_KEY).get(0).getValue()+DELIVERY_EXE
                    + "<br>"
                    + SP_REJECTED
                    + "<br>"
                    + "<br>"
                    + MAG
                    + "<br>"
                    + "<br>"
                    + environmentPropertyService.getEnvironmentPropertyModelByKey(DEPLOYED_ENV_KEY).get(0).getValue()+PROJECT+"/DeliveryAcceptance"
                    + "<br>"
                    + MAG_REJECTED
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
         }else if (woID != 0 && status.equalsIgnoreCase("TRANSFERRED")) {
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
                     + "<p style =\"text-align:right;margin:0px;font-family:Ericsson Capital TT\">ISF ALERTS </p>\n"
                     + "</td> \n"
                     + "</tr> \n"
                     + "<tr>\n"
                     + "<td colspan=\"3\" style=\"padding-bottom: 10px;padding-top: 10px;text-align:left;font-size:14px;\">\n"
                     + "<pre>Dear <b>" + senderName + "</b>,<br> "
                     + "<br>"
                     + "Work Order with <b>WorkOrderID= " + woID + "</b> for <b>NEID= " + neID + "</b> has been <b> " + status + "</b>  by <b> " + receiverName + "(" + receiverID + ")</b><br>"
                     + "<br>"
                     + SP
                     + "<br>"
                     + "<br>"
                     + environmentPropertyService.getEnvironmentPropertyModelByKey(DEPLOYED_ENV_KEY).get(0).getValue()+DELIVERY_EXE
                     + "<br>"
                     + SP_CLOSED
                     + "<br>"
                     + "<br>"
                     + MAG
                     + "<br>"
                     + "<br>"
                     + environmentPropertyService.getEnvironmentPropertyModelByKey(DEPLOYED_ENV_KEY).get(0).getValue()+PROJECT+"/DeliveryAcceptance"
                     + "<br>"
                     + MAG_CLOSED
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
         }
        else{
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
                    + "<p style =\"text-align:right;margin:0px;font-family:Ericsson Capital TT\">ISF ALERTS </p>\n"
                    + "</td> \n"
                    + "</tr> \n"
                    + "<tr>\n"
                    + "<td colspan=\"3\" style=\"padding-bottom: 10px;padding-top: 10px;text-align:left;font-size:14px;\">\n"
                    + "<pre>Dear User,<br> "
                    + "<br>"
                    + "A Work Order has been <b> " + status + "</b>  by <b> " + senderName + "(" + senderID + ")</b><br>"
                    + "<br>"
                    + "<b>WorkOrderID  && AssignedTo && Planned StartDate :</b><br>" + plannedStart    
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
        
        public String generateMailBodyForWorkFlow(String drSignumID,String drName, String employeeSignumID,String empName,String status,String wfName) {

        String mailBody ="";
                if(status.equalsIgnoreCase("rejected")){
                    mailBody= "<html>\n"
                        + "<head>\n"
                        + "<meta charset=\"UTF-8\">\n"
                        + "<title>ISF Alert</title>\n"
                        + "</head>\n"
                        + "<body style=\"background:#e1e1e1;\">\n"
                        + "<table cellpadding=\"0\" cellspacing=\"0\" width=\"650\" align=\"center\" style=\"background:#fff;border:1px solid #c1c1c1; font-size:12px;font-family:arial; padding: 40px;color:#58595B;\">\n"
                        + "<tr>\n"
                        + "<td colspan=\"3\" style=\"padding-bottom: 0px;border-bottom: 1px solid #c1c1c1;\">\n"
                        + "<img alt=\"Ericsson\" src=\"http://www.ericsson.com/shared/eipa/images/elogo.png\" />\n"
                        + "<p style =\"text-align:right;margin:0px;font-family:Ericsson Capital TT\">ISF ALERTS </p>\n"
                        + "</td> \n"
                        + "</tr> \n"
                        + "<tr>\n"
                        + "<td colspan=\"3\" style=\"padding-bottom: 10px;padding-top: 10px;text-align:left;font-size:14px;\">\n"
                        + "<pre>Dear <b>" + empName + "</b>,<br> "
                        + "<br>"
                        + "Your request for workflow: "+ wfName +" modification is <span style='color:Red'><b>Rejected</b></span> by "+ drName + "(" + drSignumID + ")</b><br>"
                        + "<br>"
                        + "<b>Service Professions</b> follow :	"
                        + "<br><br>"
                        + environmentPropertyService.getEnvironmentPropertyModelByKey(DEPLOYED_ENV_KEY).get(0).getValue()+DELIVERY_EXE
                        + "<br>"
                        + WF_SP
                        + "<br>"
                        + "No further action required on rejected Work flow	"
                        + "<br><br>"
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
                }else if(status.equalsIgnoreCase("approved")){
                        mailBody= "<html>\n"
                        + "<head>\n"
                        + "<meta charset=\"UTF-8\">\n"
                        + "<title>ISF Alert</title>\n"
                        + "</head>\n"
                        + "<body style=\"background:#e1e1e1;\">\n"
                        + "<table cellpadding=\"0\" cellspacing=\"0\" width=\"650\" align=\"center\" style=\"background:#fff;border:1px solid #c1c1c1; font-size:12px;font-family:arial; padding: 40px;color:#58595B;\">\n"
                        + "<tr>\n"
                        + "<td colspan=\"3\" style=\"padding-bottom: 0px;border-bottom: 1px solid #c1c1c1;\">\n"
                        + "<img alt=\"Ericsson\" src=\"http://www.ericsson.com/shared/eipa/images/elogo.png\" />\n"
                        + "<p style =\"text-align:right;margin:0px;font-family:Ericsson Capital TT\">ISF ALERTS </p>\n"
                        + "</td> \n"
                        + "</tr> \n"
                        + "<tr>\n"
                        + "<td colspan=\"3\" style=\"padding-bottom: 10px;padding-top: 10px;text-align:left;font-size:14px;\">\n"
                        + "<pre>Dear <b>" + empName + "</b>,<br> "
                        + "<br>"
                        + "Your request for workflow: "+ wfName + "modification is <span style='color:green'><b>Approved</b></span> by "+ drName + "(" + drSignumID + ")</b><br>"
                        + "<br>"
                        + "<b>Service Professions</b> follow :"
                        + "<br><br>"
                        + environmentPropertyService.getEnvironmentPropertyModelByKey(DEPLOYED_ENV_KEY).get(0).getValue()+DELIVERY_EXE
                        + "<br>"  
                        +  WF_SP
                        + "<br>"
                        +  WF_SP_MSG
                        + "<br><br>"
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
                }
        return mailBody;
        }
        
        public String generateMailBodyForWFApproval(String pSignum, String pName, String eSignum, String eName, String projectID, String subActivity) {

        String mailBody = "<html>\n"
                        + "<head>\n"
                        + "<meta charset=\"UTF-8\">\n"
                        + "<title>ISF Alert</title>\n"
                        + "</head>\n"
                        + "<body style=\"background:#e1e1e1;\">\n"
                        + "<table cellpadding=\"0\" cellspacing=\"0\" width=\"650\" align=\"center\" style=\"background:#fff;border:1px solid #c1c1c1; font-size:12px;font-family:arial; padding: 40px;color:#58595B;\">\n"
                        + "<tr>\n"
                        + "<td colspan=\"3\" style=\"padding-bottom: 0px;border-bottom: 1px solid #c1c1c1;\">\n"
                        + "<img alt=\"Ericsson\" src=\"http://www.ericsson.com/shared/eipa/images/elogo.png\" />\n"
                        + "<p style =\"text-align:right;margin:0px;font-family:Ericsson Capital TT\">ISF ALERTS </p>\n"
                        + "</td> \n"
                        + "</tr> \n"
                        + "<tr>\n"
                        + "<td colspan=\"3\" style=\"padding-bottom: 10px;padding-top: 10px;text-align:left;font-size:14px;\">\n"
                        + "<pre>Dear User,<br> "
                        + "<br>"
                        + "Work Flow for Project :<b>"+projectID+"</b> & SubActivity :<b>"+subActivity+"</b> is modified by " +eName+ "(" + eSignum + ")<br>"
                        + "<br>"
                        + "<b>Please verifiy to <span style='color:green'><b>Approve</b></span>/<span style='color:Red'><b>Reject</b></span> the WorkFlow changes</b>"
                        + "<br>"
                        + "<br>"
                        +  WF_PM
                        + "<br>"
                        + "<br>"
                        + environmentPropertyService.getEnvironmentPropertyModelByKey(DEPLOYED_ENV_KEY).get(0).getValue()+PROJECT+"/Search"
                        + "<br>"
                        + WF_SP
                        + "<br>"
                        + WF_PM_MSG
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
        return mailBody;
        }

   
}
