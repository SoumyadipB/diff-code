package com.ericsson.isf.mapper;

import org.apache.ibatis.annotations.Param;

/**
 *
 * @author eakinhm
 */

public interface CountriesMapper {

	boolean isCountryExists(@Param("countryName") String countryName);

}
