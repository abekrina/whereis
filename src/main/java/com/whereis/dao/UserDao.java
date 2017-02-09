package com.whereis.dao;

import com.whereis.exceptions.users.NoSuchUser;
import com.whereis.exceptions.groups.NoUserInGroup;
import com.whereis.exceptions.groups.UserAlreadyInGroup;
import com.whereis.exceptions.users.UserWithEmailExists;
import com.whereis.model.Group;
import com.whereis.model.Location;
import com.whereis.model.User;
import org.hibernate.Session;

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
    void save(User user) throws UserWithEmailExists;
    void update(User user) throws NoSuchUser;
    User getByEmail(String email);
    void deleteByEmail(String email);

    void leaveGroup(Group group, User user) throws NoUserInGroup;

    void joinGroup(Group group, User user) throws UserAlreadyInGroup;

    boolean assertUserInGroup(Group group, User user);

    Set<Group> getGroupsForUser(User user);

    void saveUserLocation(Location location, User user);
}
