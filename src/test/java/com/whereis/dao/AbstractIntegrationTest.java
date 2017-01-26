package com.whereis.dao;

import com.mockrunner.mock.jdbc.MockResultSet;
import com.whereis.configuration.LogConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class AbstractIntegrationTest extends AbstractTransactionalTestNGSpringContextTests {
    String MAIN_TABLE;
    String MAIN_SEQUENCE;

    public String[] tableNames = {"groups", "users", "invites", "usersingroups", "locations", "tokens"};

    public String[] groupsColumnes = {"id", "name", "identity"};
    public String[] usersColumnes = {"id", "first_name", "last_name", "email"};
    public String[] invitesColumnes = {"id", "sent_by", "sent_to_email", "timestamp", "group_id"};
    public String[] usersInGroupsColumnes = {"id", "user_id", "group_id", "joined_at"};
    public String[] locationsColumnes = {"id", "user_id", "group_id", "latitude", "longitude", "timestamp", "ip"};
    public String[] tokensColumnes = {"id", "user_id", "access_token", "refresh_token",
            "token_type", "expires_in", "isssued", "scope"};

    public MockResultSet expectedGroups;
    public MockResultSet expectedUsers;
    public MockResultSet expectedInvites;
    public MockResultSet expectedUsersInGroups;
    public MockResultSet expectedLocations;
    public MockResultSet expectedTokens;

    @Autowired
    DataSource dataSource;

    @BeforeTest
    public void initialize() {
        // Set configuration for logger
        ConfigurationFactory.setConfigurationFactory(new LogConfigurationFactory());
    }

    @BeforeMethod
    public void cleanupMainTable() throws SQLException {
        setupTestData();
        setupMocks();

        if (MAIN_TABLE == null && MAIN_SEQUENCE == null) {
            try (Statement databaseTruncationStatement = dataSource.getConnection().createStatement()) {
                databaseTruncationStatement.executeUpdate("DELETE FROM " + MAIN_TABLE + " WHERE id >= 1;");
                databaseTruncationStatement.execute("ALTER SEQUENCE " + MAIN_SEQUENCE + " RESTART WITH 1;");
            }
        }
    }

    @BeforeTransaction
    public void cleanup() throws SQLException {
        for (String table : tableNames) {
            try (Statement databaseTruncationStatement = dataSource.getConnection().createStatement()) {
                databaseTruncationStatement.executeUpdate("TRUNCATE " + MAIN_TABLE);
            }
        }
    }

    public abstract void setupTestData();

    private void setupMocks() {
        for (String columnName : groupsColumnes) {
            expectedGroups.addColumn(columnName);
        }
        for (String columnName : usersColumnes) {
            expectedUsers.addColumn(columnName);
        }
        for (String columnName : invitesColumnes) {
            expectedInvites.addColumn(columnName);
        }
        for (String columnName : locationsColumnes) {
            expectedLocations.addColumn(columnName);
        }
        for (String columnName : usersInGroupsColumnes) {
            expectedUsersInGroups.addColumn(columnName);
        }
        for (String columnName : tokensColumnes) {
            expectedTokens.addColumn(columnName);
        }
    }
}
