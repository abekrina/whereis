package com.whereis.service;

import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.Person;
import com.whereis.authentication.GoogleAuthenticationFilter;
import com.whereis.dao.UserDao;
import com.whereis.exceptions.*;
import com.whereis.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service("userService")
@Transactional
public class DefaultUserService implements UserService {

    private static final Logger logger = LogManager.getLogger(GoogleAuthenticationFilter.class);

    @Autowired
    public UserDao dao;

    @Override
    public User get(int id) {
        return dao.get(id);
    }

    @Override
    public void save(User user) throws UserWithEmailExists {
        dao.save(user);
    }

    @Override
    public void update(User user) throws NoSuchUser {
        dao.update(user);
    }

    @Override
    public boolean delete(User user) {
        return dao.delete(user.getClass(), user.getId());
    }

    @Override
    public User getByEmail(String email) {
        return dao.getByEmail(email);
    }

    @Override
    public void deleteByEmail(String email) {
        dao.deleteByEmail(email);
    }

    @Override
    public User createGoogleUser(Plus plus) throws IOException {
        Person profile = plus.people().get("me").execute();
        String userEmail = profile.getEmails().get(0).getValue();
        User newUser = new User();
        Person.Name userName = profile.getName();
        if (userName == null) {
            newUser.setFirstName(profile.getDisplayName());
        } else {
            newUser.setFirstName(userName.getGivenName());
            newUser.setLastName(userName.getFamilyName());
        }

        newUser.setEmail(userEmail);
        try {
            save(newUser);
        } catch (UserWithEmailExists userWithEmailExists) {
            logger.error("Attempting to register user this email which already exists ", userWithEmailExists);
            throw new IOException("Attempting to register user this email which already exists ", userWithEmailExists);
        }
        return newUser;
    }
}
