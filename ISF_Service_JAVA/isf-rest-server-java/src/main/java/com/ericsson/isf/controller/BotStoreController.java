package com.ericsson.isf.controller;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import com.ericsson.isf.exception.ApplicationException;
import com.ericsson.isf.model.DesktopLibraryResponseModel;
import com.ericsson.isf.model.DownloadTemplateModel;
import com.ericsson.isf.model.GetBotDetailByIdDTO;
import com.ericsson.isf.model.GetBotStageDetailsForTestingDTO;
import com.ericsson.isf.model.LanguageBaseVersionModel;
import com.ericsson.isf.model.RPARequestDetailsDTO;
import com.ericsson.isf.model.Response;
import com.ericsson.isf.model.TblRpaDeployedBotModel;
import com.ericsson.isf.model.UserLibraryVersionModel;
import com.ericsson.isf.model.UserWiseDesktopVersionModel;
import com.ericsson.isf.model.botstore.BotDetail;
import com.ericsson.isf.model.botstore.DesktopAppResponseModel;
import com.ericsson.isf.model.botstore.RpaApiResponse;
import com.ericsson.isf.model.botstore.TblProjects;
import com.ericsson.isf.model.botstore.TblRpaBotstaging;
import com.ericsson.isf.model.botstore.TblRpaBottesting;
import com.ericsson.isf.model.botstore.TblRpaDeployedBot;
import com.ericsson.isf.model.botstore.TblRpaDeployedBotView;
import com.ericsson.isf.model.botstore.TblRpaRequest;
import com.ericsson.isf.model.botstore.TblRpaRequestDetails;
import com.ericsson.isf.service.BotStoreService;
import com.ericsson.isf.service.RpaService;
import com.ericsson.isf.util.AppConstants;
import com.ericsson.isf.util.BotStoreUtil;
import com.fasterxml.jackson.annotation.JsonView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 *
 * @author esaabeh
 */
@RestController
@RequestMapping("/botStore")
public class BotStoreController {

	private static final Logger LOG = LoggerFactory.getLogger(BotStoreController.class);
	private static final String PROVIDED_DATA_CANNOT_BE_BLANK_FOR_SAVING_BOT_REQUEST="Provided data cannot be blank/null for saving the BOT Request.";
	private static final String ONLY_ZIP_FILES_ARE_ALLOWED_TO_UPLOAD="Only .zip files are allowed to upload.";
	private static final String PROVIDED_REQUEST_CANNOT_BE_NULL="Provided Request cannot be null.";
	private static final String ERROR_WHILE_CREATING_RPA_BOT_REQUEST="Error while Creating RPA BOT Request: {}";
	private static final String ERROR_IN_CREATING_RPA_BOT_REQUEST="Error in creating RPA BOT request:: %s";
	private static final String ONLY_EXE_OR_JAR_IS_ALLOWED_TO_UPLOAD="Only .exe or .jar is allowed to upload inside the exe.zip.";
	private static final String TBLRPABOTSTAGING_CREATE_REQUEST_SUCCESS="TblRpaBotstaging Create Request: SUCCESS";
	private static final String PROVIDED_DATA_CANNOT_BE_BLANK_FOR_UPDATING_BOT_REQUEST="Provided data cannot be blank/null for updating the BOT Request.";

	@Autowired
	private BotStoreService botStoreService;

	@Autowired
	private RpaService rpaService;
	/**
	 * 
	 * @return List<TblRpaDeployedBot>
	 * @throws IOException
	 * @throws SQLException
	 */
	@RequestMapping(value = "/getBOTsForExplore", method = RequestMethod.GET)
	public List<TblRpaDeployedBotModel> getBOTsForExplore() throws IOException, SQLException {
		return rpaService.getBOTsForExploreAuditData();
	}

	@RequestMapping(value = "/getBOTsForExplore/{taskId}", method = RequestMethod.GET)
	public List<TblRpaDeployedBot> getBOTsForExplore(@PathVariable("taskId") int taskId)
			throws IOException, SQLException {
		List<TblRpaDeployedBot> rpaDeployedBotList = botStoreService.getBOTsForExplore(taskId);
		return rpaDeployedBotList;
	}

	// Used for Explore BOT details and New Request Details
	/**
	 * 
	 * @param rpaRequestID
	 * @return TblRpaRequest
	 */
	@RequestMapping(value = "/getRPARequestDetails/{rpaRequestID}", method = RequestMethod.GET)
	public RPARequestDetailsDTO getRPARequestDetails(@PathVariable("rpaRequestID") int rpaRequestID) {
		if (rpaRequestID < 1) {
			return new RPARequestDetailsDTO();
		} else {
			return this.botStoreService.getRPARequestDetails(rpaRequestID);
		}
	}

	// Used for Explore BOT details and New Request Details
	@RequestMapping(value = "/getRPARequestDetailsWithExtraData/{rpaRequestID}", method = RequestMethod.GET)
	public TblRpaRequestDetails getRPARequestDetailsWithExtraData(@PathVariable("rpaRequestID") int rpaRequestID) {
		if (rpaRequestID < 1) {
			return new TblRpaRequestDetails();
		} else {
			TblRpaRequestDetails rpaRequestDetails = this.botStoreService
					.getRPARequestDetailsWithExtraData(rpaRequestID);
			return rpaRequestDetails;
		}
	}

	// Used for Create BOT Request Details
	@RequestMapping(value = "/getRPARequestWithBotStaging/{rpaRequestID}", method = RequestMethod.GET)
	public TblRpaRequest getRPARequestWithBotStaging(@PathVariable("rpaRequestID") int rpaRequestID)
			throws IOException, SQLException {
		if (rpaRequestID < 1) {
			return new TblRpaRequest();
		} else {
			TblRpaRequest rpaRequest = this.botStoreService.getRPARequestWithBotStaging(rpaRequestID);
			return rpaRequest;
		}
	}

