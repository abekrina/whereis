package com.whereis.authentication;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.whereis.model.User;
import com.whereis.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
public class GoogleAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    protected HttpSession httpSession;

    private static final Logger logger = LogManager.getLogger(GoogleAuthenticationProvider.class);

    private static final HttpTransport TRANSPORT = new NetHttpTransport();

    private static final JacksonFactory JSON_FACTORY = new JacksonFactory();

    private String CLIENT_ID = System.getenv("WHEREIS_GOOGLE_CLIENT_ID");

    @Autowired
    private UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        GoogleAuthentication googleAuthentication = (GoogleAuthentication) authentication;

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(TRANSPORT, JSON_FACTORY)
                .setAudience(Collections.singletonList(CLIENT_ID))
                .setIssuer("accounts.google.com")
                .build();


        GoogleIdToken idToken = null;

        try {
            logger.error("TOKEN: " + googleAuthentication.getIdTokenToVerify());
            idToken = verifier.verify(googleAuthentication.getIdTokenToVerify());
        } catch (GeneralSecurityException e) {
            logger.error("User not authorized by Google due to error " + e);
            throw new GoogleAuthenticationException(e.getMessage());
        } catch (IOException e) {
            logger.error("Error when verifying ID token: ", e);
        }

        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();

            googleAuthentication.setCredentials(idToken);

            User user = userService.getByEmail(payload.getEmail());

            if (user == null) {
                try {
                    user = userService.createGoogleUser(payload);
                } catch (IOException e) {
                    return null;
                }
            } else {
                user.setFirstName(payload.get("given_name") == null ? (String)payload.get("name") :
                        (String)payload.get("given_name"));
                user.setLastName((String)payload.get("family_name"));
                userService.merge(user);
            }

            googleAuthentication.setPrincipal(user);
            googleAuthentication.setAuthenticated(true);

        } else {
            System.out.println("Invalid ID token.");

            try {
                logger.error("TOKEN: " + googleAuthentication.getIdTokenToVerify());
                idToken = verifier.verify(googleAuthentication.getIdTokenToVerify());
            } catch (GeneralSecurityException e) {
                logger.error("User not authorized by Google due to error " + e);
                throw new GoogleAuthenticationException(e.getMessage());
            } catch (IOException e) {
                logger.error("Error when verifying ID token: ", e);
            }

            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();

                googleAuthentication.setCredentials(idToken);

                User user = userService.getByEmail(payload.getEmail());

                if (user == null) {
                    try {
                        user = userService.createGoogleUser(payload);
                    } catch (IOException e) {
                        return null;
                    }
                } else {
                    user.setFirstName(payload.get("given_name") == null ? (String)payload.get("name") :
                            (String)payload.get("given_name"));
                    user.setLastName((String)payload.get("family_name"));
                    userService.merge(user);
                }

                googleAuthentication.setPrincipal(user);
                googleAuthentication.setAuthenticated(true);

            } else {
                System.out.println("Invalid ID token. AGAIN `>_<");
            }
        }

        return googleAuthentication;
    }

    @Override
    public boolean supports(Class<?> authenticationClass) {
        if (authenticationClass.equals(GoogleAuthentication.class)) {
            return true;
        } else {
            return false;
        }
    }
}
