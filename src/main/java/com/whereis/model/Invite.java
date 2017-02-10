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

    protected int sent_by;

    @NotNull
    @Column(nullable = false)
    //TODO: map by hibernate
    protected String sent_to_email;

    @CreationTimestamp
    protected Timestamp timestamp;

    @ManyToOne
    @JoinColumn(name = "group_id")
    protected Group group;

    public int getId() {
        return id;
    }

    public int getSentBy() {
        return sent_by;
    }

    public void setSentBy(int sent_by) {
        this.sent_by = sent_by;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getSentToEmail() {
        return sent_to_email;
    }

    public void setSentToEmail(String sent_to_email) {
        this.sent_to_email = sent_to_email;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
