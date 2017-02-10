package com.whereis.dao;

import com.whereis.exceptions.groups.GroupWithIdentityExistsException;
import com.whereis.exceptions.groups.NoUserInGroupException;
import com.whereis.exceptions.groups.UserAlreadyInGroupException;
import com.whereis.exceptions.users.NoSuchUserException;
import com.whereis.exceptions.users.UserWithEmailExistsException;
import com.whereis.model.Group;
import com.whereis.model.User;
import com.whereis.testconfig.TestHibernateConfiguration;
import com.whereis.testconfig.TestWebMvcConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Set;

// Настроить surefire
@ContextConfiguration(classes = {TestHibernateConfiguration.class, TestWebMvcConfiguration.class})
@WebAppConfiguration
public class DefaultUserDaoIT extends AbstractIntegrationTest {
    @Autowired
    private UserDao userDao;

    @Autowired
    private GroupDao groupDao;

    private User defaultUser;

    private Group defaultGroup;

    private void setupDefaultUser()   {
        defaultUser = new User();
        defaultUser.setEmail("sweetpotatodevelopment@gmail.com");
        defaultUser.setFirstName("Potato");
        defaultUser.setLastName("Development");
    }

    private void setupDefaultGroup() {
        defaultGroup = new Group();
        defaultGroup.setIdentity("12345");
        defaultGroup.setName("Default Group");
    }

    @BeforeMethod
    public void setupTestData() {
        setupDefaultUser();
        setupDefaultGroup();
    }

    @Test
    public void testSaveUser() throws UserWithEmailExistsException {
        userDao.save(defaultUser);
        Assert.assertEquals(userDao.get(defaultUser.getId()), defaultUser);
    }

    @Test(expectedExceptions = UserWithEmailExistsException.class)
    public void testUserNotSavedIfSameEmailExists() throws UserWithEmailExistsException {
        userDao.save(defaultUser);

        User userWithSameEmail = new User();
        userWithSameEmail.setEmail(defaultUser.getEmail());
        userWithSameEmail.setFirstName("Some");
        userWithSameEmail.setLastName("User");

        userDao.save(userWithSameEmail);
    }

    @Test
    public void testUpdateUser() throws NoSuchUserException, UserWithEmailExistsException {
        userDao.save(defaultUser);

        defaultUser.setFirstName("Alena");
        defaultUser.setLastName("Bekrina");
        defaultUser.setEmail("abekrina@gmail.com");
        userDao.update(defaultUser);

        Assert.assertEquals(userDao.get(defaultUser.getId()).getFirstName(), "Alena");
        Assert.assertEquals(userDao.get(defaultUser.getId()).getLastName(), "Bekrina");
        Assert.assertEquals(userDao.get(defaultUser.getId()).getEmail(), "abekrina@gmail.com");
    }

    @Test(expectedExceptions = NoSuchUserException.class)
    public void testUpdateNonExistingUser() throws NoSuchUserException {
        // Expect exception in this case
        userDao.update(defaultUser);
    }

    @Test
    public void testGetUserByEmail() throws UserWithEmailExistsException {
        userDao.save(defaultUser);
        Assert.assertEquals(userDao.getByEmail(defaultUser.getEmail()), defaultUser);
    }

    @Test
    public void testDeleteUser() throws UserWithEmailExistsException {
        userDao.save(defaultUser);
        Assert.assertEquals(userDao.get(defaultUser.getId()), defaultUser);
        userDao.delete(defaultUser.getClass(), defaultUser.getId());
        Assert.assertNull(userDao.get(defaultUser.getId()));
    }
//TODO: move to service test
/*    @Test
    public void testLeaveGroup() throws NoUserInGroupException, UserAlreadyInGroupException, UserWithEmailExistsException, GroupWithIdentityExistsException {
        userDao.save(defaultUser);
        groupDao.save(defaultGroup);
        int id = defaultUser.getId();
        userDao.joinGroup(defaultGroup, defaultUser);
        // Data is written to table, default user is in default group
        Assert.assertTrue(userDao.assertUserInGroup(defaultGroup, defaultUser));

        TestTransaction.flagForCommit();
        TestTransaction.end();

        id = defaultUser.getId();
        // Default user leave group
        userDao.leaveGroup(defaultGroup, defaultUser);
        // Default user not in the group
        Assert.assertFalse(userDao.assertUserInGroup(defaultGroup, defaultUser));
    }

    @Test(expectedExceptions = NoUserInGroupException.class)
    public void testLeaveUserNotGroupMember() throws NoUserInGroupException {
        // Try to delete row about user in group
        userDao.leaveGroup(defaultGroup, defaultUser);
    }


    //TODO: think should I change this to check result not by my code? If not, how should I test "join group"?
    //TODO: for now join group test is ommited, add it when decide what to do
    @Test
    public void testJoinAndGetGroupsForUser() throws UserAlreadyInGroupException {
        // Add user to group
        userDao.joinGroup(defaultGroup, defaultUser);
        // Set expected result
        Set<Group> expected = new HashSet<>();
        expected.add(defaultGroup);

        Set<Group> groupsOfUser = userDao.getGroupsForUser(defaultUser);

        Assert.assertEquals(groupsOfUser, expected);
    }

    @Test
    public void testFindAbsentRelationInDB() {
        Assert.assertEquals(userDao.getGroupsForUser(defaultUser), new HashSet<Group>());
    }

    @Test(expectedExceptions = UserAlreadyInGroupException.class)
    public void testSaveRelationAlreadyExisting() throws UserAlreadyInGroupException {
        // Add user to group
        userDao.joinGroup(defaultGroup, defaultUser);

        // Add same user one more time
        userDao.joinGroup(defaultGroup, defaultUser);
    }*/
}