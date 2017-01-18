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
    UsersInGroupsDao dao;

    @Autowired
    GroupDao groupDao;

    @Override
    public UsersInGroup get(int id) {
        return dao.get(id);
    }

    @Override
    public void save(UsersInGroup userInGroup) throws UserAlreadyInGroup {
        dao.save(userInGroup);
    }

    @Override
    public void update(UsersInGroup userInGroup) {
        dao.update(userInGroup);
    }

    @Override
    public void delete(UsersInGroup userInGroup) {
        dao.delete(userInGroup);
    }

    @Override
    public void leave(String groupIdentity, User user) throws NoUserInGroup {
        Group groupToLeave = groupDao.getByIdentity(groupIdentity);
        dao.leave(groupToLeave, user);
    }
}
