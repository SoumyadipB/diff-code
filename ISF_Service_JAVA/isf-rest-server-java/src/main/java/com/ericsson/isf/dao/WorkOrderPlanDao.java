/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ericsson.isf.mapper.WorkOrderMapper;
import com.ericsson.isf.model.BookingDetailsModel;
import com.ericsson.isf.model.CreateWorkOrderModel;
import com.ericsson.isf.model.CreateWorkOrderModel2;
import com.ericsson.isf.model.CreateWorkOrderNetworkElementModel;
import com.ericsson.isf.model.DOIDModel;
import com.ericsson.isf.model.DOWOModel;
import com.ericsson.isf.model.DataTableColumnSpecs;
import com.ericsson.isf.model.DataTableRequest;
import com.ericsson.isf.model.DeleteWOListModel;
import com.ericsson.isf.model.EmailModel;
import com.ericsson.isf.model.EmployeeBasicDetails;
import com.ericsson.isf.model.EnvironmentPropertyModel;
import com.ericsson.isf.model.ExecutionPlanDetail;
import com.ericsson.isf.model.ExecutionPlanFlow;
import com.ericsson.isf.model.ExecutionPlanModel;
import com.ericsson.isf.model.FcStepDetails;
import com.ericsson.isf.model.FinalRecordsForWOCreationModel;
import com.ericsson.isf.model.InProgressNextStepModal;
import com.ericsson.isf.model.InProgressTaskModel;
import com.ericsson.isf.model.MailModel;
import com.ericsson.isf.model.NextStepModel;
import com.ericsson.isf.model.NextSteps;
import com.ericsson.isf.model.ProjectModel;
import com.ericsson.isf.model.ProjectNodeTypeModel;
import com.ericsson.isf.model.ServerTimeModel;
import com.ericsson.isf.model.WOInputFileModel;
import com.ericsson.isf.model.WOOutputFileModel;
import com.ericsson.isf.model.WOOutputFileResponseModel;
import com.ericsson.isf.model.WorkOrderCompleteDetailsModel;
import com.ericsson.isf.model.WorkOrderInputFileModel;
import com.ericsson.isf.model.WorkOrderModel;
import com.ericsson.isf.model.WorkOrderOutputFileModel;
import com.ericsson.isf.model.WorkOrderPlanModel;
import com.ericsson.isf.model.WorkOrderPlanModel2;
import com.ericsson.isf.model.WorkOrderPlanNodesModel;
import com.ericsson.isf.model.WorkOrderViewProjectModel;
import com.ericsson.isf.service.EnvironmentPropertyService;
import com.ericsson.isf.service.OutlookAndEmailService;
import com.ericsson.isf.util.IsfCustomIdInsert;
/**
 *
 * @author eguphee
 */
@Repository
public class WorkOrderPlanDao {

    @Qualifier("sqlSession")
    /*Create session from SQLSessionFactory */
    @Autowired
    private SqlSessionTemplate sqlSession;
    
    @Autowired 
    private OutlookAndEmailService emailService;
    
	@Autowired /* Bind to bean/pojo */
	private EnvironmentPropertyService environmentPropertyService;
	
	@Autowired
	private IsfCustomIdInsert isfCustomIdInsert;

    public List<WorkOrderModel> checkNotStartedStatusOfWorkOrderPlan(WorkOrderPlanModel wOPlanObject) {
        WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return workOrderMapper.checkNotStartedStatusOfWorkOrderPlan(wOPlanObject.getwOPlanID());
    }

    public void deleteWorkOrderPlanDao(WorkOrderPlanModel wOPlanObject) {
        WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        workOrderMapper.deleteWorkOrderPlan(wOPlanObject);
    }

    public List<WorkOrderPlanModel> getWorkOrderPlanDetails(int woPlanID,int projectId,String signumID) {
        WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return workOrderMapper.getWorkOrderPlanDetails(woPlanID,projectId,signumID);
    }
    
    
    public WorkOrderPlanModel getPlanDetailsById(int woPlanID) {
        WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return workOrderMapper.getPlanDetailsById(woPlanID);
    }

    public void updateWorkOrderPlanDao(WorkOrderPlanModel wOPlanObject) {
        WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        workOrderMapper.updateWorkOrderAndWorkOrderNodes(wOPlanObject);
        workOrderMapper.updateWorkOrderPlanAndWOPlanNodes(wOPlanObject);
    }

    public int getEstdHrs(int projectID,int subActId)
    {
     int estdHrs;
     WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
     estdHrs=workOrderMapper.getEstdHrs(projectID,subActId);
     return estdHrs;
    }
    
    
//    public void createWorkOrderPlan(CreateWorkOrderModel wOPlanObject ,Date plannedEndDate ) {
//        WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
//        workOrderMapper.createWorkOrderPlan(wOPlanObject,plannedEndDate);
//    }

    public List<WorkOrderViewProjectModel> getWorkOrderViewDetails(String projectID, String scope, String activity, String WOID, String startDate, String endDate, String assignedTo, String signum_LoggedIn, boolean isWeekEndIncluded, boolean isOddHoursIncluded,String status,String nodeName, String marketArea, String assignedBy) {
        WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return workOrderMapper.getWorkOrderViewDetails(projectID, scope, activity, WOID, startDate, endDate, assignedTo, signum_LoggedIn, isWeekEndIncluded, isOddHoursIncluded,status,nodeName,marketArea,assignedBy);
    }
 public List<Map<String,Object>> getNEIDByProjectID(int projectID,String term) {

    	int pageSize=25;
        int pageNo=0;
    	RowBounds rowBounds=new RowBounds(pageNo*pageSize,pageSize);
    	WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return workOrderMapper.getNEIDByProjectID(projectID,'%'+term+'%',rowBounds);
    }

    public List<WorkOrderPlanModel> searchWorkOrderPlanDetails(int projectID, int domainID, int serviceAreaID, int technologyID, String activityName, int subActivityID) {
        WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return workOrderMapper.searchWorkOrderPlanDetails(projectID, domainID, serviceAreaID, technologyID, activityName, subActivityID);
    }
    public int isPlanEditable(int woPlanId) {
        WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return workOrderMapper.isPlanEditable(woPlanId);
    }
    public List<WorkOrderPlanModel2> getWorkOrderPlans(int projectID, String startDate, String endDate, String woStatus, DataTableRequest req) {
        
    	WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return workOrderMapper.getWorkOrderPlans(projectID, req,startDate,endDate,woStatus);
    }
    
