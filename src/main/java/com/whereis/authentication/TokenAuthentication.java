package com.whereis.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;

public class TokenAuthentication implements Authentication {
    private String code;
    private String state;
    private String token;
    private boolean isAuthenticated = false;

    public TokenAuthentication(String code, String state) {
        this.code = code;
        this.state = state;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<GrantedAuthority>(0);
    }
    @Override
    public Object getCredentials() {
        return token;
    }
    public void setCredentials(String token){
        this.token = token;
    }
    @Override
    public Object getDetails() {
        return null;
    }
    @Override
    public Object getPrincipal() {
        return null;
    }
    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }
    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.isAuthenticated = isAuthenticated;
    }
    @Override
    public String getName() {
        return state;
    }

    public String getCode() {
        return code;
    }
}
