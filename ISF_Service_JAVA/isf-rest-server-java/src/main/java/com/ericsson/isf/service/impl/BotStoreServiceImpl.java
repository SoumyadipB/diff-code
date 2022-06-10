package com.ericsson.isf.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.hibernate.HibernateException;
import org.hibernate.PropertyValueException;
import org.hibernate.SessionFactory;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ericsson.isf.config.ApplicationConfigurations;
import com.ericsson.isf.dao.ActivityMasterDAO;
import com.ericsson.isf.dao.BotStoreDao;
import com.ericsson.isf.dao.WOExecutionDAO;
import com.ericsson.isf.exception.ApplicationException;
import com.ericsson.isf.model.AspLoginModel;
import com.ericsson.isf.model.BotConfig;
import com.ericsson.isf.model.CurrentWorkOrderModel;
import com.ericsson.isf.model.DesktopInformationModel;
import com.ericsson.isf.model.DesktopLibraryResponseModel;
import com.ericsson.isf.model.EmployeeModel;
import com.ericsson.isf.model.GetBotDetailByIdDTO;
import com.ericsson.isf.model.GetBotStageDetailsForTestingDTO;
import com.ericsson.isf.model.LanguageBaseVersionModel;
import com.ericsson.isf.model.RPARequestDetailsDTO;
import com.ericsson.isf.model.Response;
import com.ericsson.isf.model.UserLibraryVersionModel;
import com.ericsson.isf.model.UserWiseDesktopVersionModel;
import com.ericsson.isf.model.botstore.BotDetail;
import com.ericsson.isf.model.botstore.DesktopAppResponseModel;
import com.ericsson.isf.model.botstore.RpaApiResponse;
import com.ericsson.isf.model.botstore.TblMarketareas;
import com.ericsson.isf.model.botstore.TblOpportunity;
import com.ericsson.isf.model.botstore.TblProjects;
import com.ericsson.isf.model.botstore.TblRpaBotExecutionDetail;
import com.ericsson.isf.model.botstore.TblRpaBotFile;
import com.ericsson.isf.model.botstore.TblRpaBotrequirement;
import com.ericsson.isf.model.botstore.TblRpaBotstaging;
import com.ericsson.isf.model.botstore.TblRpaBottesting;
import com.ericsson.isf.model.botstore.TblRpaDeployedBot;
import com.ericsson.isf.model.botstore.TblRpaDeployedBotView;
import com.ericsson.isf.model.botstore.TblRpaRequest;
import com.ericsson.isf.model.botstore.TblRpaRequestDetails;
import com.ericsson.isf.model.botstore.TblRpaRequestTool;
import com.ericsson.isf.service.BotService;
import com.ericsson.isf.service.BotStoreService;
import com.ericsson.isf.service.ExternalInterfaceManagmentService;
import com.ericsson.isf.service.OutlookAndEmailService;
import com.ericsson.isf.service.RedisService;
import com.ericsson.isf.service.RpaService;
import com.ericsson.isf.service.ValidationUtilityService;
import com.ericsson.isf.util.AppConstants;
import com.ericsson.isf.util.BotStoreUtil;
import com.ericsson.isf.util.ConfigurationFilesConstants;
import com.ericsson.isf.util.IsfCustomIdInsert;
import com.fasterxml.jackson.annotation.JsonView;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlob;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlobDirectory;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.azure.storage.blob.ListBlobItem;

/**
 *
 * @author esaabeh
 */

@Service
@Transactional("txManager")
public class BotStoreServiceImpl implements BotStoreService {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(BotStoreServiceImpl.class);
    private static final String SUCCESS_MSG="Stop request of %s has been Successful.";
    private static final String NO_DATA_MSG="No %s found for this requestId: ";
    private static final String ERROR_MSG="Error in Stopping %s.";
	private static final String SUCCESS_MSG_RESPONSE = "Bot Testing Stopped Successfully";
	private static final String DEPLOYED="DEPLOYED";
	private static final String APPROVED="APPROVED";
	private static final String REJECTED="REJECTED";
	private static final String TESTED="TESTED";
	private static final String ACCEPTED="ACCEPTED";
	private static final String ASSIGNED="ASSIGNED";
	private static final String INPUT="/input/";
	private static final String BOTID_MESSAGE ="Unable to get data for given Bot ID";

    @Autowired
    private OutlookAndEmailService emailService;

    @Autowired
    private ActivityMasterDAO activityMasterDAO;

    @Autowired
    private BotStoreDao botStoreDAO;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	RpaService rpaService;

	@Autowired
    private ApplicationConfigurations configurations;

	@Autowired
	private BotService botService;

	@Autowired
	private BotStoreService botStoreService;

	@Autowired
    private IsfCustomIdInsert isfCustomIdInsert;
	
	@Autowired
	private RedisService redisService;
	
	@Autowired
	private ValidationUtilityService validationUtilityService;
	
	@Autowired
	private WOExecutionDAO wOExecutionDAO;
	
	@Autowired
	private CloudBlobContainer cloudBlobContainer;
	
	@Autowired
	private ExternalInterfaceManagmentService externalInterfaceManagmentService;

    public RPARequestDetailsDTO getRPARequestDetails(int rpaRequestID) {

		TblRpaRequest rpaRequestModel = botStoreDAO.getRPARequestDetails(rpaRequestID);

		RPARequestDetailsDTO rpaRequestDetailsDTO=new RPARequestDetailsDTO();
		TblRpaRequest requestForUi = new TblRpaRequest();

    	Set<TblRpaBotrequirement> tblRpaBotrequirementsForUi = new HashSet<TblRpaBotrequirement>(0);
		TblRpaBotrequirement botRequirementForUi = null;

    	Set<TblRpaRequestTool> tblToolsForUi = new HashSet<TblRpaRequestTool>(0);
		TblRpaRequestTool reqToolForUi = null;

    	try {
			Map<Integer, String[]> dataMap = populateExtraDataForBots();

        	if (null == rpaRequestModel) {
        		return rpaRequestDetailsDTO;
			} else {
				requestForUi = new TblRpaRequest();
            	requestForUi.setRpaRequestId(rpaRequestModel.getRpaRequestId());
            	requestForUi.setWorkflowDefid(rpaRequestModel.getWorkflowDefid());
            	requestForUi.setWfstepid(rpaRequestModel.getWfstepid());
            	requestForUi.setSubactivityId(rpaRequestModel.getSubactivityId());
            	requestForUi.setTaskId(rpaRequestModel.getTaskId());
            	requestForUi.setSpocsignum(rpaRequestModel.getSpocsignum());
            	requestForUi.setDescription(rpaRequestModel.getDescription());
            	requestForUi.setCurrentExecutioncountWeekly(rpaRequestModel.getCurrentExecutioncountWeekly());
            	requestForUi.setCurrentAvgExecutiontime(rpaRequestModel.getCurrentAvgExecutiontime());
            	requestForUi.setCreatedBy(rpaRequestModel.getCreatedBy());
				requestForUi.setIsInputRequired(rpaRequestModel.getIsInputRequired());

				for (TblRpaBotrequirement reqt : rpaRequestModel.getTblRpaBotrequirements()) {

					botRequirementForUi = new TblRpaBotrequirement();
            		botRequirementForUi.setIsDataFetching(reqt.getIsDataFetching());
            		botRequirementForUi.setIsReportPrepration(reqt.getIsReportPrepration());
            		botRequirementForUi.setOutputFormat(reqt.getOutputFormat());

					for (TblRpaRequestTool tool : reqt.getTblRpaRequestTools()) {

						reqToolForUi = new TblRpaRequestTool();
                		reqToolForUi.setToolId(tool.getToolId());
                		reqToolForUi.setToolName(tool.getToolName());
                		reqToolForUi.setIsMobilePassRequired(tool.getIsMobilePassRequired());
                		reqToolForUi.setIsPasswordRequired(tool.getIsPasswordRequired());
                		reqToolForUi.setIsVpnrequired(tool.getIsVpnrequired());

                		tblToolsForUi.add(reqToolForUi);
            		}
            		botRequirementForUi.setTblRpaRequestTools(tblToolsForUi);
            		tblRpaBotrequirementsForUi.add(botRequirementForUi);
            	}

				// getting Step data:
				if (null != dataMap.get(rpaRequestID)) {
                	requestForUi.setWorkFlowName(dataMap.get(rpaRequestID)[0]);
					requestForUi.setSubactivityName(dataMap.get(rpaRequestID)[1] + "/" + dataMap.get(rpaRequestID)[2]);
                	requestForUi.setTaskName(dataMap.get(rpaRequestID)[3]);
                	requestForUi.setStepID(dataMap.get(rpaRequestID)[4]);
                	requestForUi.setStepName(dataMap.get(rpaRequestID)[5]);
                	requestForUi.setwFID(Integer.parseInt(dataMap.get(rpaRequestID)[6]));
                }

				requestForUi.setTblRpaBotrequirements(tblRpaBotrequirementsForUi);
				rpaRequestDetailsDTO.setTblRpaRequest(requestForUi);
				rpaRequestDetailsDTO.setLanguageBaseVersion(botStoreDAO.getLanguageBaseVersionByRpaID(rpaRequestID));
            }
		} catch (Exception e) {
			LOG.info("Error while getting RPARequest Details:" + e.getMessage());
			e.printStackTrace();
			throw new ApplicationException(500, "Getting Error to fetch data.");

		}

        return rpaRequestDetailsDTO;
    }

    public TblRpaRequestDetails getRPARequestDetailsWithExtraData(int rpaRequestID) {

		TblRpaRequestDetails details = new TblRpaRequestDetails();

    	details.setRpaRequestId(rpaRequestID);
    	details.setTblRpaRequest(this.getRPARequestDetails(rpaRequestID).getTblRpaRequest());

		List<Object[]> l = botStoreDAO.getRPARequestOtherDetails(rpaRequestID);
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		for (Object[] r : l) {
			map = new HashMap<String, Object>();
			map.put("rpaRequestID", (Integer) r[0]);
			map.put("ProjectID", (Integer) r[1]);

    		data.add(map);

    		details.setData(data);
    	}

        return details;
    }

	public TblRpaBotstaging getRPAStagingData(int rpaRequestID) {
    	return botStoreDAO.getRPAStagingData(rpaRequestID);
    }

    public TblRpaRequest getRPARequestWithBotStaging(int rpaRequestID) {

		TblRpaRequest tblRpaRequest = this.getRPARequestDetails(rpaRequestID).getTblRpaRequest();

    	if (null == tblRpaRequest) {
    		return tblRpaRequest;
			// throw new ApplicationException(500, "No details exists for the Request!!!");
		} else {
			TblRpaBotstaging tblRpaBotstaging = botStoreDAO.getRPAStagingData(rpaRequestID);

        	Set<TblRpaBotstaging> tblRpaBotstagingSetForUi = new HashSet<TblRpaBotstaging>(0);
			TblRpaBotstaging tblRpaBotstagingForUi = new TblRpaBotstaging();

        	tblRpaBotstagingForUi.setSrno(tblRpaBotstaging.getSrno());
        	tblRpaBotstagingForUi.setBotid(tblRpaBotstaging.getBotid());
        	tblRpaBotstagingForUi.setAssignedTo(tblRpaBotstaging.getAssignedTo());
        	tblRpaBotstagingForUi.setBotname(tblRpaBotstaging.getBotname());
        	tblRpaBotstagingForUi.setRpaexecutionTime(tblRpaBotstaging.getRpaexecutionTime());
        	tblRpaBotstagingForUi.setBotlanguage(tblRpaBotstaging.getBotlanguage());
        	tblRpaBotstagingForUi.setTargetExecutionFileName(tblRpaBotstaging.getTargetExecutionFileName());
        	tblRpaBotstagingForUi.setModuleClassName(tblRpaBotstaging.getModuleClassName());
        	tblRpaBotstagingForUi.setModuleClassMethod(tblRpaBotstaging.getModuleClassMethod());
        	tblRpaBotstagingForUi.setParallelWoexecution(tblRpaBotstaging.getParallelWoexecution());
        	tblRpaBotstagingForUi.setReuseFactor(tblRpaBotstaging.getReuseFactor());
        	tblRpaBotstagingForUi.setLineOfCode(tblRpaBotstaging.getLineOfCode());
        	tblRpaBotstagingForUi.setStatus(tblRpaBotstaging.getStatus());
        	tblRpaBotstagingForUi.setStatusDescription(tblRpaBotstaging.getStatusDescription());
        	tblRpaBotstagingForUi.setCreatedOn(tblRpaBotstaging.getCreatedOn());
        	tblRpaBotstagingSetForUi.add(tblRpaBotstagingForUi);

    		tblRpaRequest.setTblRpaBotstagings(tblRpaBotstagingSetForUi);
        }
        return tblRpaRequest;
    }

    public List<TblRpaRequest> getRPARequestById(int rpaRequestID) {

		List<TblRpaRequest> rpaRequestModel = botStoreDAO.getRPARequestById(rpaRequestID);
		if (rpaRequestModel == null || (null != rpaRequestModel && rpaRequestModel.isEmpty())) {
			return rpaRequestModel = new ArrayList<TblRpaRequest>();
			// throw new ApplicationException(500, "No details exists for the Request!!!");
        } else {

			Integer projectId = null;

			List<TblRpaDeployedBot> botsForUi = new ArrayList<TblRpaDeployedBot>();
			TblRpaDeployedBot botForUi = null;
			TblRpaRequest reqForUi = null;
			TblProjects projForUi = null;
			TblOpportunity oppForUi = null;
			TblMarketareas mktForUi = null;
			for (TblRpaRequest t : rpaRequestModel) {
				// List<TblProjects> pl= botStoreDAO.getProjectById(projectId);
        	}

            return rpaRequestModel;
        }

    }

    public List<TblRpaDeployedBot> getBOTsForExplore() {

		List<TblRpaDeployedBot> botsForUi = new ArrayList<TblRpaDeployedBot>();

    	try {
			List<TblRpaDeployedBot> rpaDeployedBots = botStoreDAO.getBOTs();
            if (rpaDeployedBots.isEmpty()) {
            	rpaDeployedBots = new ArrayList<TblRpaDeployedBot>();
            } else {
				TblRpaRequest req = null;

				Map<Integer, String[]> dataMap = populateExtraDataForBots();

				List<TblRpaRequest> rpaRequestModel = null;
				TblRpaDeployedBot botForUi = null;
				TblRpaRequest reqForUi = null;
				TblProjects projForUi = null;
				TblOpportunity oppForUi = null;
				TblMarketareas mktForUi = null;
				for (TblRpaDeployedBot bot : rpaDeployedBots) {
					if (bot.getTblRpaRequest().getRpaRequestId() > 1) {
						rpaRequestModel = botStoreDAO.getRPARequestById(bot.getTblRpaRequest().getRpaRequestId());
						req = rpaRequestModel.get(0);
						botForUi = new TblRpaDeployedBot();
                    	botForUi.setBotid(bot.getBotid());
                    	botForUi.setBotname(bot.getBotname());
                    	botForUi.setCreatedOn(bot.getCreatedOn());
                    	botForUi.setIsRunOnServer(bot.getIsRunOnServer());
                    	botForUi.setIsActive(bot.getIsActive());
                    	botForUi.setIsAuditPass(bot.getIsAuditPass());
						botForUi.setIsInputRequired(bot.getIsInputRequired());

						reqForUi = new TblRpaRequest();
						projForUi = new TblProjects();
                    	projForUi.setProjectId(req.getTblProjects().getProjectId());
                    	projForUi.setProjectName(req.getTblProjects().getProjectName());
						oppForUi = new TblOpportunity();
						mktForUi = new TblMarketareas();
						mktForUi.setMarketAreaName(
								req.getTblProjects().getTblOpportunity().getTblMarketareas().getMarketAreaName());
                    	oppForUi.setTblMarketareas(mktForUi);
                    	projForUi.setTblOpportunity(oppForUi);
                    	reqForUi.setSubactivityId(req.getSubactivityId());
                    	reqForUi.setTblProjects(projForUi);
                    	reqForUi.setRpaRequestId(bot.getTblRpaRequest().getRpaRequestId());
						reqForUi.setIsInputRequired(req.getIsInputRequired());

						// getting Step data:
						if (null != dataMap.get(req.getRpaRequestId())) {
                    		reqForUi.setWorkFlowName(dataMap.get(req.getRpaRequestId())[0]);
							reqForUi.setSubactivityName(dataMap.get(req.getRpaRequestId())[1] + "/"
									+ dataMap.get(req.getRpaRequestId())[2]);
                    		reqForUi.setTaskName(dataMap.get(req.getRpaRequestId())[3]);
                        }
						botForUi.setTblRpaRequest(reqForUi);

                    	botsForUi.add(botForUi);
            		}
            	}
            }

            setBotExeData(botsForUi);

		} catch (Exception e) {
			e.printStackTrace();
            LOG.error("Error in method getBOTsForExplore()");
		}

    	return botsForUi;
    }

	private Map<Integer, String[]> populateExtraDataForBots() throws Exception {
		// getting request data for task name, WorkFlowName, subact name:
		Map<Integer, String[]> dataMap = new HashMap<Integer, String[]>();
		List<Object[]> taskList = botStoreDAO.getRPARequestStepDetailsNew();
		String[] taskDataArr = null;
		Integer rId = null;
		for (Object[] d : taskList) {
			taskDataArr = new String[7];

			rId = (Integer) d[0];
			if (null != d[1])
				taskDataArr[0] = (String) d[1];
    		else {
				taskDataArr[0] = "";
    		}	
			if (null != d[2])
				taskDataArr[1] = (String) d[2];
    		else {
				taskDataArr[1] = "";
    		}
			if (null != d[3])
				taskDataArr[2] = (String) d[3];
    		else {
				taskDataArr[2] = "";
    		}
			if (null != d[4])
				taskDataArr[3] = (String) d[4];
    		else {
				taskDataArr[3] = "";
    		}
			if (null != d[5])
				taskDataArr[4] = (String) d[5];
    		else {
				taskDataArr[4] = "";
    		}
			if (null != d[6])
				taskDataArr[5] = (String) d[6];
    		else {
				taskDataArr[5] = "";
    		}
			if (null != d[7])
				taskDataArr[6] = (String) d[7].toString();
    		else {
				taskDataArr[6] = "0";
    		}
    		dataMap.put(rId, taskDataArr);
    	}

        return dataMap;
    }

	private void setBotExeData(List<TblRpaDeployedBot> botsForUi) {
		// for getting BOT Execution details:
		List<Object[]> data = botStoreDAO.getBotExeDetails();
		Integer reqId = null;
		Integer botExeCount = null;
		Double botExeHr = null;
		Integer botExeFailCount = null;
		Map<Integer, List<Object>> map = new HashMap<Integer, List<Object>>();
		List<Object> l = null;
		Object val = null;
		for (Object[] d : data) {
			if (null != d[0]) {
				reqId = (Integer) d[0];

				if (null != d[1] && d[1].toString().trim().length() > 0) {
					val = d[1].toString().trim();
					botExeCount = Integer.parseInt(val.toString());
				} else {
					botExeCount = 0;// if value is null
				}

				if (null != d[2] && d[2].toString().trim().length() > 0) {
					val = d[2].toString().trim();
					botExeHr = Double.parseDouble(val.toString());
				} else {
					botExeHr = 0.0;// if value is null
				}

				if (null != d[3] && d[3].toString().trim().length() > 0) {
					val = d[3].toString().trim();
					botExeFailCount = Integer.parseInt(val.toString());
				} else {
					botExeFailCount = 0;// if value is null
				}

				l = new ArrayList<Object>();
        		l.add(botExeCount);
        		l.add(botExeHr);
        		l.add(botExeFailCount);
        	}
    		map.put(reqId, l);
    	}

		for (TblRpaDeployedBot d : botsForUi) {
			if (map.containsKey(d.getBotid())) {
				d.setRpaexecutionTime((Integer) map.get(d.getBotid()).get(0));
				d.setCurrentAvgExecutionTime(((Double) map.get(d.getBotid()).get(1)).intValue());
				d.setReuseFactor((Integer) map.get(d.getBotid()).get(2));
        	}
        }
    }

    public List<TblRpaDeployedBot> getBOTsForExplore(int taskId) {

		List<TblRpaDeployedBot> botsForUi = new ArrayList<TblRpaDeployedBot>();

		List<TblRpaDeployedBot> rpaDeployedBots = botStoreDAO.getBOTs(taskId);
        if (rpaDeployedBots.isEmpty()) {
        	rpaDeployedBots = new ArrayList<TblRpaDeployedBot>();
			// throw new ApplicationException(500, "No details exists for the Request!!!");
        } else {

        	try {
				Map<Integer, String[]> dataMap = populateExtraDataForBots();

				TblRpaRequest req = null;

				List<TblRpaRequest> rpaRequestModel = null;
				TblRpaDeployedBot botForUi = null;
				TblRpaRequest reqForUi = null;
				TblProjects projForUi = null;
				TblOpportunity oppForUi = null;
				TblMarketareas mktForUi = null;
				for (TblRpaDeployedBot bot : rpaDeployedBots) {
					if (bot.getTblRpaRequest().getRpaRequestId() > 1) {
						rpaRequestModel = botStoreDAO.getRPARequestById(bot.getTblRpaRequest().getRpaRequestId());
						req = rpaRequestModel.get(0);
						botForUi = new TblRpaDeployedBot();
                    	botForUi.setBotid(bot.getBotid());
                    	botForUi.setBotname(bot.getBotname());
                    	botForUi.setCreatedOn(bot.getCreatedOn());
						reqForUi = new TblRpaRequest();
						projForUi = new TblProjects();
                    	projForUi.setProjectId(req.getTblProjects().getProjectId());
                    	projForUi.setProjectName(req.getTblProjects().getProjectName());
						oppForUi = new TblOpportunity();
						mktForUi = new TblMarketareas();
						mktForUi.setMarketAreaName(
								req.getTblProjects().getTblOpportunity().getTblMarketareas().getMarketAreaName());
                    	oppForUi.setTblMarketareas(mktForUi);
                    	projForUi.setTblOpportunity(oppForUi);
                    	reqForUi.setSubactivityId(req.getSubactivityId());
                    	reqForUi.setTblProjects(projForUi);
                    	reqForUi.setRpaRequestId(bot.getTblRpaRequest().getRpaRequestId());

						// getting Step data:
						if (null != dataMap.get(req.getRpaRequestId())) {
                    		reqForUi.setWorkFlowName(dataMap.get(req.getRpaRequestId())[0]);
							reqForUi.setSubactivityName(dataMap.get(req.getRpaRequestId())[1] + "/"
									+ dataMap.get(req.getRpaRequestId())[2]);
                    		reqForUi.setTaskName(dataMap.get(req.getRpaRequestId())[3]);
                        }

                    	botForUi.setTblRpaRequest(reqForUi);

                    	botsForUi.add(botForUi);
            		}
            	}
            } catch (Exception e) {
				LOG.info("Error while getting BOTs For Explore by taskId:" + e.getMessage());
    			e.printStackTrace();
			}
        }

        setBotExeData(botsForUi);

    	return botsForUi;
    }

