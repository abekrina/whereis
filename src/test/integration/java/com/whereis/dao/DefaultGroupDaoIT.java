package com.whereis.dao;

import com.whereis.AbstractIntegrationTest;
import com.whereis.exceptions.groups.GroupWithIdentityExistsException;
import com.whereis.exceptions.groups.NoSuchGroupException;
import com.whereis.exceptions.groups.UserAlreadyInGroupException;
import com.whereis.exceptions.users.UserWithEmailExistsException;
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

    private Group defaultGroup;

    private User defaultOwnerUser;

    @BeforeMethod
    public void setupTestData() throws UserWithEmailExistsException, UserAlreadyInGroupException {
        setupDefaultGroup();
        setupDefaultOwnerUser();
    }

    private void setupDefaultGroup() {
        defaultGroup = new Group();
        defaultGroup.setIdentity("12345");
        defaultGroup.setName("Default Group");
    }

    private void setupDefaultOwnerUser() throws UserWithEmailExistsException {
        defaultOwnerUser = new User();
        defaultOwnerUser.setEmail("sweetpotatodevelopment@gmail.com");
        defaultOwnerUser.setFirstName("Potato");
        defaultOwnerUser.setLastName("Development");
        //defaultOwnerUser.getGroups().add(new UsersInGroup(defaultOwnerUser, defaultGroup, new Timestamp(123123123)));
        userDao.save(defaultOwnerUser);
    }

    @Test
    public void testSaveGroup() throws GroupWithIdentityExistsException {
        groupDao.save(defaultGroup);
        Assert.assertEquals(groupDao.getByIdentity(defaultGroup.getIdentity()), defaultGroup);
    }

    @Test(expectedExceptions = GroupWithIdentityExistsException.class)
    public void testGroupNotSavedIfIdentityExists() throws GroupWithIdentityExistsException {
        groupDao.save(defaultGroup);
        Assert.assertEquals(groupDao.getByIdentity(defaultGroup.getIdentity()), defaultGroup);

        Group anotherGroup = new Group();
        anotherGroup.setIdentity(defaultGroup.getIdentity());
        anotherGroup.setName("Another group");

        groupDao.save(anotherGroup);
    }

    @Test
    public void testUpdateGroup() throws GroupWithIdentityExistsException, NoSuchGroupException {
        groupDao.save(defaultGroup);
        Assert.assertEquals(groupDao.getByIdentity(defaultGroup.getIdentity()), defaultGroup);

        defaultGroup.setName("Name changed");

        groupDao.update(defaultGroup);
        Assert.assertEquals(groupDao.getByIdentity(defaultGroup.getIdentity()).getName(), "Name changed");
    }

    @Test(expectedExceptions = NoSuchGroupException.class)
    public void testUpdateNonExistingUser() throws NoSuchGroupException {
        groupDao.update(defaultGroup);
    }

    @Test
    public void testGetGroupByIdentity() throws GroupWithIdentityExistsException {
        groupDao.save(defaultGroup);
        Assert.assertEquals(groupDao.getByIdentity(defaultGroup.getIdentity()), defaultGroup);
    }
}