	/**
	 * creates request for new bot to be developed by Bot Developer modified by
	 * - @ehrmsng
	 * 
	 * @param tblRpaRequestStr
	 * @param infile
	 * @param outfile
	 * @param logicfile
	 * @param wfOwner
	 * @param isInputRequired
	 * @return RpaApiResponse
	 * @throws Exception
	 * @throws IOException
	 * @throws SQLException
	 * 
	 */
	@RequestMapping(value = "/createNewRequest", method = RequestMethod.POST, consumes = "multipart/form-data")
	public RpaApiResponse createNewRequest(@RequestParam("tblRpaRequestStr") String tblRpaRequestStr,
			@RequestPart(value = "infile", required = false) MultipartFile infile,
			@RequestPart("outfile") MultipartFile outfile, @RequestPart("logicfile") MultipartFile logicfile,
			@RequestParam(value = "wfOwner", required = false) String wfOwner,
			@RequestParam(value = "isInputRequired") boolean isInputRequired)
			{

		RpaApiResponse res = new RpaApiResponse();
		try {
			if (null == tblRpaRequestStr || (isInputRequired && infile == null) || outfile == null
					|| logicfile == null) {

				res.setApiSuccess(false);
				res.setResponseMsg(PROVIDED_DATA_CANNOT_BE_BLANK_FOR_SAVING_BOT_REQUEST);
				LOG.info(PROVIDED_DATA_CANNOT_BE_BLANK_FOR_SAVING_BOT_REQUEST);
			} else if ((!isInputRequired || infile.getOriginalFilename().contains(".zip"))
					&& outfile.getOriginalFilename().contains(".zip")
					&& logicfile.getOriginalFilename().contains(".zip")) {

				if (null != wfOwner && wfOwner.contains("[")) {
					wfOwner = wfOwner.trim().split("\\[")[0];
				}
				// converting JSON to TblRpaRequest
				Gson gson = new Gson();
				JsonParser parser = new JsonParser();
				JsonObject object = (JsonObject) parser.parse(tblRpaRequestStr);// response will be the json String
				TblRpaRequest tblRpaRequest = gson.fromJson(object, TblRpaRequest.class);
				tblRpaRequest.setIsInputRequired(isInputRequired);

				res = botStoreService.createNewRequest(tblRpaRequest, infile, outfile, logicfile, "NEW", wfOwner);
				LOG.info("TblRpaRequest Create Request: SUCCESS.");
			} else {

				res.setApiSuccess(false);
				res.setResponseMsg(ONLY_ZIP_FILES_ARE_ALLOWED_TO_UPLOAD);
				LOG.info(ONLY_ZIP_FILES_ARE_ALLOWED_TO_UPLOAD);
			}
		} catch (Exception e) {
			LOG.info("Error while Creating RPA Request: {}" , e.getMessage());
			e.printStackTrace();
			res.setApiSuccess(false);
			res.setResponseMsg("Error in creating RPA request:: " + e.getMessage());
		}

		return res;
	}

	/**
	 * updates RpaRequest Table and files in blob
	 * 
	 * @modified - EHRMSNG
	 * @param tblRpaRequestStr
	 * @param infile
	 * @param outfile
	 * @param logicfile
	 * @return RpaApiResponse
	 * @throws Exception
	 * @throws IOException
	 * @throws SQLException
	 */
	@RequestMapping(value = "/updateNewRequest", method = RequestMethod.POST, consumes = "multipart/form-data")
	public RpaApiResponse updateNewRequest(@RequestParam("tblRpaRequestStr") String tblRpaRequestStr,
			@RequestParam(value = "infile", required = false) MultipartFile infile,
			@RequestParam(value = "outfile", required = false) MultipartFile outfile,
			@RequestParam(value = "logicfile", required = false) MultipartFile logicfile){
		RpaApiResponse res = new RpaApiResponse();
		try {

			if (null == tblRpaRequestStr) {
				res.setApiSuccess(false);
				res.setResponseMsg(PROVIDED_REQUEST_CANNOT_BE_NULL);
				LOG.info(PROVIDED_REQUEST_CANNOT_BE_NULL);
			} else {
				Gson gson = new Gson();
				JsonParser parser = new JsonParser();
				JsonObject object = (JsonObject) parser.parse(tblRpaRequestStr);// response will be the json String
				TblRpaRequest tblRpaRequest = gson.fromJson(object, TblRpaRequest.class);

				if (tblRpaRequest.getIsInputRequired() && infile == null) {
					res.setApiSuccess(false);
					res.setResponseMsg("Provided input file");
					LOG.info("Provided input file");
				} else {
					res = botStoreService.updateNewRequest(tblRpaRequest, infile, outfile, logicfile, "UPDATED");
					LOG.info("TblRpaRequest Update Request: SUCCESS and Request ID--> {}" , res.getData());

				}
			}
		} catch (Exception e) {
			LOG.info("Error while Update RPA Request: {}" , e.getMessage());
			e.printStackTrace();
			res.setApiSuccess(false);
			res.setResponseMsg("Error in updating RPA request:: " + e.getMessage());
		}

		return res;
	}


