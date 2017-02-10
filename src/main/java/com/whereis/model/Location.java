package com.whereis.model;

import com.sun.istack.internal.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Immutable
@Entity
@Table(name = "locations")
public class Location {
    @Id
    @GeneratedValue
    protected int id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    protected User user;

    @Column(name = "timestamp")
    @Type(type = "java.sql.Timestamp")
    @Temporal(value = TemporalType.TIMESTAMP)
    @CreationTimestamp
    protected Timestamp timestamp;

    @NotNull
    @Column(nullable = false)
    protected double latitude;

    @NotNull
    @Column(nullable = false)
    protected double longitude;

    protected String ip;

    @NotNull
    @ManyToOne()
    @JoinColumn(name = "group_id")
    protected Group group;

    public Location() {}

    public Location(double latitude, double longitude, String ip, Group group, User user) {
        this.latitude = latitude;
        this.longitude = longitude;
        if (ip != null && !ip.equals("")) {
            this.ip = ip;
        }
        this.group = group;
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

    public Timestamp getTimestamp() {
        return timestamp;
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

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @Override
    public boolean equals(Object anotherLocation) {
        if (anotherLocation == this) {
            return true;
        }
        if (!(anotherLocation instanceof Location)) {
            return false;
        }
        Location otherLocation = (Location) anotherLocation;
        return Objects.equals(otherLocation.getId(), id)
                && Objects.equals(otherLocation.getTimestamp(), timestamp)
                && Objects.equals(otherLocation.getLatitude(), latitude)
                && Objects.equals(otherLocation.getLongitude(), longitude)
                && Objects.equals(otherLocation.getGroup(), group)
                && Objects.equals(otherLocation.getUser(), user)
                && Objects.equals(otherLocation.getIp(), ip);
    }

}
