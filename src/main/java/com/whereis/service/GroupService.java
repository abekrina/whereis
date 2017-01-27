package com.whereis.service;

import com.whereis.exceptions.GroupWithIdentityExists;
import com.whereis.exceptions.NoSuchGroup;
import com.whereis.model.Group;
import com.whereis.model.User;

public interface GroupService {
    Group get(int id);
    void save(Group group, User currentUser) throws GroupWithIdentityExists;
    void update(Group group) throws NoSuchGroup;
    boolean delete(Group group);
    Group getByIdentity(String identity);
}
