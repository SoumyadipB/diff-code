package com.ericsson.isf.bot.migration.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RpaBotFileMapModel {

	private int rpaRequestId;
	private List<RpaBotFileModel> rpaBotFileModels;
}
