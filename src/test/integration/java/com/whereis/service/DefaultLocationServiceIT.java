package com.whereis.service;

import com.whereis.AbstractIntegrationTest;
import com.whereis.exceptions.groups.GroupException;
import com.whereis.exceptions.users.UserException;
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
public class DefaultLocationServiceIT extends AbstractIntegrationTest {
    @Autowired
    UserService userService;

    @Autowired
    GroupService groupService;

    @Autowired
    LocationService locationService;

    private User defaultUser;

    private Group defaultGroup;

    private Location defaultLocation;
    private Location defaultLocation2;
    private Location defaultLocation3;


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

        defaultLocation3 = new Location();
        defaultLocation3.setUser(defaultUser);
        defaultLocation3.setLatitude(333333);
        defaultLocation3.setLongitude(333333);
        defaultLocation3.setIp("192.168.0.0");
        defaultLocation3.setGroup(defaultGroup);
    }

    @BeforeMethod
    public void setupTestData() {
        setupDefaultUser();
        setupDefaultGroup();
        setupDefaultLocation();
    }

    @Test
    public void testGetLastLocationForUser() throws UserException, GroupException {
        userService.save(defaultUser);
        groupService.save(defaultGroup);

        // Save locations
        userService.saveUserLocation(defaultLocation);
        userService.saveUserLocation(defaultLocation2);
        userService.saveUserLocation(defaultLocation3);

        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();

        Assert.assertEquals(locationService.getLastLocationForUser(defaultUser), defaultLocation3);
        Assert.assertTrue(locationService.getLastLocationForUser(defaultUser).getTimestamp()
                .after(locationService.get(defaultLocation2.getId()).getTimestamp()) ||
                locationService.getLastLocationForUser(defaultUser).getTimestamp()
                        .equals(locationService.get(defaultLocation2.getId()).getTimestamp()));
    }
}
