package com.whereis.service;

import com.whereis.exceptions.GroupWithIdentityExists;
import com.whereis.exceptions.NoSuchGroup;
import com.whereis.model.Group;

public interface GroupService {
    Group get(int id);
    void save(Group group) throws GroupWithIdentityExists;
    void update(Group group) throws NoSuchGroup;
    void delete(Group group);
    Group getByIdentity(String identity);
}
