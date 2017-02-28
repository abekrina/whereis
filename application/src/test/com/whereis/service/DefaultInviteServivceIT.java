package com.whereis.service;

import com.whereis.AbstractIntegrationTest;
import com.whereis.testconfig.TestHibernateConfiguration;
import com.whereis.testconfig.TestWebMvcConfiguration;
import com.whereis.exceptions.groups.GroupException;
import com.whereis.exceptions.invites.InviteException;
import com.whereis.exceptions.users.UserException;
import com.whereis.model.Group;
import com.whereis.model.Invite;
import com.whereis.model.User;
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

    private User defaultSentToUser;

    private User defaultSentByUser;

    private Group defaultGroup;

    private Invite defaultInvite;

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
        setupDefaultInvite();
    }

    @Test
    public void testGetPendingInvite() throws UserException, GroupException, InviteException {
        userService.save(defaultSentToUser);
        userService.save(defaultSentByUser);
        groupService.save(defaultGroup);
        inviteService.saveInviteForUser(defaultInvite);

        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();

        Assert.assertEquals(inviteService.getPendingInviteFor(defaultSentToUser, defaultGroup), defaultInvite);
    }

    @Test
    public void testGetPendingInviteIfNotExists() throws GroupException, UserException {
        userService.save(defaultSentToUser);
        groupService.save(defaultGroup);

        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();

        Assert.assertNull(inviteService.getPendingInviteFor(defaultSentToUser, defaultGroup));
    }
}
