package com.ericsson.isf.azure.keyVault.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ericsson.isf.config.ApplicationConfigurations;
import com.ericsson.isf.model.AzureAppModel;
import com.ericsson.isf.model.VaultModel;
import com.ericsson.isf.service.AzureService;
import com.ericsson.isf.util.AppUtil;
import com.ericsson.isf.util.ConfigurationFilesConstants;
import com.microsoft.azure.keyvault.KeyVaultClient;
import com.microsoft.azure.keyvault.models.SecretBundle;

/**
 * 
 */
@Service
public class ClientSecretKeyValueProvider {

	@Autowired
	private ApplicationConfigurations configurations;

	@Autowired
	private AzureService azureService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ClientSecretKeyValueProvider.class);

	public String getSecretValue(String secretType) {

		
		String appName = configurations.getStringProperty(ConfigurationFilesConstants.AZURE_APP_NAME);
		String keyVaultName = configurations.getStringProperty(ConfigurationFilesConstants.AZURE_KEYVAULT_NAME);
		
	//	String secretValueName = configurations.getStringProperty(ConfigurationFilesConstants.AZURE_APP_NAME);
		VaultModel vaultModel = azureService.getActiveSecretInfoByVaultNameAndSecretType(keyVaultName, secretType);
		AzureAppModel azureAppModel = azureService.getInfoByAzureAppName(appName);
		
		return getSecretValue(azureAppModel, vaultModel);
	}
	
	public String getSecretValue(AzureAppModel azureAppModel, VaultModel vaultModel) {

		// ClientSecretKeyVaultCredential is the implementation of KeyVaultCredentials

		KeyVaultClient client = new KeyVaultClient(new ClientSecretKeyVaultCredential(azureAppModel.getClientID(),
				AppUtil.DecryptText(azureAppModel.getClientSecret(), "tser")));

		// KEYVAULT_URL is the location of the keyvault to use:
		// https://yourkeyvault.vault.azure.net
		// "testSecret" is the name of the secret in the key vault
		SecretBundle secret = client.getSecret("https://" + vaultModel.getVaultName() + ".vault.azure.net/", vaultModel.getSecretName());
		LOGGER.info("secretValue success ");
		return secret.value();
	}
}