    public Integer getWorkOrderPlansCount(int projectID, String startDate, String endDate, String woStatus, DataTableRequest req) {
    	   List<DataTableColumnSpecs> row =req.getColumns();
    	   row.remove(4);
    	WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
    	return workOrderMapper.getWorkOrderPlansCount(projectID, req,startDate,endDate,woStatus);
    }
    
      public List<WorkOrderPlanModel> checkProjectEditByProjID(int projectID) {
        WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return workOrderMapper.checkProjectEditByProjID(projectID);
    }

    public void deleteWorkOrder(int wOID, String signumID) {
        WorkOrderMapper deleteWorkOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        deleteWorkOrderMapper.deleteWorkOrder(wOID, signumID);
    }
    
    public void deleteWONodes(int wOID){
        WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        workOrderMapper.deleteWONodes(wOID);
    }

    public Boolean checkNotStartedStatusOfWorkOrder(int wOID) {
        WorkOrderMapper deleteWorkOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return deleteWorkOrderMapper.checkNotStartedStatusOfWorkOrder(wOID);
    }

    public void updateWorkOrder(WorkOrderModel workOrderModel) {
        WorkOrderMapper updateWorkOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        updateWorkOrderMapper.updateWorkOrder(workOrderModel);
    }
    public void updateWorkOrderParentId(int parentWorkOrderID,int woid) {
        WorkOrderMapper updateWorkOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        updateWorkOrderMapper.updateWorkOrderParentId(parentWorkOrderID,woid);
    }
    public void updatePreviousWOID(int workOrderId) {
        WorkOrderMapper updateWorkOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        updateWorkOrderMapper.updatePreviousWOID(workOrderId);
    }
    public void closeDeferedWorkOrder(int workOrderId) {
        WorkOrderMapper updateWorkOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        updateWorkOrderMapper.closeDeferedWorkOrder(workOrderId);
    }
    public void setInprogressDeferedWorkOrder(int workOrderId) {
        WorkOrderMapper updateWorkOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        updateWorkOrderMapper.setInprogressDeferedWorkOrder(workOrderId);
    }
    public void setAssignedDeferedWorkOrder(int workOrderId) {
        WorkOrderMapper updateWorkOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        updateWorkOrderMapper.setAssignedDeferedWorkOrder(workOrderId);
    }
   
    public List<String> getDeferedWorkOrderDetails(int workOrderId) {
        WorkOrderMapper getWorkOrderDetailsMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return getWorkOrderDetailsMapper.getDeferedWorkOrderDetails(workOrderId);

    }

    public List<WorkOrderModel> getWorkOrderDetails(int wOID) {
        WorkOrderMapper getWorkOrderDetailsMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return getWorkOrderDetailsMapper.getWorkOrderDetails(wOID);

    }
    
    public WorkOrderModel getWorkOrderDetailsById(int wOID) {
        WorkOrderMapper getWorkOrderDetailsMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return getWorkOrderDetailsMapper.getWorkOrderDetailsById(wOID);

    }

    public void insertNodeDetails(int woid, String nodeType, String commSeperated) {
        WorkOrderMapper getWorkOrderDetailsMapper = sqlSession.getMapper(WorkOrderMapper.class);
        getWorkOrderDetailsMapper.insertNodeDetails(woid, nodeType, commSeperated);
    }

    public void deleteNodeforWO(int woid) {
        WorkOrderMapper getWorkOrderDetailsMapper = sqlSession.getMapper(WorkOrderMapper.class);
        getWorkOrderDetailsMapper.deleteNodeforWO(woid);
    }

    public void transferWorkOrder(int wOID, String senderID, String receiverID) {
        WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
        woManagementMapper.transferWorkOrder(wOID, senderID, receiverID);
    
    } 
    
    public void massUpdateWorkOrder(int wOID, String senderID, String receiverID,
            Date plannedStartDate, Date plannedEndDate) {
        WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
        woManagementMapper.massUpdateWorkOrder(wOID, senderID, receiverID, plannedEndDate,
                 plannedStartDate);
    
    } 

    public void sendSuccessMail(String woIDs, String senderID, String receiverID) {
        WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
        woManagementMapper.sendSuccessMail(woIDs, senderID, receiverID);
    }

    public List<String> getPriority() {
       WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
       return woManagementMapper.getPriority();
    }
    
    public List<String> getNodeType(Integer countryCustomerGroupID) {
        WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return woManagementMapper.getNodeType(countryCustomerGroupID);
    }
    
     public List<String> getMarketArea(int countryCustomerGroupID,String nodeType, String type) {
        WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return woManagementMapper.getMarketArea(countryCustomerGroupID,nodeType,type);
    }
     
     public List<String> getVendor(int countryCustomerGroupID,String marketArea,String nodeType) {
        WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return woManagementMapper.getVendor(countryCustomerGroupID,marketArea,nodeType);
    }

    public List<ProjectNodeTypeModel> getNodeNames(int projectID,String elementType ,String type) {
        WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return woManagementMapper.getNodeNames(projectID,elementType,type);
    }
    
    
    public List<ProjectNodeTypeModel> getNodeNamesFilter(int projectID,String elementType ,String type,String vendor,String market,
            String tech,String domain,String subDomain) {
        WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return woManagementMapper.getNodeNamesFilter(projectID,elementType,type,vendor,market,tech,domain,subDomain);
    }
    
    public List<String> getNodeNamesFilterValidate(int projectID,String elementType ,
    		String type,
    		String vendor,String market,
        String tech,String domain,String subDomain,List<String> nodesCluster) {   
    	String allNodes = "";
    	List<String> tmpNodesCluster1 = new ArrayList<String>();
    	for (String a  : nodesCluster) {
    		a = a.replaceAll( "'", "''");
    		tmpNodesCluster1.add(a);
    	}
        allNodes=String.join("','",tmpNodesCluster1 );
        allNodes = "'"+allNodes+"'";
        WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return woManagementMapper.getNodeNamesFilterValidate(projectID,elementType,
        		type,
        		vendor,market,tech,domain,subDomain,allNodes);
    }

    public List<ProjectNodeTypeModel> getNodeNamesForSite(int projectID, String type) {
        WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return woManagementMapper.getNodeNamesForSite(projectID,type);
    }
    
    public List<ProjectNodeTypeModel> getNodeNamesForSiteFilter(int projectID, String type,String vendor,String market,String tech,String domain,String subDomain) {
        WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return woManagementMapper.getNodeNamesForSiteFilter(projectID,type,vendor,market,tech,domain,subDomain);
    }
    
