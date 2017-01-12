package com.whereis.model;

import javax.persistence.*;
import java.sql.Time;
import java.sql.Timestamp;

@Entity
//TODO:change to users_in_group
@Table(name = "usersInGroups")
public class UsersInGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int user_id;

    private int group_id;

    private Timestamp joined_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return user_id;
    }

    public void setUserId(int user_id) {
        this.user_id = user_id;
    }

    public int getGroupId() {
        return group_id;
    }

    public void setGroupId(int groupId) {
        this.group_id = groupId;
    }

    public Timestamp getJoinedAt() {
        return joined_at;
    }

    public void setJoinedAt(Timestamp joined) {
        this.joined_at = joined;
    }
}
