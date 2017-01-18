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
import org.testng.annotations.Test;

import javax.sql.DataSource;


@ContextConfiguration(classes = {TestHibernateConfiguration.class, TestWebMvcConfiguration.class})
@WebAppConfiguration
public class DefaultGroupDaoIT extends AbstractIntTestForDao {
    @Autowired
    DataSource dataSource;

    @Autowired
    GroupDao groupDao;

    Group defaultGroup;

    @Override
    void setupTestData() {
        setupDefaultGroup();
        MAIN_TABLE = "groups";
        MAIN_SEQUENCE = "groups_id_seq";
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
    public void testSaveGroup() throws GroupWithIdentityExists {
        groupDao.save(defaultGroup);
        Assert.assertEquals(groupDao.get(1), defaultGroup);
    }

    @Test(expectedExceptions = GroupWithIdentityExists.class)
    public void testGroupNotSavedIfIdentityExists() throws GroupWithIdentityExists {
        groupDao.save(defaultGroup);
        Assert.assertEquals(groupDao.get(1), defaultGroup);

        Group anotherGroup = new Group();
        anotherGroup.setIdentity(defaultGroup.getIdentity());
        anotherGroup.setId(2);
        anotherGroup.setName("Another group");

        groupDao.save(anotherGroup);
    }

    @Test
    public void testUpdateGroup() throws GroupWithIdentityExists, NoSuchGroup {
        groupDao.save(defaultGroup);
        Assert.assertEquals(groupDao.get(1), defaultGroup);

        defaultGroup.setName("Name changed");
        defaultGroup.setIdentity("12345");

        groupDao.update(defaultGroup);
        Assert.assertEquals(groupDao.get(1).getName(), "Name changed");
        Assert.assertEquals(groupDao.get(1).getIdentity(), "12345");
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
