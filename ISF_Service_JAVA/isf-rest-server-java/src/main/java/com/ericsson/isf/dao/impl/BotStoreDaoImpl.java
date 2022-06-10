package com.ericsson.isf.dao.impl;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.ParameterMode;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.procedure.ProcedureCall;
import org.hibernate.procedure.ProcedureOutputs;
import org.hibernate.type.BinaryType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ericsson.isf.dao.BotStoreDao;
import com.ericsson.isf.model.DesktopLibraryResponseModel;
import com.ericsson.isf.model.LanguageBaseVersionModel;
import com.ericsson.isf.model.UserLibraryVersionModel;
import com.ericsson.isf.model.UserWiseDesktopVersionModel;
import com.ericsson.isf.model.botstore.DesktopAppResponseModel;
import com.ericsson.isf.model.botstore.TblProjects;
import com.ericsson.isf.model.botstore.TblRpaBotExecutionDetail;
import com.ericsson.isf.model.botstore.TblRpaBotFile;
import com.ericsson.isf.model.botstore.TblRpaBotFileNew;
import com.ericsson.isf.model.botstore.TblRpaBotrequirement;
import com.ericsson.isf.model.botstore.TblRpaBotstaging;
import com.ericsson.isf.model.botstore.TblRpaBottesting;
import com.ericsson.isf.model.botstore.TblRpaDeployedBot;
import com.ericsson.isf.model.botstore.TblRpaDeployedBotView;
import com.ericsson.isf.model.botstore.TblRpaRequest;
import com.ericsson.isf.model.botstore.TblRpaRequestTool;
import com.ericsson.isf.model.botstore.TblWorkFlowLinks;
import com.fasterxml.jackson.annotation.JsonView;

/**
 *
 * @author esaabeh
 */
@Repository
public class BotStoreDaoImpl implements BotStoreDao{

	@Autowired
	SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
    public TblRpaRequest getRPARequestDetails(int rpaRequestID) {
        return (TblRpaRequest) sessionFactory.getCurrentSession()
        		.createQuery("from TblRpaRequest where rpaRequestID = :rpaRequestID AND isActive = :isActive")
        		.setParameter("rpaRequestID", rpaRequestID).setParameter("isActive", 1).getSingleResult();
    }

	@SuppressWarnings("unchecked")
    public List<Object[]> getRPARequestOtherDetails(int rpaRequestID) {
    	return sessionFactory.getCurrentSession().createSQLQuery("select rpaRequestID, ProjectID from transactionalData.TBL_RPA_REQUEST where rpaRequestID="+rpaRequestID)
    			.list();
    }

	@SuppressWarnings("unchecked")
    public List<Object[]> getBotExeDetails() {
    	//return sessionFactory.getCurrentSession().createSQLQuery(""
    		//	+ "select BOTID,[Bot Executed Count],[Bot execution(Hours)],[Bot execution Fail(Count)] from transactionalData.TBL_DETAILDUMP_DEOPLYED_BOTS (nolock) where BOTID is not null").list();
    	
    	return sessionFactory.getCurrentSession().createSQLQuery(""
    			+ "select BOTID,[BotExecutedCount],BotexecutionHours,BotexecutionFailCount from transactionalData.TBL_DETAILDUMP_DEOPLYED_BOTS (nolock) where BOTID is not null").list();
    }

	@SuppressWarnings("unchecked")
    public List<Object[]> getRPARequestStepDetails(int rpaRequestID, int WorkflowDEFID, int SubactivityID, int TaskID, int WFSTEPID) {
    	return sessionFactory.getCurrentSession()
    			.createSQLQuery("select distinct wf.WorkFlowName, subact.Activity, subact.SubActivity, step.Task "
    			+ "from [transactionalData].[TBL_RPA_REQUEST] req "
    			+ "join  [transactionalData].[TBL_SUBACTIVITY_FLOWCHART_DEF] wf on req.WorkflowDEFID=wf.SubActivityFlowChartDefID "
    			+ "join [refData].[TBL_SUBACTIVITY] subact on  req.SubactivityID = subact.SubActivityID "
    			+ "join [transactionalData].[TBL_FLOWCHART_STEP_DETAILS] step on  req.TaskID = step.TaskID and req.WFSTEPID = step.StepID "
    			+ "where req.rpaRequestID="+rpaRequestID
    			+ " and req.WorkflowDEFID="+WorkflowDEFID
    			+ " and req.SubactivityID="+SubactivityID
    			+ " and req.TaskID="+TaskID
    			+ " and req.WFSTEPID="+WFSTEPID)
    			.list();
    }

	@SuppressWarnings("unchecked")
    public List<Object[]> getRPARequestStepDetailsNew() {
    	return sessionFactory.getCurrentSession()
    			.createSQLQuery("select distinct req.rpaRequestID ,tsfd.WorkFlowName, subact.Activity, subact.SubActivity ,step.Task,step.StepID,step.StepName,tsfd.WFID \r\n " + 
    					" from [transactionalData].[TBL_RPA_REQUEST] req \r\n" + 
    					" join [refData].[TBL_SUBACTIVITY] subact on  subact.SubactivityID = req.SubActivityID \r\n" + 
    					" join [transactionalData].[TBL_SUBACTIVITY_FLOWCHART_DEF] tsfd \r\n" + 
    					" on tsfd.ProjectID=req.ProjectID and tsfd.SubActivityID=req.SubactivityID and tsfd.SubActivityFlowChartDefID=req.WorkflowDEFID \r\n" + 
    					" join [transactionalData].[TBL_FLOWCHART_STEP_DETAILS] step on step.StepID=req.WFSTEPID \r\n" + 
    					" and step.TaskID=req.TaskID and step.SubActivityFlowChartDefID=req.WorkflowDEFID").list();
    }

