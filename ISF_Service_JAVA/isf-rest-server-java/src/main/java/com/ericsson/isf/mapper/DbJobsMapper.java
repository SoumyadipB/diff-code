/**
 * 
 */
package com.ericsson.isf.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ericsson.isf.model.DbJobsModel;
import com.ericsson.isf.model.ServiceStartStopModel;

/**
 * @author ekarmuj
 *
 */
public interface DbJobsMapper {

	public List<DbJobsModel> startJob(@Param("procedureName") String procedureName);

	public List<DbJobsModel> stopJob(@Param("procedureName") String procedureName);
}
