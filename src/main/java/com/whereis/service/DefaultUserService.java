package com.whereis.service;

import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.Person;
import com.whereis.authentication.GoogleAuthenticationFilter;
import com.whereis.dao.UserDao;
import com.whereis.exceptions.groups.NoUserInGroupException;
import com.whereis.exceptions.invites.NoInviteForUserToGroupException;
import com.whereis.exceptions.users.NoSuchUserException;
import com.whereis.exceptions.groups.UserAlreadyInGroupException;
import com.whereis.exceptions.users.UserWithEmailExistsException;
import com.whereis.model.Group;
import com.whereis.model.Invite;
import com.whereis.model.Location;
import com.whereis.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Set;

@Service("userService")
@Transactional
public class DefaultUserService implements UserService {

    private static final Logger logger = LogManager.getLogger(GoogleAuthenticationFilter.class);

    @Autowired
    public UserDao dao;

    @Autowired
    LocationService locationService;

    @Autowired
    public InviteService inviteService;

    @Override
    public User get(int id) {
        return dao.get(id);
    }

    @Override
    public void save(User user) throws UserWithEmailExistsException {
        dao.save(user);
    }

    @Override
    public void update(User user) throws NoSuchUserException {
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
        } catch (UserWithEmailExistsException userWithEmailExistsException) {
            logger.error("Attempting to register user this email which already exists ", userWithEmailExistsException);
            throw new IOException("Attempting to register user this email which already exists ", userWithEmailExistsException);
        }
        return newUser;
    }

    @Override
    public boolean checkUserInGroup(Group group, User user) {
        return user.getGroups().contains(group);
    }

    @Override
    public Set<Group> getGroupsForUser(User user) {
        return user.getGroups();
    }

    @Override
    public void saveUserLocation(Location location) {
        // TODO: check if all this is needed
        locationService.save(location);
        location.getUser().saveUserLocation(location);
        dao.merge(location.getUser());
        try {
            dao.update(location.getUser());
        } catch (NoSuchUserException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void leaveGroup(Group group, User user) throws NoUserInGroupException {
        if (!user.leave(group)){
            throw new NoUserInGroupException("There is no user " + this + " in group " + group);
        }

    }

    @Override
    public void joinGroup(Group group, User user) throws UserAlreadyInGroupException, NoInviteForUserToGroupException {
        Invite inviteForUser = inviteService.getPendingInviteFor(user, group);
        if (inviteForUser == null) {
            throw new NoInviteForUserToGroupException("There is no invite for user " + user + " to group " + group);
        }
        if (!user.joinGroup(group)){
            inviteService.delete(inviteForUser);
            throw new UserAlreadyInGroupException("User " + this + "already joined group " + group);
        } else {
            group.addUserToGroup(user);
        }
    }
}
