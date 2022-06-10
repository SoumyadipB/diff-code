package com.ericsson.isf.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ericsson.isf.dao.CRManagementDAO;
import com.ericsson.isf.dao.ResourceRequestDAO;
import com.ericsson.isf.exception.ApplicationException;
import com.ericsson.isf.model.CompetenceSubModel;
import com.ericsson.isf.model.DataTableRequest;
import com.ericsson.isf.model.DownloadTemplateModel;
import com.ericsson.isf.model.EEDashboardDataModel;
import com.ericsson.isf.model.EmployeeModel;
import com.ericsson.isf.model.EngagementDetails;
import com.ericsson.isf.model.EngineerDetailsModel;
import com.ericsson.isf.model.LeavePlanModel;
import com.ericsson.isf.model.ResigReqModel;
import com.ericsson.isf.model.ResoucePositionFmModel;
import com.ericsson.isf.model.ResourceCalandarModel;
import com.ericsson.isf.model.ResourceEngagementModel;
import com.ericsson.isf.model.ResourceModel;
import com.ericsson.isf.model.ResourcePositionWorkEffortModel;
import com.ericsson.isf.model.ResourceRequestModel;
import com.ericsson.isf.model.Response;
import com.ericsson.isf.model.SearchResourceByFilterModel;
import com.ericsson.isf.model.WorkEffortModel;
import com.ericsson.isf.util.AppConstants;
import com.ericsson.isf.util.AppUtil;

import com.ericsson.isf.util.DateTimeUtil;

import net.sf.json.JSONArray;

@Service
public class ResourceRequestService {

	@Autowired
	private ResourceRequestDAO resourceRequestDAO;

	@Autowired
	private ActivityMasterService activityMasterService;

	@Autowired
	private CRManagementDAO changeManagementDAO;

	@Autowired
	private OutlookAndEmailService emailService;

	private static final Logger LOG = LoggerFactory.getLogger(ResourceRequestService.class);

	private static final String RECORDS_TOTAL = "recordsTotal";
	private static final String RECORDS_FILTERED = "recordsFiltered";
	private static final String RESOURCE = "Resource";
	private static final String BACKLOGHOURS = "BacklogHours";
	private static final String SIGNUM = "Signum";
	private static final String EMPLOYEE_NAME = "EmployeeName";
	private static final String HOURS = " Hrs";
	private static final String WO = " WO";
	private static final String TOTAL_COUNTS = "TOTALCNT";
	private static final String PLANNED_START_DATE = "plannedStartDate";
	private static final String PLANNED_END_DATE = "PlannedEndDate";
	private static final String FLOWCHART_DEF_ID = "flowchartdefid";
	private static final String CURRENTBACKLOGCOUNTS = "Current Backlog Counts";

	public List<HashMap<String, Object>> searchResourcesByFilter(
			SearchResourceByFilterModel searchResourceByFilterModel) {
		List<HashMap<String, Object>> resourcelist = new ArrayList<>();
		resourcelist = this.resourceRequestDAO.searchResourcesByFilter(searchResourceByFilterModel);
		return resourcelist;
	}

	public List<HashMap<String, Object>> searchResourcesByFilters(
			SearchResourceByFilterModel searchResourceByFilterModel) {
		List<HashMap<String, Object>> resourcelist = new ArrayList<>();

		double hours = AppUtil.getNoOfWeekdayHoursBetweenDates(
				AppUtil.getDateFromString(searchResourceByFilterModel.getStartDate()),
				AppUtil.getDateFromString(searchResourceByFilterModel.getEndDate()));

		searchResourceByFilterModel.setHours((float) hours);

		resourcelist = this.resourceRequestDAO.searchResourcesByFilters(searchResourceByFilterModel);
		return resourcelist;
	}

	public List<LinkedHashMap<String, Object>> getResourceRequestsByFilter(Integer ProjectID, Integer DomainID,
			Integer SubDomainID, Integer SubServiceAreaID, Integer TechnologyID, String PositionStatus,
			String AllocatedResource, String spoc, String marketArea) {
		List<LinkedHashMap<String, Object>> resourcelist = new ArrayList<>();
		resourcelist = this.resourceRequestDAO.getResourceRequestsByFilter(ProjectID, DomainID, SubDomainID,
				SubServiceAreaID, TechnologyID, PositionStatus, AllocatedResource, spoc, marketArea);
		return resourcelist;
	}

	//
	public List<CompetenceSubModel> getCompetenceSubDetails(int rrID) {
		List<CompetenceSubModel> resourcelist = new ArrayList<>();
		resourcelist = this.resourceRequestDAO.getCompetenceSubDetails(rrID);
		return resourcelist;
	}

	public List<HashMap<String, Object>> getResourcePositionSubList(ResourcePositionWorkEffortModel rpef, int rrID) {
		List<HashMap<String, Object>> resourcelist = new ArrayList<>();
		resourcelist = this.resourceRequestDAO.getResourcePositionSubList(rpef, rrID);
		return resourcelist;
	}

	public List<HashMap<String, Object>> getCompetenceLevel(int rrID) {
		List<HashMap<String, Object>> resourcelist = new ArrayList<>();
		resourcelist = this.resourceRequestDAO.getCompetenceLevel(rrID);
		return resourcelist;
	}

	public List<HashMap<String, Object>> getCertificateSubDetails(int rrID) {
		List<HashMap<String, Object>> resourcelist = new ArrayList<>();
		resourcelist = this.resourceRequestDAO.getCertificateSubDetails(rrID);
		return resourcelist;
	}

	public List<ResoucePositionFmModel> getPositionsAndAllocatedResources(Integer rrID, String positionStatus,
			String spoc, String marketArea, Integer projectID) {
		List<ResoucePositionFmModel> parDetails = new ArrayList<>();
		parDetails = this.resourceRequestDAO.getPositionsAndAllocatedResources(rrID, positionStatus, spoc, marketArea,
				projectID);
		return parDetails;
	}

	public List<ResoucePositionFmModel> getAllPositions(Integer rrID, String positionStatus, String spoc,
			String marketArea, Integer projectID) {
		return this.resourceRequestDAO.getAllPositions(rrID, positionStatus, spoc, marketArea, projectID);
	}

	public List<ResourceRequestModel> getDemandRequestDetail(int rrID) {
		List<ResourceRequestModel> rrDetails = new ArrayList<>();
		rrDetails = this.resourceRequestDAO.getDemandRequestDetail(rrID);
		return rrDetails;
	}

	public List<ResourcePositionWorkEffortModel> getResourceRequestWEffort(int rrID) {
		List<ResourcePositionWorkEffortModel> rrDetails = new ArrayList<>();
		rrDetails = this.resourceRequestDAO.getResourceRequestWEffort(rrID);
		return rrDetails;
	}

	public List<ResourcePositionWorkEffortModel> getResourceRequestWEffortDetails(int rrID) {
		List<ResourcePositionWorkEffortModel> rrDetails = new ArrayList<>();
		return this.resourceRequestDAO.getResourceRequestWEffortDetails(rrID);

	}

	/*
	 * //TODO code cleanup
	 * 
	 * @Transactional("transactionManager") public boolean
	 * changePositionStatus(ResourcePositionModel resourcePositionModel){ return
	 * this.resourceRequestDAO.changePositionStatus(resourcePositionModel); }
	 */

