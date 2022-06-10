package com.ericsson.isf.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class JwtUserDetails implements UserDetails {

    private String userName;
    private String unique_name;
    private String token;
    private String id;
    private Collection<? extends GrantedAuthority> authorities;


    public JwtUserDetails(String unique_name) {

        this.userName = userName;
        this.unique_name = unique_name;
        this.id = id;
        this.token= token;
 
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    public String getUserName() {
        return userName;
    }

    public String getToken() {
        return token;
    }


    public String getId() {
        return id;
    }

	public String getUnique_name() {
		return unique_name;
	}

	public void setUnique_name(String unique_name) {
		this.unique_name = unique_name;
	}

}