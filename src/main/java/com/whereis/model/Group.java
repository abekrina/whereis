package com.whereis.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "groups")
public class Group {
    @Id
    //TODO: заменить  id на identity
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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(" id: ");
        builder.append(id);
        builder.append(" name: ");
        builder.append(name);
        builder.append(" identity: ");
        builder.append(identity);
        return builder.toString();
    }

    /*
    *   Checks equality of database entries
    */
    @Override
    public boolean equals(Object anotherGroup) {
        if (anotherGroup == this) return true;
        if (!(anotherGroup instanceof Group)) {
            return false;
        }
        Group otherGroup = (Group) anotherGroup;
        return otherGroup.getId() == id
                && Objects.equals(otherGroup.getIdentity(),identity)
                && Objects.equals(otherGroup.getName(), name);
    }
}
