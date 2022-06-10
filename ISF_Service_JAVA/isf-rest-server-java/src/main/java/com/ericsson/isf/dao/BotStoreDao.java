package com.ericsson.isf.dao;

import java.util.List;

import org.hibernate.HibernateException;

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
import com.ericsson.isf.model.botstore.TblRpaRequest;
import com.ericsson.isf.model.botstore.TblRpaRequestTool;

public interface BotStoreDao {

	public TblRpaRequest getRPARequestDetails(int rpaRequestID);
	public List<Object[]> getRPARequestOtherDetails(int rpaRequestID);
	public List<Object[]> getRPARequestStepDetails(int rpaRequestID, int WorkflowDEFID, int SubactivityID, int TaskID, int WFSTEPID);
	public List<Object[]> getBotExeDetails();
	
	public List<TblRpaRequest> getRPARequestById(int rpaRequestID);

	public List<TblProjects> getProjectById(int projectId);
	public List<TblProjects> getProjectByName(String projName);
	public List<TblProjects> getProjectDetails(String rpaRequestID);

	public List<TblRpaDeployedBot> getBOTs();
	public List<TblRpaDeployedBot> getBOTs(int taskId);
	public TblRpaDeployedBot getBotDetailById(int botId);

	public void createRpaRequest(TblRpaRequest tblRpaRequest) throws HibernateException;
	public void createRpaBotrequirement(TblRpaBotrequirement tblRpaBotrequirement) throws HibernateException;
	public void createRpaRequestTool(TblRpaRequestTool tblRpaRequestTool) throws HibernateException;

	public void createBotStaging(TblRpaBotstaging tblRpaBotstaging) throws HibernateException;
	public void updateBotStaging(TblRpaBotstaging tblRpaBotstaging);
	
	public List<TblRpaRequest> getRPARequests();
	public List<TblRpaRequest> getRPARequests(String signum);

	public List<TblRpaRequest> getRPARequestsNEW();
	
	public List<TblRpaBotstaging> getAssignedRequestListForDev(String signum);

	public TblRpaBotstaging getRPAStagingData(int rpaRequestID);
	public List<Object[]> getRPAStagingDataNew();
	public List<Object[]> getRPAStagingDataNewForRPAAdmin();
	public List<TblRpaBotstaging> getStagingBotsByReqId(int rpaRequestID);

	public List<TblRpaBotstaging> getRPAStagingForAccepted(int rpaRequestID, String userSignum, String status);
	public List<TblRpaBotstaging> getRPAStagingForAccepted(int rpaRequestID, String status);
	public List<TblRpaBotstaging> getRPAStagingForReject(int rpaRequestID);

	public List<TblRpaBotstaging> getRPAStagingByReqAndStatus(int rpaRequestID, String status);

	public void createRpaDeployBot(TblRpaDeployedBot tblRpaDeployedBot);

	public void createBotTestingRequest(TblRpaBottesting botTest);

	public TblRpaBottesting getTestingDataById(int testId);

	public TblRpaBottesting getTestingDataByReq(Integer reqId, String userSignum);
	
	public List<TblRpaRequest> getRequestsByTaskIdAndWFDefId(int taskId, int workflowDefid);

	public List<Object[]> getStageBotsStatus();
	public List<Object[]> getStageBotsStatusForRPAAdmin();

	public List<Object[]> getRPARequestStepDetailsNew();
	public List<Object[]> getRPARequestStepDetailsNewForRPAAdmin();

	public void deleteBotStage(TblRpaBotstaging tblRpaBotstaging) throws HibernateException;

	public List<TblRpaBottesting> getTestingDataByStatusSignum(String status, String userSignum);
	
	public List<DesktopAppResponseModel> getDesktopAppVersionv1();
	
	public byte[] botDownloadUsingProc(String botPath);
	
	public List<Object[]> getBotStepDetails(int botId);

	public List<Object[]> getWfOwner(int workflowDefId);

	public List<Object[]> getSpmSignum(int projectId);
	
	public void createUpdateRpaFile(TblRpaBotFile tblRpaBotFile) throws HibernateException;
	public void createUpdateRpaFileNew(TblRpaBotFileNew tblRpaBotFile) throws HibernateException;
	
	public List<TblRpaBotFile> getRPAFile(String botId, String fileName);
	public List<TblRpaBotFileNew> getRPAFileNew(String botId, String fileName);
	public List<TblRpaBotExecutionDetail> getOUTFile(String fileName);
	
	public void createRpaInputFile(TblRpaBotExecutionDetail tblRpaBotExecutionDetail) throws HibernateException;
	public List<TblRpaBotFile> getRPAFileDetails(int reqId);
	
	
	public UserWiseDesktopVersionModel getDesktopVersionBySignum(String signum);
	public void saveDesktopVersionBySignum(UserWiseDesktopVersionModel model);
	public TblRpaBotExecutionDetail getCascadedBOTInFile(String signum, Integer woNo, Integer taskId);
	public void updateRpaDeployedBot(TblRpaDeployedBot deployedBot);
	public void updateRpaRequestInputFileStatus(TblRpaDeployedBot deployedBot);
	public List<Object[]> getBotStepDetailsForDeployedBOT(int rpaRequestId);
	public int stopInprogressBot(Integer rpaRequestId, String  signum, int isTestingSuccessful, String status);
	public Integer getCurrentLanguageVersion(int rpaRequestId);
//	public void updateVersionMarcroDeployedBot(TblRpaDeployedBot deployBotToInsert);
//	public void updateVersionMarcroStagingBot(TblRpaBotstaging tblRpaBotstaging);
	
	public UserLibraryVersionModel getLibraryVersionBySignum(String signum);
	public void saveLibraryVersionBySignum(UserLibraryVersionModel userLibraryVersion);
	public List<DesktopLibraryResponseModel> getLibraryVersionGap(float version);
	public boolean checkIfMacroBot(int parseInt);
	public DesktopAppResponseModel getDesktopAppVersion();
	
	public List<LanguageBaseVersionModel> getLanguageBaseVersion();
	
	public LanguageBaseVersionModel getLanguageBaseVersionByVersion(String baseVersion);
	public String getLanguageBaseVersionByRpaID(int rpaRequestId);
	//public void updateBotVersionIdRpaDeployedBOT(int botid, Integer languageBaseVersionID);
	public LanguageBaseVersionModel getLanguageBaseByVersionID(int LanguageBaseVersionID);

	
}