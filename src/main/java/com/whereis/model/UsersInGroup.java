package com.whereis.model;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
    protected Timestamp joinedAt;

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
        return joinedAt;
    }

    public void setJoinedAt(Timestamp joined) {
        this.joinedAt = joined;
    }

    @Override
    public boolean equals(Object usersInGroupObj) {
        if (usersInGroupObj == this) {
            return true;
        }
        if (!(usersInGroupObj instanceof UsersInGroup)) {
            return  false;
        }
        UsersInGroup usersInGroup = (UsersInGroup) usersInGroupObj;
        return Objects.equals(usersInGroup.getId(), id) &&
                Objects.equals(usersInGroup.getGroup(), group) &&
                Objects.equals(usersInGroup.getJoinedAt(), joinedAt) &&
                Objects.equals(usersInGroup.getUser(), user);
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
