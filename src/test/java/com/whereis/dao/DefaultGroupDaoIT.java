package com.whereis.dao;

import com.whereis.exceptions.groups.GroupWithIdentityExists;
import com.whereis.exceptions.groups.NoSuchGroup;
import com.whereis.exceptions.groups.UserAlreadyInGroup;
import com.whereis.exceptions.users.UserWithEmailExists;
import com.whereis.model.Group;
import com.whereis.model.User;
import com.whereis.model.UsersInGroup;
import com.whereis.testconfig.TestHibernateConfiguration;
import com.whereis.testconfig.TestWebMvcConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


@ContextConfiguration(classes = {TestHibernateConfiguration.class, TestWebMvcConfiguration.class})
@WebAppConfiguration
public class DefaultGroupDaoIT extends AbstractIntegrationTest {

    @Autowired
    GroupDao groupDao;

    @Autowired
    UserDao userDao;

    @Autowired
    UsersInGroupsDao usersInGroupsDao;

    private Group defaultGroup;

    private User defaultOwnerUser;

    private UsersInGroup defaultUserToGroup;

    @BeforeMethod
    public void setupTestData() throws UserWithEmailExists, UserAlreadyInGroup {
        setupDefaultGroup();
        setupDefaultOwnerUser();
    }

    private void setupDefaultGroup() {
        defaultGroup = new Group();
        defaultGroup.setIdentity("12345");
        defaultGroup.setName("Default Group");
    }

    private void setupDefaultOwnerUser() throws UserWithEmailExists {
        defaultOwnerUser = new User();
        defaultOwnerUser.setEmail("sweetpotatodevelopment@gmail.com");
        defaultOwnerUser.setFirstName("Potato");
        defaultOwnerUser.setLastName("Development");
        //defaultOwnerUser.getGroups().add(new UsersInGroup(defaultOwnerUser, defaultGroup, new Timestamp(123123123)));
        userDao.save(defaultOwnerUser);
    }

    @Test
    public void testSaveGroup() throws GroupWithIdentityExists {
        groupDao.save(defaultGroup);
        Assert.assertEquals(groupDao.getByIdentity(defaultGroup.getIdentity()), defaultGroup);
    }

    @Test(expectedExceptions = GroupWithIdentityExists.class)
    public void testGroupNotSavedIfIdentityExists() throws GroupWithIdentityExists {
        groupDao.save(defaultGroup);
        Assert.assertEquals(groupDao.getByIdentity(defaultGroup.getIdentity()), defaultGroup);

        Group anotherGroup = new Group();
        anotherGroup.setIdentity(defaultGroup.getIdentity());
        anotherGroup.setName("Another group");

        groupDao.save(anotherGroup);
    }

    @Test
    public void testUpdateGroup() throws GroupWithIdentityExists, NoSuchGroup {
        groupDao.save(defaultGroup);
        Assert.assertEquals(groupDao.getByIdentity(defaultGroup.getIdentity()), defaultGroup);

        defaultGroup.setName("Name changed");

        groupDao.update(defaultGroup);
        Assert.assertEquals(groupDao.getByIdentity(defaultGroup.getIdentity()).getName(), "Name changed");
    }

    @Test(expectedExceptions = NoSuchGroup.class)
    public void testUpdateNonExistingUser() throws NoSuchGroup {
        groupDao.update(defaultGroup);
    }

    @Test
    public void testGetGroupByIdentity() throws GroupWithIdentityExists {
        groupDao.save(defaultGroup);
        Assert.assertEquals(groupDao.getByIdentity(defaultGroup.getIdentity()), defaultGroup);
    }
}
