package com.whereis.service;

import com.google.api.services.plus.Plus;
import com.whereis.exceptions.invites.NoInviteForUserToGroupException;
import com.whereis.exceptions.users.NoSuchUserException;
import com.whereis.exceptions.groups.NoUserInGroupException;
import com.whereis.exceptions.groups.UserAlreadyInGroupException;
import com.whereis.exceptions.users.UserWithEmailExistsException;
import com.whereis.model.Group;
import com.whereis.model.User;

import java.io.IOException;

public interface UserService {
    User get(int id);
    void save(User user) throws UserWithEmailExistsException;
    void update(User user) throws NoSuchUserException;
    boolean delete(User user);
    User getByEmail(String email);
    void deleteByEmail(String email);
    User createGoogleUser(Plus plus) throws IOException;

    void leaveGroup(Group group, User user) throws NoUserInGroupException;

    void joinGroup(Group group, User user) throws UserAlreadyInGroupException, NoInviteForUserToGroupException;

    void assertUserInGroup(Group group, User user);
}
