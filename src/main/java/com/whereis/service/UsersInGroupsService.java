package com.whereis.service;

import com.whereis.exceptions.NoUserInGroup;
import com.whereis.model.User;
import com.whereis.model.UsersInGroup;

public interface UsersInGroupsService {
    UsersInGroup get(int id);
    void save(UsersInGroup user);
    void update(UsersInGroup user);
    void delete(UsersInGroup user);
    void leave(String groupIdentity, User user) throws NoUserInGroup;
}
