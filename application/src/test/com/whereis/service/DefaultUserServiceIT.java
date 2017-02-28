package com.whereis.service;

import com.whereis.testconfig.TestHibernateConfiguration;
import com.whereis.AbstractIntegrationTest;
import com.whereis.exceptions.groups.GroupException;
import com.whereis.exceptions.groups.NoUserInGroupException;
import com.whereis.exceptions.groups.UserAlreadyInGroupException;
import com.whereis.exceptions.invites.InviteException;
import com.whereis.exceptions.users.UserException;
import com.whereis.model.Group;
import com.whereis.model.Invite;
import com.whereis.model.User;
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


@ContextConfiguration(classes = {TestHibernateConfiguration.class, TestWebMvcConfiguration.class})
@WebAppConfiguration
public class DefaultUserServiceIT extends AbstractIntegrationTest {
    @Autowired
    UserService userService;

    @Autowired
    GroupService groupService;

    @Autowired
    InviteService inviteService;

    private User defaultUser;

    private User defaultUser2;

    private Group defaultGroup;

    private Invite defaultInvite;

    private void setupDefaultUser()   {
        defaultUser = new User();
        defaultUser.setEmail("sweetpotatodevelopment@gmail.com");
        defaultUser.setFirstName("Potato");
        defaultUser.setLastName("Development");

        defaultUser2 = new User();
        defaultUser2.setEmail("abekrina@gmail.com");
        defaultUser2.setFirstName("Alena");
        defaultUser2.setLastName("Bekrina");
    }

    private void setupDefaultGroup() {
        defaultGroup = new Group();
        defaultGroup.setIdentity("12345");
        defaultGroup.setName("Default Group");
    }

    private void setupDefaultInvite() {
        defaultInvite = new Invite();
        defaultInvite.setSentToUser(defaultUser);
        defaultInvite.setGroup(defaultGroup);
        defaultInvite.setSentByUser(defaultUser2);
    }

    @BeforeMethod
    public void setupTestData() {
        setupDefaultUser();
        setupDefaultGroup();
        setupDefaultInvite();
    }

    @Test
    public void testLeaveGroup() throws Exception {
        userService.save(defaultUser);
        groupService.save(defaultGroup);
        inviteService.saveInviteForUser(defaultInvite);

        userService.joinGroup(defaultGroup, defaultUser);
        // Data is written to table, default user is in default group
        Assert.assertTrue(userService.checkUserInGroup(defaultGroup, defaultUser));

        // Default user leave group
        userService.leaveGroup(defaultGroup, defaultUser);
        // Default user not in the group
        Assert.assertFalse(userService.checkUserInGroup(defaultGroup, defaultUser));
    }

    @Test(expectedExceptions = NoUserInGroupException.class)
    public void testLeaveUserNotGroupMember() throws Exception {
        userService.save(defaultUser);
        groupService.save(defaultGroup);
        // Try to delete row about user in group
        userService.leaveGroup(defaultGroup, defaultUser);
    }

    @Test
    public void testJoinAndGetGroupsForUser() throws UserException, InviteException, GroupException {
        userService.save(defaultUser);
        groupService.save(defaultGroup);
        inviteService.saveInviteForUser(defaultInvite);

        // Add user to group
        userService.joinGroup(defaultGroup, defaultUser);
        // Set expected result
        Set<Group> expected = new HashSet<>();
        expected.add(defaultGroup);

        Set<Group> groupsOfUser = userService.getGroupsForUser(defaultUser);

        Assert.assertEquals(groupsOfUser, expected);
    }

    @Test
    public void testFindAbsentRelationInDB() throws Exception {
        userService.save(defaultUser);
        groupService.save(defaultGroup);
        Assert.assertEquals(userService.getGroupsForUser(defaultUser), new HashSet<Group>());
    }

    @Test(expectedExceptions = UserAlreadyInGroupException.class)
    public void testSaveRelationAlreadyExisting() throws GroupException, InviteException, UserException {
        userService.save(defaultUser);
        userService.save(defaultUser2);
        groupService.save(defaultGroup);
        inviteService.saveInviteForUser(defaultInvite);

        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();

        // Add user to group
        userService.joinGroup(defaultGroup, defaultUser);

        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();

        Assert.assertTrue(userService.checkUserInGroup(defaultGroup, defaultUser));

        Invite newInvite = new Invite(defaultInvite.getSentToUser(), defaultInvite.getGroup(), defaultInvite.getSentByUser());
        inviteService.saveInviteForUser(newInvite);

        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();

        // Add same user one more time
        userService.joinGroup(defaultGroup, defaultUser);
  }
}
