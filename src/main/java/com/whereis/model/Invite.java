package com.whereis.model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Type;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Immutable
@Table(name = "invites")
public class Invite {
    @Id
    @GeneratedValue
    protected int id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sentByUser")
    protected User sentByUser;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sentToUser")
    protected User sentToUser;

    @Column(name = "timestamp")
    @Type(type = "java.sql.Timestamp")
    @Temporal(value = TemporalType.TIMESTAMP)
    @CreationTimestamp
    protected Timestamp timestamp;

    @ManyToOne
    @JoinColumn(name = "groupId")
    protected Group group;

    public Invite() {}

    public Invite(User sentToUser, Group group) {
        this.sentToUser = sentToUser;
        this.group = group;
    }

    public int getId() {
        return id;
    }

    public User getSentByUser() {
        return sentByUser;
    }

    public void setSentByUser(User sentByUser) {
        this.sentByUser = sentByUser;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public User getSentToUser() {
        return sentToUser;
    }

    public void setSentToUser(User sentToUser) {
        this.sentToUser = sentToUser;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof Invite)) {
            return false;
        }
        Invite otherInvite = (Invite) object;
        return Objects.equals(otherInvite.getId(), id) &&
                Objects.equals(otherInvite.getGroup(), group) &&
                Objects.equals(otherInvite.getSentByUser(), sentByUser) &&
                Objects.equals(otherInvite.getSentToUser(), sentToUser) &&
                Objects.equals(otherInvite.getTimestamp(), timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, group, sentByUser, sentToUser, timestamp);
    }
}
