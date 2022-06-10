package com.ericsson.isf.bot.migration.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RpaBotExecutionDetailModel 
{

	private int botID;
	private String signum;
	private int woNo;
	private int projectId;
	private int taskID;
	private String nodes;
	private String botPlateform;
	private String inputFileName;
	private String outputFileName;
	private byte[] inputDataFile;
	private byte[] outputDataFile;
	private String createdOn;
	
}
