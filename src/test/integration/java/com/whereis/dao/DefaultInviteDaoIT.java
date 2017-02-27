package com.whereis.dao;

import com.whereis.AbstractIntegrationTest;
import com.whereis.exceptions.groups.GroupWithIdentityExistsException;
import com.whereis.exceptions.invites.UserAlreadyInvitedException;
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
public class DefaultInviteDaoIT extends AbstractIntegrationTest {
    @Autowired
    InviteDao inviteDao;

    @Autowired
    GroupDao groupDao;

    @Autowired
    UserDao userDao;

    private Invite defaultInvite;
    private User defaultSentToUser;
    private User defaultSentByUser;
    private Group defaultGroup;

    @BeforeMethod
    public void setupTestData() {
        setupDefaultUsers();
        setupDefaultGroup();
        setupDefaultInvite();
    }

    private void setupDefaultUsers() {
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
        defaultGroup.setName("Default group");
    }

    private void setupDefaultInvite() {
        defaultInvite = new Invite();
        defaultInvite.setGroup(defaultGroup);
        defaultInvite.setSentByUser(defaultSentByUser);
        defaultInvite.setSentToUser(defaultSentToUser);
    }

    @Test
    public void testSaveInvite() throws UserAlreadyInvitedException, GroupWithIdentityExistsException, UserWithEmailExistsException {
        userDao.save(defaultSentToUser);
        userDao.save(defaultSentByUser);
        groupDao.save(defaultGroup);
        inviteDao.save(defaultInvite);

        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();

        Assert.assertEquals(inviteDao.get(defaultInvite.getId()), defaultInvite);
        Assert.assertNotNull(inviteDao.get(defaultInvite.getId()).getTimestamp());
    }

    @Test(expectedExceptions = UserAlreadyInvitedException.class)
    public void testSaveDuplicateInvite() throws UserAlreadyInvitedException, GroupWithIdentityExistsException, UserWithEmailExistsException {
        userDao.save(defaultSentToUser);
        userDao.save(defaultSentByUser);
        groupDao.save(defaultGroup);
        inviteDao.save(defaultInvite);

        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();

        userDao.refresh(defaultSentToUser);
        Invite duplicateInvite = new Invite();
        duplicateInvite.setGroup(defaultInvite.getGroup());
        duplicateInvite.setSentByUser(defaultSentByUser);
        duplicateInvite.setSentToUser(defaultSentToUser);

        Assert.assertEquals(inviteDao.get(defaultInvite.getId()), defaultInvite);
        inviteDao.save(defaultInvite);
    }

    @Test
    public void testGetSameInvite() throws UserAlreadyInvitedException, GroupWithIdentityExistsException, UserWithEmailExistsException {
        userDao.save(defaultSentToUser);
        userDao.save(defaultSentByUser);
        groupDao.save(defaultGroup);
        inviteDao.save(defaultInvite);

        Invite sameInvite = new Invite();
        sameInvite.setGroup(defaultInvite.getGroup());
        sameInvite.setSentToUser(defaultInvite.getSentToUser());
        sameInvite.setSentByUser(defaultInvite.getSentByUser());

        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();

        Assert.assertEquals(inviteDao.getSameInvite(sameInvite), defaultInvite);
    }

    @Test
    public void testGetSameInviteIfNotExists() throws GroupWithIdentityExistsException, UserWithEmailExistsException {
        userDao.save(defaultSentToUser);
        groupDao.save(defaultGroup);

        Assert.assertNull(inviteDao.getSameInvite(defaultInvite));
    }
}