    public List<String> getNodeNamesForSiteFilterValidate(int projectID, String type,String vendor,String market,String tech,String domain,String subDomain,List<String> nodesCluster) {
    	String allNodes = "";
    	List<String> tmpNodesCluster1 = new ArrayList<String>();
    	for (String a  : nodesCluster) {
    		a = a.replaceAll( "'", "''");
    		tmpNodesCluster1.add(a);
    	}
        allNodes=String.join("','",tmpNodesCluster1 );
        allNodes = "'"+allNodes+"'";
        WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return woManagementMapper.getNodeNamesForSiteFilterValidate(projectID,type,vendor,market,tech,domain,subDomain,allNodes);
    }
    
    public List<ProjectNodeTypeModel> getNodeNamesForSector(int projectID, String type) {
        WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
        
        return woManagementMapper.getNodeNamesForSector(projectID,type);
    }
    
    
    public List<ProjectNodeTypeModel> getNodeNamesForSectorFilter(int projectID, String type,String vendor,String market,String tech,String domain,String subDomain) {
        WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
        
        return woManagementMapper.getNodeNamesForSectorFilter(projectID,type,vendor,market,tech,domain,subDomain);
    }
    
    public List<String> getNodeNamesForSectorFilterValidate(int projectID, String type,String vendor,String market,String tech,String domain,String subDomain,List<String> nodesSector) {
    	String allNodes = "";
    	List<String> tmpNodesCluster1 = new ArrayList<String>();
    	for (String a  : nodesSector) {
    		a = a.replaceAll( "'", "''");
    		tmpNodesCluster1.add(a);
    	}
        allNodes=String.join("','",tmpNodesCluster1 );
        allNodes = "'"+allNodes+"'";
        WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
        
        return woManagementMapper.getNodeNamesForSectorFilterValidate(projectID,type,vendor,market,tech,domain,subDomain,allNodes);
    }


    public Boolean checkWOStatus(Integer t) {
        WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return woManagementMapper.checkWOStatus(t);
    }

    public void updateTransferWOLOG(Integer t, String receiverID, String senderID) {
        WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
        woManagementMapper.updateTransferWOLOG(t,receiverID,senderID);
    }

    
    public void addWorkOrderPlan(CreateWorkOrderModel wOPlanObject,String signum) {
        WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
        woManagementMapper.addWorkOrderPlan(wOPlanObject,signum);
        wOPlanObject.setwOPlanID(isfCustomIdInsert.generateCustomId(wOPlanObject.getwOPlanID()));
    }
    public void addWorkOrderPlanNodes(CreateWorkOrderModel wOPlanObject,String nodeType,String nodeName) {
        WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
        woManagementMapper.addWorkOrderPlanNodes(wOPlanObject,nodeType, nodeName);
    }
    
    public void createWorkOrder( CreateWorkOrderModel2 woPlanModel) {
        WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
        woManagementMapper.createWorkOrder(woPlanModel);
        woPlanModel.setWoId((isfCustomIdInsert.generateCustomId(woPlanModel.getWoId())));
        
        // delete from execution plan flow if wo is reopened
        Integer parentWorkOrderID = woPlanModel.getParentWorkOrderId();
		if(parentWorkOrderID != null) {
        	deleteWorkOrderFromExecutionPlanFlow(parentWorkOrderID);
        }
    }

    public void deleteWorkOrderFromExecutionPlanFlow(int parentWorkOrderID) {

    	 WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
         woManagementMapper.deleteWorkOrderFromExecutionPlanFlow(parentWorkOrderID);
	}

	public boolean checkIFWOExists(int woid) {
        WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return woManagementMapper.checkIFWOExists(woid);
    }
	public List<ProjectModel> getProjectBySignum(String signum, String marketArea, String role) {
		// TODO Auto-generated method stub
		WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return woManagementMapper.getProjectBySignum(signum,marketArea,role);
	}

    public MailModel getWoMailNotificationDetails(int woID) {
        WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return woManagementMapper.getWoMailNotificationDetails(woID);
    }

    public List<String> getEmailIDs(List<String> lstSignumID) {
        WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return woManagementMapper.getEmailIDs(lstSignumID);
    }

    public Map<String,String> getCreatorEmailID(String createdBy) {
        WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return woManagementMapper.getCreatorEmailID(createdBy);
    }
    private static final String DEPLOYED_ENV_KEY="BaseUrl";
    public void SendMailNotification(String senderSignumID, String body, String receiverSignumID) throws IOException, MessagingException, InterruptedException {
        WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
        EmailModel emailDetails = new EmailModel();
        emailDetails.setBody(body);
        emailDetails.setTo(receiverSignumID);
        emailDetails.setCc(senderSignumID);
        emailDetails.setSubject("WorkOrder Notification");
        
        List<EnvironmentPropertyModel> environmentPropertyModel = environmentPropertyService.getEnvironmentPropertyModelByKey(DEPLOYED_ENV_KEY);
        if ("CLOUD".equalsIgnoreCase(environmentPropertyModel.get(0).getDeployedEnv())) {
        	emailService.sendGridMailUtility(emailDetails, true);
        }else {
        	emailService.sendSmtpMail(emailDetails, true);
        	//woManagementMapper.SendMailNotification(receiverSignumID,body,senderSignumID);
        }
    }

    public Map<String,Integer> getWorkFlowVersion(int projectID, int subActivityID) {
        WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return woManagementMapper.getWorkFlowVersion(projectID,subActivityID);
    }
            
     public int getWorkFlowVersionWhileUpload(int projectID, int subActivityID) {
        WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return woManagementMapper.getWorkFlowVersionWhileUpload(projectID,subActivityID);
    }        

    public List<Integer> getWorkFlowVersionList(int projectID, int subActivityID) {
        WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return woManagementMapper.getWorkFlowVersionList(projectID,subActivityID);
    }
            
    public int getLatestWorkFlowVersion(int projectID, int subActivityID) {
        WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return woManagementMapper.getLatestWorkFlowVersion(projectID,subActivityID);
    }

    public void updateWOWFVersion(int woID, int wfVersion,int workFlowDefID) {
         WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
         woManagementMapper.updateWOWFVersion(woID,wfVersion,workFlowDefID);
    }

    public List<ProjectNodeTypeModel> getClusterNames(int projectID, String type) {
        WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return woManagementMapper.getClusterNames(projectID,type);
    }
    
     public List<ProjectNodeTypeModel> getClusterNamesFilter(int projectID,String type,String vendor,String market,String tech,String domain,String subDomain) {
        WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return woManagementMapper.getClusterNamesFilter(projectID,type,vendor,market,tech,domain,subDomain);
    }
    
