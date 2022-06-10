/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ericsson.isf.dao.ActivityMasterDAO;
import com.ericsson.isf.dao.DemandForecastDAO;
import com.ericsson.isf.exception.ApplicationException;
import com.ericsson.isf.model.AllocatedResourceModel;
import com.ericsson.isf.model.AuditDataModel;
import com.ericsson.isf.model.DemandForecasSaveDetailsModel;
import com.ericsson.isf.model.DemandForecastDetailModel;
import com.ericsson.isf.model.DemandForecastFulfillmentModel;
import com.ericsson.isf.model.DemandForecastModel;
import com.ericsson.isf.model.DemandRequestModel;
import com.ericsson.isf.model.EmployeeModel;

import com.ericsson.isf.model.ProjectFilterModel;
import com.ericsson.isf.model.ProjectScopeDetailMappingModel;
import com.ericsson.isf.model.ProjectsModel;
import com.ericsson.isf.model.ResourceRequestWorkEffortsModel;
import com.ericsson.isf.model.Response;
import com.ericsson.isf.model.VendorTechModel;
import com.ericsson.isf.service.audit.AuditManagementService;
import com.ericsson.isf.util.AppConstants;

import com.ericsson.isf.util.IsfCustomIdInsert;

/**
 *
 * @author edhhklu
 */
@Service
public class DemandForecastService {

	private static final String NO_DATA_FOUND = "No Data Found";

	String TOOL_ID = "ToolID";

	@Autowired
	private ActivityMasterDAO activityMasterDAO;

	@Autowired
	private DemandForecastDAO demandForecastDAO;

	@Autowired
	private OutlookAndEmailService emailService;

	@Autowired
	DemandManagementService demandManagementService;

	@Autowired
	ProjectService projectService;

	@Autowired
	AuditManagementService auditServiec;

	@Autowired
	private IsfCustomIdInsert isfCustomIdInsert;

	private static final String PM = "DemandForecast/DemandForecast";

	@Transactional("transactionManager")
	public Response<Void> saveDemandSummaryDraft(List<DemandForecastModel> demandSummarySaveRequestList,
			boolean sendMail) {

		List<Map<String, Object>> existingData = demandForecastDAO.getDemandSummaryByRole(
				demandSummarySaveRequestList.get(0).getProjectId(), demandSummarySaveRequestList.get(0).getRole());

		Response<Void> response = new Response<>();
		for (DemandForecastModel demandSummarySaveRequest : demandSummarySaveRequestList) {
			try {
				demandForecastDAO.saveDemandSummaryDraft(demandSummarySaveRequest);
			} catch (Exception e) {
				/*
				 * try{ demandForecastDAO.updateDemandSummaryDraft(demandSummarySaveRequest);
				 * }catch(Exception ex){
				 */
				e.printStackTrace();
				// }
			}
		}
		if (sendMail) {
			createMailContent(demandSummarySaveRequestList, AppConstants.NOTIFICATION_TYPE_CREATE_DR_SPM);
		}
		processAuditData(demandSummarySaveRequestList, existingData);
		return response;
	}

	private static final String FIELDNAME_PLANNED_POSITIONCOUNT = "Planned Head Count";
	private static final String FIELDNAME_EFFECTIVE_POSITIONCOUNT = "Effective Head Count";
	private static final String FIELDNAME_FULFILLED_POSITIONCOUNT = "Fulfilled Head Count";
	private static final String COMMENT_CATEGORY_SAVEDEMAND = "SAVEDEMAND";
	private static final String DEMAND_FORECAST_DETAILS_UPDATED = "MSG_FIELDNAME_NEWVALUE";

	@Transactional("transactionManager")
	void processAuditData(List<DemandForecastModel> demandSummarySaveRequestList,
			List<Map<String, Object>> existingData) {
		// month,d.
		Map<String, Integer> existingDataMonthWise = new HashMap<>();
		for (Map<String, Object> e : existingData) {
			existingDataMonthWise.put(e.get("month").toString(), ((Double) e.get("positionCount")).intValue());
		}

		demandSummarySaveRequestList.sort(new SummaryMonthComparator());

		for (DemandForecastModel rec : demandSummarySaveRequestList) {
			Integer existingCount = existingDataMonthWise.get(MONTH_DATE_FORMAT.format(rec.getMonth()));
			if (existingCount != null && (existingDataMonthWise.get(MONTH_DATE_FORMAT.format(rec.getMonth())) == rec
					.getPositionCount())) {
				continue;
			}

			AuditDataModel auditDataModel = new AuditDataModel();
			auditDataModel.setAuditPageId(rec.getProjectId());
			auditDataModel.setAuditGroupCategory(AppConstants.AUDIT_PREFIX_DEMAND_SUMMARY);
			auditDataModel.setCreatedBy(rec.getSignum());
			auditDataModel.setNewValue((rec.getPositionCount()) + "");
			if (existingCount != null) {
				auditDataModel.setOldValue(existingCount + "");
				// default message
			} else {
				auditDataModel.setMessage(DEMAND_FORECAST_DETAILS_UPDATED);
			}

			if (RESOURCE_MANAGER.equalsIgnoreCase(rec.getRole())) {
				auditDataModel.setFieldName(FIELDNAME_PLANNED_POSITIONCOUNT);
			} else if (PROJECT_MANAGER.equalsIgnoreCase(rec.getRole())) {
				auditDataModel.setFieldName(FIELDNAME_EFFECTIVE_POSITIONCOUNT);
			} else if (FULFILMENT_MANAGER.equalsIgnoreCase(rec.getRole())) {
				auditDataModel.setFieldName(FIELDNAME_FULFILLED_POSITIONCOUNT);
			}

			auditDataModel.setActorType(rec.getRole());
			auditDataModel.setCommentCategory(COMMENT_CATEGORY_SAVEDEMAND);
			auditDataModel.setContext(new SimpleDateFormat(AppConstants.DEMAND_DATE_FORMAT).format(rec.getMonth()));
			auditServiec.addToAudit(auditDataModel);
		}
	}

