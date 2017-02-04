package com.whereis.model;

import com.sun.istack.internal.NotNull;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.sql.Timestamp;

@Immutable
@Entity
@Table(name = "locations")
public class Location {
    @Id
    @GeneratedValue
    protected int id;

    //TODO: override equals and hashcode
    @ManyToOne
    @JoinColumn(name = "user_id")
    protected User user;

    @NotNull
    @Column(nullable = false, name = "timestamp")
    protected Timestamp timestamp;

    @NotNull
    @Column(nullable = false)
    protected double latitude;

    @NotNull
    @Column(nullable = false)
    protected double longitude;

    protected String ip;

    @NotNull
    @Column(nullable = false)
    protected String group_identity;

    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
