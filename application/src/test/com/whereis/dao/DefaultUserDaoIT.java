package com.whereis.dao;

import com.whereis.AbstractIntegrationTest;
import com.whereis.testconfig.TestHibernateConfiguration;
import com.whereis.testconfig.TestWebMvcConfiguration;
import com.whereis.exceptions.users.NoSuchUserException;
import com.whereis.exceptions.users.UserWithEmailExistsException;
import com.whereis.model.Group;
import com.whereis.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

// TODO: настроить записть логов в файл
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
}