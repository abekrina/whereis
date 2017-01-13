package com.whereis.dao;

import com.whereis.configuration.LogConfigurationFactory;
import com.whereis.exceptions.NoUserInGroup;
import com.whereis.model.Group;
import com.whereis.model.User;
import com.whereis.model.UsersInGroup;
import com.whereis.testconfig.TestHibernateConfiguration;
import com.whereis.testconfig.TestWebMvcConfiguration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

@ContextConfiguration(classes = {TestHibernateConfiguration.class, TestWebMvcConfiguration.class})
@WebAppConfiguration
public class DefaultUsersInGroupDaoIT extends AbstractTransactionalTestNGSpringContextTests {
    @Autowired
    DataSource dataSource;

    @Autowired
    UsersInGroupsDao usersInGroupsDao;

    User defaultUser;
    Group defaultGroup;
    UsersInGroup defaultUsersInGroup;

    @BeforeTest
    public void initialize() {
        // Set configuration for logger
        ConfigurationFactory.setConfigurationFactory(new LogConfigurationFactory());

        // Create test objects
        setupDefaultUser();
        setupDefaultGroup();
        setupUsersInGroup();
    }

    private void setupDefaultUser() {
        defaultUser = new User();
        defaultUser.setId(1);
        defaultUser.setEmail("sweetpotatodevelopment@gmail.com");
        defaultUser.setFirstName("Potato");
        defaultUser.setLastName("Development");
    }

    private void setupDefaultGroup() {
        defaultGroup = new Group();
        defaultGroup.setId(1);
        defaultGroup.setIdentity("12345");
        defaultGroup.setName("Default group");
    }

    private void setupUsersInGroup() {
        defaultUsersInGroup = new UsersInGroup();
        defaultUsersInGroup.setId(1);
        defaultUsersInGroup.setGroupId(defaultGroup.getId());
        defaultUsersInGroup.setUserId(defaultUser.getId());
        defaultUsersInGroup.setJoinedAt(new Timestamp(1231231231));
    }

    //TODO: проверить подходит ли мне truncate table в API Hibernate
    @BeforeMethod
    public void cleanup() throws SQLException {
        Statement databaseTruncationStatement = null;
        try {
            databaseTruncationStatement = dataSource.getConnection().createStatement();
            databaseTruncationStatement.executeUpdate("DELETE FROM usersingroups WHERE id >= 1;");
            databaseTruncationStatement.execute("ALTER SEQUENCE usersingroups_id_seq RESTART WITH 1;");
        } finally {
            databaseTruncationStatement.close();
        }
    }

    @Test
    @Transactional(propagation = Propagation.NESTED)
    public void leave_UserInGroup() throws NoUserInGroup {
        usersInGroupsDao.save(defaultUsersInGroup);
        // Data is written to table, default user is in default group
        Assert.assertEquals(usersInGroupsDao.get(defaultUsersInGroup.getId()), defaultUsersInGroup);
        // Default user leave group
        usersInGroupsDao.leave(defaultGroup, defaultUser);
        // Default user not in the group
        Assert.assertNull(usersInGroupsDao.get(defaultUsersInGroup.getId()));
    }

    @Test(expectedExceptions = NoUserInGroup.class)
    @Transactional(propagation = Propagation.NESTED)
    public void leave_NoUserInGroup() throws NoUserInGroup {
        // Try to delete row about user in group
        usersInGroupsDao.leave(defaultGroup, defaultUser);
    }
}
