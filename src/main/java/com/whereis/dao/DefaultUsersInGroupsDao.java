package com.whereis.dao;

import com.whereis.authentication.GoogleAuthenticationFilter;
import com.whereis.model.UsersInGroup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository("usersInGroupsDao")
public class DefaultUsersInGroupsDao extends AbstractDao<UsersInGroup> implements UsersInGroupsDao {

    private static final Logger logger = LogManager.getLogger(GoogleAuthenticationFilter.class);
    // TODO: is this class needed?
}
