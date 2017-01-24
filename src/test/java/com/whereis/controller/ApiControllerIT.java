package com.whereis.controller;

import com.whereis.authentication.GoogleAuthentication;
import com.whereis.testconfig.TestHibernateConfiguration;
import com.whereis.testconfig.TestWebMvcConfiguration;
import com.whereis.util.ControllerTestHelper;
import com.whereis.util.DBUnitHelper;
import org.dbunit.Assertion;
import org.dbunit.IDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.sql.DataSource;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {TestHibernateConfiguration.class, TestWebMvcConfiguration.class})
public class ApiControllerIT {
    private MockMvc mockMvc;
    @Autowired
    WebApplicationContext wac;

    private static final String DATASET_PATH = "src/test/resources/initialize-db.xml";

    private IDatabaseTester databaseTester;
    private IDataSet dataSet;

    @Autowired
    private DataSource dataSource;


    @Before
    public void setUp() throws Exception {
        dataSet = DBUnitHelper.readDataSet(DATASET_PATH);
        databaseTester = DBUnitHelper.setUpDatabaseTester(dataSource, dataSet);
        databaseTester.onSetup();

        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .apply(springSecurity())
                .build();
    }

    @After
    public void tearDown() throws Exception {
        databaseTester.onTearDown();
    }

    @Test
    @WithMockUser
    public void deleteUserFromGroup() throws Exception {
        GoogleAuthentication authentication = ControllerTestHelper.getTestAuthentication(1,
                "sweetpotatodevelopment@gmail.com",
                "Potato", "Development");
        mockMvc.perform(delete("/group/{identity}/leave", "i1d2e3n4t5i6t7y8")
                .with(authentication(authentication)))
                .andExpect(status().isOk());
        IDataSet actualDataSet = databaseTester.getConnection().createDataSet();
        IDataSet expectedDataSet = DBUnitHelper.readDataSet("src/test/resources/user-left-group-expected.xml");
        Assertion.assertEquals(expectedDataSet, actualDataSet);

    }
}