	@SuppressWarnings("unchecked")
    public List<Object[]> getBotStepDetails(int botId) {
    	return sessionFactory.getCurrentSession()
    			.createSQLQuery("select distinct req.rpaRequestID, proj.projectName, wf.WorkFlowName, subact.Activity, subact.SubActivity, step.Task, step.StepID,step.StepName,wf.WFID "
    			+ "from [transactionalData].[TBL_RPA_REQUEST] (nolock) req "
    			+ "join  [transactionalData].[TBL_SUBACTIVITY_FLOWCHART_DEF] wf (nolock) on req.WorkflowDEFID=wf.SubActivityFlowChartDefID "
    			+ "join [refData].[TBL_SUBACTIVITY] subact (nolock) on  req.SubactivityID = subact.SubActivityID "
    			+ "join [transactionalData].[TBL_FLOWCHART_STEP_DETAILS] step (nolock) on  req.TaskID = step.TaskID "
    			+ "join [transactionalData].[TBL_PROJECTS] proj (nolock) on  req.ProjectID = proj.ProjectID "
    			//+ "and req.WFSTEPID = step.StepID").list();
    			+ "and convert(varchar(100), req.WFSTEPID) = convert(varchar(100), step.StepID) "
    			+ "and rpaRequestID="+botId).list();
    }
    
    @SuppressWarnings("unchecked")
    public List<Object[]> getBotStepDetailsForDeployedBOT(int botId) {
    	return sessionFactory.getCurrentSession()
    			.createSQLQuery("select distinct top 1 req.rpaRequestID, proj.projectName, sfd.WorkFlowName, subact.Activity, subact.SubActivity, task.Task, fsd.StepID,fsd.StepName,sfd.WFID "
    			+  " from [transactionalData].[TBL_RPA_REQUEST] (nolock) req "
    			+  " left join [transactionalData].[TBL_RPA_DeployedBOT] dbot on dbot.rpaRequestID =req.rpaRequestID "
    			+  " left join [transactionalData].[TBL_FLOWCHART_STEP_DETAILS] fsd on   fsd.RpaID = req.rpaRequestID "
    			+  " left join [transactionalData].[TBL_SUBACTIVITY_FLOWCHART_DEF] sfd on sfd.SubActivityFlowChartDefID=fsd.SubActivityFlowChartDefID "
    			+  " left join [refData].[TBL_TASK] task on task.TaskID=req.TaskID "
    			+  " left join [refData].[TBL_SUBACTIVITY] subact (nolock) on  req.SubactivityID = subact.SubActivityID "
    			+  " left join [transactionalData].[TBL_PROJECTS] proj (nolock) on  req.ProjectID = proj.ProjectID "
    			+ " where req.rpaRequestID="+botId).list();
    }



	@SuppressWarnings("unchecked")
    public List<TblRpaRequest> getRPARequestById(int rpaRequestID) {
        return (List<TblRpaRequest>) sessionFactory.getCurrentSession()
        		.createQuery("from TblRpaRequest where rpaRequestID = :rpaRequestID").setParameter("rpaRequestID", rpaRequestID).getResultList();

    }

	@SuppressWarnings("unchecked")
    public List<TblRpaRequest> getRPARequests() {
        return (List<TblRpaRequest>) sessionFactory.getCurrentSession()
        		.createQuery("from TblRpaRequest where isActive = :isActive").setParameter("isActive", 1).getResultList();

    }

	@SuppressWarnings("unchecked")
    public List<TblRpaRequest> getRPARequests(String signum) {
        return (List<TblRpaRequest>) sessionFactory.getCurrentSession()
        		.createQuery("from TblRpaRequest where isActive = :isActive AND createdBy = :createdBy")
        		.setParameter("isActive", 1).setParameter("createdBy", signum).getResultList();
    }

	@SuppressWarnings("unchecked")
    public List<TblRpaBotstaging> getAssignedRequestListForDev(String signum) {
        return (List<TblRpaBotstaging>) sessionFactory.getCurrentSession()
        		.createQuery("from TblRpaBotstaging where assignedTo = :assignedTo AND isActive = :isActive AND isnull(BOTLanguage,'') NOT IN ('MACRO','PENGUIN')").setParameter("assignedTo", signum).setParameter("isActive", 1).getResultList();
    }


	@SuppressWarnings("unchecked")
    public List<TblProjects> getProjectById(int projectId) {
		


		return (List<TblProjects>) sessionFactory.getCurrentSession()
        		.createQuery("from TblProjects where projectId = :projectId").setParameter("projectId", projectId).getResultList();

/*
		return (List<TblProjects>) sessionFactory.getCurrentSession()
        		.createQuery("SELECT servAreaId, startDate, endDate, servAreaId from TblProjects where projectId = :projectId").setParameter("projectId", projectId).getResultList();
*/
		//return (TblProjects) sessionFactory.getCurrentSession().find(TblProjects.class, projectId);

    }
	
