package com.whereis.service;

import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.Person;
import com.whereis.authentication.GoogleAuthenticationFilter;
import com.whereis.dao.UserDao;
import com.whereis.exceptions.invites.NoInviteForUserToGroup;
import com.whereis.exceptions.users.NoSuchUser;
import com.whereis.exceptions.groups.NoUserInGroup;
import com.whereis.exceptions.groups.UserAlreadyInGroup;
import com.whereis.exceptions.users.UserWithEmailExists;
import com.whereis.model.Group;
import com.whereis.model.Invite;
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

    @Autowired
    public InviteService inviteService;

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

    @Override
    public void leaveGroup(Group group, User user) throws NoUserInGroup {
        dao.leaveGroup(group, user);
    }

    @Override
    public void joinGroup(Group group, User user) throws UserAlreadyInGroup, NoInviteForUserToGroup {
        //TODO: make boolean method for checking invites
        Invite inviteForUser = inviteService.getPendingInviteFor(user, group);
        if (inviteForUser == null) {
            throw new NoInviteForUserToGroup("There is no invite for user " + user + " to group " + group);
        }
        dao.joinGroup(group, user);
        inviteService.delete(inviteForUser);
    }

    @Override
    public void assertUserInGroup(Group group, User user) {
        dao.assertUserInGroup(group, user);
    }
}