     public List<ProjectNodeTypeModel> getClusterNamesByFilter(int projectID,String type,String vendor,String market,String tech,String domain,String subDomain,String term) {
    	 int pageSize=50;
 		int pageNo=0;
 		RowBounds rowBounds=new RowBounds(pageNo*pageSize,pageSize);
         WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
         return woManagementMapper.getClusterNamesByFilter(projectID,type,vendor,market,tech,domain,subDomain,'%'+term+'%',rowBounds);
     }
     public List<String> getClusterNamesFilterValidate(int projectID,
    		 String type,
    		 String vendor,String market,String tech,String domain,String subDomain,List<String> nodesCluster) {

    	String allNodes = "";
    	Collections.replaceAll(nodesCluster, "'", "''");
        allNodes=String.join("','",nodesCluster );
        allNodes = "'"+allNodes+"'";
    	
        WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return woManagementMapper.getClusterNamesFilterValidate(projectID,
        		type,
        		vendor,market,tech,domain,subDomain,allNodes);
    } 

    public int getEstdHrsForScope(int projectID, int subActivityID, int scopeID) {
        WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return workOrderMapper.getEstdHrsForScope(projectID,subActivityID,scopeID);
    }

	public List<Map<String, Object>> getDomainDetailsByWOPlanID(int woPlanId) {
		// TODO Auto-generated method stub
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return workOrderMapper.getDomainDetailsByWOPlanID(woPlanId);
	}

	public List<Map<String, Object>> getPlanDataByWOPlanID(int woPlanId) {
		// TODO Auto-generated method stub
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return workOrderMapper.getPlanDataByWOPlanID(woPlanId);
	}

	public List<Map<String, Object>> getNodeTypeByWOPlanID(int woPlanId) {
		// TODO Auto-generated method stub
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return workOrderMapper.getNodeTypeByWOPlanID(woPlanId);
	}

	public List<Map<String, Object>> getNodeNamesByWOPlanID(int woPlanId) {
		// TODO Auto-generated method stub
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return workOrderMapper.getNodeNamesByWOPlanID(woPlanId);
	}

	public List<Map<String, Object>> getAssignedUsersByWOPlanID(int woPlanId) {
		// TODO Auto-generated method stub
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return workOrderMapper.getAssignedUsersByWOPlanID(woPlanId);
	}

	public void updateWorkOrderPlanStatus(int workOrderPlanID) {
		// TODO Auto-generated method stub
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        workOrderMapper.updateWorkOrderPlanStatus(workOrderPlanID);
		
	}

	public void updateWorkOrderStatusNew(int workOrderPlanID) {
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        workOrderMapper.updateWorkOrderStatusNew(workOrderPlanID);
		
	}
	public void updateWorkOrderStatus( int woID ,  String updatedBY, String status ) {
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        workOrderMapper.updateWorkOrderStatus(  woID ,   updatedBY,  status );
		
	}
	

	public List<Map<String, Object>> getCheckBoxData(int woPlanId) {
		// TODO Auto-generated method stub
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return workOrderMapper.getCheckBoxData(woPlanId);
		
	}

  
	public List<Map<String,Object>> getSourcesForMapping() {
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return workOrderMapper.getSourcesForMapping(); 
   }
	
	public List<Map<String,Object>> getSourcesForPlan(int projectId) {
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return workOrderMapper.getSourcesForPlan(projectId); 
   }

    public void uploadFileForWOCreation(Integer projectID, String createdBy, String fileName) {
        WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        workOrderMapper.uploadFileForWOCreation(projectID,createdBy,fileName); 
    }
   

	public void saveExecutionPlan(ExecutionPlanModel executionPlanModel) {
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        workOrderMapper.saveExecutionPlan(executionPlanModel);	
	}
	
	public void setInactiveExistingExecPlan(Integer projectId,String planName) {
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        workOrderMapper.setInactiveExistingExecPlan(projectId,planName);	
	}
	
	public void saveExecutionPlanDetails(ExecutionPlanModel executionPlanModel) {
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        workOrderMapper.saveExecutionPlanDetails(executionPlanModel);
		
	}
	public List<ExecutionPlanModel> getExecutionPlans(int projectId) {
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return workOrderMapper.getExecutionPlansByProjectId(projectId);
		
	}
	
	public List<ExecutionPlanDetail> getExecutionPlanDetailsByExecutionPlanId(int executionPlanId) {
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return workOrderMapper.getExecutionPlanDetailsByExecutionPlanId(executionPlanId);
		
	}
	
	public void updateExecutionPlanDetails(ExecutionPlanModel executionPlanModel) {
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        workOrderMapper.updateExecutionPlanDetails(executionPlanModel);
		
	}
	
	public void deleteExecutionPlanDetails(ExecutionPlanModel executionPlanModel) {
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        workOrderMapper.deleteExecutionPlanDetails(executionPlanModel);
		
	}
	public List<ExecutionPlanModel> searchExecutionPlans(Integer subactivityId, Integer workFlowVersionNo) {
		WorkOrderMapper flowChartMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return flowChartMapper.searchExecutionPlans(subactivityId,workFlowVersionNo); 
     }
	public void updateExecutionPlanStatus(int executionPlanId,boolean isactive,String userName) {
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        workOrderMapper.updateExecutionPlanStatus(executionPlanId,isactive,userName);
		
	}
	public void updateExecutionPlan(ExecutionPlanModel executionPlanModel) {
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        workOrderMapper.updateExecutionPlan(executionPlanModel);
		
	}
	
	public void saveExecutionPlanFlow(ExecutionPlanFlow executionPlanFlow) {
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        workOrderMapper.saveExecutionPlanFlow(executionPlanFlow);
		
	}
	
	public ExecutionPlanFlow getExecutionPlanFlowByGroupId(long execPlanGroupId, long executionPlanDetailId) {
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return workOrderMapper.getExecutionPlanFlowByGroupId(execPlanGroupId,executionPlanDetailId);
		
	}
	
	public void markExecutionPlanFlowComplete(long execPlanGroupId, long executionPlanDetailId) {
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        workOrderMapper.markExecutionPlanFlowComplete(execPlanGroupId,executionPlanDetailId);
		
	}
	
	
	public void markExecutionPlanFlowStepComplete(long execPlanGroupId, long executionPlanDetailId,long executionPlanFlowId) {
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        workOrderMapper.markExecutionPlanFlowStepComplete(execPlanGroupId,executionPlanDetailId,executionPlanFlowId);
		
	}
	
	
	
	
	public ExecutionPlanFlow getExecutionFlowByWoid(int woid) {
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return workOrderMapper.getExecutionFlowByWoid(woid);
		
	}
	