	@SuppressWarnings("unchecked")
    public List<TblProjects> getProjectByName(String projName) {
		
		
/*
		return (List<TblProjects>) sessionFactory.getCurrentSession()
        		.createQuery("from TblProjects where cpm = :projectName").setParameter("projectName", projName).getResultList();
*/

		List<TblWorkFlowLinks> l= (List<TblWorkFlowLinks>) sessionFactory.getCurrentSession()
        		.createQuery("from TblWorkFlowLinks where z = :projectName").setParameter("projectName", Integer.parseInt(projName)).getResultList();
		
	
			
		
		List<TblProjects> projects= null;
		return projects;
		
    }


	@SuppressWarnings("unchecked")
    public List<TblProjects> getProjectDetails(String rpaRequestID) {
        return (List<TblProjects>) sessionFactory.getCurrentSession()
        		.createQuery("from TblRpaRequest where rpaRequestID = :rpaRequestID").setParameter("rpaRequestID", rpaRequestID).getResultList();
    }

	@SuppressWarnings("unchecked")
    public List<TblRpaDeployedBot> getBOTs() {
        return (List<TblRpaDeployedBot>) sessionFactory.getCurrentSession()
        		.createQuery("from TblRpaDeployedBot db").getResultList();
    }

	@SuppressWarnings("unchecked")
    public List<TblRpaDeployedBot> getBOTs(int taskId) {
        return (List<TblRpaDeployedBot>) sessionFactory.getCurrentSession()
        		.createQuery("from TblRpaDeployedBot db where db.tblRpaRequest.taskId = :taskId AND db.isActive = :isActive AND isnull(BOTLanguage,'') NOT IN :BOTLanguage").setParameter("taskId", taskId).setParameter("isActive", 1).setParameter("BOTLanguage", "('MACRO','PENGUIN')").getResultList();
    }

	//@JsonView(TblRpaDeployedBotView.Object.class)
	@SuppressWarnings("unchecked")
	public TblRpaDeployedBot getBotDetailById(int botId) {
		Object tblRpaDeployedBot = null;
		try{
			tblRpaDeployedBot = sessionFactory.getCurrentSession()
					.createQuery("FROM TblRpaDeployedBot where BOTID = :BOTID order by systemid DESC").setParameter("BOTID", botId).getSingleResult();
		}
		catch (NoResultException nre){
			//Ignore this because as per your logic this is ok!
		}

		if(tblRpaDeployedBot == null) {
			return null;
		}else {
			return (TblRpaDeployedBot) tblRpaDeployedBot;
		}

    }
	
	@SuppressWarnings("unchecked")
	public void createRpaRequest(TblRpaRequest tblRpaRequest) throws HibernateException{
        sessionFactory.getCurrentSession().saveOrUpdate(tblRpaRequest);
    }

	@SuppressWarnings("unchecked")
	public void createRpaBotrequirement(TblRpaBotrequirement tblRpaBotrequirement) throws HibernateException{
        sessionFactory.getCurrentSession().saveOrUpdate(tblRpaBotrequirement);
    }

	@SuppressWarnings("unchecked")
	public void createRpaRequestTool(TblRpaRequestTool tblRpaRequestTool) throws HibernateException{
        sessionFactory.getCurrentSession().saveOrUpdate(tblRpaRequestTool);
    }

	@SuppressWarnings("unchecked")
	public void createBotStaging(TblRpaBotstaging tblRpaBotstaging) throws HibernateException{
		sessionFactory.getCurrentSession().saveOrUpdate(tblRpaBotstaging);
    }

	@SuppressWarnings("unchecked")
	public void updateBotStaging(TblRpaBotstaging tblRpaBotstaging) throws HibernateException{
		sessionFactory.getCurrentSession().update(tblRpaBotstaging);
    }

	@SuppressWarnings("unchecked")
    public TblRpaBotstaging getRPAStagingData(int rpaRequestID) {
        
		List<TblRpaBotstaging> list= (List<TblRpaBotstaging>) sessionFactory.getCurrentSession()
        		.createQuery("from TblRpaBotstaging where rpaRequestID = :rpaRequestID AND isActive = :isActive order by createdOn desc").setParameter("rpaRequestID", rpaRequestID).setParameter("isActive", 1).getResultList();
        
		TblRpaBotstaging st= null;
		if(null!= list && list.size()>0)
			st= list.get(0);
		
        return st;
    }
	
