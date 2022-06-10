package com.ericsson.isf.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ericsson.isf.mapper.CountriesMapper;

/**
 *
 * @author eakinhm
 */

@Repository
public class CountriesDao {

	@Qualifier("sqlSession")
	/* Create session from SQLSessionFactory */
	@Autowired
	private SqlSessionTemplate sqlSession;

	public boolean isCountryExists(String countryName) {
		CountriesMapper countriesMapper = sqlSession.getMapper(CountriesMapper.class);
		return countriesMapper.isCountryExists(countryName);
	}

}
