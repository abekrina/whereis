package com.whereis.service;

import com.google.api.client.auth.openidconnect.IdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.Person;
import com.whereis.dao.GroupDao;
import com.whereis.dao.UserDao;
import com.whereis.dao.UsersInGroupsDao;
import com.whereis.exceptions.groups.NoUserInGroupException;
import com.whereis.exceptions.invites.NoInviteForUserToGroupException;
import com.whereis.exceptions.users.NoSuchUserException;
import com.whereis.exceptions.groups.UserAlreadyInGroupException;
import com.whereis.exceptions.users.UserWithEmailExistsException;
import com.whereis.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Set;

@Service("userService")
@Transactional
public class DefaultUserService implements UserService {

    private static final Logger logger = LogManager.getLogger(DefaultUserService.class);

    @Autowired
    public UserDao userDao;

    @Autowired
    public GroupDao groupDao;

    @Autowired
    public UsersInGroupsDao usersInGroupsDao;

    @Autowired
    LocationService locationService;

    @Autowired
    public InviteService inviteService;

    @Override
    public User get(int id) {
        return userDao.get(id);
    }

    @Override
    public void save(User user) throws UserWithEmailExistsException {
        userDao.save(user);
    }

    @Override
    public void update(User user) throws NoSuchUserException {
        userDao.update(user);
    }

    @Override
    public void merge(User user) {
        userDao.merge(user);
    }

    @Override
    public boolean delete(User user) {
        return userDao.delete(user.getClass(), user.getId());
    }

    @Override
    public User getByEmail(String email) {
        return userDao.getByEmail(email);
    }

    @Override
    public void deleteByEmail(String email) {
        userDao.deleteByEmail(email);
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

    /**
     * Creates and saves new User with data from payload
     * @param payload
     * @return a new User
     * @throws IOException
     */
    @Override
    public User createGoogleUser(GoogleIdToken.Payload payload) throws IOException {
        User user = new User();

        String firstName = (String)payload.get("given_name");
        if (firstName == null) {
            user.setFirstName((String)payload.get("name"));
        } else {
            user.setFirstName(firstName);
        }
        user.setLastName((String)payload.get("family_name"));
        user.setEmail(payload.getEmail());

        try {
            save(user);
            logger.info("User firstname: " + user.getFirstName() + " lastname: " + user.getLastName() + " email: "
            + user.getEmail());
        } catch (UserWithEmailExistsException userWithEmailExistsException) {
            logger.error("Attempting to register user this email which already exists ", userWithEmailExistsException);
            throw new IOException("Attempting to register user this email which already exists ", userWithEmailExistsException);
        }
        return user;
    }

    @Override
    @Transactional
    public boolean checkUserInGroup(Group group, User user) {
        user = userDao.get(user.getId());
        return user.getGroups().contains(group);
    }

    @Override
    @Transactional
    public Set<Group> getGroupsForUser(User user) {
        user = userDao.get(user.getId());
        return user.getGroups();
    }

    @Override
    @Transactional
    public void saveUserLocation(Location location) {
        location.setUser(userDao.get(location.getUser().getId()));
        locationService.save(location);
        location.getUser().saveUserLocation(location);
        location.getGroup().addLocationOfUser(location);
    }

    @Override
    @Transactional
    public void leaveGroup(Group group, User user) throws NoUserInGroupException {
        group = groupDao.get(group.getId());
        UsersInGroup relationToDelete = group.getUserToGroupRelation(user);
        user = userDao.get(user.getId());
        user.leave(relationToDelete);
        group.deleteUserFromGroup(relationToDelete);
        usersInGroupsDao.delete(relationToDelete.getClass(), relationToDelete.getId());
    }

    @Override
    @Transactional
    public void joinGroup(Group group, User user) throws UserAlreadyInGroupException, NoInviteForUserToGroupException {
        Invite inviteForUser = inviteService.getPendingInviteFor(user, group);
        if (inviteForUser == null) {
            throw new NoInviteForUserToGroupException("There is no invite for user " + user + " to group " + group);
        }
        if (!checkUserInGroup(group, user)) {
            UsersInGroup usersInGroup = new UsersInGroup(user, group);
            usersInGroupsDao.save(usersInGroup);
            group = groupDao.get(group.getId());
            group.addUserToGroup(usersInGroup);
            user = userDao.get(user.getId());
            user.joinGroup(usersInGroup);
            user.deleteInviteForUser(inviteForUser);
            group.deleteInviteToGroup(inviteForUser);
            inviteService.delete(inviteForUser);
        } else {
            user.deleteInviteForUser(inviteForUser);
            group.deleteInviteToGroup(inviteForUser);
            inviteService.delete(inviteForUser);
            throw new UserAlreadyInGroupException("User " + this + "already joined group " + group);
        }
    }
}
