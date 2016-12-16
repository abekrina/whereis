package com.whereis.service;

import com.whereis.dao.UserLocationDao;
import com.whereis.dao.UserLocationDaoImpl;
import com.whereis.model.User;
import com.whereis.model.UserLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userLocationService")
@Transactional
public class UserLocationService {
    @Autowired
    UserLocationDaoImpl userLocationDao;

    public UserLocation getCurrentUserLocation(User user) {
        return userLocationDao.getCurrentLocationOfUser(user);
    }

    public void saveUserLocation(UserLocation userLocation) {
        userLocationDao.saveUserLocation(userLocation);
    }
}
