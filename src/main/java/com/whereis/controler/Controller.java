package com.whereis.controler;

import com.whereis.model.User;
import com.whereis.model.UserLocation;
import com.whereis.service.UserLocationService;
import com.whereis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;

@RestController
public class Controller {
    @Autowired
    private UserService userService;

    @Autowired
    private UserLocationService userLocationService;

    @RequestMapping("/")
    public String home() {
        User user = new User();
        user.setName("Alena");
        user.setEmail("larmyztab@gmail.com");
        userService.saveUser(user);

        return "42";
    }

    @RequestMapping("/findUser")
    //
    public User findUser() {
        return userService.findUserByEmail("larmyztab@gmail.com");
    }

    @RequestMapping("/getUserLocation")
    public UserLocation getUserLocation() {
        User user = new User();
        user.setName("Alena");
        user.setEmail("larmyztab@gmail.com");
        return userLocationService.getCurrentUserLocation(user);
    }

    @RequestMapping("/saveUserLocation")
    public void saveUserLocation() {
        User user = new User();
        user.setName("Alena");
        user.setEmail("larmyztab@gmail.com");
        user.setId(1);
        UserLocation userLocation = new UserLocation();
        userLocation.setLatitude(0.123);
        userLocation.setLongitude(0.321);
        userLocation.setTimestamp(new Timestamp(1481877171));
        userLocation.setIp("192.168.0.0");
        userLocation.setUser(user);

        userLocationService.saveUserLocation(userLocation);
    }

}