	/**
	 * creates new bot for java, python, penguin to be tested and approved. Own
	 * Development
	 * 
	 * @modified EHRMSNG
	 * @param tblRpaRequestStr
	 * @param tblRpaBotstagingStr
	 * @param infile
	 * @param outfile
	 * @param logicfile
	 * @param codefile
	 * @param wfOwner
	 * @param isInputRequired
	 * @return RpaApiResponse
	 * @throws Exception
	 * @throws IOException
	 * @throws SQLException
	 */
//	@ExceptionHandler(MultipartException.class)
	@RequestMapping(value = "/createBotRequestForJavaPython", method = RequestMethod.POST, consumes = "multipart/form-data")
	public RpaApiResponse createBotRequestForJavaPython(@RequestParam("tblRpaRequestStr") String tblRpaRequestStr,
			@RequestParam("tblRpaBotstagingStr") String tblRpaBotstagingStr,
			@RequestPart("infile") MultipartFile infile,
			@RequestPart("outfile") MultipartFile outfile,
			@RequestPart("logicfile") MultipartFile logicfile,
			@RequestPart("codefile") MultipartFile codefile,
			@RequestPart(value ="exefile" , required =false) MultipartFile exefile,
			@RequestParam(value = "wfOwner", required = false) String wfOwner,
			@RequestParam(value = "isInputRequired") boolean isInputRequired,
			@RequestParam(value = "whlfile", required = false) MultipartFile whlfile)
			{
		RpaApiResponse res = new RpaApiResponse();
		try {
			if (null == tblRpaRequestStr || null == tblRpaBotstagingStr || (isInputRequired && infile == null)
					|| outfile == null || logicfile == null || codefile == null) {
				res.setApiSuccess(false);
				res.setResponseMsg(PROVIDED_DATA_CANNOT_BE_BLANK_FOR_SAVING_BOT_REQUEST);
				LOG.info(PROVIDED_DATA_CANNOT_BE_BLANK_FOR_SAVING_BOT_REQUEST);

			} else if ((!isInputRequired || infile.getOriginalFilename().contains(".zip"))
					&& outfile.getOriginalFilename().contains(".zip")
					&& logicfile.getOriginalFilename().contains(".zip")
					&& codefile.getOriginalFilename().contains(".zip")) {
				if (wfOwner != null && wfOwner.contains("["))
					wfOwner = wfOwner.trim().split("\\[")[0];
				
				Gson gson = new Gson();
				JsonParser parser = new JsonParser();
				JsonObject object = (JsonObject) parser.parse(tblRpaRequestStr);// response will be the json String
				TblRpaRequest tblRpaRequest = gson.fromJson(object, TblRpaRequest.class);
				tblRpaRequest.setIsInputRequired(isInputRequired);

				object = (JsonObject) parser.parse(tblRpaBotstagingStr);// response will be the json String
				TblRpaBotstaging tblRpaBotstaging = gson.fromJson(object, TblRpaBotstaging.class);
				
				if(tblRpaBotstaging.getBotlanguage().equalsIgnoreCase("java") || 
						tblRpaBotstaging.getBotlanguage().equalsIgnoreCase("DotNet")	) {
				if (BotStoreUtil.unzipFileFromZipinBLOB(exefile) != null) {
					res = botStoreService.createBotRequestForJavaPython(tblRpaRequest, tblRpaBotstaging, infile,
							outfile, logicfile, codefile, exefile,whlfile, wfOwner);
				} 
				
				else {
					res.setApiSuccess(false);
					res.setResponseMsg(ONLY_EXE_OR_JAR_IS_ALLOWED_TO_UPLOAD);
					LOG.info(ONLY_EXE_OR_JAR_IS_ALLOWED_TO_UPLOAD);
				}}
				else {
					res = botStoreService.createBotRequestForJavaPython(tblRpaRequest, tblRpaBotstaging, infile,
							outfile, logicfile, codefile, exefile,whlfile, wfOwner);
				}
			}
			

			else {
				res.setApiSuccess(false);
				res.setResponseMsg(ONLY_ZIP_FILES_ARE_ALLOWED_TO_UPLOAD);
				LOG.info(ONLY_ZIP_FILES_ARE_ALLOWED_TO_UPLOAD);
			}

		}catch (MultipartException me) {
			LOG.info(ERROR_WHILE_CREATING_RPA_BOT_REQUEST , me.getMessage());
			me.printStackTrace();
			res.setApiSuccess(false);
			res.setResponseMsg(String.format(ERROR_IN_CREATING_RPA_BOT_REQUEST, me.getMessage()));
		} catch (Exception e) {
			LOG.info(ERROR_WHILE_CREATING_RPA_BOT_REQUEST , e.getMessage());
			e.printStackTrace();
			res.setApiSuccess(false);
			res.setResponseMsg(String.format(ERROR_IN_CREATING_RPA_BOT_REQUEST, e.getMessage()));
		}
		

		return res;
	}

	/**
	 * updates any bot which is not yet tested
	 * 
	 * @param tblRpaRequestStr
	 * @param tblRpaBotstagingStr
	 * @param infile
	 * @param botConfigJson
	 * @param outfile
	 * @param logicfile
	 * @param codefile
	 * @param exefile
	 * @return RpaApiResponse
	 * @throws Exception
	 * @throws IOException
	 * @throws SQLException
	 */
	@RequestMapping(value = "/updateBotRequest", method = RequestMethod.POST, consumes = "multipart/form-data")
	public RpaApiResponse updateBotRequest(@RequestParam("tblRpaRequestStr") String tblRpaRequestStr,
			@RequestParam("tblRpaBotstagingStr") String tblRpaBotstagingStr,
			@RequestParam(value = "infile", required = false) MultipartFile infile,
			@RequestParam(value = "botConfigJson", required = false) String botConfigJson,
			@RequestParam(value = "outfile", required = false) MultipartFile outfile,
			@RequestParam(value = "logicfile", required = false) MultipartFile logicfile,
			@RequestParam(value = "codefile", required = false) MultipartFile codefile,
			@RequestParam(value = "exefile", required = false) MultipartFile exefile,
			@RequestParam(value = "whlfile", required = false) MultipartFile whlfile)
			{
		RpaApiResponse res = new RpaApiResponse();

		if (null == tblRpaRequestStr || null == tblRpaBotstagingStr) {
			res.setApiSuccess(false);
			res.setResponseMsg(PROVIDED_REQUEST_CANNOT_BE_NULL);
			LOG.info(PROVIDED_REQUEST_CANNOT_BE_NULL);
		} else {
			try {
				Gson gson = new Gson();
				JsonParser parser = new JsonParser();
				JsonObject object = (JsonObject) parser.parse(tblRpaRequestStr);// response will be the json String

				TblRpaRequest tblRpaRequest = gson.fromJson(object, TblRpaRequest.class);

				object = (JsonObject) parser.parse(tblRpaBotstagingStr);// response will be the json String
				TblRpaBotstaging tblRpaBotstaging = gson.fromJson(object, TblRpaBotstaging.class);

				res = botStoreService.updateBotRequest(tblRpaRequest, tblRpaBotstaging, infile, outfile, logicfile,
						codefile, exefile, whlfile, botConfigJson);
				LOG.info("TblRpaRequest Update Request: SUCCESS and Request ID-->" + res.getData());

			} catch (Exception e) {
				e.printStackTrace();
				res.setApiSuccess(false);
				res.setResponseMsg("Error in Updating RPA request:: " + e.getMessage());
			}
		}

		return res;
	}

