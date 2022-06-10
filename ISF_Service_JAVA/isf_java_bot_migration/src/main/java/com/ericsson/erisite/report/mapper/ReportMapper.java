package com.ericsson.erisite.report.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ericsson.erisite.report.model.ActivityDataModel;
import com.ericsson.erisite.report.model.ProjectDataModel;
import com.ericsson.erisite.report.model.WorkPlanFullModel;

@Mapper
public interface ReportMapper {

	public void insertErisiteData(@Param("activityDataModel") ActivityDataModel act, @Param("workPlanData") WorkPlanFullModel workPlanData,
			@Param("siteName") String siteName,	@Param("projectDataModel") 		ProjectDataModel projectDataModel);

	public void insertErisiteAcivityData(@Param("activityDataModel") ActivityDataModel activityDataModel);
	

}
