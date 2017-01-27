package com.whereis.dao;

import com.whereis.exceptions.UserAlreadyInvited;
import com.whereis.model.Group;
import com.whereis.model.Invite;
import com.whereis.model.User;
import com.whereis.testconfig.TestHibernateConfiguration;
import com.whereis.testconfig.TestWebMvcConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.sql.Timestamp;

@ContextConfiguration(classes = {TestHibernateConfiguration.class, TestWebMvcConfiguration.class})
@WebAppConfiguration
public class DefaultInviteDaoIT extends AbstractIntegrationTest {
    @Autowired
    InviteDao inviteDao;

    private Invite defaultInvite;
    private User defaultUser;
    private Group defaultGroup;

    @BeforeTest
    public void setupTestData() {
        setupDefaultInvite();
        setupDefaultUser();
        setupDefaultGroup();
    }

    private void setupDefaultInvite() {
        if (defaultInvite == null) {
            defaultInvite = new Invite();
        }
        defaultInvite.setId(1);
        defaultInvite.setTimestamp(new Timestamp(123435123));
        defaultInvite.setGroupId(1);
        defaultInvite.setSentBy(1);
        defaultInvite.setSentToEmail("sweetpotatodevelopment@gmail.com");
    }

    private void setupDefaultUser() {
        if (defaultUser == null) {
            defaultUser = new User();
        }
        defaultUser.setId(1);
        defaultUser.setEmail("sweetpotatodevelopment@gmail.com");
        defaultUser.setFirstName("Potato");
        defaultUser.setLastName("Development");
    }

    private void setupDefaultGroup() {
        if (defaultGroup == null) {
            defaultGroup = new Group();
        }
        defaultGroup.setId(1);
        defaultGroup.setIdentity("12345");
        defaultGroup.setName("Default group");
    }

    @Test
    public void testSaveInvite() throws UserAlreadyInvited {
        inviteDao.save(defaultInvite);
        Assert.assertEquals(inviteDao.get(defaultInvite.getId()), defaultInvite);
    }

    @Test(expectedExceptions = UserAlreadyInvited.class)
    public void testSaveDuplicateInvite() throws UserAlreadyInvited {
        inviteDao.save(defaultInvite);
        Assert.assertEquals(inviteDao.get(defaultInvite.getId()), defaultInvite);
        inviteDao.save(defaultInvite);
    }

    @Test
    public void testGetSameInvite() throws UserAlreadyInvited {
        inviteDao.save(defaultInvite);

        Invite sameInvite = new Invite();
        sameInvite.setGroupId(defaultInvite.getGroupId());
        sameInvite.setSentToEmail(defaultInvite.getSentToEmail());
        sameInvite.setSentBy(3);

        Assert.assertEquals(inviteDao.getSameInvite(sameInvite), defaultInvite);
    }

    @Test
    public void testGetSameInviteIfNotExists() {
        Assert.assertNull(inviteDao.getSameInvite(defaultInvite));
    }

    @Test
    public void testGetPendingInvite() throws UserAlreadyInvited {
        inviteDao.save(defaultInvite);
        Assert.assertEquals(inviteDao.getPendingInviteFor(defaultUser, defaultGroup), defaultInvite);
    }

    @Test
    public void testGetPendingInviteIfNotExists() {
        Assert.assertNull(inviteDao.getPendingInviteFor(defaultUser, defaultGroup));
    }
}
