package com.whereis.service;

import com.whereis.model.UsersInGroup;

public interface UsersInGroupsService {
    UsersInGroup get(int id);
/*    void save(UsersInGroup user) throws UserAlreadyInGroup;
    void update(UsersInGroup user);*/
    boolean delete(UsersInGroup user);
/*    void leave(String groupIdentity, User user) throws NoUserInGroup;

    UsersInGroup findUserInGroup(Group group, User user);

    void addUserToGroup(Group group, User user) throws UserAlreadyInGroup;*/
}
