package com.ericsson.isf.test.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ericsson.isf.dao.ActivityMasterDAO;
import com.ericsson.isf.dao.CompetenceDAO;
import com.ericsson.isf.exception.ApplicationException;
import com.ericsson.isf.model.DomainModel;
import com.ericsson.isf.model.EmployeeModel;
import com.ericsson.isf.model.Response;
import com.ericsson.isf.model.TechnologyModel;
import com.ericsson.isf.model.UserCompetenceModel;
import com.ericsson.isf.model.VendorModel;
import com.ericsson.isf.service.CompetenceService;
import com.ericsson.isf.service.OutlookAndEmailService;
import com.ericsson.isf.test.util.CompetenceUtil;
import com.ericsson.isf.util.AppConstants;
import com.ericsson.isf.util.CompetenceStatusEnum;

public class TestCompetenceService {

	private static final String SENT_TO_MANAGER_APPROVED_INITIATED = "'Sent To Manager','Approved','Initiated'";

	@InjectMocks
	private CompetenceService competenceService;

	@Mock
	private CompetenceDAO competenceDAOMock;

	@Mock
	private OutlookAndEmailService outlookAndEmailServiceMock;

	@Mock
	private ActivityMasterDAO activityMasterDAOMock;

	private HashMap<String, Object> deliveryCompetanceDetailMap = new HashMap<String, Object>();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		deliveryCompetanceDetailMap.put(CompetenceUtil.DELIVERY_COMPETANCE_ID, 1);
	}

	@Test
	public void testGetUserCompetenceData() {

		UserCompetenceModel reqUserCompetenceModel = new UserCompetenceModel(CompetenceUtil.LOGGED_IN_SIGNUM,
				CompetenceUtil.REQUESTED_BY_SIGNUM, Arrays.asList(CompetenceStatusEnum.INITIATED.getDisplayStatus()));
		List<Map<String, Object>> expectedResponse = CompetenceUtil
				.getExpectedResponseForGetUserCompetenceData(CompetenceStatusEnum.INITIATED.getDisplayStatus());

		// stubbing
		when(competenceDAOMock.getUserCompetenceData(reqUserCompetenceModel, CompetenceUtil.STATUS_STRING,
				CompetenceUtil.USER_ROLE)).thenReturn(expectedResponse);
		List<Map<String, Object>> actualResponse = competenceService.getUserCompetenceData(reqUserCompetenceModel,
				CompetenceUtil.USER_ROLE);

		// verify data
		Assert.assertEquals(expectedResponse, actualResponse);
		
		verify(competenceDAOMock, times(1)).getUserCompetenceData(reqUserCompetenceModel, CompetenceUtil.STATUS_STRING,
				CompetenceUtil.USER_ROLE);
		verifyNoMoreInteractions(competenceDAOMock);
	}

	@Test
	public void testInsertCompetenceData_initiateStatusWithEditableTrue() {

		List<UserCompetenceModel> userCompetenceModel = getReqModel_insertCompetenceData(0,
				CompetenceStatusEnum.INITIATED.getDisplayStatus(), true, CompetenceUtil.REQUESTED_BY_SIGNUM,
				CompetenceUtil.CHANGED_BY_ENGINEER);
		UserCompetenceModel userCompetence = userCompetenceModel.get(0);
		Response<Void> expectedResponse = new Response<Void>();
		expectedResponse.addFormMessage(String.format(CompetenceStatusEnum.INITIATED.getSuccessMsg(), 1));

		// stubbing
		when(competenceDAOMock.isValidRequest(userCompetence, 1, SENT_TO_MANAGER_APPROVED_INITIATED))
				.thenReturn(true);
		when(competenceDAOMock.insertCompetenceData(userCompetence)).thenReturn(true);
		doStubbing(userCompetenceModel, CompetenceStatusEnum.INITIATED.getDisplayStatus(),
				AppConstants.ISF_COMPETENCE_INITIATE_DELETE);

		Response<Void> actualResponse = competenceService.insertCompetenceData(userCompetenceModel);

		// verify data
		Assert.assertEquals(expectedResponse.getFormMessages(), actualResponse.getFormMessages());
		Assert.assertEquals(expectedResponse.getFormMessageCount(), actualResponse.getFormMessageCount());
		verify(competenceDAOMock, times(1)).insertCompetenceData(userCompetence);

	}

	@Test
	public void testInsertCompetenceData_initiateStatusWithEditableTrueWithAppException() {

		List<UserCompetenceModel> userCompetenceModel = getReqModel_insertCompetenceData(0,
				CompetenceStatusEnum.INITIATED.getDisplayStatus(), true, CompetenceUtil.REQUESTED_BY_SIGNUM,
				CompetenceUtil.CHANGED_BY_ENGINEER);
		UserCompetenceModel userCompetence = userCompetenceModel.get(0);
		Response<Void> expectedResponse = new Response<Void>();
		expectedResponse.addFormError("Request is already raised by " + userCompetence.getRequestedBySignum()
		+ " for competance " + userCompetence.getCompetanceID());

		// stubbing
		when(competenceDAOMock.isValidRequest(userCompetence, 1, SENT_TO_MANAGER_APPROVED_INITIATED)).thenThrow(
				new ApplicationException(500, "Request is already raised by " + userCompetence.getRequestedBySignum()
						+ " for competance " + userCompetence.getCompetanceID()));
		doStubbing(userCompetenceModel, CompetenceStatusEnum.INITIATED.getDisplayStatus(),
				AppConstants.ISF_COMPETENCE_INITIATE_DELETE);

		Response<Void> actualResponse = competenceService.insertCompetenceData(userCompetenceModel);

		// verify data
		Assert.assertEquals(expectedResponse.getFormErrorCount(), actualResponse.getFormErrorCount());
		Assert.assertEquals(expectedResponse.getFormErrors(), actualResponse.getFormErrors());
		verify(competenceDAOMock, times(0)).insertCompetenceData(userCompetence);

	}
	
	@Test
	public void testInsertCompetenceData_initiateStatusWithEditableTrueSuccessFalse() {

		List<UserCompetenceModel> userCompetenceModel = getReqModel_insertCompetenceData(0,
				CompetenceStatusEnum.INITIATED.getDisplayStatus(), true, CompetenceUtil.REQUESTED_BY_SIGNUM,
				CompetenceUtil.CHANGED_BY_ENGINEER);
		UserCompetenceModel userCompetence = userCompetenceModel.get(0);
		Response<Void> expectedResponse = new Response<Void>();
		expectedResponse.addFormError(String.format(CompetenceStatusEnum.INITIATED. getErrorMsg(), 1));

		// stubbing
		when(competenceDAOMock.isValidRequest(userCompetence, 1, SENT_TO_MANAGER_APPROVED_INITIATED))
				.thenReturn(true);
		when(competenceDAOMock.insertCompetenceData(userCompetence)).thenReturn(false);
		doStubbing(userCompetenceModel, CompetenceStatusEnum.INITIATED.getDisplayStatus(),
				AppConstants.ISF_COMPETENCE_INITIATE_DELETE);

		Response<Void> actualResponse = competenceService.insertCompetenceData(userCompetenceModel);

		// verify data
		Assert.assertEquals(expectedResponse.getFormErrors(), actualResponse.getFormErrors());
		Assert.assertEquals(expectedResponse.getFormErrorCount(), actualResponse.getFormErrorCount());
		verify(competenceDAOMock, times(1)).insertCompetenceData(userCompetence);

	}

	@Test
	public void testInsertCompetenceData_SentToManagerStatusWithEditableTrue() {

		List<UserCompetenceModel> userCompetenceModel = getReqModel_insertCompetenceData(1,
				CompetenceStatusEnum.SENT_TO_MANAGER.getDisplayStatus(), true, CompetenceUtil.REQUESTED_BY_SIGNUM,
				CompetenceUtil.CHANGED_BY_ENGINEER);
		UserCompetenceModel userCompetence = userCompetenceModel.get(0);
		Response<Void> expectedResponse = new Response<Void>();
		expectedResponse.addFormMessage(String.format(CompetenceStatusEnum.SENT_TO_MANAGER.getSuccessMsg(), 1));

		// stubbing
		when(competenceDAOMock.isValidRequest(userCompetence, 1, SENT_TO_MANAGER_APPROVED_INITIATED))
				.thenReturn(true);
		when(competenceDAOMock.insertCompetenceData(userCompetence)).thenReturn(true);
		doStubbing(userCompetenceModel, CompetenceStatusEnum.SENT_TO_MANAGER.getDisplayStatus(),
				AppConstants.ISF_COMPETENCE_REQUEST);

		Response<Void> actualResponse = competenceService.insertCompetenceData(userCompetenceModel);

		// verify data
		Assert.assertEquals(expectedResponse.getFormMessages(), actualResponse.getFormMessages());
		Assert.assertEquals(expectedResponse.getFormMessageCount(), actualResponse.getFormMessageCount());
		verify(competenceDAOMock, times(1)).insertCompetenceData(userCompetence);

	}

	@Test
	public void testInsertCompetenceData_SentToManagerStatusWithEditableFalse() {

		List<UserCompetenceModel> userCompetenceModel = getReqModel_insertCompetenceData(1,
				CompetenceStatusEnum.SENT_TO_MANAGER.getDisplayStatus(), false, CompetenceUtil.REQUESTED_BY_SIGNUM,
				CompetenceUtil.CHANGED_BY_ENGINEER);
		UserCompetenceModel userCompetence = userCompetenceModel.get(0);
		Response<Void> expectedResponse = new Response<Void>();
		expectedResponse.addFormMessage(String.format(CompetenceStatusEnum.SENT_TO_MANAGER.getSuccessMsg(), 1));

		// stubbing
		when(competenceDAOMock.getUserCompetenceRow(userCompetence.getId()))
				.thenReturn(userCompetenceModel.get(0));
		when(competenceDAOMock.insertCompetenceData(userCompetence)).thenReturn(true);
		doStubbing(userCompetenceModel, CompetenceStatusEnum.SENT_TO_MANAGER.getDisplayStatus(),
				AppConstants.ISF_COMPETENCE_REQUEST);

		Response<Void> actualResponse = competenceService.insertCompetenceData(userCompetenceModel);

		// verify data
		Assert.assertEquals(expectedResponse.getFormMessages(), actualResponse.getFormMessages());
		Assert.assertEquals(expectedResponse.getFormMessageCount(), actualResponse.getFormMessageCount());
		verify(competenceDAOMock, times(1)).insertCompetenceData(userCompetence);

	}

	@Test
	public void testInsertCompetenceData_ApproveStatus() {

		List<UserCompetenceModel> userCompetenceModel = getReqModel_insertCompetenceData(1,
				CompetenceStatusEnum.APPROVED.getDisplayStatus(), false, CompetenceUtil.LM_SIGNUM, CompetenceUtil.CHANGED_BY_LM);
		UserCompetenceModel userCompetence = userCompetenceModel.get(0);
		Response<Void> expectedResponse = new Response<Void>();
		expectedResponse.addFormMessage(String.format(CompetenceStatusEnum.APPROVED.getSuccessMsg(), 1));

		// stubbing
		when(competenceDAOMock.getUserCompetenceRow(userCompetence.getId())).thenReturn(userCompetence);
		when(competenceDAOMock.insertCompetenceData(userCompetence)).thenReturn(true);
		doStubbing(userCompetenceModel, CompetenceStatusEnum.APPROVED.getDisplayStatus(),
				AppConstants.ISF_COMPETENCE_APPROVE_REJECT);

		Response<Void> actualResponse = competenceService.insertCompetenceData(userCompetenceModel);

		// verify data
		Assert.assertEquals(expectedResponse.getFormMessages(), actualResponse.getFormMessages());
		Assert.assertEquals(expectedResponse.getFormMessageCount(), actualResponse.getFormMessageCount());
		verify(competenceDAOMock, times(1)).insertCompetenceData(userCompetence);

	}

	@Test
	public void testInsertCompetenceData_ApproveStatusWithException() {

		List<UserCompetenceModel> userCompetenceModel = getReqModel_insertCompetenceData(1,
				CompetenceStatusEnum.APPROVED.getDisplayStatus(), false, CompetenceUtil.LM_SIGNUM, CompetenceUtil.CHANGED_BY_LM);
		UserCompetenceModel userCompetence = userCompetenceModel.get(0);
		Response<Void> expectedResponse = new Response<Void>();
		expectedResponse.addFormError(String.format(CompetenceStatusEnum.APPROVED.getErrorMsg(), 1));

		// stubbing
		when(competenceDAOMock.getUserCompetenceRow(userCompetence.getId()))
				.thenThrow(new NullPointerException());
		doStubbing(userCompetenceModel, CompetenceStatusEnum.APPROVED.getDisplayStatus(),
				AppConstants.ISF_COMPETENCE_APPROVE_REJECT);

		Response<Void> actualResponse = competenceService.insertCompetenceData(userCompetenceModel);

		// verify data
		Assert.assertEquals(expectedResponse.getFormErrors(), actualResponse.getFormErrors());
		Assert.assertEquals(expectedResponse.getFormErrorCount(), actualResponse.getFormErrorCount());
		verify(competenceDAOMock, times(0)).insertCompetenceData(userCompetence);

	}

	@Test
	public void testInsertCompetenceData_RejectStatus() {

		List<UserCompetenceModel> userCompetenceModel = getReqModel_insertCompetenceData(1,
				CompetenceStatusEnum.REJECTED.getDisplayStatus(), false, CompetenceUtil.LM_SIGNUM, CompetenceUtil.CHANGED_BY_LM);
		UserCompetenceModel userCompetence = userCompetenceModel.get(0);
		Response<Void> expectedResponse = new Response<Void>();
		expectedResponse.addFormMessage(String.format(CompetenceStatusEnum.REJECTED.getSuccessMsg(), 1));

		// stubbing
		when(competenceDAOMock.getUserCompetenceRow(userCompetence.getId())).thenReturn(userCompetence);
		when(competenceDAOMock.isValidRequest(userCompetence, 1, "'Rejected'"))
		.thenReturn(true);
		when(competenceDAOMock.insertCompetenceData(userCompetence)).thenReturn(true);

		doStubbing(userCompetenceModel, CompetenceStatusEnum.REJECTED.getDisplayStatus(),
				AppConstants.ISF_COMPETENCE_APPROVE_REJECT);

		Response<Void> actualResponse = competenceService.insertCompetenceData(userCompetenceModel);

		// verify data
		Assert.assertEquals(expectedResponse.getFormMessages(), actualResponse.getFormMessages());
		Assert.assertEquals(expectedResponse.getFormMessageCount(), actualResponse.getFormMessageCount());
		verify(competenceDAOMock, times(1)).insertCompetenceData(userCompetence);

	}

	private List<UserCompetenceModel> getReqModel_insertCompetenceData(int systemID, String status, boolean isEditable,
			String requestedBySignum, String changedBy) {
		List<UserCompetenceModel> userCompetenceModel = new LinkedList<UserCompetenceModel>();
		UserCompetenceModel reqUserCompetenceModel = new UserCompetenceModel(systemID, CompetenceUtil.LOGGED_IN_SIGNUM,
				requestedBySignum, 1, 1, 1, 0, status, changedBy, CompetenceUtil.REQUESTED_BY_SIGNUM, 1, 1, null, 1,
				isEditable, 0, null, CompetenceUtil.LM_SIGNUM);
		userCompetenceModel.add(reqUserCompetenceModel);
		return userCompetenceModel;
	}

	@SuppressWarnings("serial")
	private void doStubbing(List<UserCompetenceModel> userCompetenceModel, String displayStatus, String templateId) {

		HashMap<String, Object> deliveryCompetanceDetailMap = new HashMap<String, Object>();
		UserCompetenceModel userCompetence = userCompetenceModel.get(0);
		deliveryCompetanceDetailMap.put(StringUtils.capitalize(CompetenceUtil.DELIVERY_COMPETANCE_ID), 1);
		EmployeeModel employee = new EmployeeModel();
		employee.setEmployeeEmailId("emp email");
		employee.setEmployeeName("emp eakinhm");

		EmployeeModel manager = new EmployeeModel();
		manager.setEmployeeEmailId("man email");
		manager.setEmployeeName("man eakinhm");

		when(competenceDAOMock.getDeliveryCompetanceDetail(userCompetence)).thenReturn(deliveryCompetanceDetailMap);
		when(competenceDAOMock.getManagerSignum(userCompetence.getRequestedBySignum()))
				.thenReturn(CompetenceUtil.LM_SIGNUM);
		when(competenceDAOMock.getManagerSignum(userCompetence.getLmSignum())).thenReturn(CompetenceUtil.SLM_SIGNUM);
		when(outlookAndEmailServiceMock.sendMail(templateId, new HashMap<String, Object>())).thenReturn(true);
		when(activityMasterDAOMock.getEmployeeBySignum(CompetenceUtil.REQUESTED_BY_SIGNUM)).thenReturn(employee);
		when(activityMasterDAOMock.getEmployeeBySignum(CompetenceUtil.LM_SIGNUM)).thenReturn(manager);

		when(competenceDAOMock.getDeployedEnv()).thenReturn("Env");
		List<VendorModel> vmList = new ArrayList<VendorModel>() {
			{
				add(new VendorModel(1, "vendor1", true));
			}
		};
		when(activityMasterDAOMock.getVendorDetailsByID(1, null)).thenReturn(vmList);
		when(competenceDAOMock.getCompetenceGradeById(1)).thenReturn("compGrade1");
		when(competenceDAOMock.getCompetenceServiceAreaById(1)).thenReturn("sa");
		when(competenceDAOMock.getCompetenceUpgradeById(1)).thenReturn("CU");
		List<TechnologyModel> tmList = new ArrayList<TechnologyModel>() {
			{
				add(new TechnologyModel(1, "tech1", true));
			}
		};
		when(activityMasterDAOMock.getTechnologyDetailsByID(1, null)).thenReturn(tmList);
		List<DomainModel> dmList = new ArrayList<DomainModel>() {
			{
				add(new DomainModel(1, "dm1", "subdm1", true));
			}
		};
		when(activityMasterDAOMock.getDomainDetailsByID(1, null)).thenReturn(dmList);
	}
}
