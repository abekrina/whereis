package com.whereis.authentication;

import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.Person;
import com.whereis.exceptions.users.NoSuchUserException;
import com.whereis.model.User;
import com.whereis.service.UserService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.io.IOException;

@Service
public class GoogleAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    protected HttpSession httpSession;

    private static final Logger logger = LogManager.getLogger(GoogleAuthenticationProvider.class);

    private static final HttpTransport TRANSPORT = new NetHttpTransport();

    private static final JacksonFactory JSON_FACTORY = new JacksonFactory();

    String CLIENT_ID = System.getenv("WHEREIS_GOOGLE_CLIENT_ID");

    String CLIENT_SECRET = System.getenv("WHEREIS_GOOGLE_CLIENT_SECRET");

    @Autowired
    private UserService userService;

    private GoogleCredential buildCredential(TokenResponse tokenResponse) throws IOException {

        return new GoogleCredential.Builder()
                .setJsonFactory(JSON_FACTORY)
                .setTransport(TRANSPORT)
                .setClientSecrets(CLIENT_ID, CLIENT_SECRET).build()
                .setFromTokenResponse(JSON_FACTORY.fromString(tokenResponse.toString(), GoogleTokenResponse.class));
    }

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        if (!(auth instanceof GoogleAuthentication)) {
            return null;
        }
        GoogleAuthentication tokenAuth = (GoogleAuthentication) auth;

        // Check if user is already connected
        if (tokenAuth.getGoogleToken() != null) {
            tokenAuth.setAuthenticated(true);
            return tokenAuth;
        }

        GoogleTokenResponse tokenResponse = null;
        try {
            tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                    TRANSPORT, JSON_FACTORY, CLIENT_ID, CLIENT_SECRET, tokenAuth.getCode(), "postmessage"
            ).execute();

            tokenAuth.setCredentials(tokenResponse);
            tokenAuth.setAuthenticated(true);
            setUserInfo(tokenAuth);
        } catch (TokenResponseException e) {
            logger.error("User not authorized by Google due to error " + e.getDetails().getError() +
                    " see explanation here https://tools.ietf.org/html/rfc6749#section-8.5 ", e);
            // TODO: google how to handle exceptions as json responce
            throw new UnapprovedClientAuthenticationException("Google refused to authenticate due error "
                    + e.getDetails().getError(), e);
        } catch (IOException e) {
            logger.error("Error during getting token from Google", e);
            throw new AuthenticationServiceException("IO error", e);
        }
        return tokenAuth;
    }

    private void setUserInfo(GoogleAuthentication auth) throws IOException {
        GoogleIdToken idToken = auth.getGoogleTokenResponse().parseIdToken();
        GoogleIdToken.Payload payload = idToken.getPayload();

        User user = userService.getByEmail(payload.getEmail());
        if (user == null) {
            Plus plus = new Plus.Builder(TRANSPORT, JSON_FACTORY, buildCredential(auth.getGoogleTokenResponse()))
                    .setApplicationName("")
                    .build();
            User newUser = userService.createGoogleUser(plus);
            logger.info("User with email " + newUser.getEmail() + " registered successfully");
            //TODO: welcome new user by email :)
            auth.setPrincipal(newUser);
        } else {
            if (user.getFirstName() == null || "".equals(user.getFirstName())) {
                Plus plus = new Plus.Builder(TRANSPORT, JSON_FACTORY, buildCredential(auth.getGoogleTokenResponse()))
                        .setApplicationName("")
                        .build();
                Person person = plus.people().get("me").execute();
                user.setFirstName(person.getName().getGivenName());
                userService.merge(user);
            }
            if (user.getLastName() == null || "".equals(user.getLastName())) {
                Plus plus = new Plus.Builder(TRANSPORT, JSON_FACTORY, buildCredential(auth.getGoogleTokenResponse()))
                        .setApplicationName("")
                        .build();
                Person person = plus.people().get("me").execute();
                user.setLastName(person.getName().getFamilyName());
                userService.merge(user);
            }
            auth.setPrincipal(user);
        }
    }

    @Override
    public boolean supports(Class<?> aClass) {
        if (aClass.equals(GoogleAuthentication.class)) {
            return true;
        } else {
            return false;
        }
    }
}
