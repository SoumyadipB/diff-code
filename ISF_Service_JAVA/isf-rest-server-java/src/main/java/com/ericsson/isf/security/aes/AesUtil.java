package com.ericsson.isf.security.aes;

import java.security.spec.AlgorithmParameterSpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ericsson.isf.azure.keyVault.service.ClientSecretKeyValueProvider;
import com.ericsson.isf.config.ApplicationConfigurations;
import com.ericsson.isf.model.AzureAppModel;
import com.ericsson.isf.model.VaultModel;
import com.ericsson.isf.service.AppService;
import com.ericsson.isf.service.AzureService;
import com.ericsson.isf.util.AppConstants;
import com.ericsson.isf.util.ConfigurationFilesConstants;

@Component
public class AesUtil {
	
	@Autowired
	private ApplicationConfigurations configurations;
	
	@Autowired
	private AzureService azureService;
	
	@Autowired
	private ClientSecretKeyValueProvider clientSecretKeyValueProvider;
	
	private final Logger LOG = LoggerFactory.getLogger(AppService.class);
	
	private static String KEY_AES;
	
	private static String IVS_AES;
	
	private static final String APIM_AUTH_KEY = "AesSecretKey";
	
	@PostConstruct
    void init() {
		if(KEY_AES==null) {
			try {

				String keyVaultName = configurations.getStringProperty(ConfigurationFilesConstants.AZURE_KEYVAULT_NAME);

				VaultModel vaultModel = azureService.getActiveSecretInfoByVaultNameAndSecretType(keyVaultName,
						APIM_AUTH_KEY);
				AzureAppModel azureAppModel = azureService.getInfoByAzureAppName(
						configurations.getStringProperty(ConfigurationFilesConstants.AZURE_APP_NAME));

				KEY_AES = clientSecretKeyValueProvider.getSecretValue(azureAppModel, vaultModel);
				LOG.info("AES security key obtained");
			
			} catch (Exception e) {
				LOG.error("error occured while getting apim AES Key: " + e.getMessage());
			}
		}
		
		//KEY_AES="WmZq4t7w!z%C*F-J";
		IVS_AES="abcdefg123456789";
    }  
	
	public byte[] encrypt(byte[] value) {
		
        try {
            byte[] key = KEY_AES.getBytes(AppConstants.UTF_8_STRING);
            byte[] ivs = IVS_AES.getBytes(AppConstants.UTF_8_STRING);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
            AlgorithmParameterSpec paramSpec = new IvParameterSpec(ivs);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, paramSpec);
            return cipher.doFinal(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }
	
	public byte[] decrypt(byte[] value) {
        try {
            byte[] key = KEY_AES.getBytes(AppConstants.UTF_8_STRING);
            byte[] ivs = IVS_AES.getBytes(AppConstants.UTF_8_STRING);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
            AlgorithmParameterSpec paramSpec = new IvParameterSpec(ivs);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, paramSpec);
            return cipher.doFinal(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }
	
	public String encryptBase64(String value) {
		return Base64.getEncoder().encodeToString(encrypt(value.getBytes()));
	}
	
	public byte[] decryptBase64(String value) {
		return decrypt( Base64.getDecoder().decode(value));
	}

	//This Method is used to get AES and IVS key and it is called from externalInterface/getSecretAuthorizationValue
	public List<String> getAESKeyFromKeyVault() {
		List<String> keys = new ArrayList<>();
		keys.add(KEY_AES);
        keys.add(IVS_AES);
		
		return keys;
	}
}
