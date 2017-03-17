package com.whereis.service;

import com.whereis.exceptions.groups.GroupWithIdentityExistsException;
import com.whereis.exceptions.groups.NoSuchGroupException;
import com.whereis.model.Group;

public interface GroupService {
    Group get(int id);
    void save(Group group);

    void update(Group group) throws NoSuchGroupException;
    boolean delete(Group group);
    Group getByIdentity(String identity);
}
