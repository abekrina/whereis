package com.whereis.dao;

import com.whereis.exceptions.UserWithEmailExists;
import com.whereis.model.Location;
import com.whereis.model.User;
import com.whereis.testconfig.TestHibernateConfiguration;
import com.whereis.testconfig.TestWebMvcConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.sql.Timestamp;

@ContextConfiguration(classes = {TestHibernateConfiguration.class, TestWebMvcConfiguration.class})
@WebAppConfiguration
public class DefaultLocationDaoIT extends AbstractIntegrationTest {
    @Autowired
    LocationDao locationDao;

    private Location defaultLocation;
    private Location defaultLocation2;

    private User defaultUser;

    @BeforeMethod
    public void setupTestData() {
        setupDefaultUser();
        setupDefaultLocation();
    }

    private void setupDefaultUser()   {
        defaultUser = new User();
        defaultUser.setEmail("sweetpotatodevelopment@gmail.com");
        defaultUser.setFirstName("Potato");
        defaultUser.setLastName("Development");
    }

    private void setupDefaultLocation() {
        defaultLocation = new Location();
        defaultLocation.setUser(defaultUser);
        defaultLocation.setTimestamp(new Timestamp(1484767310));
        defaultLocation.setLatitude(111111);
        defaultLocation.setLongitude(222222);
        defaultLocation.setIp("192.168.0.0");
        defaultLocation.setGroupIdentity("1i2d3e4n5t6i7t8y");

        defaultLocation2 = new Location();
        defaultLocation2.setUser(defaultUser);
        defaultLocation2.setTimestamp(new Timestamp(1584767374));
        defaultLocation2.setLatitude(111111);
        defaultLocation2.setLongitude(222222);
        defaultLocation2.setIp("192.168.0.0");
        defaultLocation2.setGroupIdentity("1i2d3e4n5t6i7t8y");
    }

    @Test
    public void testSaveLocation() {
        locationDao.save(defaultLocation);
        Assert.assertEquals(locationDao.get(defaultLocation.getId()), defaultLocation);
    }

    @Test
    public void testGetLastLocationForUser() {
        Timestamp newTimestamp = new Timestamp(1584767374);
      /*  // Save location
        locationDao.save(defaultLocation);

        // Save location with next timestamp

        defaultLocation.setTimestamp(newTimestamp);
        locationDao.save(defaultLocation);*/

        defaultUser.getLocations().add(defaultLocation);
        defaultUser.getLocations().add(defaultLocation2);

        TestTransaction.flagForCommit();
        TestTransaction.end();

//        Assert.assertEquals(locationDao.getLastLocationForUser(defaultUser), defaultLocation);
        Assert.assertEquals(locationDao.getLastLocationForUser(defaultUser).getTimestamp(), newTimestamp);
    }
}
