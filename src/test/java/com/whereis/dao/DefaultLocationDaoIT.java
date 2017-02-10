package com.whereis.dao;

import com.whereis.exceptions.users.UserWithEmailExists;
import com.whereis.model.Group;
import com.whereis.model.Location;
import com.whereis.model.User;
import com.whereis.testconfig.TestHibernateConfiguration;
import com.whereis.testconfig.TestWebMvcConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.Timestamp;

import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;

@ContextConfiguration(classes = {TestHibernateConfiguration.class, TestWebMvcConfiguration.class})
@WebAppConfiguration
public class DefaultLocationDaoIT extends AbstractIntegrationTest {
    @Autowired
    LocationDao locationDao;

    @Autowired
    UserDao userDao;

    private Location defaultLocation;
    private Location defaultLocation2;

    private User defaultUser;
    private Group defaultGroup;

    @BeforeMethod
    public void setupTestData() {
        setupDefaultUser();
        setupDefaultGroup();
        setupDefaultLocation();
    }

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

    private void setupDefaultLocation() {
        defaultLocation = new Location();
        defaultLocation.setUser(defaultUser);
        defaultLocation.setLatitude(111111);
        defaultLocation.setLongitude(222222);
        defaultLocation.setIp("192.168.0.0");
        defaultLocation.setGroup(defaultGroup);

        defaultLocation2 = new Location();
        defaultLocation2.setUser(defaultUser);
        defaultLocation2.setLatitude(222222);
        defaultLocation2.setLongitude(222222);
        defaultLocation2.setIp("192.168.0.0");
        defaultLocation2.setGroup(defaultGroup);
    }

    @Test
    public void testSaveLocation() {
        locationDao.save(defaultLocation);
        Assert.assertEquals(locationDao.get(defaultLocation.getId()), defaultLocation);
    }

    @Test
    public void testGetLastLocationForUser() throws UserWithEmailExists {
        userDao.save(defaultUser);
        // Save locations
        userDao.saveUserLocation(defaultLocation, defaultUser);
        userDao.saveUserLocation(defaultLocation2, defaultUser);

        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();

        Assert.assertEquals(locationDao.getLastLocationForUser(defaultUser), defaultLocation2);
        Assert.assertTrue(locationDao.getLastLocationForUser(defaultUser).getTimestamp().after(locationDao.get(defaultLocation.getId()).getTimestamp()));
    }
}
