package com.ericsson.isf.service;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.ericsson.isf.model.DesktopLibraryResponseModel;
import com.ericsson.isf.model.GetBotDetailByIdDTO;
import com.ericsson.isf.model.LanguageBaseVersionModel;
import com.ericsson.isf.model.RPARequestDetailsDTO;
import com.ericsson.isf.model.Response;
import com.ericsson.isf.model.UserLibraryVersionModel;
import com.ericsson.isf.model.Response;
import com.ericsson.isf.model.UserWiseDesktopVersionModel;
import com.ericsson.isf.model.botstore.BotDetail;
import com.ericsson.isf.model.botstore.DesktopAppResponseModel;
import com.ericsson.isf.model.botstore.RpaApiResponse;
import com.ericsson.isf.model.botstore.TblProjects;
import com.ericsson.isf.model.botstore.TblRpaBotExecutionDetail;
import com.ericsson.isf.model.botstore.TblRpaBotstaging;
import com.ericsson.isf.model.botstore.TblRpaBottesting;
import com.ericsson.isf.model.botstore.TblRpaDeployedBot;
import com.ericsson.isf.model.botstore.TblRpaRequest;
import com.ericsson.isf.model.botstore.TblRpaRequestDetails;
import com.ericsson.isf.model.GetBotStageDetailsForTestingDTO;

public interface BotStoreService {

	public RPARequestDetailsDTO getRPARequestDetails(int rpaRequestID);
	public TblRpaRequestDetails getRPARequestDetailsWithExtraData(int rpaRequestID);

	public TblRpaRequest getRPARequestWithBotStaging(int rpaRequestID);
	public List<TblRpaRequest> getRPARequestById(int rpaRequestID);

	public List<TblRpaDeployedBot> getBOTsForExplore();
	public List<TblRpaDeployedBot> getBOTsForExplore(int taskId);
	public Response<GetBotDetailByIdDTO> getBotDetailById(int botId);
	public TblRpaDeployedBot getBotDetail(int botId);
	//public void disableBotDetailById(int botId);

	public List<TblProjects> getProjectByName(String projName);

	public RpaApiResponse createNewRequest(TblRpaRequest tblRpaRequest, MultipartFile infile, MultipartFile outfile, MultipartFile logicfile, String reqStatus, String wfOwner);
	
	public RpaApiResponse createBotRequestForJavaPython(TblRpaRequest tblRpaRequest, TblRpaBotstaging tblRpaBotstaging, MultipartFile infile, MultipartFile outfile, MultipartFile logicfile, MultipartFile codefile, MultipartFile exefile, MultipartFile whlfile, String wfOwner);

	public RpaApiResponse updateNewRequest(TblRpaRequest tblRpaRequest, MultipartFile infile, MultipartFile outfile, MultipartFile logicfile, String reqStatus) throws IOException;
	public RpaApiResponse updateBotRequest(TblRpaRequest tblRpaRequest, TblRpaBotstaging tblRpaBotstaging, MultipartFile infile, MultipartFile outfile, MultipartFile logicfile, MultipartFile codefile, MultipartFile exefile, MultipartFile whlfile, String botConfigJson);

	public List<TblRpaRequest> getNewRequestList(String listFor) throws Exception;
	
	public List<TblRpaRequest> getNewRequestListForRPAAdmin(String listFor) throws Exception;
	
	public List<TblRpaBotstaging> getAssignedRequestListForDev(String listFor);
	
	public RpaApiResponse createBotStaging(TblRpaBotstaging bot);
	
	public RpaApiResponse assignRequestToDev(int reqId, String userSignum, String adminSignum);
	public RpaApiResponse updateBotForNonFeasibile(int reqId, String userSignum, String desc);

	public RpaApiResponse submitBotWithLanguageForJavaPython(TblRpaBotstaging bot, MultipartFile codefile, MultipartFile exeFile, MultipartFile infile, boolean isInputRequired , MultipartFile whlfile);

