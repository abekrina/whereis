package com.whereis.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.whereis.authentication.GoogleAuthentication;
import com.whereis.dao.AbstractIntegrationTest;
import com.whereis.testconfig.TestHibernateConfiguration;
import com.whereis.testconfig.TestWebMvcConfiguration;
import com.whereis.util.ControllerTestHelper;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.http.converter.json.GsonFactoryBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import javax.sql.DataSource;

import java.sql.SQLException;

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

    @BeforeMethod
    public void setup() throws SQLException {
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

    @Test
    @WithMockUser
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
                    scripts = "classpath:controller-it-setup-cleanup/setupSaveLocation.sql")})
    public void testSaveLocation() throws Exception {
        GoogleAuthentication authentication = ControllerTestHelper.getTestAuthentication(1,
                "sweetpotatodevelopment@gmail.com",
                "Potato", "Development");

        JsonObject location = new JsonObject();
        location.addProperty("latitude", 37.345345);
        location.addProperty("longitude", -121.34535);
        mockMvc.perform(MockMvcRequestBuilders.put("/group/{identity}/savemylocation",
                "i1d2e3n4t5i6t7y8").with(authentication(authentication)).contentType(MediaType.APPLICATION_JSON)
                .content(location.toString()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
                    scripts = "classpath:controller-it-setup-cleanup/setupSaveLocation.sql")})
    public void testGetLocations() {
        
    }
}