	/*
	 * //TODO code cleanup public List<ResourcePositionModel>
	 * searchDemandPositions(Integer domainID,Integer serviceAreaID,Integer
	 * technologyID,String positionStatus,String resourceSignum){
	 * List<ResourcePositionModel> rplist = new ArrayList<>(); rplist =
	 * this.resourceRequestDAO.searchDemandPositions(domainID,serviceAreaID,
	 * technologyID,positionStatus,resourceSignum); return rplist;
	 * 
	 * }
	 */
	private static final int HOURS_PER_DAY = 24;

	private static final double WORKING_HOURS_PER_DAY = 8.5;
	private static final int SHIFT_TIME_START_HOUR = 8;
	private static final int SHIFT_TIME_END_HOUR = 17;

	/**
	 * 
	 * @param signumID
	 * @param projectID
	 * @param startDate1
	 * @param endDate1
	 * @param term
	 * @param dataTableReq
	 * @return Map<String, Object>
	 * @throws ParseException
	 */
	public Map<String, Object> getWOResourceLevelDetails(String signumID, Integer projectID, String startDate,
			String endDate, String term, DataTableRequest dataTableReq) throws ParseException {
		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, Map<String, EngagementDetails>> resultData = new HashMap<>();
		List<Map<String, Object>> formattedResponse = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> allSignumforProject = new LinkedList<Map<String, Object>>();
		List<String> signumForProject = new LinkedList<String>();

		if (projectID != 0) {
			allSignumforProject = resourceRequestDAO.getSignumsWorkingInProject(signumID, projectID, startDate, endDate,
					dataTableReq, term);
			if (allSignumforProject.isEmpty()) {
				response.put("data", formattedResponse);
				response.put("draw", dataTableReq.getDraw());
				response.put("responseMessage",
						"The specified signum is not associated with the project.Please enter valid signum!");
				response.put(RECORDS_TOTAL, 0);
				response.put(RECORDS_FILTERED, 0);
				return response;
			}
		}

		// convert map of Signum into List of Signum
		for (Map<String, Object> signum : allSignumforProject) {
			signumForProject.add((String) signum.get(SIGNUM));
		}

		// Global search for a signum
		if (StringUtils.isNotBlank(term)) {
			signumForProject.clear();
			signumForProject.add(term);
		}

		resultData = getHoursBookedInInterval(signumID, signumForProject, projectID, startDate, endDate);

		// To change the response compatible for server side pagination
		Iterator<String> it = resultData.keySet().iterator();
		while (it.hasNext()) {
			Map<String, EngagementDetails> dates = new HashMap<String, EngagementDetails>();
			Map<String, Object> tmp = new HashMap<String, Object>();

			String userSignum = it.next();
			dates = resultData.get(userSignum);

			tmp.put(SIGNUM.toLowerCase(), userSignum);
			tmp.put("dates", dates);

			formattedResponse.add(tmp);
		}

		response.put("data", formattedResponse);
		response.put("draw", dataTableReq.getDraw());
		response.put("responseMessage", "Success");

		if (MapUtils.isNotEmpty(resultData)) {
			if (projectID != 0 && StringUtils.isBlank(term)) {
				response.put(RECORDS_TOTAL, allSignumforProject.get(0).get(TOTAL_COUNTS));
				response.put(RECORDS_FILTERED, allSignumforProject.get(0).get(TOTAL_COUNTS));
			} else {
				response.put(RECORDS_TOTAL, 1);
				response.put(RECORDS_FILTERED, 1);
			}
		} else {
			response.put(RECORDS_TOTAL, 0);
			response.put(RECORDS_FILTERED, 0);
		}

		return response;
	}