	private static class SummaryMonthComparator implements Comparator<DemandForecastModel> {

		@Override
		public int compare(DemandForecastModel o1, DemandForecastModel o2) {
			o1.getMonth().compareTo(o2.getMonth());
			return 0;
		}

	}

	public List<Map<String, Object>> getDemandSummary(String signum, Date startDate, Integer pageSize, String role,
			String marketArea) {
		String allDates = getDatesPivoting(startDate, pageSize);
		List<Map<String, Object>> demandDetails=new ArrayList<Map<String, Object>>();
		if(StringUtils.equalsIgnoreCase(role, FULFILMENT_MANAGER)|| StringUtils.equalsIgnoreCase(role, RESOURCE_MANAGER)|| StringUtils.equalsIgnoreCase(role, PROJECT_MANAGER)) {
		 demandDetails = demandForecastDAO.getDemandSummary(signum, startDate, pageSize,
				allDates, role, marketArea);
		transformDataForTable(demandDetails, startDate, pageSize);
		demandDetails.sort(new SummaryComparator());
		}
		else {
			Map<String, Object> error=new HashMap<String, Object>();
			error.put("Error","Invalid Role ,Only "+FULFILMENT_MANAGER+","+RESOURCE_MANAGER+","+PROJECT_MANAGER+" allowed.");
			demandDetails.add(error);
			}
		return demandDetails;
	}

	private static class SummaryComparator implements Comparator<Map<String, Object>> {

		@Override
		public int compare(Map<String, Object> o1, Map<String, Object> o2) {
			return (ALL_ROLES.indexOf(o1.get(COLUMN_NAME_ROLE)) - ALL_ROLES.indexOf(o2.get(COLUMN_NAME_ROLE)));
		}

	}

	public boolean isDraftAllowed(Integer projectId, String role) {

		return demandForecastDAO.isDraftAllowed(projectId, role);
	}

	private static final String FULFILMENT_MANAGER = "Fulfillment Manager";
	private static final String RESOURCE_MANAGER = "Resource Planning Manager";
	private static final String PROJECT_MANAGER = "Project Manager";

	private static final List<String> ALL_ROLES = Arrays.asList(RESOURCE_MANAGER, PROJECT_MANAGER, FULFILMENT_MANAGER);

	private static final String COLUMN_NAME_PROJECTID = "Actions";
	private static final String COLUMN_NAME_PROJECT_NAME = "Project Name";
	private static final String COLUMN_NAME_ROLE = "Head Count";

	private List<Map<String, Object>> transformDataForTable(List<Map<String, Object>> data, Date startDate,
			Integer pageSize) {
		List<String> alldates = getDatesPivotingList(startDate, pageSize);
		Map<String, Map<String, Object>> tmpData = new HashMap<>();

		for (Map<String, Object> m : data) {
			String role = (m.get(COLUMN_NAME_ROLE) == null) ? ALL_ROLES.get(0) : m.get(COLUMN_NAME_ROLE).toString();
			tmpData.put("" + m.get(COLUMN_NAME_PROJECTID) + role, m);
			m.put(COLUMN_NAME_ROLE, role);
		}
		List<Map<String, Object>> additionalData = new ArrayList<>();
		for (Map<String, Object> m : data) {
			for (String role : ALL_ROLES) {
				Map<String, Object> roleData = tmpData.get("" + m.get(COLUMN_NAME_PROJECTID) + role);
				if (roleData == null) {
					roleData = new LinkedHashMap<>();
					roleData.put(COLUMN_NAME_PROJECTID, m.get(COLUMN_NAME_PROJECTID));
					roleData.put(COLUMN_NAME_PROJECT_NAME, m.get(COLUMN_NAME_PROJECT_NAME));
					roleData.put(COLUMN_NAME_ROLE, role);
					additionalData.add(roleData);
					tmpData.put("" + m.get(COLUMN_NAME_PROJECTID) + role, roleData);
				}
				for (String d : alldates) {
					if (!roleData.containsKey(d)) {
						roleData.put(d, null);
					}
				}
			}
		}

		data.addAll(additionalData);
		return data;
	}

	public List<DemandForecastDetailModel> getDemandForecastDetails(String signum, Integer projectId, Date startDate,
			Integer pageSize, String role) {
		List<DemandForecastDetailModel> result = demandForecastDAO.getDemandForecastDetails(signum, projectId,
				startDate, pageSize, role);
		for (int i = 0; i < result.size(); i++) {
			result.get(i).setReturnTools(demandForecastDAO.getToolDetails(result.get(i).getPositionId()));
		}

		for (int i = 0; i < result.size(); i++) {
			List<Integer> res = new ArrayList<>();
			if (result.get(i).getReturnTools() != null) {
				for (int j = 0; j < result.get(i).getReturnTools().size(); j++) {
					res.add((Integer) result.get(i).getReturnTools().get(j).get(TOOL_ID));
				}
				result.get(i).setToolList(res);
			}
		}

		return result;
	}

	public List<Map<String, Object>> getDomainSubdomain(String projectScopeID, String serviceAreaID) {
		return demandForecastDAO.getDomainSubdomain(projectScopeID, serviceAreaID);
	}

	public List<Map<String, Object>> getTechnologies(String projectScopeID, String serviceAreaID, String domainID) {
		return demandForecastDAO.getTechnologies(projectScopeID, serviceAreaID, domainID);
	}

