package com.whereis.dao;

import com.whereis.model.UsersInGroup;

public interface UsersInGroupsDao {
    /**
     *  Methods are implemented in AbstractDao
     */
    UsersInGroup get(int id);
    boolean delete(Class <? extends UsersInGroup> type, int id);

    /**
     *  Methods specific for every implementation of this interface
     */
/*    void save(UsersInGroup user) throws UserAlreadyInGroupException;
    void update(UsersInGroup user);
    void leave(Group group, User user) throws NoUserInGroupException;

    UsersInGroup findUserInGroup(UsersInGroup usersInGroup);

    UsersInGroup findUserInGroup(Group group, User user);*/
}
