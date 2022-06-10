package com.ericsson.isf.controller;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.ericsson.isf.jwtSecurity.JwtGenerator;
import com.ericsson.isf.model.ExternalSourceModel;
import com.ericsson.isf.model.JwtApiUser;
import com.ericsson.isf.model.JwtUser;
import com.ericsson.isf.model.Response;
import com.ericsson.isf.service.AccessManagementService;
import com.ericsson.isf.util.AppConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

/**
*
* @author ejangua
*/

@RestController
@RequestMapping("/token")

public class TokenController  {

    private static final Logger LOG = LoggerFactory.getLogger(TokenController.class);

    private JwtGenerator jwtGenerator;
    
    @Autowired
    private AccessManagementService accessManagementService;

    public TokenController(JwtGenerator jwtGenerator) {
        this.jwtGenerator = jwtGenerator;
    }

    @RequestMapping(value = "/logon", method = RequestMethod.POST)
    public String logon(@RequestBody final JwtUser jwtUser) throws ServletException {
    	
    	// getting already active tokens for source name
    	String existsToken = accessManagementService.getActiveToken(jwtUser.getExternalSourceName());
    	if(StringUtils.isNotBlank(existsToken)) {
    		return existsToken;
    	}
        return jwtGenerator.generate(jwtUser);

    }
    
    @RequestMapping(value = "/logoff", method = RequestMethod.POST)
    public void logoff(@RequestHeader("X-Auth-Token") String token) {
        jwtGenerator.logoff(token);

    }
    
    @RequestMapping(value = "/generateAPIToken", method = RequestMethod.POST)
    public Response generateAPIToken(@RequestBody JwtApiUser jwtApiUser) throws ServletException {
    	String token = "";
    	Response response = new Response();
    	List<Integer> alreadyCreatedTokenApi = new ArrayList();
    	
        int requestedAPI = 0;
        String requestedApiName = "";
		ExternalSourceModel model = accessManagementService.getExternalReferenceDetails(jwtApiUser.getExternalRefID());
		String sourceName = model.getSourceName();
		
        String tokenForExternalRedIf = accessManagementService
        		.checkTokenForExternalRefId(jwtApiUser);

        // check Token for given ExternalRefId
        if(StringUtils.isBlank(tokenForExternalRedIf)) {

        	// generate new token if token not found for given ExternalRefId. 
        	String newToken = jwtGenerator.generateAPIToken(jwtApiUser,requestedAPI);

        	response.addFormMessage("New Token is generated for " + sourceName);

        	for(int j=0; j < jwtApiUser.getRequestedAPI().size(); ++j) {
        		accessManagementService.insertTokenApi(newToken, jwtApiUser, jwtApiUser.getRequestedAPI().get(j));
        	}
        } else {
    		// if token is already created for given ExternalRefId, Compare its expiry year
            String tokenForExpComp = accessManagementService
            		.checkTokenForExternalRefId(jwtApiUser);
            
            Claims body = Jwts.parser()
    				.setSigningKey(AppConstants.SECRET)
    				.parseClaimsJws(tokenForExpComp)
    				.getBody();
    		
            int oldExpYr = (int)body.get("expirationInYear");
            int newExpYr = jwtApiUser.getExpirationInYear();
            
            //if expiry year is same then, check if, token is generated for requested API.
            if(oldExpYr == newExpYr) {
            	for(int k=0; k < jwtApiUser.getRequestedAPI().size(); ++k) {
            		requestedAPI = jwtApiUser.getRequestedAPI().get(k);
            		String existsTokenForApi = accessManagementService
                			.getActiveApiToken(requestedAPI,jwtApiUser);

            		requestedApiName = accessManagementService.getApiName(requestedAPI);

                	if(StringUtils.isNotBlank(existsTokenForApi)) {
                		alreadyCreatedTokenApi.add(requestedAPI);
                		response.addFormMessage("Token for API "
            				+ requestedApiName
            				+ " is already generated in ISF");
                	} else {
                		// if API not found, Insert the API for give ExternalRefId with same TOKEN
                		accessManagementService.insertTokenApi(tokenForExpComp, jwtApiUser, requestedAPI);
                    	response.addFormMessage("Token for API "
                    			+ requestedApiName
                    			+ " is generated.");
                	}
            	}
            }else {
    			response = new Response();
    			response.addFormMessage("Token can not be generated for existing ExternalReference Name with different Expiration date.");
            }
        }
    	response.setResponseData(alreadyCreatedTokenApi);
    	return response;
    }
}
