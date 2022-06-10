package com.ericsson.isf.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ericsson.isf.dao.ActivityMasterDAO;
import com.ericsson.isf.dao.CompetenceDAO;
import com.ericsson.isf.exception.ApplicationException;
import com.ericsson.isf.helper.FileCSVHelper;
import com.ericsson.isf.model.BulkUploadModel;
import com.ericsson.isf.model.CompetenceServiceAreaModel;
import com.ericsson.isf.model.CompetenceTrainingModel;
import com.ericsson.isf.model.DownloadTemplateModel;
import com.ericsson.isf.model.EmployeeModel;
import com.ericsson.isf.model.Response;
import com.ericsson.isf.model.UserCompetenceModel;
import com.ericsson.isf.util.AppConstants;
import com.ericsson.isf.util.AppUtil;
import com.ericsson.isf.util.ApplicationMessages;
import com.ericsson.isf.util.CompetenceStatusEnum;
import com.ericsson.isf.util.FileUtil;
import com.ericsson.isf.util.IsfCustomIdInsert;

/**
 * 
 * @author eakinhm
 *
 */
@Service
public class CompetenceService {
	private final Logger LOG = LoggerFactory.getLogger(CompetenceService.class);

	@Autowired
	private CompetenceDAO competenceDAO;

	@Autowired
	private AppService appService;

	@Autowired
	private OutlookAndEmailService emailService;

	@Autowired
	private ActivityMasterDAO activityMasterDAO;

	@Autowired
	private IsfCustomIdInsert isfCustomIdInsert;
	
	public static int oldCompetenceID;

	public List<Map<String, Object>> getDomain(int competenceTypeID) {

		return competenceDAO.getDomain(competenceTypeID);
	}

	public List<Map<String, Object>> getTechnology(int competenceTypeID, int subdomainID) {

		return competenceDAO.getTechnology(competenceTypeID, subdomainID);
	}

	public List<Map<String, Object>> getVendor(int competenceTypeID, int subdomainID, int technologyID) {

		return competenceDAO.getVendor(competenceTypeID, subdomainID, technologyID);
	}

	public List<Map<String, Object>> getCompetence() {
		return competenceDAO.getCompetence();
	}

	public List<Map<String, Object>> getServiceArea(int competenceTypeID, int subdomainID, int technologyID,
			int vendorID) {
		return competenceDAO.getServiceArea(competenceTypeID, subdomainID, technologyID, vendorID);

	}

	public List<Map<String, Object>> getBaseline() {
		return competenceDAO.getBaseline();
	}

	public Response<List<Map<String, Object>>> getAmbition(String flag, int competenceGradeID) {
		if (competenceGradeID == 5) {
			flag = "same";
		}
		Response<List<Map<String, Object>>> response = new Response<List<Map<String, Object>>>();
		try {
			response.setResponseData(competenceDAO.getAmbition(flag, competenceGradeID));
		} catch (Exception e) {
			response.addFormError(e.getMessage());
		}
		return response;
	}

