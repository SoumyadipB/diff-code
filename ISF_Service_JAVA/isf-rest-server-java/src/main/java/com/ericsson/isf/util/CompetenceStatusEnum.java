package com.ericsson.isf.util;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author eakinhm
 *
 */
public enum CompetenceStatusEnum {

	SENT_TO_MANAGER("Sent To Manager","%d Requests has been sent for approval successfully!"
			, "Unable to send %d requests for approval. The supplied data might have problem, Please verify!"),
	APPROVED("Approved","%d Requests has been approved successfully!"
			, "Unable to approve %d requests!"),
	REJECTED("Rejected","%d Requests has been rejected successfully!"
			, "Unable to reject %d requests!"),
	INITIATED("Initiated","Request has been initiated successfully!"
			, "Unable to initiate request!"),
	DELETED("Deleted","Request has been deleted successfully!"
			, "Unable to delete request!");

	private String displayStaus;
	private String successMsg;
	private String errorMsg;

	CompetenceStatusEnum(final String displayStaus, final String successMsg, final String errorMsg) {
		this.displayStaus = displayStaus;
		this.successMsg = successMsg;
		this.errorMsg = errorMsg;
	}

	public String getDisplayStatus() {
		return this.displayStaus;
	}

	public String getSuccessMsg() {
		return this.successMsg;
	}

	public String getErrorMsg() {
		return this.errorMsg;
	}

	public static List<String> getAllStatus() {
		List<String> competenceStatusList = new LinkedList<String>();
		Arrays.stream(CompetenceStatusEnum.values())
				.forEach(competenceStatus -> competenceStatusList.add(competenceStatus.getDisplayStatus()));
		return competenceStatusList;
	}

	public static String getDisplayStatus(final String receivedStatusFromUI) {
		String displayStaus = Arrays.stream(CompetenceStatusEnum.values())
				.filter(compStatus -> compStatus.getDisplayStatus().equalsIgnoreCase(receivedStatusFromUI)).findFirst()
				.get().getDisplayStatus();
		return displayStaus;
	}

	@SuppressWarnings("serial")
	public static List<String> getUnallowedStatusForRequest() {

		List<String> unallowedStatusList = new LinkedList<String>() {
			{
				add(SENT_TO_MANAGER.getDisplayStatus());
				add(APPROVED.getDisplayStatus());
				add(INITIATED.getDisplayStatus());
			}
		};
		return unallowedStatusList;
	}

	public static CompetenceStatusEnum getEnumValue(final String status) {
		return Arrays.stream(CompetenceStatusEnum.values())
				.filter(compStatus -> compStatus.getDisplayStatus().equalsIgnoreCase(status)).findFirst().get();
	}

	public static String getUnallowedStatusStringForRequest() {

		String statusString = "";
		for (String status : getUnallowedStatusForRequest()) {
			statusString += "'" + status + "',";
		}
		return statusString.substring(0, statusString.length()-1);
	}
}
