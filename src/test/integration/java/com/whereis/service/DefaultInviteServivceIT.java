package com.whereis.service;

import com.whereis.AbstractIntegrationTest;
import com.whereis.exceptions.groups.GroupException;
import com.whereis.exceptions.invites.InviteException;
import com.whereis.exceptions.invites.UserAlreadyInvitedException;
import com.whereis.exceptions.users.UserException;
import com.whereis.exceptions.users.UserWithEmailExistsException;
import com.whereis.model.Group;
import com.whereis.model.Invite;
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
public class DefaultInviteServivceIT extends AbstractIntegrationTest {
    @Autowired
    UserService userService;

    @Autowired
    GroupService groupService;

    @Autowired
    InviteService inviteService;

    private User defaultUser;

    private Group defaultGroup;

    private Invite defaultInvite;

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

    private void setupDefaultInvite() {
        defaultInvite = new Invite();
        defaultInvite.setSentToUser(defaultUser);
        defaultInvite.setGroup(defaultGroup);
    }

    @BeforeMethod
    public void setupTestData() {
        setupDefaultUser();
        setupDefaultGroup();
        setupDefaultInvite();
    }

    @Test
    public void testGetPendingInvite() throws UserException, GroupException, InviteException {
        userService.save(defaultUser);
        groupService.save(defaultGroup);
        inviteService.saveInviteForUser(defaultInvite);

        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();

        Assert.assertEquals(inviteService.getPendingInviteFor(defaultUser, defaultGroup), defaultInvite);
    }

    @Test
    public void testGetPendingInviteIfNotExists() throws GroupException, UserException {
        userService.save(defaultUser);
        groupService.save(defaultGroup);

        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();

        Assert.assertNull(inviteService.getPendingInviteFor(defaultUser, defaultGroup));
    }
}