	@Transactional("transactionManager")
	public Response<Void> insertCompetenceData(List<UserCompetenceModel> userCompetenceModel) {
		Response<Void> apiResponse = new Response<Void>();
		List<UserCompetenceModel> userCompetenceModel1 = new ArrayList<UserCompetenceModel>();
		List<UserCompetenceModel> oldUserCompetenceModelList = new ArrayList<UserCompetenceModel>();
		int errorCount = 0, successCount = 0;
		String status = StringUtils.EMPTY;
		boolean isSuccess = false;
		
		CheckForUpdateOrDelete(userCompetenceModel, apiResponse);
		///////////// all thing is done beside this ////////////////////////////////////
		for (UserCompetenceModel userCompetence : userCompetenceModel) {
			isSuccess = false;
			try {
				UserCompetenceModel newUserCompetenceModel = new UserCompetenceModel();
				UserCompetenceModel oldUserCompetenceModel = null;
				status = userCompetence.getStatus();
				// in if statement this only add or update will happen
				if (userCompetence.getIsEditable()) {
					newUserCompetenceModel = userCompetence;
					HashMap<String, Object> deliveryCompetanceDetail = competenceDAO
							.getDeliveryCompetanceDetail(userCompetence);
					if (MapUtils.isNotEmpty(deliveryCompetanceDetail)) {
						newUserCompetenceModel
								.setDeliveryCompetanceID((int) deliveryCompetanceDetail.get("DeliveryCompetanceID"));
					}

					if (userCompetence.getId() != 0) {
						oldUserCompetenceModel = getUserCompetenceModelForSystemId(userCompetence.getId());
					}

					validateUserCompetence(newUserCompetenceModel, deliveryCompetanceDetail, oldUserCompetenceModel);

				} else {
					// this else condition for delete or Sent To Manager.
					newUserCompetenceModel = competenceDAO.getUserCompetenceRow(userCompetence.getId());
					newUserCompetenceModel.setCompetenceGradeID(newUserCompetenceModel.getBaseline());
					newUserCompetenceModel.setChangedBy(userCompetence.getChangedBy());
				}

				newUserCompetenceModel.setParentSystemID(userCompetence.getId());
				newUserCompetenceModel.setStatus(CompetenceStatusEnum.getDisplayStatus(userCompetence.getStatus()));
				newUserCompetenceModel.setLmSignum(
						this.competenceDAO.getManagerSignum(newUserCompetenceModel.getRequestedBySignum()));
				newUserCompetenceModel
						.setSlmSignum(this.competenceDAO.getManagerSignum(newUserCompetenceModel.getLmSignum()));
                

				// end of delete and sent to manager case
				isSuccess = competenceDAO.insertCompetenceData(newUserCompetenceModel);
				if (isSuccess) {
					successCount++;
					userCompetenceModel1.add(newUserCompetenceModel);
					oldUserCompetenceModelList.add(oldUserCompetenceModel);
				} else {
					errorCount++;
				}
			} catch (ApplicationException ex) {
				apiResponse.addFormError(ex.getMessage());
				apiResponse.addErrorsDetail("Error Description", ex.getMessage());
				apiResponse.addErrorsDetail("Error Code", ex.getErrorCode());
			} catch (Exception e) {
				errorCount++;
				e.printStackTrace();
				continue;
			}
		}
/////////////end of all thing is done beside this ////////////////////////////////////

		if (successCount != 0) {

			apiResponse.addFormMessage(
					String.format(CompetenceStatusEnum.getEnumValue(status).getSuccessMsg(), successCount));

			if (CompetenceStatusEnum.SENT_TO_MANAGER.getDisplayStatus().equalsIgnoreCase(status)
					&& !userCompetenceModel1.get(0).getIsEditable()) {
				emailService.sendMail(AppConstants.ISF_COMPETENCE_REQUEST,
						enrichMailforCompetenceRequest(userCompetenceModel1));
			}
			if (CompetenceStatusEnum.APPROVED.getDisplayStatus().equalsIgnoreCase(status)
					|| CompetenceStatusEnum.REJECTED.getDisplayStatus().equalsIgnoreCase(status)) {
				emailService.sendMail(AppConstants.ISF_COMPETENCE_APPROVE_REJECT,
						enrichMailforApproveReject(userCompetenceModel1));
			}
			if (CompetenceStatusEnum.DELETED.getDisplayStatus().equalsIgnoreCase(status)
					&& userCompetenceModel1.get(0).getChangedBy().equalsIgnoreCase("LM")) {
				emailService.sendMail(AppConstants.ISF_COMPETENCE_INITIATE_DELETE,
						enrichMailforCompetenceEdit_Initiate_Delete(userCompetenceModel1));
			}
			if (CompetenceStatusEnum.SENT_TO_MANAGER.getDisplayStatus().equalsIgnoreCase(status)
					&& userCompetenceModel1.get(0).getIsEditable() && userCompetenceModel1.get(0).getId() != 0) {
				emailService.sendMail(AppConstants.ISF_COMPETENCE_EDIT,
						enrichMailforCompetenceEdit(userCompetenceModel1, oldUserCompetenceModelList));
			}
		}
		if (errorCount != 0) {
			apiResponse
					.addFormError(String.format(CompetenceStatusEnum.getEnumValue(status).getErrorMsg(), errorCount));
		}

		return apiResponse;

	}
	
	private boolean isOnlyGradeUpgardeBetweenUserModels(UserCompetenceModel newUserCompetenceModel,UserCompetenceModel oldUserCompetenceModel) {
		if(newUserCompetenceModel.getCompetanceID()==oldUserCompetenceModel.getCompetanceID() 
				&& newUserCompetenceModel.getDomainID()==oldUserCompetenceModel.getDomainID() 
				&& newUserCompetenceModel.getTechnologyID()==oldUserCompetenceModel.getTechnologyID()
				&& newUserCompetenceModel.getVendorID() == oldUserCompetenceModel.getVendorID()
				&& newUserCompetenceModel.getCompetenceGradeID() != oldUserCompetenceModel.getCompetenceGradeID()) {
			return true;
		}
		
		return false;
	}

