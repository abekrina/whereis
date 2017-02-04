package com.whereis.model;

import com.sun.istack.internal.NotNull;
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
    protected String sent_to_email;

    protected Timestamp timestamp;

    @NotNull
    @Column(nullable = false)
    protected int group_id;

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

    public int getGroupId() {
        return group_id;
    }

    public void setGroupId(int group_id) {
        this.group_id = group_id;
    }
}
