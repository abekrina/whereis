package com.whereis.service;

import com.whereis.model.UsersInGroup;

public interface UsersInGroupsService {
    UsersInGroup get(int id);
/*    void save(UsersInGroup user) throws UserAlreadyInGroupException;
    void update(UsersInGroup user);*/
    boolean delete(UsersInGroup user);
/*    void leave(String groupIdentity, User user) throws NoUserInGroupException;

    UsersInGroup findUserInGroup(Group group, User user);

    void joinGroup(Group group, User user) throws UserAlreadyInGroupException;*/
}
