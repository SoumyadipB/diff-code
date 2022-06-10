package com.ericsson.isf.util;

import java.util.Comparator;
import java.util.Date;

import com.ericsson.isf.model.ExecutionPlanDetail;

public class SortExecutionPlanDetailbyDateTime implements Comparator<ExecutionPlanDetail> {

	@Override
	public int compare(ExecutionPlanDetail executionPlanDetail1, ExecutionPlanDetail executionPlanDetail2) {

		Date date = new Date();
		Date executionPlanDetail1Date = PlannedEndDateCal.calculateDateByAddingDayAndHour(date,
				executionPlanDetail1.getDay(), executionPlanDetail1.getHour());
		
		Date executionPlanDetail2Date = PlannedEndDateCal.calculateDateByAddingDayAndHour(date,
				executionPlanDetail2.getDay(), executionPlanDetail2.getHour());
		
		return executionPlanDetail1Date.compareTo(executionPlanDetail2Date);
	}

}
