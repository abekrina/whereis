package com.whereis.model;

import com.sun.istack.internal.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Immutable
@Table(name = "invites")
public class Invite {
    @Id
    @GeneratedValue
    protected int id;

    @ManyToOne
    @JoinColumn(name = "sent_by_user")
    protected User sent_by_user;

    @ManyToOne
    @JoinColumn(name = "sent_to_user")
    protected User sent_to_user;

    @CreationTimestamp
    protected Timestamp timestamp;

    @ManyToOne
    @JoinColumn(name = "group_id")
    protected Group group;

    public int getId() {
        return id;
    }

    public User getSentByUser() {
        return sent_by_user;
    }

    public void setSentByUser(User sent_by_user) {
        this.sent_by_user = sent_by_user;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public User getSentToUser() {
        return sent_to_user;
    }

    public void setSentToUser(User sent_to_user) {
        this.sent_to_user = sent_to_user;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
