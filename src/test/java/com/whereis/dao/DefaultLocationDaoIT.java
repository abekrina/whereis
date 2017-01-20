package com.whereis.dao;

import com.whereis.model.Location;
import com.whereis.model.User;
import com.whereis.testconfig.TestHibernateConfiguration;
import com.whereis.testconfig.TestWebMvcConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.sql.Timestamp;

@ContextConfiguration(classes = {TestHibernateConfiguration.class, TestWebMvcConfiguration.class})
@WebAppConfiguration
public class DefaultLocationDaoIT extends AbstractIntTestForDao {
    @Autowired
    LocationDao locationDao;

    private Location defaultLocation;

    @Override
    void setupTestData() {
        MAIN_TABLE = "locations";
        MAIN_SEQUENCE = "locations_id_seq";

        setupDefaultLocation();
    }

    private void setupDefaultLocation() {
        if (defaultLocation == null) {
            defaultLocation = new Location();
        }
        defaultLocation.setId(1);
        defaultLocation.setUserId(1);
        defaultLocation.setTimestamp(new Timestamp(1484767310));
        defaultLocation.setLatitude(111111);
        defaultLocation.setLongitude(222222);
        defaultLocation.setIp("192.168.0.0");
    }

    @Test
    public void testSaveLocation() {
        locationDao.save(defaultLocation);
        Assert.assertEquals(locationDao.get(1), defaultLocation);
    }

    @Test
    public void testGetLastLocationForUser() {
        // Save location
        locationDao.save(defaultLocation);

        // Save location with next timestamp
        Timestamp newTimestamp = new Timestamp(1484767374);
        defaultLocation.setTimestamp(newTimestamp);
        locationDao.save(defaultLocation);

        Assert.assertEquals(locationDao.getLastLocationForUser(1).getTimestamp(), newTimestamp);
    }
}
