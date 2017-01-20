package com.whereis.dao;

import com.whereis.configuration.LogConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class AbstractIntTestForDao extends AbstractTransactionalTestNGSpringContextTests {
    String MAIN_TABLE;
    String MAIN_SEQUENCE;

    @Autowired
    DataSource dataSource;

    @BeforeTest
    public void initialize() {
        // Set configuration for logger
        ConfigurationFactory.setConfigurationFactory(new LogConfigurationFactory());
    }

    @BeforeMethod
    public void cleanup() throws SQLException {
        setupTestData();

        try (Statement databaseTruncationStatement = dataSource.getConnection().createStatement()) {
            databaseTruncationStatement.executeUpdate("DELETE FROM " + MAIN_TABLE + " WHERE id >= 1;");
            databaseTruncationStatement.execute("ALTER SEQUENCE " + MAIN_SEQUENCE + " RESTART WITH 1;");
        }
    }

    abstract void setupTestData();
}
