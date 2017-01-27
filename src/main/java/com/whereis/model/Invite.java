package com.whereis.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "invites")
public class Invite {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private int sent_by;

    private String sent_to_email;

    //@Temporal(TemporalType.TIMESTAMP)
    private Timestamp timestamp;

    private int group_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
