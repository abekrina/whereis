package com.whereis.service;

import com.whereis.exceptions.groups.GroupWithIdentityExists;
import com.whereis.exceptions.groups.NoSuchGroup;
import com.whereis.model.Group;
import com.whereis.model.User;

public interface GroupService {
    Group get(int id);
    void save(Group group) throws GroupWithIdentityExists;

    void update(Group group) throws NoSuchGroup;
    boolean delete(Group group);
    Group getByIdentity(String identity);
}
