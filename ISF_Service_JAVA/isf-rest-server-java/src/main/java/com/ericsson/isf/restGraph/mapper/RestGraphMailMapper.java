/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.restGraph.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ericsson.isf.restGraph.model.TokenResponse;

/**
 *
 * @author eakinhm
 */
public interface RestGraphMailMapper {

	public boolean saveRefreshToken(@Param("tokenResponse") TokenResponse tokenResponse);
	
	public Map<String, Object> getActiveRefreshToken();
	
	public void inactivateRefreshTokenByID(@Param("id") int id);

}
