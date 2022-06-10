package com.ericsson.isf.test.util;

import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CompetenceUtil {

	public static final String DELIVERY_COMPETANCE_ID = "deliveryCompetanceID";
	public static final String LOGGED_IN_SIGNUM = "loggedInSignum";
	public static final String REQUESTED_BY_SIGNUM = "requestedBySignum";
	public static final String LM_SIGNUM = "lmSignum";
	public static final String SLM_SIGNUM = "slmSignum";
	public static final String CHANGED_BY_ENGINEER = "engineer";
	public static final String CHANGED_BY_LM = "LM";
	public static final String STATUS_STRING = "'Initiated'";
	public static final String USER_ROLE = "User";

	@SuppressWarnings("static-access")
	public static List<Map<String, Object>> getExpectedResponseForGetUserCompetenceData(String status) {
		List<Map<String, Object>> expectedResponse = new LinkedList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("systemID", 1);
		map.put("userCompetanceID", null);
		map.put(LOGGED_IN_SIGNUM, LOGGED_IN_SIGNUM);
		map.put(REQUESTED_BY_SIGNUM, REQUESTED_BY_SIGNUM);
		map.put(DELIVERY_COMPETANCE_ID, 1);
		map.put("baseline", 1);
		map.put("competenceUpgradeID", 1);
		map.put("parentSystemID", 1);
		map.put(LM_SIGNUM, LM_SIGNUM);
		map.put(SLM_SIGNUM, SLM_SIGNUM);
		map.put("status", status);
		map.put("changedBy", "changedByName");
		Calendar cal = Calendar.getInstance();
		map.put("createdon", cal.DATE);
		map.put("createdBy", LOGGED_IN_SIGNUM);
		map.put("rowversion", "AAAAAAABQ4c=");
		map.put("Active", true);
		map.put("BaselineName", "BaselineName");
		map.put("CompetencyUpgrade", "CompetencyUpgrade");
		map.put("Technology", "tech1");
		map.put("Vendor", "vendor1");
		map.put("DomainSubDomain", "dm1 / subdm1");
		map.put("CompetenceType", "CompetenceType");
		map.put("Competency_Service_Area", "Competency_Service_Area");
		map.put("competanceUpgradeLevel", "nextLevel");
		map.put("WBL_TOTAL_HRS", 0.33);
		map.put("CompetenceID", 22);
		map.put("ILT_TOTAL_HRS", 0.89);

		expectedResponse.add(map);
		return expectedResponse;
	}
}