	public List<Map<String, Object>> getScopeDetails(String projectId) {
		return demandForecastDAO.getScopeDetails(projectId);
	}

	public static void main(String... s) {
		System.out.println(new DemandForecastService().getDatesPivoting(new Date(), 6));
	}

	private String getDatesPivoting(Date startDate, Integer pageSize) {
		SimpleDateFormat sdf = new SimpleDateFormat("MMM yyyy");
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		String res = "";
		for (int i = 0; i < pageSize; i++) {
			if (i > 0) {
				res += ",[" + sdf.format(cal.getTime()) + "]";
				;
			} else {
				res += "[" + sdf.format(cal.getTime()) + "]";
			}
			cal.add(Calendar.MONTH, 1);
		}

		return res;

	}

	private static SimpleDateFormat MONTH_DATE_FORMAT = new SimpleDateFormat("MMM yyyy");

	private List<String> getDatesPivotingList(Date startDate, Integer pageSize) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		List<String> dates = new ArrayList<>();
		for (int i = 0; i < pageSize; i++) {
			dates.add(MONTH_DATE_FORMAT.format(cal.getTime()));
			cal.add(Calendar.MONTH, 1);
		}

		return dates;

	}

	private static final String DEFAULT_SELECT_STRING_UI = "--Select--";
	private static final String DEMAND_ACTION_DELETE = "";
	private static final String DEMAND_ACTION_DELETE_RESOURCE_REQUEST = "DELETED";

	@Transactional("transactionManager")
	public Response<Void> saveDemandForecastDetails(DemandForecasSaveDetailsModel saverequest) throws ParseException {

		// save the data coming in request
		Response<Void> response = new Response<>();
		List<DemandForecastDetailModel> positionData = saverequest.getPositionData();
		DemandForecastDetailModel existingpData;
		if (positionData != null && positionData.size() > 0) {
			long noOfDays = 0;
			for (DemandForecastDetailModel data : positionData) {
				List<Integer> psd = demandForecastDAO.getProjectScopeDetail(data);
				existingpData = demandForecastDAO.getDemandForecastDetailsByRRID(data.getPositionId());

				if (psd.size() > 1) {
					response.addFormError(
							"There are multiple deliverables for the selected combination! Please correct deliverable details in project.");
					return response;
				}
				if (psd == null || psd.size()==0) {
					response.addFormError("Deliverable combination incorrect!");
					return response;
				} else {
					data.setProjectScopeDetailId(psd.get(0));
				}
				if (data.getProjectScopeId() == null || data.getProjectScopeId() == 0 || data.getDomainID() == null
						|| data.getDomainID() == 0 || data.getServiceAreaID() == null || data.getServiceAreaID() == 0
						|| CollectionUtils.isEmpty(data.getVendorTechModel())) {
					response.addFormError("Please enter all details!");
					return response;
				}
				if (data.getResourceType() == null || data.getResourceType().trim().length() == 0
						|| DEFAULT_SELECT_STRING_UI.equalsIgnoreCase(data.getResourceType())) {
					response.addFormError("Please enter NE Type!");
					return response;
				}
				if (data.getJobRoleId() == null || data.getJobRoleId() == 0) {
					response.addFormError("Please enter JOB Role!");
					return response;
				}
				if (data.getJobStageId() == null || data.getJobStageId() == 0) {
					response.addFormError("Please enter JOB Stage!");
					return response;
				}
				if (data.getSpSource() == null || data.getSpSource().length() == 0
						|| DEFAULT_SELECT_STRING_UI.equalsIgnoreCase(data.getSpSource())) {
					response.addFormError("Please enter NE Source!");
					return response;
				}
				if (data.getCity() == null || data.getCity().length() == 0) {
					response.addFormError("Please enter valid Delivery Location!");
					return response;
				}
				if (data.getFte() < 0.0) {
					response.addFormError("Incorrect FTE% !");
					return response;
				}
				if (data.getStartDate().after(data.getEndDate())) {
					response.addFormError("End Date cannot be less than Start Date!");
					return response;
				}
				if(data.getDemandTypeID() == null || data.getDemandTypeID()==0) {
					response.addFormError("Please enter Demand Type!");
					return response;	
				}
				List<VendorTechModel> vendortech = data.getVendorTechModel();
				List<VendorTechModel> correctVendortech = new ArrayList<>();
				List<String> incorrectVendortech = new ArrayList<>();
				for(VendorTechModel data1 : vendortech) {
					
					String vendor = data1.getVendor();
					String tech = data1.getTechnology();
					if(tech==null) {
						incorrectVendortech.add(vendor);
						continue;
					}
					String vendortechname = vendor + "-"+  tech;
					boolean flag = demandForecastDAO.validateVendortech(vendortechname);
					if(flag) {
						correctVendortech.add(data1);
					}
					else {
						incorrectVendortech.add(vendortechname);
					}
				}
				//if all the vendortechs are wrong
				if(CollectionUtils.isEmpty(correctVendortech)) {
					String error = "Incorrect 'Vendor-Tech' are:- ";
					for(String name : incorrectVendortech) {
						error = error +"'" + name+"'" + ",";
					}
					error = error.substring(0,error.length()-1);
					error = error+ " Please try again with valid 'Vendor-Tech'";

					response.addFormError(error);
					return response;
				}
				//if partial are wrong
				else if(!CollectionUtils.isEmpty(incorrectVendortech)) {
					String error = "Incorrect 'Vendor-Tech' are:- ";
					for(String name : incorrectVendortech) {
						error = error +"'" + name+"'" + ",";
					}
					error = error.substring(0,error.length()-1);
					error = error + (" and Demand has been raised for valid Vendor-Tech");
					response.addFormWarning(error);
					data.setVendorTechModel(correctVendortech);
				}
				if (existingpData != null) {

					if (DEMAND_FORECAST_STATUS_SUBMITTED.equalsIgnoreCase(existingpData.getStatus())) {

						if (data.getRemoteCount() < existingpData.getRemoteCount()
								|| data.getOnsiteCount() < existingpData.getOnsiteCount()) {
							response.addFormError("Remote & Onsite count cannot be decreased!");
							return response;
						}
					}
				}

				if ("sendtofm".equalsIgnoreCase(saverequest.getOperation())) {
					noOfDays = TimeUnit.DAYS.convert((data.getStartDate().getTime() - new Date().getTime()),
							TimeUnit.MILLISECONDS);
					if (noOfDays < 14) {
						response.addFormWarning(
								"Start date is within 15 days. Best effort will be made by fulfillment manager to fulfil demand request");
					}
					if (existingpData != null) {

						if (DEMAND_FORECAST_STATUS_SUBMITTED.equalsIgnoreCase(data.getStatus())) {

							if (data.getRemoteCount() < existingpData.getRemoteCount()
									|| data.getOnsiteCount() < existingpData.getOnsiteCount()) {
								response.addFormError("Remote & Onsite count cannot be decreased!");
								return response;
							}
						}
					}
				}
				if (!DEMAND_FORECAST_STATUS_SUBMITTED.equalsIgnoreCase(data.getStatus())
						&& !DEMAND_ACTION_DELETE.equalsIgnoreCase(saverequest.getOperation())) {
					Date todayDate = new Date();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String today = sdf.format(todayDate);
					String start = sdf.format(data.getStartDate());
					Date today1 = new SimpleDateFormat("yyyy-MM-dd").parse(today);
					Date startdate = new SimpleDateFormat("yyyy-MM-dd").parse(start);
					if (startdate.before(today1)) {
						response.addFormError("Start Date should not be less than Today Date !");
						return response;
					}
				}

			}
			for (DemandForecastDetailModel data : positionData) {
				if (data.getPositionId() != null && data.getPositionId() != 0) {
					demandForecastDAO.updateDemandForecastDetails(data, saverequest.getProjectid(),
							saverequest.getRole(), saverequest.getSignum(), saverequest.getOperation());
					demandForecastDAO.deleteDemandVendorMapping(data.getPositionId());
					demandForecastDAO.deleteDemandToolMapping(data.getPositionId());
					ResourceRequestWorkEffortsModel resourceRequestWorkEffort = new ResourceRequestWorkEffortsModel();
					resourceRequestWorkEffort.setStartDate(data.getStartDate());
					resourceRequestWorkEffort.setEndDate(data.getEndDate());
					resourceRequestWorkEffort.setFtePercent(data.getFte());
					resourceRequestWorkEffort.setHours(data.getHours());
					resourceRequestWorkEffort.setCreatedBy(saverequest.getSignum());
					resourceRequestWorkEffort.setResourceRequestID(data.getPositionId());
					resourceRequestWorkEffort.setDuration((data.getDuration() / 8) + 1);

					demandForecastDAO.updateResourceRequestWorkEfforts(resourceRequestWorkEffort);
					
					
					saveDemandVendorTechCombination(data.getVendorTechModel(), data.getPositionId(), saverequest.getSignum());

					if (!DEMAND_ACTION_DELETE.equalsIgnoreCase(saverequest.getOperation())
							&& data.getToolList() != null) {
						for (int i = 0; i < data.getToolList().size(); i++) {
							demandForecastDAO.saveDemandToolMapping(data.getPositionId(), data.getToolList().get(i));
						}
					}

					if (DEMAND_ACTION_DELETE_RESOURCE_REQUEST
							.equalsIgnoreCase(saverequest.getPositionData().get(0).getStatus())) {
						demandForecastDAO.deleteResourcePositionByRrID(data.getPositionId(),saverequest.getSignum());
						demandForecastDAO.deleteWorkEffortsByRrID(data.getPositionId(),saverequest.getSignum());
					}
				} else {
					int sysID = demandForecastDAO.saveDemandForecastDetails(data, saverequest.getProjectid(), saverequest.getRole(),
							saverequest.getSignum(), saverequest.getOperation());
					
					int positionIdAndInstanceId = isfCustomIdInsert.generateCustomId(sysID);
					data.setPositionId(positionIdAndInstanceId);
					ResourceRequestWorkEffortsModel resourceRequestWorkEffort = new ResourceRequestWorkEffortsModel();
					resourceRequestWorkEffort.setStartDate(data.getStartDate());
					resourceRequestWorkEffort.setEndDate(data.getEndDate());
					resourceRequestWorkEffort.setFtePercent(data.getFte());
					resourceRequestWorkEffort.setHours(data.getHours());
					resourceRequestWorkEffort.setCreatedBy(saverequest.getSignum());
					resourceRequestWorkEffort.setResourceRequestID(data.getPositionId());
					resourceRequestWorkEffort.setDuration((data.getDuration() / 8) + 1);
					demandForecastDAO.saveResourceRequestWorkEfforts(resourceRequestWorkEffort);
					
					
					saveDemandVendorTechCombination(data.getVendorTechModel(), data.getPositionId(), saverequest.getSignum());

					if (!DEMAND_ACTION_DELETE.equalsIgnoreCase(saverequest.getOperation())
							&& data.getToolList() != null) {
						for (int i = 0; i < data.getToolList().size(); i++) {
							demandForecastDAO.saveDemandToolMapping(data.getPositionId(), data.getToolList().get(i));
						}
					}
				}

			}

			if (PROJECT_MANAGER.equalsIgnoreCase(saverequest.getRole())
					&& !saverequest.getPositionData().get(0).getStatus().equalsIgnoreCase(AppConstants.DELETE_STATUS)) {
				updateSummaryCountForSPM(saverequest);
			}
			if (PROJECT_MANAGER.equalsIgnoreCase(saverequest.getRole())
					&& saverequest.getPositionData().get(0).getStatus().equalsIgnoreCase(AppConstants.DELETE_STATUS)) {
				// deleteSummaryCountForSPM(saverequest);
				updateSummaryCountForDeletedData(saverequest);
			}

			if ("sendtofm".equalsIgnoreCase(saverequest.getOperation())) {
				sendToFM(saverequest);
			}
			if ("sendtospm".equalsIgnoreCase(saverequest.getOperation())) {
				demandForecastDAO.deleteOthersUsersDrafts(saverequest.getSignum(), saverequest.getProjectid());
//    			createMailContentRpm(saverequest.getProjectid(), saverequest.getSignum(),"CREATE_RPM_TO_SPM",saverequest);

			}
			if ("sendbacktorpm".equalsIgnoreCase(saverequest.getOperation())) {
				createMailContentRpm(saverequest.getProjectid(), saverequest.getSignum(), "CREATE_SPM_TO_RPM",
						saverequest);
			}
		} else {
			response.addFormError("Position Data Null");
			response.addFormWarning("Enter Valid data");
		}
		return response;
	}



	ResponseEntity<Response<Void>> saveDemandVendorTechCombination(List<VendorTechModel>  vendorTechDataModel, Integer positionID, String signum) {
		Response<Void> response=new Response<>();

		try {
			if(CollectionUtils.isNotEmpty(vendorTechDataModel)) {
				for(VendorTechModel VendorTechModelList : vendorTechDataModel) {
					VendorTechModelList.setPositionID(positionID);
					VendorTechModelList.setCreatedBy(signum);
					
				}
			}
			this.demandForecastDAO.saveDemandVendorTechCombination(vendorTechDataModel);
			
			
		}
		catch (ApplicationException exe) {
			response.addFormError(exe.getMessage());
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception ex) {
			response.addFormError(ex.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}	
		return new ResponseEntity<>(response, HttpStatus.OK);
		
		
	}

	private Response<Void> updateSummaryCountForDeletedData(DemandForecasSaveDetailsModel saverequest)
			throws ParseException {
		Response<Void> response = new Response<>();
		DemandForecastDetailModel getExistingpData = demandForecastDAO.getDemandForecastDetailsByRrIdAndPId(
				saverequest.getPositionData().get(0).getPositionId(), saverequest.getProjectid());

		List<DemandForecastModel> summaryDataList = getSummeryDataForSPM(saverequest,
				getExistingpData.getStartDate(), getExistingpData.getEndDate());
		for (DemandForecastModel demandSummarySaveRequest : summaryDataList) {
			int positionCount = demandForecastDAO.getDemandSummaryDraftDetails(demandSummarySaveRequest);
			if(positionCount - (getExistingpData.getOnsiteCount() + getExistingpData.getRemoteCount())>=0) {
			demandSummarySaveRequest.setPositionCount(
					positionCount - (getExistingpData.getOnsiteCount() + getExistingpData.getRemoteCount()));
			}
			demandForecastDAO.updateDemandSummaryDraft(demandSummarySaveRequest);
		}
		return response;
	}

	private List<DemandForecastModel> getSummeryDataForSPM(DemandForecasSaveDetailsModel saverequest, Date date1,
			Date lastDate) {

		Map<String, DemandForecastModel> summaryData = new HashMap<>();
		LocalDate startDate = date1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate endDate = lastDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		while ((startDate.compareTo(endDate) <= 0)
				|| (startDate.getYear() == endDate.getYear() && startDate.getMonthValue() == endDate.getMonthValue())) {
			DemandForecastModel df;
			df = summaryData.get(
					MONTH_DATE_FORMAT.format(Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant())));
			if (df == null) {
				df = new DemandForecastModel();
				df.setSignum(saverequest.getSignum());
				String sDate = MONTH_DATE_FORMAT
						.format(Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
				try {
					df.setMonth(MONTH_DATE_FORMAT.parse(sDate));
				} catch (ParseException e) {
				}
				summaryData.put(sDate, df);
			}

			df.setProjectId(saverequest.getProjectid());
			df.setRole(saverequest.getRole());
			startDate = startDate.plusMonths(1);
		}
		List<DemandForecastModel> summaryDataList = new ArrayList<>();
		for (String date : summaryData.keySet()) {
			summaryDataList.add(summaryData.get(date));
		}
		return summaryDataList;
	}

	

	public void updateSummaryCountForSPM(DemandForecasSaveDetailsModel saverequest) {

		Map<String, DemandForecastModel> summaryData = new HashMap<>();
		List<DemandForecastDetailModel> allDetailsData = demandForecastDAO
				.getAllDemandForecastDetailsByRole(saverequest.getProjectid(), saverequest.getRole());

		for (DemandForecastDetailModel data : allDetailsData) {

			if ("".equalsIgnoreCase(saverequest.getOperation())
					|| "sendbacktorpm".equalsIgnoreCase(saverequest.getOperation())
					|| "sendtofm".equalsIgnoreCase(saverequest.getOperation())) {
				LocalDate startDate = data.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				LocalDate endDate = data.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				while ((startDate.compareTo(endDate) <= 0) || (startDate.getYear() == endDate.getYear()
						&& startDate.getMonthValue() == endDate.getMonthValue())) {
					DemandForecastModel df;
					df = summaryData.get(MONTH_DATE_FORMAT
							.format(Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant())));
					if (df == null) {
						df = new DemandForecastModel();
						df.setSignum(saverequest.getSignum());
						String sDate = MONTH_DATE_FORMAT
								.format(Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
						try {
							df.setMonth(MONTH_DATE_FORMAT.parse(sDate));
						} catch (ParseException e) {
						}
						summaryData.put(sDate, df);
					}

					df.setProjectId(saverequest.getProjectid());
					df.setRole(saverequest.getRole());
					df.setPositionCount(df.getPositionCount() + data.getOnsiteCount() + data.getRemoteCount());

					startDate = startDate.plusMonths(1);
				}
			}

		}

		List<DemandForecastModel> summaryDataList = new ArrayList<>();
		for (String date : summaryData.keySet()) {
			summaryDataList.add(summaryData.get(date));
		}
		if (summaryDataList.size() > 0) {
			saveDemandSummaryDraft(summaryDataList, false);
		}
	}

	private void createMailContentRpm(Integer projectid, String signum, String type,
			DemandForecasSaveDetailsModel saverequest) {
		Map<String, Object> placeHolder = new HashMap<>();
		placeHolder.put(AppConstants.PROJECT_ID, projectid);
		placeHolder.put("signumID", signum);
		List<Map<String, Object>> rrIdData = new LinkedList<>();
		for (DemandForecastDetailModel pData : saverequest.getPositionData()) {
			Map<String, Object> rrdata = new LinkedHashMap<>();
			rrdata.put("subDomain", pData.getDomain_subDomain());
			rrdata.put("onsiteCount", pData.getOnsiteCount());
			rrdata.put("remoteCount", pData.getRemoteCount());
			rrdata.put("resourceType", pData.getResourceType());

			rrIdData.add(rrdata);
		}
		placeHolder.put("rrIdData", rrIdData);

		ProjectFilterModel projectFilterModel = new ProjectFilterModel();
		projectFilterModel.setProjectID(projectid);

		List<ProjectsModel> projectsModel = projectService.getProjectByFilters(projectFilterModel);

		EmployeeModel eDetails = activityMasterDAO.getEmployeeBySignum(signum);

		placeHolder.put(AppConstants.CURRENT_USER, signum);
		placeHolder.put(AppConstants.CURRENT_USER_NAME, eDetails.getEmployeeName());

		placeHolder.put(AppConstants.PROJECT_ID, projectid);
		placeHolder.put("pageLink", PM);
		placeHolder.put("request", saverequest);
		emailService.sendMail(type, "New Demand Created in " + projectsModel.get(0).getProjectName(), placeHolder);
	}

	private void createMailContent(List<DemandForecastModel> demandSummarySaveRequestList, String type) {
		Map<String, Map<String, String>> response = new HashMap<>();
		for (DemandForecastModel demandSummarySaveRequest : demandSummarySaveRequestList) {
			Map<String, String> projData = response.get(demandSummarySaveRequest.getProjectId() + "");
			if (projData == null) {
				projData = new HashMap<String, String>();
				response.put(demandSummarySaveRequest.getProjectId() + "", projData);
			}
			projData.put(MONTH_DATE_FORMAT.format(demandSummarySaveRequest.getMonth()),
					(int) demandSummarySaveRequest.getPositionCount() + "");

		}

		for (String proj : response.keySet()) {

			ProjectFilterModel projectFilterModel = new ProjectFilterModel();
			projectFilterModel.setProjectID(Integer.parseInt(proj));

			List<ProjectsModel> projectsModel = projectService.getProjectByFilters(projectFilterModel);

			EmployeeModel eDetails = activityMasterDAO
					.getEmployeeBySignum(demandSummarySaveRequestList.get(0).getSignum());

			Map<String, Object> placeHolder = new HashMap<>();
			placeHolder.put(AppConstants.CURRENT_USER, demandSummarySaveRequestList.get(0).getSignum());
			placeHolder.put(AppConstants.PROJECT_ID, proj);
			placeHolder.put("positionData", response.get(proj));
			placeHolder.put("pageLink", PM);
			placeHolder.put(AppConstants.CURRENT_USER_NAME, eDetails.getEmployeeName());
			emailService.sendMail(type, "New Demand Created in " + projectsModel.get(0).getProjectName(), placeHolder);
		}
	}

	private static String DEMAND_FORECAST_STATUS_SUBMITTED = "SUBMITTED";

	public void sendToFM(DemandForecasSaveDetailsModel saverequest) {
		for (DemandForecastDetailModel pData : saverequest.getPositionData()) {
			if (!DEMAND_FORECAST_STATUS_SUBMITTED.equalsIgnoreCase(pData.getStatus())) {
				String currentUser = saverequest.getSignum();

				demandForecastDAO.sendToFM(pData.getPositionId());
				Map<String, Object> data = createMapData(pData, currentUser, saverequest.getProjectid());
				Map<String, Object> placeHolder = emailService.enrichMailforCM(data,
						AppConstants.NOTIFICATION_TYPE_CREATE_DR);
				placeHolder.put("pageLink1", PM);
				emailService.sendMail(AppConstants.NOTIFICATION_TYPE_CREATE_DR, placeHolder);
			} else {

				DemandRequestModel demandRequestModel = new DemandRequestModel();
				demandRequestModel.setResourceRequestId(pData.getPositionId());
				demandRequestModel.setResourceType(pData.getResourceType());
				demandRequestModel.setResourceDescription(pData.getDescription());
				demandRequestModel.setCreatedBy(saverequest.getSignum());
				demandRequestModel.setProjScopeDetailId(demandForecastDAO.getProjectScopeDetail(pData).get(0));
				demandRequestModel.setProjectId(saverequest.getProjectid());
				demandRequestModel.setJobRoleId(pData.getJobRoleId());
				demandRequestModel.setJobStageId(pData.getJobStageId());
				demandRequestModel.setLocationId(null); // TODO check proper value;
				demandRequestModel.setRemoteCount(pData.getRemoteCount());
				demandRequestModel.setOnsiteCount(pData.getOnsiteCount());
				demandRequestModel.setStartDate(pData.getStartDate());
				demandRequestModel.setEndDate(pData.getEndDate());
				demandRequestModel.setDuration((int) pData.getDuration());
				demandRequestModel.setFtePercent((int) pData.getFte());
				demandRequestModel.setRemoteLocation(pData.getSpSource());
				demandRequestModel.setOnsiteLocation(pData.getSpSource());
				demandRequestModel.setResourceCity(pData.getCity());
				demandRequestModel.setResourceCountry(pData.getCountry());
				demandRequestModel.setResourceLat(pData.getLatitude());
				demandRequestModel.setResourceLng(pData.getLongitude());
				demandRequestModel.setResourceTimeZone(pData.getTimeZone());
				demandRequestModel.setJobStageName(pData.getJobStageName());
				demandRequestModel.setHours((int) pData.getHours());
				demandRequestModel.setVendorTechModel(pData.getVendorTechModel());
				demandRequestModel.setJobRoleName(pData.getJobRoleName());
				
				demandManagementService.updateDemandResourceRequest(demandRequestModel);

//					String currentUser = saverequest.getSignum();
//					Map<String,Object> data = createMapData(pData,currentUser,saverequest.getProjectid());
//					Map<String,Object> placeHolder = emailService.enrichMailforCM(data,AppConstants.NOTIFICATION_TYPE_CREATE_DR);
//					placeHolder.put("pageLink1",PM);
//					emailService.sendMail(AppConstants.NOTIFICATION_TYPE_CREATE_DR,placeHolder);
			}
		}
	}

	private Map<String, Object> createMapData(DemandForecastDetailModel pData, String currentUser, int projectID) {
		Map<String, Object> fmData = new HashMap<>();
		fmData.put("CreatedBy", currentUser);
		fmData.put("currentUser", currentUser);
		fmData.put("ResourceType", pData.getResourceType());
		fmData.put("StartDate", pData.getStartDate());
		fmData.put("EndDate", pData.getEndDate());
		fmData.put("FTEPercent", pData.getFte());
		fmData.put("RemoteCount", pData.getRemoteCount());
		fmData.put("OnsiteCount", pData.getOnsiteCount());
		fmData.put("RRID", pData.getPositionId());
		fmData.put("ResourceCountry", pData.getCountry());
		fmData.put("Hours", pData.getHours());
		fmData.put("ProjScopeDetailID", pData.getProjectScopeDetailId());
		fmData.put("ProjectID", projectID);

		return fmData;
	}

	public List<DemandForecastFulfillmentModel> getDemandForecastDetailsByfilter(Integer projectID,
			String positionStatus, String marketArea) {
		return this.demandForecastDAO.getDemandForecastDetailsByfilter(projectID, positionStatus, marketArea);
	}

	private static final String POSITION_STATUS_PROPOSED = "Proposed";

	@Transactional("transactionManager")
	public void updateFMSummary(List<AllocatedResourceModel> allocatedResourceModel, Integer projectId) {
		String currentuser = "";
		if (allocatedResourceModel.size() > 0) {
			currentuser = allocatedResourceModel.get(0).getLoggedInUser();
		}

		List<DemandForecastFulfillmentModel> positionDetails = demandForecastDAO
				.getDemandPositionDetailsByprojectId(projectId);
		Map<String, DemandForecastModel> summaryData = new HashMap<>();
		for (DemandForecastFulfillmentModel am : positionDetails) {
			LocalDate startDate = am.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate endDate = am.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			while ((startDate.compareTo(endDate) <= 0) || (startDate.getYear() == endDate.getYear()
					&& startDate.getMonthValue() == endDate.getMonthValue())) {
				DemandForecastModel df;
				df = summaryData.get(MONTH_DATE_FORMAT
						.format(Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant())));
				if (df == null) {
					df = new DemandForecastModel();
					df.setSignum(currentuser);
					String sDate = MONTH_DATE_FORMAT.format(Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
					try {
						df.setMonth(MONTH_DATE_FORMAT.parse(sDate));
					} catch (ParseException e) {
					}
					summaryData.put(sDate, df);
				}

				df.setProjectId(projectId);
				df.setRole(FULFILMENT_MANAGER);
				df.setPositionCount(df.getPositionCount() + 1);
				startDate = startDate.plusMonths(1);
			}
		}

		if (summaryData.size() > 0) {
			List<DemandForecastModel> summaryDataList = new ArrayList<>();
			for (String date : summaryData.keySet()) {
				summaryDataList.add(summaryData.get(date));
			}
			saveDemandSummaryDraft(summaryDataList, false);
		}
	}

	public void migrateDemandforecastDateFM(Integer projectId) {
		Date referenceDate = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(referenceDate);
		c.add(Calendar.MONTH, -3);
		Date migrationLimitDate = c.getTime();
		String role = "Project Manager";

		List<DemandForecastFulfillmentModel> existingData = demandForecastDAO
				.getDemandForecastDetailsByfilter(projectId, "'Proposed','Deployed','Closed','Resource Allocated'", "");
		Map<String, DemandForecastModel> summaryData = new HashMap<>();
		for (DemandForecastFulfillmentModel data : existingData) {

			if (data.getEndDate().before(migrationLimitDate)) {
				continue;
			}
			Calendar cal = Calendar.getInstance();
			cal.setTime(data.getStartDate());
			while ((cal.getTime().getYear() + cal.getTime().getMonth()) <= (data.getEndDate().getYear()
					+ data.getEndDate().getMonth())) {

				DemandForecastModel df;
				df = summaryData.get(MONTH_DATE_FORMAT.format(cal.getTime()));
				if (df == null) {
					df = new DemandForecastModel();
					String sDate = MONTH_DATE_FORMAT.format(cal.getTime());
					try {
						df.setMonth(MONTH_DATE_FORMAT.parse(sDate));
					} catch (ParseException e) {
					}
					summaryData.put(sDate, df);
				}

				df.setProjectId(projectId);
				df.setRole(FULFILMENT_MANAGER);
				df.setPositionCount(df.getPositionCount() + 1);
				cal.add(Calendar.MONTH, 1);
			}
		}

		System.out.println("test");

		if (summaryData.size() > 0) {
			List<DemandForecastModel> summaryDataList = new ArrayList<>();
			for (String date : summaryData.keySet()) {
				DemandForecastModel spmData = summaryData.get(date);
				summaryDataList.add(spmData);

				DemandForecastModel rpmData = new DemandForecastModel();
				rpmData.setMonth(spmData.getMonth());
				rpmData.setPositionCount(spmData.getPositionCount());
				rpmData.setProjectId(spmData.getProjectId());
				rpmData.setRole(FULFILMENT_MANAGER);
				summaryDataList.add(rpmData);
				saveDemandSummaryDraft(summaryDataList, false);
			}

		}

	}

	/*
	 * //TODO Code cleanup public void migrateDemandforecastDate(){ Date
	 * referenceDate = new Date(); Calendar c = Calendar.getInstance();
	 * c.setTime(referenceDate); c.add(Calendar.MONTH, -4); Date startDate
	 * =c.getTime(); Integer projectId=210; String role="Project Manager";
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * List<DemandForecastDetailModel> existingData =
	 * demandForecastDAO.getDemandForecastDetailsForMigration("",projectId,
	 * startDate, 4,role); Map<String,DemandForecastModel> summaryData=new
	 * HashMap<>();
	 * 
	 * Calendar cal = Calendar.getInstance(); c.add(Calendar.MONTH, -3); Date
	 * migrationLimitDate =c.getTime();
	 * 
	 * for(DemandForecastDetailModel data:existingData){
	 * if(data.getPositionId()!=4714){ continue; }
	 * if(data.getEndDate().before(migrationLimitDate)){ continue; }
	 * 
	 * 
	 * 
	 * cal=Calendar.getInstance(); cal.setTime(data.getStartDate());
	 * while((cal.getTime().getYear()+cal.getTime().getMonth())<=(data.getEndDate().
	 * getYear()+data.getEndDate().getMonth())){ DemandForecastModel df;
	 * df=summaryData.get(MONTH_DATE_FORMAT.format(cal.getTime())); if(df==null){
	 * df=new DemandForecastModel(); String
	 * sDate=MONTH_DATE_FORMAT.format(cal.getTime()); try {
	 * df.setMonth(MONTH_DATE_FORMAT.parse(sDate)); } catch (ParseException e) { }
	 * summaryData.put(sDate, df); }
	 * 
	 * df.setProjectId(projectId); df.setRole(role);
	 * df.setPositionCount(df.getPositionCount()+data.getOnsiteCount()+data.
	 * getRemoteCount()); cal.add(Calendar.MONTH, 1); } }
	 * 
	 * System.out.println("test");
	 * 
	 * if(summaryData.size()>0){ List<DemandForecastModel> summaryDataList=new
	 * ArrayList<>(); for(String date:summaryData.keySet()){ DemandForecastModel
	 * spmData = summaryData.get(date); summaryDataList.add(spmData);
	 * 
	 * DemandForecastModel rpmData=new DemandForecastModel();
	 * rpmData.setMonth(spmData.getMonth());
	 * rpmData.setPositionCount(spmData.getPositionCount());
	 * rpmData.setProjectId(spmData.getProjectId());
	 * rpmData.setRole("Resource Planning Manager"); summaryDataList.add(rpmData);
	 * saveDemandSummaryDraft(summaryDataList,false); }
	 * 
	 * }
	 * 
	 * 
	 * migrateDemandforecastDateFM(projectId);
	 * 
	 * }
	 */

	public List<ProjectScopeDetailMappingModel> getAllScopeDetailsByProject(String projectId) {
		return demandForecastDAO.getAllScopeDetailsByProject(projectId);
	}

	public List<Map<String, Object>> getResourceRequestedBySubScope(int projectID, int projectScopeDetailID) {
		return demandForecastDAO.getResourceRequestedBySubScope(projectID, projectScopeDetailID);
	}

	public ResponseEntity<Response<List<VendorTechModel>>> getVendorTechCombination(int start, int length,
			String term) {
		Response<List<VendorTechModel>> result = new Response<>();
		try {

			List<VendorTechModel> vendorTechCombination = this.demandForecastDAO.getVendorTechCombination(start, length,
					term);

			if (vendorTechCombination.isEmpty()) {
				result.addFormMessage(NO_DATA_FOUND);
				result.setResponseData(vendorTechCombination);
			} else {
				result.setResponseData(vendorTechCombination);
			}

		} catch (ApplicationException exe) {
			result.addFormError(exe.getMessage());
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception ex) {
			result.addFormError(ex.getMessage());
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

}
