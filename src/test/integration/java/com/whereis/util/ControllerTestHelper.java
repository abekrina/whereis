package com.whereis.util;

import com.whereis.authentication.GoogleAuthentication;
import com.whereis.model.User;

public class ControllerTestHelper {

    public static GoogleAuthentication getTestAuthentication(User testUser) {
        GoogleAuthentication authentication = new GoogleAuthentication("123", "123");
        authentication.setPrincipal(testUser);
        authentication.setAuthenticated(true);
        return authentication;
    }
}
