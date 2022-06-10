/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor. 
 */
package com.ericsson.isf.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ericsson.isf.mapper.AzureMapper;
import com.ericsson.isf.model.AzureAppModel;
import com.ericsson.isf.model.VaultModel;

/**
 *
 * @author eakinhm
 */
@Repository
public class AzureDAO {

	@Qualifier("sqlSession")
	/* Create session from SQLSessionFactory */
	@Autowired
	private SqlSessionTemplate sqlSession;

	public AzureAppModel getInfoByAzureAppName(String appName) {
		AzureMapper azureMapper = sqlSession.getMapper(AzureMapper.class);
		return azureMapper.getInfoByAzureAppName(appName);
	}

	public VaultModel getActiveSecretInfoByVaultNameAndSecretType(String vaultName, String secretType) {
		AzureMapper azureMapper = sqlSession.getMapper(AzureMapper.class);
		return azureMapper.getActiveSecretInfoByVaultNameAndSecretType(vaultName, secretType);
	}
	
	public List<VaultModel> getAllSecretInfoByVaultNameAndSecretType(String vaultName, String secretType) {
		AzureMapper azureMapper = sqlSession.getMapper(AzureMapper.class);
		return azureMapper.getAllSecretInfoByVaultNameAndSecretType(vaultName, secretType);
	}

	public void activateVaultKey(VaultModel vaultModel) {
		AzureMapper azureMapper = sqlSession.getMapper(AzureMapper.class);
		azureMapper.activateVaultKey(vaultModel);
	}

	public void inactivateVaultKey(VaultModel vaultModel) {
		AzureMapper azureMapper = sqlSession.getMapper(AzureMapper.class);
		azureMapper.inactivateVaultKey(vaultModel);
	}

}
