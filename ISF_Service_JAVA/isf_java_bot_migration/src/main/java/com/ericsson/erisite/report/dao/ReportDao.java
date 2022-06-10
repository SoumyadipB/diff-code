package com.ericsson.erisite.report.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ericsson.erisite.report.mapper.ReportMapper;
import com.ericsson.erisite.report.model.ActivityDataModel;
import com.ericsson.erisite.report.model.ProjectDataModel;
import com.ericsson.erisite.report.model.WorkPlanFullModel;

@Repository
public class ReportDao {

	@Autowired
	private ReportMapper reportMapper;

	public void insertErisiteAcivityData(ActivityDataModel activityDataModel) {
		reportMapper.insertErisiteAcivityData(activityDataModel);
		
	}

	public void insertErisiteData(ActivityDataModel act, WorkPlanFullModel workPlanData, String siteName,
			ProjectDataModel projectDataModel) {
		reportMapper.insertErisiteData(act, workPlanData, siteName, projectDataModel);
		
	}
}
