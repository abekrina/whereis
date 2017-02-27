package com.whereis.dao;


import com.whereis.model.UsersInGroup;

public interface UsersInGroupsDao {
    /**
     *  Methods are implemented in AbstractDao
     */
    boolean delete(Class <? extends UsersInGroup> type, int id);
    UsersInGroup get(int id);

    /**
     *  Methods specific for every implementation of this interface
     */
    void save(UsersInGroup usersInGroup);
}
