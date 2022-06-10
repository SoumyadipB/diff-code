package com.ericsson.isf.jwtSecurity;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ericsson.isf.model.ExternalSourceModel;
import com.ericsson.isf.model.JwtApiUser;
import com.ericsson.isf.model.JwtUser;
import com.ericsson.isf.service.AccessManagementService;
import com.ericsson.isf.util.AppConstants;

@Component
public class JwtValidator {
	
	@Autowired /*Bind to bean/pojo  */
    private AccessManagementService accessManagementService;
    
	public JwtUser validate(String token) {

        JwtUser jwtUser = null;
        try {
            Claims body = Jwts.parser()
                    .setSigningKey(AppConstants.SECRET)
                    .parseClaimsJws(token)
                    .getBody();
            
            jwtUser = new JwtUser();
            jwtUser.setOwnerSignum(((String)body.get("ownerSignum")));
            jwtUser.setExternalSourceName((String)body.get("externalSourceName"));
            
        }
        catch (Exception e) {
            System.out.println(e);
        }

        return jwtUser;
    }
    
	public JwtApiUser validateApiToken(String token,JwtApiUser jwtApiUser) {
	  int externalRefID = jwtApiUser.getExternalRefID();
	  int expirationInYear = jwtApiUser.getExpirationInYear();
	  
	  try {
    		Claims body = Jwts.parser()
    				.setSigningKey(AppConstants.SECRET)
    				.parseClaimsJws(token)
    				.getBody();
    		jwtApiUser = new JwtApiUser();
    		
    		jwtApiUser.setOwnerSignum((String)body.get("ownerSignum"));
    		jwtApiUser.setExternalRefID((int)body.get("externalRefID"));
    		jwtApiUser.setExpirationInYear((int)body.get("expirationInYear"));

    		if(externalRefID == jwtApiUser.getExternalRefID() && expirationInYear != jwtApiUser.getExpirationInYear()) {
    			ExternalSourceModel model = accessManagementService.getExternalReferenceDetails(jwtApiUser.getExternalRefID());
    	    	
    			jwtApiUser.setMessage("Token is already generated for " + model.getSourceName() );
    			System.out.println("msg : " + jwtApiUser.getMessage());
    			return jwtApiUser;
    		}
    		
    	} catch (Exception e) {
    		System.out.println(e);
    	}
    	
    	return jwtApiUser;
    }
}