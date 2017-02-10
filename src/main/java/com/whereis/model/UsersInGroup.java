package com.whereis.model;

import com.sun.istack.internal.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Objects;

@Immutable
@Entity
@Table(name = "usersInGroups")
public class UsersInGroup {
    @Id
    @GeneratedValue
    protected int id;

    @NotNull
    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    protected User user;

    @NotNull
    @ManyToOne(targetEntity = Group.class)
    @JoinColumn(name = "groupId", insertable = false, updatable = false)
    protected Group group;

    @CreationTimestamp
    protected Timestamp joined_at;

    public UsersInGroup() {

    }
    public UsersInGroup(User user, Group group) {
        this.user = user;
        this.group = group;
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

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Timestamp getJoinedAt() {
        return joined_at;
    }

    public void setJoinedAt(Timestamp joined) {
        this.joined_at = joined;
    }

    @Override
    public boolean equals(Object usersInGroupObj) {
        UsersInGroup usersInGroup = (UsersInGroup) usersInGroupObj;
        if (usersInGroup.getId() == id && usersInGroup.getUser() == user
                && usersInGroup.getGroup() == group) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, group);
    }

    @Override
    public String toString() {
        return "user: " + user + " group: " + group;
    }
}
