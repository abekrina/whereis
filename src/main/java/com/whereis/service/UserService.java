package com.whereis.service;

import com.google.api.services.plus.Plus;
import com.whereis.exceptions.invites.NoInviteForUserToGroup;
import com.whereis.exceptions.users.NoSuchUser;
import com.whereis.exceptions.groups.NoUserInGroup;
import com.whereis.exceptions.groups.UserAlreadyInGroup;
import com.whereis.exceptions.users.UserWithEmailExists;
import com.whereis.model.Group;
import com.whereis.model.User;

import java.io.IOException;

public interface UserService {
    User get(int id);
    void save(User user) throws UserWithEmailExists;
    void update(User user) throws NoSuchUser;
    boolean delete(User user);
    User getByEmail(String email);
    void deleteByEmail(String email);
    User createGoogleUser(Plus plus) throws IOException;

    void leaveGroup(Group group, User user) throws NoUserInGroup;

    void joinGroup(Group group, User user) throws UserAlreadyInGroup, NoInviteForUserToGroup;

    void assertUserInGroup(Group group, User user);
}