	/**
	 * Calculation of Booked Hours for different purpose.
	 * 
	 * @author ekmbuma
	 * 
	 * @param signumID
	 * @param signumForProject
	 * @param projectID
	 * @param startDate
	 * @param endDate
	 * @return Map<String, Map<String, EngagementDetails>>
	 * @throws ParseException
	 */
	private Map<String, Map<String, EngagementDetails>> getHoursBookedInInterval(String signumID,
			List<String> signumForProject, Integer projectID, String startDate, String endDate) throws ParseException {

		Date startDate1 = DateTimeUtil.convertStringToDate(startDate, AppConstants.DEFAULT_DATE_FORMAT);
		Date endDate1 = DateTimeUtil.convertStringToDate(endDate, AppConstants.DEFAULT_DATE_FORMAT);

		List<String> dateRange = getListOfDaysBetweenDates(startDate1, endDate1);

		Map<String, Map<String, EngagementDetails>> resultData = new HashMap<>();
		double avgLoe = 0.0;
		double avgLoePerDay = 0.0;
		double adhocHours = 0.0;
		Map<Integer, Double> inProgressRemainingSla = new HashMap<>();

		List<Map<String, Object>> avgLoeMap = null;
		String woList = StringUtils.EMPTY;
		String dataDate = StringUtils.EMPTY;
		Date today = new Date();
		int numberOfDays = 0;
		int numberOfDaysForBacklog = 0;
		List<String> wodateRange;
		List<String> backlogWodateRange;

		List<ResourceEngagementModel> workOrderList = this.resourceRequestDAO.getWOResourceLevelDetails(signumID,
				signumForProject, projectID, startDate, endDate);

		for (ResourceEngagementModel wo : workOrderList) {
			if (wo.getType().equalsIgnoreCase("planned")) {

				if (woList.trim().length() > 0 && !woList.trim().contains(String.valueOf(wo.getwOID())))
					woList = woList + "," + wo.getwOID();
				else
					woList = String.valueOf(wo.getwOID());
			}
		}

		List<Map<String, Object>> listOfBacklogWOforProjectWithSignum = resourceRequestDAO
				.getBacklogWorkOrdersForProjectWithSignum(projectID, signumForProject);

		for (Map<String, Object> map : listOfBacklogWOforProjectWithSignum) {

			if (woList.trim().length() > 0 && !woList.trim().contains(String.valueOf(map.get("woid"))))
				woList = woList + "," + map.get("woid");
			else
				woList = String.valueOf(map.get("woid"));
		}

		if (woList.trim().length() != 0) {
			avgLoeMap = this.resourceRequestDAO.getAvgLoeForWoID(signumID, projectID, startDate, endDate);
		}

		if (!workOrderList.isEmpty()) {
			for (ResourceEngagementModel wo : workOrderList) {
				if (null != wo.getSignumID()) {
					Map<String, EngagementDetails> userData;
					wodateRange = getListOfDaysBetweenDates(wo.getPlannedStartDate(), wo.getPlannedEndDate());
					numberOfDays = wodateRange.size();
					if (!resultData.containsKey(wo.getSignumID() + " (" + wo.getEmployeeName() + ")")) {

						userData = new LinkedHashMap<String, EngagementDetails>();
						for (String date : dateRange) {
							userData.put(date, new EngagementDetails());
						}

						// calculation of Backlog Hours
						if (startDate1.before(today) && endDate1.after(today)) {
							List<Map<String, Object>> listOfBacklogWO = resourceRequestDAO
									.getBacklogWorkOrders(wo.getSignumID());
							EngagementDetails todaysData = userData.get(formatter.format(today));
							double backLogHours = 0.0;
							for (Map<String, Object> map : listOfBacklogWO) {
								// backLogHours = backLogHours+hoursDiff((Date)map.get("plannedStartDate"),
								// (Date)map.get("PlannedEndDate"));
								if (map.get(FLOWCHART_DEF_ID) != null) {
									avgLoe = getLoe((int) map.get(FLOWCHART_DEF_ID), avgLoeMap);
								}
								if (avgLoe != 0)
									backLogHours = backLogHours + avgLoe;
								else {
									if (formatter.format((Date) map.get(PLANNED_START_DATE))
											.equalsIgnoreCase(formatter.format((Date) map.get(PLANNED_END_DATE)))) {

										if (hoursDiff((Date) map.get(PLANNED_START_DATE),
												(Date) map.get(PLANNED_END_DATE)) < WORKING_HOURS_PER_DAY)
											backLogHours = backLogHours + hoursDiff((Date) map.get(PLANNED_START_DATE),
													(Date) map.get(PLANNED_END_DATE));
										else
											backLogHours = backLogHours + WORKING_HOURS_PER_DAY;
									} else {
										backlogWodateRange = getDaysBetweenDates((Date) map.get(PLANNED_START_DATE),
												(Date) map.get(PLANNED_END_DATE));
										numberOfDaysForBacklog = backlogWodateRange.size();

										backLogHours = backLogHours + (WORKING_HOURS_PER_DAY * numberOfDaysForBacklog);
									}
								}
								todaysData.addToUniqueWorkOrders((Integer) map.get("woid"));
							}
							todaysData.setBacklogHours(backLogHours);
						}

						resultData.put(wo.getSignumID() + " (" + wo.getEmployeeName() + ")", userData);
					}

					if (!wo.getType().equalsIgnoreCase("Planned")) {
						userData = resultData.get(wo.getSignumID() + " (" + wo.getEmployeeName() + ")");
						dataDate = formatter.format(wo.getPlannedStartDate());

						if (wo.getType().trim().equalsIgnoreCase("Project Meeting")
								|| wo.getType().trim().equalsIgnoreCase("Project Training")) {
							adhocHours = hoursDiff(wo.getPlannedStartDate(), wo.getPlannedEndDate());
							if (userData.containsKey(dataDate)) {
								userData.get(dataDate).addToPlannedProjectHours(adhocHours);
							}
						} else if (wo.getType().equalsIgnoreCase("Internal Meeting")
								|| wo.getType().equalsIgnoreCase("Internal Training")) {
							adhocHours = hoursDiff(wo.getPlannedStartDate(), wo.getPlannedEndDate());
							if (userData.containsKey(dataDate)) {
								userData.get(dataDate).addToPlannedInternalHours(adhocHours);
							}
						} else if (wo.getType().trim().equalsIgnoreCase("Project AdHoc")
								|| wo.getType().trim().equalsIgnoreCase("Internal AdHoc")
								|| wo.getType().equalsIgnoreCase("Others Break")) {
							adhocHours = hoursDiff(wo.getPlannedStartDate(), wo.getPlannedEndDate());
							if (userData.containsKey(dataDate)) {
								userData.get(dataDate).addToAdhocHours(adhocHours);
							}
						} else {
							adhocHours = hoursDiff(wo.getPlannedStartDate(), wo.getPlannedEndDate());
							if (userData.containsKey(dataDate)) {
								userData.get(dataDate).addToPlannedInternalHours(adhocHours);
							}
						}
					} else if (wo.getStatus().equalsIgnoreCase("ASSIGNED")
							|| wo.getStatus().equalsIgnoreCase("REOPENED")
							|| wo.getStatus().equalsIgnoreCase("PLANNED")) {
						avgLoe = getLoe(wo.getFlowchartDefId(), avgLoeMap);
						userData = resultData.get(wo.getSignumID() + " (" + wo.getEmployeeName() + ")");
						if (avgLoe == 0.0) {
							avgLoe = numberOfDays * WORKING_HOURS_PER_DAY;
						}
						avgLoePerDay = avgLoe / numberOfDays; // Loe Calculation
						for (String date : wodateRange) {
							if (userData.containsKey(date)) {
								userData.get(date).addToUniqueWorkOrders(wo.getwOID());
								userData.get(date).addToPlannedHours(avgLoePerDay);
							}
						}
					} else if ((wo.getStatus().equalsIgnoreCase("INPROGRESS")
							|| wo.getStatus().equalsIgnoreCase("ONHOLD")) && wo.getBookinghours() != 0.0) {
						if (inProgressRemainingSla.containsKey(wo.getwOID())) {
							avgLoe = getLoe(wo.getFlowchartDefId(), avgLoeMap);
							inProgressRemainingSla.put(wo.getwOID(), avgLoe - wo.getBookinghours());
						} else {
							avgLoe = getLoe(wo.getFlowchartDefId(), avgLoeMap);
							if (avgLoe == 0.0) {
								avgLoe = numberOfDays * WORKING_HOURS_PER_DAY;
							}
							inProgressRemainingSla.put(wo.getwOID(), avgLoe - wo.getBookinghours());
						}
						userData = resultData.get(wo.getSignumID() + " (" + wo.getEmployeeName() + ")");
						userData.get(formatter.format(wo.getBookingEndDate())).addToPlannedHours(wo.getBookinghours());

					} else if (wo.getStatus().equalsIgnoreCase("CLOSED")
							|| wo.getStatus().equalsIgnoreCase("COMPLETED")) {
						if (wo.getBookingStartDate() != null) {
							userData = resultData.get(wo.getSignumID() + " (" + wo.getEmployeeName() + ")");
							userData.get(formatter.format(wo.getBookingStartDate()))
									.addToPlannedHours(wo.getBookinghours());
							userData.get(formatter.format(wo.getBookingStartDate()))
									.addToUniqueWorkOrders(wo.getwOID());
						}
					}
				}
			}
		} else {
			Map<String, EngagementDetails> userData;
			userData = new LinkedHashMap<String, EngagementDetails>();
			EmployeeModel empDetail = this.activityMasterService.getEmployeeBySignum(signumForProject.get(0));
			for (String date : dateRange) {
				userData.put(date, new EngagementDetails());
			}
			resultData.put(signumForProject.get(0) + " (" + empDetail.getEmployeeName() + ")", userData);
		}

		double availableHours = 0.0;
		for (Map<String, EngagementDetails> engg : resultData.values()) {
			adhocHours = 0.0;
			for (String s : engg.keySet()) {
				adhocHours = engg.get(s).getPlannedProjectHours() + engg.get(s).getPlannedInternalHours()
						+ engg.get(s).getAdhocHours();

				availableHours = WORKING_HOURS_PER_DAY
						- (adhocHours + engg.get(s).getBacklogHours() + engg.get(s).getPlannedHours());
				if (availableHours >= 0.0)
					engg.get(s).addToAvailableHours(availableHours);

				if (s.equalsIgnoreCase(formatter.format(today))) {
					EngagementDetails todaysData = engg.get(formatter.format(today));
					for (int woid : todaysData.getUniqueWorkOrders()) {
						if (inProgressRemainingSla.containsKey(woid) && inProgressRemainingSla.get(woid) > 0) {
							engg.get(formatter.format(today)).addToBacklogHours(inProgressRemainingSla.get(woid));
						}
					}
				}
			}
		}

		// code for leave Hours.
		double leaveHours = 0.0;
		boolean isOnLeave = false;

		for (String signum : resultData.keySet()) {
			List<LeavePlanModel> leavePlanData = activityMasterService.getLeavePlanBySignum(signum.substring(0, 7));

			// can get leave data for a week. Using new Method
			Map<String, EngagementDetails> userDataDatewise = resultData.get(signum);
			Set<String> weeklyDate = userDataDatewise.keySet();
			leaveHours = 0.0;
			isOnLeave = false;

			for (String date : weeklyDate) {
				for (LeavePlanModel leaveData : leavePlanData) {
					DateFormat stringDateFormat = new SimpleDateFormat(AppConstants.DEFAULT_DATE_FORMAT);
					String weekStartDate = stringDateFormat.format(leaveData.getStartDate());
					String weekEndDate = stringDateFormat.format(leaveData.getEndDate());

					if (weekStartDate.equals(weekEndDate) && leaveData.getIsActive() == 1
							&& weekStartDate.equals(date)) {
						if (leaveData.getLeaveHours() != 0.0)
							leaveHours = leaveData.getLeaveHours();
						else
							leaveHours = 8.5;
						isOnLeave = true;
						userDataDatewise.get(date).setLeaveHours(leaveHours);
						userDataDatewise.get(date).setIsOnLeave(isOnLeave);
					} else if (leaveData.getStartDate().before(leaveData.getEndDate())) {
						List<String> leaveDateRange = getListOfDaysBetweenDates(leaveData.getStartDate(),
								leaveData.getEndDate());
						if (leaveDateRange.contains(date)) {
							if (leaveData.getLeaveHours() != 0.0)
								leaveHours = leaveData.getLeaveHours();
							else
								leaveHours = 8.5;
							isOnLeave = true;
							userDataDatewise.get(date).setLeaveHours(leaveHours);
							userDataDatewise.get(date).setIsOnLeave(isOnLeave);
						}
					}
				}
			}
		}

		return resultData;
	}

