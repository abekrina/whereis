package com.whereis.dao;

import com.whereis.AbstractIntegrationTest;
import com.whereis.exceptions.groups.GroupWithIdentityExistsException;
import com.whereis.model.Group;
import com.whereis.model.Location;
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

@ContextConfiguration(classes = {TestHibernateConfiguration.class, TestWebMvcConfiguration.class})
@WebAppConfiguration
public class DefaultLocationDaoIT extends AbstractIntegrationTest {
    @Autowired
    LocationDao locationDao;

    @Autowired
    UserDao userDao;

    @Autowired
    GroupDao groupDao;

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
        defaultGroup.setName("Default Group");
    }

    private void setupDefaultLocation() {
        defaultLocation = new Location();
        defaultLocation.setUser(defaultUser);
        defaultLocation.setLatitude(111111);
        defaultLocation.setLongitude(222222);
        defaultLocation.setIp("192.168.0.0");
        defaultLocation.setGroup(defaultGroup);
    }

    @Test
    public void testSaveLocation() throws GroupWithIdentityExistsException {
        groupDao.save(defaultGroup);
        locationDao.save(defaultLocation);

        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();

        Assert.assertEquals(locationDao.get(defaultLocation.getId()), defaultLocation);
        Assert.assertNotNull(locationDao.get(defaultLocation.getId()).getTimestamp());
    }
}
