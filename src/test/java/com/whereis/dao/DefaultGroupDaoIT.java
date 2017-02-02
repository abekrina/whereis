package com.whereis.dao;

import com.whereis.exceptions.GroupWithIdentityExists;
import com.whereis.exceptions.NoSuchGroup;
import com.whereis.model.Group;
import com.whereis.testconfig.TestHibernateConfiguration;
import com.whereis.testconfig.TestWebMvcConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import javax.sql.DataSource;


@ContextConfiguration(classes = {TestHibernateConfiguration.class, TestWebMvcConfiguration.class})
@WebAppConfiguration
public class DefaultGroupDaoIT extends AbstractIntegrationTest {
    @Autowired
    DataSource dataSource;

    @Autowired
    GroupDao groupDao;

    Group defaultGroup;

    @BeforeTest
    public void setupTestData() {
        setupDefaultGroup();
    }

    private void setupDefaultGroup() {
        if (defaultGroup == null) {
            defaultGroup = new Group();
        }
        defaultGroup.setIdentity("12345");
        defaultGroup.setName("Default Group");
    }

    @Test
    public void testSaveGroup() throws GroupWithIdentityExists {
        groupDao.save(defaultGroup);
        Assert.assertEquals(groupDao.getByIdentity(defaultGroup.getIdentity()), defaultGroup);
    }

    @Test(expectedExceptions = GroupWithIdentityExists.class)
    public void testGroupNotSavedIfIdentityExists() throws GroupWithIdentityExists {
        groupDao.save(defaultGroup);
        Assert.assertEquals(groupDao.getByIdentity(defaultGroup.getIdentity()), defaultGroup);

        Group anotherGroup = new Group();
        anotherGroup.setIdentity(defaultGroup.getIdentity());
        anotherGroup.setName("Another group");

        groupDao.save(anotherGroup);
    }

    @Test
    public void testUpdateGroup() throws GroupWithIdentityExists, NoSuchGroup {
        groupDao.save(defaultGroup);
        Assert.assertEquals(groupDao.getByIdentity(defaultGroup.getIdentity()), defaultGroup);

        defaultGroup.setName("Name changed");

        groupDao.update(defaultGroup);
        Assert.assertEquals(groupDao.getByIdentity(defaultGroup.getIdentity()).getName(), "Name changed");
    }

    @Test(expectedExceptions = NoSuchGroup.class)
    public void testUpdateNonExistingUser() throws NoSuchGroup {
        groupDao.update(defaultGroup);
    }

    @Test
    public void testGetGroupByIdentity() throws GroupWithIdentityExists {
        groupDao.save(defaultGroup);
        Assert.assertEquals(groupDao.getByIdentity(defaultGroup.getIdentity()), defaultGroup);
    }
}
