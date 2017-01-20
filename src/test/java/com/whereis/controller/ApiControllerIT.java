package com.whereis.controller;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.whereis.authentication.GoogleAuthentication;
import com.whereis.model.User;
import com.whereis.testconfig.TestHibernateConfiguration;
import com.whereis.testconfig.TestWebMvcConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@ContextConfiguration(classes = {TestHibernateConfiguration.class, TestWebMvcConfiguration.class})
@DatabaseSetup("classpath:initialize-db.xml")
public class ApiControllerIT {
    private MockMvc mockMvc;
    @Autowired
    WebApplicationContext wac;


    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .apply(springSecurity())
                .build();
    }

    @After
    public void after() {

    }

    @Test
    @WithMockUser
    // как это работает?
    // exception while allowing TestExecutionListener [com.github.springtestdbunit.DbUnitTestExecutionListener@2a40cd94] to process 'after' execution for test
    @ExpectedDatabase("classpath:user-left-group-expected.xml")
    public void deleteUserFromGroup() throws Exception {
        mockMvc.perform(delete("/group/{identity}/leave", "i1d2e3n4t5i6t7y8").with(authentication(getTestAuthentication())))
                .andExpect(status().isOk());
               // .andExpect(content().string("{\"id\":1,\"description\":\"Lorem ipsum\",\"title\":\"Foo\"}"));

        //.andExpect(content().mimeType(IntegrationTestUtil.APPLICATION_JSON_UTF8))
    }

    private GoogleAuthentication getTestAuthentication() {
        User testUser = new User();
        testUser.setId(1);
        testUser.setEmail("sweetpotatodevelopment@gmail.com");
        testUser.setFirstName("Potato");
        testUser.setLastName("Development");
        GoogleAuthentication authentication = new GoogleAuthentication("123", "123");
        authentication.setPrincipal(testUser);
        authentication.setAuthenticated(true);
        return authentication;
    }
}