	private double getLoe(int flowChartDefID, List<Map<String, Object>> avgLoeMap) {

		double avgLoe = 0.0;

		for (Map<String, Object> map : avgLoeMap) {
			if (map.get("flowchartdefid") != null && (int) map.get("flowchartdefid") == flowChartDefID) {
				avgLoe = (double) map.get("AVGHours");
			}

		}
		return avgLoe;
	}

	SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private double hoursDiff(Date d1, Date d2) {
		return (d2.getTime() - d1.getTime()) / (60.0 * 60 * 1000);

	}

	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	private List<String> getDaysBetweenDates(Date startdate, Date enddate) {
		List<String> dates = new ArrayList<String>();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(startdate);
		Date result = null;
		while (calendar.getTime().before(enddate)) {
			result = calendar.getTime();
			dates.add(formatter.format(result));
			calendar.add(Calendar.DATE, 1);
		}
		result = calendar.getTime();
		if (result.compareTo(enddate) <= 0)
			dates.add(formatter.format(result));
		return dates;
	}

	private List<String> getListOfDaysBetweenDates(Date startdate, Date enddate) {
		List<String> dates = new ArrayList<>();
		LocalDate startDate = startdate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate endDate = enddate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		while (startDate.compareTo(endDate) <= 0) {
			String sDate = formatter.format(Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
			dates.add(sDate);
			startDate = startDate.plusDays(1);
		}
		return dates;
	}
//	     public JSONArray getWOResourceLevelDetails(String signumID, Integer projectID, String startDate, String endDate) {
//	    	 return this.resourceRequestDAO.getWOResourceLevelDetails(signumID,projectID,startDate,endDate);
//	     }

	public JSONArray getResourceWOLevelDetails(String signumID) {
		return this.resourceRequestDAO.getResourceWOLevelDetails(signumID);
	}

	/*
	 * //TODO Code Cleanup public List<HashMap<String, Object>>
	 * SearchDemandRequests(Integer ProjectScopeID,Integer DomainID, Integer
	 * SubServiceAreaID,Integer TechnologyID) { List<HashMap<String, Object>>
	 * resourcelist = new ArrayList<>(); resourcelist =
	 * this.resourceRequestDAO.SearchDemandRequests(ProjectScopeID,DomainID,
	 * SubServiceAreaID,TechnologyID); return resourcelist; }
	 */
	/*
	 * //TODO code cleanup
	 * 
	 * @Transactional("transactionManager") public boolean
	 * CreateResourceReplacement(ChangeRequestPositionModel
	 * changeRequestPositionModel) { return
	 * this.resourceRequestDAO.CreateResourceReplacement(changeRequestPositionModel)
	 * ; }
	 */

	@Transactional("transactionManager")
	public boolean ModifyDemandRequest(ResourceModel resourceModel) {

		return this.resourceRequestDAO.ModifyDemandRequest(resourceModel);
	}

	/*
	 * //TODO code cleanup public List<HashMap<String, Object>>
	 * searchChangeRequests(Integer projectID,String changeRequestType,String
	 * positionStatus,String resourceSignum) { List<HashMap<String, Object>> crlist
	 * = new ArrayList<>(); crlist =
	 * this.resourceRequestDAO.searchChangeRequests(projectID,changeRequestType,
	 * positionStatus,resourceSignum); return crlist; }
	 */

	/*
	 * //TODO code cleanup
	 * 
	 * @Transactional("transactionManager") public boolean
	 * modifyDemandPosition(ModifyDemandModel modifyDemandModel) {
	 * 
	 * return this.resourceRequestDAO.modifyDemandPosition(modifyDemandModel); }
	 */

	public Map<String, Object> getBookedResourceBySignum(String signum, int weId) {
		DecimalFormat numberFormatter = new DecimalFormat("0.00");
		WorkEffortModel weDetais = changeManagementDAO.getWorkEffortDetailsByID(weId);
		float reqHours = (float) (AppUtil.WORKING_HOURS_IN_A_DAY * weDetais.getFte_Percent() / 100);

		List<Map<String, Object>> data = this.resourceRequestDAO.getBookedResourceBySignum(signum,
				weDetais.getStartDate(), weDetais.getEndDate());
		Calendar calendar = Calendar.getInstance();

		SimpleDateFormat df = new SimpleDateFormat(AppConstants.DEFAULT_DATE_FORMAT);

		Map<String, Object> bookingData = new HashMap<>();
		for (Map<String, Object> d : data) {
			double blockedHrs = (double) d.get("BlockedHrs");
			bookingData.put(d.get("date").toString(), blockedHrs);
		}

		Map<String, Object> response = new HashMap<>();
		List<Map<String, Object>> availabilityDetails = new ArrayList<>();

		response.put("details", availabilityDetails);
		int i = 0;
		int totaldays = 0;
		boolean isContinue = true;
		String recommendedEndDate = "";
		String recommendedStartDate = "";
		double totalAvailHours = 0;
		for (calendar.setTime(weDetais.getStartDate()); calendar.getTimeInMillis() <= weDetais.getEndDate()
				.getTime(); calendar.add(Calendar.DATE, 1)) {

			String date = df.format(calendar.getTime());
			Map<String, Object> row = new LinkedHashMap<>();
			row.put("dt", date);

			double bookedhours = (bookingData.get(date) == null) ? 0
					: Double.parseDouble(bookingData.get(date).toString());
			double availhours = AppUtil.WORKING_HOURS_IN_A_DAY - bookedhours;
			if (availhours > reqHours) {
				availhours = reqHours;
			}

			row.put("avail_hr", availhours);
			row.put("req_hr", reqHours);
			int day = calendar.get(Calendar.DAY_OF_WEEK);
			boolean isWeekend = (day == Calendar.SUNDAY || day == Calendar.SATURDAY);
			row.put("isweekend", isWeekend);
			if (!isWeekend) {
				totaldays++;
				totalAvailHours += availhours;
			}
			row.put("month_name", calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) + "-"
					+ calendar.get(Calendar.YEAR));
			availabilityDetails.add(row);

			if (isContinue && reqHours <= availhours) {
				if (!isWeekend) {
					if ("".equals(recommendedStartDate)) {
						recommendedStartDate = date;
					} else {
						recommendedEndDate = date;
					}
					i++;
				}
			} else {
				if (!"".equals(recommendedStartDate)) {
					isContinue = false;
				}
			}

		}

		response.put("availability", numberFormatter.format((totalAvailHours / weDetais.getHours()) * 100));
		response.put("recommendedEndDate", recommendedEndDate);
		response.put("recommendedStartDate", recommendedStartDate);

		return response;
	}

