package com.whereis.model;

import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Immutable
@Table(name = "invites")
public class Invite {
    @Id
    @GeneratedValue
    protected int id;

    @ManyToOne
    @JoinColumn(name = "sentByUser")
    protected User sentByUser;

    @ManyToOne
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

    public Invite() {
    }

    public Invite(User sentToUser, Group group, User sentByUser) {
        this.sentToUser = sentToUser;
        this.group = group;
        this.sentByUser = sentByUser;
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
                Objects.equals(otherInvite.getGroup().getName(), group.getName()) &&
                Objects.equals(otherInvite.getSentByUser().getFirstName() + otherInvite.getSentByUser().getLastName() +
                                otherInvite.getSentByUser().getId(),
                        sentByUser.getFirstName() + sentByUser.getLastName() + sentByUser.getId()) &&
                Objects.equals(otherInvite.getSentToUser().getFirstName() + otherInvite.getSentToUser().getLastName() +
                                otherInvite.getSentToUser().getId(),
                        sentToUser.getFirstName() + sentToUser.getLastName() + sentToUser.getId());
    }

    // TODO: возможно ли использовать id вместо хеша?
    @Override
    public int hashCode() {
        return Objects.hash(id, group.getName(), sentByUser.getFirstName() + sentByUser.getLastName() +
                sentByUser.getId(), sentToUser.getFirstName() + sentToUser.getLastName() + sentToUser.getId());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(" id: ");
        builder.append(id);
        builder.append(" group: ");
        builder.append(group);
        builder.append(" sentByUser: ");
        builder.append(sentByUser);
        builder.append(" sentToUser: ");
        builder.append(sentToUser);
        builder.append(" timestamp: ");
        builder.append(timestamp);
        return builder.toString();
    }
}
