/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor. 
 */
package com.ericsson.isf.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ericsson.isf.model.AzureAppModel;
import com.ericsson.isf.model.VaultModel;

/**
 *
 * @author eakinhm
 *
 */
public interface AzureMapper {

	public AzureAppModel getInfoByAzureAppName(@Param("appName") String appName);

	public VaultModel getActiveSecretInfoByVaultNameAndSecretType(@Param("vaultName") String vaultName,
			@Param("secretType") String secretType);
	
	public List<VaultModel> getAllSecretInfoByVaultNameAndSecretType(@Param("vaultName") String vaultName,
			@Param("secretType") String secretType);

	public void activateVaultKey(@Param("vaultModel") VaultModel vaultModel);

	public void inactivateVaultKey(@Param("vaultModel") VaultModel vaultModel);

}