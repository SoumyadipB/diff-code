package com.ericsson.isf.iva.jwt.security;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import com.ericsson.isf.iva.model.JwtUser;
import com.ericsson.isf.iva.util.AppConstants;



@Component
public class JwtValidator {
	
	
    
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
    
	
}