	private Response<Void> CheckForUpdateOrDelete(List<UserCompetenceModel> userCompetenceModel,
			Response<Void> apiResponse) {
		for (UserCompetenceModel userCompetence : userCompetenceModel) {
			try {

				if (!userCompetence.getIsEditable()) {
					UserCompetenceModel newUserCompetenceModel = competenceDAO
							.getUserCompetenceRow(userCompetence.getId());
					if (!newUserCompetenceModel.isActive()) {

						throw new ApplicationException(101,
								"One of the request has been modified, please refresh the page!");
					}
				}
			} catch (ApplicationException ex) {
				apiResponse.addFormError(ex.getMessage());
				apiResponse.addErrorsDetail("Error Description", ex.getMessage());
				apiResponse.addErrorsDetail("Error Code", ex.getErrorCode());
				return apiResponse;
			} catch (Exception e) {
				e.printStackTrace();
				apiResponse.addFormError(e.getMessage());
				return apiResponse;
			}
		}
		return apiResponse;
	}

	private UserCompetenceModel getUserCompetenceModelForSystemId(int systemID) {
		Map<String, Object> map = competenceDAO.getCompetenceDetailsBySystemID(systemID);
		UserCompetenceModel userCompetenceModel = new UserCompetenceModel();
		userCompetenceModel.setCompetanceID((int) map.get("CompetenceID"));
		userCompetenceModel.setVendor((String) map.get("vendor"));
		userCompetenceModel.setVendorID((int) map.get("VendorID"));
		userCompetenceModel.setCompetenceGrade((String) map.get("competenceGrade"));
		userCompetenceModel.setCompetencyServiceArea((String) map.get("competencyServiceArea"));
		userCompetenceModel.setCompetenceUpgrade((String) map.get("competenceUpgrade"));
		userCompetenceModel.setTechnology((String) map.get("technology"));
		userCompetenceModel.setTechnologyID((int) map.get("TechnologyID"));
		userCompetenceModel.setDomain((String) map.get("domain"));
		userCompetenceModel.setDomainID((int) map.get("DomainID"));
		userCompetenceModel.setCompetenceGradeID((int) map.get("CompetenceGRADEID"));
		userCompetenceModel.setCompetenceUpgradeID((int) map.get("CompetenceUpgradeID"));
		return userCompetenceModel;
	}

	private Map<String, Object> enrichMailforCompetenceEdit(List<UserCompetenceModel> userCompetenceModel1,
			List<UserCompetenceModel> oldUserCompetenceModelList) {
		List<UserCompetenceModel> newUserCompetenceModelList = getCompetenceDataForMail(userCompetenceModel1);
		EmployeeModel employee = activityMasterDAO
				.getEmployeeBySignum(newUserCompetenceModelList.get(0).getRequestedBySignum());
		EmployeeModel manager = activityMasterDAO.getEmployeeBySignum(newUserCompetenceModelList.get(0).getLmSignum());
		String toMailId = StringUtils.EMPTY;
		String ccMailId = StringUtils.EMPTY;
		String greetingName = StringUtils.EMPTY;
		String editedBy = StringUtils.EMPTY;
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("newRequestData", newUserCompetenceModelList);
		data.put("oldRequestData", oldUserCompetenceModelList);
		data.put("managerName", manager.getEmployeeName());
		data.put("env", this.competenceDAO.getDeployedEnv());
		data.put("requestedByName", employee.getEmployeeName());
		if (newUserCompetenceModelList.get(0).getChangedBy().equalsIgnoreCase("engineer")) {
			toMailId = manager.getEmployeeEmailId();
			ccMailId = employee.getEmployeeEmailId();
			greetingName = manager.getEmployeeName();
			editedBy = employee.getEmployeeName();
		} else {
			toMailId = employee.getEmployeeEmailId();
			ccMailId = manager.getEmployeeEmailId();
			greetingName = employee.getEmployeeName();
			editedBy = manager.getEmployeeName();
		}
		data.put("editedBy", editedBy);
		data.put("greetingName", greetingName);
		data.put(AppConstants.CUSTOM_NOTIFICATIONS_TO, toMailId);
		data.put(AppConstants.CUSTOM_NOTIFICATIONS_CC, ccMailId);

		return data;
	}

	@Transactional("transactionManager")
	private Map<String, Object> enrichMailforApproveReject(List<UserCompetenceModel> userCompetenceModel1) {
		List<UserCompetenceModel> userCompetenceModel2 = getCompetenceDataForMail(userCompetenceModel1);
		EmployeeModel employee = activityMasterDAO
				.getEmployeeBySignum(userCompetenceModel2.get(0).getRequestedBySignum());
		EmployeeModel manager = activityMasterDAO.getEmployeeBySignum(userCompetenceModel2.get(0).getLmSignum());
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("request", userCompetenceModel2);
		data.put("status", userCompetenceModel2.get(0).getStatus());
		data.put("managerName", manager.getEmployeeName());
		data.put("requestedByName", employee.getEmployeeName());
		data.put("env", this.competenceDAO.getDeployedEnv());
		data.put(AppConstants.CUSTOM_NOTIFICATIONS_TO, employee.getEmployeeEmailId());
		data.put(AppConstants.CUSTOM_NOTIFICATIONS_CC, manager.getEmployeeEmailId());
		return data;
	}

