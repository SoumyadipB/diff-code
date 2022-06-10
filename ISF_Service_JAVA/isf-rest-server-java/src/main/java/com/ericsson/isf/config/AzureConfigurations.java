package com.ericsson.isf.config;

import java.net.URISyntaxException;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.ericsson.isf.azure.keyVault.service.ClientSecretKeyValueProvider;
import com.ericsson.isf.model.AzureAppModel;
import com.ericsson.isf.model.VaultModel;
import com.ericsson.isf.service.AppService;
import com.ericsson.isf.service.AzureService;
import com.ericsson.isf.util.ConfigurationFilesConstants;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;

@Configuration
public class AzureConfigurations {

	private static final String BLOB_STORAGEKEY = "blobStoragekey";

	@Autowired
	private ApplicationConfigurations configurations;

	@Autowired
	private ClientSecretKeyValueProvider clientSecretKeyValueProvider;

	@Autowired
	private AzureService azureService;

	@Autowired
	private AppService appService;

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(AzureConfigurations.class);

	@Bean
	public CloudBlobClient cloudBlobClient() throws Exception {

		CloudStorageAccount storageAccount = getCloudStorageAccount();

		if (storageAccount == null) {
			throw new Exception("Unable to get CloudStorageAccount");
		}
		return storageAccount.createCloudBlobClient();
	}

	private CloudStorageAccount getCloudStorageAccount() {

		try {

			String keyVaultName = configurations.getStringProperty(ConfigurationFilesConstants.AZURE_KEYVAULT_NAME);

			VaultModel vaultModel = azureService.getActiveSecretInfoByVaultNameAndSecretType(keyVaultName,
					BLOB_STORAGEKEY);
			AzureAppModel azureAppModel = azureService.getInfoByAzureAppName(
					configurations.getStringProperty(ConfigurationFilesConstants.AZURE_APP_NAME));

			String secretValue = clientSecretKeyValueProvider.getSecretValue(azureAppModel, vaultModel);
			try {

				return CloudStorageAccount.parse(secretValue);
			} catch (IllegalArgumentException ex) {

				appService.sendMailforStorageKeyInvalidation(
						"Unable to build connection with Storage key 1, same will be inactivated and will try to make connection with key 2!");
				List<VaultModel> vaultModels = azureService.getAllSecretInfoByVaultNameAndSecretType(keyVaultName,
						BLOB_STORAGEKEY);

				for (VaultModel vault : vaultModels) {

					if (vault.isActive()) {

						azureService.inactivateVaultKey(vault);
					} else {

						azureService.activateVaultKey(vault);
						secretValue = clientSecretKeyValueProvider.getSecretValue(azureAppModel, vault);
						return CloudStorageAccount.parse(secretValue);
					}
				}

			}
		} catch (Exception e) {
			LOGGER.error("error occured while getting storage account: {} " , e.getMessage());
		}
		return null;

	}

	@Bean
	@Primary
	public CloudBlobContainer testBlobContainer() throws Exception {
		return cloudBlobClient().getContainerReference(
				configurations.getStringProperty(ConfigurationFilesConstants.BOT_STORE_CONTAINER));
	}
	@Bean
	public CloudBlobContainer commonBlobContainer() throws Exception {
		 CloudBlobContainer commonContainer=cloudBlobClient().getContainerReference(
				configurations.getStringProperty(ConfigurationFilesConstants.COMMON_BLOB_CONTAINER));
		 commonContainer.createIfNotExists();
		 return commonContainer;
	}
}