package com.whereis.controller;

import com.google.gson.JsonObject;
import com.whereis.authentication.GoogleAuthentication;
import com.whereis.dao.AbstractIntegrationTest;
import com.whereis.dao.LocationDao;
import com.whereis.model.Location;
import com.whereis.model.User;
import com.whereis.testconfig.TestHibernateConfiguration;
import com.whereis.testconfig.TestWebMvcConfiguration;
import com.whereis.util.ControllerTestHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.sql.DataSource;

import java.sql.SQLException;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebAppConfiguration
@ContextConfiguration(classes = {TestHibernateConfiguration.class, TestWebMvcConfiguration.class})
public class ApiControllerIT extends AbstractIntegrationTest {
    private MockMvc mockMvc;
    @Autowired
    WebApplicationContext wac;

    JdbcTemplate  jdbcTemplate = new JdbcTemplate();

    @Autowired
    DataSource dataSource;

    @Autowired
    LocationDao locationDao;

    private User defaultUser1;

    private User defaultUser2;

    @BeforeMethod
    public void setup() throws SQLException {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .apply(springSecurity())
                .build();

        setupDefaultUsers();
    }

    private void setupDefaultUsers()   {
        if (defaultUser1 == null) {
            defaultUser1 = new User();
        }
        defaultUser1.setEmail("sweetpotatodevelopment@gmail.com");
        defaultUser1.setFirstName("Potato");
        defaultUser1.setLastName("Development");

        if (defaultUser2 == null) {
            defaultUser2 = new User();
        }
        defaultUser2.setEmail("abekrina@gmail.com");
        defaultUser2.setFirstName("Alena");
        defaultUser2.setLastName("Bekrina");
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
    }

    @Test
    @WithMockUser
    public void testJoinToGroup() throws Exception {
        GoogleAuthentication authentication = ControllerTestHelper.getTestAuthentication(2,
                    "abekrina@gmail.com",
                    "Alena", "Bekrina");

        mockMvc.perform(post("/group/{identity}/join", "i1d2e3n4t5i6t7y8").with(authentication(authentication)))
               .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void testJoinWithoutInvite() throws Exception {
        GoogleAuthentication authentication = ControllerTestHelper.getTestAuthentication(2,
                "abekrina@gmail.com",
                "Alena", "Bekrina");

        mockMvc.perform(post("/group/{identity}/join", "i1d2e3n4t5i6t7y8").with(authentication(authentication)))
                .andExpect(status().isBadRequest());
    }

    //TODO: Add case when user not in the group
    @Test
    @WithMockUser
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
                    scripts = "classpath:controller-it-setup/setupSaveLocation.sql")})
    public void testSaveLocation() throws Exception {
        // Setup data
        Location expectedLocation = new Location();
        expectedLocation.setUser(defaultUser1);
        expectedLocation.setLatitude(37.345345);
        expectedLocation.setLongitude(-121.34535);
        expectedLocation.setGroupIdentity("i1d2e3n4t5i6t7y8");
        expectedLocation.setIp("127.0.0.1");

        GoogleAuthentication authentication = ControllerTestHelper.getTestAuthentication(1,
                "sweetpotatodevelopment@gmail.com",
                "Potato", "Development");

        JsonObject location = new JsonObject();
        location.addProperty("latitude", expectedLocation.getLatitude());
        location.addProperty("longitude", expectedLocation.getLongitude());

        // Send query with test data
        mockMvc.perform(MockMvcRequestBuilders.put("/group/{identity}/savemylocation",
                expectedLocation.getGroupIdentity()).with(authentication(authentication)).contentType(MediaType.APPLICATION_JSON)
                .content(location.toString()))
                .andExpect(status().isOk());

        // Get result of query from database
        Location actualLocation = locationDao.getLastLocationForUser(defaultUser1);

        Assert.assertEquals(actualLocation.getGroupIdentity(), expectedLocation.getGroupIdentity());
        Assert.assertEquals(actualLocation.getIp(), expectedLocation.getIp());
        Assert.assertEquals(actualLocation.getLatitude(), expectedLocation.getLatitude());
        Assert.assertEquals(actualLocation.getLongitude(), expectedLocation.getLongitude());
        Assert.assertEquals(actualLocation.getUser(), expectedLocation.getUser());
    }


    // TODO: add case with more locations with auth.principal location. Shouldn't return auth.principal's location
    @Test
    @WithMockUser
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
                    scripts = "classpath:controller-it-setup/setupGetLocation.sql")})
    public void testGetLocations() throws Exception {
        // Setup data
        Location expectedLocation = new Location();
        expectedLocation.setUser(defaultUser2);
        expectedLocation.setLatitude(37.345345);
        expectedLocation.setLongitude(-121.34535);
        expectedLocation.setGroupIdentity("i1d2e3n4t5i6t7y8");
        expectedLocation.setIp("127.0.0.1");

        GoogleAuthentication authentication = ControllerTestHelper.getTestAuthentication(1,
                "sweetpotatodevelopment@gmail.com",
                "Potato", "Development");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/group/{identity}/getlocations",
                "i1d2e3n4t5i6t7y8").with(authentication(authentication)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
        String expectedJson = "[null,{" +
                "\"id\":1," +
                "\"timestamp\":1455767272690," +
                "\"latitude\":37.345345," +
                "\"longitude\":-121.232323," +
                "\"ip\":\"127.0.0.1\"," +
                "\"userId\":2," +
                "\"groupIdentity\":\"i1d2e3n4t5i6t7y8\"" +
                "}]";
        Assert.assertEquals(result.getResponse().getContentAsString(), expectedJson);
    }
}
