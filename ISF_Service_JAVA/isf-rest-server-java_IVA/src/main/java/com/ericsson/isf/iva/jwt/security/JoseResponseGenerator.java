package com.ericsson.isf.iva.jwt.security;

import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Base64;
import java.util.Date;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ericsson.isf.iva.exception.ApplicationException;
import com.ericsson.isf.iva.profiles.configuration.AppConfig;
import com.ericsson.isf.iva.util.AppConstants;
import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;


@Component
public class JoseResponseGenerator {

	@Autowired
	AppConfig appconfig;
	
   
    public String getNewKey() {
    	int keyBitLength = EncryptionMethod.A128CBC_HS256.cekBitLength();
    	SecretKey key;
		try {
			KeyGenerator keygenerator =KeyGenerator.getInstance("AES");
			keygenerator.init(keyBitLength);
			key =keygenerator.generateKey();
			String stringKey=Base64.getEncoder().encodeToString(key.getEncoded());
	    	System.out.println(stringKey);
	    	return stringKey;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
    }

	public String generateSignedResponse(String servletResponse) {
		try {
			
			//TODO--set expiration time
			//setting the JWT fomrat
			JWTClaimsSet claims =new JWTClaimsSet.Builder().issueTime(new Date()).issuer(AppConstants.ISF)
					.claim("response", servletResponse).build();
			
			//signing response
			SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256),claims);
			signedJWT.sign(new MACSigner(appconfig.getSecretKey()));
			
			//encrypting signed reponse
			JWEHeader header = new JWEHeader(JWEAlgorithm.DIR, EncryptionMethod.A128CBC_HS256);
			JWEObject jweObject = new JWEObject(header, new Payload(signedJWT));
			jweObject.encrypt(new DirectEncrypter(appconfig.getSecretKey()));
			
			return jweObject.serialize();
		}
		catch(JOSEException e){
			//LOG.error("JWT Token Epired and Updated at back end !!");
			throw new ApplicationException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"not able to sign and encrypt object");
		}
	}

	public String parse(String jweString) throws ParseException, JOSEException {
		JWEObject jweObject = JWEObject.parse(jweString);

		// Decrypt with private key
		jweObject.decrypt(new DirectDecrypter(appconfig.getSecretKey()));

		// Extract payload
		SignedJWT signedJWT = jweObject.getPayload().toSignedJWT();


		// Check the signature
		if(signedJWT.verify(new MACVerifier(appconfig.getSecretKey()))) {
			// Retrieve the JWT claims...
			return signedJWT.getJWTClaimsSet().getClaim("response").toString();
		}
		else {
			throw new ApplicationException(HttpServletResponse.SC_BAD_REQUEST,"mesage tampered with");
		}
	}
}
