package com.whereis.authentication;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.whereis.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;

public class GoogleAuthentication implements Authentication {
    private String tokenToVerify = "";
    private GoogleIdToken googleToken;
    private boolean isAuthenticated = false;
    private User principal;

    public GoogleAuthentication(String tokenToVerify) {
        this.tokenToVerify = tokenToVerify;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<GrantedAuthority>(0);
    }

    @Override
    public Object getCredentials() {
        return googleToken;
    }

    void setCredentials(GoogleIdToken token){
        this.googleToken = token;
    }

    @Override
    public Object getDetails() {
        return googleToken.getPayload().getExpirationTimeSeconds();
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
        return principal.getFirstName() + " " + principal.getLastName();
    }

    public String getIdTokenToVerify() {
        return tokenToVerify;
    }
}
