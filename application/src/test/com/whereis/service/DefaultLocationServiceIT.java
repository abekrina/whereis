package com.whereis.service;

import com.whereis.AbstractIntegrationTest;
import com.whereis.testconfig.TestHibernateConfiguration;
import com.whereis.testconfig.TestWebMvcConfiguration;
import com.whereis.exceptions.groups.GroupException;
import com.whereis.exceptions.users.UserException;
import com.whereis.model.Group;
import com.whereis.model.Invite;
import com.whereis.model.Location;
import com.whereis.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

@ContextConfiguration(classes = {TestHibernateConfiguration.class, TestWebMvcConfiguration.class})
@WebAppConfiguration
public class DefaultLocationServiceIT extends AbstractIntegrationTest {
    @Autowired
    UserService userService;

    @Autowired
    GroupService groupService;

    @Autowired
    LocationService locationService;

    @Autowired
    InviteService inviteService;

    private User defaultSentToUser;
    private User defaultSentByUser;

    private Invite defaultInvite;
    private Group defaultGroup;

    private Location defaultLocation;
    private Location defaultLocation2;
    private Location defaultLocation3;


    private void setupDefaultUsers()   {
        defaultSentToUser = new User();
        defaultSentToUser.setEmail("sweetpotatodevelopment@gmail.com");
        defaultSentToUser.setFirstName("Potato");
        defaultSentToUser.setLastName("Development");

        defaultSentByUser = new User();
        defaultSentByUser.setEmail("abekrina@gmail.com");
        defaultSentByUser.setFirstName("Alena");
        defaultSentByUser.setLastName("Bekrina");
    }

    private void setupDefaultGroup() {
        defaultGroup = new Group();
        defaultGroup.setIdentity("12345");
        defaultGroup.setName("Default Group");
    }

    private void setupDefaultLocation() {
        defaultLocation = new Location();
        defaultLocation.setUser(defaultSentToUser);
        defaultLocation.setLatitude(111111);
        defaultLocation.setLongitude(222222);
        defaultLocation.setIp("192.168.0.0");
        defaultLocation.setGroup(defaultGroup);

        defaultLocation2 = new Location();
        defaultLocation2.setUser(defaultSentToUser);
        defaultLocation2.setLatitude(222222);
        defaultLocation2.setLongitude(222222);
        defaultLocation2.setIp("192.168.0.0");
        defaultLocation2.setGroup(defaultGroup);

        defaultLocation3 = new Location();
        defaultLocation3.setUser(defaultSentToUser);
        defaultLocation3.setLatitude(333333);
        defaultLocation3.setLongitude(333333);
        defaultLocation3.setIp("192.168.0.0");
        defaultLocation3.setGroup(defaultGroup);
    }

    private void setupDefaultInvite() {
        defaultInvite = new Invite();
        defaultInvite.setSentToUser(defaultSentToUser);
        defaultInvite.setGroup(defaultGroup);
        defaultInvite.setSentByUser(defaultSentByUser);
    }

    @BeforeMethod
    public void setupTestData() {
        setupDefaultUsers();
        setupDefaultGroup();
        setupDefaultLocation();
        setupDefaultInvite();
    }

    @Test
    public void testGetLastLocationForUser() throws UserException, GroupException {
        userService.save(defaultSentToUser);
        groupService.save(defaultGroup);

        // Save locations
        userService.saveUserLocation(defaultLocation);
        userService.saveUserLocation(defaultLocation2);
        userService.saveUserLocation(defaultLocation3);

        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();

        Assert.assertEquals(locationService.getLastLocationForUser(defaultSentToUser), defaultLocation3);
        Assert.assertTrue(locationService.getLastLocationForUser(defaultSentToUser).getTimestamp()
                .after(locationService.get(defaultLocation2.getId()).getTimestamp()) ||
                locationService.getLastLocationForUser(defaultSentToUser).getTimestamp()
                        .equals(locationService.get(defaultLocation2.getId()).getTimestamp()));
    }

    @Test
    public void testGetLastLocationsForGroupMembers() throws Exception {
        userService.save(defaultSentToUser);
        userService.save(defaultSentByUser);
        groupService.save(defaultGroup);

        inviteService.saveInviteForUser(defaultInvite);
        userService.joinGroup(defaultGroup, defaultSentToUser);

        // Save locations
        userService.saveUserLocation(defaultLocation);
        userService.saveUserLocation(defaultLocation2);
        userService.saveUserLocation(defaultLocation3);

        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();

        List<Location> expectedLocations = new ArrayList<>();
        expectedLocations.add(defaultLocation3);

        Group testGroup = groupService.get(defaultGroup.getId());

        testGroup.getUsersInGroup();

        Assert.assertEquals(locationService.getLastLocationsForGroupMembers(defaultGroup), expectedLocations);
    }
}
