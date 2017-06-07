package com.whereis.authentication;


import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

public class TokenVerifyProvider implements AuthenticationProvider {

    private static final Logger logger = LogManager.getLogger(GoogleAuthenticationProvider.class);

    private static final HttpTransport TRANSPORT = new NetHttpTransport();

    private static final JacksonFactory JSON_FACTORY = new JacksonFactory();

    String CLIENT_ID = System.getenv("WHEREIS_GOOGLE_CLIENT_ID");

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        GoogleAuthentication googleAuthentication = (GoogleAuthentication) authentication;
        GoogleIdToken idToken = (GoogleIdToken) googleAuthentication.getCredentials();

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(TRANSPORT, JSON_FACTORY)
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();

        try {
            if (idToken.verify(verifier)) {
                googleAuthentication.setAuthenticated(true);
            } else {
                googleAuthentication.setAuthenticated(false);
            }
        } catch (GeneralSecurityException e) {
            logger.error("User not authorized by Google due to error " + e);
            throw new GoogleAuthenticationException(e.getMessage());
        } catch (IOException e) {
            logger.error("Error when verifying ID token: ", e);
        }

        return googleAuthentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return false;
    }
}
