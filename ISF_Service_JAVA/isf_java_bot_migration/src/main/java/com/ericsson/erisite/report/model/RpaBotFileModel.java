package com.ericsson.isf.bot.migration.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RpaBotFileModel {

	private int rpaRequestId;
	private String fileName;
	private String fileType;
	private byte[] dataFile;
	private int isActive;
}