	@Transactional("transactionManager")
	private Map<String, Object> enrichMailforCompetenceEdit_Initiate_Delete(
			List<UserCompetenceModel> userCompetenceModel1) {
		List<UserCompetenceModel> userCompetenceModel2 = getCompetenceDataForMail(userCompetenceModel1);
		EmployeeModel employee = activityMasterDAO
				.getEmployeeBySignum(userCompetenceModel2.get(0).getRequestedBySignum());
		EmployeeModel manager = activityMasterDAO.getEmployeeBySignum(userCompetenceModel2.get(0).getLmSignum());
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("request", userCompetenceModel2);
		data.put("status", userCompetenceModel2.get(0).getStatus());
		data.put("managerName", manager.getEmployeeName());
		data.put("requestedByName", employee.getEmployeeName());
		data.put("env", this.competenceDAO.getDeployedEnv());
		if (userCompetenceModel2.get(0).getChangedBy().equalsIgnoreCase("LM")) {
			data.put(AppConstants.CUSTOM_NOTIFICATIONS_TO, employee.getEmployeeEmailId());
			data.put(AppConstants.CUSTOM_NOTIFICATIONS_CC, manager.getEmployeeEmailId());
			data.put("deletedBy", manager.getEmployeeName());
			data.put("user", employee.getEmployeeName());
		}
		return data;

	}

	@Transactional("transactionManager")
	private List<UserCompetenceModel> getCompetenceDataForMail(List<UserCompetenceModel> userCompetenceModel1) {
		List<UserCompetenceModel> userCompetenceModel2 = new ArrayList<UserCompetenceModel>();
		for (UserCompetenceModel ucm : userCompetenceModel1) {
			ucm.setVendor(this.activityMasterDAO.getVendorDetailsByID(ucm.getVendorID(), null).get(0).getVendor());
			ucm.setCompetenceGrade(this.competenceDAO.getCompetenceGradeById(ucm.getCompetenceGradeID()));
			ucm.setCompetencyServiceArea(this.competenceDAO.getCompetenceServiceAreaById(ucm.getCompetanceID()));
			ucm.setCompetenceUpgrade(this.competenceDAO.getCompetenceUpgradeById(ucm.getCompetenceUpgradeID()));
			ucm.setTechnology(this.activityMasterDAO.getTechnologyDetailsByID(ucm.getTechnologyID(), null).get(0)
					.getTechnology());
			ucm.setDomain(this.activityMasterDAO.getDomainDetailsByID(ucm.getDomainID(), null).get(0).getDomain()
					+ " / "
					+ this.activityMasterDAO.getDomainDetailsByID(ucm.getDomainID(), null).get(0).getSubDomain());
			userCompetenceModel2.add(ucm);
		}
		return userCompetenceModel2;
	}

	@Transactional("transactionManager")
	private Map<String, Object> enrichMailforCompetenceRequest(List<UserCompetenceModel> userCompetenceModel1) {
		List<UserCompetenceModel> userCompetenceModel2 = getCompetenceDataForMail(userCompetenceModel1);
		EmployeeModel employee = activityMasterDAO
				.getEmployeeBySignum(userCompetenceModel2.get(0).getRequestedBySignum());
		EmployeeModel manager = activityMasterDAO.getEmployeeBySignum(userCompetenceModel2.get(0).getLmSignum());
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("request", userCompetenceModel2);
		data.put("managerName", manager.getEmployeeName());
		data.put("requestedByName", employee.getEmployeeName());
		data.put("env", this.competenceDAO.getDeployedEnv());
		data.put(AppConstants.CUSTOM_NOTIFICATIONS_TO, manager.getEmployeeEmailId());
		data.put(AppConstants.CUSTOM_NOTIFICATIONS_CC, employee.getEmployeeEmailId());

		return data;

	}

