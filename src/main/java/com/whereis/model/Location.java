package com.whereis.model;

import com.sun.istack.internal.NotNull;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

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

    @Column(name = "timestamp", updatable = false)
    @Type(type = "java.time.LocalDateTime")
    @Temporal(value = TemporalType.TIMESTAMP)
    @CreationTimestamp
    protected LocalDateTime timestamp;

    @NotNull
    @Column(nullable = false)
    protected double latitude;

    @NotNull
    @Column(nullable = false)
    protected double longitude;

    protected String ip;
    // Change to hibernate mapping
    @NotNull
    @Column(nullable = false)
    protected String group_identity;

    public Location() {

    }

    public Location(double latitude, double longitude, String ip, String group_identity, User user) {
        this.latitude = latitude;
        this.longitude = longitude;
        if (ip != null && ip != "") {
            this.ip = ip;
        }
        this.group_identity = group_identity;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
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
