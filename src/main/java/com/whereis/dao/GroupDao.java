package com.whereis.dao;

import com.whereis.exceptions.groups.GroupWithIdentityExistsException;
import com.whereis.exceptions.groups.NoSuchGroupException;
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
    void save(Group group) throws GroupWithIdentityExistsException;
    void update(Group group) throws NoSuchGroupException;

    void refresh(Group group);

    void merge(Group group);

    Group getByIdentity(String identity);
}
