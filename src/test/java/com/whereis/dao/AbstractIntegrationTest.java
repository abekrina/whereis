package com.whereis.dao;

import com.whereis.configuration.LogConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@WebAppConfiguration
public abstract class AbstractIntegrationTest extends AbstractTransactionalTestNGSpringContextTests {

    public String[] tableNames = {"groups", "users", "invites", "usersingroups", "locations", "tokens"};

    public String[] groupsColumnes = {"id", "name", "identity"};
    public String[] usersColumnes = {"id", "first_name", "last_name", "email"};
    public String[] invitesColumnes = {"id", "sent_by", "sent_to_email", "timestamp", "group_id"};
    public String[] usersInGroupsColumnes = {"id", "user_id", "group_id", "joined_at"};
    public String[] locationsColumnes = {"id", "user_id", "group_id", "latitude", "longitude", "timestamp", "ip"};
    public String[] tokensColumnes = {"id", "user_id", "access_token", "refresh_token",
            "token_type", "expires_in", "isssued", "scope"};

    String[] seqNames = {"groups_id_seq", "users_id_seq", "invites_id_seq",
            "usersingroups_id_seq", "locations_id_seq", "tokens_id_seq"};

    @Autowired
    DataSource dataSource;

    @BeforeTest
    public void initialize() {
        // Set configuration for logger
        ConfigurationFactory.setConfigurationFactory(new LogConfigurationFactory());
    }

    @AfterMethod
    public void cleanup() throws SQLException {
        for (String tableName : tableNames) {
            if (TestTransaction.isActive()) {
                TestTransaction.end();
                TestTransaction.start();
            }
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            // if Postgresql:
            //statement.execute("TRUNCATE TABLE " + tableName + " CASCADE;");

            // if h2:
            statement.execute("SET REFERENTIAL_INTEGRITY FALSE;");
            statement.execute("TRUNCATE TABLE " + tableName + ";");
            statement.execute("SET REFERENTIAL_INTEGRITY TRUE;");
        }
    }
}
