package com.whereis.model;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "locations")
public class Location {
    @Id
    @GeneratedValue
    protected int id;

    protected int user_id;

    protected Timestamp timestamp;

    protected double latitude;

    protected double longitude;

    protected String ip;

    protected String group_identity;

    public int getId() {
        return id;
    }

    public int getUserId() {
        return user_id;
    }

    public void setUserId(int userId) {
        this.user_id = userId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }


    public String getGroupIdentity() {
        return group_identity;
    }

    public void setGroupIdentity(String group_identity) {
        this.group_identity = group_identity;
    }

}
