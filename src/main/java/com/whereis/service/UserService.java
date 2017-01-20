package com.whereis.service;

import com.google.api.services.plus.Plus;
import com.whereis.exceptions.GoogleApiException;
import com.whereis.exceptions.NoSuchUser;
import com.whereis.exceptions.UserNotFoundException;
import com.whereis.exceptions.UserWithEmailExists;
import com.whereis.model.User;

import java.io.IOException;

public interface UserService {
    User get(int id);
    void save(User user) throws UserWithEmailExists;
    void update(User user) throws NoSuchUser;
    void delete(User user);
    User getByEmail(String email);
    void deleteByEmail(String email);
    User createGoogleUser(Plus plus) throws IOException;
}
