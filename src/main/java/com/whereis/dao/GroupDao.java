package com.whereis.dao;

import com.whereis.model.Group;

public interface GroupDao {
    /**
     *  Methods are implemented in AbstractDao
     *  @see AbstractDao
     */
    Group get(int id);
    void delete(Group group);

    /**
     *  Methods specific for every implementation of this interface
     */
    void save(Group group);
    void update(Group group);
}
