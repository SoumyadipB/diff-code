package com.ericsson.isf.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ericsson.isf.model.EnvironmentPropertyModel;

/**
 *
 * @author eakinhm
 */

public interface EnvironmentPropertyMapper {

	List<EnvironmentPropertyModel> getEnvironmentPropertyModelByKey(@Param("key") final String key);
}
