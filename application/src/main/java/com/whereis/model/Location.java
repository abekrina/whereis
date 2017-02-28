package com.whereis.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Objects;

@Immutable
@Entity
@Table(name = "locations")
public class Location {
    @Id
    @GeneratedValue
    protected int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    @JsonBackReference
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
        if (ip != null && ip != "") {
            this.ip = ip;
        }
        this.group = group;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    @JsonIgnore
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
        return ((Location) anotherLocation).getId() == id
                && ((Location) anotherLocation).getTimestamp().equals(timestamp)
                && ((Location) anotherLocation).getLatitude() == latitude
                && ((Location) anotherLocation).getLongitude() == longitude
                && ((Location) anotherLocation).getGroup().equals(group)
                && ((Location) anotherLocation).getUser().equals(user)
                && ((Location) anotherLocation).getIp().equals(ip);
    }

    // TODO: change to use user and group property or change to id if it is ok
    @Override
    public int hashCode() {
        return Objects.hash(id, timestamp, latitude, longitude, group, user, ip);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(" id: ");
        builder.append(id);
        builder.append(" timestamp: ");
        builder.append(timestamp);
        builder.append(" latitude: ");
        builder.append(latitude);
        builder.append(" longitude: ");
        builder.append(longitude);
        builder.append(" group: ");
        builder.append(group);
        builder.append(" user: ");
        builder.append(user);
        builder.append(" ip: ");
        builder.append(ip);
        return builder.toString();
    }

}
