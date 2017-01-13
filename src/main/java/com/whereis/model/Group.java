package com.whereis.model;

import javax.persistence.*;

@Entity
@Table(name = "groups")
public class Group {
    @Id
    //TODO: заменить  id на identity
    @Deprecated
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String identity;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }
}
