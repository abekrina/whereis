package com.whereis.service;

import com.whereis.model.UsersInGroup;

public interface UsersInGroupsService {
    UsersInGroup get(int id);
    void save(UsersInGroup user);
    void update(UsersInGroup user);
    void delete(UsersInGroup user);
}
