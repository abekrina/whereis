package com.whereis.controller;

import com.whereis.authentication.GoogleAuthentication;
import com.whereis.dao.AbstractIntegrationTest;
import com.whereis.testconfig.TestHibernateConfiguration;
import com.whereis.testconfig.TestWebMvcConfiguration;
import com.whereis.util.ControllerTestHelper;
import com.whereis.util.DBUnitHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import com.mockrunner.mock.jdbc.MockResultSet;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.HashMap;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebAppConfiguration
@ContextConfiguration(classes = {TestHibernateConfiguration.class, TestWebMvcConfiguration.class})
public class ApiControllerIT extends AbstractIntegrationTest {
    private MockMvc mockMvc;
    @Autowired
    WebApplicationContext wac;

    @Autowired
    DataSource dataSource;

    String[] seqNames = {"groups_id_seq", "users_id_seq", "invites_id_seq",
            "usersingroups_id_seq", "locations_id_seq", "tokens_id_seq"};


    @Override
    @BeforeTest
    public void setupTestData() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .apply(springSecurity())
                .build();
    }

    @AfterTest
    public void tearDown() throws Exception {
    }

    @Test
    @WithMockUser
    public void testDeleteUserFromGroup() throws Exception {
        GoogleAuthentication authentication = ControllerTestHelper.getTestAuthentication(1,
                "sweetpotatodevelopment@gmail.com",
                "Potato", "Development");


        mockMvc.perform(delete("/group/{identity}/leave", "i1d2e3n4t5i6t7y8")
                .with(authentication(authentication)))
                .andExpect(status().isOk());

        HashMap<String, Object> user = new HashMap<>();
        user.put("id", 1);
        user.put("first_name", "Potato");
        user.put("last_name", "Development");
        user.put("email", "sweetpotatodevelopment@gmail.com");
        expectedUsers.addRow(user);

        user.put("id", 2);
        user.put("first_name", "Alena");
        user.put("last_name", "Bekrina");
        user.put("email", "abekrina@gmail.com");
        expectedUsers.addRow(user);

        HashMap<String, Object> group = new HashMap<>();
        group.put("id", 1);
        group.put("name", "BestGroup");
        group.put("identity", "i1d2e3n4t5i6t7y8");
        expectedGroups.addRow(group);

        HashMap<String, Object> userInGroup = new HashMap<>();
        userInGroup.put("id", 1);
        userInGroup.put("user_id", 1);
        userInGroup.put("group_id", 1);
        userInGroup.put("joined_at", new Timestamp(123123123));
        expectedUsersInGroups.addRow(userInGroup);

        userInGroup.put("id", 1);
        userInGroup.put("user_id", 2);
        userInGroup.put("group_id", 1);
        userInGroup.put("joined_at", new Timestamp(123123123));
        expectedUsersInGroups.addRow(userInGroup);

    <usersingroups id="1" user_id="1" group_id="1" joined_at="2017-01-11 11:11:11.000000000" />
    <usersingroups id="2" user_id="2" group_id="1" joined_at="2017-01-11 12:11:11.000000000" />

        for (String name : tableNames) {
            Statement statement = dataSource.getConnection().createStatement();
            statement.execute("SELECT * FROM " + name);
            ResultSet result = statement.getResultSet();

        }



    }

    @Test
    @WithMockUser
    public void testJoinToGroup() throws Exception {
        GoogleAuthentication authentication = ControllerTestHelper.getTestAuthentication(2,
                    "abekrina@gmail.com",
                    "Alena", "Bekrina");


        DBUnitHelper.applyDataset(databaseTester, JOIN_GROUP_SETUP);

        mockMvc.perform(post("/group/{identity}/join", "i1d2e3n4t5i6t7y8").with(authentication(authentication)))
               .andExpect(status().isOk());

        TestTransaction.flagForCommit();
        TestTransaction.end();

        IDataSet actualDataSet = databaseTester.getConnection().createDataSet();
        IDataSet expectedDataSet = DBUnitHelper.readDataSet(JOIN_GROUP_EXPECTED);

        String[] ignoreColumnes = {"joined_at"};
        Assertion.assertEqualsIgnoreCols(expectedDataSet, actualDataSet, "usersingroups", ignoreColumnes);

        for (String seq : seqNames) {
            try (Statement databaseTruncationStatement = dataSource.getConnection().createStatement()) {
               //databaseTruncationStatement.execute("ALTER SEQUENCE " + seq + " RESTART WITH 1;");
            }
        }

    }

    @Test
    @WithMockUser
    public void testJoinWithoutInvite() throws Exception {
        GoogleAuthentication authentication = ControllerTestHelper.getTestAuthentication(2,
                "abekrina@gmail.com",
                "Alena", "Bekrina");

        DBUnitHelper.applyDataset(databaseTester, JOIN_WITHOUT_INVITE_SETUP);

        mockMvc.perform(post("/group/{identity}/join", "i1d2e3n4t5i6t7y8").with(authentication(authentication)))
                .andExpect(status().isBadRequest());

        TestTransaction.end();

        IDataSet actualDataSet = databaseTester.getConnection().createDataSet();
        IDataSet expectedDataSet = DBUnitHelper.readDataSet(JOIN_WITHOUT_INVITE_EXPECTED);

        Assertion.assertEquals(expectedDataSet, actualDataSet);
    }
}