	@Transactional("transactionManager")
	private void validateUserCompetence(UserCompetenceModel userCompetence,
			HashMap<String, Object> deliveryCompetanceDetail, UserCompetenceModel oldUserCompetenceModel) {

		if (MapUtils.isEmpty(deliveryCompetanceDetail)) {
			throw new ApplicationException(500, "Unable to save data for " + userCompetence.getRequestedBySignum()
					+ " for competance " + userCompetence.getCompetanceID());
		}

		if (userCompetence.getCompetenceGradeID() == 0 || userCompetence.getCompetanceID() == 0
				|| userCompetence.getVendorID() == 0 || userCompetence.getTechnologyID() == 0
				|| userCompetence.getDomainID() == 0 || userCompetence.getCompetenceUpgradeID() == 0) {
			throw new ApplicationException(500, "Please fill all mandatory fields!");
		}

		if(!this.isUniqueCompetence(userCompetence,oldUserCompetenceModel)) {
			throw new ApplicationException(500, "Request already raised for given Combination!");
		}
		userCompetence.setRequestedStatus(CompetenceStatusEnum.getUnallowedStatusForRequest());
		if (userCompetence.getId() == 0) {
			if (!isValidRequest(userCompetence, userCompetence.getDeliveryCompetanceID())) {
				throw new ApplicationException(500, "Request already raised for given Combination!");
			}
		} else {
			List<UserCompetenceModel> userCompetenceModels = competenceDAO
					.getUserCompetenceDetailsByStatusDeliveryCompetenceID(userCompetence.getDeliveryCompetanceID(),
							CompetenceStatusEnum.getUnallowedStatusStringForRequest(), userCompetence);
			if (!CollectionUtils.isEmpty(userCompetenceModels)) {

				if (userCompetenceModels.size() > 2) {
					throw new ApplicationException(500, "Malfarmed Data found related to this competence!");
				}
				if (userCompetenceModels.get(0).getId() == userCompetence.getId()) {
					// TODO
				} else {
					throw new ApplicationException(500, "Request already raised for given Combination!");
				}
			}
		}

	}

	@Transactional("transactionManager")
	private boolean isValidRequestForCompetenceGrade(UserCompetenceModel newUserCompetence,
			UserCompetenceModel oldUserCompetence) {

		if (newUserCompetence.getCompetenceGradeID() != oldUserCompetence.getCompetenceGradeID()
				|| newUserCompetence.getCompetenceUpgradeID() != oldUserCompetence.getCompetenceUpgradeID()) {
			return true;
		}
		return false;
	}

	public List<Map<String, Object>> getWBLData() {
		return competenceDAO.getWBLData();
	}

	public Response<Void> insertCompetenceServiceArea(CompetenceServiceAreaModel competenceServiceAreaModel) {
		Response<Void> apiResponse = new Response<Void>();
		Boolean checkdata = this.competenceDAO.checkCompetenceExists(competenceServiceAreaModel);
		if (!checkdata) {
			this.competenceDAO.insertCompetenceServiceArea(competenceServiceAreaModel);
			int competenceIdAndInstanceId = isfCustomIdInsert
					.generateCustomId(competenceServiceAreaModel.getCompetenceID());
			competenceServiceAreaModel.setCompetenceID(competenceIdAndInstanceId);
			this.competenceDAO.insertDeliveryCompetenceServiceArea(competenceServiceAreaModel);
			competenceIdAndInstanceId = isfCustomIdInsert
					.generateCustomId(competenceServiceAreaModel.getCompetenceID());
			competenceServiceAreaModel.setCompetenceID(competenceIdAndInstanceId);
			apiResponse.addFormMessage("Data Inserted SuccessFully");
		} else {
			apiResponse.addFormError("Data Already Exist");
		}
		return apiResponse;
	}

	public List<Map<String, Object>> getUserCompetenceData(UserCompetenceModel userCompetenceModel, String role) {
		String statusString = "";
		for (String status : userCompetenceModel.getRequestedStatus()) {
			statusString += "'" + status + "',";
		}
		statusString = statusString.substring(0, statusString.length() - 1);
		return competenceDAO.getUserCompetenceData(userCompetenceModel, statusString, role);
	}

	public List<Map<String, Object>> getAllCompetenceServiceArea() {
		return competenceDAO.getAllCompetenceServiceArea();
	}

	public List<Map<String, Object>> getCompetenceUpgrade() {
		return competenceDAO.getCompetenceUpgrade();
	}