	public void updateExecutionPlanFlowWoId(int oldWoid,int newWoId, int newPlanId) {
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        workOrderMapper.updateExecutionPlanFlowWoId(oldWoid,newWoId,newPlanId);
		
	}
	
	
	
	
	public ExecutionPlanDetail getExecutionPlanDetailsByExecutionPlanDetailId(long executionPlanDetailId) {
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return workOrderMapper.getExecutionPlanDetailsByExecutionPlanDetailId(executionPlanDetailId);
		
	}
	
	public ExecutionPlanDetail getExecutionPlanDetailsByTaskNumber(long executionPlanDetailId) {
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return workOrderMapper.getExecutionPlanDetailsByExecutionPlanDetailId(executionPlanDetailId);
		
	}
	
	public ExecutionPlanModel getActiveExecutionPlanByid(long executionPlanDetailId) {
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return workOrderMapper.getActiveExecutionPlanByid(executionPlanDetailId);
		
	}
	
	public List<String> isPlanInExecution(long executionPlanId) {
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return workOrderMapper.isPlanInExecution(executionPlanId);
		
	}
	
	public ExecutionPlanModel getExecutionPlandDetilsbyPlanWoId(long woId) {
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return workOrderMapper.getExecutionPlandDetilsbyWoPlanId(woId+"");
		
	}
        
        public Integer getWOIdbyWoplanID(int woPlanId) {
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return workOrderMapper.getWOIdbyWoplanID(woPlanId);
		
	}
	
    public List<LinkedHashMap<String, Object>> getInprogressTask(String signum) {
    	WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        List<LinkedHashMap<String, Object>> result = workOrderMapper.getInprogressTask(signum);
    	
    	return result;
    }
    
    public Double getWoTotalTime(String woid) {
    	WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
    	return workOrderMapper.getWoTotalTime(woid);
    }
    
    public List<ServerTimeModel> getServerTimeByTaskID(String listTaskID,String listStepID,String listWOID) {
    	WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
    	return workOrderMapper.getServerTimeByTaskID(listTaskID,listStepID,listWOID);
    }
        
    public List<LinkedHashMap<String, Object>> getBookingDetails(int WoID,String SignumID) {
    	WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        List<LinkedHashMap<String, Object>> result = workOrderMapper.getBookingDetails(WoID,SignumID);
    	return result;
    }

    public List<Map<String,Object>> getPlannedDateForWoPlanID(int woPlanID) {
        WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return workOrderMapper.getPlannedDateForWoPlanID(woPlanID);
    }

	public List<Map<String, Object>> getBotIDByWFSignum(String signum) {
		// TODO Auto-generated method stub
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return workOrderMapper.getBotIDByWFSignum(signum);
	}

	public List<Map<String, Object>> getWoIDByProjectID(String projectID,
			String subactivityID, String subActivityFlowChartDefID) {
		// TODO Auto-generated method stub
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return workOrderMapper.getWoIDByProjectID(projectID, subactivityID, subActivityFlowChartDefID);
	}

	public String getWoStartDate(int getwOID) {
		// TODO Auto-generated method stub
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return workOrderMapper.getWoStartDate(getwOID);
	}

	public void updateWoStartDateByCurrentDate(int getwOID) {
		// TODO Auto-generated method stub
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        workOrderMapper.updateWoStartDateByCurrentDate(getwOID);
	}

	public void updateWoStartDates(int getwOID, String startDate) {
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        workOrderMapper.updateWoStartDates(getwOID, startDate);
		
	}
	public List<Map<String, Object>> getWorkOrderDetailsByName(String woName, String signum) {
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return workOrderMapper.getWorkOrderDetailsByName(woName, signum);
		
	}
	public List<Map<String, Object>> getAdditionalInfoOfPlan(int woplanId) {
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return workOrderMapper.getAdditionalInfoOfPlan(woplanId);
		
	}

    public List<NextSteps> getNextStepData(String stepID, Integer defID) {
        WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return workOrderMapper.getNextStepData(stepID,defID);
    }
    
    public List<NextStepModel> getNextStepDataModel(String stepID, Integer defID) {
        WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return workOrderMapper.getNextStepDataModel(stepID,defID);
    }

    public List<String> getWorkOrderNodes(int woId) {
        WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return workOrderMapper.getWorkOrderNodes(woId);
    }

	public int getWorkFlowDefID(int projectID, int subActivityID, int workFlowVersionNo, String wfid) {
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return workOrderMapper.getWorkFlowDefID(projectID,subActivityID,workFlowVersionNo,wfid);
	}

	public int getFlowChartDefID(int woID,int workFlowVersionNo) {
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return workOrderMapper.getFlowChartDefID(woID,workFlowVersionNo);
	}

	public List<String> getWoMailNotificationDetailsForDAC(int woID) {
		 WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
	        return woManagementMapper.getWoMailNotificationDetailsForDAC(woID);
	}

	public List<ProjectNodeTypeModel> getNodeNamesBySiteFilter(int projectID,String vendor, String market,
			String tech, String domain, String subDomain, String term) {
		 int pageSize=50;
	 		int pageNo=0;
	 		RowBounds rowBounds=new RowBounds(pageNo*pageSize,pageSize);
	         WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
	         return woManagementMapper.getNodeNamesBySiteFilter(projectID,vendor,market,tech,domain,subDomain,'%'+term+'%',rowBounds);
	}

	public List<ProjectNodeTypeModel> getNodeNamesBySectorFilter(int projectID, String vendor,
			String market, String tech, String domain, String subDomain, String term) {
		 int pageSize=50;
	 		int pageNo=0;
	 		RowBounds rowBounds=new RowBounds(pageNo*pageSize,pageSize);
	         WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
	         return woManagementMapper.getNodeNamesBySectorFilter(projectID,vendor,market,tech,domain,subDomain,'%'+term+'%',rowBounds);
	
	}

	public List<ProjectNodeTypeModel> getNodeNamesByFilter(int projectID, String elementType,String type,String vendor, String market, String tech, String domain, String subDomain, String term) {
		int pageSize=50;
 		int pageNo=0;
 		RowBounds rowBounds=new RowBounds(pageNo*pageSize,pageSize);
		WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return woManagementMapper.getNodeNamesByFilter(projectID,elementType,type,vendor,market,tech,domain,subDomain,'%'+term+'%',rowBounds);
    
	}

	public List<Map<String, Object>> getLatestVersionOfWfBySubactivityID(
			String projectID, String subactivityID,
			String subActivityFlowChartDefID, String workFlowName, int wfid) {
		// TODO Auto-generated method stub
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return workOrderMapper.getLatestVersionOfWfBySubactivityID(projectID, subactivityID, subActivityFlowChartDefID, workFlowName,wfid);
	}

