package com.ericsson.isf.mqttclient.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class JwtUser {
	
    private Date expiration;
    private Date issuedAt;
    private String token;
    private String userName;
    private String password;
    private String type;
    private String decodedUserName;
    private String decodedPassword;
    private String externalSourceName;
    private String ownerSignum;

}