	public Response<Void> insertCompetenceDataBulk(MultipartFile file, String signum) throws IOException, SQLException {
		Response<Void> response = new Response<>();
		String fileName = file.getName();
		fileName = AppUtil.getFileNameWithTimestamp(fileName);
		String relativeFilePath = signum;
		this.uploadFile(file, relativeFilePath, fileName);
		String filePath = appService.getConfigUploadFilePath() + "/" + relativeFilePath + "/" + fileName;

		ArrayList<Set<Object>> filters = new ArrayList<Set<Object>>();

		List<Map<String, Object>> technology = getAllTechnology();
		List<Map<String, Object>> vendor = getAllVendor();
		List<Map<String, Object>> domain = getAllDomain();
		List<Map<String, Object>> compType = this.competenceDAO.getAllCompetenceType();
		Set<Object> vend = new HashSet<Object>();
		for (Map<String, Object> map : vendor) {
			vend.add(map.get("Vendor"));
		}
		;
		filters.add(vend);

		Set<Object> tech = new HashSet<Object>();
		for (Map<String, Object> map : technology) {
			tech.add(map.get("Technology"));
		}
		;
		filters.add(tech);

		Set<Object> dom = new HashSet<Object>();
		Set<Object> subDom = new HashSet<Object>();
		String s[];
		for (Map<String, Object> map : domain) {
			s = map.get("Domain").toString().split("/");
			dom.add(s[0].trim());
			subDom.add(s[1].trim());
		}
		;
		filters.add(dom);
		filters.add(subDom);

		Set<Object> cType = new HashSet<Object>();
		for (Map<String, Object> map : compType) {
			cType.add(map.get("CompetenceType"));
		}
		;
		filters.add(cType);

		Map<String, String> validationResult = FileCSVHelper.validateCompetenceDataFile(filePath, filters);
		if (validationResult != null && !validationResult.isEmpty()) {
			for (String key : validationResult.keySet()) {
				response.addFormError(validationResult.get(key));
			}
			return response;
		}

		int noOfRows = getNoOfRows(filePath);

		boolean isBulkUploadSuccess = appService.CsvBulkUploadNewGen(filePath, AppConstants.COMPETENCY_TEMP_TABLE_NAME,
				fileName);

		try {
			if (isBulkUploadSuccess) {
				BulkUploadModel bulk = new BulkUploadModel();
				bulk.setFileName(fileName);
				bulk.setSignum(signum);
				this.competenceDAO.insertCompetenceDataBulk(bulk);
				response.addFormMessage("Number of rows inserted are: " + bulk.getRecordsInserted()
						+ "<br> Number of that are not inserted: " + (noOfRows - 1 - bulk.getRecordsInserted()));
				return response;

			}
			response.addFormError(ApplicationMessages.ERROR_BCP_FILE_UPLOAD);
			return response;
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} finally {
			appService.dropTable(fileName);
		}
		return response;
	}

	public Response<Void> insertTraining(CompetenceTrainingModel competenceTrainingModel) {
		Response<Void> apiResponse = new Response<Void>();
		Integer trainingID = this.competenceDAO.checkTrainingExists(competenceTrainingModel);

		if (trainingID == null) {
			this.competenceDAO.insertTrainingCatalog(competenceTrainingModel);
			int traingIdAndInstanceId = isfCustomIdInsert.generateCustomId(competenceTrainingModel.getTrainingID());
			competenceTrainingModel.setTrainingID(traingIdAndInstanceId);
			this.competenceDAO.insertCompetanceTrainingDetail(competenceTrainingModel);
			traingIdAndInstanceId = isfCustomIdInsert.generateCustomId(competenceTrainingModel.getTrainingID());
			competenceTrainingModel.setTrainingID(traingIdAndInstanceId);
			apiResponse.addFormMessage("Data Inserted SuccessFully");
		} else {
			boolean flag = this.competenceDAO.checkCompetenceTrainingMappingExists(competenceTrainingModel);

			if (!flag) {
				competenceTrainingModel.setTrainingID(trainingID);
				this.competenceDAO.insertCompetanceTrainingDetail(competenceTrainingModel);
				apiResponse.addFormMessage("Data Inserted SuccessFully");
			} else {
				apiResponse.addFormError("Data Already Exist");
			}

		}

		return apiResponse;

	}

	public int getNoOfRows(String filePath) throws IOException {
		FileReader fr = new FileReader(filePath);
		LineNumberReader lnr = new LineNumberReader(fr);

		int linenumber = 0;

		while (lnr.readLine() != null) {
			linenumber++;
		}
		lnr.close();
		return linenumber;
	}

	public void uploadFile(MultipartFile file, String relativeFilePath, String fileName) {
		String directory = appService.getConfigUploadFilePath() + "/" + relativeFilePath;
		File df = new File(directory);

		if (!df.exists()) {
			df.mkdirs();
		}

		BufferedOutputStream stream = null;
		if (file != null && !file.isEmpty()) {
			try {
				byte[] bytes = file.getBytes();
				stream = new BufferedOutputStream(new FileOutputStream(
						new File(appService.getConfigUploadFilePath() + "/" + relativeFilePath + "/" + fileName)));
				stream.write(bytes);

			} catch (Exception e) {

				throw new ApplicationException(500, "File upload failed with error - " + e.getMessage());
			} finally {
				try {
					stream.close();
				} catch (IOException e) {
					LOG.info(e.getMessage());

				}
			}
		}
	}

	public List<Map<String, Object>> getTrainingData(int competenceID) {
		return competenceDAO.getTrainingData(competenceID);
	}

