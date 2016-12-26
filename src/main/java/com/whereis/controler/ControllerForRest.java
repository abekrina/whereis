package com.whereis.controler;

import com.whereis.model.Location;
import com.whereis.model.User;
import com.whereis.service.DefaultLocationService;
import com.whereis.service.LocationService;
import com.whereis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;

//TODO: All methods here would be rewritten

@RestController
public class ControllerForRest {
    @Autowired
    private UserService userService;

    @Autowired
    private LocationService locationService;

    @RequestMapping("/saveUser")
    public String saveUser() {
        User user = new User();
        user.setName("Alena");
        user.setEmail("larmyztab@gmail.com");
        userService.save(user);

        return "42";
    }

    @RequestMapping("/getUserByEmail")
    public User getUserByEmail() {
        return userService.getByEmail("larmyztab@gmail.com");
    }

    @RequestMapping("/getUserLocation")
    public Location getUserLocation() {
        User user = new User();
        user.setName("Alena");
        user.setEmail("larmyztab@gmail.com");
        return locationService.getLastLocationForUser(user);
    }

    @RequestMapping("/saveLocation")
    public void saveUserLocation() {
        User user = new User();
        user.setName("Alena");
        user.setEmail("larmyztab@gmail.com");
        user.setId(1);
        Location location = new Location();
        location.setLatitude(0.123);
        location.setLongitude(0.321);
        location.setTimestamp(new Timestamp(1481877171));
        location.setIp("192.168.0.0");
        location.setUserId(user.getId());

        locationService.save(location);
    }

    @RequestMapping("/getLastLocationForUser")
    public Location getLastLocationForUser() {
        User user = userService.getByEmail("larmyztab@gmail.com");
        Location lastLocation = locationService.getLastLocationForUser(user);
        return  lastLocation;
    }

}
