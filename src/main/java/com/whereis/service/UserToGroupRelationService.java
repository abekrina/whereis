package com.whereis.service;

import com.whereis.model.UserToGroupRelation;

public interface UserToGroupRelationService {
    UserToGroupRelation get(int id);
    void save(UserToGroupRelation user);
    void update(UserToGroupRelation user);
    void delete(UserToGroupRelation user);
}
