/**
 * 
 */
package com.ericsson.isf.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ericsson.isf.model.InstanceModel;

public interface IsfCustomIdMapper {

	InstanceModel getInstance(@Param("databaseIp") String databaseIp);

}
