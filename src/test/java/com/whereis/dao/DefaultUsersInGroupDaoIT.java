package com.whereis.dao;

import com.whereis.exceptions.NoUserInGroup;
import com.whereis.exceptions.UserAlreadyInGroup;
import com.whereis.model.Group;
import com.whereis.model.User;
import com.whereis.model.UsersInGroup;
import com.whereis.testconfig.TestHibernateConfiguration;
import com.whereis.testconfig.TestWebMvcConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import javax.sql.DataSource;
import java.sql.Timestamp;

@ContextConfiguration(classes = {TestHibernateConfiguration.class, TestWebMvcConfiguration.class})
@WebAppConfiguration
public class DefaultUsersInGroupDaoIT extends AbstractIntegrationTest {
    @Autowired
    DataSource dataSource;

    @Autowired
    UsersInGroupsDao usersInGroupsDao;

    User defaultUser;
    Group defaultGroup;
    UsersInGroup defaultUsersInGroup;


    private void setupDefaultUser() {
        if (defaultUser == null) {
            defaultUser = new User();
        }
        defaultUser.setEmail("sweetpotatodevelopment@gmail.com");
        defaultUser.setFirstName("Potato");
        defaultUser.setLastName("Development");
    }

    private void setupDefaultGroup() {
        if (defaultGroup == null) {
            defaultGroup = new Group();
        }
        defaultGroup.setIdentity("12345");
        defaultGroup.setName("Default group");
    }

    private void setupUsersInGroup() {
        if (defaultUsersInGroup == null) {
            defaultUsersInGroup = new UsersInGroup();
        }
        defaultUsersInGroup.setGroup(defaultGroup);
        defaultUsersInGroup.setUser(defaultUser);
        defaultUsersInGroup.setJoinedAt(new Timestamp(1231231231));
    }

    @BeforeTest
    public void setupTestData() {
        setupDefaultUser();
        setupDefaultGroup();
        setupUsersInGroup();
    }

    @Test
    public void testLeaveGroup() throws NoUserInGroup, UserAlreadyInGroup {
        usersInGroupsDao.save(defaultUsersInGroup);
        // Data is written to table, default user is in default group
        Assert.assertEquals(usersInGroupsDao.get(defaultUsersInGroup.getId()), defaultUsersInGroup);
        // Default user leave group
        usersInGroupsDao.leave(defaultGroup, defaultUser);
        // Default user not in the group
        Assert.assertNull(usersInGroupsDao.get(defaultUsersInGroup.getId()));
    }

    @Test(expectedExceptions = NoUserInGroup.class)
    public void testLeaveUserNotGroupMember() throws NoUserInGroup {
        // Try to delete row about user in group
        usersInGroupsDao.leave(defaultGroup, defaultUser);
    }

    @Test
    public void testFindRelationInDB() throws UserAlreadyInGroup {
        usersInGroupsDao.save(defaultUsersInGroup);
        Assert.assertEquals(usersInGroupsDao.findUserInGroup(defaultUsersInGroup), defaultUsersInGroup);
    }

    @Test
    public void testFindAbsentRelationInDB() {
        Assert.assertNull(usersInGroupsDao.findUserInGroup(defaultUsersInGroup));
    }

    @Test
    public void testSaveRelation() throws UserAlreadyInGroup {
        usersInGroupsDao.save(defaultUsersInGroup);
        Assert.assertEquals(usersInGroupsDao.get(defaultUsersInGroup.getId()), defaultUsersInGroup);
    }

    @Test(expectedExceptions = UserAlreadyInGroup.class)
    public void testSaveRelationAlreadyExisting() throws UserAlreadyInGroup {
        usersInGroupsDao.save(defaultUsersInGroup);
        Assert.assertEquals(usersInGroupsDao.get(defaultUsersInGroup.getId()), defaultUsersInGroup);
        usersInGroupsDao.save(defaultUsersInGroup);
    }
}