	public Boolean checkNotStartedStatusOfWorkOrder1(List<Integer> woid) {
		WorkOrderMapper deleteWorkOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        return deleteWorkOrderMapper.checkNotStartedStatusOfWorkOrder1(woid);

	}

	public void deleteWONodes1(DeleteWOListModel deleteWOListModel) {
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        workOrderMapper.deleteWONodes1(deleteWOListModel);
		
	}

	public void deleteWorkOrder1(DeleteWOListModel deleteWOListModel) {
		WorkOrderMapper deleteWorkOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        deleteWorkOrderMapper.deleteWorkOrder1(deleteWOListModel);

		
	}

	public EmployeeBasicDetails getEmpInfo(String lastModifiedBy) {
		 WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
	        return woManagementMapper.getEmpInfo(lastModifiedBy);
	}

	public List<Integer> getWOPlanList(List<Integer> woid) {
		 WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
	        return woManagementMapper.getWOPlanList(woid);
	}

	public Boolean checkPlanStatus(int woplan) {
		 WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
	        return woManagementMapper.checkPlanStatus(woplan);
	}

	public void inactiveWOPlanList(List<Integer> woplanlist) {
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        workOrderMapper.inactiveWOPlanList(woplanlist);
	}
	public void inactiveWOPlan(int woplan) {
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        workOrderMapper.inactiveWOPlan(woplan);
	}

	public int getWOPlanByWOID(int wOID) {
		 WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
	        return woManagementMapper.getWOPlanByWOID(wOID);
	}
	

	public void updateWoFcDefIdForAssignedWo(int flowchartDefId,int newVersion,int projectId
			,int subActivityId,String workFlowName, int workFlowId) {
		 WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
	     woManagementMapper.updateWoFcDefIdForAssignedWo(flowchartDefId,newVersion,projectId,subActivityId,workFlowName,workFlowId);
	}
	public List<FcStepDetails> getStepBookingDetailsByWoidV1(Integer woId) {
		 WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
	     return woManagementMapper.getStepBookingDetailsByWoidV1(woId);
	}
	/*
	 * Replica getStepBookingDetailsByWoid
	 * */
	public List<Map<String, Object>> getStepBookingDetailsByWoid(Integer woId) {
		 WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
	     return woManagementMapper.getStepBookingDetailsByWoid(woId);
	}

	public boolean addDeliverablePlanMapping(Integer executionPlanId,Integer scopeId,String currentUser) {
		WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
		return woManagementMapper.addDeliverablePlanMapping(executionPlanId,scopeId,currentUser);
	}
	
	public void deleteDeliverablePlanMapping(Integer scopeId) {
		WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
		woManagementMapper.deleteDeliverablePlanMapping(scopeId);
	}
	
	public void insertOutputFileWO(WorkOrderOutputFileModel workOrderOutputFileModel) {
		 WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
	     woManagementMapper.insertOutputFileWO(workOrderOutputFileModel);
		
	}

	public void insertInputFileWO(WorkOrderInputFileModel inputFile) {
		 WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
	     woManagementMapper.insertInputFileWO(inputFile);
	}

	public List<WOOutputFileResponseModel> getWOOutputFile(int woid) {
		 WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
	        return woManagementMapper.getWOOutputFile(woid);
	}

	public List<WOInputFileModel> getWOInputFile(int woid) {
		 WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
	        return woManagementMapper.getWOInputFile(woid);
	}

	public int getCountOfUnassignedWOByWOPLAN(int woplanid) {
		 WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
	        return woManagementMapper.getCountOfUnassignedWOByWOPLAN(woplanid);
	}

	
	public Integer getExecutionPlanIdByScopeId(Integer scopeId) {
		WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
		return woManagementMapper.getExecutionPlanIdByScopeId(scopeId);
	}

	public boolean checkIfStepInStartedState(int woid) {
		 WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
	        return woManagementMapper.checkIfStepInStartedState(woid);
	}

	public void deleteInputFile(int id, String signumID) {
		WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
		woManagementMapper.deleteInputFile(id, signumID);
		
	}

	public void deleteOutputFile(int id, String signumID) {
		WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
		woManagementMapper.deleteOutputFile(id, signumID);	}



	public void editOutputFile(WorkOrderOutputFileModel workOrderOutputFileModel) {
		 WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
	     woManagementMapper.editOutputFile(workOrderOutputFileModel);
	}

	public void deleteOutputFile1(WorkOrderOutputFileModel workOrderOutputFileModel) {
		WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
		woManagementMapper.deleteOutputFile1(workOrderOutputFileModel);
	}

	public void deleteInputFile1(WorkOrderInputFileModel workOrderInputFileModel) {
		WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
		woManagementMapper.deleteInputFile1(workOrderInputFileModel);
	}

	public List<String> checkNameIsPresentOrNot(List<WOOutputFileModel> file, int woid) {
		 WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
	        return woManagementMapper.checkNameIsPresentOrNot(file,woid);
	}

	public void updateExecutionPlanName(final String newPlanName, final String oldPlanName, final int projectId) {
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        workOrderMapper.updateExecutionPlanName(newPlanName, oldPlanName, projectId);
		
	}

	public List<String> getPriorityByName(String priority) {
		 WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
	     return woManagementMapper.getPriorityByName(priority);
	}

	public boolean isExternalSourceExists(String source) {
		WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
	    return woManagementMapper.isExternalSourceExists(source);
	}

	public boolean isValidNodeNameAndNodeType(String nodeName, String nodeType, int projectID) {
		WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
	    return woManagementMapper.isValidNodeNameAndNodeType(nodeName, nodeType, projectID);
	}

	public void createWorkOrder2(CreateWorkOrderModel2 createWorkOrderModel) {
		WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
	    woManagementMapper.createWorkOrder2(createWorkOrderModel);
	}

	public void createNodesForSingleWorkOrder(int getwOPlanID, String[] nodeNames) {
		WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
	    woManagementMapper.createNodesForSingleWorkOrder(getwOPlanID, nodeNames);
	}

	public void editWOPriority(int woid, String priority, String signumID) {
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        workOrderMapper.editWOPriority(woid,priority,signumID);
		
	}

	public List<Map<String, Object>> getWorkOrdersByProjectID(String projectID, String woName, String projectScopeID, String assignedBy, String assignedTo, String nodeName, String marketArea, String role, String signum)
	{
		int pageSize=50;
		int pageNo=0;
		RowBounds rowBounds=new RowBounds(pageNo*pageSize,pageSize);
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
			
        return  workOrderMapper.getWorkOrdersByProjectID(rowBounds, projectID.toLowerCase(),'\''+ woName+'\'', projectScopeID.toLowerCase(),assignedBy.toLowerCase(),assignedTo.toLowerCase(),nodeName.toLowerCase(), marketArea, role, signum);
	}

