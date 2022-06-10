/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor. 
 */
package com.ericsson.isf.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ericsson.isf.dao.AzureDAO;
import com.ericsson.isf.model.AzureAppModel;
import com.ericsson.isf.model.VaultModel;

/**
 *
 * @author eakinhm
 */
@Service
public class AzureService {

	@Autowired
	private AzureDAO azureDAO;

	@Transactional("transactionManager")
	public AzureAppModel getInfoByAzureAppName(String appName) {

		return azureDAO.getInfoByAzureAppName(appName);
	}
	
	@Transactional("transactionManager")
	public VaultModel getActiveSecretInfoByVaultNameAndSecretType(String vaultName, String secretType) {

		return azureDAO.getActiveSecretInfoByVaultNameAndSecretType(vaultName, secretType);
	}
	
	@Transactional("transactionManager")
	public List<VaultModel> getAllSecretInfoByVaultNameAndSecretType(String vaultName, String secretType) {

		return azureDAO.getAllSecretInfoByVaultNameAndSecretType(vaultName, secretType);
	}

	public void activateVaultKey(VaultModel vaultModel) {

		azureDAO.activateVaultKey(vaultModel);
		
	}

	public void inactivateVaultKey(VaultModel vaultModel) {

		azureDAO.inactivateVaultKey(vaultModel);
	}

}