	/*
	 * //TODO code cleanup public List<Map<String, Object>>
	 * getAllCertifications(String issuer) { return
	 * resourceRequestDAO.getAllCertifications(issuer); }
	 */
	public ResponseEntity<Response<List<ResourceCalandarModel>>> getResourceCalander(String signum, String startdate,
			String enddate) {
		Response<List<ResourceCalandarModel>> response = new Response();
		try {
			response.setResponseData(resourceRequestDAO.getResourceCalander(signum, startdate, enddate));
			LOG.info("Getting Resource Calandar : Success!!");
		} catch (ApplicationException e) {
			LOG.error(e.getMessage());
			response.addFormError(e.getMessage());
			return new ResponseEntity<Response<List<ResourceCalandarModel>>>(response, HttpStatus.OK);
		} catch (Exception ex) {
			LOG.error(ex.getMessage());
			response.addFormError(ex.getMessage());
			return new ResponseEntity<Response<List<ResourceCalandarModel>>>(response,
					HttpStatus.INTERNAL_SERVER_ERROR);

		}
		return new ResponseEntity<Response<List<ResourceCalandarModel>>>(response, HttpStatus.OK);
	}

	/*
	 * //TODO code cleanup public List<Map<String, Object>> getJobRoles() { return
	 * resourceRequestDAO.getJobRoles(); }
	 */
	public List<Map<String, Object>> getJobStages() {
		return resourceRequestDAO.getJobStages();
	}

	/*
	 * //TODO code cleanup public List<Map<String, Object>> getOnsiteLocations() {
	 * return resourceRequestDAO.getOnsiteLocations(); }
	 */
	public List<Map<String, Object>> getBacklogWorkOrders(String signum) {
		return resourceRequestDAO.getBacklogWorkOrders(signum);
	}

	@Transactional("transactionManager")
	public boolean updateResignedResource(ResigReqModel resModel) {

		List<WorkEffortModel> weList = changeManagementDAO.getFutureWorkEffortsBySignum(resModel.getSignum());
		for (WorkEffortModel we : weList) {
			if (AppConstants.PROPOSAL_PENDING_STATUS.equalsIgnoreCase(we.getPositionStatus())
					|| AppConstants.PROPOSAL_ALLOCATED_STATUS.equalsIgnoreCase(we.getPositionStatus())
					|| AppConstants.PROPOSAL_PORPOSED_STATUS.equalsIgnoreCase(we.getPositionStatus())) {
				changeManagementDAO.updateWorkEffortPositionStatusByWeId(AppConstants.PROPOSAL_PENDING_STATUS,
						we.getWorkEffortID() + "");
				changeManagementDAO.updateWorkEffortStatusByWeId(true, we.getWorkEffortID() + "");
				changeManagementDAO.updateWorkEffortSignum(we.getWorkEffortID(), resModel.getCurrentUser(), null);
			} else if (AppConstants.PROPOSAL_DEPLOYED_STATUS.equalsIgnoreCase(we.getPositionStatus())) {
				splitWorkEffort(we, resModel.getReleaseDate(), resModel.getCurrentUser());
			}

			this.activityMasterService.updatePoistionStatusByWfStatus(we.getResourcePositionID());
		}

		Map<String, Object> data = new HashMap<String, Object>();
		ResigReqModel req = (ResigReqModel) data.put("r", resModel);
		data.put("w", weList);
		emailService.sendMail(AppConstants.NOTIFICATION_RESIGN_RESOURCE,
				emailService.enrichMailforCM(data, AppConstants.NOTIFICATION_RESIGN_RESOURCE));
		return true;
	}

	private void splitWorkEffort(WorkEffortModel wEffortModel, Date splitDate, String currentUser) {
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(splitDate);
		cal2.setTime(wEffortModel.getEndDate());

		cal2.set(Calendar.HOUR_OF_DAY, 0);
		cal2.set(Calendar.MINUTE, 0);
		cal2.set(Calendar.SECOND, 0);
		cal2.set(Calendar.MILLISECOND, 0);

		cal1.getTime();
		cal2.getTime();

		if (cal1.before(cal2)) {
			Calendar newCal = Calendar.getInstance();
			newCal = cal1;

			// change end date of original work effort
			double totalHrs = AppUtil.getNoOfWeekdayHoursBetweenDates(wEffortModel.getStartDate(), newCal.getTime());
			float hoursPerFte = (float) ((totalHrs * wEffortModel.getFte_Percent()) / 100);
			this.changeManagementDAO.updateWorkEffortDetails(wEffortModel.getStartDate(), newCal.getTime(),
					wEffortModel.getWorkEffortID(), currentUser,
					AppUtil.getNoOfWeekdayDaysBetweenDates(wEffortModel.getStartDate(), newCal.getTime()), hoursPerFte);

			newCal.add(Calendar.DATE, 1);

			WorkEffortModel newWorkEffort = new WorkEffortModel();
			newWorkEffort.setPositionStatus("Proposal Pending");
			newWorkEffort.setResourcePositionID(wEffortModel.getResourcePositionID());
			newWorkEffort.setStartDate(newCal.getTime());
			newWorkEffort.setEndDate(wEffortModel.getEndDate());
			newWorkEffort.setIsActive(false);
			double noOfHours = AppUtil.getNoOfWeekdayHoursBetweenDates(cal1.getTime(), newWorkEffort.getEndDate());

			hoursPerFte = (float) ((noOfHours * wEffortModel.getFte_Percent()) / 100);
			newWorkEffort.setDuration((int) (noOfHours / AppUtil.WORKING_HOURS_IN_A_DAY)); // days
			newWorkEffort.setFte_Percent(wEffortModel.getFte_Percent());
			newWorkEffort.setHours((int) hoursPerFte);
			newWorkEffort.setWorkEffortStatus(wEffortModel.getWorkEffortStatus());
			newWorkEffort.setCreatedBy(currentUser);
			newWorkEffort.setLastModifiedBy(currentUser);
			newWorkEffort.setAllocatedBy(currentUser);
			this.changeManagementDAO.insertInWorkEffort(newWorkEffort);

			this.changeManagementDAO.changeBookedResourceStatusByWEIDAndDates(wEffortModel.getWorkEffortID(),
					newCal.getTime(), wEffortModel.getEndDate(), 0);

		}
	}

	/*
	 * //TODO code cleanup public List<Map<String, Object>>
	 * getBacklogWorkOrdersForProject(Integer projectID) { return
	 * resourceRequestDAO.getBacklogWorkOrdersForProject(projectID); }
	 */
	public List<Map<String, Object>> getAllPositionsCount(Integer projectID) {
		return resourceRequestDAO.getAllPositionsCount(projectID);
	}

