package com.whereis.service;

import com.whereis.dao.GroupDao;
import com.whereis.dao.UsersInGroupsDao;
import com.whereis.exceptions.NoUserInGroup;
import com.whereis.exceptions.UserAlreadyInGroup;
import com.whereis.model.Group;
import com.whereis.model.User;
import com.whereis.model.UsersInGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultUsersInGroupsService implements UsersInGroupsService {
    @Autowired
    UsersInGroupsDao usersInGroupsDao;

    @Autowired
    GroupDao groupDao;

    @Override
    public UsersInGroup get(int id) {
        return usersInGroupsDao.get(id);
    }

    @Override
    public void save(UsersInGroup userInGroup) throws UserAlreadyInGroup {
        usersInGroupsDao.save(userInGroup);
    }

    @Override
    public void update(UsersInGroup userInGroup) {
        usersInGroupsDao.update(userInGroup);
    }

    @Override
    public void delete(UsersInGroup userInGroup) {
        usersInGroupsDao.delete(userInGroup);
    }

    @Override
    public void leave(String groupIdentity, User user) throws NoUserInGroup {
        Group groupToLeave = groupDao.getByIdentity(groupIdentity);
        usersInGroupsDao.leave(groupToLeave, user);
    }

    @Override
    public UsersInGroup findUserInGroup(Group group, User user) {
        return usersInGroupsDao.findUserInGroup(group, user);
    }
}
