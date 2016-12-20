package com.whereis.dao;

import com.whereis.model.UserToGroupRelation;

public interface UserToGroupRelationDao {
    /**
     *  Methods are implemented in AbstractDao
     */
    UserToGroupRelation get(int id);
    void delete(UserToGroupRelation user);

    /**
     *  Methods specific for every implementation of this interface
     */
    void save(UserToGroupRelation user);
    void update(UserToGroupRelation user);
}