	//getting all the requests staging data with latest stage entry:
	@SuppressWarnings("unchecked")
    public List<Object[]> getRPAStagingDataNew() {
    	return sessionFactory.getCurrentSession().createSQLQuery("SELECT req.rpaRequestID, "
    			+ "stg1.SRNO,stg1.BOTID,stg1.AssignedTo,stg1.BOTName,stg1.RPAExecutionTime,stg1.BOTLanguage, "
    			+ "stg1.TargetExecutionFileName,stg1.ModuleClassName,stg1.ModuleClassMethod,stg1.ParallelWOExecution,stg1.ReuseFactor, "
    			+ "stg1.LineOfCode,stg1.Status,stg1.StatusDescription,stg1.CreatedBY,stg1.CreatedOn,stg1.ModifiedBy,stg1.ModifiedOn "
    			+ "FROM transactionalData.TBL_RPA_REQUEST as req "
    			+ "OUTER APPLY (SELECT top(1) * "
    			+ "FROM transactionalData.TBL_RPA_BOTSTAGING as stg WHERE req.rpaRequestID=stg.rpaRequestID "
    			+ "and stg.isActive=1 order by stg.SRNO desc) as stg1 "
    			+ "WHERE req.isActive=1").list();
    }

	@SuppressWarnings("unchecked")
    public List<TblRpaBotstaging> getStagingBotsByReqId(int rpaRequestID) {
        return (List<TblRpaBotstaging>) sessionFactory.getCurrentSession()
        		.createQuery("from TblRpaBotstaging where rpaRequestID = :rpaRequestID AND isActive = :isActive").setParameter("rpaRequestID", rpaRequestID).setParameter("isActive", 1).getResultList();
    }

	@SuppressWarnings("unchecked")
    public List<TblRpaBotstaging> getRPAStagingForAccepted(int rpaRequestID, String userSignum, String status) {
        return (List<TblRpaBotstaging>) sessionFactory.getCurrentSession()
        		.createQuery("from TblRpaBotstaging where rpaRequestID = :rpaRequestID AND assignedTo = :assignedTo AND status LIKE :status AND isActive = :isActive")
        		.setParameter("rpaRequestID", rpaRequestID).setParameter("assignedTo", userSignum).setParameter("status", status+"%").setParameter("isActive", 1).getResultList();
    }

	@SuppressWarnings("unchecked")
    public List<TblRpaBotstaging> getRPAStagingForAccepted(int rpaRequestID, String status) {
        return (List<TblRpaBotstaging>) sessionFactory.getCurrentSession()
        		.createQuery("from TblRpaBotstaging where rpaRequestID = :rpaRequestID AND status LIKE :status AND isActive = :isActive")
        		.setParameter("rpaRequestID", rpaRequestID).setParameter("status", status+"%").setParameter("isActive", 1).getResultList();
    }

	@SuppressWarnings("unchecked")
    public List<TblRpaBotstaging> getRPAStagingForReject(int rpaRequestID) {
        return (List<TblRpaBotstaging>) sessionFactory.getCurrentSession()
        		.createQuery("from TblRpaBotstaging where rpaRequestID = :rpaRequestID AND isActive = :isActive")
        		.setParameter("rpaRequestID", rpaRequestID).setParameter("isActive", 1).getResultList();
    }

	@SuppressWarnings("unchecked")
    public List<TblRpaBotstaging> getRPAStagingByReqAndStatus(int rpaRequestID, String status) {
        return (List<TblRpaBotstaging>) sessionFactory.getCurrentSession()
        		.createQuery("from TblRpaBotstaging where rpaRequestID = :rpaRequestID AND status LIKE :status AND isActive = :isActive")
        		.setParameter("rpaRequestID", rpaRequestID).setParameter("status", status+"%").setParameter("isActive", 1).getResultList();
    }

	@SuppressWarnings("unchecked")
	public void createRpaDeployBot(TblRpaDeployedBot tblRpaDeployedBot) throws HibernateException{
		sessionFactory.getCurrentSession().saveOrUpdate(tblRpaDeployedBot);
    }
	
	@SuppressWarnings("unchecked")
	public void createBotTestingRequest(TblRpaBottesting botTest) throws HibernateException{
		sessionFactory.getCurrentSession().saveOrUpdate(botTest);
    }

	@SuppressWarnings("unchecked")
	public TblRpaBottesting getTestingDataById(int testId) {
        return (TblRpaBottesting) sessionFactory.getCurrentSession()
        		.createQuery("FROM TblRpaBottesting where testId = :testId").setParameter("testId", testId).getSingleResult();
    }

	@SuppressWarnings("unchecked")
	public TblRpaBottesting getTestingDataByReq(Integer reqId, String signum) {
        List<TblRpaBottesting> testList= null;
		
        testList= (List<TblRpaBottesting>) sessionFactory.getCurrentSession()
        		.createQuery("FROM TblRpaBottesting bt where bt.tblRpaRequest.rpaRequestId = :reqId AND bt.createdBy =: signum AND bt.status = :status")
        		.setParameter("reqId", reqId)
        		.setParameter("signum", signum)
        		.setParameter("status", "INPROGRESS").getResultList();
        
        if(null!= testList && testList.size()>0)
        	return testList.get(0);
        else
        	return null;
        
    }

	@SuppressWarnings("unchecked")
    public List<TblRpaRequest> getRequestsByTaskIdAndWFDefId(int taskId, int workflowDefid) {
        return (List<TblRpaRequest>) sessionFactory.getCurrentSession()
        		.createQuery("from TblRpaRequest where taskId =: taskId AND workflowDefid =: workflowDefid AND isActive = :isActive")
        		.setParameter("taskId", taskId).setParameter("workflowDefid", workflowDefid).setParameter("isActive", 1).getResultList();
    }


