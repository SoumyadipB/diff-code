/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Mapper;
/**
 *
 * @author ejangua
 */
@Mapper
public interface ExternalInterfaceManagmentMapper {

	@Select("select top 10000 * from\r\n" + 
			"		[refData].[TBL_Employees];")
	public List<Map<String, Object>> checkIsfHealth();

}
