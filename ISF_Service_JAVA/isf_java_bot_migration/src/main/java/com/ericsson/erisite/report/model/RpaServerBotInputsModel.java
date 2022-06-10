package com.ericsson.isf.bot.migration.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RpaServerBotInputsModel {
	

	private int woid;
	private int taskID;
	private String stepId;
	private String signum;
	private String detailType;
	private String fileName;
	private byte[] fileData;
	private String bookingId;
	private int serverBotDetailId;
	private String createdOn;
	private String createdBy;
	private String updatedOn;
	private String updatedBy;
  
}
