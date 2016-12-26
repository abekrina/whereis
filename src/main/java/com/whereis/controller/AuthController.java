package com.whereis.controller;

import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.whereis.model.User;
import com.whereis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.Person;

import java.io.IOException;

@RestController
public class AuthController extends AbstractController {
    private static final HttpTransport TRANSPORT = new NetHttpTransport();

    private static final JacksonFactory JSON_FACTORY = new JacksonFactory();

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

    @RequestMapping(value = "/connect", method = RequestMethod.POST)
    public ResponseEntity connect(
        @RequestParam("state") String stateParam,
        @RequestBody String code
    ) throws IOException {
        String tokenData = (String) httpSession.getAttribute("token");

        // Check if user is already connected
        if (tokenData != null) {
            return new ResponseEntity<>("User already connected", HttpStatus.OK);
        }

        // Check state param
        if (!stateParam.equals(httpSession.getAttribute("state"))) {
            return new ResponseEntity<>("Invalid state param", HttpStatus.UNAUTHORIZED);
        }

        // Try to upgrade token
        try {
            GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                TRANSPORT, JSON_FACTORY, CLIENT_ID, CLIENT_SECRET, code, "postmessage"
            ).execute();

            GoogleIdToken idToken = tokenResponse.parseIdToken();
            GoogleIdToken.Payload payload = idToken.getPayload();

            httpSession.setAttribute("token", tokenResponse.toString());

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

            return new ResponseEntity<>(
                "Successfully connected user",
                HttpStatus.OK
            );
        } catch (TokenResponseException e) {
            return new ResponseEntity<>(
                "Failed to upgrade the authorization code",
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        } catch (IOException e) {
            return new ResponseEntity<>(
                "Failed to read token data from Google",
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @RequestMapping(value = "/disconnect", method = RequestMethod.GET)
    public ResponseEntity disconnect() {
        String tokenData = (String) httpSession.getAttribute("token");

        if (tokenData == null) {
            return new ResponseEntity<>("User not connected", HttpStatus.UNAUTHORIZED);
        }

        try {
            GoogleCredential credential = buildCredential();

            HttpResponse revokeResponse = TRANSPORT.createRequestFactory()
                .buildGetRequest(new GenericUrl(String.format(
                    "https://accounts.google.com/o/oauth2/revoke?token=%s",
                    credential.getAccessToken()
                ))).execute();

            httpSession.removeAttribute("token");

            return new ResponseEntity<>(
                "Successfully disconnected",
                HttpStatus.OK
            );
        } catch (IOException e) {
            return new ResponseEntity<>(
                "Failed to revoke token for given user",
                HttpStatus.BAD_REQUEST
            );
        }
    }
}