	public Map<String, Object> getSubServiceAreaPCode(int serviceAreaID) {
		return resourceRequestDAO.getSubServiceAreaPCode(serviceAreaID);
	}

	public List<Map<String, Object>> getJobRoles() {
		return resourceRequestDAO.getJobRoles();
	}

	public List<Map<String, Object>> getAllCertifications(String Issuer) {
		return resourceRequestDAO.getAllCertifications(Issuer);
	}

	public List<Map<String, Object>> getUniqueIssuer() {
		return resourceRequestDAO.getUniqueIssuer();
	}

	public List<Map<String, Object>> getOnsiteLocations() {
		return resourceRequestDAO.getOnsiteLocations();
	}

	public List<Map<String, Object>> getPositionStatus() {
		return resourceRequestDAO.getPositionStatus();
	}

	public List<Map<String, Object>> getFilteredCompetences(String competenceString) {
		return resourceRequestDAO.getFilteredCompetences(competenceString);
	}

	/**
	 * 
	 * 
	 * @param projectID
	 * @param startDate
	 * @param endDate
	 * @param term
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> getSignumsFilteredForEngEngagement(Integer projectID, String startDate,
			String endDate, String term) {

		return resourceRequestDAO.getSignumsFilteredForEngEngagement(projectID, startDate, endDate, term);
	}

	/**
	 * Download API for downloading Engineering Engagement data in excel
	 * 
	 * @author ekmbuma
	 * 
	 * @param signumID
	 * @param projectID
	 * @param startDate1
	 * @param endDate1
	 * @param term
	 * @return DownloadTemplateModel
	 * @throws ParseException
	 */
	public DownloadTemplateModel downlaodEngEngagementDataInExcel(String signumID, Integer projectID, String startDate,
			String endDate, String term) throws ParseException {
		DownloadTemplateModel downloadTemplateModel = new DownloadTemplateModel();

		String fName = (projectID != 0 ? (projectID + AppConstants.CHAR_HYPHEN) : StringUtils.EMPTY)
				+ "EngineerEngagement-"
				+ (DateTimeUtil.convertDateToString(new Date(), AppConstants.SIMPLE_DATE_FORMAT))
				+ AppConstants.EXTENSION_XLSX;

		List<Map<String, Object>> arrangedData = new ArrayList<Map<String, Object>>();

		Date startDate1 = DateTimeUtil.convertStringToDate(startDate, AppConstants.DEFAULT_DATE_FORMAT);
		Date endDate1 = DateTimeUtil.convertStringToDate(endDate, AppConstants.DEFAULT_DATE_FORMAT);
		List<String> dateRange = getListOfDaysBetweenDates(startDate1, endDate1);

		List<String> signumForProject = new LinkedList<String>();
		Map<String, Map<String, EngagementDetails>> engEngagementData = new HashMap<>();

		List<Map<String, Object>> userSignum = getAllSignumForProject(signumID, projectID, startDate, endDate);

		for (Map<String, Object> signum : userSignum) {
			signumForProject.add((String) signum.get(SIGNUM));
		}

		if (projectID == 0) {
			userSignum.clear();
			EmployeeModel emp = this.activityMasterService.getEmployeeBySignum(signumID);

			Map<String, Object> temp = new HashMap<String, Object>();
			temp.put(SIGNUM, emp.getSignum());
			temp.put(EMPLOYEE_NAME, emp.getEmployeeName());

			userSignum.add(temp);
		}

		engEngagementData = getHoursBookedInInterval(signumID, signumForProject, projectID, startDate, endDate);

		for (Map<String, Object> user : userSignum) {

			String empName = user.get(SIGNUM) + " (" + user.get(EMPLOYEE_NAME) + ")";

			Map<String, EngagementDetails> userData = engEngagementData.get(empName);
			Map<String, Object> data = new HashMap<String, Object>();

			if (engEngagementData.keySet().contains(empName)) {
				data.put(RESOURCE, empName);

				if (startDate1.before(new Date()) && endDate1.after(new Date())) {
					data.put(BACKLOGHOURS,
							userData.get(DateTimeUtil.convertDateToString(new Date(), AppConstants.DEFAULT_DATE_FORMAT))
									.getBacklogHours() + HOURS);
				} else {
					data.put(BACKLOGHOURS, "NA");
				}

				for (String date : dateRange) {

					double totalHrs = userData.get(date).getTotalHours();
					BigDecimal bd = new BigDecimal(totalHrs).setScale(2, RoundingMode.HALF_EVEN);
					totalHrs = bd.doubleValue();

					data.put(date, totalHrs + HOURS + AppConstants.FORWARD_SLASH_WITH_SPACE
							+ userData.get(date).getTotalNoOfWos() + WO);
				}
				arrangedData.add(data);
			}
		}

		LOG.info("arrangedData " + arrangedData);
		byte[] fData = null;
		if (arrangedData.size() != 0) {
			fData = generateXlsFile(arrangedData, startDate, endDate);

		}

		downloadTemplateModel.setpFileContent(fData);
		downloadTemplateModel.setpFileName(fName);

		return downloadTemplateModel;
	}

	/**
	 * @author ekmbuma
	 * 
	 * @param result
	 * @param startDate1
	 * @param endDate1
	 * @return byte[]
	 * @throws ParseException
	 */
	private byte[] generateXlsFile(List<Map<String, Object>> result, String startDate, String endDate)
			throws ParseException {

		List<String> dateRange = getListOfDaysBetweenDates(
				DateTimeUtil.convertStringToDate(startDate, AppConstants.DEFAULT_DATE_FORMAT),
				DateTimeUtil.convertStringToDate(endDate, AppConstants.DEFAULT_DATE_FORMAT));

		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try {
			Row row = sheet.createRow(0);
			int col = 0;

			// create header
			Row header = sheet.createRow(0);
			header.createCell(0).setCellValue(RESOURCE.toUpperCase());
			header.createCell(1).setCellValue(CURRENTBACKLOGCOUNTS.toUpperCase());

			for (int i = 0; i < dateRange.size(); i++) {
				header.createCell(i + 2).setCellValue(dateRange.get(i).toString().toUpperCase());
			}

			// write data
			for (int i = 1; i <= result.size(); i++) {
				col = 0;
				row = sheet.createRow(i);

				Object[] objects = { result.get(i - 1).get(RESOURCE), result.get(i - 1).get(CURRENTBACKLOGCOUNTS),
						result.get(i - 1).get(dateRange.get(0)), result.get(i - 1).get(dateRange.get(1)),
						result.get(i - 1).get(dateRange.get(2)), result.get(i - 1).get(dateRange.get(3)),
						result.get(i - 1).get(dateRange.get(4)), result.get(i - 1).get(dateRange.get(5)),
						result.get(i - 1).get(dateRange.get(6)) };

				for (Object obj : objects) {
					Cell cell = row.createCell(col++);
					if (obj instanceof String) {
						cell.setCellValue((String) obj);
					} else if (obj instanceof Double) {
						cell.setCellValue((Double) obj);
					} else if (obj instanceof Integer) {
						cell.setCellValue((Integer) obj);
					}
				}
			}

			workbook.write(baos);

		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] xls = baos.toByteArray();

		return xls;
	}

