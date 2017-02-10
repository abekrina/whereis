package com.whereis.dao;

import com.whereis.exceptions.groups.GroupWithIdentityExists;
import com.whereis.exceptions.groups.NoSuchGroup;
import com.whereis.model.Group;

public interface GroupDao {
    /**
     *  Methods are implemented in AbstractDao
     *  @see AbstractDao
     */
    Group get(int id);
    boolean delete(Class<? extends Group> type, int id);

    /**
     *  Methods specific for every implementation of this interface
     */
    void save(Group group) throws GroupWithIdentityExists;
    void update(Group group) throws NoSuchGroup;
    Group getByIdentity(String identity);
}
