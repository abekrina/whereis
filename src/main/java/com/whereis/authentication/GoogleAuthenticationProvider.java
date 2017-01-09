package com.whereis.authentication;

import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.plus.Plus;
import com.whereis.exceptions.GoogleApiException;
import com.whereis.model.User;
import com.whereis.service.UserService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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

    @Value("${google.client.id}")
    String CLIENT_ID;

    @Value("${google.client.secret}")
    String CLIENT_SECRET;

    @Autowired
    private UserService userService;

    private GoogleCredential buildCredential() throws IOException {
        String tokenData = (String) httpSession.getAttribute("token");

        return new GoogleCredential.Builder()
                .setJsonFactory(JSON_FACTORY)
                .setTransport(TRANSPORT)
                .setClientSecrets(CLIENT_ID, CLIENT_SECRET).build()
                //TODO: fix NPE
                .setFromTokenResponse(JSON_FACTORY.fromString(tokenData, GoogleTokenResponse.class));
    }

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        //TODO: throw exception if not authorized
        TokenAgainstCodeAuthentication tokenAuth = (TokenAgainstCodeAuthentication) auth;
        // Check if user is already connected
        //TODO:  учесть то что токен экспайрится
        if (tokenAuth.getCredentials() != null) {
            tokenAuth.setAuthenticated(true);
            return tokenAuth;
        }

        // Check state param
        if (!tokenAuth.getName().equals(httpSession.getAttribute("state"))) {
            tokenAuth.setAuthenticated(false);
            return tokenAuth;
        }

        getToken(tokenAuth);

        return tokenAuth;
    }

    private boolean getToken(TokenAgainstCodeAuthentication tokenAuth) {
        try {
            GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                    TRANSPORT, JSON_FACTORY, CLIENT_ID, CLIENT_SECRET, tokenAuth.getCode(), "postmessage"
            ).execute();

            tokenAuth.setCredentials(tokenResponse.toString());
            tokenAuth.setAuthenticated(true);
            Plus plus = new Plus.Builder(TRANSPORT, JSON_FACTORY, buildCredential())
                    .setApplicationName("")
                    .build();

            GoogleIdToken idToken = tokenResponse.parseIdToken();
            GoogleIdToken.Payload payload = idToken.getPayload();

            User user = userService.getByEmail(payload.getEmail());
            if (user == null) {
                //TODO: прикрутить log4j
                //TODO: передавать эксепшн в лог
                userService.createGoogleUser(plus);
                logger.info("User with email " + user.getEmail() + " registered successfully");
            }
        } catch (GoogleApiException | IOException e) {
            tokenAuth.setAuthenticated(false);
            //TODO: choose exception and implement error handling
            // after setting log4j
            //throw new AuthenticationException(e);
        } catch (TokenResponseException e) {

        }
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
