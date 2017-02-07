package com.whereis.dao;

import com.whereis.exceptions.NoSuchUser;
import com.whereis.exceptions.UserWithEmailExists;
import com.whereis.model.User;
import com.whereis.testconfig.TestHibernateConfiguration;
import com.whereis.testconfig.TestWebMvcConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

// Настроить surefire
@ContextConfiguration(classes = {TestHibernateConfiguration.class, TestWebMvcConfiguration.class})
@WebAppConfiguration
public class DefaultUserDaoIT extends AbstractIntegrationTest {
    @Autowired
    private UserDao userDao;

    private User defaultUser;

    private void setupDefaultUser()   {
        defaultUser = new User();
        defaultUser.setEmail("sweetpotatodevelopment@gmail.com");
        defaultUser.setFirstName("Potato");
        defaultUser.setLastName("Development");
    }

    @BeforeMethod
    public void setupTestData() {
        setupDefaultUser();
    }

    @Test
    public void testSaveUser() throws UserWithEmailExists {
        userDao.save(defaultUser);
        Assert.assertEquals(userDao.get(defaultUser.getId()), defaultUser);
    }

    @Test(expectedExceptions = UserWithEmailExists.class)
    public void testUserNotSavedIfSameEmailExists() throws UserWithEmailExists {
        userDao.save(defaultUser);

        User userWithSameEmail = new User();
        userWithSameEmail.setEmail(defaultUser.getEmail());
        userWithSameEmail.setFirstName("Some");
        userWithSameEmail.setLastName("User");

        userDao.save(userWithSameEmail);
    }

    @Test
    public void testUpdateUser() throws NoSuchUser, UserWithEmailExists {
        userDao.save(defaultUser);

        defaultUser.setFirstName("Alena");
        defaultUser.setLastName("Bekrina");
        defaultUser.setEmail("abekrina@gmail.com");
        userDao.update(defaultUser);

        Assert.assertEquals(userDao.get(defaultUser.getId()).getFirstName(), "Alena");
        Assert.assertEquals(userDao.get(defaultUser.getId()).getLastName(), "Bekrina");
        Assert.assertEquals(userDao.get(defaultUser.getId()).getEmail(), "abekrina@gmail.com");
    }

    @Test(expectedExceptions = NoSuchUser.class)
    public void testUpdateNonExistingUser() throws NoSuchUser {
        // Expect exception in this case
        userDao.update(defaultUser);
    }

    @Test
    public void testGetUserByEmail() throws UserWithEmailExists {
        userDao.save(defaultUser);
        Assert.assertEquals(userDao.getByEmail(defaultUser.getEmail()), defaultUser);
    }

    @Test
    public void testDeleteUser() throws UserWithEmailExists {
        userDao.save(defaultUser);
        Assert.assertEquals(userDao.get(defaultUser.getId()), defaultUser);
        userDao.delete(defaultUser.getClass(), defaultUser.getId());
        Assert.assertNull(userDao.get(defaultUser.getId()));
    }
}