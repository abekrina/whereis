package com.whereis.dao;

import com.whereis.model.User;
import com.whereis.testconfig.TestHibernateConfiguration;
import com.whereis.testconfig.TestWebMvcConfiguration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@ContextConfiguration(classes = {TestHibernateConfiguration.class, TestWebMvcConfiguration.class})
@WebAppConfiguration
public class Helper {
    User defaultUser;

    @Rollback(false)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveUser(UserDao dao) {
        setupDefaultUser();
        dao.save(defaultUser);
    }

    private void setupDefaultUser()   {
        defaultUser = new User();
        defaultUser.setId(1);
        defaultUser.setEmail("sweetpotatodevelopment@gmail.com");
        defaultUser.setFirstName("Potato");
        defaultUser.setLastName("Development");
    }

}