	/**
	 * @author ekmbuma
	 * 
	 * @param signumID
	 * @param projectID
	 * @param startDate1
	 * @param endDate1
	 * @return List<Map<String,Object>>
	 */
	private List<Map<String, Object>> getAllSignumForProject(String term, Integer projectID, String startDate1,
			String endDate1) {
		return resourceRequestDAO.getAllSignumForProject(term, projectID, startDate1, endDate1);
	}

	public Map<String, Object> getEEDashboardData(String signumID, Integer projectID, String startDate, String endDate,
			String term, DataTableRequest dataTableReq) {
		Map<String, Object> response = new HashMap<>();
		Map<String, Map<String, EEDashboardDataModel>> resultData = new HashMap<>();
		List<Map<String, Object>> formattedResponse = new ArrayList<>();
		List<Map<String, Object>> allSignumforProject = new LinkedList<>();

		allSignumforProject = this.getEmployeesInProject(projectID, signumID, startDate, endDate, term, dataTableReq);

		if (allSignumforProject.isEmpty()) {
			response.put("data", formattedResponse);
			response.put("draw", dataTableReq.getDraw());
			response.put("responseMessage",
					"The specified signum is not associated with the project.Please enter valid signum!");
			response.put(RECORDS_TOTAL, 0);
			response.put(RECORDS_FILTERED, 0);
			return response;
		}

		// fetch Closed WO Counts
		resultData = getDashboardData(signumID, allSignumforProject, projectID, startDate, endDate);

		// fetch backlog WO counts
		List<Map<String, Object>> backlogWOCounts = getBacklogWOCounts(projectID, allSignumforProject);

		// To change the response compatible for server side pagination
		Iterator<String> it = resultData.keySet().iterator();
		while (it.hasNext()) {
			Map<String, EEDashboardDataModel> dates = new HashMap<>();
			Map<String, Object> tmp = new HashMap<>();

			String userSignum = it.next();
			dates = resultData.get(userSignum);

			tmp.put(SIGNUM.toLowerCase(), userSignum);
			tmp.put("dates", dates);

//			if (CollectionUtils.isEmpty(backlogWOCounts)) {
//				tmp.put("backlogCounts", 0);
//			}

			if (!resultData.containsKey("backlogCounts")) {
				for (Map<String, Object> map : backlogWOCounts) {
					String[] signumSplit = userSignum.split(" ");
					String signum = StringUtils.trim(signumSplit[0]);
					if (signum.equalsIgnoreCase((String) map.get("SIGNUMID"))) {
						tmp.put("backlogCounts", map.get("COUNTS"));
					}
				}

			}else {
				tmp.put("backlogCounts", 0);				
			}
			formattedResponse.add(tmp);
		}

		response.put("data", formattedResponse);
		response.put("draw", dataTableReq.getDraw());
		response.put("responseMessage", "Success");

		if (MapUtils.isNotEmpty(resultData)) {
			if (projectID != 0 && StringUtils.isBlank(term)) {
				response.put(RECORDS_TOTAL, allSignumforProject.get(0).get(TOTAL_COUNTS));
				response.put(RECORDS_FILTERED, allSignumforProject.get(0).get(TOTAL_COUNTS));
			} else {
				response.put(RECORDS_TOTAL, 1);
				response.put(RECORDS_FILTERED, 1);
			}
		} else {
			response.put(RECORDS_TOTAL, 0);
			response.put(RECORDS_FILTERED, 0);
		}

		return response;
	}

	private List<Map<String, Object>> getBacklogWOCounts(Integer projectID,
			List<Map<String, Object>> allSignumforProject) {
		// BACKLOG COUNTS
		List<String> signums = new ArrayList<>();

		// list of signum
		for (Map<String, Object> map : allSignumforProject) {
			signums.add((String) map.get("Signum"));
		}

		return resourceRequestDAO.getBacklogWOCounts(projectID, signums);
	}

	private List<Map<String, Object>> getEmployeesInProject(Integer projectID, String signumID, String startDate,
			String endDate, String term, DataTableRequest dataTableReq) {
		List<Map<String, Object>> employeeList = new LinkedList<>();

		if (projectID == 0) {
			Map<String, Object> temp = new HashMap<>();
			EmployeeModel emp = activityMasterService.getEmployeeBySignum(signumID);
			temp.put("Signum", emp.getSignum());
			temp.put("EmployeeName", emp.getEmployeeName());
			temp.put("TOTALCNT", 1);

			employeeList.add(temp);

			return employeeList;
		} else {
			employeeList = resourceRequestDAO.getEmployeesInProject(projectID, startDate, endDate, dataTableReq, term);

			if (StringUtils.isNotBlank(term)) {
				employeeList = employeeList.stream().filter(emp -> emp.get("Signum").equals(term)).collect(Collectors.toList());
			}
			
		}

//		// Handle Global Search By Signum
//		if (StringUtils.isNotBlank(term)) {
//			Map<String, Object> temp = new HashMap<>();
//			EmployeeModel emp = activityMasterService.getEmployeeBySignum(term);
//			temp.put("Signum", emp.getSignum());
//			temp.put("EmployeeName", emp.getEmployeeName());
//			temp.put("TOTALCNT", 1);
//
//			employeeList.add(temp);
//
//			return employeeList;
//		}
//
//		// Handle No Signum filter
//		if (projectID != 0 && StringUtils.isBlank(term)) {
//			employeeList = resourceRequestDAO.getEmployeesInProject(projectID, startDate, endDate, dataTableReq, term);
//		}

		return employeeList;

	}

	public Map<String, Map<String, EEDashboardDataModel>> getDashboardData(String signumID,
			List<Map<String, Object>> signumForProject, Integer projectID, String startDate, String endDate) {

		Date startDate1 = DateTimeUtil.convertStringToDate(startDate, AppConstants.DEFAULT_DATE_FORMAT);
		Date endDate1 = DateTimeUtil.convertStringToDate(endDate, AppConstants.DEFAULT_DATE_FORMAT);

		List<String> dateRange = getListOfDaysBetweenDates(startDate1, endDate1);

		Map<String, Map<String, EEDashboardDataModel>> resultData = new HashMap<>();

//		if (CollectionUtils.size(signumForProject) < 1) {
//			Map<String, EEDashboardDataModel> userData = new LinkedHashMap<>();
//			EmployeeModel empDetail = this.activityMasterService
//					.getEmployeeBySignum(signumForProject.get(0).toString());
//			for (String date : dateRange) {
//				userData.put(date, new EEDashboardDataModel());
//			}
//			resultData.put(signumForProject.get(0) + " (" + empDetail.getEmployeeName() + ")", userData);
//		} else {

			for (Map<String, Object> empData : signumForProject) {
				Map<String, EEDashboardDataModel> userData;

				if (!resultData.containsKey(empData.get("Signum") + " (" + empData.get("EmployeeName") + ")")) {

					userData = new LinkedHashMap<>();
					for (String date : dateRange) {
						userData.put(date, new EEDashboardDataModel());
					}
					List<Map<String, Object>> closedWOCounts = resourceRequestDAO.getClosedWOCounts(projectID,
							(String) empData.get("Signum"), startDate, endDate);

					for (String date : dateRange) {
						for (Map<String, Object> map : closedWOCounts) {
							EEDashboardDataModel dateWise = new EEDashboardDataModel();
							if (map.get("Date").equals(date)) {
								dateWise.setClosedWOCounts((int) map.get("Counts"));
								userData.put((String) map.get("Date"), dateWise);

							}
						}
					}

					resultData.put(empData.get("Signum") + " (" + empData.get("EmployeeName") + ")", userData);
				}
			}
//		}

		return resultData;
	}

