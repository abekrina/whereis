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
import com.google.api.services.plus.model.Person;
import com.whereis.model.User;
import com.whereis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Logger;

public class AuthenticationTokenProvider implements AuthenticationProvider {
    @Autowired
    protected HttpSession httpSession;

    private static final Logger logger = Logger.getLogger(AuthenticationTokenProvider.class.getName());

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
                .setFromTokenResponse(JSON_FACTORY.fromString(tokenData, GoogleTokenResponse.class));
    }

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        TokenAuthentication tokenAuth = (TokenAuthentication) auth;
        /*if (auth.isAuthenticated())
            return auth;*/
        // Check if user is already connected
        //  учесть то что токен экспайрится
        if (tokenAuth.getCredentials() != null) {
            tokenAuth.setAuthenticated(true);
            return tokenAuth;
        }

        // Check state param
        if (!tokenAuth.getName().equals(httpSession.getAttribute("state"))) {
            tokenAuth.setAuthenticated(false);
            return tokenAuth;
        }

        // Try to upgrade token
        try {
            GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                    TRANSPORT, JSON_FACTORY, CLIENT_ID, CLIENT_SECRET, tokenAuth.getCode(), "postmessage"
            ).execute();

            GoogleIdToken idToken = tokenResponse.parseIdToken();
            GoogleIdToken.Payload payload = idToken.getPayload();

            //httpSession.setAttribute("token", tokenResponse.toString());
            tokenAuth.setCredentials(tokenResponse.toString());
            tokenAuth.setAuthenticated(true);
            User existingUser = userService.getByEmail(payload.getEmail());

            if (existingUser == null) {
                Plus plus = new Plus.Builder(TRANSPORT, JSON_FACTORY, buildCredential())
                        .setApplicationName("")
                        .build();
                Person profile = plus.people().get("me").execute();

                User user = new User();
                user.setName(profile.getDisplayName());
                user.setEmail(payload.getEmail());
                userService.save(user);
            }
        } catch (TokenResponseException e) {
            tokenAuth.setAuthenticated(false);
            // сообщить об ошибке
        } catch (IOException e) {
            tokenAuth.setAuthenticated(false);
            //сообщить об ошибке "Failed to read token data from Google"
        }
        return tokenAuth;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
