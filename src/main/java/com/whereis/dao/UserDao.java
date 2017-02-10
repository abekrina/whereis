package com.whereis.dao;

import com.whereis.exceptions.groups.UserAlreadyInGroupException;
import com.whereis.exceptions.users.NoSuchUserException;
import com.whereis.exceptions.groups.NoUserInGroupException;
import com.whereis.exceptions.users.UserWithEmailExistsException;
import com.whereis.model.Group;
import com.whereis.model.Location;
import com.whereis.model.User;

import java.util.Set;


public interface UserDao {
    /**
     *  Methods are implemented in AbstractDao
     */
    User get(int id);
    boolean delete(Class <? extends User> type, int id);
    /**
     *  Methods specific for every implementation of this interface
     */
    void save(User user) throws UserWithEmailExistsException;
    void update(User user) throws NoSuchUserException;
    User getByEmail(String email);
    void deleteByEmail(String email);

    void leaveGroup(Group group, User user) throws NoUserInGroupException;

    void joinGroup(Group group, User user) throws UserAlreadyInGroupException;

    boolean assertUserInGroup(Group group, User user);

    Set<Group> getGroupsForUser(User user);

    void saveUserLocation(Location location, User user);
}