	public RpaApiResponse updateBotTestResults(int reqId, String userSignum, String status);

	public RpaApiResponse updateBotTestResultsForAuditUAT(int reqId, String userSignum, String status);

	public RpaApiResponse deployBot(int reqId, String userSignum);

	public RpaApiResponse approveBot(int reqId, String userSignum);

	public RpaApiResponse changeDeployedBotStatus(int botId, String userSignum, int status);
	
	public RpaApiResponse deleteAutomationRequest(int reqId, String userSignum);

	public RpaApiResponse createBotTestingRequest(Integer reqId, String userSignum);
	public RpaApiResponse updateBotTestingResults(Integer testId, Integer isTestingSuccessful);
	public TblRpaBottesting getTestingDataByReq(Integer reqId, String userSignum);
	
	public GetBotStageDetailsForTestingDTO getBotStageDetailsForTesting(Integer reqId, String status);

	public void downloadFileAsStream(HttpServletResponse response, Integer reqId, String fileType);
	public String downloadFile(Integer reqId, String fileType);
	public void downloadBotAsStream(HttpServletResponse response, Integer reqId);
	
	
	public List<Map<String, Object>> getBotCurrentStatusForApproval(List<Map<String, Object>> data);


	public RpaApiResponse changeRunOnServerStatus(int botId, String userSignum, int status);

	public RpaApiResponse changeAuditPassFail(int botId, String userSignum, int status);

	public Map<String, String> getTestingDataByStatusSignum(String status, String userSignum);

	public Response<List<DesktopAppResponseModel>> getDesktopAppVersionv1();


	public byte[] botDownload(BotDetail botDetail);

	public boolean uploadBotOutput(BotDetail botDetail);

	public BotDetail botOutputDownload(BotDetail botDetail);
	
	public byte[] downloadBotOutputByFileName(String outputFileName);

	public void debugApi(int botId);
	public TblRpaBotstaging getRPAStagingData(int rpaRequestID);
	public RpaApiResponse submitBotWithLanguageForOthers(TblRpaBotstaging tblRpaBotstaging, int referenceid);
	
	public BotDetail botDownloadDB(BotDetail botDetail);
	public byte[] botDownloadDBFile(BotDetail botDetail);
	public byte[] botDownloadDBOUTFile(String OutFileName);
	
	public String botMigrationDB(BotDetail botDetail);
	public String botMigrationDBNew(BotDetail botDetail);
	
	public String uploadBotInputOutput(BotDetail botDetail);
	public TblRpaBottesting getTestingDataById(int testId);
	
	public UserWiseDesktopVersionModel getDesktopVersionBySignum(String signum);
	public void saveDesktopVersionBySignum(UserWiseDesktopVersionModel model);
	public TblRpaBotExecutionDetail getCascadedBOTInFile(String signum, Integer woNo, Integer taskId);
	public String uploadUpdatedBotOutput(BotDetail botDetail, MultipartFile outFile);
	public RpaApiResponse updateInputFileStatus(int botId, boolean isInputRequired,String workFlowOwner) throws IOException;
	public ResponseEntity<RpaApiResponse> stopInprogressBot(Integer rpaRequestId, String  signum);
	public Object getDataFromRedis(String key);
	public void deleteDataFromRedis(String key);
	
	public Response<UserLibraryVersionModel> getLibraryVersionBySignum(String signum);
	public Response<Boolean> saveLibraryVersionBySignum(UserLibraryVersionModel userLibraryVersionModel);
	
	public Response<List<DesktopLibraryResponseModel>> getLibraryVersionGap(float version);
	public DesktopAppResponseModel getDesktopAppVersion();
	
	public Response<List<LanguageBaseVersionModel>> getLanguageBaseVersion();
	public Response<URI> getPythonInstallerUrl(String pythonBaseVersion);

  
}
