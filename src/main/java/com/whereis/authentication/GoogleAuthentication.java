package com.whereis.authentication;

import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.whereis.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;

public class GoogleAuthentication implements Authentication {
    private String code;
    private String unique_visitor_code;
    private GoogleTokenResponse googleToken;
    private boolean isAuthenticated = false;
    private User principal;

    public GoogleAuthentication(String code, String unique_visitor_code) {
        this.code = code;
        this.unique_visitor_code = unique_visitor_code;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<GrantedAuthority>(0);
    }
    @Override
    public Object getCredentials() {
        return googleToken.getAccessToken();
    }
    GoogleTokenResponse getGoogleTokenResponse() {
        return googleToken;
    }
    public void setCredentials(GoogleTokenResponse token){
        this.googleToken = token;
    }
    @Override
    public Object getDetails() {
        return googleToken.getExpiresInSeconds();
    }
    @Override
    public Object getPrincipal() {
        return principal;
    }
    public void setPrincipal(User user) {
        this.principal = user;
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
        return principal.getFirstName() + principal.getLastName() + principal.getId();
    }

    public String getCode() {
        return code;
    }
}