	@SuppressWarnings("unchecked")
    public List<Object[]> getStageBotsStatus() {
    	return sessionFactory.getCurrentSession().createSQLQuery("SELECT rpaRequestID, UPPER(Status) from transactionalData.TBL_RPA_BOTSTAGING").list();
    }


	@SuppressWarnings("unchecked")
	public void deleteBotStage(TblRpaBotstaging tblRpaBotstaging) throws HibernateException{
		sessionFactory.getCurrentSession().delete(tblRpaBotstaging);
    }

	
	@SuppressWarnings("unchecked")
	public List<TblRpaBottesting> getTestingDataByStatusSignum(String status, String signum) {
        List<TblRpaBottesting> testList= null;
		
        testList= (List<TblRpaBottesting>) sessionFactory.getCurrentSession()
        		.createQuery("FROM TblRpaBottesting bt where bt.createdBy =: signum AND bt.status = :status")
        		.setParameter("signum", signum)
        		.setParameter("status", status).getResultList();
        
        return testList;
        
    }

	
	@SuppressWarnings("unchecked")
    public List<DesktopAppResponseModel> getDesktopAppVersionv1() {
		return (List<DesktopAppResponseModel>) sessionFactory.getCurrentSession()
				.createNativeQuery("select [UpdateType],[Version],[UpdatedOn],[Active],[ISFDesktopUpdatesID]\r\n" + 
						"from\r\n" + 
						"(\r\n" + 
						"select [UpdateType],[Version],[UpdatedOn],[Active],[ISFDesktopUpdatesID],max(Version) over (partition by UpdateType) max_version from   refData.TBL_ISFDesktopUpdates where Active=1\r\n" + 
						") t\r\n" + 
						"where Version = max_version", DesktopAppResponseModel.class)
				.getResultList();
	}

    
	@SuppressWarnings("unchecked")
    public byte[] botDownloadUsingProc(String botPath){
    	//return sessionFactory.getCurrentSession().createSQLQuery("CALL ProcGetFileContent(:botPath)")
    		//	.setParameter("botPath", botPath).list();
    	
    	ProcedureCall query= sessionFactory.getCurrentSession().createStoredProcedureCall("ProcGetFileContent");
        query.registerParameter(
            "pFileName", String.class, ParameterMode.IN).bindValue(botPath);
        query.registerParameter("pFileContent", BinaryType.class, ParameterMode.OUT);
        query.registerParameter("pErrorFlag", Integer.class, ParameterMode.OUT);
        query.registerParameter("pErrorMsg", String.class, ParameterMode.OUT);

		ProcedureOutputs procedureResult=query.getOutputs();
		byte[] file= (byte[]) procedureResult.getOutputParameterValue("pFileContent");
		//System.out.println((Integer)procedureResult.getOutputParameterValue("pErrorFlag"));
		//System.out.println((String)procedureResult.getOutputParameterValue("pErrorMsg"));
		
		//System.out.println("Max Result : "+query.getFetchSize()+" : "+file.length);
		
		return file;
    }

	
	@SuppressWarnings("unchecked")
    public List<Object[]> getWfOwner(int workflowDefId) {
    	return sessionFactory.getCurrentSession().createSQLQuery("select WFOwner, WorkFlowName from transactionalData.TBL_SUBACTIVITY_FLOWCHART_DEF where SubActivityFlowChartDefID="+workflowDefId).list();
    }

	@SuppressWarnings("unchecked")
    public List<Object[]> getSpmSignum(int projectId) {
    	return sessionFactory.getCurrentSession().createSQLQuery("select ProjectCreator, ProjectName from [transactionalData].[TBL_PROJECTS] proj where proj.ProjectID="+projectId).list();
    }
    
    @SuppressWarnings("unchecked")
	public void createUpdateRpaFile(TblRpaBotFile tblRpaBotFile) throws HibernateException{
        sessionFactory.getCurrentSession().saveOrUpdate(tblRpaBotFile);
        
    }
    
    @SuppressWarnings("unchecked")
	public void createUpdateRpaFileNew(TblRpaBotFileNew tblRpaBotFileNew) throws HibernateException{
        sessionFactory.getCurrentSession().saveOrUpdate(tblRpaBotFileNew);
        
    }
    

