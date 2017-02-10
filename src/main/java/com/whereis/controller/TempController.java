package com.whereis.controller;

import com.whereis.exceptions.users.UserWithEmailExistsException;
import com.whereis.model.Location;
import com.whereis.model.User;
import com.whereis.service.LocationService;
import com.whereis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//TODO: All methods here will be rewritten

@RestController
public class TempController extends AbstractController {

    @Autowired
    private UserService userService;

    @Autowired
    private LocationService locationService;

    @RequestMapping("/saveUser")
    public String saveUser() {
        User user = new User();
        user.setFirstName("Alena");
        user.setLastName("Bekrina");
        user.setEmail("larmyztab@gmail.com");
        try {
            userService.save(user);
        } catch (UserWithEmailExistsException userWithEmailExistsException) {
            userWithEmailExistsException.printStackTrace();
            return "user exists";
        }

        return "42";
    }

    @RequestMapping("/getUserByEmail")
    public User getUserByEmail() {
        return userService.getByEmail("larmyztab@gmail.com");
    }

    @RequestMapping("/getUserLocation")
    public Location getUserLocation() {
        User user = new User();
        //user.setName("Alena");
        user.setEmail("larmyztab@gmail.com");
        return locationService.getLastLocationForUser(user);
    }

    @RequestMapping("/saveLocation")
    public void saveUserLocation() {
        User user = new User();
        //user.setName("Alena");
        user.setEmail("larmyztab@gmail.com");
        Location location = new Location();
        location.setLatitude(0.123);
        location.setLongitude(0.321);
        location.setIp("192.168.0.0");

        locationService.save(location);
    }

    @RequestMapping("/getLastLocationForUser")
    public Location getLastLocationForUser() {
        User user = userService.getByEmail("larmyztab@gmail.com");
        Location lastLocation = locationService.getLastLocationForUser(user);
        return  lastLocation;
    }
}