	public List<Integer> getWorkOrdersByWoplanid(int woplnid) {
		WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
	    return woManagementMapper.getWorkOrdersByWoplanid(woplnid);
	}
	

	public List<Map<String, Object>> getWFListForDeliverablePlan(Integer executionPlanId) {

		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
		return workOrderMapper.getWFListForDeliverablePlan(executionPlanId);
	}

	public List<FinalRecordsForWOCreationModel> getWorkOrderModelForBulkCreation(String source) {
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
		return workOrderMapper.getWorkOrderModelForBulkCreation(source);
	}

	public void deleteProcessedDataFromWOCreationTable(int woHistoryID) {
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
		workOrderMapper.deleteProcessedDataFromWOCreationTable(woHistoryID);
	}

	public void createDOID(DOIDModel doID) {
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
		workOrderMapper.createDOID(doID);
		doID.setDoID((isfCustomIdInsert.generateCustomId(doID.getDoID())));
	}

	public boolean saveEditedWorkOrderDetails(WorkOrderCompleteDetailsModel oldWorkOrderDetails,Date plannedStartDate, Date plannedEndDate) {
		
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
		return workOrderMapper.saveEditedWorkOrderDetails(oldWorkOrderDetails,plannedStartDate,plannedEndDate);
	}

	public Integer getDeliverableIdByNameAndProjectID(String deliverableName, int projectID) {
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
		return workOrderMapper.getDeliverableIdByNameAndProjectID(deliverableName,projectID);
	}

	public void markExecutionPlanFlowComplete(ExecutionPlanFlow flowDetails) {
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
		workOrderMapper.markExecutionPlanFlowComplete(flowDetails);
		
	}

	public void markWoHistoryIDCompleted(int woHistoryID, String modifiedBy) {
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
		workOrderMapper.markWoHistoryIDCompleted(woHistoryID,modifiedBy);
		
	}

	public void insertWOCreationTable(CreateWorkOrderModel woPlanObject, List<CreateWorkOrderModel2> createWorkOrderModel2List) {
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
		workOrderMapper.insertWOCreationTable(woPlanObject, createWorkOrderModel2List);
		
	}

	public void createNodesForWorkOrder(CreateWorkOrderModel2 createWorkOrderModel2, String[] nodeNames) {
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
		workOrderMapper.createNodesForWorkOrder(createWorkOrderModel2, nodeNames);
		
	}

	public ExecutionPlanFlow getExecutionPlanFlowByDOID(int doID, int executionPlanDetailId) {
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
		return workOrderMapper.getExecutionPlanFlowByDOID(doID, executionPlanDetailId);
	}

	public void insertBulkWoErrorTable(CreateWorkOrderModel woPlanObject, String errorDetails, String ERROR_CATEGORY) {
		
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
		workOrderMapper.insertBulkWoErrorTable(woPlanObject, errorDetails , ERROR_CATEGORY);
		
	}

	public List<WorkOrderPlanNodesModel> getNodesByWOId(int workOrderId) {

		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
		return workOrderMapper.getNodesByWOId(workOrderId);
	}

	public void updateWoPlanStartEndDateAndTime(int woPlanID) {
		
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
		workOrderMapper.updateWoPlanStartEndDateAndTime(woPlanID);
	}

	public List<DOWOModel> getWorkOrdersByDoid(int doid) {
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
		return workOrderMapper.getWorkOrdersByDoid(doid);
	}

	public List<ProjectNodeTypeModel> getNodeNamesForDeliverable(int projectID, String elementType, String type,
			String vendor, String market, String techCommaSeparated, String domainCommaSeparated, String term) {
		
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
		return workOrderMapper.getNodeNamesForDeliverable(projectID,elementType,type,vendor,market,techCommaSeparated,domainCommaSeparated,'%'+term+'%');
	}

	public List<Map<String, Object>> getActivity(int subActivityID) {
		
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
		return workOrderMapper.getActivity(subActivityID);
	}

	public List<String> getNodeValidateForDeliverable(int projectID, String elementType, String type, String vendor,
			String market, String techCommaSeparated, String domainCommaSeparated, String nodeNamesCommaSeparated) {

		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
		return workOrderMapper.getNodeValidateForDeliverable(projectID,elementType,type,vendor,market,techCommaSeparated,domainCommaSeparated,nodeNamesCommaSeparated);
	}

	public List<String> getValidateJsonForExternalSource(String externalSourceName) {

		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
		return workOrderMapper.getValidateJsonForExternalSource(externalSourceName);
	}

	public List<Map<String, Object>> getExecutionPlanDetailsByProjectIDSubactivityID(Integer projectID,
			Integer subActivityID, Integer subActivityFlowChartDefID) {
		
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
		return workOrderMapper.getExecutionPlanDetailsByProjectIDSubactivityID(projectID,subActivityID,subActivityFlowChartDefID);
	}

	public List<WorkOrderOutputFileModel> getWOOutputFileDetails(int woid) {

		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
		return workOrderMapper.getWOOutputFileDetails(woid);
	}

	public List<WorkOrderOutputFileModel> getWOOutputFileDetailsByWoIDAndWoOutputFileModel(
			WOOutputFileModel woOutputFileModel, int woid) {
		
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
		return workOrderMapper.getWOOutputFileDetailsByWoIDAndWoOutputFileModel(woOutputFileModel, woid);
	}
	public List<LinkedHashMap<String, Object>> getAllBookingDetails(int WoID,String SignumID, boolean flag, String stepId) {
    	WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
        List<LinkedHashMap<String, Object>> result = workOrderMapper.getAllBookingDetails(WoID,SignumID,flag,stepId);
    	return result;
    }

	public int checkOutputFileCount(WorkOrderOutputFileModel woOutputFileModel) {

		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
		return workOrderMapper.checkOutputFileCount(woOutputFileModel);
	}

	public String validateStepIdAndExecutionType(String flowChartStepId, int flowChartDefID) {
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
		return workOrderMapper.validateStepIdAndExecutionType(flowChartStepId,flowChartDefID);
	}


	public void updateDefIdForAssignWo(int oldDefID) {
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
		workOrderMapper.updateDefIdForAssignWo(oldDefID);
	}

	public void updateWoAutoSense(int projectID, int subActivityID,
			 int flowcharDefId) {
		 WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
	     woManagementMapper.updateWoAutoSense(projectID,subActivityID,flowcharDefId);
		
	}

	public List<InProgressTaskModel> getInprogressTaskBySignum(String signum) {
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
		return workOrderMapper.getInprogressTaskBySignum(signum);
	}

