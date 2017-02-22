package com.whereis.dao;

import com.whereis.model.UsersInGroup;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository("usersInGroupsDao")
public class DefaultUsersInGroupsDao extends AbstractDao<UsersInGroup> implements UsersInGroupsDao {
    @Override
    public void save(UsersInGroup usersInGroup) {
        getSession().save(usersInGroup);
    }
}