	public Response<Void> enableDisableCompetence(int competenceID, boolean active) {
		Response<Void> apiResponse = new Response<Void>();
		Boolean checkCompExistsorNot = this.competenceDAO.enableCompExistence(competenceID);
		if (checkCompExistsorNot) {
			this.competenceDAO.enableDisableCompetence(competenceID, active);
			if (active == true) {
				apiResponse.addFormMessage("Competence Enabled Successfully");
			} else {
				apiResponse.addFormMessage("Competence Disabled Successfully");
			}
		} else {
			apiResponse.addFormError("Competence Not Exists");
		}
		return apiResponse;
	}

	public Response<Void> insertMasterTrainingBulk(MultipartFile file, String signum) throws IOException, SQLException {
		Response<Void> response = new Response<>();
		String fileName = file.getName();
		fileName = AppUtil.getFileNameWithTimestamp(fileName);
		String relativeFilePath = signum;
		this.uploadFile(file, relativeFilePath, fileName);
		String filePath = appService.getConfigUploadFilePath() + "/" + relativeFilePath + "/" + fileName;

		Map<String, String> validationResult = FileCSVHelper.validateMasterDataFile(filePath);
		if (validationResult != null && !validationResult.isEmpty()) {
			for (String key : validationResult.keySet()) {
				response.addFormError(validationResult.get(key));
			}
			return response;
		}

		int noOfRows = getNoOfRows(filePath);

		boolean isBulkUploadSuccess = appService.CsvBulkUploadNewGen(filePath, AppConstants.MASTER_DATA_TEMP_TABLE_NAME,
				fileName);

		try {
			if (isBulkUploadSuccess) {
				BulkUploadModel bulk = new BulkUploadModel();
				bulk.setFileName(fileName);
				bulk.setSignum(signum);
				this.competenceDAO.insertMasterTrainingDataBulk(bulk);
				response.addFormMessage("Number of rows inserted are: " + bulk.getRecordsInserted()
						+ " <br> Number of Rows that failed to insert: " + (noOfRows - 1 - bulk.getRecordsInserted()));
				return response;

			}
			response.addFormError(ApplicationMessages.ERROR_BCP_FILE_UPLOAD);
			return response;
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} finally {
			appService.dropTable(fileName);
		}
		return response;
	}

	public Response<Void> insertItmTrainingBulk(MultipartFile file, String signum) throws IOException, SQLException {
		Response<Void> response = new Response<>();
		String fileName = file.getName();
		fileName = AppUtil.getFileNameWithTimestamp(fileName);
		String relativeFilePath = signum;
		this.uploadFile(file, relativeFilePath, fileName);
		String filePath = appService.getConfigUploadFilePath() + "/" + relativeFilePath + "/" + fileName;

		Map<String, String> validationResult = FileCSVHelper.validateITMDataFile(filePath);
		if (validationResult != null && !validationResult.isEmpty()) {
			for (String key : validationResult.keySet()) {
				response.addFormError(validationResult.get(key));
			}
			return response;
		}

		int noOfRows = getNoOfRows(filePath);

		boolean isBulkUploadSuccess = appService.CsvBulkUploadNewGen(filePath,
				AppConstants.ITM_TRAINING_TEMP_TABLE_NAME, fileName);

		try {
			if (isBulkUploadSuccess) {
				BulkUploadModel bulk = new BulkUploadModel();
				bulk.setFileName(fileName);
				bulk.setSignum(signum);
				this.competenceDAO.insertItmTrainingBulk(bulk);
				response.addFormMessage("Number of rows inserted are: " + bulk.getRecordsInserted()
						+ " <br> Number of Rows that failed to insert: " + (noOfRows - 1 - bulk.getRecordsInserted()));
				return response;

			}
			response.addFormError(ApplicationMessages.ERROR_BCP_FILE_UPLOAD);
			return response;
		} catch (NumberFormatException e) {
			appService.dropTable(fileName);
		} finally {

		}
		return response;
	}

	public UserCompetenceModel getUserCompetenceRow(int systemID) {
		return competenceDAO.getUserCompetenceRow(systemID);
	}

	public List<Map<String, Object>> getEngDataForLM(UserCompetenceModel userCompetenceModel) {
		List<Map<String, Object>> requestRaisedCountByCompetanceType = competenceDAO
				.getRequestRaisedCountByCompetanceType(userCompetenceModel);

		return requestRaisedCountByCompetanceType;
	}

	@Transactional("transactionManager")
	private boolean isValidRequest(UserCompetenceModel userCompetence, int deliveryCompetanceID) {

		String statusString = "";
		for (String status : userCompetence.getRequestedStatus()) {
			statusString += "'" + status + "',";
		}
		statusString = statusString.substring(0, statusString.length() - 1);
		return competenceDAO.isValidRequest(userCompetence, deliveryCompetanceID, statusString);
	}