	public InProgressNextStepModal getNextStepInfoForSignumId(String flowChartStepID, int flowChartDefID) {
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);		
		return workOrderMapper.getNextStepInfoForSignumId(flowChartStepID,flowChartDefID);
	}
	
	public InProgressNextStepModal getNextStepInfoWithProficiency(String flowChartStepID, int flowChartDefID, int proficiencyID) {
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);		
		return workOrderMapper.getNextStepInfoWithProficiency(flowChartStepID,flowChartDefID, (proficiencyID==0)?21:proficiencyID);
	}

	public List<String> getValidateJsonForApi(String apiName, String externalSourceName) {
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
		return workOrderMapper.getValidateJsonForApi(apiName, externalSourceName);
	}

	public boolean validateFailureReason(String reason, String category) {
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
		return workOrderMapper.validateFailureReason(reason,category);
	}

	public List<String> getStepWOIDBookings(int wOID, String stepid) {
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
		return workOrderMapper.getStepWOIDBookings(wOID,stepid);
	}

	public String getExecutionType(String stepid, int taskID, int flowChartDefID) {
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
		return workOrderMapper.getExecutionType(stepid,taskID,flowChartDefID);
	}

	public boolean validateStepIDforStepType(String flowChartStepId, int flowChartDefId) {
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
		return workOrderMapper.validateStepIDforStepType(flowChartStepId,flowChartDefId);
	}

	public int getTaskIdONStepIdAndFlowChartdefId(String flowChartStepId, int flowChartDefID) {
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
		return workOrderMapper.getTaskIdONStepIdAndFlowChartdefId(flowChartStepId,flowChartDefID);
	}

	public String getPreviousStepStepId(String flowChartStepId, int flowChartDefID) {
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
		return workOrderMapper.getPreviousStepStepId(flowChartStepId,flowChartDefID);
	}

	public boolean checkPreviousStepCompleted(String previousStepStepId, int flowChartDefID,int woId, String executionType) {
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
		return workOrderMapper.checkPreviousStepCompleted(previousStepStepId,flowChartDefID,woId,executionType);
	}

	public boolean checkDecisionStepComplitionStatus(String previousStepStepId, int flowChartDefID, int woId) {
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
		return workOrderMapper.checkDecisionStepComplitionStatus(previousStepStepId,flowChartDefID,woId);
	}

	public boolean checkStepWithDecisionValue(String previousStepStepId,int flowChartDefID,int woId, String decisionValue) {
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
		return workOrderMapper.checkStepWithDecisionValue(previousStepStepId,flowChartDefID,woId,decisionValue);
	}

	public BookingDetailsModel getBookingDetailsOnWoidTaskIdDefIdStepId(int wOID, int taskID, int flowchartdefid,
			String stepID) {
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
		return workOrderMapper.getBookingDetailsOnWoidTaskIdDefIdStepId( wOID,  taskID,  flowchartdefid,
				 stepID);
	}

	public String getLatestConpletedEnabledStep(int woID) {
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
		return workOrderMapper.getLatestConpletedEnabledStep(woID);
	}

	public boolean checkIfNextStepDisabled(String flowChartStepID, int flowChartDefID, int workOrderProficiencyLevel) {
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
		return workOrderMapper.checkIfNextStepDisabled(flowChartStepID, flowChartDefID, workOrderProficiencyLevel);
	}

	public String checkWorkOrderForSrRequest(List<Integer> listofWoid) {
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
		return workOrderMapper.checkWorkOrderForSrRequest(listofWoid);
	}

	public Integer checkWorkOrderLinkedWithSRID(int wOID) {
		WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
		return workOrderMapper.checkWorkOrderLinkedWithSRID(wOID);
	}
	
	    public boolean validateWoid(int wOID) {
		   WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
		   return  workOrderMapper.validateWoid(wOID);		
	    }
		
		public boolean validateWOStatusForWOName(int wOID) {
			   WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
			   return  workOrderMapper.validateWOStatusForWOName(wOID);		
		}
		
		public boolean validateWOStatusForStartDate(int wOID) {
			   WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
			   return  workOrderMapper.validateWOStatusForStartDate(wOID);		
		}
		
		public boolean validateSource(String externalSourceName) {
			   WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
			   return  workOrderMapper.validateSource(externalSourceName);		
		}
		
		public boolean validateSignumForPMDR(String signumID,int projectID) {
			   WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
			   return  workOrderMapper.validateSignumForPMDR(signumID,projectID);		
		}
		
		public boolean validateExternalSourceForErisite(String externalSourceName) {
			   WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
			   return  workOrderMapper.validateExternalSourceForErisite(externalSourceName);		
		}
		
		public boolean validateExternalSourceForErisiteWO(String createdBy) {
			   WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
			   return  workOrderMapper.validateExternalSourceForErisiteWO(createdBy);		
		}
		
		public String getCreatedByOfWOID(int wOID) {
			   WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
			   return  workOrderMapper.getCreatedByOfWOID(wOID);		
		}
		 public void addNetworkWorkOrderPlan(CreateWorkOrderNetworkElementModel wOPlanObject,String signum) {
		        WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
		        woManagementMapper.addNetworkWorkOrderPlan(wOPlanObject,signum);
		        wOPlanObject.setwOPlanID(isfCustomIdInsert.generateCustomId(wOPlanObject.getwOPlanID()));
		    }

		public void createWorkOrderNodes(CreateWorkOrderModel2 createModel, List<WorkOrderPlanNodesModel> listOfNode) {
			WorkOrderMapper workOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
			workOrderMapper.createWorkOrderNodes(createModel,listOfNode);
			
		}
		
		public List<ProjectModel> getProjectBySignumForProjectQueue(String signum) {
			WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
	        return woManagementMapper.getProjectBySignumForProjectQueue(signum);
		}

		
		public boolean validateLastModifiedByForPMDRNE(String logedInSignum, int projectId, int woid) {
			WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
	        return woManagementMapper.validateLastModifiedByForPMDRNE(logedInSignum,projectId,woid);
		}

		public boolean validateLastModifiedForPMDRBookedResource(String lastModifiedBy, int projectID) {
			WorkOrderMapper woManagementMapper = sqlSession.getMapper(WorkOrderMapper.class);
	        return woManagementMapper.validateLastModifiedForPMDRBookedResource(lastModifiedBy,projectID);
		}

		public boolean checkNotStartedStatusOfWorkOrderV1(int wOID) {
			 WorkOrderMapper deleteWorkOrderMapper = sqlSession.getMapper(WorkOrderMapper.class);
		     return deleteWorkOrderMapper.checkNotStartedStatusOfWorkOrderV1(wOID);
		}
	
}
