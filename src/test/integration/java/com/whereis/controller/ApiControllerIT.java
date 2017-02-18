package com.whereis.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.whereis.AbstractIntegrationTest;
import com.whereis.authentication.GoogleAuthentication;
import com.whereis.dao.GroupDao;
import com.whereis.dao.LocationDao;
import com.whereis.model.Group;
import com.whereis.model.Invite;
import com.whereis.model.Location;
import com.whereis.model.User;
import com.whereis.service.GroupService;
import com.whereis.service.InviteService;
import com.whereis.service.LocationService;
import com.whereis.service.UserService;
import com.whereis.testconfig.TestHibernateConfiguration;
import com.whereis.testconfig.TestWebMvcConfiguration;
import com.whereis.util.ControllerTestHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    GroupDao groupDao;

    @Autowired
    LocationService locationService;

    @Autowired
    UserService userService;

    @Autowired
    GroupService groupService;

    @Autowired
    InviteService inviteService;


    private User defaultUser1;

    private User defaultUser2;

    private Group defaultGroup;

    @BeforeMethod
    public void setup() throws SQLException {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .apply(springSecurity())
                .build();

        setupDefaultUsers();
        setupDefaultGroup();
    }

    private void setupDefaultUsers() {
        defaultUser1 = new User();
        defaultUser1.setEmail("sweetpotatodevelopment@gmail.com");
        defaultUser1.setFirstName("Potato");
        defaultUser1.setLastName("Development");

        defaultUser2 = new User();
        defaultUser2.setEmail("abekrina@gmail.com");
        defaultUser2.setFirstName("Alena");
        defaultUser2.setLastName("Bekrina");
    }

    private void setupDefaultGroup() {
        defaultGroup = new Group();
        defaultGroup.setIdentity("i1d2e3n4t5i6t7y8");
        defaultGroup.setName("Default Group");
    }

    @Test
    @WithMockUser
    public void testJoinWithoutInvite() throws Exception {
        GoogleAuthentication authentication = ControllerTestHelper.getTestAuthentication(defaultUser1);

        groupService.save(defaultGroup);
        userService.save(defaultUser1);
        userService.save(defaultUser2);

        Assert.assertNull(inviteService.getPendingInviteFor(defaultUser1, defaultGroup));

        mockMvc.perform(post("/group/{identity}/join", defaultGroup.getIdentity()).with(authentication(authentication)))
                .andExpect(status().isBadRequest());

        Assert.assertFalse(userService.checkUserInGroup(defaultGroup, defaultUser1));
    }

    @Test
    @WithMockUser
    public void testInviteAndJoinGroup() throws Exception {
        GoogleAuthentication authentication = ControllerTestHelper.getTestAuthentication(defaultUser2);

        userService.save(defaultUser1);
        userService.save((User)authentication.getPrincipal());
        groupService.save(defaultGroup);

        JsonObject invitedUser = new JsonObject();
        invitedUser.addProperty("email", defaultUser1.getEmail() );
        mockMvc.perform(MockMvcRequestBuilders.post("/group/{identity}/invite", defaultGroup.getIdentity())
                .with(authentication(authentication))
                .contentType(MediaType.APPLICATION_JSON)
                .content(invitedUser.toString()))
                .andExpect(status().isOk());

        Invite expectedInvite = new Invite();
        expectedInvite.setSentByUser((User)authentication.getPrincipal());
        expectedInvite.setSentToUser(defaultUser1);
        expectedInvite.setGroup(defaultGroup);
        expectedInvite.setTimestamp(new Timestamp(System.currentTimeMillis()));

        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();

        Invite actualInvite = inviteService.getPendingInviteFor(defaultUser1, defaultGroup);
        Assert.assertEquals(actualInvite.getGroup(), expectedInvite.getGroup());
        Assert.assertEquals(actualInvite.getSentByUser(), expectedInvite.getSentByUser());
        Assert.assertEquals(actualInvite.getSentToUser(), expectedInvite.getSentToUser());
        Assert.assertTrue(actualInvite.getTimestamp().after(expectedInvite.getTimestamp()));

        authentication = ControllerTestHelper.getTestAuthentication(defaultUser1);

        mockMvc.perform(post("/group/{identity}/join", defaultGroup.getIdentity())
                .with(authentication(authentication)))
                .andExpect(status().isOk());

        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();

        Assert.assertTrue(userService.checkUserInGroup(defaultGroup, defaultUser1));
        Assert.assertTrue(userService.getGroupsForUser(defaultUser1).contains(defaultGroup));

        mockMvc.perform(delete("/group/{identity}/leave", defaultGroup.getIdentity())
                .with(authentication(authentication)))
                .andExpect(status().isOk());

        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();

        Assert.assertFalse(userService.checkUserInGroup(defaultGroup, defaultUser1));
        Assert.assertFalse(userService.getGroupsForUser(defaultUser1).contains(defaultGroup));
    }

    //TODO: Add case when user not in the group
    @Test
    @WithMockUser
    public void testSaveLocation() throws Exception {
        userService.save(defaultUser1);
        userService.save(defaultUser2);
        groupService.save(defaultGroup);
        inviteService.saveInviteForUser(new Invite(defaultUser1, defaultGroup));
        inviteService.saveInviteForUser(new Invite(defaultUser2, defaultGroup));
        userService.joinGroup(defaultGroup, defaultUser1);
        userService.joinGroup(defaultGroup, defaultUser2);

        // Setup data
        Location expectedLocation = new Location();
        expectedLocation.setUser(defaultUser1);
        expectedLocation.setLatitude(37.345345);
        expectedLocation.setLongitude(-121.34535);
        expectedLocation.setGroup(defaultGroup);
        expectedLocation.setIp("127.0.0.1");

        GoogleAuthentication authentication = ControllerTestHelper.getTestAuthentication(defaultUser1);

        JsonObject location = new JsonObject();
        location.addProperty("latitude", expectedLocation.getLatitude());
        location.addProperty("longitude", expectedLocation.getLongitude());

        // Send query with test data
        mockMvc.perform(MockMvcRequestBuilders.put("/group/{identity}/savemylocation",
                expectedLocation.getGroup().getIdentity()).with(authentication(authentication)).contentType(MediaType.APPLICATION_JSON)
                .content(location.toString()))
                .andExpect(status().isOk());

        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();

        // Get result of query from database
        Location actualLocation = locationService.getLastLocationForUser(defaultUser1);

        Assert.assertEquals(actualLocation.getGroup(), expectedLocation.getGroup());
        Assert.assertEquals(actualLocation.getIp(), expectedLocation.getIp());
        Assert.assertEquals(actualLocation.getLatitude(), expectedLocation.getLatitude());
        Assert.assertEquals(actualLocation.getLongitude(), expectedLocation.getLongitude());
        Assert.assertEquals(actualLocation.getUser(), expectedLocation.getUser());
    }


    // TODO: add case with more locations with auth.principal location. Shouldn't return auth.principal's location
    @Test
    @WithMockUser
    @Rollback(false)
    public void testGetLocations() throws Exception {
        userService.save(defaultUser1);
        userService.save(defaultUser2);
        groupService.save(defaultGroup);
        inviteService.saveInviteForUser(new Invite(defaultUser1, defaultGroup));
        inviteService.saveInviteForUser(new Invite(defaultUser2, defaultGroup));
        userService.joinGroup(defaultGroup, defaultUser1);
        userService.joinGroup(defaultGroup, defaultUser2);

        // Setup data
        Location expectedLocation = new Location();
        expectedLocation.setUser(defaultUser2);
        expectedLocation.setLatitude(37.345345);
        expectedLocation.setLongitude(-121.34535);
        expectedLocation.setGroup(defaultGroup);
        expectedLocation.setIp("127.0.0.1");

        userService.saveUserLocation(expectedLocation);

        GoogleAuthentication authentication = ControllerTestHelper.getTestAuthentication(defaultUser1);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/group/{identity}/getlocations",
                defaultGroup.getIdentity()).with(authentication(authentication)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

        List<Location> expectedLocations = new ArrayList<>();
        expectedLocations.add(expectedLocation);

        ObjectMapper mapper = new ObjectMapper();
        String expectedResponce = mapper.writeValueAsString(expectedLocations);

        Assert.assertEquals(result.getResponse().getContentAsString(), expectedResponce);
    }
}