	@Transactional("transactionManager")
	private boolean isUniqueCompetence(UserCompetenceModel userCompetence, UserCompetenceModel oldUserCompetenceModel) {
		boolean competenceUnique =competenceDAO.isUniqueCompetence(userCompetence);
		if(oldUserCompetenceModel != null) {
			return this.isOnlyGradeUpgardeBetweenUserModels(userCompetence, oldUserCompetenceModel) || competenceUnique;
		}
		return competenceUnique;
	}
	public DownloadTemplateModel downloadCompetenceData(String fileName) throws IOException {
		DownloadTemplateModel downloadTemplateModel = new DownloadTemplateModel();
		List<Map<String, Object>> data = new ArrayList<>();
		String fName = "";
		if (fileName.equalsIgnoreCase("CompetenceList")) {
			data = this.competenceDAO.downloadCompetenceData();
			fName = "CompetenceList.xlsx";
		}
		byte[] fData = FileUtil.generateXlsFile(data);
		downloadTemplateModel.setpFileContent(fData);
		downloadTemplateModel.setpFileName(fName);
		return downloadTemplateModel;
	}

	public List<Map<String, Object>> getAllDomain() {
		return competenceDAO.getAllDomain();
	}

	public List<Map<String, Object>> getAllTechnology() {
		return competenceDAO.getAllTechnology();
	}

	public List<Map<String, Object>> getAllVendor() {
		return competenceDAO.getAllVendor();
	}

	public Response<Void> updateCompetenceAssesmentData(UserCompetenceModel userCompetenceModel) {

		Response<Void> apiResponse = new Response<Void>();

		try {

			UserCompetenceModel existingUserCompetenceModel = competenceDAO
					.getUserCompetenceRow(userCompetenceModel.getId());

			existingUserCompetenceModel = validateCompetenceForAssesmentUpdation(existingUserCompetenceModel,
					userCompetenceModel);
			existingUserCompetenceModel.setParentSystemID(existingUserCompetenceModel.getId());
			boolean isSuccess = competenceDAO.insertCompetenceData(existingUserCompetenceModel);
			if (isSuccess) {
				apiResponse.addFormMessage("Competence assesment data has been updated successfully!");
			} else {
				apiResponse.addFormMessage("Error occured while updating Competence assesment data!");
			}
		} catch (Exception e) {

			apiResponse.addFormError(e.getMessage());
		}

		return apiResponse;
	}

	private UserCompetenceModel validateCompetenceForAssesmentUpdation(UserCompetenceModel existingUserCompetenceModel,
			UserCompetenceModel userCompetenceModel) {

		String loggedInSignum = userCompetenceModel.getLoggedInSignum();
		String requestedBySignum = userCompetenceModel.getRequestedBySignum();
		String actualLmSignum = competenceDAO.getManagerSignum(existingUserCompetenceModel.getRequestedBySignum());

		if (!CompetenceStatusEnum.APPROVED.getDisplayStatus()
				.equalsIgnoreCase(existingUserCompetenceModel.getStatus())) {

			throw new ApplicationException(500, "Assessment data can be updated for approved competence only!");
		}

		if (!existingUserCompetenceModel.isActive()) {

			throw new ApplicationException(500, "Provided Competence is not active!");
		}

		if (StringUtils.isBlank(loggedInSignum) || StringUtils.isBlank(requestedBySignum)) {

			throw new ApplicationException(500, "Please provide required information!");
		}

		if (!StringUtils.equalsIgnoreCase(loggedInSignum, actualLmSignum)) {

			throw new ApplicationException(500, "Only Manager have rights to update competence assesment data!");
		}

		if (!StringUtils.equalsIgnoreCase(requestedBySignum, existingUserCompetenceModel.getRequestedBySignum())) {

			throw new ApplicationException(500, "This competence has been raised by other person!");
		}

		existingUserCompetenceModel.setOjtHours(userCompetenceModel.getOjtHours());
		if (userCompetenceModel.getAssessmentScore() != 0.0) {

			if (userCompetenceModel.getAssessmentScore() > 10) {
				throw new ApplicationException(500, "Assessment Score must be less than 10!");
			}
			existingUserCompetenceModel.setAssessmentScore(userCompetenceModel.getAssessmentScore());
		}
		if (userCompetenceModel.getDeliveryScore() != 0.0) {

			if (userCompetenceModel.getDeliveryScore() > 10) {
				throw new ApplicationException(500, "Delivery Score must be less than 10!");
			}
			existingUserCompetenceModel.setDeliveryScore(userCompetenceModel.getDeliveryScore());
		}
		if (userCompetenceModel.getScopeComplexity() != 0.0) {

			if (userCompetenceModel.getScopeComplexity() > 10) {
				throw new ApplicationException(500, "Scope Complexity must be less than 10!");
			}
			existingUserCompetenceModel.setScopeComplexity(userCompetenceModel.getScopeComplexity());
		}
		return existingUserCompetenceModel;
	}
}
