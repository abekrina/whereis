package com.whereis.service;

import com.whereis.model.Group;

public interface GroupService {
    Group get(int id);
    void save(Group group);
    void update(Group group);
    void delete(Group group);
}
