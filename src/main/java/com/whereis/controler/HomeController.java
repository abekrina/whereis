package com.whereis.controler;

import com.whereis.model.User;
import com.whereis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @Autowired
    private UserService userService;

    @RequestMapping("/")
    public String home() {
        User user = new User();
        user.setName("Alena");
        user.setEmail("larmyztab@gmail.com");
        userService.saveUser(user);

        return "42";
    }

    @RequestMapping("/find_user")
    public User findUser() {
        return userService.findUserByEmail("larmyztab@gmail.com");
    }


}
