package com.whereis.dao;

import com.whereis.configuration.LogConfigurationFactory;
import com.whereis.model.User;
import com.whereis.testconfig.TestHibernateConfiguration;
import com.whereis.testconfig.TestWebMvcConfiguration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Statement;

@ContextConfiguration(classes = {TestHibernateConfiguration.class, TestWebMvcConfiguration.class})
@WebAppConfiguration
public class DefaultUserDaoIT extends AbstractTransactionalTestNGSpringContextTests {
    @Autowired
    DataSource dataSource;

    @Autowired
    UserDao userDao;

    User defaultUser;

    Helper helper = new Helper();

    @BeforeTest
    public void initialize() {
        setupDefaultUser();
        ConfigurationFactory.setConfigurationFactory(new LogConfigurationFactory());
    }

    @AfterMethod
    public void cleanup() throws SQLException {
        Statement databaseTruncationStatement = null;
        try {
            databaseTruncationStatement = dataSource.getConnection().createStatement();
            databaseTruncationStatement.executeUpdate("DELETE FROM users WHERE id >= 1;");
            databaseTruncationStatement.execute("ALTER SEQUENCE users_id_seq RESTART WITH 1;");
        } finally {
            databaseTruncationStatement.close();
        }
    }

    @Rollback(false)
    @Transactional
    public void saveUser() {
        userDao.save(defaultUser);
    }

    private void setupDefaultUser()   {
        defaultUser = new User();
        defaultUser.setId(1);
        defaultUser.setEmail("sweetpotatodevelopment@gmail.com");
        defaultUser.setFirstName("Potato");
        defaultUser.setLastName("Development");
    }

    @Rollback(false)
    @Transactional(propagation = Propagation.NEVER)
    @Test
    public void save_NoSuchUser() {
        helper.saveUser(userDao);
        Assert.assertEquals(userDao.getByEmail(defaultUser.getEmail()), defaultUser);
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    public void save_SameUserExists() {
        userDao.save(defaultUser);
        Assert.assertEquals(userDao.get(1), defaultUser);

        // Attempting to save same user one more time
        userDao.save(defaultUser);
        Assert.assertNull(userDao.get(2));
        }
}