	private void enginnerDetailsModelSetHours(String signumID, Integer projectID, String date,EngineerDetailsModel details) {
		List<Map<String, Object>> manualHours;
		List<Map<String, Object>> automaticHours;
		List<Map<String, Object>> projectAdhocHours;
		List<Map<String, Object>> internalAdhocHours;
		
		List<LeavePlanModel> leaveHours;
		// manual hours
		manualHours = resourceRequestDAO.getManualHours(projectID, signumID, date);
		if (CollectionUtils.isNotEmpty(manualHours)) {
			details.setManualHours((double) manualHours.get(0).get("ManualHours"));
		}
		// digital hours
		automaticHours = resourceRequestDAO.getAutomaticHours(projectID, signumID, date);
		if (CollectionUtils.isNotEmpty(automaticHours)) {
			details.setDigitalHours((double) automaticHours.get(0).get("AutomatedHours"));
		}
		// project adhoc hours
		projectAdhocHours = resourceRequestDAO.getProjectAdhocHours(projectID, signumID, date);
		if (CollectionUtils.isNotEmpty(projectAdhocHours)) {
			details.setProjectAdhocHours((double) projectAdhocHours.get(0).get("ProjectAdhocHours"));
		}
		// internal adhoc hours
		internalAdhocHours = resourceRequestDAO.getInternalAdhocHours(0, signumID, date);
		if (CollectionUtils.isNotEmpty(internalAdhocHours)) {
			details.setInternalAdhocHours((double) internalAdhocHours.get(0).get("InternalAdhocHours"));
		}
		// leave hours
		leaveHours = resourceRequestDAO.getLeaveHours(signumID, date);
		if (CollectionUtils.isNotEmpty(leaveHours)) {
			if (CollectionUtils.isNotEmpty(leaveHours)) {
				for (LeavePlanModel leaveData : leaveHours) {
					if (leaveData.getLeaveHours() == 0) {
						details.setLeaveHours(8.5);
					} else {
						details.setLeaveHours(leaveData.getLeaveHours());
					}
				}
			}
		}
	}

	public ResponseEntity<Response<EngineerDetailsModel>> getEngineerDetails(String signumID, Integer projectID, String date) throws ParseException {
		// Selected Date
		Date selectedDate = DateTimeUtil.convertStringToDate(date, AppConstants.DEFAULT_DATE_FORMAT);
		Date today = DateTimeUtil.convertStringToDate(DateTimeUtil.convertDateToString(new Date(), AppConstants.DEFAULT_DATE_FORMAT), AppConstants.DEFAULT_DATE_FORMAT);
		
		Response<EngineerDetailsModel> response = new Response<>();
		
		List<Map<String, Object>> plannedAssignedWOCount;
		List<Map<String, Object>> inProgressWOcount;
		EngineerDetailsModel details = new EngineerDetailsModel();
		if (projectID == null) {
			projectID = 0;
		}
			
		//Conditions 
		if (selectedDate.before(today)) {
			enginnerDetailsModelSetHours(signumID, projectID, date, details);
		} 
		
		else if (selectedDate.after(today)) {
			enginnerDetailsModelSetHours(signumID, projectID, date, details);
			
			// Planned and Assigned WO Count
			plannedAssignedWOCount = resourceRequestDAO.getPlannedAssignedWOCount(projectID, signumID, date);
			details.setPlannedAssignedWOCount((int) plannedAssignedWOCount.get(0).get("PlannedAssignedWOCount"));
		} 
		else {
			enginnerDetailsModelSetHours(signumID, projectID, date, details);
			
			// Planned and Assigned WO Count
			plannedAssignedWOCount = resourceRequestDAO.getPlannedAssignedWOCount(projectID, signumID, date);
			details.setPlannedAssignedWOCount((int) plannedAssignedWOCount.get(0).get("PlannedAssignedWOCount"));
			
			// InProgress WO Count
			inProgressWOcount = resourceRequestDAO.getInProgressWOcount(projectID, signumID, date);
			details.setInprogressWOCount((int) inProgressWOcount.get(0).get("InProgressWOCount"));

		}
		response.setResponseData(details);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	public DownloadTemplateModel downlaodEEDashboardData(String signumID, Integer projectID, String startDate,
			String endDate, String term) throws ParseException {
		DownloadTemplateModel downloadTemplateModel = new DownloadTemplateModel();

		String fName = (projectID != 0 ? (projectID + AppConstants.CHAR_HYPHEN) : StringUtils.EMPTY)
				+ "EngineerEngagementDashboard-"
				+ (DateTimeUtil.convertDateToString(new Date(), AppConstants.SIMPLE_DATE_FORMAT))
				+ AppConstants.EXTENSION_XLSX;

		// Raw Data
		List<Map<String, Object>> arrangedData = new ArrayList<>();

		Date startDate1 = DateTimeUtil.convertStringToDate(startDate, AppConstants.DEFAULT_DATE_FORMAT);
		Date endDate1 = DateTimeUtil.convertStringToDate(endDate, AppConstants.DEFAULT_DATE_FORMAT);
		List<String> dateRange = getListOfDaysBetweenDates(startDate1, endDate1);

		Map<String, Map<String, EEDashboardDataModel>> dashboardData = new HashMap<>();

		List<Map<String, Object>> userSignum = getAllSignumForProject(term, projectID, startDate, endDate);

		if (projectID == 0) {
			userSignum.clear();
			EmployeeModel emp = this.activityMasterService.getEmployeeBySignum(signumID);

			Map<String, Object> temp = new HashMap<String, Object>();
			temp.put(SIGNUM, emp.getSignum());
			temp.put(EMPLOYEE_NAME, emp.getEmployeeName());

			userSignum.add(temp);
		}

		if(CollectionUtils.isNotEmpty(userSignum)) {
			dashboardData = getDashboardData(signumID, userSignum, projectID, startDate, endDate);

			List<Map<String, Object>> backlogWOCounts = getBacklogWOCounts(projectID, userSignum);

			for (Map<String, Object> user : userSignum) {

				String empName = user.get(SIGNUM) + " (" + user.get(EMPLOYEE_NAME) + ")";

				Map<String, EEDashboardDataModel> userData = dashboardData.get(empName);
				Map<String, Object> data = new HashMap<>();

				if (dashboardData.keySet().contains(empName)) {
					data.put(RESOURCE, empName);
					// Backlog counts
					if (!dashboardData.containsKey(CURRENTBACKLOGCOUNTS)) {
						for (Map<String, Object> map : backlogWOCounts) {
							if (((String) user.get(SIGNUM)).equalsIgnoreCase((String) map.get("SIGNUMID"))) {
								data.put(CURRENTBACKLOGCOUNTS, map.get("COUNTS"));
							}
						}

					}else {
						data.put(CURRENTBACKLOGCOUNTS, 0);
					}				
					for (String date : dateRange) {
						int closedWOCounts = userData.get(date).getClosedWOCounts();
						data.put(date, closedWOCounts + " closed WO");
					}
					arrangedData.add(data);
				}
			}
			
		}

		LOG.info("arrangedData " + arrangedData);
		byte[] fData = null;
		if (arrangedData.size() != 0) {
			fData = generateXlsFile(arrangedData, startDate, endDate);

		}

		downloadTemplateModel.setpFileContent(fData);
		downloadTemplateModel.setpFileName(fName);

		return downloadTemplateModel;
	}

}