	@SuppressWarnings("unchecked")
	@Override
	public List<TblRpaBotFile> getRPAFile(String rpaRequestID, String fileName) 
	{
		List<TblRpaBotFile> testList= null;
    	
    	testList = sessionFactory.getCurrentSession()
        		.createQuery("from TblRpaBotFile where rpaRequestID = :rpaRequestID AND fileName = :fileName AND isActive = :isActive")
        		.setParameter("rpaRequestID", rpaRequestID).setParameter("fileName", fileName).setParameter("isActive", 1).list();
    	
    	return testList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TblRpaBotFileNew> getRPAFileNew(String rpaRequestID, String fileName) 
	{
		List<TblRpaBotFileNew> testList= null;
    	
    	testList = sessionFactory.getCurrentSession()
        		.createQuery("from TblRpaBotFileNew where rpaRequestID = :rpaRequestID AND fileName = :fileName AND isActive = :isActive")
        		.setParameter("rpaRequestID", rpaRequestID).setParameter("fileName", fileName).setParameter("isActive", 1).list();
    	
    	return testList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TblRpaBotExecutionDetail> getOUTFile(String OutputFileName) 
	{
		List<TblRpaBotExecutionDetail> testList= null;
    	
    	testList = sessionFactory.getCurrentSession()
        		.createQuery("from TblRpaBotExecutionDetail where OutputFileName = :OutputFileName")
        		.setParameter("OutputFileName", OutputFileName).list();
    	
    	return testList;
	}

	@SuppressWarnings("unchecked")
	public void createRpaInputFile(TblRpaBotExecutionDetail tblRpaBotExecutionDetail) throws HibernateException{
        sessionFactory.getCurrentSession().saveOrUpdate(tblRpaBotExecutionDetail);
    }

	@SuppressWarnings("unchecked")
	@Override
	public List<TblRpaBotFile> getRPAFileDetails(int reqId)
	{
       List<TblRpaBotFile> testList= null;
       testList = sessionFactory.getCurrentSession()
        		.createQuery("from TblRpaBotFile where rpaRequestID = :rpaRequestID AND isActive = :isActive")
        		.setParameter("rpaRequestID", reqId).setParameter("isActive", 1).list();
       return testList;	
    }

	@Override
	public UserWiseDesktopVersionModel getDesktopVersionBySignum(String signum) {
		List<UserWiseDesktopVersionModel> model = sessionFactory.getCurrentSession()
        	.createQuery("from UserWiseDesktopVersionModel v where SignumID = :signum order by v.updatedOn desc")
        	.setParameter("signum", signum).setMaxResults(1).list();
		if(model.size() != 0)
			return model.get(0);
		else
			return null;
	}

	@Override
	public void saveDesktopVersionBySignum(UserWiseDesktopVersionModel model) {
		sessionFactory.getCurrentSession().saveOrUpdate(model);
	}

	@Override
	public TblRpaBotExecutionDetail getCascadedBOTInFile(String signum, Integer woNo, Integer taskId) {
		TblRpaBotExecutionDetail testList= null;
    	
    	testList = (TblRpaBotExecutionDetail) sessionFactory.getCurrentSession()
        		.createQuery("from TblRpaBotExecutionDetail d where Signum = :Signum AND WONo = :WONo AND TaskID = :TaskID  order by d.srno desc")
        		.setParameter("Signum", signum).setParameter("WONo", woNo).setParameter("TaskID", taskId).setMaxResults(1).uniqueResult();
    	
    	return testList;
	}

	@Override
	public void updateRpaDeployedBot(TblRpaDeployedBot deployedBot) {
		sessionFactory.getCurrentSession()
		.createQuery("Update TblRpaDeployedBot set ModifiedBy = :ModifiedBy, ModifiedOn = :ModifiedOn, isInputRequired = :isInputRequired "
				+ "where BOTID = :BOTID")
		.setParameter("ModifiedBy", deployedBot.getModifiedBy())
		.setParameter("ModifiedOn", deployedBot.getModifiedOn())
		.setParameter("isInputRequired", deployedBot.getIsInputRequired())
		.setParameter("BOTID", deployedBot.getBotid())
		.executeUpdate();
    }

	@Override
	public void updateRpaRequestInputFileStatus(TblRpaDeployedBot deployedBot) {
		sessionFactory.getCurrentSession()
		.createQuery("Update TblRpaRequest set ModifiedBy = :ModifiedBy, ModifiedOn = :ModifiedOn, isInputRequired = :isInputRequired "
				+ "where rpaRequestID = :rpaRequestID")
		.setParameter("ModifiedBy", deployedBot.getModifiedBy())
		.setParameter("ModifiedOn", deployedBot.getModifiedOn())
		.setParameter("isInputRequired", deployedBot.getIsInputRequired())
		.setParameter("rpaRequestID", deployedBot.getBotid())
		.executeUpdate();
    }

	@Override
	public int stopInprogressBot(Integer rpaRequestId, String  signum, int isTestingSuccessful, String status) {
		return sessionFactory.getCurrentSession()
		.createQuery("Update TblRpaBottesting set isTestingSuccessful=:isTestingSuccessful, Status=:Status where rpaRequestID=:rpaRequestID and CreatedBY=:CreatedBY and  Status='INPROGRESS'")
		.setParameter("isTestingSuccessful", isTestingSuccessful)
		.setParameter("Status", status)
		.setParameter("rpaRequestID", rpaRequestId)
		.setParameter("CreatedBY", signum)
		.executeUpdate();
	}
	@Override
	public Integer getCurrentLanguageVersion(int rpaRequestId) {
		String sqlQuery="select LanguageVersion from transactionalData.TBL_RPA_BOTSTAGING where rpaRequestId="+rpaRequestId;
        Query query = sessionFactory.getCurrentSession().createSQLQuery(sqlQuery);  
        List listRes = query.list();
        return  (Integer) listRes.get(0);	
	}
//	@Override
//	public void updateVersionMarcroDeployedBot(TblRpaDeployedBot tblRpaBotstaging) {
//		sessionFactory.getCurrentSession()
//		.createQuery("Update TblRpaDeployedBot set LanguageVersion = :LanguageVersion "
//				+ "where BOTID = :BOTID")
//		.setParameter("LanguageVersion", tblRpaBotstaging.getLanguageVersion())
//		.setParameter("BOTID", tblRpaBotstaging.getBotid())
//		.executeUpdate();
//    }
//	@Override
//	public void updateVersionMarcroStagingBot(TblRpaBotstaging tblRpaBotstaging) {
//		sessionFactory.getCurrentSession()
//		.createQuery("Update TblRpaBotstaging set LanguageVersion = :LanguageVersion "
//				+ "where BOTID = :BOTID")
//		.setParameter("LanguageVersion", tblRpaBotstaging.getLanguageVersion())
//		.setParameter("BOTID", tblRpaBotstaging.getBotid())
//		.executeUpdate();
//    }
	

	@Override
	public UserLibraryVersionModel getLibraryVersionBySignum(String signum) {
		List<UserLibraryVersionModel> data = sessionFactory.getCurrentSession()
        	.createQuery("from UserLibraryVersionModel v where SignumID = :signum order by v.updatedOn desc")
        	.setParameter("signum", signum).setMaxResults(1).list();
		
		if(data.size() != 0)
			return data.get(0);
		else
			return null;
	}
	
	@Override
	public void saveLibraryVersionBySignum(UserLibraryVersionModel userLibraryVersion) {
		sessionFactory.getCurrentSession().save(userLibraryVersion);
	}
	
	@Override
	public List<DesktopLibraryResponseModel> getLibraryVersionGap(float version) {
		List<DesktopLibraryResponseModel> model = sessionFactory.getCurrentSession()
				.createNativeQuery("select version, row_number() over(order by UpdatedOn ) sequence ,  updatedOn , updateType from  refData.TBL_ISFDesktopUpdates where try_convert (float , version ) > :version and  updateType = 'LibraryJar' order by UpdatedOn asc", DesktopLibraryResponseModel.class)
				.setParameter("version", version).list(); 
		
		return model;
	}
	
	//getting all the requests staging data with latest stage entry:
		@SuppressWarnings("unchecked")
	    public List<Object[]> getRPAStagingDataNewForRPAAdmin() {
	    	return sessionFactory.getCurrentSession().createSQLQuery("SELECT req.rpaRequestID, "
	    			+ "stg1.SRNO,stg1.BOTID,stg1.AssignedTo,stg1.BOTName,stg1.RPAExecutionTime,stg1.BOTLanguage, "
	    			+ "stg1.TargetExecutionFileName,stg1.ModuleClassName,stg1.ModuleClassMethod,stg1.ParallelWOExecution,stg1.ReuseFactor, "
	    			+ "stg1.LineOfCode,stg1.Status,stg1.StatusDescription,stg1.CreatedBY,stg1.CreatedOn,stg1.ModifiedBy,stg1.ModifiedOn "
	    			+ "FROM transactionalData.TBL_RPA_REQUEST as req "
	    			+ "OUTER APPLY (SELECT top(1) * "
	    			+ "FROM transactionalData.TBL_RPA_BOTSTAGING as stg WHERE req.rpaRequestID=stg.rpaRequestID "
	    			+ "and stg.isActive=1 order by stg.SRNO desc) as stg1 "
	    			+ "WHERE req.isActive=1 and isnull(stg1.BOTLanguage,'')!='MACRO'").list();
	    }
	    
	    @SuppressWarnings("unchecked")
	    public List<Object[]> getStageBotsStatusForRPAAdmin() {
	    	return sessionFactory.getCurrentSession().createSQLQuery("SELECT rpaRequestID, UPPER(Status) from transactionalData.TBL_RPA_BOTSTAGING where isnull(BOTLanguage,'')!='MACRO'").list();
	    }
	    
	    @SuppressWarnings("unchecked")
	    public List<Object[]> getRPARequestStepDetailsNewForRPAAdmin() {
	    	return sessionFactory.getCurrentSession()
	    			.createSQLQuery("select distinct req.rpaRequestID ,tsfd.WorkFlowName, subact.Activity, subact.SubActivity ,step.Task,step.StepID,step.StepName,tsfd.WFID \r\n " + 
	    					" from [transactionalData].[TBL_RPA_REQUEST] req \r\n" + 
	    					" join [refData].[TBL_SUBACTIVITY] subact on  subact.SubactivityID = req.SubActivityID \r\n" + 
	    					" join [transactionalData].[TBL_SUBACTIVITY_FLOWCHART_DEF] tsfd \r\n" + 
	    					" on tsfd.ProjectID=req.ProjectID and tsfd.SubActivityID=req.SubactivityID and tsfd.SubActivityFlowChartDefID=req.WorkflowDEFID \r\n" + 
	    					" join [transactionalData].[TBL_FLOWCHART_STEP_DETAILS] step on step.StepID=req.WFSTEPID \r\n" + 
	    					" and step.TaskID=req.TaskID and step.SubActivityFlowChartDefID=req.WorkflowDEFID \r\n"
	    	                + "OUTER APPLY (SELECT top(1) * "
			                + "FROM transactionalData.TBL_RPA_BOTSTAGING as stg WHERE req.rpaRequestID=stg.rpaRequestID "
			                + "and stg.isActive=1 order by stg.SRNO desc) as stg1 "
			                + "WHERE req.isActive=1 and isnull(stg1.BOTLanguage,'')!='MACRO'").list();
	    }

	@SuppressWarnings("unchecked")
	@Override
	public boolean checkIfMacroBot(int botId) {
		
		Session current=sessionFactory.openSession();
		List<TblRpaBotstaging> list =(List<TblRpaBotstaging>) current
	        	.createQuery("from TblRpaBotstaging where rpaRequestID = :rpaRequestID AND BOTLanguage = :BOTLanguage")
	        	.setParameter("rpaRequestID", botId).setParameter("BOTLanguage", "MACRO").getResultList();
		current.close();
		
		if(null!= list && list.size()>0)
			return true;
		else
			return false;
	}
	
	@SuppressWarnings("unchecked")
    public List<TblRpaRequest> getRPARequestsNEW() {
        return (List<TblRpaRequest>) sessionFactory.getCurrentSession()
        		.createNativeQuery("Select rr.[SystemID]\r\n" + 
        				"      ,[requestName]\r\n" + 
        				"      ,[ProjectID]\r\n" + 
        				"      ,[WorkflowDEFID]\r\n" + 
        				"      ,[WFSTEPID]\r\n" + 
        				"      ,[SubactivityID]\r\n" + 
        				"      ,[TaskID]\r\n" + 
        				"      ,[SPOCSignum]\r\n" + 
        				"      ,[Description]\r\n" + 
        				"      ,[CurrentExecutioncountWeekly]\r\n" + 
        				"      ,[CurrentAvgExecutiontime]\r\n" + 
        				"      ,[RequestStatus]\r\n" + 
        				"      ,rr.[CreatedBY]\r\n" + 
        				"      ,rr.[CreatedOn]\r\n" + 
        				"      ,rr.[ModifiedBy]\r\n" + 
        				"      ,rr.[ModifiedOn]\r\n" + 
        				"      ,rr.[isActive]\r\n" + 
        				"      ,[WorkFlowName]\r\n" + 
        				"      ,[SubactivityName]\r\n" + 
        				"      ,[TaskName]\r\n" + 
        				"      ,[referenceBotId]\r\n" + 
        				"      ,[VideoURL]\r\n" + 
        				"      ,rr.[InstanceID]\r\n" + 
        				"      ,rr.[rpaRequestID]\r\n" + 
        				"      ,[isInputRequired]\r\n" + 
        				"	  from [transactionalData].[TBL_RPA_REQUEST] rr outer apply (SELECT top(1) * \r\n" + 
        				"    			FROM transactionalData.TBL_RPA_BOTSTAGING as stg WHERE rr.rpaRequestID=stg.rpaRequestID \r\n" + 
        				"    			and stg.isActive=1 order by stg.SRNO desc) as stg1 \r\n" + 
        				"  		where  rr.isActive = 1 and isnull(stg1.BOTLanguage,'')!='MACRO' ",TblRpaRequest.class).getResultList();
	}
	
	@SuppressWarnings("unchecked")
    public DesktopAppResponseModel getDesktopAppVersion() {
		return (DesktopAppResponseModel) sessionFactory.getCurrentSession()
    	.createQuery("from DesktopAppResponseModel d where d.updateType = 'App' and d.active = 1").getSingleResult();
	}
	
	@Override
	public List<LanguageBaseVersionModel> getLanguageBaseVersion() {
		List<LanguageBaseVersionModel> model = sessionFactory.getCurrentSession()
		.createNativeQuery("select distinct * from refdata.TBL_LanguageBaseVersion where isActive = 1", LanguageBaseVersionModel.class).getResultList(); 
		
		return model;
	}
	
	@SuppressWarnings("unchecked")
    public LanguageBaseVersionModel getLanguageBaseVersionByVersion(String baseVersion) {
		return (LanguageBaseVersionModel) sessionFactory.getCurrentSession()
    	.createQuery("from LanguageBaseVersionModel d where d.languageBaseVersion = :BaseVersion and d.isActive = 1").setParameter("BaseVersion", baseVersion).uniqueResult();
	}
	
	public String getLanguageBaseVersionByRpaID(int rpaRequestId) {
		return (String)sessionFactory.getCurrentSession()
    	.createNativeQuery("select top 1 ver.LanguageBaseVersion from transactionalData.TBL_RPA_BOTSTAGING bot \r\n" + 
    			"inner join refData.TBL_LanguageBaseVersion ver on ver.LanguageBaseVersionID=bot.LanguageBaseVersionID\r\n" + 
    			"where bot.rpaRequestID=:RpaRequestId and bot.LanguageBaseVersionID is not null and bot.isActive=1").setParameter("RpaRequestId", rpaRequestId).uniqueResult();
	}
	
	@Override
	public LanguageBaseVersionModel getLanguageBaseByVersionID(int languageBaseVersionID) {
		return (LanguageBaseVersionModel)  sessionFactory.getCurrentSession()
				.createQuery("from LanguageBaseVersionModel d where d.languageBaseVersionID = :LanguageBaseVersionID and d.isActive = 1").setParameter("LanguageBaseVersionID", languageBaseVersionID).uniqueResult(); 
	}

}