	// listFor: SIGNUM or ALL
	public List<TblRpaRequest> getNewRequestList(String listFor) throws Exception {

		List<TblRpaRequest> tblRpaRequests = null;
		List<TblRpaRequest> requestsForUi = new ArrayList<TblRpaRequest>();

    	Set<TblRpaBotstaging> tblRpaBotstagingSetForUi = null;

		TblRpaBotstaging tblRpaBotstaging = null;
		TblRpaBotstaging stagForUi = null;

    	try {
			if (listFor.equalsIgnoreCase("ALL")) {
				tblRpaRequests = botStoreDAO.getRPARequests();
			}
        	else {
				tblRpaRequests = botStoreDAO.getRPARequests(listFor); // passing SIGNUM here
        	}

			if (null == tblRpaRequests || tblRpaRequests.isEmpty()) {
            	return requestsForUi;
			} else {
				LOG.info("Requests List Size--> {}" , tblRpaRequests.size());
				TblRpaRequest reqForUi = null;
				TblProjects projForUi = null;

				// getting all the requests staging data with latest stage entry:
				Map<Integer, TblRpaBotstaging> stgMap = new HashMap<Integer, TblRpaBotstaging>();
				List<Object[]> stgLatestData = botStoreDAO.getRPAStagingDataNew();
				Integer reqId = null;
				for (Object[] d : stgLatestData) {
					TblRpaBotstaging stg = null;
					reqId = (Integer) d[0];
					if (null != d[1]) {
						stg = new TblRpaBotstaging();
						stg.setAssignedTo((String) d[3]);
						stg.setBotname((String) d[4]);
						stg.setRpaexecutionTime((Integer) d[5]);
						stg.setBotlanguage((String) d[6]);
						stg.setTargetExecutionFileName((String) d[7]);
						stg.setModuleClassName((String) d[8]);
						stg.setModuleClassMethod((String) d[9]);
						stg.setParallelWoexecution((String) d[10]);
						stg.setReuseFactor((Integer) d[11]);
						stg.setLineOfCode((Integer) d[12]);
						stg.setStatus((String) d[13]);
						stg.setStatusDescription((String) d[14]);
						stg.setCreatedBy((String) d[15]);
						stg.setCreatedOn((Date) d[16]);
						stg.setModifiedBy((String) d[17]);
						stg.setModifiedOn((Date) d[18]);
						stg.setModifiedOn((Date) d[18]);
            		}
            		stgMap.put(reqId, stg);
            	}

				// getting request and its staging status for current status display:
				Map<Integer, Set<String>> reqStatusMap = new HashMap<Integer, Set<String>>();
				List<Object[]> stgData = botStoreDAO.getStageBotsStatus();
				Set<String> statusSet = null;
				String status = null;
				for (Object[] d : stgData) {
					reqId = (Integer) d[0];
					if (null != d[1])
						status = (String) d[1];

					if (reqStatusMap.containsKey(reqId))
            			reqStatusMap.get(reqId).add(status);
					else {
						statusSet = new HashSet<String>();
                		statusSet.add(status);
                		reqStatusMap.put(reqId, statusSet);
            		}
            	}

				// getting request data for task name, WorkFlowName, subact name:
				Map<Integer, String[]> dataMap = new HashMap<Integer, String[]>();
				List<Object[]> taskList = botStoreDAO.getRPARequestStepDetailsNew();
				String[] taskDataArr = null;
				Integer rId = null;
				for (Object[] d : taskList) {
					taskDataArr = new String[4];

					rId = (Integer) d[0];
					if (null != d[1])
						taskDataArr[0] = (String) d[1];
            		else {
						taskDataArr[0] = "";
            		}
					if (null != d[2])
						taskDataArr[1] = (String) d[2];
            		else {
						taskDataArr[1] = "";
            		}
					if (null != d[3])
						taskDataArr[2] = (String) d[3];
            		else {
						taskDataArr[2] = "";
            		}
					if (null != d[4])
						taskDataArr[3] = (String) d[4];
            		else {
						taskDataArr[3] = "";
            		}

            		dataMap.put(rId, taskDataArr);
            	}

				for (TblRpaRequest req : tblRpaRequests) {
					reqForUi = new TblRpaRequest();
    				reqForUi.setRequestStatus(req.getRequestStatus());
            		reqForUi.setRpaRequestId(req.getRpaRequestId());
    				reqForUi.setRequestName(req.getRequestName());
					reqForUi.setIsInputRequired(req.getIsInputRequired());
            		reqForUi.setSpocsignum(req.getSpocsignum());
            		reqForUi.setCurrentExecutioncountWeekly(req.getCurrentExecutioncountWeekly());

            		reqForUi.setCurrentAvgExecutiontime(req.getCurrentAvgExecutiontime());

            		reqForUi.setWorkflowDefid(req.getWorkflowDefid());
            		reqForUi.setWfstepid(req.getWfstepid());
            		reqForUi.setSubactivityId(req.getSubactivityId());
            		reqForUi.setTaskId(req.getTaskId());
            		reqForUi.setDescription(req.getDescription());
            		reqForUi.setCreatedOn(req.getCreatedOn());

					projForUi = new TblProjects();
                	projForUi.setProjectId(req.getTblProjects().getProjectId());
//                	projForUi.setProjectName(req.getTblProjects().getProjectName());
                	reqForUi.setTblProjects(projForUi);
            		
					// getting Step data:
					if (null != dataMap.get(req.getRpaRequestId())) {
                		reqForUi.setWorkFlowName(dataMap.get(req.getRpaRequestId())[0]);
						reqForUi.setSubactivityName(
								dataMap.get(req.getRpaRequestId())[1] + "/" + dataMap.get(req.getRpaRequestId())[2]);
                		reqForUi.setTaskName(dataMap.get(req.getRpaRequestId())[3]);
                    }

					String botCurrStatus = null;
					if (null != reqStatusMap.get(req.getRpaRequestId()))
						botCurrStatus = getBotCurrStatusNew(reqStatusMap.get(req.getRpaRequestId()));

					tblRpaBotstaging = stgMap.get(req.getRpaRequestId());
					if (tblRpaBotstaging != null) {
						stagForUi = new TblRpaBotstaging();
    					stagForUi.setAssignedTo(tblRpaBotstaging.getAssignedTo());
    					stagForUi.setBotname(tblRpaBotstaging.getBotname());
    					stagForUi.setBotlanguage(tblRpaBotstaging.getBotlanguage());
    					stagForUi.setRpaexecutionTime(tblRpaBotstaging.getRpaexecutionTime());
    					stagForUi.setTargetExecutionFileName(tblRpaBotstaging.getTargetExecutionFileName());
    					stagForUi.setModuleClassName(tblRpaBotstaging.getModuleClassName());
    					stagForUi.setModuleClassMethod(tblRpaBotstaging.getModuleClassMethod());
    					stagForUi.setParallelWoexecution(tblRpaBotstaging.getParallelWoexecution());
    					stagForUi.setReuseFactor(tblRpaBotstaging.getReuseFactor());
    					stagForUi.setLineOfCode(tblRpaBotstaging.getLineOfCode());
    					stagForUi.setStatus(botCurrStatus);
    					stagForUi.setStatusDescription(tblRpaBotstaging.getStatusDescription());
    					stagForUi.setCreatedBy(tblRpaBotstaging.getCreatedBy());
    					stagForUi.setCreatedOn(tblRpaBotstaging.getCreatedOn());

						tblRpaBotstagingSetForUi = new HashSet<TblRpaBotstaging>(0);
    					tblRpaBotstagingSetForUi.add(stagForUi);
    					reqForUi.setTblRpaBotstagings(tblRpaBotstagingSetForUi);
      			 	}

                    reqForUi.setVideoURL(req.getVideoURL());

                	requestsForUi.add(reqForUi);
            	}
            }
		} catch (Exception e) {
			e.printStackTrace();
            LOG.info("Error in method getNewRequestList(String listFor)");
            throw e;
		}

    	return requestsForUi;
    }

	public String getBotCurrStatus(List<TblRpaBotstaging> stagBots) {
		String botCurrStatus = null;
		Set<String> stSet = new HashSet<String>();
		for (TblRpaBotstaging stag : stagBots)
    		stSet.add(stag.getStatus().toUpperCase());

		if (stSet.contains(DEPLOYED)) {
			botCurrStatus = DEPLOYED;}
		else if (stSet.contains(APPROVED)) {
			botCurrStatus = APPROVED;}
		else if (stSet.contains(REJECTED)) {
			botCurrStatus = REJECTED;}
		else if (stSet.contains(TESTED)) {
			botCurrStatus = TESTED;}
		else if (stSet.contains(ACCEPTED)) {
			botCurrStatus = ACCEPTED;}
		else if (stSet.contains(ASSIGNED)) {
			botCurrStatus = ASSIGNED;}
    	else {
			botCurrStatus = "NA";}

    	return botCurrStatus;
    }

	public String getBotCurrStatusNew(Set<String> stSet) {
		String botCurrStatus = null;

		if (stSet.contains(DEPLOYED)) {
			botCurrStatus = DEPLOYED;}
		else if (stSet.contains(APPROVED)) {
			botCurrStatus = APPROVED;}
		else if (stSet.contains(REJECTED)) {
			botCurrStatus = REJECTED;}
		else if (stSet.contains(TESTED)) {
			botCurrStatus = TESTED;}
		else if (stSet.contains(ACCEPTED)) {
			botCurrStatus = ACCEPTED;}
		else if (stSet.contains(ASSIGNED)) {
			botCurrStatus = ASSIGNED;}
    	else {
			botCurrStatus = "NA";}

    	return botCurrStatus;
    }

    public List<TblRpaBotstaging> getAssignedRequestListForDev(String listFor) {

		List<TblRpaBotstaging> stageBots = botStoreDAO.getAssignedRequestListForDev(listFor); // passing SIGNUM here

		List<TblRpaBotstaging> stageBotsForUi = new ArrayList<TblRpaBotstaging>();
		TblRpaRequest reqForUi = null;
		TblRpaBotstaging stagBot = null;
		TblRpaBotstaging stagForUi = null;
		TblProjects projForUi = null;

		if (null == stageBots || stageBots.isEmpty()) {
        	return stageBotsForUi;
		} else {
        	try {

				Map<Integer, String[]> dataMap = populateExtraDataForBots();

				LOG.info("Requests List Size--> {}" , stageBots.size());

				Map<Integer, List<TblRpaBotstaging>> reqToStageMap = new HashMap<Integer, List<TblRpaBotstaging>>();
				List<TblRpaBotstaging> botList = null;
				for (TblRpaBotstaging bot : stageBots) {
					if (reqToStageMap.containsKey(bot.getTblRpaRequest().getRpaRequestId()))
            			reqToStageMap.get(bot.getTblRpaRequest().getRpaRequestId()).add(bot);
					else {
						botList = new ArrayList<TblRpaBotstaging>();
            			botList.add(bot);
            			reqToStageMap.put(bot.getTblRpaRequest().getRpaRequestId(), botList);
            		}
            	}

				Map<Integer, List<TblRpaBotstaging>> reqToStageFilteredMap = new HashMap<Integer, List<TblRpaBotstaging>>();
				boolean f = false;
				List<TblRpaBotstaging> botListFiltered = null;
				for (Integer r : reqToStageMap.keySet()) {
					f = false;

					for (TblRpaBotstaging b : reqToStageMap.get(r)) {

						if (b.getStatus().equalsIgnoreCase(DEPLOYED)) {

							f = true;
            				break;
            			}
        			}
					if (f == false) {
						for (TblRpaBotstaging b : reqToStageMap.get(r)) {
							if (reqToStageFilteredMap.containsKey(r)) {

                    			reqToStageFilteredMap.get(r).add(b);
							} else {

								botListFiltered = new ArrayList<TblRpaBotstaging>();
                    			botListFiltered.add(b);
                    			reqToStageFilteredMap.put(r, botListFiltered);
                    		}
            			}
        			}
        		}

				for (Integer r : reqToStageFilteredMap.keySet()) {
        			Collections.sort(reqToStageFilteredMap.get(r), new Comparator<TblRpaBotstaging>() {
        				@Override
        				public int compare(TblRpaBotstaging o1, TblRpaBotstaging o2) {
        					return o2.getCreatedOn().compareTo(o1.getCreatedOn());
        				}
    				});
        		}

				for (Integer r : reqToStageFilteredMap.keySet()) {

					stagBot = reqToStageFilteredMap.get(r).get(0);
					stagForUi = new TblRpaBotstaging();
        			stagForUi.setSrno(stagBot.getSrno());
        			stagForUi.setBotid(stagBot.getBotid());
        			stagForUi.setAssignedTo(stagBot.getAssignedTo());
        			stagForUi.setBotname(stagBot.getBotname());
        			stagForUi.setBotlanguage(stagBot.getBotlanguage());
        			stagForUi.setRpaexecutionTime(stagBot.getRpaexecutionTime());
        			stagForUi.setTargetExecutionFileName(stagBot.getTargetExecutionFileName());
        			stagForUi.setModuleClassName(stagBot.getModuleClassName());
        			stagForUi.setModuleClassMethod(stagBot.getModuleClassMethod());
        			stagForUi.setParallelWoexecution(stagBot.getParallelWoexecution());
        			stagForUi.setReuseFactor(stagBot.getReuseFactor());
        			stagForUi.setLineOfCode(stagBot.getLineOfCode());
        			stagForUi.setStatus(stagBot.getStatus());
        			stagForUi.setStatusDescription(stagBot.getStatusDescription());
        			stagForUi.setCreatedBy(stagBot.getCreatedBy());
        			stagForUi.setCreatedOn(stagBot.getCreatedOn());

					projForUi = new TblProjects();
                	projForUi.setProjectId(stagBot.getTblRpaRequest().getTblProjects().getProjectId());
                	projForUi.setProjectName(stagBot.getTblRpaRequest().getTblProjects().getProjectName());

					reqForUi = new TblRpaRequest();
        			reqForUi.setRpaRequestId(stagBot.getTblRpaRequest().getRpaRequestId());
					reqForUi.setIsInputRequired(stagBot.getTblRpaRequest().getIsInputRequired());
        			reqForUi.setSpocsignum(stagBot.getTblRpaRequest().getSpocsignum());
					reqForUi.setCurrentExecutioncountWeekly(
							stagBot.getTblRpaRequest().getCurrentExecutioncountWeekly());
        			reqForUi.setCurrentAvgExecutiontime(stagBot.getTblRpaRequest().getCurrentAvgExecutiontime());
        			reqForUi.setWorkflowDefid(stagBot.getTblRpaRequest().getWorkflowDefid());
        			reqForUi.setSubactivityId(stagBot.getTblRpaRequest().getSubactivityId());
        			reqForUi.setWfstepid(stagBot.getTblRpaRequest().getWfstepid());
        			reqForUi.setTaskId(stagBot.getTblRpaRequest().getTaskId());

					// getting Step data:
					if (null != dataMap.get(stagBot.getTblRpaRequest().getRpaRequestId())) {
                		reqForUi.setWorkFlowName(dataMap.get(stagBot.getTblRpaRequest().getRpaRequestId())[0]);
						reqForUi.setSubactivityName(dataMap.get(stagBot.getTblRpaRequest().getRpaRequestId())[1] + "/"
								+ dataMap.get(stagBot.getTblRpaRequest().getRpaRequestId())[2]);
                		reqForUi.setTaskName(dataMap.get(stagBot.getTblRpaRequest().getRpaRequestId())[3]);
                    }

        			reqForUi.setTblProjects(projForUi);

        			stagForUi.setTblRpaRequest(reqForUi);

        			stageBotsForUi.add(stagForUi);

        		}
            } catch (Exception e) {
				LOG.info("Error while getting Assigned Request List For Dev:" + e.getMessage());
    			e.printStackTrace();
			}
        }
    	return stageBotsForUi;
    }


public Response<GetBotDetailByIdDTO> getBotDetailById(int botId)  {
    	
    	Response<GetBotDetailByIdDTO> response = new Response<>();
    	try {
    		 LOG.info("getBotDetailById:Start");
    		 validationUtilityService.validateIntForZero(botId, "BotID");
    		 
    	   TblRpaDeployedBot rpaDeployedBot = botStoreDAO.getBotDetailById(botId);
    	   
    	   if ( null == rpaDeployedBot) {
    		   response.addFormMessage(BOTID_MESSAGE);
			}
    	   else{
    		   //botStoreDAO.getBotDetailById(botId);
    		 GetBotDetailByIdDTO getBotDetailByIdDTO= new GetBotDetailByIdDTO();
    		 getBotDetailByIdDTO.setTblRpaDeployedBot(rpaDeployedBot);
    		 if(rpaDeployedBot.getLanguageBaseVersionID()!=null) {
    			 getBotDetailByIdDTO.setLanguageBaseVersion(botStoreDAO.getLanguageBaseByVersionID(rpaDeployedBot.getLanguageBaseVersionID()).getLanguageBaseVersion());
    		 }
             response.setResponseData(getBotDetailByIdDTO);
			}
         }
    	catch(ApplicationException exe) {
			response.addFormMessage(exe.getMessage());
		}
		catch (Exception e) {
			response.addFormError(e.getMessage());
		}
		return response;
    	
    }

	public TblRpaDeployedBot getBotDetail(int botId) {
	
	    return botStoreDAO.getBotDetailById(botId);
	}

    public List<TblProjects> getProjectByName(String projName) {

		List<TblProjects> projects = botStoreDAO.getProjectByName(projName);
        if (projects.isEmpty()) {
        	return new ArrayList<TblProjects>();
			// throw new ApplicationException(500, "No details exists for the Request!!!");
        } else {

			for (TblProjects project : projects) {

        	}
        	return projects;
        }
    }

    @Transactional("txManager")
	public RpaApiResponse createNewRequest(TblRpaRequest tblRpaRequest, MultipartFile infile, MultipartFile outfile,
			MultipartFile logicfile, String reqStatus, String wfOwner) {
		RpaApiResponse res = new RpaApiResponse();

		try {
			List<TblRpaRequest> requests = botStoreDAO.getRequestsByTaskIdAndWFDefId(tblRpaRequest.getTaskId(),
					tblRpaRequest.getWorkflowDefid());
			boolean isAllowed = true;
			String userSignum = null;
			if (null != requests && requests.size() > 0) {
				for (TblRpaRequest r : requests) {
					for (TblRpaBotstaging st : r.getTblRpaBotstagings()) {
						if (!st.getStatus().equalsIgnoreCase(DEPLOYED)) {
							isAllowed = false;
							userSignum = r.getCreatedBy();
        					break;
        				}
        			}
					if (!isAllowed)
        				break;
        		}
    		}

			if (isAllowed) {
				// Saving request data in Tables::
            	tblRpaRequest.setCreatedOn(new Date());
            	tblRpaRequest.setRequestStatus(reqStatus);

            	botStoreDAO.createRpaRequest(tblRpaRequest);

				// TblRpaBotrequirement and TblRpaRequestTool dummy(no use now) table by RPA
				TblRpaBotrequirement bReq = tblRpaRequest.getTblRpaBotrequirements().iterator().next();
            	bReq.setTblRpaRequest(tblRpaRequest);
            	bReq.setCreatedOn(new Date());

            	botStoreDAO.createRpaBotrequirement(bReq);

				for (TblRpaRequestTool tool : bReq.getTblRpaRequestTools()) {
            		tool.setTblRpaBotrequirement(bReq);
            		tool.setCreatedOn(new Date());

                	botStoreDAO.createRpaRequestTool(tool);
            	}

				if (tblRpaRequest.getIsInputRequired()) {
					externalInterfaceManagmentService.uploadFileInContainer(infile, 
							AppConstants.BOT_CONF_FILES+"/"+tblRpaRequest.getRpaRequestId() + INPUT);
				}
				externalInterfaceManagmentService.uploadFileInContainer(outfile, 
						AppConstants.BOT_CONF_FILES+"/"+tblRpaRequest.getRpaRequestId() + "/output/");
				externalInterfaceManagmentService.uploadFileInContainer(logicfile, 
						AppConstants.BOT_CONF_FILES+"/"+tblRpaRequest.getRpaRequestId() + "/logic/");
            	getMailDataAndSend(tblRpaRequest, tblRpaRequest.getCreatedBy(), "CREATED");

				LOG.info("RPA Request Created Successfully. Request id is : {}" , tblRpaRequest.getRpaRequestId());
            	res.setApiSuccess(true);
				res.setData(tblRpaRequest.getRpaRequestId() + "");
				res.setResponseMsg(
						"RPA Request Created Successfully. Request id is : " + tblRpaRequest.getRpaRequestId());

			} else {
				LOG.info("RPA Request is already raised by the SIGNUM:: {}" , userSignum);
            	res.setApiSuccess(false);
				res.setData(tblRpaRequest.getRpaRequestId() + "");
				res.setResponseMsg("RPA Request is already raised by the SIGNUM:: " + userSignum);
    		}
		} catch (PropertyValueException he) {
			LOG.info("Error while Creating RPA Request:" + he.getStackTrace());
			he.printStackTrace();
        	res.setApiSuccess(false);
			res.setResponseMsg("Error in creating RPA request:: " + he.getStackTrace());
		} catch (HibernateException he) {
			LOG.info("Error while Creating RPA Request:" + he.getStackTrace());
			he.printStackTrace();
        	res.setApiSuccess(false);
			res.setResponseMsg("Error in creating RPA request:: " + he.getStackTrace());
		} catch (Exception e) {
        	try {
			} catch (Exception e2) {
				LOG.info("Error while Creating RPA Request:" + e.getMessage());
				e.printStackTrace();
	        	res.setApiSuccess(false);
				res.setResponseMsg("Error in creating RPA request:: " + e.getMessage());
			}
			LOG.info("Error while Creating RPA Request:" + e.getMessage());
			e.printStackTrace();
        	res.setApiSuccess(false);
			res.setResponseMsg("Error in creating RPA request:: " + e.getMessage());
		}

    	return res;
    }

