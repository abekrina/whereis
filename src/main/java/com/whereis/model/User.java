package com.whereis.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String first_name;

    private String last_name;

    private String email;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return first_name;
    }

    public void setFirstName(String first_name) {
        this.first_name = first_name;
    }

    public String getLastName() {
        return last_name;
    }

    public void setLastName(String last_name) {
        this.last_name = last_name;
    }

    @Override
    public boolean equals(Object user) {
        if (user == this) return true;
        if (!(user instanceof User)) {
            return false;
        }
        User otherUser = (User) user;
        return otherUser.getId() == id
                && Objects.equals(otherUser.getEmail(), email)
                && Objects.equals(otherUser.getFirstName(), first_name)
                && Objects.equals(otherUser.getLastName(), last_name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, first_name, last_name);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(" id: ");
        builder.append(id);
        builder.append(" email: ");
        builder.append(email);
        builder.append(" first name: ");
        builder.append(first_name);
        builder.append(" last name: ");
        builder.append(last_name);
        return builder.toString();
    }
}
