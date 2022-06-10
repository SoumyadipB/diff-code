package com.ericsson.isf.jwtSecurity;


import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ericsson.isf.model.JwtApiUser;
import com.ericsson.isf.model.JwtUser;
import com.ericsson.isf.model.TokenApiMappingModel;
import com.ericsson.isf.model.TokenMappingModel;
import com.ericsson.isf.service.AccessManagementService;
import com.ericsson.isf.util.AppConstants;
import com.ericsson.isf.util.IsfCustomIdInsert;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtGenerator {

	@Autowired 
	private AccessManagementService accessManagementService;
	
	@Autowired
	IsfCustomIdInsert isfCustomIdInsert;
	
    public String generate(JwtUser jwtUser) throws ServletException {
 
    	jwtUser.setDecodedUserName(decrypt(jwtUser.getUserName()));
    	jwtUser.setDecodedPassword(decrypt(jwtUser.getPassword()));
    	Map<String, Object> data = this.accessManagementService.validateUserPassword(jwtUser.getDecodedUserName(), jwtUser.getDecodedPassword() );
    	if (data != null){
    		Claims claims = Jwts.claims()
                    .setSubject(jwtUser.getDecodedPassword());
            claims.put("userName", jwtUser.getDecodedUserName());
            claims.put("externalSourceName", jwtUser.getExternalSourceName());
            long nowMs = System.currentTimeMillis();
    		Date now = new Date(nowMs);
    		
    		Calendar cal = Calendar.getInstance();
    		cal.add(Calendar.YEAR, 1);
    		
            String token = Jwts.builder()
                    .setClaims(claims)
                    .signWith(SignatureAlgorithm.HS256, AppConstants.SECRET)
                    .setExpiration(cal.getTime())
                    .setIssuedAt(now)
                    .compact();
            
            TokenMappingModel tokenMappingModel = new TokenMappingModel.TokenBuilder()
            		.setToken(token)
            		.setType((String) data.get("Type"))
            		.setSourceID((int)data.get("SourceID"))
            		.setActivationDate(new Date())
            		.setExpirationDate(null)
            		.setActive(1)
            		.setSignumID(null)
            		.setEmployeeEmailID(null)
            		.build();
            		
            this.accessManagementService.insertTokenMappingDetails(tokenMappingModel);
            
            long tokenId=isfCustomIdInsert.generateCustomId(tokenMappingModel.getTokenID());
            tokenMappingModel.setTokenID(tokenId);
            
            if(StringUtils.isNotBlank(jwtUser.getExternalSourceName())) {
            	accessManagementService.insertTokenForExternalSource(jwtUser, tokenMappingModel.getToken());
            }
            return token;
    	}else {
    		throw new ServletException("User name and Does not match. Please try again!!");
    	}
    }

    public String decrypt(String encrypted) {
    	Base64.Decoder decoder = Base64.getUrlDecoder();  
    	String dStr = new String(decoder.decode(encrypted));  
		return dStr;  
    
    }
    @Transactional("transactionManager")
	public void logoff(String token) {
		this.accessManagementService.updateTokenMappingDetails(token.split(" ")[1]);
	}

	public String generateAPIToken(JwtApiUser jwtApiUser,int requestedAPI) throws ServletException {
		
		Claims claims = Jwts.claims().setSubject(jwtApiUser.getOwnerSignum());
		
		claims.put("ownerSignum", jwtApiUser.getOwnerSignum());
		claims.put("externalRefID", jwtApiUser.getExternalRefID());
		claims.put("expirationInYear", jwtApiUser.getExpirationInYear());
		
		long nowMs = System.currentTimeMillis();
		Date now = new Date(nowMs);
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, jwtApiUser.getExpirationInYear());

		
		String newToken = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, AppConstants.SECRET)
                .setExpiration(cal.getTime())
                .setIssuedAt(now)
                .compact();
		return newToken;
	}
}