	private void dbBotFileUpload(String requestId, String requestSignum, String fileName, String fileType,
			byte[] bytes) {
		try {
			if (fileName != null && fileType != null) {
				TblRpaBotFile tblRpaBotFile = new TblRpaBotFile();

	         	tblRpaBotFile.setRpaRequestId(Integer.valueOf(requestId));
	         	tblRpaBotFile.setFileName(fileName);
	         	tblRpaBotFile.setFileType(fileType);
	         	tblRpaBotFile.setDataFile(bytes);
	         	tblRpaBotFile.setCreatedBy(requestSignum);
	         	tblRpaBotFile.setCreatedOn(new Date());
	         	tblRpaBotFile.setIsActive(1);
	         	botStoreDAO.createUpdateRpaFile(tblRpaBotFile);

			}

		} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

	private void dbFileUpload(String requestId, String requestSignum, String folderNameForFileType,
			MultipartFile file) {
		try {
    		byte[] bytes = file.getBytes();
			String fileName = null;
			String fileType = null;

			if (folderNameForFileType.equalsIgnoreCase("input")) {
				fileName = "input.zip";
				fileType = "input";
			} else if (folderNameForFileType.equalsIgnoreCase("output")) {
				fileName = "output.zip";
				fileType = "output";
			} else if (folderNameForFileType.equalsIgnoreCase("code")) {
				fileName = "code.zip";
				fileType = "code";
			} else if (folderNameForFileType.equalsIgnoreCase("logic")) {
				fileName = "logic.zip";
				fileType = "logic";
			} else if (folderNameForFileType.equalsIgnoreCase("exe")) {
				fileName = "exe.zip";
				fileType = "exe";
			//	byte = GetBinaryFile(file);

			}
			
			// BLOB Upload
			/*
			 * if (fileName != null && fileType != null) { TblRpaBotFile tblRpaBotFile = new
			 * TblRpaBotFile();
			 * 
			 * tblRpaBotFile.setRpaRequestId(Integer.valueOf(requestId));
			 * tblRpaBotFile.setFileName(fileName); tblRpaBotFile.setFileType(fileType);
			 * tblRpaBotFile.setDataFile(bytes); tblRpaBotFile.setCreatedBy(requestSignum);
			 * tblRpaBotFile.setCreatedOn(new Date()); tblRpaBotFile.setIsActive(1);
			 * botStoreDAO.createUpdateRpaFile(tblRpaBotFile);
			 * 
			 * }
			 */

		} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

	private void botMigrationDB(String requestId, String requestSignum, String fileType, String fileName, byte[] bytes,
			String sType) {
		try {
			if (fileName != null && fileType != null) {
				externalInterfaceManagmentService.uploadFileInContainerFromByteArray(bytes,
						AppConstants.BOT_CONF_FILES+"/"+requestId + "/"+fileType+"/", fileName);
				
			}
		} catch (Exception e) {
    		e.printStackTrace();
			LOG.info("Error while Updating record in DB for : " + requestId + " -> " + fileName);
    	}
    }
	
	private void sendRequestCreationMail(String wfOwner, TblRpaRequest tblRpaRequest) throws Exception {

		Map<String, Object> placeholders = new HashMap<String, Object>();
		List<TblRpaRequest> tblRpaRequestList = null;
		try {
			tblRpaRequestList = getNewRequestList(tblRpaRequest.getCreatedBy());

			if (tblRpaRequestList != null) {
				for (TblRpaRequest tblRpaRequest1 : tblRpaRequestList) {
					if (tblRpaRequest1.getRpaRequestId() == tblRpaRequest.getRpaRequestId()) {

    					placeholders.put("requestID", tblRpaRequest1.getRpaRequestId());
						placeholders.put("taskID", tblRpaRequest1.getTaskId());
    					placeholders.put("wfDefID", tblRpaRequest1.getWorkflowDefid());
    					placeholders.put("subActivityID", tblRpaRequest1.getSubactivityId());

    					placeholders.put("requestName", tblRpaRequest1.getRequestName());
						placeholders.put("taskName", tblRpaRequest1.getTaskName());
    					placeholders.put("wfName", tblRpaRequest1.getWorkFlowName());
    					placeholders.put("subActivityName", tblRpaRequest1.getSubactivityName());
    				}
    			}
    		}
			EmployeeModel currentUser = null;
			if (wfOwner != null && wfOwner.trim().length() > 0 && !wfOwner.equalsIgnoreCase("null")) {
				currentUser = activityMasterDAO.getEmployeeBySignum(wfOwner);
				placeholders.put(AppConstants.CURRENT_USER_NAME,
						currentUser.getSignum() + " (" + currentUser.getEmployeeName() + ")");
				placeholders.put(AppConstants.CUSTOM_NOTIFICATIONS_TO, currentUser.getEmployeeEmailId());
				currentUser = activityMasterDAO.getEmployeeBySignum(tblRpaRequest.getCreatedBy());
				placeholders.put(AppConstants.CUSTOM_NOTIFICATIONS_CC, currentUser.getEmployeeEmailId());

				if (placeholders.containsKey(AppConstants.CUSTOM_NOTIFICATIONS_TO)
						&& placeholders.get(AppConstants.CUSTOM_NOTIFICATIONS_TO) != null
						&& placeholders.containsKey(AppConstants.CUSTOM_NOTIFICATIONS_CC)
						&& placeholders.get(AppConstants.CUSTOM_NOTIFICATIONS_CC) != null)
					emailService.sendMail(AppConstants.NEW_BOT_REQUEST, placeholders);

				LOG.info("Mail sent succesfully to : " + wfOwner + " and : " + tblRpaRequest.getCreatedBy());
			} else {
    			currentUser = activityMasterDAO.getEmployeeBySignum(tblRpaRequest.getCreatedBy());
    			placeholders.put(AppConstants.CUSTOM_NOTIFICATIONS_TO, currentUser.getEmployeeEmailId());
    			placeholders.put(AppConstants.CUSTOM_NOTIFICATIONS_CC, currentUser.getEmployeeEmailId());

				if (placeholders.containsKey(AppConstants.CUSTOM_NOTIFICATIONS_TO)
						&& placeholders.get(AppConstants.CUSTOM_NOTIFICATIONS_TO) != null
						&& placeholders.containsKey(AppConstants.CUSTOM_NOTIFICATIONS_CC)
						&& placeholders.get(AppConstants.CUSTOM_NOTIFICATIONS_CC) != null)
					emailService.sendMail(AppConstants.NEW_BOT_REQUEST, placeholders);

				LOG.info("Mail sent succesfully to : " + tblRpaRequest.getCreatedBy());
    		}

		} catch (Exception e) {
    		e.printStackTrace();
    		LOG.info("Error in method sendRequestCreationMail(String wfOwner, TblRpaRequest tblRpaRequest)");
    		throw e;
    	}
	}

	@Transactional("txManager")
	public RpaApiResponse updateNewRequest(TblRpaRequest tblRpaRequest, MultipartFile infile, MultipartFile outfile,
			MultipartFile logicfile, String reqStatus) throws IOException {

		RpaApiResponse res = new RpaApiResponse();

		TblRpaRequest req = botStoreDAO.getRPARequestDetails(tblRpaRequest.getRpaRequestId());
		if (null != req) {
			boolean isInsertedBeforeUpdate = req.getIsInputRequired();
			updateTblRpaRequest(tblRpaRequest, req);

			// there's changes in input file
			if (isInsertedBeforeUpdate && tblRpaRequest.getIsInputRequired()) {
				addOrUpdateBlobFile(infile, "input", tblRpaRequest);

			}
			// this was a input file bot now it is update to be no input file bot
			else if (isInsertedBeforeUpdate && !tblRpaRequest.getIsInputRequired()) {
				String filename = "input.zip";
				String rpaReqId = Integer.toString(tblRpaRequest.getRpaRequestId());

				externalInterfaceManagmentService.uploadFileInContainer(infile, 
						AppConstants.BOT_CONF_FILES+"/"+rpaReqId + INPUT);

				/*
				 * List<TblRpaBotFile> list = botStoreDAO.getRPAFile(rpaReqId, filename); if
				 * (!list.isEmpty()) { list.get(0).setModifiedBy(tblRpaRequest.getModifiedBy());
				 * list.get(0).setModifiedOn(new Date()); list.get(0).setIsActive(0);
				 * botStoreDAO.createUpdateRpaFile(list.get(0)); } else { throw new
				 * ApplicationException(200,
				 * "could not find the existing input file corresponding to the rpaID :" +
				 * req.getRpaRequestId()); }
				 */
			}
			// Bot was no input bot but now it's updated to mandatory input
			else if (!isInsertedBeforeUpdate && tblRpaRequest.getIsInputRequired()) {

				externalInterfaceManagmentService.uploadFileInContainer(infile, 
						AppConstants.BOT_CONF_FILES+"/"+tblRpaRequest.getRpaRequestId() + INPUT);
				//				dbFileUpload(tblRpaRequest.getRpaRequestId() + "", tblRpaRequest.getModifiedBy(), "input", infile);
			}

			addOrUpdateBlobFile(outfile, "output", tblRpaRequest);
			addOrUpdateBlobFile(logicfile, "logic", tblRpaRequest);

			LOG.info("RPA Request Update Successfully. Request id is : " + tblRpaRequest.getRpaRequestId());
			res.setApiSuccess(true);
			res.setData(tblRpaRequest.getRpaRequestId() + "");
			res.setResponseMsg("RPA Request Update Successfully. Request id is : " + tblRpaRequest.getRpaRequestId());
		}

		return res;
	}

	private void updateTblRpaRequest(TblRpaRequest tblRpaRequest, TblRpaRequest req) {

		req.setIsInputRequired(tblRpaRequest.getIsInputRequired());
		req.setRequestName(tblRpaRequest.getRequestName());
		req.setSpocsignum(tblRpaRequest.getSpocsignum());
		req.setDescription(tblRpaRequest.getDescription());
		req.setCurrentExecutioncountWeekly(tblRpaRequest.getCurrentExecutioncountWeekly());
		req.setCurrentAvgExecutiontime(tblRpaRequest.getCurrentAvgExecutiontime());
		req.setModifiedBy(tblRpaRequest.getModifiedBy());
		req.setModifiedOn(new Date());
        
		botStoreDAO.createRpaRequest(req);
            	}

	private void addOrUpdateBlobFile(MultipartFile file, String filename, TblRpaRequest tblRpaRequest)
			throws IOException {

		if (file != null) {
			String rpaReqId = Integer.toString(tblRpaRequest.getRpaRequestId());
			/*
			 * List<TblRpaBotFile> list = botStoreDAO.getRPAFile(rpaReqId, filename +
			 * ".zip");
			 * 
			 * if (!list.isEmpty()) {
			 * list.get(0).setModifiedBy(tblRpaRequest.getModifiedBy());
			 * list.get(0).setModifiedOn(new Date()); byte[] bytes = file.getBytes();
			 * list.get(0).setDataFile(bytes); botStoreDAO.createUpdateRpaFile(list.get(0));
			 * } else { dbFileUpload(tblRpaRequest.getRpaRequestId() + "",
			 * tblRpaRequest.getModifiedBy(), filename, file); }
			 */
			externalInterfaceManagmentService.uploadFileInContainer(file, 
					AppConstants.BOT_CONF_FILES+"/"+rpaReqId + "/"+filename+"/");
		}
	}



    public  byte[] zipBytes(String filename, byte[] input) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(baos);
        ZipEntry entry = new ZipEntry(filename);
        entry.setSize(input.length);
        zos.putNextEntry(entry);
        zos.write(input);
        zos.closeEntry();
        zos.close();
        return baos.toByteArray();
    }

	@Transactional("txManager")
	public RpaApiResponse createBotRequestForJavaPython(TblRpaRequest tblRpaRequest, TblRpaBotstaging tblRpaBotstaging,
			MultipartFile infile, MultipartFile outfile, MultipartFile logicfile, MultipartFile codefile,
			MultipartFile exefile,MultipartFile whlfile, String wfOwner) {

		RpaApiResponse res = new RpaApiResponse();
		String jarOrPythonExeName = null;

		try {
			res = createNewRequest(tblRpaRequest, infile, outfile, logicfile, "CREATE", wfOwner);

			if (res.isApiSuccess()) {

				if (null != res.getData()) {
					// Getting target file name from zip file.
					
					String fileName = null;
					

					if (tblRpaBotstaging.getBotlanguage().toUpperCase().equalsIgnoreCase("PYTHON")) {
						 fileName = whlfile.getOriginalFilename();
					}
					else {
						 fileName = BotStoreUtil.getFileNameFromZip(exefile);
					}
					tblRpaBotstaging.setTargetExecutionFileName(fileName);
                    tblRpaBotstaging.setBotid(tblRpaRequest.getRpaRequestId());
        			tblRpaBotstaging.setTblRpaRequest(tblRpaRequest);
        			tblRpaBotstaging.setCreatedOn(new Date());
					if (tblRpaBotstaging.getParallelWoexecution() != null)
        				tblRpaBotstaging.setParallelWoexecution(tblRpaBotstaging.getParallelWoexecution());
        			else {
        			tblRpaBotstaging.setParallelWoexecution("NO"); }
        			tblRpaBotstaging.setIsActive(1);
        			

                	botStoreDAO.createBotStaging(tblRpaBotstaging);

					// Upload Code zip file
                	externalInterfaceManagmentService.uploadFileInContainer(codefile, 
                			AppConstants.BOT_CONF_FILES+"/"+tblRpaRequest.getRpaRequestId() + "/code/");

					// Upload exe zip file
//                	externalInterfaceManagmentService.uploadFileInContainer(exefile, 
//                			AppConstants.BOT_CONF_FILES+"/"+tblRpaRequest.getRpaRequestId() + "/exe/");

					if (tblRpaBotstaging.getBotlanguage().equalsIgnoreCase("Java")) {
						// unzip Zip file in
						externalInterfaceManagmentService.uploadFileInContainer(exefile, 
	                			AppConstants.BOT_CONF_FILES+"/"+tblRpaRequest.getRpaRequestId() + "/exe/");
						
						if ( exefile != null  && BotStoreUtil.unzipFileFromZipinBLOB(exefile) != null) {
							// Upload exe unzip file from local folder
							byte[] bytes = BotStoreUtil.unzipFileFromZipinBLOB(exefile);
							String jarFileName = tblRpaRequest.getRpaRequestId() + ".jar";
							
							externalInterfaceManagmentService.uploadFileInContainerFromByteArray(bytes,
									AppConstants.BOT_CONF_FILES+"/"+tblRpaRequest.getRpaRequestId() + "/bot/", jarFileName);

						} else {
                        	res.setApiSuccess(false);
                            res.setResponseMsg(AppConstants.EXE_ERROR);
                            throw new Exception();
                		}
					} 
					
					//Whl file code for Python 
					else if (tblRpaBotstaging.getBotlanguage().toUpperCase().equalsIgnoreCase("PYTHON")) {
						if (null != whlfile) {
							jarOrPythonExeName = whlfile.getOriginalFilename(); 
							if(whlfile !=null && jarOrPythonExeName.endsWith(".whl")) {
								externalInterfaceManagmentService.uploadFileInContainerFromByteArray(whlfile.getBytes(), 
			    						AppConstants.BOT_CONF_FILES+"/"+tblRpaRequest.getRpaRequestId() + "/bot/", jarOrPythonExeName);															
							}
						}
						 else {
	                        	res.setApiSuccess(false);
	                            res.setResponseMsg(AppConstants.WHL_ERROR);
	                            throw new Exception();
	                		}
					}
					
					else if (tblRpaBotstaging.getBotlanguage().equalsIgnoreCase("DotNet")) {
						// unzip Zip file in
						if (BotStoreUtil.unzipFileFromZipinBLOB(exefile) != null) {
							// Upload exe unzip file from local folder:
							byte[] bytes = BotStoreUtil.unzipFileFromZipinBLOB(exefile);
							String jarFileName = tblRpaRequest.getRpaRequestId() + ".exe";
							externalInterfaceManagmentService.uploadFileInContainerFromByteArray(bytes,
									AppConstants.BOT_CONF_FILES+"/"+tblRpaRequest.getRpaRequestId() + "/bot/", jarFileName);
						} else {
                        	res.setApiSuccess(false);
                            res.setResponseMsg(AppConstants.EXE_ERROR);
                            throw new Exception();
                		}
                	}

					// save botconfig json
					if (tblRpaBotstaging.getBotConfigJson() != null) {
	                	BotConfig botConfig = new BotConfig();
	                	botConfig.setJson(tblRpaBotstaging.getBotConfigJson());
						botConfig.setReferenceId(tblRpaBotstaging.getBotid() + "");
	                	botConfig.setSignum(tblRpaBotstaging.getCreatedBy());
	                	botConfig.setType(AppConstants.BOT_CONFIG_TYPE_BOT);
	                 	rpaService.saveBotConfig(botConfig);
                	}
					LOG.info(AppConstants.RPA_REQUEST_CREATION  , tblRpaRequest.getRpaRequestId());
                    	}
                	}
		} catch (PropertyValueException he) {
			LOG.info(AppConstants.ERROR_CREATING_RPA_REQUEST , he.getStackTrace());
			he.printStackTrace();
        	res.setApiSuccess(false);
			res.setResponseMsg(AppConstants.ERROR_CREATING_RPA_REQUEST_DOUBLE + he.getStackTrace());
		} catch (HibernateException he) {
			LOG.info(AppConstants.ERROR_CREATING_RPA_REQUEST + he.getStackTrace());
			he.printStackTrace();
        	res.setApiSuccess(false);
			res.setResponseMsg(AppConstants.ERROR_CREATING_RPA_REQUEST_DOUBLE + he.getStackTrace());
		} catch (Exception e) {
        	try {
				File file = new File(configurations.getStringProperty(ConfigurationFilesConstants.BOTSTORE_UPLOAD_PATH)
						+ tblRpaRequest.getRpaRequestId());
				if (file.exists()) {
            		FileUtils.deleteDirectory(file);
            	}
			} catch (Exception e2) {
				LOG.info(AppConstants.ERROR_CREATING_RPA_REQUEST , e.getMessage());
				e.printStackTrace();
            	res.setApiSuccess(false);
				res.setResponseMsg(AppConstants.ERROR_CREATING_RPA_REQUEST_DOUBLE + e.getMessage());
			}

			LOG.info(AppConstants.ERROR_CREATING_RPA_REQUEST , e.getMessage());
			e.printStackTrace();
        	res.setApiSuccess(false);
			res.setResponseMsg(AppConstants.ERROR_CREATING_RPA_REQUEST_DOUBLE + e.getMessage());
		}
		return res;
    }

    @Transactional("txManager")
	public RpaApiResponse updateBotRequest(TblRpaRequest tblRpaRequest, TblRpaBotstaging tblRpaBotstaging,
			MultipartFile infile, MultipartFile outfile, MultipartFile logicfile, MultipartFile codefile,
			MultipartFile exefile, MultipartFile whlfile, String botConfigJson) {

		RpaApiResponse res = new RpaApiResponse();
		try {
			LOG.info("tblRpaBotstaging.getBotlanguage()---> {}", tblRpaBotstaging.getBotlanguage());
			String macroOrJarOrPythonExeName = null;

			if (tblRpaBotstaging.getBotlanguage().equalsIgnoreCase("MACRO")) {
				res.setApiSuccess(false);
				res.setData(tblRpaRequest.getRpaRequestId() + "");
				res.setResponseMsg("Update Bot functionality for Macro Bot is not supported.");
				return res;
			}

			if (tblRpaBotstaging.getBotlanguage().equalsIgnoreCase("Java")) {
				if (null != exefile)
					macroOrJarOrPythonExeName = BotStoreUtil.getFileNameFromZip(exefile);
			} else if (tblRpaBotstaging.getBotlanguage().toUpperCase().equalsIgnoreCase("PYTHON")) {
				if (null != whlfile && !whlfile.isEmpty())
					macroOrJarOrPythonExeName = whlfile.getOriginalFilename();
			} else {
				macroOrJarOrPythonExeName = BotStoreUtil.getFileNameFromZip(codefile);
			}

			LOG.info("macroOrJarOrPythonExeName---> {}", macroOrJarOrPythonExeName);

			if (tblRpaBotstaging.getStatusDescription() != null
					&& !tblRpaBotstaging.getStatusDescription().equalsIgnoreCase("ByDev")) {

				res = updateNewRequest(tblRpaRequest, infile, outfile, logicfile, "UPDATED");
			} else {
				res.setData(tblRpaRequest.getRpaRequestId() + "");
				TblRpaRequest req = botStoreDAO.getRPARequestDetails(tblRpaRequest.getRpaRequestId());
				req.setIsInputRequired(tblRpaRequest.getIsInputRequired());
				req.setModifiedOn(new Date());
				req.setModifiedBy(tblRpaBotstaging.getModifiedBy());
				botStoreDAO.createRpaRequest(req);
				if (tblRpaRequest.getIsInputRequired()) {
					addOrUpdateBlobFile(infile, "input", tblRpaRequest);
				}
			}

			if (null != res.getData()) {
				List<TblRpaBotstaging> stagBots = botStoreDAO.getStagingBotsByReqId(tblRpaRequest.getRpaRequestId());

				for (TblRpaBotstaging b : stagBots) {
					if (b.getStatus().equalsIgnoreCase(TESTED))
						botStoreDAO.deleteBotStage(b);
					else {
						if (tblRpaBotstaging.getStatusDescription() != null
								&& !tblRpaBotstaging.getStatusDescription().equalsIgnoreCase("ByDev")) {
							if (tblRpaBotstaging.getBotname() != null)
								b.setBotname(tblRpaBotstaging.getBotname());

							b.setRpaexecutionTime(tblRpaBotstaging.getRpaexecutionTime());

							if (tblRpaBotstaging.getModuleClassName() != null)
								b.setModuleClassName(tblRpaBotstaging.getModuleClassName());

							b.setModuleClassMethod(tblRpaBotstaging.getModuleClassMethod());

							if (null != tblRpaBotstaging.getReuseFactor())
								b.setReuseFactor(tblRpaBotstaging.getReuseFactor());
							if (null != tblRpaBotstaging.getLineOfCode())
								b.setLineOfCode(tblRpaBotstaging.getLineOfCode());

							b.setParallelWoexecution(tblRpaBotstaging.getParallelWoexecution());
							b.setModifiedBy(tblRpaBotstaging.getModifiedBy());
							b.setModifiedOn(new Date());
							if (tblRpaBotstaging.getLanguageBaseVersionID() != null)
								b.setLanguageBaseVersionID(tblRpaBotstaging.getLanguageBaseVersionID());

						} else {
							if (tblRpaBotstaging.getModuleClassName() != null)
								b.setModuleClassName(tblRpaBotstaging.getModuleClassName());

							b.setModuleClassMethod(tblRpaBotstaging.getModuleClassMethod());
							if (null != tblRpaBotstaging.getReuseFactor())
								b.setReuseFactor(tblRpaBotstaging.getReuseFactor());
							if (null != tblRpaBotstaging.getLineOfCode())
								b.setLineOfCode(tblRpaBotstaging.getLineOfCode());

							if (tblRpaBotstaging.getLanguageBaseVersionID() != null)
								b.setLanguageBaseVersionID(tblRpaBotstaging.getLanguageBaseVersionID());
						}

						if (macroOrJarOrPythonExeName != null) {
							b.setTargetExecutionFileName(macroOrJarOrPythonExeName);

						}

						botStoreDAO.createBotStaging(b);
					}
				}

				// Upload Code zip file
				if (null != codefile) {

					String rpaReqId = Integer.toString(tblRpaRequest.getRpaRequestId());
					externalInterfaceManagmentService.uploadFileInContainer(codefile,
							AppConstants.BOT_CONF_FILES + "/" + rpaReqId + "/code/");
				}

				// Upload exe zip file
				if (null != exefile) {

					externalInterfaceManagmentService.uploadFileInContainer(exefile,
							AppConstants.BOT_CONF_FILES + "/" + tblRpaRequest.getRpaRequestId() + "/exe/");

					if (null != tblRpaBotstaging.getBotlanguage()
							&& tblRpaBotstaging.getBotlanguage().equalsIgnoreCase("JavaTest")) {
						// unzip Zip file in
						if (BotStoreUtil.unzipFileFromZipinBLOB(exefile) != null) {
							// Upload exe unzip file from local folder:
							byte[] bytes = BotStoreUtil.unzipFileFromZipinBLOB(exefile);
							String jarFileName = tblRpaRequest.getRpaRequestId() + ".jar";
							String rpaReqId = Integer.toString(tblRpaRequest.getRpaRequestId());

							externalInterfaceManagmentService.uploadFileInContainerFromByteArray(bytes,
									AppConstants.BOT_CONF_FILES + "/" + rpaReqId + "/bot/", jarFileName);
						} else {
							res.setApiSuccess(false);
							res.setResponseMsg("The exeFile did not unzip correctly.");
							throw new Exception();
						}
					}
				}

				if (whlfile != null && !whlfile.isEmpty() && macroOrJarOrPythonExeName.endsWith(".whl")) {

					externalInterfaceManagmentService.uploadFileInContainerFromByteArray(whlfile.getBytes(),
							AppConstants.BOT_CONF_FILES + "/" + tblRpaRequest.getRpaRequestId() + "/bot/",
							macroOrJarOrPythonExeName);
				}

				if (botConfigJson != null) {

					BotConfig botConfig = new BotConfig();
					botConfig.setDescription(tblRpaRequest.getRpaRequestId() + "");
					botConfig.setReferenceId(tblRpaRequest.getRpaRequestId() + "");
					botConfig.setJson(botConfigJson);
					botConfig.setSignum(tblRpaBotstaging.getAssignedTo());
					botConfig.setType(AppConstants.BOT_CONFIG_TYPE_BOT);
					rpaService.updateBotConfig(botConfig);
				}

				LOG.info("RPA Request Updated Successfully. Request id is : {}", tblRpaRequest.getRpaRequestId());
				res.setApiSuccess(true);
				res.setData(tblRpaRequest.getRpaRequestId() + "");
				res.setResponseMsg(
						"RPA Request Update Successfully. Request id is : " + tblRpaRequest.getRpaRequestId());

			}
		} catch (Exception e) {
			LOG.info("Error while Updating RPA Request: {}", e.getMessage());
			e.printStackTrace();
			res.setApiSuccess(false);
			res.setResponseMsg("Error in Updating RPA request:: " + e.getMessage());
		}
		return res;
    }

    @Transactional("txManager")
	public RpaApiResponse createBotStaging(TblRpaBotstaging tblRpaBotstaging) {
		RpaApiResponse res = new RpaApiResponse();
		try {
    		int rpaRequestId = tblRpaBotstaging.getTblRpaRequest().getRpaRequestId();

			TblRpaRequest rpaReqForMail = botStoreDAO.getRPARequestById(rpaRequestId).get(0);
			if(tblRpaBotstaging.getBotlanguage().equalsIgnoreCase("Python")) {
				List<TblRpaBotstaging >stagingAssigned=botStoreDAO.getRPAStagingForAccepted(rpaRequestId, ASSIGNED);
				if(!stagingAssigned.isEmpty())
				tblRpaBotstaging.setLanguageBaseVersionID(stagingAssigned.get(0).getLanguageBaseVersionID());
			}

    		tblRpaBotstaging.setTblRpaRequest(rpaReqForMail);

    		tblRpaBotstaging.setCreatedOn(new Date());
    		botStoreDAO.createBotStaging(tblRpaBotstaging);

			if (tblRpaBotstaging.getStatus().equalsIgnoreCase(TESTED)) {
    			getMailDataAndSend(rpaReqForMail, tblRpaBotstaging.getCreatedBy(), TESTED);
    		}

    		res.setApiSuccess(true);
        	res.setResponseMsg("BOT Stage created successfully");
		} catch (Exception e) {
			LOG.info("Error in creating BOT stage entry:" + e.getMessage());
			e.printStackTrace();
        	res.setApiSuccess(false);
			res.setResponseMsg("Error in creating BOT stage entry:: " + e.getMessage());
		}
    	return res;
    }

    @Transactional("txManager")
	public RpaApiResponse assignRequestToDev(int reqId, String userSignum, String adminSignum) {
		RpaApiResponse res = new RpaApiResponse();
		TblRpaRequest reqt = null;

		try {
			List<TblRpaRequest> rpaRequestModel = botStoreDAO.getRPARequestById(reqId);
			reqt = rpaRequestModel.get(0);

			if ((reqt.getTblRpaDeployedBot().size() > 0
					&& reqt.getTblRpaDeployedBot().iterator().next().getStatus().equalsIgnoreCase(DEPLOYED))
					|| reqt.getRequestStatus().equalsIgnoreCase("CREATE")) {
            	res.setApiSuccess(false);
            	res.setResponseMsg("BOT cannot be assigned to developer as it is not a development request.");
			} else {
				List<TblRpaBotstaging> stagAssigenedBots = botStoreDAO.getStagingBotsByReqId(reqId);
				if (null != stagAssigenedBots && stagAssigenedBots.size() > 0) {
					for (TblRpaBotstaging st : stagAssigenedBots) {
        				st.setIsActive(0);
        				botStoreDAO.createBotStaging(st);
        			}
        		}

				TblRpaBotstaging botStag = new TblRpaBotstaging();

        		botStag.setBotid(reqId);
				TblRpaRequest req = botStoreDAO.getRPARequestById(reqId).get(0);
        		botStag.setTblRpaRequest(req);
        		botStag.setBotname(reqt.getRequestName());
        		botStag.setAssignedTo(userSignum);
        		botStag.setStatus(ASSIGNED);
        		botStag.setStatusDescription(ASSIGNED);
        		botStag.setCreatedBy(adminSignum);
        		botStag.setCreatedOn(new Date());
        		botStag.setIsActive(1);

        		botStoreDAO.createBotStaging(botStag);
        		res.setApiSuccess(true);
				res.setData(reqId + "");
				res.setResponseMsg(
						"BOT assigned successfully to developer " + userSignum + " for Request id:: " + reqId);

				// TblRpaRequest tblRpaRequest= botStoreDAO.getRPARequestById(reqId).get(0);
    			getMailDataAndSend(reqt, adminSignum, ASSIGNED);

            }

		} catch (Exception e) {
			LOG.info("Error in assign BOT to developer:" + e.getMessage());
			e.printStackTrace();
        	res.setApiSuccess(false);
			res.setResponseMsg("Error in assign BOT to developer:: " + e.getMessage());
		}
    	return res;
    }

    @Transactional("txManager")
	public RpaApiResponse updateBotForNonFeasibile(int reqId, String userSignum, String desc) {
		RpaApiResponse res = new RpaApiResponse();
		try {
			List<TblRpaBotstaging> stageBots = null;

			stageBots = botStoreDAO.getRPAStagingForReject(reqId);
			if (null != stageBots && stageBots.size() > 0) {
				TblRpaBotstaging stageBotToUpdate = stageBots.get(0);

				TblRpaBotstaging stageBotToInsert = new TblRpaBotstaging();
				TblRpaRequest rpaReq = botStoreDAO.getRPARequestById(reqId).get(0);

        		stageBotToInsert.setBotid(stageBotToUpdate.getBotid());
        		stageBotToInsert.setTblRpaRequest(rpaReq);
        		stageBotToInsert.setAssignedTo(stageBotToUpdate.getAssignedTo());
        		stageBotToInsert.setBotname(stageBotToUpdate.getBotname());
        		stageBotToInsert.setRpaexecutionTime(stageBotToUpdate.getRpaexecutionTime());
        		stageBotToInsert.setBotlanguage(stageBotToUpdate.getBotlanguage());
        		stageBotToInsert.setTargetExecutionFileName(stageBotToUpdate.getTargetExecutionFileName());
        		stageBotToInsert.setModuleClassName(stageBotToUpdate.getModuleClassName());
        		stageBotToInsert.setModuleClassMethod(stageBotToUpdate.getModuleClassMethod());
        		stageBotToInsert.setParallelWoexecution(stageBotToUpdate.getParallelWoexecution());
        		stageBotToInsert.setReuseFactor(stageBotToUpdate.getReuseFactor());
        		stageBotToInsert.setLineOfCode(stageBotToUpdate.getLineOfCode());
        		stageBotToInsert.setStatus(REJECTED);
        		stageBotToInsert.setStatusDescription(desc);
        		stageBotToInsert.setCreatedOn(new Date());
        		stageBotToInsert.setCreatedBy(userSignum);
        		stageBotToInsert.setIsActive(1);

        		botStoreDAO.createBotStaging(stageBotToInsert);

				TblRpaRequest rpaReqForMail = botStoreDAO.getRPARequestById(reqId).get(0);
    			getMailDataAndSend(rpaReqForMail, userSignum, REJECTED);

        		res.setApiSuccess(true);
				res.setResponseMsg("Record updated successfully for Request id:: " + reqId);
			} else {
            	res.setApiSuccess(false);
                res.setResponseMsg("No data found for rejection.");
    		}
		} catch (Exception e) {
			LOG.info("Error in updateBotForNonFeasibile()::" + e.getMessage());
			e.printStackTrace();
        	res.setApiSuccess(false);
			res.setResponseMsg("Error in updateBotForNonFeasibile():: " + e.getMessage());
		}
    	return res;
    }

	// Used for Insert into stage if Admin assign to another dev when already
	// assigned a dev
	// else update BOT stage status to ACCEPTED to a dev
    @Transactional("txManager")
	public RpaApiResponse updateBotTestResults(int reqId, String userSignum, String status) {
		RpaApiResponse res = new RpaApiResponse();

		try {
			TblRpaBotstaging stageBotToUpdate = null;
			TblRpaBotstaging stageBotToInsert = null;

			List<TblRpaBotstaging> stagBots = botStoreDAO.getRPAStagingForAccepted(reqId, userSignum, status);
			if (null != stagBots && stagBots.size() > 0) {
				stageBotToUpdate = stagBots.get(0);
				/*    			
    		
    			
    			
				*/
    			stageBotToUpdate.setModifiedOn(new Date());
    			stageBotToUpdate.setModifiedBy(userSignum);

    			botStoreDAO.createBotStaging(stageBotToUpdate);
			} else {

				stagBots = botStoreDAO.getRPAStagingForAccepted(reqId, userSignum, ACCEPTED);

				if (null != stagBots && stagBots.size() > 0) {
					stageBotToUpdate = stagBots.get(0);

					stageBotToInsert = new TblRpaBotstaging();
					TblRpaRequest rpaReq = new TblRpaRequest();
            		rpaReq.setRpaRequestId(reqId);

            		stageBotToInsert.setBotid(stageBotToUpdate.getBotid());
            		stageBotToInsert.setTblRpaRequest(rpaReq);
            		stageBotToInsert.setAssignedTo(userSignum);
            		stageBotToInsert.setBotname(stageBotToUpdate.getBotname());
            		stageBotToInsert.setRpaexecutionTime(stageBotToUpdate.getRpaexecutionTime());
            		stageBotToInsert.setBotlanguage(stageBotToUpdate.getBotlanguage());
            		stageBotToInsert.setTargetExecutionFileName(stageBotToUpdate.getTargetExecutionFileName());
            		stageBotToInsert.setModuleClassName(stageBotToUpdate.getModuleClassName());
            		stageBotToInsert.setModuleClassMethod(stageBotToUpdate.getModuleClassMethod());
            		stageBotToInsert.setParallelWoexecution(stageBotToUpdate.getParallelWoexecution());
            		stageBotToInsert.setReuseFactor(stageBotToUpdate.getReuseFactor());
            		stageBotToInsert.setLineOfCode(stageBotToUpdate.getLineOfCode());
            		stageBotToInsert.setStatus(status);
					stageBotToInsert.setStatusDescription(status + " By User:" + userSignum);
            		stageBotToInsert.setCreatedOn(new Date());
            		stageBotToInsert.setCreatedBy(userSignum);
            		stageBotToInsert.setIsActive(1);

        			botStoreDAO.createBotStaging(stageBotToInsert);
				} else {
                	res.setApiSuccess(false);
                    res.setResponseMsg("You are not allowed to test this flow.");
        		}
    		}

        	res.setApiSuccess(true);
        	res.setResponseMsg("Bot Test Results updated Successfully.");

			/*
			 * tblRpaBotstaging.setCreatedOn(new Date());
			 * botStoreDAO.createBotStaging(tblRpaBotstaging);
			 */
		} catch (Exception e) {
			LOG.info("Error while Creating BOT Stage:" + e.getMessage());
			e.printStackTrace();
        	res.setApiSuccess(false);
			res.setResponseMsg("Error in creating RPA request:: " + e.getMessage());
		}

    	return res;
    }

	// Used for BOT APPROVE
    @Transactional("txManager")
	public RpaApiResponse approveBot(int reqId, String userSignum)  {
		RpaApiResponse res = new RpaApiResponse();
		try {
			TblRpaBotstaging stageBotToUpdate = null;
			TblRpaBotstaging stageBotToInsert = null;
			TblRpaDeployedBot deployBotToInsert = null;

			// List<TblRpaBotstaging> stagBots= botStoreDAO.getRPAStagingForAccepted(reqId,
			// userSignum, "TESTED");
			List<TblRpaBotstaging> stagBots = botStoreDAO.getRPAStagingForAccepted(reqId, "TESTED");

			if (null != stagBots && stagBots.size() > 0) {
				stageBotToUpdate = stagBots.get(0);

				stageBotToInsert = new TblRpaBotstaging();

				TblRpaRequest rpaReqForMail = botStoreDAO.getRPARequestById(reqId).get(0);

        		stageBotToInsert.setBotid(stageBotToUpdate.getBotid());
        		stageBotToInsert.setTblRpaRequest(rpaReqForMail);
        		stageBotToInsert.setAssignedTo(stageBotToUpdate.getAssignedTo());
        		stageBotToInsert.setBotname(stageBotToUpdate.getBotname());
        		stageBotToInsert.setRpaexecutionTime(stageBotToUpdate.getRpaexecutionTime());
        		stageBotToInsert.setBotlanguage(stageBotToUpdate.getBotlanguage());
        		stageBotToInsert.setTargetExecutionFileName(stageBotToUpdate.getTargetExecutionFileName());
        		stageBotToInsert.setModuleClassName(stageBotToUpdate.getModuleClassName());
        		stageBotToInsert.setModuleClassMethod(stageBotToUpdate.getModuleClassMethod());
        		stageBotToInsert.setParallelWoexecution(stageBotToUpdate.getParallelWoexecution());
        		stageBotToInsert.setReuseFactor(stageBotToUpdate.getReuseFactor());
        		stageBotToInsert.setLineOfCode(stageBotToUpdate.getLineOfCode());
        		stageBotToInsert.setStatus("APPROVED");
				stageBotToInsert.setStatusDescription("Approved by User:" + userSignum);
        		stageBotToInsert.setCreatedOn(new Date());
        		stageBotToInsert.setCreatedBy(userSignum);
        		stageBotToInsert.setIsActive(1);
        		stageBotToInsert.setLanguageBaseVersionID(stageBotToUpdate.getLanguageBaseVersionID());
        		
    			botStoreDAO.createBotStaging(stageBotToInsert);

    			getMailDataAndSend(rpaReqForMail, rpaReqForMail.getCreatedBy(), "APPROVED");

    			res.setApiSuccess(true);
                res.setResponseMsg("BOT Approved successfully.");
                LOG.info("BOT Approval Request: SUCCESS");
			} else {
            	res.setApiSuccess(false);
                res.setResponseMsg("Can not be Approved now as Testing is not done yet.");
    			LOG.info("Can not be Approved now as Testing is not done yet");
    		}

			/*
			 * tblRpaBotstaging.setCreatedOn(new Date());
			 * botStoreDAO.createBotStaging(tblRpaBotstaging);
			 */
		} catch (Exception e) {
			LOG.info("Error while Approving BOT:" + e.getMessage());
			e.printStackTrace();
        	res.setApiSuccess(false);
			res.setResponseMsg("Error while Approving RPA:: " + e.getMessage());
		}
    	return res;
    }

	// Used for BOT DEPLOY
    @Transactional("txManager")
	public RpaApiResponse deployBot(int reqId, String userSignum){
		RpaApiResponse res = new RpaApiResponse();
		try {
			TblRpaBotstaging stageBotToUpdate = null;
			TblRpaBotstaging stageBotToInsert = null;
			TblRpaDeployedBot deployBotToInsert = null;

			
			List<TblRpaBotstaging> stagBots = botStoreDAO.getRPAStagingForAccepted(reqId, "APPROVED");

			if (null != stagBots && stagBots.size() > 0) {
				stageBotToUpdate = stagBots.get(0);

				stageBotToInsert = new TblRpaBotstaging();
				TblRpaRequest rpaReq = botStoreDAO.getRPARequestById(reqId).get(0);

        		stageBotToInsert.setBotid(stageBotToUpdate.getBotid());
        		stageBotToInsert.setTblRpaRequest(rpaReq);
				stageBotToInsert.setAssignedTo(stageBotToUpdate.getAssignedTo());
        		stageBotToInsert.setBotname(stageBotToUpdate.getBotname());
        		stageBotToInsert.setRpaexecutionTime(stageBotToUpdate.getRpaexecutionTime());
        		stageBotToInsert.setBotlanguage(stageBotToUpdate.getBotlanguage());
        		stageBotToInsert.setTargetExecutionFileName(stageBotToUpdate.getTargetExecutionFileName());
        		stageBotToInsert.setModuleClassName(stageBotToUpdate.getModuleClassName());
        		stageBotToInsert.setModuleClassMethod(stageBotToUpdate.getModuleClassMethod());
        		stageBotToInsert.setParallelWoexecution(stageBotToUpdate.getParallelWoexecution());
        		stageBotToInsert.setReuseFactor(stageBotToUpdate.getReuseFactor());
        		stageBotToInsert.setLineOfCode(stageBotToUpdate.getLineOfCode());
        		stageBotToInsert.setStatus("DEPLOYED");
				stageBotToInsert.setStatusDescription("Deployed by User:" + userSignum);
        		stageBotToInsert.setCreatedOn(new Date());
        		stageBotToInsert.setCreatedBy(userSignum);
        		stageBotToInsert.setIsActive(1);
        		stageBotToInsert.setIsRunOnServer(0);
        		stageBotToInsert.setLanguageBaseVersionID(stageBotToUpdate.getLanguageBaseVersionID());

    			botStoreDAO.createBotStaging(stageBotToInsert);

				deployBotToInsert = new TblRpaDeployedBot();
				TblRpaRequest rpaReqDep = botStoreDAO.getRPARequestById(reqId).get(0);

        		deployBotToInsert.setBotid(stageBotToUpdate.getBotid());
        		deployBotToInsert.setTblRpaRequest(rpaReqDep);
        		deployBotToInsert.setBotname(stageBotToUpdate.getBotname());
				deployBotToInsert.setCurrentAvgExecutionTime(stageBotToUpdate.getTblRpaRequest().getCurrentAvgExecutiontime());
        		deployBotToInsert.setRpaexecutionTime(stageBotToUpdate.getRpaexecutionTime());
        		deployBotToInsert.setBotlanguage(stageBotToUpdate.getBotlanguage());
        		deployBotToInsert.setTargetExecutionFileName(stageBotToUpdate.getTargetExecutionFileName());
        		deployBotToInsert.setModuleClassName(stageBotToUpdate.getModuleClassName());
        		deployBotToInsert.setModuleClassMethod(stageBotToUpdate.getModuleClassMethod());
        		deployBotToInsert.setParallelWoexecution(stageBotToUpdate.getParallelWoexecution());
        		deployBotToInsert.setReuseFactor(stageBotToUpdate.getReuseFactor());
        		deployBotToInsert.setLineOfCode(stageBotToUpdate.getLineOfCode());
        		deployBotToInsert.setStatus("DEPLOYED");
        		deployBotToInsert.setCreatedOn(new Date());
        		deployBotToInsert.setCreatedBy(userSignum);
        		deployBotToInsert.setIsActive(1);
        		deployBotToInsert.setIsRunOnServer(0);
        		deployBotToInsert.setIsAuditPass(-1);
        		deployBotToInsert.setLanguageBaseVersionID(stageBotToUpdate.getLanguageBaseVersionID());
				deployBotToInsert.setIsInputRequired(rpaReqDep.getIsInputRequired());
				TblRpaRequest rpaRequestModel = botStoreDAO.getRPARequestDetails(reqId);
				if (rpaRequestModel.getReferenceBotId() != null) {
        	    deployBotToInsert.setReferenceBotId(rpaRequestModel.getReferenceBotId());
        		}
    			botStoreDAO.createRpaDeployBot(deployBotToInsert);

				/*
				 * if (stageBotToUpdate.getBotlanguage().toLowerCase().contains("python")) {
				 * 
				 * String filename = "SuperBOT-Python.jar";
				 * 
				 * String jarFileName = String.valueOf(reqId) + ".jar";
				 * 
				 * //uploadBOTfile
				 * externalInterfaceManagmentService.uploadFileInContainerFromByteArray(
				 * downloadSuperBOT(filename),
				 * AppConstants.BOT_CONF_FILES+"/"+String.valueOf(reqId) + "/bot/",
				 * jarFileName);
				 * 
				 * } else if (stageBotToUpdate.getBotlanguage().toLowerCase().contains("java"))
				 * { // Jar file already uploaded in CreateBot and SubmitBotWithLang. }
				 */

				TblRpaRequest rpaReqForMail = botStoreDAO.getRPARequestById(reqId).get(0);
    			getMailDataAndSend(rpaReqForMail, rpaReqForMail.getCreatedBy(), "DEPLOYED");

    			res.setApiSuccess(true);
                res.setResponseMsg("BOT Deployed successfully.");
              
                LOG.info("BOT deploy Request: SUCCESS");
			} else {
            	res.setApiSuccess(false);
                res.setResponseMsg("Can not be Deploy now as Approved is not done yet.");
    			LOG.info("Can not be Deploy now as Approved is not done yet");
    		}

			
		} catch (Exception e) {
			LOG.info("Error while Deploying BOT:" + e.getMessage());
			e.printStackTrace();
        	res.setApiSuccess(false);
			res.setResponseMsg("Error while Deploying BOT:: " + e.getMessage());
		}
    	return res;
    }


	private byte[] downloadSuperBOT(String fileName) throws URISyntaxException, StorageException, IOException{
//		Response<Map<String,URI>> res = externalInterfaceManagmentService.DownloadFileFromContainer(fileName,Integer.parseInt(rpaReqId));
		
		return externalInterfaceManagmentService.downloadSuperBOT(fileName);
		
	}

	// Used for changing Deployed BOT status
	@Transactional("txManager")
	public RpaApiResponse changeDeployedBotStatus(int botId, String userSignum, int status) {
		RpaApiResponse res = new RpaApiResponse();
		try {
			TblRpaDeployedBot depBot = botStoreDAO.getBotDetailById(botId);

			if (null != depBot) {
    			depBot.setIsActive(status);
    			depBot.setModifiedOn(new Date());
    			depBot.setModifiedBy(userSignum);

    			botStoreDAO.createRpaDeployBot(depBot);
            	res.setApiSuccess(true);
            	res.setResponseMsg("BOT Status changed Successfully.");
			} else {
    			LOG.info("Bot not found.");
            	res.setApiSuccess(false);
            	res.setResponseMsg("Bot not found as deployed in database.");
    		}
		} catch (Exception e) {
			LOG.info("Error while changing BOT status: " + e.getMessage());
			e.printStackTrace();
        	res.setApiSuccess(false);
			res.setResponseMsg("Error in changing BOT status:: " + e.getStackTrace());
		}
    	return res;
    }

	// Used for AUDIT, UAT
    @Transactional("txManager")
	public RpaApiResponse updateBotTestResultsForAuditUAT(int reqId, String userSignum, String status) {
		RpaApiResponse res = new RpaApiResponse();
		try {
			TblRpaBotstaging stageBotToUpdate = null;
			TblRpaBotstaging stageBotToInsert = null;

			String newStatus = null;
			if (status.equalsIgnoreCase("AUDITPASS") || status.equalsIgnoreCase("AUDITFAIL"))
				newStatus = "AUDIT";
			else if (status.equalsIgnoreCase("UATPASS") || status.equalsIgnoreCase("UATFAIL"))
				newStatus = "UAT";

			List<TblRpaBotstaging> stagBots = botStoreDAO.getRPAStagingByReqAndStatus(reqId, newStatus);
			if (null != stagBots && stagBots.size() > 0) {
				stageBotToUpdate = stagBots.get(0);
				/*    			
    		
    			
    			
				*/
    			stageBotToUpdate.setStatus(status);
				stageBotToUpdate.setStatusDescription(status + " By User:" + userSignum);
    			stageBotToUpdate.setModifiedOn(new Date());
    			stageBotToUpdate.setModifiedBy(userSignum);

    			botStoreDAO.createBotStaging(stageBotToUpdate);
			} else {

				stagBots = botStoreDAO.getRPAStagingForAccepted(reqId, DEPLOYED);

				if (null != stagBots && stagBots.size() > 0) {
					stageBotToUpdate = stagBots.get(0);

					stageBotToInsert = new TblRpaBotstaging();
					TblRpaRequest rpaReq = new TblRpaRequest();
            		rpaReq.setRpaRequestId(reqId);

            		stageBotToInsert.setBotid(stageBotToUpdate.getBotid());
            		stageBotToInsert.setTblRpaRequest(rpaReq);
            		stageBotToInsert.setAssignedTo(userSignum);
            		stageBotToInsert.setBotname(stageBotToUpdate.getBotname());
            		stageBotToInsert.setRpaexecutionTime(stageBotToUpdate.getRpaexecutionTime());
            		stageBotToInsert.setBotlanguage(stageBotToUpdate.getBotlanguage());
            		stageBotToInsert.setTargetExecutionFileName(stageBotToUpdate.getTargetExecutionFileName());
            		stageBotToInsert.setModuleClassName(stageBotToUpdate.getModuleClassName());
            		stageBotToInsert.setModuleClassMethod(stageBotToUpdate.getModuleClassMethod());
            		stageBotToInsert.setParallelWoexecution(stageBotToUpdate.getParallelWoexecution());
            		stageBotToInsert.setReuseFactor(stageBotToUpdate.getReuseFactor());
            		stageBotToInsert.setLineOfCode(stageBotToUpdate.getLineOfCode());
            		stageBotToInsert.setStatus(status);
					stageBotToInsert.setStatusDescription(status + " By User:" + userSignum);
            		stageBotToInsert.setCreatedOn(new Date());
            		stageBotToInsert.setCreatedBy(userSignum);
            		stageBotToInsert.setIsActive(1);

        			botStoreDAO.createBotStaging(stageBotToInsert);
				} else {
                	res.setApiSuccess(false);
                    res.setResponseMsg("No Deployed Bot found for Auditing for this request.");
        		}
    		}
        	res.setApiSuccess(true);
        	res.setResponseMsg("BOT Status changed Successfully.");

			/*
			 * tblRpaBotstaging.setCreatedOn(new Date());
			 * botStoreDAO.createBotStaging(tblRpaBotstaging);
			 */
		} catch (Exception e) {
			LOG.info("Error while Creating BOT Stage:" + e.getMessage());
			e.printStackTrace();
        	res.setApiSuccess(false);
			res.setResponseMsg("Error in changing BOT status:: " + e.getStackTrace());
		}

    	return res;
    }

	// Used for AUDIT, UAT
	public GetBotStageDetailsForTestingDTO getBotStageDetailsForTesting(Integer reqId, String status) {
		GetBotStageDetailsForTestingDTO stagForClient = new GetBotStageDetailsForTestingDTO();

		try {
			List<TblRpaBotstaging> stagBots = botStoreDAO.getRPAStagingByReqAndStatus(reqId, status);
			if (null != stagBots && stagBots.size() > 0) {
				stagForClient.setTblRpaBotstaging(stagBots.get(0));
				if(stagBots.get(0).getLanguageBaseVersionID()!=null) {
					stagForClient.setLanguageBaseVersion(botStoreDAO.getLanguageBaseByVersionID(stagBots.get(0).getLanguageBaseVersionID()).getLanguageBaseVersion());
				}
				return stagForClient;
    		}
		} catch (Exception e) {
			LOG.info("Error in getBotStageDetailsForTesting:: " + e.getMessage());
			e.printStackTrace();
		}

    	return stagForClient;
    }
    
    public void multipartFileToFile(MultipartFile multipart, Path dir) throws IOException {
    	    File filepath = new File("");
    	    multipart.transferTo(filepath);
    	}
    


	@Transactional("txManager")
	public RpaApiResponse submitBotWithLanguageForJavaPython(TblRpaBotstaging tblRpaBotstaging, MultipartFile codefile,
			MultipartFile exeFile, MultipartFile infile, boolean isInputRequired , MultipartFile whlfile) {
		RpaApiResponse res = new RpaApiResponse();
		String jarOrPythonName = null;
		try {

			TblRpaBotstaging stageBotToUpdate = null;

			TblRpaBotstaging stageAssignedBotToUpdate = null;
			List<TblRpaBotstaging> stagAssigenedBots = botStoreDAO.getRPAStagingForAccepted(tblRpaBotstaging.getTblRpaRequest().getRpaRequestId(), ASSIGNED);
			if (null != stagAssigenedBots && !stagAssigenedBots.isEmpty()) {
				stageAssignedBotToUpdate = stagAssigenedBots.get(0);
				//changes done here
				//stageAssignedBotToUpdate.setLanguageBaseVersionID(tblRpaBotstaging.getLanguageBaseVersionID());
				stageAssignedBotToUpdate.setLanguageBaseVersionID(tblRpaBotstaging.getLanguageBaseVersionID());
    		}

			// Getting target file name from zip file.
			String javaPythonFileName = null;
			if (tblRpaBotstaging.getBotlanguage().toUpperCase().equalsIgnoreCase("PYTHON")) {
				javaPythonFileName = whlfile.getOriginalFilename();
			}
			else {
				javaPythonFileName = BotStoreUtil.getFileNameFromZip(exeFile);
			}
			
			// Insert input file
			TblRpaRequest tblRpaRequest = botStoreDAO
					.getRPARequestDetails(tblRpaBotstaging.getTblRpaRequest().getRpaRequestId());
			tblRpaRequest.setIsInputRequired(isInputRequired);
			tblRpaBotstaging.setTblRpaRequest(tblRpaRequest);
			botStoreDAO.createRpaRequest(tblRpaBotstaging.getTblRpaRequest());
			if (tblRpaBotstaging.getTblRpaRequest().getIsInputRequired()) {
				externalInterfaceManagmentService.uploadFileInContainer(infile, 
						AppConstants.BOT_CONF_FILES+"/"+tblRpaRequest.getRpaRequestId() + INPUT);
				
			}

			List<TblRpaBotstaging> stagBots = botStoreDAO.getRPAStagingForAccepted(
					tblRpaBotstaging.getTblRpaRequest().getRpaRequestId(), tblRpaBotstaging.getAssignedTo(),
					ACCEPTED);
			if (null != stagBots && stagBots.size() > 0) {
				stageBotToUpdate = stagBots.get(0);

    			stageBotToUpdate.setBotname(tblRpaBotstaging.getBotname());
    			stageBotToUpdate.setRpaexecutionTime(tblRpaBotstaging.getRpaexecutionTime());
    			stageBotToUpdate.setBotlanguage(tblRpaBotstaging.getBotlanguage());
    			stageBotToUpdate.setTargetExecutionFileName(tblRpaBotstaging.getTargetExecutionFileName());
    			stageBotToUpdate.setModuleClassName(tblRpaBotstaging.getModuleClassName());
    			stageBotToUpdate.setModuleClassMethod(tblRpaBotstaging.getModuleClassMethod());
    			
				if (tblRpaBotstaging.getParallelWoexecution() != null)
    				stageBotToUpdate.setParallelWoexecution(tblRpaBotstaging.getParallelWoexecution());
    			else
    				stageBotToUpdate.setParallelWoexecution("NO");

    			stageBotToUpdate.setParallelWoexecution(tblRpaBotstaging.getParallelWoexecution());
    			stageBotToUpdate.setReuseFactor(tblRpaBotstaging.getReuseFactor());
    			stageBotToUpdate.setLineOfCode(tblRpaBotstaging.getLineOfCode());
				stageBotToUpdate.setStatusDescription("Edited By Developer:" + tblRpaBotstaging.getCreatedBy());
    			stageBotToUpdate.setModifiedOn(new Date());
    			stageBotToUpdate.setModifiedBy(tblRpaBotstaging.getCreatedBy());
    			stageBotToUpdate.setTargetExecutionFileName(javaPythonFileName);
    			//changes
    			stageAssignedBotToUpdate.setLanguageBaseVersionID(tblRpaBotstaging.getLanguageBaseVersionID());	

    			botStoreDAO.createBotStaging(stageBotToUpdate);

    			externalInterfaceManagmentService.uploadFileInContainer(codefile, 
    					AppConstants.BOT_CONF_FILES+"/"+tblRpaRequest.getRpaRequestId() + "/code/");

    			externalInterfaceManagmentService.uploadFileInContainer(exeFile, 
    					AppConstants.BOT_CONF_FILES+"/"+tblRpaRequest.getRpaRequestId() + "/exe/");
			} 
			else {
    			tblRpaBotstaging.setBotid(tblRpaBotstaging.getTblRpaRequest().getRpaRequestId());
				if (null != stageAssignedBotToUpdate)
        			tblRpaBotstaging.setBotname(stageAssignedBotToUpdate.getBotname());
    			tblRpaBotstaging.setCreatedOn(new Date());
    			tblRpaBotstaging.setStatusDescription(tblRpaBotstaging.getStatus());
    			tblRpaBotstaging.setTargetExecutionFileName(javaPythonFileName);

				if (tblRpaBotstaging.getParallelWoexecution() != null)
    				tblRpaBotstaging.setParallelWoexecution(tblRpaBotstaging.getParallelWoexecution());
    			else
    				tblRpaBotstaging.setParallelWoexecution("NO");

    			tblRpaBotstaging.setIsActive(1);

    			botStoreDAO.createBotStaging(tblRpaBotstaging);

				// Upload Code zip file
    			externalInterfaceManagmentService.uploadFileInContainer(codefile, 
    					AppConstants.BOT_CONF_FILES+"/"+tblRpaRequest.getRpaRequestId() + "/code/");
    			
//				// Upload Exe zip file
//    			externalInterfaceManagmentService.uploadFileInContainer(exeFile, 
//    					AppConstants.BOT_CONF_FILES+"/"+tblRpaRequest.getRpaRequestId() + "/exe/");
    			
				TblRpaRequest rpaReqForMail = botStoreDAO
						.getRPARequestById(tblRpaBotstaging.getTblRpaRequest().getRpaRequestId()).get(0);
    			getMailDataAndSend(rpaReqForMail, tblRpaBotstaging.getCreatedBy(), ACCEPTED);
    		}

			if (null != stageAssignedBotToUpdate) {
				stagBots = botStoreDAO.getRPAStagingForAccepted(tblRpaBotstaging.getTblRpaRequest().getRpaRequestId(),
						tblRpaBotstaging.getAssignedTo(), ACCEPTED);
				stageBotToUpdate = stagBots.get(0);

    			stageAssignedBotToUpdate.setBotlanguage(stageBotToUpdate.getBotlanguage());
    			stageAssignedBotToUpdate.setRpaexecutionTime(stageBotToUpdate.getRpaexecutionTime());
    			stageAssignedBotToUpdate.setTargetExecutionFileName(stageBotToUpdate.getTargetExecutionFileName());
    			stageAssignedBotToUpdate.setModuleClassName(stageBotToUpdate.getModuleClassName());
    			stageAssignedBotToUpdate.setModuleClassMethod(stageBotToUpdate.getModuleClassMethod());
    			stageAssignedBotToUpdate.setParallelWoexecution(stageBotToUpdate.getParallelWoexecution());

    			botStoreDAO.createBotStaging(stageAssignedBotToUpdate);
			}

			if (tblRpaBotstaging.getBotlanguage().equalsIgnoreCase("Java")) {
				// unzip Zip file in
				externalInterfaceManagmentService.uploadFileInContainer(exeFile, 
    					AppConstants.BOT_CONF_FILES+"/"+tblRpaRequest.getRpaRequestId() + "/exe/");
				//changes done here also 
				if (exeFile != null  && BotStoreUtil.unzipFileFromZipinBLOB(exeFile) != null) {
					byte[] bytes = BotStoreUtil.unzipFileFromZipinBLOB(exeFile);
					String jarFileName = tblRpaBotstaging.getTblRpaRequest().getRpaRequestId() + ".jar";
					
					externalInterfaceManagmentService.uploadFileInContainerFromByteArray(bytes,
							AppConstants.BOT_CONF_FILES+"/"+tblRpaRequest.getRpaRequestId() + "/bot/", jarFileName);
				} else {
                	res.setApiSuccess(false);
                    res.setResponseMsg(AppConstants.EXE_FILE_ERROR);
                    throw new Exception();
        		}
			}
			
			//for whl python file 
			else if (tblRpaBotstaging.getBotlanguage().toUpperCase().equalsIgnoreCase("PYTHON")) {
				if (null != whlfile) {
					jarOrPythonName = whlfile.getOriginalFilename(); 
					if(whlfile !=null && jarOrPythonName.endsWith(".whl")) {
						externalInterfaceManagmentService.uploadFileInContainerFromByteArray(whlfile.getBytes(), 
	    						AppConstants.BOT_CONF_FILES+"/"+tblRpaRequest.getRpaRequestId() + "/bot/", jarOrPythonName);															
					}
				}
				 else {
                    	res.setApiSuccess(false);
                        res.setResponseMsg(AppConstants.WHL_ERROR);
                        throw new Exception();
            		}
			}
			
			else if (tblRpaBotstaging.getBotlanguage().equalsIgnoreCase("DotNet")) {
				// unzip Zip file in
				if (BotStoreUtil.unzipFileFromZipinBLOB(exeFile) != null) {
					// Upload exe unzip file from local folder:
					byte[] bytes = BotStoreUtil.unzipFileFromZipinBLOB(exeFile);
					String jarFileName = tblRpaBotstaging.getTblRpaRequest().getRpaRequestId() + ".exe";
					
					externalInterfaceManagmentService.uploadFileInContainerFromByteArray(bytes,
							AppConstants.BOT_CONF_FILES+"/"+tblRpaRequest.getRpaRequestId() + "/bot/", jarFileName);
				} else {
                	res.setApiSuccess(false);
                    res.setResponseMsg(AppConstants.EXE_FILE_ERROR);
                    throw new Exception();
        		}
        	}

			// save botconfig json
        	BotConfig botConfig = new BotConfig();
        	botConfig.setJson(tblRpaBotstaging.getBotConfigJson());
			botConfig.setReferenceId(tblRpaBotstaging.getBotid() + "");
        	botConfig.setType(AppConstants.BOT_CONFIG_TYPE_BOT);
         	rpaService.saveBotConfig(botConfig);

        	res.setApiSuccess(true);
        	res.setResponseMsg(AppConstants.REQUEST_SUCCESSFUL);
		} catch (Exception e) {
			LOG.info(AppConstants.ERROR_BOTSTAGE , e.getMessage());
			e.printStackTrace();
        	res.setApiSuccess(false);
			res.setResponseMsg(AppConstants.ERROR_SUBMITTING_BOT + e.getMessage());
		}

    	return res;
    }

	// Used for delete new request and new BOT request
    @Transactional("txManager")
	public RpaApiResponse deleteAutomationRequest(int reqId, String userSignum) {
		RpaApiResponse res = new RpaApiResponse();

		try {
			TblRpaRequest botReq = botStoreDAO.getRPARequestDetails(reqId);
			boolean f = false;

			if (null != botReq) {
				/*
				 * List<TblRpaBotFile> list = botStoreDAO.getRPAFileDetails(reqId); if
				 * (list.size() > 0) { for (TblRpaBotFile s : list) { s.setIsActive(0);
				 * s.setModifiedBy(userSignum); s.setModifiedOn(new Date());
				 * botStoreDAO.createUpdateRpaFile(s); }
				 * 
				 * }
				 */
				for (TblRpaBotstaging s : botReq.getTblRpaBotstagings()) {
					if (s.getStatus().equalsIgnoreCase(DEPLOYED) || s.getStatus().equalsIgnoreCase(APPROVED))
						f = true;
				}

				if (f) {
    				LOG.info("Approved/Deployed BOT can not deleted..");
				} else {
        			botReq.setIsActive(0);
        			botReq.setModifiedBy(userSignum);
        			botReq.setModifiedOn(new Date());

        			botStoreDAO.createRpaRequest(botReq);

					for (TblRpaBotrequirement r : botReq.getTblRpaBotrequirements()) {
        				r.setIsActive(0);
            			r.setModifiedBy(userSignum);
            			r.setModifiedOn(new Date());

            			botStoreDAO.createRpaBotrequirement(r);

						for (TblRpaRequestTool t : r.getTblRpaRequestTools()) {
            				t.setIsActive(0);
                			t.setModifiedBy(userSignum);
                			t.setModifiedOn(new Date());

                			botStoreDAO.createRpaRequestTool(t);
        			}
					}

					for (TblRpaBotstaging s : botReq.getTblRpaBotstagings()) {
        				s.setIsActive(0);
            			s.setModifiedBy(userSignum);
            			s.setModifiedOn(new Date());

            			botStoreDAO.createBotStaging(s);
        			}
					for (TblRpaBottesting t : botReq.getTblRpaBottesting()) {
            			t.setStatus("DELETED");
            			botStoreDAO.createBotTestingRequest(t);
        			}
    			}
				LOG.info("Delete Request Successfully. Request id is : " + reqId);
            	res.setApiSuccess(true);
            	res.setResponseMsg("Delete Request Successfully.");
			} else {
    			LOG.info("Bot Request not found.");
    		}
		} catch (Exception e) {
			LOG.info("Error in delting RPA request: " + e.getMessage());
			e.printStackTrace();
        	res.setApiSuccess(false);
			res.setResponseMsg("Error in delting RPA request:: " + e.getMessage());
		}

    	return res;
    }
    @Transactional("txManager")
	public ResponseEntity<RpaApiResponse> stopInprogressBot(Integer rpaRequestId, String  signum) {
    	RpaApiResponse res = new RpaApiResponse();
    	try {
    		int i=botStoreDAO.stopInprogressBot(rpaRequestId, signum, AppConstants.CHAR_ONE, AppConstants.COMPLETED);
    		if(i==0) {
    			LOG.info(String.format(NO_DATA_MSG, AppConstants.INPROGRESS_BOT));
            	res.setApiSuccess(true);
            	res.setResponseMsg(String.format(NO_DATA_MSG, AppConstants.INPROGRESS_BOT)+rpaRequestId);
    		}else if(i==1) {
    			LOG.info(String.format(SUCCESS_MSG, AppConstants.INPROGRESS_BOT));
            	res.setApiSuccess(true);
            	//res.setResponseMsg(String.format(SUCCESS_MSG, AppConstants.INPROGRESS_BOT));
            	res.setResponseMsg(SUCCESS_MSG_RESPONSE);
    		}
    	}catch (Exception e) {
    		LOG.info(String.format(ERROR_MSG, AppConstants.INPROGRESS_BOT)+ e.getMessage());
    		res.setApiSuccess(false);
    		res.setResponseMsg(String.format(ERROR_MSG, AppConstants.INPROGRESS_BOT)+ e.getMessage());
    		return new ResponseEntity<RpaApiResponse>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<RpaApiResponse>(res, HttpStatus.OK);
	}

	private static String BOT_TEST_ID = "BOT_TEST_ID";

    @Transactional("txManager")
	public RpaApiResponse createBotTestingRequest(Integer reqId, String userSignum) {
		LOG.info("In createBotTestingRequest(Integer reqId, String userSignum), for reqId-->" + reqId
				+ " and userSignum-->" + userSignum);

		RpaApiResponse res = new RpaApiResponse();

		try {
    		LOG.info("In createBotTestingRequest");
			TblRpaBottesting oldBotTest = botStoreDAO.getTestingDataByReq(reqId, userSignum);
			if (oldBotTest != null && oldBotTest.getStatus().equalsIgnoreCase("INPROGRESS")) {
				LOG.info("Bot Testing is already started and is in In-Progress stage for request id-->" + reqId);
            	res.setApiSuccess(false);
				res.setResponseMsg(
						"Bot Testing is already started and is in In-Progress stage. Please ensure your ISF App is up and running.");
			} else {
				TblRpaBottesting botTest = new TblRpaBottesting();

				TblRpaRequest req = botStoreDAO.getRPARequestDetails(reqId);

        		botTest.setTblRpaRequest(req);
        		botTest.setStatus("INPROGRESS");
        		botTest.setIsTestingSuccessful(0);
        		botTest.setCreatedOn(new Date());
        		botTest.setCreatedBy(userSignum);

        		botStoreDAO.createBotTestingRequest(botTest);

				Map<String, String> data = new HashMap<>();
				data.put(BOT_TEST_ID, botTest.getTestId() + "");
        		res.setDataMap(data);
            	res.setApiSuccess(true);
            	res.setResponseMsg("RPA BOT testing request Created Successfully.");
    		}
		} catch (Exception e) {
			LOG.info("Error while Creating BOT Testing request: " + e.getMessage());
        	res.setApiSuccess(false);
			res.setResponseMsg("Error while Creating BOT Testing request: " + e.getMessage());
			e.printStackTrace();
		}

    	return res;
    }

	public TblRpaBottesting getTestingDataByReq(Integer reqId, String userSignum) {
		TblRpaBottesting botTestForClient = null;
		try {
			TblRpaBottesting botTest = botStoreDAO.getTestingDataByReq(reqId, userSignum);

			botTestForClient = new TblRpaBottesting();

    		botTestForClient.setTestId(botTest.getTestId());
    		botTestForClient.setIsTestingSuccessful(botTest.getIsTestingSuccessful());
    		botTestForClient.setStatus(botTest.getStatus());
    		botTestForClient.setCreatedBy(botTest.getCreatedBy());
    		botTestForClient.setCreatedOn(botTest.getCreatedOn());

		} catch (Exception e) {
			LOG.info("Error while Creating BOT Testing request: " + e.getMessage());
			e.printStackTrace();
		}

    	return botTestForClient;
    }

    @Transactional("txManager")
	public RpaApiResponse updateBotTestingResults(Integer testId, Integer isTestingSuccessful) {
		RpaApiResponse res = new RpaApiResponse();

		try {
			// TblRpaBottesting botTestForClient= new TblRpaBottesting();

			TblRpaBottesting botTest = botStoreDAO.getTestingDataById(testId);

    		botTest.setIsTestingSuccessful(isTestingSuccessful);
    		botTest.setStatus("COMPLETED");

    		botStoreDAO.createBotTestingRequest(botTest);

        	res.setApiSuccess(true);
        	res.setResponseMsg("RPA BOT testing request updated Successfully.");
		} catch (Exception e) {
			LOG.info("Error while updating BOT Testing request: " + e.getMessage());
        	res.setApiSuccess(false);
			res.setResponseMsg("Error while updating BOT Testing request: " + e.getMessage());
			e.printStackTrace();
		}

    	return res;
    }

	private void botStoreFileUpload_Code(int requestID, MultipartFile file) {
		try {
          String fileName = file.getOriginalFilename();

			String relativeInPath = BotStoreUtil.getRelativeFilePathUploadCode(requestID);

          botService.uploadFile(file, relativeInPath, fileName);
           LOG.info("Code File Uploaded Successfully.");
		} catch (Exception e) {
          e.printStackTrace();
			LOG.info("Code File Uploaded Failed::" + e.getMessage());
			// throw new ApplicationException(500,"Error while uploading code file:" +
			// e.getMessage());
       }
    }

	private void botStoreFileUpload_Exe(int requestID, MultipartFile file) {
		try {
          String fileName = file.getOriginalFilename();

			String relativeInPath = BotStoreUtil.getRelativeFilePathUploadExe(requestID);

           botService.uploadFile(file, relativeInPath, fileName);
           LOG.info("Code File Uploaded Successfully.");
		} catch (Exception e) {
          e.printStackTrace();
			LOG.info("Code File Uploaded Failed::" + e.getMessage());
			// throw new ApplicationException(500,"Error while uploading code file:" +
			// e.getMessage());
       }
    }

	private void botStoreFileUpload_Logic(int requestID, MultipartFile file) {
		try {
              String fileName = file.getOriginalFilename();

			String relativeInPath = BotStoreUtil.getRelativeFilePathUploadLogic(requestID);

              botService.uploadFile(file, relativeInPath, fileName);
           LOG.info("Logic File Uploaded Successfully.");
		} catch (Exception e) {
              e.printStackTrace();
			LOG.info("Code File Uploaded Failed::" + e.getMessage());
			// throw new ApplicationException(500,"Error while uploading logic file:" +
			// e.getMessage());
       }
    }

	private void botStoreFileUpload_Output(int requestID, MultipartFile file) {
		try {
              String fileName = file.getOriginalFilename();

			String relativeInPath = BotStoreUtil.getRelativeFilePathUploadOutput(requestID);

              botService.uploadFile(file, relativeInPath, fileName);
           LOG.info("Output File Uploaded Successfully.");
		} catch (Exception e) {
              e.printStackTrace();
			LOG.info("Code File Uploaded Failed::" + e.getMessage());
			// throw new ApplicationException(500,"Error while uploading output file:" +
			// e.getMessage());
       }
    }

	private void botStoreFileUpload_Input(int requestID, MultipartFile file) {
		try {
              String fileName = file.getOriginalFilename();

              BotStoreUtil btU = new BotStoreUtil();
			String relativeInPath = BotStoreUtil.getRelativeFilePathUploadInput(requestID);

//           //Convert File into Multipart
              botService.uploadFile(file, relativeInPath, fileName);
           LOG.info("Input File Uploaded Successfully.");
		} catch (Exception e) {
              e.printStackTrace();
			LOG.info("Code File Uploaded Failed::" + e.getMessage());
			// throw new ApplicationException(500,"Error while uploading input file:" +
			// e.getMessage());
       }
    }

    @Transactional("txManager")
	public void downloadFileAsStream(HttpServletResponse response, Integer reqId, String fileType) {
		RpaApiResponse res = new RpaApiResponse();
		String fileName = "abc.zip";
		ZipOutputStream zipOutputStream =null;
		File file =null;
		FileInputStream fileInputStream =null;
		try {
    		response.setContentType("application/zip");
			response.setHeader("Content-type", "application/octet-stream");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileType + ".zip");
			// writeWorkbookToResponse(response,
			// generateXlsFile(result),reportDetials.getReportName()+"-"+ (new
			// SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date())) +".xlsx");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

    		zipOutputStream = new ZipOutputStream(response.getOutputStream());

			String filePath = BotStoreUtil.getAbsFileNameInDir(
					configurations.getStringProperty(ConfigurationFilesConstants.BOTSTORE_UPLOAD_PATH) + reqId
							+ File.separator + "input");
			file = new File(filePath);

    		zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
    		fileInputStream = new FileInputStream(file);
            IOUtils.copy(fileInputStream, zipOutputStream);

            res.setApiSuccess(true);
        	res.setResponseMsg("Request flow done successfully");
		} catch (Exception e) {
			LOG.info("Error in creating BOT stage entry:" + e.getMessage());
			e.printStackTrace();
        	res.setApiSuccess(false);
			res.setResponseMsg("Error in creating BOT stage entry:: " + e.getMessage());
		}finally {
			try {
				fileInputStream.close();
	            zipOutputStream.closeEntry();
                zipOutputStream.close();
			} catch (Exception e2) {
				LOG.info("Error in creating BOT stage entry:" + e2.getMessage());
			}
		}
		// return res;
    }

	/*
	 * @Transactional("txManager") public void
	 * downloadFileAsStream(HttpServletResponse response, Integer reqId, String
	 * fileType) { RpaApiResponse res= new RpaApiResponse(); String fileName=
	 * "abc.zip"; try { response.setContentType("application/zip");
	 * response.setHeader("Content-type","application/octet-stream");
	 * response.setHeader("Content-Disposition","attachment;filename=" +
	 * fileType+".zip"); //writeWorkbookToResponse(response,
	 * generateXlsFile(result),reportDetials.getReportName()+"-"+ (new
	 * SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date())) +".xlsx");
	 * response.setHeader("Content-Disposition","attachment;filename=" + fileName);
	 * 
	 * String filePath=
	 * BotStoreUtil.getAbsFileNameInDir(environment.getRequiredProperty(
	 * "botstore.upload.path") + reqId + File.separator + "input"); File f= new
	 * File(filePath);
	 * 
	 * OutputStream out = response.getOutputStream(); wbook.write(out); out.flush();
	 * out.close();
	 * 
	 * res.setApiSuccess(true);
	 * res.setResponseMsg("Request flow done successfully"); } catch (Exception e) {
	 * LOG.info("Error in creating BOT stage entry:"+e.getMessage());
	 * e.printStackTrace(); res.setApiSuccess(false);
	 * res.setResponseMsg("Error in creating BOT stage entry:: "+e.getMessage()); }
	 * //return res; }
	 */

	public String downloadFile(Integer reqId, String fileType) {
		LOG.info("FileType for downloadFile: " + fileType);

		String filePath = null;

		try {
			if (fileType.equalsIgnoreCase("input")) {
				filePath = configurations.getStringProperty(ConfigurationFilesConstants.BOTSTORE_UPLOAD_BASEFOLDER)
						+ File.separator + reqId + File.separator + "input" + File.separator + "input.zip";
			} else if (fileType.equalsIgnoreCase("output")) {
				filePath = configurations.getStringProperty(ConfigurationFilesConstants.BOTSTORE_UPLOAD_BASEFOLDER)
						+ File.separator + reqId + File.separator + "output" + File.separator + "output.zip";
			} else if (fileType.equalsIgnoreCase("logic")) {
				filePath = configurations.getStringProperty(ConfigurationFilesConstants.BOTSTORE_UPLOAD_BASEFOLDER)
						+ File.separator + reqId + File.separator + "logic" + File.separator + "logic.zip";
			} else if (fileType.equalsIgnoreCase("code")) {
				filePath = configurations.getStringProperty(ConfigurationFilesConstants.BOTSTORE_UPLOAD_BASEFOLDER)
						+ File.separator + reqId + File.separator + "code" + File.separator + "code.zip";
			} else if (fileType.equalsIgnoreCase("exe")) {
				filePath = configurations.getStringProperty(ConfigurationFilesConstants.BOTSTORE_UPLOAD_BASEFOLDER)
						+ File.separator + reqId + File.separator + "exe" + File.separator + "exe.zip";
    		}
		} catch (Exception e) {
			LOG.info("Error in downloadFile:" + e.getMessage());
			e.printStackTrace();
		}
    	return filePath;
    }

	public void downloadBotAsStream(HttpServletResponse response, Integer reqId) {
		LOG.info("downloadBotAsStream(): " + reqId);

		InputStream fis = null;

		try {
			// fis=
			// AppUtil.downloadFileAsStream(environment.getRequiredProperty("botstore.upload.basefolder")
			// + File.separator + "DeployedBots" + File.separator + reqId+".jar");

		} catch (Exception e) {
			LOG.info("Error in downloadFile:" + e.getMessage());
			e.printStackTrace();
		}
    }

    public List<Map<String, Object>> getBotCurrentStatusForApproval(List<Map<String, Object>> data) {
		List<Map<String, Object>> dataNew = new ArrayList<Map<String, Object>>();
		Map<String, Object> mapNew = null;

		List<TblRpaBotstaging> stagBots = null;
		String botCurrStatus = null;
		for (Map<String, Object> m : data) {
			mapNew = new HashMap<String, Object>();
			for (String k : m.keySet()) {
				// System.out.println("11---->"+k+"----"+m.get(k).toString());

				if (null != m.get("rpaRequestID")) {
					stagBots = botStoreDAO.getStagingBotsByReqId((Integer) m.get("rpaRequestID"));
					if (stagBots != null && stagBots.size() > 0)
						botCurrStatus = getBotCurrStatus(stagBots);
	    			else
						botCurrStatus = "NA";

				}
				mapNew.put(k, m.get(k));
			}
			mapNew.put("BotCurrentStatus", botCurrStatus);
			dataNew.add(mapNew);
		}

		return dataNew;
    }


	/*
	 * public void disableBotDetailById(int botId) {
	 * 
	 * TblRpaDeployedBot bot= botStoreDAO.getBotDetailById(botId); if (null == bot)
	 * { throw new ApplicationException(500,
	 * "No details exists for the Request!!!"); } else {
	 * 
	 * 
	 * return bot; } }
	 */

	public Map<String, Object> enrichMailforBOTrequestCreation(AspLoginModel req, String pwd) {
		Map<String, Object> data = new HashMap<String, Object>();
    	data.put("request", req);
    	data.put("pwd", pwd);
    	data.put(AppConstants.CUSTOM_NOTIFICATIONS_TO, req.getEmail());
    	return data;

    }

    @Transactional("txManager")
	public RpaApiResponse changeRunOnServerStatus(int botId, String userSignum, int status) {
		RpaApiResponse res = new RpaApiResponse();
		try {
			TblRpaDeployedBot depBot = botStoreDAO.getBotDetailById(botId);

			if (null != depBot) {
    			depBot.setIsRunOnServer(status);
    			depBot.setModifiedOn(new Date());
    			depBot.setModifiedBy(userSignum);

    			botStoreDAO.createRpaDeployBot(depBot);
            	res.setApiSuccess(true);
            	res.setResponseMsg("BOT Status changed Successfully.");
			} else {
    			LOG.info("Bot not found.");
            	res.setApiSuccess(false);
            	res.setResponseMsg("Bot not found as deployed in database.");
    		}
		} catch (Exception e) {
			LOG.info("Error while changing BOT status: " + e.getMessage());
			e.printStackTrace();
        	res.setApiSuccess(false);
			res.setResponseMsg("Error in changing BOT status:: " + e.getStackTrace());
		}
    	return res;
    }

	@Transactional("txManager")
	public RpaApiResponse changeAuditPassFail(int botId, String userSignum, int status) {
		RpaApiResponse res = new RpaApiResponse();
		try {
			TblRpaDeployedBot depBot = botStoreDAO.getBotDetailById(botId);

			if (null != depBot) {
    			depBot.setIsAuditPass(status);
    			depBot.setModifiedOn(new Date());
    			depBot.setModifiedBy(userSignum);

    			botStoreDAO.createRpaDeployBot(depBot);
            	res.setApiSuccess(true);
            	res.setResponseMsg("BOT Status changed Successfully.");
			} else {
    			LOG.info("Bot not found.");
            	res.setApiSuccess(false);
            	res.setResponseMsg("Bot not found as deployed in database.");
    		}
		} catch (Exception e) {
			LOG.info("Error while changing BOT status: " + e.getMessage());
			e.printStackTrace();
        	res.setApiSuccess(false);
			res.setResponseMsg("Error in changing BOT status:: " + e.getStackTrace());
		}
    	return res;
    }

	public Map<String, String> getTestingDataByStatusSignum(String status, String userSignum) {

		Map<String, String> data = new HashMap<String, String>();

		try {
			
			HashMap<String, Object> isInstalledDesktopVersionUpdated = wOExecutionDAO
					.isInstalledDesktopVersionUpdated(userSignum);
			
			if ((Integer) isInstalledDesktopVersionUpdated.get("result") == 0) {
				return data;
			}
			if(!wOExecutionDAO.isInstalledLibraryVersionUpdated(userSignum)) {
				return data;
			}
			List<TblRpaBottesting> list = botStoreDAO.getTestingDataByStatusSignum(status, userSignum);
			for (TblRpaBottesting botTest : list) {
				data.put("rpaRequestID", botTest.getTblRpaRequest().getRpaRequestId() + "");
				data.put("isInputRequired", botTest.getTblRpaRequest().getIsInputRequired() + "");
    		}
		} catch (Exception e) {
			LOG.info("Error in getTestingDataByStatusSignum(): " + e.getMessage());
			e.printStackTrace();
			throw new ApplicationException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Getting Error to fetch data." + e.getMessage());
		}

    	return data;
    }

	public Response<List<DesktopAppResponseModel>> getDesktopAppVersionv1() {
		Response<List<DesktopAppResponseModel>> response = new Response<>();
		try {
			response.setResponseData(botStoreDAO.getDesktopAppVersionv1());
		}
		catch(Exception ex) {
			LOG.info(ex.getMessage());
			ex.printStackTrace();
			response.addFormError(ex.getMessage());
		}

    	return response;
    }

    @Transactional("txManager")
	public byte[] botDownload(BotDetail botDetail) {
		// RpaApiResponse res= new RpaApiResponse();
		byte[] botJarFile = null;
		String status = "";
		try {
			LOG.info("botDetail.getBotId()-->" + botDetail.getBotId());
			LOG.info("botDetail.getBotName()-->" + botDetail.getBotName());

			// below code to download file from FTP location-->

			String URL = configurations.getStringProperty(ConfigurationFilesConstants.BOTSTORE_RPA_BOT_DOWNLOAD_FTPURL)
					+ configurations.getStringProperty(ConfigurationFilesConstants.BOTSTORE_RPA_BOT_DOWNLOAD_BOTPATH)
					+ botDetail.getBotId() + ".jar";

			LOG.info("URL for botDownload-->" + URL);

    		List<NameValuePair> params = new ArrayList<NameValuePair>();
    		params.add(new BasicNameValuePair("botName", botDetail.getBotName()));
    		params.add(new BasicNameValuePair("botID", botDetail.getBotId()));

			botJarFile = BotStoreUtil.makeHttpRequestBOTDownload(URL, "GET", params);

			// below code to download file using DB proc-->

			// botJarFile=
			// botStoreDAO.botDownloadUsingProc(environment.getRequiredProperty("botstore.rpa.bot.download.basepath")
			// + botDetail.getBotId()+".jar");

			if (botJarFile.length > 0) {
				// FileUtils.writeByteArrayToFile(new File(botDetail.getBotDownloadLocation() +
				// botDetail.getBotId()+".jar"), botJarFile);
				status = "Success";
			}

			// res.setApiSuccess(true);
			// res.setResponseMsg("BOT Stage created successfully");
		} catch (Exception e) {
			LOG.info("Error in botDownload(): " + e.getMessage());
			e.printStackTrace();
			status = "Fail";
			// res.setApiSuccess(false);
			// res.setResponseMsg("Error in creating BOT stage entry:: "+e.getMessage());
		}

    	return botJarFile;
    }

    @Transactional("txManager")
	public boolean uploadBotOutput(BotDetail botDetail) {
		// RpaApiResponse res= new RpaApiResponse();
		boolean status = false;
		try {
			LOG.info("botDetail.getZipInputFilePath(): " + botDetail.getZipInputFilePath());
			LOG.info("botDetail.getZipFileName(): " + botDetail.getZipFileName());

            String server = configurations.getStringProperty(ConfigurationFilesConstants.BOTSTORE_RPA_UPLOAD_OUTPUT_IP);
            String port = configurations.getStringProperty(ConfigurationFilesConstants.BOTSTORE_RPA_UPLOAD_OUTPUT_PORT);
            String user = configurations.getStringProperty(ConfigurationFilesConstants.BOTSTORE_RPA_UPLOAD_OUTPUT_USER);
            String pass = configurations.getStringProperty(ConfigurationFilesConstants.BOTSTORE_RPA_UPLOAD_OUTPUT_PWD);
			String uploadPath = configurations
					.getStringProperty(ConfigurationFilesConstants.BOTSTORE_RPA_UPLOAD_OUTPUT_PATH);

			if (server.contains("http:")) {
				server = server.replace("http://", "");

				status = BotStoreUtil.uploadFileUsingFTP(server, port, user, pass, uploadPath,
						botDetail.getZipInputFilePath(), botDetail.getZipFileName());
			} else if (server.contains("https:")) {
				server = server.replace("https://", "");
				status = BotStoreUtil.UploadFileUsingSFTP(server, port, user, pass, uploadPath,
						botDetail.getZipInputFilePath(), botDetail.getZipFileName());
            }

			// res.setApiSuccess(true);
			// res.setResponseMsg("BOT Stage created successfully");
		} catch (Exception e) {
			status = false;
			LOG.info("Error in uploadBotOutput(): " + e.getMessage());
			e.printStackTrace();
			// res.setApiSuccess(false);
			// res.setResponseMsg("Error in :: "+e.getMessage());
		}

    	return status;
    }

    @Transactional("txManager")
	public BotDetail botOutputDownload(BotDetail botDetail) {
		// RpaApiResponse res= new RpaApiResponse();
		byte[] botOutputFile = null;
		try {
			LOG.info("botDetail.getZipFileName()-->" + botDetail.getOutputFileName());

			String URL = configurations.getStringProperty(ConfigurationFilesConstants.BOTSTORE_RPA_BOT_DOWNLOAD_FTPURL)
					+ configurations.getStringProperty(ConfigurationFilesConstants.BOTSTORE_RPA_BOT_DOWNLOAD_BOTPATH)
					+ botDetail.getOutputFileName();

			LOG.info("URL for botOutputDownload-->" + URL);

    		List<NameValuePair> params = new ArrayList<NameValuePair>();
    		params.add(new BasicNameValuePair("botName", "dummyBot"));
    		params.add(new BasicNameValuePair("botID", "0"));

			botOutputFile = BotStoreUtil.makeHttpRequestBOTDownload(URL, "GET", params);

			// botOutputFile=
			// botStoreDAO.botDownloadUsingProc(environment.getRequiredProperty("botstore.rpa.bot.botoutput.download.basepath")
			// + botDetail.getOutputFileName());

			if (botOutputFile.length > 0) {
	    		botDetail.setSuccess(true);
	    		botDetail.setResponseMsg("File download success.");
	    		botDetail.setBotOutputFileByteArr(botOutputFile);

				// FileUtils.writeByteArrayToFile(new File("C:\\delete\\test1\\" +
				// botDetail.getOutputFileName()), botOutputFile);
			} else {
	    		botDetail.setSuccess(false);
	    		botDetail.setResponseMsg("Requested file is not available on server.");
			}

			// res.setApiSuccess(true);
			// res.setResponseMsg("BOT Stage created successfully");
		} catch (Exception e) {
			LOG.info("Error in botOutputDownload(): " + e.getMessage());
    		botDetail.setSuccess(false);
    		botDetail.setResponseMsg("Requested file is not available on server.");
			e.printStackTrace();
			// res.setApiSuccess(false);
			// res.setResponseMsg("Error in creating BOT stage entry:: "+e.getMessage());
		}

    	return botDetail;
    }

	public byte[] downloadBotOutputByFileName(String outputFileName) {
		// RpaApiResponse res= new RpaApiResponse();
		byte[] botOutputFile = null;
		try {
			LOG.info("outputFileName-->" + outputFileName);

			String URL = configurations.getStringProperty(ConfigurationFilesConstants.BOTSTORE_RPA_BOT_DOWNLOAD_FTPURL)
					+ configurations.getStringProperty(ConfigurationFilesConstants.BOTSTORE_RPA_BOT_DOWNLOAD_BOTPATH)
					+ outputFileName;

			LOG.info("URL for botOutputDownload-->" + URL);

    		List<NameValuePair> params = new ArrayList<NameValuePair>();
    		params.add(new BasicNameValuePair("botName", "dummyBot"));
    		params.add(new BasicNameValuePair("botID", "0"));

			botOutputFile = BotStoreUtil.makeHttpRequestBOTDownload(URL, "GET", params);

			if (botOutputFile.length > 0) {
				LOG.info("Output File found..");
				// FileUtils.writeByteArrayToFile(new File("C:\\delete\\test1\\" +
				// botDetail.getOutputFileName()), botOutputFile);
			} else {
				botOutputFile = null;
			}
			// res.setApiSuccess(true);
			// res.setResponseMsg("BOT Stage created successfully");
		} catch (Exception e) {
			LOG.info("Error in downloadBotOutputByFileName(): " + e.getMessage());
			botOutputFile = null;
			e.printStackTrace();
			// res.setApiSuccess(false);
			// res.setResponseMsg("Error in creating BOT stage entry:: "+e.getMessage());
		}
    	return botOutputFile;
    }

    @Transactional("txManager")
	public BotDetail botDownloadDB(BotDetail botDetail) {
		try {
			Response<URI> uriResponse= externalInterfaceManagmentService.DownloadFileFromContainer(botDetail.getBotName(),Integer.parseInt(botDetail.getBotId()));

			if (uriResponse.getResponseData()!=null) {
				botDetail.setUri(uriResponse.getResponseData());
				LOG.info("botDownload Request: SUCCESS");
			} else {
				LOG.info("botDownload Request: FAIL -->File Type "+ botDetail.getBotName() + " is not found..");
			}
		} catch (Exception e) {
			LOG.info("Error in botDownload(): " + e.getMessage());
			e.printStackTrace();
		}

    	return botDetail;
    }

    @Transactional("txManager")
	public byte[] botDownloadDBFile(BotDetail botDetail) {
		byte[] botJarFile = null;
		String fileName = "";
		String botId = "";
		try {
			LOG.info("botDetail.getBotId()-->" + botDetail.getBotId());
			LOG.info("botDetail.getBotName()-->" + botDetail.getBotName());

			// below code to download file from DB location-->
			botId = botDetail.getBotId();
			fileName = botDetail.getBotName();

    		List<TblRpaBotFile> list  =  botStoreDAO.getRPAFile(botId, fileName);

			if (null != list && list.size() > 0) {
				botJarFile = list.get(0).getDataFile();
    			long size = botJarFile.length;
				if (botJarFile.length > 0) {
					LOG.info("File Type " + fileName + " is found.. Size: " + (double) size / (1024 * 1024));

//    				String fPATH="C:\\Users\\essrmma\\Desktop\\Rupinder\\1604\\"+fileName;
//    				FileUtils.writeByteArrayToFile(new File(fPATH), botJarFile);
				} else {
					LOG.info("File Type " + fileName + " is not found..");
					botJarFile = null;
    			}
    			}
		} catch (Exception e) {
			LOG.info("Error in botDownload(): " + e.getMessage());
			e.printStackTrace();
		}

    	return botJarFile;
    }

    @Transactional("txManager")
	public byte[] botDownloadDBOUTFile(String OutFileName) {
		byte[] botJarFile = null;
		try {
			LOG.info("botDetail.getBotName()-->" + OutFileName);

			// below code to download file from DB location-->

    		List<TblRpaBotExecutionDetail> list  =  botStoreDAO.getOUTFile(OutFileName);

			if (null != list && list.size() > 0) {
				botJarFile = list.get(0).getOutputDataFile();
    			long size = botJarFile.length;
				if (botJarFile.length > 0) {
					LOG.info("File Type " + OutFileName + " is found.. Size: " + (double) size / (1024 * 1024));

//    				String fPATH="C:\\Users\\essrmma\\Desktop\\Rupinder\\1604\\"+fileName;
//    				FileUtils.writeByteArrayToFile(new File(fPATH), botJarFile);
				} else {
					LOG.info("File Type " + OutFileName + " is not found..");
					botJarFile = null;
    			}
    			}
		} catch (Exception e) {
			LOG.info("Error in botDownload(): " + e.getMessage());
			e.printStackTrace();
		}

    	return botJarFile;
    }

	public void debugApi(int botId) {
    	try {
			TblRpaRequest rpaReqForMail = botStoreDAO.getRPARequestById(botId).get(0);
        	getMailDataAndSend(rpaReqForMail, rpaReqForMail.getCreatedBy(), "CREATAED");
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	private void getMailDataAndSend(TblRpaRequest tblRpaRequest, String actingUserSignum, String currRequestStatus)
			throws Exception {
		// get WFOwner(below query) and SPM (from TBL_PROJECTS.ProjectCreator) signum
		// using acting user signum
		// select WFOwner from transactionalData.TBL_SUBACTIVITY_FLOWCHART_DEF where
		// SubActivityFlowChartDefID=34585

		// getting request data for task name, WorkFlowName, subact name:
		Map<Integer, String[]> dataMap = new HashMap<Integer, String[]>();
		//List<Object[]> taskList = botStoreDAO.getBotStepDetails(tblRpaRequest.getRpaRequestId());
		List<Object[]> taskList = null;
		if(StringUtils.equalsIgnoreCase(currRequestStatus, DEPLOYED)) {
			taskList = botStoreDAO.getBotStepDetailsForDeployedBOT(tblRpaRequest.getRpaRequestId());
		}
		else {
		 taskList = botStoreDAO.getBotStepDetails(tblRpaRequest.getRpaRequestId());
		}

		Object[] taskDataArr = null;
		String projectName = null;
		String workFlowName = null;
		String subActivity = null;
		String taskName = null;
		String stepID = null;
		String stepName = null;
		int wfID =0;
		
		if (taskList != null && taskList.size() > 0) {
			taskDataArr = taskList.get(0);

			if (taskDataArr[1] != null)
				projectName = (String) taskDataArr[1];
			if (taskDataArr[2] != null)
				workFlowName = (String) taskDataArr[2];
			if (taskDataArr[3] != null && taskDataArr[4] != null)
				subActivity = (String) taskDataArr[3] + "-" + (String) taskDataArr[4];
			if (taskDataArr[5] != null)
				taskName = (String) taskDataArr[5];
			
			if (taskDataArr[6] != null)
				stepID = (String) taskDataArr[6];
			
			if (taskDataArr[7] != null)
				stepName = (String) taskDataArr[7];
			
			if (taskDataArr[8] != null)
				wfID = (int) taskDataArr[8];
				
        }

		LOG.info("projectName-->" + projectName);
		LOG.info("workFlowName-->" + workFlowName);
		LOG.info("subActivity-->" + subActivity);
		LOG.info("taskName-->" + taskName);
		LOG.info("stepID-->" + stepID);
		LOG.info("stepName-->" + stepName);
		LOG.info("wfID-->" + wfID);

		// getting users mail ids:
		String wfOwnerSignum = getWfOwnerSignum(tblRpaRequest);
		LOG.info("wfOwnerSignum-->" + wfOwnerSignum);
		String spmSignum = getSpmSignum(tblRpaRequest);
		LOG.info("spmSignum-->" + spmSignum);

		LOG.info("actingUserSignum-->" + actingUserSignum);

		sendBotMail(tblRpaRequest, wfOwnerSignum, spmSignum, actingUserSignum, projectName, workFlowName, subActivity,
				taskName, currRequestStatus, stepID, stepName , wfID);

    }

	private String getWfOwnerSignum(TblRpaRequest tblRpaRequest) throws Exception {
		String wfOwnerSignum = null;

		// get WFOwner(below query) and SPM (from TBL_PROJECTS.ProjectCreator) signum
		// using acting user signum
		// select WFOwner from transactionalData.TBL_SUBACTIVITY_FLOWCHART_DEF where
		// SubActivityFlowChartDefID=34585

		LOG.info("WorkflowDefid in getWfOwnerSignum()------> " + tblRpaRequest.getWorkflowDefid());
		List<Object[]> data = botStoreDAO.getWfOwner(tblRpaRequest.getWorkflowDefid());
		if (data != null && data.size() > 0 && data.get(0) != null && data.get(0).length > 0
				&& (String) data.get(0)[0] != null) {
			wfOwnerSignum = (String) data.get(0)[0];
        }

    	return wfOwnerSignum;
	}

	private String getSpmSignum(TblRpaRequest tblRpaRequest) throws Exception {
		String spmSignum = null;

		// get WFOwner(below query) and SPM (from TBL_PROJECTS.ProjectCreator) signum
		// using acting user signum
		// select WFOwner from transactionalData.TBL_SUBACTIVITY_FLOWCHART_DEF where
		// SubActivityFlowChartDefID=34585

		LOG.info("Project ID in getSpmSignum()------> " + tblRpaRequest.getTblProjects().getProjectId());
		List<Object[]> data = botStoreDAO.getSpmSignum(tblRpaRequest.getTblProjects().getProjectId());
		if (data != null && data.size() > 0 && data.get(0) != null && data.get(0).length > 0
				&& (String) data.get(0)[0] != null) {
			spmSignum = (String) data.get(0)[0];
        }

    	return spmSignum;
	}

	private void sendBotMail(TblRpaRequest tblRpaRequest, String wfOwnerSignum, String spmSignum,
			String actingUserSignum, String projectName, String workFlowName, String subActivity, String taskName,
			String currRequestStatus, String stepID, String stepName, int wfID) throws Exception {
		Map<String, Object> placeholders = new HashMap<String, Object>();
		List<TblRpaRequest> tblRpaRequestList = null;
		try {
			placeholders.put("requestID", tblRpaRequest.getRpaRequestId());
			placeholders.put("taskID", tblRpaRequest.getTaskId());
			placeholders.put("wfDefID", tblRpaRequest.getWorkflowDefid());
			placeholders.put("subActivityID", tblRpaRequest.getSubactivityId());

			placeholders.put("botId", tblRpaRequest.getRpaRequestId());
			placeholders.put("requestName", tblRpaRequest.getRequestName());
			placeholders.put("projectName", projectName);
			placeholders.put("wfName", workFlowName);
			placeholders.put("subActivityName", subActivity);
			placeholders.put("taskName", taskName);
			placeholders.put("currRequestStatus", currRequestStatus);
			placeholders.put("stepID", stepID);
			placeholders.put("stepName", stepName);
			placeholders.put("wfID", wfID);
			

			EmployeeModel empModel = null;
			String wfOwnerName = null;
			String wfOwnerEmail = null;
			String spmName = null;
			String spmEmail = null;
			String actingUserName = null;
			String actingUserEmail = null;

			if (wfOwnerSignum != null && wfOwnerSignum.trim().length() > 0 && !wfOwnerSignum.equalsIgnoreCase("null")) {
				empModel = activityMasterDAO.getEmployeeBySignum(wfOwnerSignum);
				wfOwnerName = empModel.getEmployeeName();
				wfOwnerEmail = empModel.getEmployeeEmailId();
    		}
			if (spmSignum != null && spmSignum.trim().length() > 0 && !spmSignum.equalsIgnoreCase("null")) {
				empModel = activityMasterDAO.getEmployeeBySignum(spmSignum);
				spmName = empModel.getEmployeeName();
				spmEmail = empModel.getEmployeeEmailId();
    		}
			if (actingUserSignum != null && actingUserSignum.trim().length() > 0
					&& !actingUserSignum.equalsIgnoreCase("null")) {
				empModel = activityMasterDAO.getEmployeeBySignum(actingUserSignum);
				actingUserName = empModel.getEmployeeName();
				actingUserEmail = empModel.getEmployeeEmailId();
			}
			if (spmEmail != null && wfOwnerEmail != null && actingUserEmail != null) {
				placeholders.put(AppConstants.CURRENT_USER_NAME, wfOwnerSignum + " (" + wfOwnerName + ")");
				placeholders.put(AppConstants.CUSTOM_NOTIFICATIONS_TO, wfOwnerEmail + ";" + spmEmail);
				placeholders.put(AppConstants.CUSTOM_NOTIFICATIONS_CC, actingUserEmail);

				if (placeholders.containsKey(AppConstants.CUSTOM_NOTIFICATIONS_TO)
						&& placeholders.get(AppConstants.CUSTOM_NOTIFICATIONS_TO) != null
						&& placeholders.containsKey(AppConstants.CUSTOM_NOTIFICATIONS_CC)
						&& placeholders.get(AppConstants.CUSTOM_NOTIFICATIONS_CC) != null)
					emailService.sendMail(AppConstants.NEW_BOT_REQUEST, placeholders);
				LOG.info("Mail sent succesfully to : " + wfOwnerEmail + ", " + spmEmail + " and : " + actingUserEmail);
			} else if (spmEmail != null && wfOwnerEmail == null && actingUserEmail != null) {
    			placeholders.put(AppConstants.CUSTOM_NOTIFICATIONS_TO, spmEmail);
    			placeholders.put(AppConstants.CUSTOM_NOTIFICATIONS_CC, actingUserEmail);

				if (placeholders.containsKey(AppConstants.CUSTOM_NOTIFICATIONS_TO)
						&& placeholders.get(AppConstants.CUSTOM_NOTIFICATIONS_TO) != null
						&& placeholders.containsKey(AppConstants.CUSTOM_NOTIFICATIONS_CC)
						&& placeholders.get(AppConstants.CUSTOM_NOTIFICATIONS_CC) != null)
					emailService.sendMail(AppConstants.NEW_BOT_REQUEST, placeholders);
				LOG.info("Mail sent succesfully to : " + spmEmail + " and : " + actingUserEmail);
			} else if (spmEmail == null && wfOwnerEmail != null && actingUserEmail != null) {
    			placeholders.put(AppConstants.CUSTOM_NOTIFICATIONS_TO, wfOwnerEmail);
    			placeholders.put(AppConstants.CUSTOM_NOTIFICATIONS_CC, actingUserEmail);

				if (placeholders.containsKey(AppConstants.CUSTOM_NOTIFICATIONS_TO)
						&& placeholders.get(AppConstants.CUSTOM_NOTIFICATIONS_TO) != null
						&& placeholders.containsKey(AppConstants.CUSTOM_NOTIFICATIONS_CC)
						&& placeholders.get(AppConstants.CUSTOM_NOTIFICATIONS_CC) != null)
					emailService.sendMail(AppConstants.NEW_BOT_REQUEST, placeholders);
				LOG.info("Mail sent succesfully to : " + wfOwnerEmail + " and : " + actingUserEmail);
			} else if (spmEmail == null && wfOwnerEmail == null && actingUserEmail != null) {
    			placeholders.put(AppConstants.CUSTOM_NOTIFICATIONS_TO, actingUserEmail);
    			placeholders.put(AppConstants.CUSTOM_NOTIFICATIONS_CC, actingUserEmail);

				if (placeholders.containsKey(AppConstants.CUSTOM_NOTIFICATIONS_TO)
						&& placeholders.get(AppConstants.CUSTOM_NOTIFICATIONS_TO) != null
						&& placeholders.containsKey(AppConstants.CUSTOM_NOTIFICATIONS_CC)
						&& placeholders.get(AppConstants.CUSTOM_NOTIFICATIONS_CC) != null)
					emailService.sendMail(AppConstants.NEW_BOT_REQUEST, placeholders);
				LOG.info("Mail sent succesfully to : " + actingUserEmail);
    		}
			LOG.info("Mail sent succesfully for Request Status: " + currRequestStatus);

		} catch (Exception e) {
    		e.printStackTrace();
    		LOG.info("Error in method sendRequestCreationMail(String wfOwner, TblRpaRequest tblRpaRequest)");
    		throw e;
    	}

	}

	@Override
	public RpaApiResponse submitBotWithLanguageForOthers(TblRpaBotstaging tblRpaBotstaging, int referenceid) {
		RpaApiResponse res = new RpaApiResponse();
		try {
			TblRpaBotstaging stageBotToUpdate = null;

			// Getting target file name from zip file.
			TblRpaBotstaging stageAssignedBotToUpdate = null;
			List<TblRpaBotstaging> stagAssigenedBots = botStoreDAO
					.getRPAStagingForAccepted(tblRpaBotstaging.getTblRpaRequest().getRpaRequestId(), ASSIGNED);
			if (null != stagAssigenedBots && stagAssigenedBots.size() > 0) {
				stageAssignedBotToUpdate = stagAssigenedBots.get(0);
    		}

			List<TblRpaBotstaging> stagBots = botStoreDAO.getRPAStagingForAccepted(
					tblRpaBotstaging.getTblRpaRequest().getRpaRequestId(), tblRpaBotstaging.getAssignedTo(),
					ACCEPTED);
			if (null != stagBots && stagBots.size() > 0) {
				stageBotToUpdate = stagBots.get(0);

    			stageBotToUpdate.setBotname(tblRpaBotstaging.getBotname());
    			stageBotToUpdate.setRpaexecutionTime(tblRpaBotstaging.getRpaexecutionTime());
    			stageBotToUpdate.setBotlanguage(tblRpaBotstaging.getBotlanguage());
    			stageBotToUpdate.setTargetExecutionFileName(tblRpaBotstaging.getTargetExecutionFileName());
    			stageBotToUpdate.setModuleClassName(tblRpaBotstaging.getModuleClassName());
    			stageBotToUpdate.setModuleClassMethod(tblRpaBotstaging.getModuleClassMethod());
				if (tblRpaBotstaging.getParallelWoexecution() != null)
    				stageBotToUpdate.setParallelWoexecution(tblRpaBotstaging.getParallelWoexecution());
    			else
    				stageBotToUpdate.setParallelWoexecution("NO");

    			stageBotToUpdate.setReuseFactor(tblRpaBotstaging.getReuseFactor());
    			stageBotToUpdate.setLineOfCode(tblRpaBotstaging.getLineOfCode());
				stageBotToUpdate.setStatusDescription("Edited By Developer:" + tblRpaBotstaging.getCreatedBy());
    			stageBotToUpdate.setModifiedOn(new Date());
    			stageBotToUpdate.setModifiedBy(tblRpaBotstaging.getCreatedBy());
    		//	stageBotToUpdate.setTargetExecutionFileName(javaPythonFileName);

    			botStoreDAO.createBotStaging(stageBotToUpdate);

				// Upload Code zip file
				// this.botStoreFileUpload_Code(tblRpaBotstaging.getTblRpaRequest().getRpaRequestId(),
				// codefile);
				// AppUtil.ftpFileUpload(environment.getRequiredProperty("botstore.upload.basefolder"),
				// tblRpaBotstaging.getTblRpaRequest().getRpaRequestId()+"", "code", codefile);

			} else {
    			tblRpaBotstaging.setBotid(tblRpaBotstaging.getTblRpaRequest().getRpaRequestId());
				if (null != stageAssignedBotToUpdate)
        			tblRpaBotstaging.setBotname(stageAssignedBotToUpdate.getBotname());
    			tblRpaBotstaging.setCreatedOn(new Date());
    			tblRpaBotstaging.setStatusDescription(tblRpaBotstaging.getStatus());
    		//	tblRpaBotstaging.setTargetExecutionFileName(javaPythonFileName);

				if (tblRpaBotstaging.getParallelWoexecution() != null)
    				tblRpaBotstaging.setParallelWoexecution(tblRpaBotstaging.getParallelWoexecution());
    			else
    				tblRpaBotstaging.setParallelWoexecution("NO");

    			tblRpaBotstaging.setIsActive(1);

    			botStoreDAO.createBotStaging(tblRpaBotstaging);

				// Upload Code zip file
				// this.botStoreFileUpload_Code(tblRpaBotstaging.getTblRpaRequest().getRpaRequestId(),
				// codefile);
				// AppUtil.ftpFileUpload(environment.getRequiredProperty("botstore.upload.basefolder"),
				// tblRpaBotstaging.getTblRpaRequest().getRpaRequestId()+"", "code", codefile);

				TblRpaRequest rpaReqForMail = botStoreDAO
						.getRPARequestById(tblRpaBotstaging.getTblRpaRequest().getRpaRequestId()).get(0);
    			getMailDataAndSend(rpaReqForMail, tblRpaBotstaging.getCreatedBy(), ACCEPTED);
    		}

			if (null != stageAssignedBotToUpdate) {
				stagBots = botStoreDAO.getRPAStagingForAccepted(tblRpaBotstaging.getTblRpaRequest().getRpaRequestId(),
						tblRpaBotstaging.getAssignedTo(), ACCEPTED);
				stageBotToUpdate = stagBots.get(0);

    			stageAssignedBotToUpdate.setBotlanguage(stageBotToUpdate.getBotlanguage());
    			stageAssignedBotToUpdate.setRpaexecutionTime(stageBotToUpdate.getRpaexecutionTime());
    			stageAssignedBotToUpdate.setTargetExecutionFileName(stageBotToUpdate.getTargetExecutionFileName());
    			stageAssignedBotToUpdate.setModuleClassName(stageBotToUpdate.getModuleClassName());
    			stageAssignedBotToUpdate.setModuleClassMethod(stageBotToUpdate.getModuleClassMethod());
    			stageAssignedBotToUpdate.setParallelWoexecution(stageBotToUpdate.getParallelWoexecution());
    			stageAssignedBotToUpdate.setReuseFactor(stageBotToUpdate.getReuseFactor());
    			stageAssignedBotToUpdate.setLineOfCode(stageBotToUpdate.getLineOfCode());

    			botStoreDAO.createBotStaging(stageAssignedBotToUpdate);

				TblRpaRequest rpaRequestModel = botStoreDAO
						.getRPARequestDetails(tblRpaBotstaging.getTblRpaRequest().getRpaRequestId());
				if (rpaRequestModel != null) {
    				rpaRequestModel.setReferenceBotId(referenceid);
    				botStoreDAO.createRpaRequest(rpaRequestModel);
    				}
			}

			// save botconfig json
        	BotConfig botConfig = new BotConfig();
        	botConfig.setJson(tblRpaBotstaging.getBotConfigJson());
			botConfig.setReferenceId(stageAssignedBotToUpdate.getBotid() + "");
        	botConfig.setSignum(tblRpaBotstaging.getCreatedBy());
        	botConfig.setType(AppConstants.BOT_CONFIG_TYPE_BOT);
         	rpaService.saveBotConfig(botConfig);

			res.setApiSuccess(true);
        	res.setResponseMsg("Request submitted Successfully.");
		} catch (Exception e) {
			LOG.info("Error while Creating BOT Stage:" + e.getMessage());
			e.printStackTrace();
        	res.setApiSuccess(false);
			res.setResponseMsg("Error in submitting Bot:: " + e.getMessage());
		}

    	return res;

	}

	@Transactional("txManager")
	public String botMigrationDB(BotDetail botDetail) {
		String status = "SUCCESS";
		try {

			if (botDetail != null && botDetail.getpFileContent() != null && botDetail.getZipFileName() != null) {
				String botID = botDetail.getBotId();

				String zFileName = botDetail.getZipFileName();

				String zipFileName = FilenameUtils.removeExtension(zFileName);

				String signum = botDetail.getSignum();
				
				boolean isMacroBot=botStoreDAO.checkIfMacroBot(Integer.parseInt(botID));
				
				if(!zipFileName.contains("SuperBOT-Macro") && isMacroBot) {
					status="Re-deployment is not supported for Macro Bots";
					return status;
				}

				String fileType = "";
				String fileName = "";
				byte[] file = null;

				LOG.info("File size in KB is : " + (double) botDetail.getpFileContent().length / 1024);
				LOG.info("zipFileName : " + botDetail.getZipFileName());

				file = botDetail.getpFileContent();
				if (zipFileName.toLowerCase().contains("input")) {
					fileType = "input";
					fileName = "input.zip";
				} else if (zipFileName.toLowerCase().contains("output")) {
					fileType = "output";
					fileName = "output.zip";
				} else if (zipFileName.toLowerCase().contains("logic")) {
					fileType = "logic";
					fileName = "logic.zip";
				} else if (zipFileName.toLowerCase().contains("code")) {
					fileType = "code";
					fileName = "code.zip";
				} else if (zipFileName.toLowerCase().contains("exe")) {
					fileType = "exe";
					fileName = "exe.zip";
				} else {
					if (zipFileName.contains("SuperBOT-Macro") || zipFileName.contains("SuperBOT-Python")) {
						fileType = "superbots";
					} else {
						fileType = "bot";
				}

					fileName = zFileName;
				}
				botMigrationDB(botID, signum, fileType, fileName, file, "");
			}
		} catch (Exception e) {
			status = "Fail";
		}

		return status;
	}

	@Transactional("txManager")
	public String botMigrationDBNew(BotDetail botDetail) {
		String status = "SUCCESS";
		try {

			if (botDetail != null && botDetail.getpFileContent() != null && botDetail.getZipFileName() != null) {
				String botID = botDetail.getBotId();
				String zipFileName = botDetail.getZipFileName();
				String signum = botDetail.getSignum();
				String fileType = "";
				String fileName = "";
				byte[] file = null;
				TblRpaRequest tblRpaRequest=botStoreDAO.getRPARequestDetails(Integer.parseInt(botDetail.getBotId()));
				Set<TblRpaBotstaging> tblRpaBotstagings=tblRpaRequest.getTblRpaBotstagings();
				validationBotMigrationDBNew(botDetail, tblRpaRequest,tblRpaBotstagings);
				
				
				LOG.info("File size in KB is : " + (double) botDetail.getpFileContent().length / 1024);
				LOG.info("zipFileName : " + botDetail.getZipFileName());

				file = botDetail.getpFileContent();
				if (zipFileName.toLowerCase().contains("input")) {
					fileType = "input";
					fileName = "input.zip";
				} else if (zipFileName.toLowerCase().contains("output")) {
					fileType = "output";
					fileName = "output.zip";
				} else if (zipFileName.toLowerCase().contains("logic")) {
					fileType = "logic";
					fileName = "logic.zip";
				} else if (zipFileName.toLowerCase().contains("code")) {
					fileType = "code";
					fileName = "code.zip";
				} else if (zipFileName.toLowerCase().contains("exe")) {
					fileType = "exe";
					fileName = "exe.zip";
				} else {
					if (zipFileName.contains("SuperBOT-Macro")) {
						fileType = "superbotmacro";
					} else if (zipFileName.contains("SuperBOT-Python")) {
						fileType = "superbotpython";
					} else if(StringUtils.equalsIgnoreCase(tblRpaBotstagings.iterator().next().getBotlanguage(), "Python")) {
						if(!botDetail.getZipFileName().toLowerCase().contains(".whl")) {
							throw new ApplicationException(200,"Input.zip, output.zip, code.zip, logic.zip and .whl file are only allowed for python bot");
						}
						fileType = "bot";
						CloudBlobDirectory blobDir=cloudBlobContainer.getDirectoryReference(AppConstants.BOT_CONF_FILES+"/"+botID + "/"+fileType);
						for (ListBlobItem blobItem : blobDir.listBlobs()) {
				            // If the item is a blob, not a virtual directory.
				            if (blobItem instanceof CloudBlob) {
				                CloudBlob blob = (CloudBlob) blobItem;
				                blob.deleteIfExists();
				            }
				        }
						
					}
					else {
						fileType = "bot";
					}

					fileName = zipFileName;
				}
				 botMigrationDB(botID, signum, fileType, fileName, file, "New");
			}
		} catch(ApplicationException ae) {
			status=ae.getErrorMessage();
		}
		catch (Exception e) {
			status = "Fail";
		}

		return status;
	}

	public void updateBotVerion(Set<TblRpaBotstaging> tblRpaBotstagings) {
		/*
		 * for(TblRpaBotstaging x:tblRpaBotstagings) { botStoreDAO.updateBotStaging(x);
		 * }
		 */
		TblRpaDeployedBot tblRpaDeployedBot=botStoreDAO.getBotDetailById(tblRpaBotstagings.iterator().next().getBotid());
		tblRpaDeployedBot.setLanguageBaseVersionID(tblRpaBotstagings.iterator().next().getLanguageBaseVersionID());
		botStoreDAO.createRpaDeployBot(tblRpaDeployedBot);
	}

	public void validationBotMigrationDBNew(BotDetail botDetail, TblRpaRequest tblRpaRequest, Set<TblRpaBotstaging> tblRpaBotstagings) {
		
		if(tblRpaRequest==null) {
			throw new ApplicationException(200,"No entry for given bot in RpaRequests");
		}
		if(tblRpaBotstagings==null || !tblRpaBotstagings.iterator().hasNext()) {
			throw new ApplicationException(200,"No entry for given bot in RpaStaging");
		}
		if(!botDetail.getZipFileName().contains("SuperBOT-Macro") && StringUtils.equalsIgnoreCase(tblRpaBotstagings.iterator().next().getBotlanguage(), "MACRO")) {
			throw new ApplicationException(200,"Re-deployment is not supported for Macro Bots");
		}
		if(StringUtils.equalsIgnoreCase(tblRpaBotstagings.iterator().next().getBotlanguage(), "Python")
				&& botDetail.getZipFileName().toLowerCase().contains(".whl")) {
			if(botDetail.getZipFileName().toLowerCase().contains("exe")) {
				throw new ApplicationException(200,"Input.zip, output.zip, code.zip, logic.zip and .whl file are only allowed for python bot");
			}
			LanguageBaseVersionModel languageBaseVersionModel=botStoreDAO.getLanguageBaseVersionByVersion(botDetail.getBaseVersion());
			if(languageBaseVersionModel==null) {
				throw new ApplicationException(200,"Please provide valid Base Version");
			}
			String[] zipSplit=botDetail.getZipFileName().split("-");
			String moduleClassName=StringUtils.join(zipSplit, '-', 0, zipSplit.length-4);
			for(TblRpaBotstaging x:tblRpaBotstagings) {
				if(x.getLanguageBaseVersionID()==null || languageBaseVersionModel.getLanguageBaseVersionID() !=x.getLanguageBaseVersionID()) {
					x.setLanguageBaseVersionID(languageBaseVersionModel.getLanguageBaseVersionID());
					x.setModuleClassName(moduleClassName);
				}
				if(!x.getTargetExecutionFileName().equalsIgnoreCase(botDetail.getZipFileName())) {
					x.setTargetExecutionFileName(botDetail.getZipFileName());
				}
			}
			for(TblRpaDeployedBot x:tblRpaRequest.getTblRpaDeployedBot()) {
				if(x.getLanguageBaseVersionID()==null || languageBaseVersionModel.getLanguageBaseVersionID() !=x.getLanguageBaseVersionID()) {
					x.setLanguageBaseVersionID(languageBaseVersionModel.getLanguageBaseVersionID());
					x.setModuleClassName(moduleClassName);
				}
				
				if(!x.getTargetExecutionFileName().equalsIgnoreCase(botDetail.getZipFileName())) {
					x.setTargetExecutionFileName(botDetail.getZipFileName());
				}
			}
		}
		
	}

	@Transactional("txManager")
	public String uploadBotInputOutput(BotDetail botDetail) {
		byte[] inputDataFile = null;
		byte[] outputDataFile = null;
		String status = "";
		String fileName=null;
		try {
			if (botDetail != null) {
				if (botDetail.getpFileContent() != null && botDetail.getpFileContent().length > 0) {
    				inputDataFile = botDetail.getpFileContent();
    				fileName= botDetail.getOutputFileName();
    				externalInterfaceManagmentService.uploadFileInContainerFromByteArray(inputDataFile,
    						AppConstants.BOT_CONF_FILES+"/"+botDetail.getBotId() + INPUT, fileName);
    				
    				status = "SUCCESS";
					LOG.info("Inputfile uploaded successfully : " + botDetail.getOutputFileName());
    			}

				if (botDetail.getBotOutputFileByteArr() != null && botDetail.getBotOutputFileByteArr().length > 0) {
    				outputDataFile = botDetail.getBotOutputFileByteArr();
    				fileName= botDetail.getOutputFileName();
    				externalInterfaceManagmentService.uploadFileInContainerFromByteArray(outputDataFile,
							AppConstants.OUTPUT_FILES+"/"+botDetail.getBotId() + "/", fileName);
    				
    				status = "SUCCESS";
					LOG.info("Outputfile uploaded successfully : " + botDetail.getOutputFileName());
    			}

//    			if(outputDataFile!=null && outputDataFile.length>0)
//    			{
				// Check if Exist
				/*
				 * List<TblRpaBotExecutionDetail> list =
				 * botStoreDAO.getOUTFile(botDetail.getOutputFileName()); if (null != list &&
				 * list.size() > 0) { status = "SUCCESS"; LOG.info("Outputfiel file exist : " +
				 * botDetail.getOutputFileName()); } else { TblRpaBotExecutionDetail
				 * tblRpaBotExecutionDetail = new TblRpaBotExecutionDetail();
				 * tblRpaBotExecutionDetail.setBotId(Integer.valueOf(botDetail.getBotId()));
				 * tblRpaBotExecutionDetail.setSignum(botDetail.getSignum());
				 * tblRpaBotExecutionDetail.setWoNo(Integer.valueOf(botDetail.getWoNo()));
				 * tblRpaBotExecutionDetail.setProjectId(Integer.valueOf(botDetail.getProjectId(
				 * )));
				 * tblRpaBotExecutionDetail.setTaskId(Integer.valueOf(botDetail.getTaskId()));
				 * tblRpaBotExecutionDetail.setNodes(botDetail.getNodes());
				 * tblRpaBotExecutionDetail.setBotPlateform(botDetail.getBotPlateForm());
				 * tblRpaBotExecutionDetail.setIsEcnConnected(botDetail.getIsEcnConnected());
				 * 
				 * // if(inputDataFile!=null && inputDataFile.length>0) // { //
				 * tblRpaBotExecutionDetail.setInputFileName(botDetail.getZipFileName()); //
				 * tblRpaBotExecutionDetail.setInputDataFile(inputDataFile); // }
				 * 
				 * if (outputDataFile != null && outputDataFile.length > 0) {
				 * tblRpaBotExecutionDetail.setOutputFileName(botDetail.getOutputFileName());
				 * tblRpaBotExecutionDetail.setOutputDataFile(outputDataFile); }
				 * 
				 * tblRpaBotExecutionDetail.setCreatedBy(botDetail.getSignum());
				 * tblRpaBotExecutionDetail.setCreatedOn(new Date());
				 * botStoreDAO.createRpaInputFile(tblRpaBotExecutionDetail);
				 * 
				 * status = "SUCCESS"; LOG.info("Outputfile uploaded successfully : " +
				 * botDetail.getOutputFileName()); }
				 */
//    			}
			}
		} catch (Exception e) {
			status = "Fail";
			LOG.info("Error in uploadBotInputOutput(): " + e.getMessage());
			//e.printStackTrace();
			// res.setApiSuccess(false);
			// res.setResponseMsg("Error in :: "+e.getMessage());
		}

    	return status;
    }

	public TblRpaBottesting getTestingDataById(int testId) {
		return botStoreDAO.getTestingDataById(testId);
	}

	@Override
	public UserWiseDesktopVersionModel getDesktopVersionBySignum(String signum) {
		return this.botStoreDAO.getDesktopVersionBySignum(signum);
	}

	@Override
	public void saveDesktopVersionBySignum(UserWiseDesktopVersionModel model) {
		model.setSignumId(StringUtils.lowerCase(model.getSignumId()));
		this.botStoreDAO.saveDesktopVersionBySignum(model);
		DesktopInformationModel desktopInformation=activityMasterDAO.getDesktopInformation(model.getSignumId());
		desktopInformation.setStatus(true);
		desktopInformation.setSignumId(model.getSignumId());
		if(desktopInformation.getCurrentDesktopVersion().equals(model.getVersion())) {
		this.redisService.setValueBasedOnKey(model.getSignumId()+AppConstants.LATESTDESTOPVERSION,desktopInformation);
		}
	}

	@Transactional("txManager")
	public TblRpaBotExecutionDetail getCascadedBOTInFile(String signum, Integer woNo, Integer taskId) {
		TblRpaBotExecutionDetail   botExecutionDetail	= new TblRpaBotExecutionDetail();
		byte[] botJarFile = null;
		try {
			// below code to download file from DB location-->

			botExecutionDetail = botStoreDAO.getCascadedBOTInFile(signum, woNo, taskId);

			if (null != botExecutionDetail && null != botExecutionDetail.getOutputDataFile()) {
				botJarFile = botExecutionDetail.getOutputDataFile();
				long size = botJarFile.length;
				if (botJarFile.length > 0) {
					// LOG.info("File Type "+OutFileName+" is found.. Size: "+(double) size / (1024
					// * 1024));

					//	    				String fPATH="C:\\Users\\essrmma\\Desktop\\Rupinder\\1604\\"+fileName;
					//	    				FileUtils.writeByteArrayToFile(new File(fPATH), botJarFile);
				} else {
					//	    				LOG.info("File Type "+OutFileName+" is not found..");
					botJarFile = null;
				}
			}
		} catch (Exception e) {
			LOG.info("Error in botDownload(): " + e.getMessage());
			e.printStackTrace();
		}
		return botExecutionDetail;
	}

	@Override
	public String uploadUpdatedBotOutput(BotDetail botDetail, MultipartFile outFile) {
		try {
			botDetail.setBotOutputFileByteArr(outFile.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		String status = uploadBotInputOutput(botDetail);
		return status;
	}

	/**
	 * Update Input File status by WFOwner
	 * 
	 * @author ekmbuma
	 * 
	 * @param botId
	 * @param isInputRequired
	 * @param workFlowOwner
	 * @return RpaApiResponse
	 * @throws Exception
	 * @throws IOException
	 * @throws SQLException
	 */
	@Transactional("txManager")
	public RpaApiResponse updateInputFileStatus(int botId, boolean isInputRequired, String workFlowOwner)
			throws IOException {

		RpaApiResponse res = new RpaApiResponse();

		try {

			boolean isMacroBot=botStoreDAO.checkIfMacroBot(botId);
			
			if(isMacroBot) {
				res.setApiSuccess(false);
				res.setData(botId + "");
				res.setResponseMsg("Update Bot functionality for Macro Bot is not supported.");
			}
			TblRpaDeployedBot deployedBot = botStoreDAO.getBotDetailById(botId);

			if (deployedBot.getBotid() != 0) {
				deployedBot.setModifiedBy(workFlowOwner);
				deployedBot.setModifiedOn(new Date());
				deployedBot.setIsInputRequired(isInputRequired);

				botStoreDAO.updateRpaDeployedBot(deployedBot); // update in table transactionalData.TBL_RPA_DeployedBOT
				botStoreDAO.updateRpaRequestInputFileStatus(deployedBot); // update in table
																			// transactionalData.TBL_RPA_REQUEST

				res.setApiSuccess(true);
				res.setData(botId + "");
				res.setResponseMsg("Input file status updated Successfully for Bot id : " + botId);

			} else {
				res.setApiSuccess(false);
				res.setData(botId + "");
				res.setResponseMsg("Bot not found for id : " + botId);
			}

		} catch (Exception e) {
			LOG.info("Error while Updating Input file status :" + e.getMessage());
			e.printStackTrace();
			res.setApiSuccess(false);
			res.setResponseMsg("Error in Updating Input file status : " + e.getMessage());
		}

		return res;
	}
	@Override
	public Object getDataFromRedis(String key) {
		return this.redisService.getDataFromRedis(key);
	}
	
	@Override
	public void deleteDataFromRedis(String key) {
		this.redisService.deleteDataFromRedis(key);
	}
	

	@Override
	public Response<UserLibraryVersionModel> getLibraryVersionBySignum(String signum) {
		
		Response<UserLibraryVersionModel> response = new Response<>();
		try {
			response.setResponseData(botStoreDAO.getLibraryVersionBySignum(signum));
		}
		catch (ApplicationException exe) 
		{
			LOG.error(exe.getMessage());
			response.addFormError(exe.getMessage());
		} 
		catch (Exception ex) {
			LOG.error(ex.getMessage());
			response.addFormError(ex.getMessage());
		}
		return response;
	}
	
	@Override
	public Response<Boolean> saveLibraryVersionBySignum(UserLibraryVersionModel userLibraryVersion){
		Response<Boolean> response = new Response<>();
		try {
			validationUtilityService.validateStringForBlank(userLibraryVersion.getLibraryVersion(), "Library Version");
			validationUtilityService.validateStringForBlank(userLibraryVersion.getSignumId(), "SignumID");
			userLibraryVersion.setCreatedBy(userLibraryVersion.getSignumId());
			userLibraryVersion.setIsActive(true);
			botStoreDAO.saveLibraryVersionBySignum(userLibraryVersion);
			response.setResponseData(true);
		}
		catch(Exception e) {
			LOG.info("Error in saveLibraryVersionBySignum(): " + e.getMessage());
			e.printStackTrace();
			response.addFormError(e.getMessage());
			response.setResponseData(false);
			
		}
		return response;
	}
	
	@Override
	public Response<List<DesktopLibraryResponseModel>> getLibraryVersionGap(float version) {
		Response<List<DesktopLibraryResponseModel>> response = new Response<>();
		try {
			response.setResponseData(botStoreDAO.getLibraryVersionGap(version));
		}
		catch (ApplicationException exe) 
		{
			LOG.error(exe.getMessage());
			response.addFormError(exe.getMessage());
		} 
		catch (Exception ex) {
			LOG.error(ex.getMessage());
			response.addFormError(ex.getMessage());
		}
		return response;
	}
	
	// listFor: ALL
	public List<TblRpaRequest> getNewRequestListForRPAAdmin(String listFor) throws Exception {

		List<TblRpaRequest> tblRpaRequests = null;
		List<TblRpaRequest> requestsForUi = new ArrayList<TblRpaRequest>();

    	Set<TblRpaBotstaging> tblRpaBotstagingSetForUi = null;

		TblRpaBotstaging tblRpaBotstaging = null;
		TblRpaBotstaging stagForUi = null;

    	try {
    		  		
			if (listFor.equalsIgnoreCase("ALL")) {
	     		tblRpaRequests = botStoreDAO.getRPARequestsNEW();
			}
        	

			if (null == tblRpaRequests || tblRpaRequests.isEmpty()) {
            	return requestsForUi;
			} else {
				LOG.info("Requests List Size--> {}" , tblRpaRequests.size());
				TblRpaRequest reqForUi = null;
				TblProjects projForUi = null;

				// getting all the requests staging data with latest stage entry:
				Map<Integer, TblRpaBotstaging> stgMap = new HashMap<Integer, TblRpaBotstaging>();
				List<Object[]> stgLatestData = botStoreDAO.getRPAStagingDataNewForRPAAdmin();
				Integer reqId = null;
				for (Object[] d : stgLatestData) {
					TblRpaBotstaging stg = null;
					reqId = (Integer) d[0];
					if (null != d[1]) {
						stg = new TblRpaBotstaging();
						stg.setAssignedTo((String) d[3]);
						stg.setBotname((String) d[4]);
						stg.setRpaexecutionTime((Integer) d[5]);
						stg.setBotlanguage((String) d[6]);
						stg.setTargetExecutionFileName((String) d[7]);
						stg.setModuleClassName((String) d[8]);
						stg.setModuleClassMethod((String) d[9]);
						stg.setParallelWoexecution((String) d[10]);
						stg.setReuseFactor((Integer) d[11]);
						stg.setLineOfCode((Integer) d[12]);
						stg.setStatus((String) d[13]);
						stg.setStatusDescription((String) d[14]);
						stg.setCreatedBy((String) d[15]);
						stg.setCreatedOn((Date) d[16]);
						stg.setModifiedBy((String) d[17]);
						stg.setModifiedOn((Date) d[18]);
						stg.setModifiedOn((Date) d[18]);
            		}
            		stgMap.put(reqId, stg);
            	}

				// getting request and its staging status for current status display:
				Map<Integer, Set<String>> reqStatusMap = new HashMap<Integer, Set<String>>();
				List<Object[]> stgData = botStoreDAO.getStageBotsStatusForRPAAdmin();
				Set<String> statusSet = null;
				String status = null;
				for (Object[] d : stgData) {
					reqId = (Integer) d[0];
					if (null != d[1])
						status = (String) d[1];

					if (reqStatusMap.containsKey(reqId))
            			reqStatusMap.get(reqId).add(status);
					else {
						statusSet = new HashSet<String>();
                		statusSet.add(status);
                		reqStatusMap.put(reqId, statusSet);
            		}
            	}

				// getting request data for task name, WorkFlowName, subact name:
				Map<Integer, String[]> dataMap = new HashMap<Integer, String[]>();
				List<Object[]> taskList = botStoreDAO.getRPARequestStepDetailsNewForRPAAdmin();
				String[] taskDataArr = null;
				Integer rId = null;
				for (Object[] d : taskList) {
					taskDataArr = new String[4];

					rId = (Integer) d[0];
					if (null != d[1])
						taskDataArr[0] = (String) d[1];
            		else {
						taskDataArr[0] = "";
            		}
					if (null != d[2])
						taskDataArr[1] = (String) d[2];
            		else {
						taskDataArr[1] = "";
            		}
					if (null != d[3])
						taskDataArr[2] = (String) d[3];
            		else {
						taskDataArr[2] = "";
            		}
					if (null != d[4])
						taskDataArr[3] = (String) d[4];
            		else {
						taskDataArr[3] = "";
            		}

            		dataMap.put(rId, taskDataArr);
            	}

				for (TblRpaRequest req : tblRpaRequests) {
					reqForUi = new TblRpaRequest();
    				reqForUi.setRequestStatus(req.getRequestStatus());
            		reqForUi.setRpaRequestId(req.getRpaRequestId());
    				reqForUi.setRequestName(req.getRequestName());
					reqForUi.setIsInputRequired(req.getIsInputRequired());
            		reqForUi.setSpocsignum(req.getSpocsignum());
            		reqForUi.setCurrentExecutioncountWeekly(req.getCurrentExecutioncountWeekly());

            		reqForUi.setCurrentAvgExecutiontime(req.getCurrentAvgExecutiontime());

            		reqForUi.setWorkflowDefid(req.getWorkflowDefid());
            		reqForUi.setWfstepid(req.getWfstepid());
            		reqForUi.setSubactivityId(req.getSubactivityId());
            		reqForUi.setTaskId(req.getTaskId());
            		reqForUi.setDescription(req.getDescription());
            		reqForUi.setCreatedOn(req.getCreatedOn());

					projForUi = new TblProjects();
                	projForUi.setProjectId(req.getTblProjects().getProjectId());
//                	projForUi.setProjectName(req.getTblProjects().getProjectName());
                	reqForUi.setTblProjects(projForUi);
            		
					// getting Step data:
					if (null != dataMap.get(req.getRpaRequestId())) {
                		reqForUi.setWorkFlowName(dataMap.get(req.getRpaRequestId())[0]);
						reqForUi.setSubactivityName(
								dataMap.get(req.getRpaRequestId())[1] + "/" + dataMap.get(req.getRpaRequestId())[2]);
                		reqForUi.setTaskName(dataMap.get(req.getRpaRequestId())[3]);
                    }

					String botCurrStatus = null;
					if (null != reqStatusMap.get(req.getRpaRequestId()))
						botCurrStatus = getBotCurrStatusNew(reqStatusMap.get(req.getRpaRequestId()));

					tblRpaBotstaging = stgMap.get(req.getRpaRequestId());
					if (tblRpaBotstaging != null) {
						stagForUi = new TblRpaBotstaging();
    					stagForUi.setAssignedTo(tblRpaBotstaging.getAssignedTo());
    					stagForUi.setBotname(tblRpaBotstaging.getBotname());
    					stagForUi.setBotlanguage(tblRpaBotstaging.getBotlanguage());
    					stagForUi.setRpaexecutionTime(tblRpaBotstaging.getRpaexecutionTime());
    					stagForUi.setTargetExecutionFileName(tblRpaBotstaging.getTargetExecutionFileName());
    					stagForUi.setModuleClassName(tblRpaBotstaging.getModuleClassName());
    					stagForUi.setModuleClassMethod(tblRpaBotstaging.getModuleClassMethod());
    					stagForUi.setParallelWoexecution(tblRpaBotstaging.getParallelWoexecution());
    					stagForUi.setReuseFactor(tblRpaBotstaging.getReuseFactor());
    					stagForUi.setLineOfCode(tblRpaBotstaging.getLineOfCode());
    					stagForUi.setStatus(botCurrStatus);
    					stagForUi.setStatusDescription(tblRpaBotstaging.getStatusDescription());
    					stagForUi.setCreatedBy(tblRpaBotstaging.getCreatedBy());
    					stagForUi.setCreatedOn(tblRpaBotstaging.getCreatedOn());

						tblRpaBotstagingSetForUi = new HashSet<TblRpaBotstaging>(0);
    					tblRpaBotstagingSetForUi.add(stagForUi);
    					reqForUi.setTblRpaBotstagings(tblRpaBotstagingSetForUi);
      			 	}

                    reqForUi.setVideoURL(req.getVideoURL());

                	requestsForUi.add(reqForUi);
            	}
            }
		} catch (Exception e) {
			e.printStackTrace();
            LOG.info("Error in method getNewRequestListForRPAAdmin(String listFor)");
            throw e;
		}

    	return requestsForUi;
    }
	
	public DesktopAppResponseModel getDesktopAppVersion() {
		DesktopAppResponseModel desktopAppResponse=new DesktopAppResponseModel();
		try {
			desktopAppResponse=botStoreDAO.getDesktopAppVersion();
		}
		catch(Exception ex) {
			LOG.info(ex.getMessage());
			ex.printStackTrace();
		}

    	return desktopAppResponse;
    }
	
	@Override
	public Response<List<LanguageBaseVersionModel>> getLanguageBaseVersion() {
		Response<List<LanguageBaseVersionModel>> response = new Response<>();
		try {
			response.setResponseData(botStoreDAO.getLanguageBaseVersion());
		}
		catch (ApplicationException exe) 
		{
			LOG.error(exe.getMessage());
			response.addFormError(exe.getMessage());
		} 
		catch (Exception ex) {
			LOG.error(ex.getMessage());
			response.addFormError(ex.getMessage());
		}
		return response;
	}
	
	public Response<URI> getPythonInstallerUrl(String pythonBaseVersion){
		Response<URI> response = new Response<>();
		StringBuilder filePath=new StringBuilder("PythonInstaller");
		filePath.append(AppConstants.FORWARD_SLASH)
			     .append(pythonBaseVersion)
			     .append(AppConstants.FORWARD_SLASH)
			     .append("python-")
			     .append(pythonBaseVersion)
			     .append(".exe");
		
		CloudBlockBlob blob=null;
		try {
			blob = cloudBlobContainer.getBlockBlobReference(filePath.toString());
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (StorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.setResponseData(blob.getUri());
		return response;
	}
	
}