	/**
	 * 
	 * @param createdBySignum
	 * @return List<TblRpaRequest>
	 * @throws IOException
	 * @throws SQLException
	 */
	@RequestMapping(value = "/getNewRequestListForDRSP/{createdBySignum}", method = RequestMethod.GET)
	public List<TblRpaRequest> getNewRequestListForDRSP(@PathVariable("createdBySignum") String createdBySignum)
			throws IOException, SQLException {
		List<TblRpaRequest> tblRpaRequestList = null;
		try {
			if (StringUtils.isEmpty(createdBySignum)) {
				return new ArrayList<>();
			} else {
				tblRpaRequestList = botStoreService.getNewRequestList(createdBySignum);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.info("Error in getNewRequestListForDRSP API- Error: {}" , e.getMessage());
			return new ArrayList<>();
		}

		return tblRpaRequestList;
	}

	/**
	 * 
	 * @param developerSignum
	 * @return List<TblRpaBotstaging>
	 * @throws IOException
	 * @throws SQLException
	 */
	@RequestMapping(value = "/getAssignedRequestListForDev/{developerSignum}", method = RequestMethod.GET)
	public List<TblRpaBotstaging> getAssignedRequestListForDev(@PathVariable("developerSignum") String developerSignum)
			throws IOException, SQLException {
		List<TblRpaBotstaging> tblRpaRequestList = null;
		if (StringUtils.isEmpty(developerSignum)) {
			return new ArrayList<>();
		} else {
			tblRpaRequestList = botStoreService.getAssignedRequestListForDev(developerSignum);
		}

		return tblRpaRequestList;
	}

	@RequestMapping(value = "/getNewRequestListForRPAAdmin", method = RequestMethod.GET)
	public List<TblRpaRequest> getNewRequestListForRPAAdmin()  {
		List<TblRpaRequest> tblRpaRequestList = null;
		try {
			tblRpaRequestList = botStoreService.getNewRequestListForRPAAdmin("ALL");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.info("Error in getNewRequestListForRPAAdmin API- Error: {}" , e.getMessage());
			return new ArrayList<TblRpaRequest>();
		}

		return tblRpaRequestList;
	}

	/**
	 * 
	 * @param botId
	 * @return TblRpaDeployedBot
	 * @throws IOException
	 * @throws SQLException
	 */
	
	@RequestMapping(value = "/getBotDetailById/{botId}", method = RequestMethod.GET)
	public Response<GetBotDetailByIdDTO> getBotDetailById(@PathVariable("botId") int botId) throws IOException, SQLException {
		return botStoreService.getBotDetailById(botId);
	}

	@RequestMapping(value = "/getProjectByName/{projName}", method = RequestMethod.GET)
	public List<TblProjects> getProjectByName(@PathVariable("projName") String projName)
			throws IOException, SQLException {
		List<TblProjects> projects = botStoreService.getProjectByName(projName);
		return projects;
	}

	@RequestMapping(value = "/createBotStaging", method = RequestMethod.POST)
	public RpaApiResponse createBotStaging(@RequestBody TblRpaBotstaging tblRpaBotstaging) {
		RpaApiResponse res = botStoreService.createBotStaging(tblRpaBotstaging);
		LOG.info(TBLRPABOTSTAGING_CREATE_REQUEST_SUCCESS);

		return res;
	}

	@RequestMapping(value = "/assignRequestToDev/{reqId}/{userSignum}/{adminSignum}", method = RequestMethod.POST)
	public RpaApiResponse assignRequestToDev(@PathVariable("reqId") int reqId,
			@PathVariable("userSignum") String userSignum, @PathVariable("adminSignum") String adminSignum) {
		return botStoreService.assignRequestToDev(reqId, userSignum, adminSignum);
	}

	@RequestMapping(value = "/updateBotForNonFeasibile/{reqId}/{userSignum}/{desc}", method = RequestMethod.POST)
	public RpaApiResponse updateBotForNonFeasibile(@PathVariable("reqId") int reqId,
			@PathVariable("userSignum") String userSignum, @PathVariable("desc") String desc) {
		RpaApiResponse res = botStoreService.updateBotForNonFeasibile(reqId, userSignum, desc);
		LOG.info(TBLRPABOTSTAGING_CREATE_REQUEST_SUCCESS);

		return res;
	}



	/**
	 * submits code and exe file developed by Bot developer
	 * 
	 * @Modified EHRMSNG
	 * @param tblRpaRequestStr
	 * @param codefile
	 * @param exeFile
	 * @param infile
	 * @param isInputRequired
	 * @return RpaApiResponse
	 */
	@RequestMapping(value = "/submitBotWithLanguageForJavaPython", method = RequestMethod.POST)
	public RpaApiResponse submitBotWithLanguageForJavaPython(
			@RequestParam("tblRpaBotstagingStr") String tblRpaRequestStr,
			@RequestPart("codefile") MultipartFile codefile,
			@RequestPart(value = "exeFile", required = false) MultipartFile exeFile,
			@RequestPart("infile") MultipartFile infile, @RequestParam("isInputRequired") boolean isInputRequired,
			@RequestPart(value = "whlfile", required = false) MultipartFile whlfile) {
		RpaApiResponse res = new RpaApiResponse();

		if (null == tblRpaRequestStr || codefile == null || (isInputRequired && infile == null)) {
			res.setApiSuccess(false);
			res.setResponseMsg(PROVIDED_DATA_CANNOT_BE_BLANK_FOR_UPDATING_BOT_REQUEST);
			LOG.info(PROVIDED_DATA_CANNOT_BE_BLANK_FOR_UPDATING_BOT_REQUEST);
		}
		else {
			Gson gson = new Gson();
			JsonParser parser = new JsonParser();
			JsonObject object = (JsonObject) parser.parse(tblRpaRequestStr);// response will be the json String
			TblRpaBotstaging tblRpaBotstaging = gson.fromJson(object, TblRpaBotstaging.class);
			
			if (tblRpaBotstaging.getBotlanguage().equalsIgnoreCase("java") ||
					tblRpaBotstaging.getBotlanguage().equalsIgnoreCase("DotNet")) {
				if (exeFile.getOriginalFilename().contains(".zip") && codefile.getOriginalFilename().contains(".zip")
						&& (!isInputRequired || infile.getOriginalFilename().contains(".zip"))) {
					if (BotStoreUtil.unzipFileFromZipinBLOB(exeFile) != null) {
						
								res = botStoreService.submitBotWithLanguageForJavaPython(tblRpaBotstaging, codefile, exeFile,
										infile, isInputRequired, whlfile);
								LOG.info(TBLRPABOTSTAGING_CREATE_REQUEST_SUCCESS);
							
						} else {
							res.setApiSuccess(false);
							res.setResponseMsg(ONLY_EXE_OR_JAR_IS_ALLOWED_TO_UPLOAD);
							LOG.info(ONLY_EXE_OR_JAR_IS_ALLOWED_TO_UPLOAD);
						}
					}
				else {
					res.setApiSuccess(false);
					res.setResponseMsg(ONLY_ZIP_FILES_ARE_ALLOWED_TO_UPLOAD);
					LOG.info(ONLY_ZIP_FILES_ARE_ALLOWED_TO_UPLOAD);
				}
			}
		
			else {
				if (whlfile.getOriginalFilename().contains(".whl") && codefile.getOriginalFilename().contains(".zip")
						&& (!isInputRequired || infile.getOriginalFilename().contains(".zip"))) {
					 
								res = botStoreService.submitBotWithLanguageForJavaPython(tblRpaBotstaging, codefile, exeFile,
										infile, isInputRequired, whlfile);
								LOG.info(TBLRPABOTSTAGING_CREATE_REQUEST_SUCCESS);
				}

				else {
					res.setApiSuccess(false);
					res.setResponseMsg(ONLY_ZIP_FILES_ARE_ALLOWED_TO_UPLOAD);
					LOG.info(ONLY_ZIP_FILES_ARE_ALLOWED_TO_UPLOAD);
				}
			}
		}
		return res;
	}

	// Method to update TESTED status by Developer and its called by SoftHumanRPA
	// Testing jar:
	// and used for TESTED
	@RequestMapping(value = "/updateBotTestResults/{reqId}/{userSignum}/{status}", method = RequestMethod.POST)
	public RpaApiResponse updateBotTestResults(@PathVariable("reqId") Integer reqId,
			@PathVariable("userSignum") String userSignum, @PathVariable("status") String status) {
		RpaApiResponse res = new RpaApiResponse();

		if (null == reqId || null == userSignum || null == status) {
			res.setApiSuccess(false);
			res.setResponseMsg("Provided data cannot be blank/null for updating BOT Test Results.");
			LOG.info("Provided data cannot be blank/null for updating BOT Test Results.");
		} else {
			res = botStoreService.updateBotTestResults(reqId, userSignum, status);
		}

		LOG.info("UpdateBotTestResults Request: SUCCESS");
		return res;
	}

	// used for AUDITPASS, AUDITFAIL
	@RequestMapping(value = "/updateBotTestResultsForAuditUAT/{reqId}/{auditorSignum}/{status}", method = RequestMethod.POST)
	public RpaApiResponse updateBotTestResultsForAuditUAT(@PathVariable("reqId") int reqId,
			@PathVariable("auditorSignum") String auditorSignum, @PathVariable("status") String status) {
		RpaApiResponse res = botStoreService.updateBotTestResultsForAuditUAT(reqId, auditorSignum, status);
		LOG.info("updateBotTestResultsForAuditUAT Request: SUCCESS");

		return res;
	}

	// Used for BOT DEPLOYEMENT
	@RequestMapping(value = "/deployBot/{reqId}/{signum}", method = RequestMethod.POST)
	public RpaApiResponse deployBot(@PathVariable("reqId") int reqId, @PathVariable("signum") String signum) {
		RpaApiResponse res = new RpaApiResponse();

		res = botStoreService.deployBot(reqId, signum);

		return res;
	}

	// Used for BOT DEPLOYEMENT
	@RequestMapping(value = "/approveBot/{reqId}/{signum}", method = RequestMethod.POST)
	public RpaApiResponse approveBot(@PathVariable("reqId") int reqId, @PathVariable("signum") String signum) {
		RpaApiResponse res = new RpaApiResponse();

		res = botStoreService.approveBot(reqId, signum);

		return res;
	}

	// Used for BOT DEPLOYEMENT
	@RequestMapping(value = "/changeDeployedBotStatus/{botId}/{signum}/{status}", method = RequestMethod.POST)
	public RpaApiResponse changeDeployedBotStatus(@PathVariable("botId") int botId,
			@PathVariable("signum") String signum, @PathVariable("status") int status) {
		RpaApiResponse res = new RpaApiResponse();
		res = botStoreService.changeDeployedBotStatus(botId, signum, status);
		LOG.info("changeDeployedBotStatus Request: SUCCESS");
		return res;
	}

	// Used for BOT DEPLOYEMENT
	@RequestMapping(value = "/deleteAutomationRequest/{reqId}/{signum}", method = RequestMethod.POST)
	public RpaApiResponse deleteAutomationRequest(@PathVariable("reqId") int reqId,
			@PathVariable("signum") String signum) {
		RpaApiResponse res = botStoreService.deleteAutomationRequest(reqId, signum);

		LOG.info("deleteAutomationRequest Request: SUCCESS");
		return res;
	}
	
	/**
	 * @author edyudev
	 * @apiNote mark completed to failed test Bot Execution 
	 * @param Integer rpaRequestId
	 * @param String signum
	 * @return RpaApiResponse 
	 **/
	@PostMapping(path = "/stopInprogressBot/{reqId}/{signum}")
	public ResponseEntity<RpaApiResponse> stopInprogressBot(@PathVariable("reqId") Integer rpaRequestId, @PathVariable("signum") String  signum) {
		return botStoreService.stopInprogressBot(rpaRequestId, signum);
	}

	// Below methods added for RPA Services::
	// used by RPA for getting BOT details:
	@RequestMapping(value = "/getBotStageDetailsForTesting/{reqId}/{status}", method = RequestMethod.GET)
	public GetBotStageDetailsForTestingDTO getBotStageDetailsForTesting(@PathVariable("reqId") Integer reqId,
			@PathVariable("status") String status) {

		GetBotStageDetailsForTestingDTO stag = null;

		if (null == reqId || null == status) {
			LOG.info("Provided data cannot be blank/null for getBotStageDetailsForTesting(reqId, status).");
		} else {
			stag = botStoreService.getBotStageDetailsForTesting(reqId, status);
			LOG.info("getBotStageDetailsForTesting Request: SUCCESS");
		}
		return stag;
	}

	// used by RPA for downloading BOT data files:
	@RequestMapping(value = "/downloadFileAsStream/{reqId}/{fileType}", method = RequestMethod.GET, produces = "application/zip")
	public void downloadBotFileAsStream(HttpServletResponse response, @PathVariable("reqId") Integer reqId,
			@PathVariable("fileType") String fileType) {
		RpaApiResponse res = new RpaApiResponse();

		if (null == reqId || null == fileType) {
			res.setApiSuccess(false);
			res.setResponseMsg("Provided data cannot be blank/null for downloadFileAsStream(reqId, status).");
			LOG.info("Provided data cannot be blank/null for downloadFileAsStream(reqId, status).");
		} else {
			botStoreService.downloadFileAsStream(response, reqId, fileType);
			LOG.info("downloadFileAsStream Request: SUCCESS");
		}
	}

	// used by RPA for downloading BOT data files as a stream:
	@RequestMapping(value = "/downloadFile/{reqId}/{fileType}", method = RequestMethod.GET)
	public String downloadFile(@PathVariable("reqId") Integer reqId, @PathVariable("fileType") String fileType) {
		String filePath = null;

		if (null == fileType) {
			filePath = "File Type cannot be blank/null for downloading Bot Store files.";
			LOG.info("File Type cannot be blank/null for downloading Bot Store files.");
		} else {
			filePath = botStoreService.downloadFile(reqId, fileType);
			LOG.info("Download File Path: {}" , filePath);
		}
		return filePath;
	}

	@RequestMapping(value = "/downloadBotAsStream/{reqId}", method = RequestMethod.GET)
	public void downloadBotAsStream(HttpServletResponse response, @PathVariable("reqId") Integer reqId) {

		if (null == reqId) {
			LOG.info("Bot Id cannot be blank/null for downloading Bot Store files.");
		} else {
			botStoreService.downloadBotAsStream(response, reqId);
		}
	}

	// Used for BOT Testing
	@RequestMapping(value = "/createBotTestingRequest/{reqId}/{signum}", method = RequestMethod.POST)
	public RpaApiResponse createBotTestingRequest(@PathVariable("reqId") Integer reqId,
			@PathVariable("signum") String signum) {
		RpaApiResponse res = new RpaApiResponse();

		if (null == reqId || signum == null) {
			res.setApiSuccess(false);
			res.setResponseMsg("Provided data cannot be blank/null for saving the Testing Details!!");
		}

		LOG.info("createBotTestingRequest Request: SUCCESS");
		return botStoreService.createBotTestingRequest(reqId, signum);
	}

	@RequestMapping(value = "/getTestingDataByReq/{reqId}/{signum}", method = RequestMethod.GET)
	public TblRpaBottesting getTestingDataByReq(@PathVariable("reqId") Integer reqId,
			@PathVariable("signum") String signum) {
		return botStoreService.getTestingDataByReq(reqId, signum);
	}

	// Used for BOT Testing update results:
	@RequestMapping(value = "/updateBotTestingResults/{testId}/{isTestingSuccessful}", method = RequestMethod.POST)
	public RpaApiResponse updateBotTestingResults(@PathVariable("testId") Integer testId,
			@PathVariable("isTestingSuccessful") Integer isTestingSuccessful) {
		RpaApiResponse res = new RpaApiResponse();

		if (null == testId || null == isTestingSuccessful) {
			res.setApiSuccess(false);
			res.setResponseMsg("Provided data cannot be blank/null for updating the Testing Details!!");
			throw new ApplicationException(500,
					"Provided data cannot be blank/null for updating the Testing Details!!");
		}

		LOG.info("updateBotTestingResults Request: SUCCESS");
		return botStoreService.updateBotTestingResults(testId, isTestingSuccessful);
	}

	@RequestMapping(value = "/changeRunOnServerStatus/{botId}/{signum}/{status}", method = RequestMethod.POST)
	public RpaApiResponse changeRunOnServerStatus(@PathVariable("botId") int botId,
			@PathVariable("signum") String signum, @PathVariable("status") int status) {
		RpaApiResponse res = botStoreService.changeRunOnServerStatus(botId, signum, status);
		LOG.info("changeRunOnServerStatus Request: SUCCESS");
		return res;
	}

	@RequestMapping(value = "/changeAuditPassFail/{botId}/{signum}/{status}", method = RequestMethod.POST)
	public RpaApiResponse changeAuditPassFail(@PathVariable("botId") int botId, @PathVariable("signum") String signum,
			@PathVariable("status") int status) {
		RpaApiResponse res = botStoreService.changeAuditPassFail(botId, signum, status);
		LOG.info("changeAuditPassFail Request: SUCCESS");
		return res;
	}

	/**
	 * 
	 * @param status
	 * @param signum
	 * @return Map<String, String>
	 */
	@RequestMapping(value = "/getTestingDataByStatusSignum/{status}/{signum}", method = RequestMethod.GET)
	public Map<String, String> getTestingDataByStatusSignum(@PathVariable("status") String status,
			@PathVariable("signum") String signum) {

		return botStoreService.getTestingDataByStatusSignum(status, signum);
	}

	@RequestMapping(value = "v1/getDesktopAppVersion", method = RequestMethod.GET)
	public Response<List<DesktopAppResponseModel>>getDesktopAppVersionv1() {
		return botStoreService.getDesktopAppVersionv1();
	}

	@RequestMapping(value = "/botDownload", method = RequestMethod.POST)
	public byte[] botDownload(@RequestBody BotDetail botDetail) {

		byte[] file = botStoreService.botDownload(botDetail);
		LOG.info("botDownload Request: SUCCESS");

		return file;
	}

	@RequestMapping(value = "/uploadBotOutput", method = RequestMethod.POST)
	public boolean uploadBotOutput(@RequestBody BotDetail botDetail) {
		boolean status = false;

		status = botStoreService.uploadBotOutput(botDetail);
		LOG.info("uploadBotOutput Request: SUCCESS");

		return status;
	}

	@RequestMapping(value = "/botOutputDownload", method = RequestMethod.POST)
	public BotDetail botOutputDownload(@RequestBody BotDetail botDetail) {
		BotDetail botData = botStoreService.botOutputDownload(botDetail);
		LOG.info("botOutputDownload Request: SUCCESS");

		return botData;
	}

	@RequestMapping(value = "/downloadBotOutputByFileName/{outputFileName}", method = RequestMethod.GET)
	public byte[] downloadBotOutputByFileName(@PathVariable("outputFileName") String outputFileName) {
		byte[] fileArr = null;

		fileArr = botStoreService.downloadBotOutputByFileName(outputFileName);
		LOG.info("downloadBotOutputByFileName Request: SUCCESS");
		return fileArr;
	}

	@RequestMapping(value = "/debugApi/{botId}", method = RequestMethod.GET)
	public void debugApi(@PathVariable("botId") int botId) {
		botStoreService.debugApi(botId);
		LOG.info("debugApi Request: SUCCESS");

	}

	@RequestMapping(value = "/submitBotWithLanguageForOthers", method = RequestMethod.POST)
	public RpaApiResponse submitBotWithLanguageForOthers(@RequestParam("tblRpaBotstagingStr") String tblRpaRequestStr,
			@RequestParam("referenceid") int referenceid) {
		RpaApiResponse res = new RpaApiResponse();

		if (null == tblRpaRequestStr) {
			res.setApiSuccess(false);
			res.setResponseMsg(PROVIDED_DATA_CANNOT_BE_BLANK_FOR_UPDATING_BOT_REQUEST);
			LOG.info(PROVIDED_DATA_CANNOT_BE_BLANK_FOR_UPDATING_BOT_REQUEST);
		} else {
			Gson gson = new Gson();
			JsonParser parser = new JsonParser();
			JsonObject object = (JsonObject) parser.parse(tblRpaRequestStr);// response will be the json String
			TblRpaBotstaging tblRpaBotstaging = gson.fromJson(object, TblRpaBotstaging.class);
			res = botStoreService.submitBotWithLanguageForOthers(tblRpaBotstaging, referenceid);
			LOG.info(TBLRPABOTSTAGING_CREATE_REQUEST_SUCCESS);
		}

		return res;
	}

	@RequestMapping(value = "/botDownloadDB", method = RequestMethod.POST)
	public BotDetail botDownloadDB(@RequestBody BotDetail botDetail) {
		return botStoreService.botDownloadDB(botDetail);
	}

	
	@RequestMapping(value = "/botMigrationDB", method = RequestMethod.POST)
	public String botMigrationDB(@RequestBody BotDetail botDetail) {
		String status = botStoreService.botMigrationDB(botDetail);
		if (status.contains("SUCCESS")) {
			LOG.info("botMigrate Request: SUCCESS");
		} else {
			LOG.info("botMigrate Request: FAIL");
		}

		return status;
	}

	@RequestMapping(value = "/botMigrationDBNew", method = RequestMethod.POST)
	public String botMigrationDBNew(@RequestBody BotDetail botDetail) {
		String status = botStoreService.botMigrationDBNew(botDetail);
		if (status.contains(AppConstants.SUCCESS_CAPS)) {
			LOG.info("botMigrate Request: SUCCESS");
		} else {
			LOG.info("botMigrate Request: FAIL");
		}

		return status;
	}

	@RequestMapping(value = "/uploadBotInputOutput", method = RequestMethod.POST)
	public String uploadBotInputOutput(@RequestBody BotDetail botDetail) {
		String status = botStoreService.uploadBotInputOutput(botDetail);
		if (status.contains(AppConstants.SUCCESS_CAPS)) {
			LOG.info("botUpload InputOutput Request: SUCCESS");
		} else {
			LOG.info("botUpload InputOutput Request: FAIL");
		}

		return status;
	}

	@RequestMapping(value = "/uploadUpdatedBotOutput", method = RequestMethod.POST, consumes = "multipart/form-data")
	public String uploadUpdatedBotOutput(@RequestParam("botDetail") String botDetails,
			@RequestPart("outFile") MultipartFile outFile) {

		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(botDetails);// response will be the json String
		BotDetail botDetail = gson.fromJson(object, BotDetail.class);

		String status = botStoreService.uploadUpdatedBotOutput(botDetail, outFile);
		if (status.contains(AppConstants.SUCCESS_CAPS)) {
			LOG.info("botUpdate Output Request: SUCCESS");
		} else {
			LOG.info("botUpdate Output Request: FAIL");
		}

		return status;
	}

	@RequestMapping(value = "/writeFileResponse", method = RequestMethod.GET)
	private DownloadTemplateModel writeFileResponse(HttpServletResponse response, @RequestParam("botId") String botId,
			@RequestParam("botName") String botName) throws IOException {
		DownloadTemplateModel downloadTemplateModel = new DownloadTemplateModel();
		try {
			BotDetail botDetail = new BotDetail();
			botDetail.setBotId(botId);
			botDetail.setBotName(botName);

			byte[] file = botStoreService.botDownloadDBFile(botDetail);
			downloadTemplateModel.setpFileContent(file);
			downloadTemplateModel.setpFileName(botName);
		} catch (Exception e) {
			LOG.info("Download Request: FAIL");
			e.printStackTrace();
		}
		return downloadTemplateModel;
	}

	@RequestMapping(value = "/writeOutFileResponse", method = RequestMethod.GET)
	private DownloadTemplateModel writeOutFileResponse(HttpServletResponse response,
			@RequestParam("fileName") String fileName) throws IOException {
		DownloadTemplateModel downloadTemplateModel = new DownloadTemplateModel();
		try {

			byte[] file = botStoreService.botDownloadDBOUTFile(fileName);
			downloadTemplateModel.setpFileContent(file);
			downloadTemplateModel.setpFileName(fileName);
		} catch (Exception e) {
			LOG.info("Download Request: FAIL");
			e.printStackTrace();
		}
		return downloadTemplateModel;
	}

	@RequestMapping(value = "/getDesktopVersionBySignum", method = RequestMethod.GET)
	public UserWiseDesktopVersionModel getDesktopVersionBySignum(@RequestParam("signum") String signum) {
		return this.botStoreService.getDesktopVersionBySignum(signum);
	}

	@RequestMapping(value = "/saveDesktopVersionBySignum", method = RequestMethod.POST)
	public void saveDesktopVersionBySignum(@RequestBody UserWiseDesktopVersionModel model) {
		this.botStoreService.saveDesktopVersionBySignum(model);
	}

	/**
	 * Update Input File status by WFOwner
	 * 
	 * @param botId
	 * @param isInputRequired
	 * @param workFlowOwner
	 * @return RpaApiResponse
	 * @throws Exception
	 * @throws IOException
	 * @throws SQLException
	 */
	@RequestMapping(value = "/updateInputFileStatus", method = RequestMethod.POST)
	public RpaApiResponse updateInputFileStatus(@RequestParam(value = "botId") int botId,
			@RequestParam(value = "isInputRequired") boolean isInputRequired,
			@RequestParam(value = "workFlowOwner") String workFlowOwner) throws Exception, IOException, SQLException {

		LOG.info("Input file status updated: SUCCESS.");
		return botStoreService.updateInputFileStatus(botId, isInputRequired, workFlowOwner);
	}
	@RequestMapping(value = "/getDataFromRedis", method = RequestMethod.GET)
	public Object getDataFromRedis(@RequestParam(value = "key") String key) {
		return this.botStoreService.getDataFromRedis(key);
	}
	
	@RequestMapping(value = "/deleteDataFromRedis", method = RequestMethod.DELETE)
	public void deleteDataFromRedis(@RequestParam(value = "key") String key) {
		 this.botStoreService.deleteDataFromRedis(key);
	}
	
	@RequestMapping(value = "/getLibraryVersionBySignum", method = RequestMethod.GET)
	public Response<UserLibraryVersionModel> getLibraryVersionBySignum(@RequestParam("signum") String signum) {
		return this.botStoreService.getLibraryVersionBySignum(signum);
	}
	
	@RequestMapping(value = "/saveLibraryVersionBySignum", method = RequestMethod.POST)
	public Response<Boolean> saveLibraryVersionBySignum(@RequestBody UserLibraryVersionModel userLibraryVersion) {
		return botStoreService.saveLibraryVersionBySignum(userLibraryVersion);
	}
	
	/**
	 * To fetch list for all library versions between deployed and user library versions
	 * 
	 * @param version
	 * @return List 
	 */
	@RequestMapping(value = "/getLibraryVersionGap", method = RequestMethod.GET)
	public Response<List<DesktopLibraryResponseModel>> getLibraryVersionGap(@RequestParam("version") float version) {
		return this.botStoreService.getLibraryVersionGap(version);
	}
	/*
	 * getDesktopAppVersion is from R15.0 Codebase
	 */
	@RequestMapping(value = "/getDesktopAppVersion", method = RequestMethod.GET)
	public DesktopAppResponseModel getDesktopAppVersion() {
		return botStoreService.getDesktopAppVersion();
	}
	
	/*
	 *  distinct LanguageBaseVersion & LanguageBaseVersionID of Python
	 *  @return List
	 */
	@RequestMapping(value = "/getLanguageBaseVersion", method = RequestMethod.GET)
	public Response<List<LanguageBaseVersionModel>> getLanguageBaseVersion() {
		return this.botStoreService.getLanguageBaseVersion();
	}
	
	@RequestMapping(value = "/getPythonInstallerUrl", method = RequestMethod.GET)
	public Response<URI> getPythonInstallerUrl(@RequestParam("pythonBaseVersion") String pythonBaseVersion) {
		return this.botStoreService.getPythonInstallerUrl(pythonBaseVersion);
	}

}
