package com.whereis.model;

import com.sun.istack.internal.NotNull;
import com.whereis.exceptions.groups.NoUserInGroup;
import com.whereis.exceptions.groups.UserAlreadyInGroup;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.OrderBy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@FilterDef(name = "lastLocation")
@Table(name = "users")
public class User implements Serializable {
    @Id
    @GeneratedValue
    protected int id;

    @NotNull
    @Column(nullable = false)
    protected String first_name;

    @NotNull
    @Column(nullable = false)
    protected String last_name;

    @NotNull
    @Column(nullable = false)
    protected String email;


    //TODO: change to Set
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, targetEntity = Location.class, cascade = CascadeType.ALL)
    @OrderBy(clause = "timestamp")
    protected List<Location> locations = new ArrayList<>();

    @OneToMany(mappedBy = "user", targetEntity = UsersInGroup.class)
    protected Set<UsersInGroup> groups = new HashSet<>();

    public Set<Group> getGroups() {
        Set<Group> groupsToReturn = new HashSet<>();
        for (UsersInGroup group : groups) {
            groupsToReturn.add(group.getGroup());
        }
        return Collections.unmodifiableSet(groupsToReturn);
    }

    public void addUserToGroup(Group group) throws UserAlreadyInGroup {
        if (!groups.add(new UsersInGroup(this, group))) {
            throw new UserAlreadyInGroup("User " + this + "already joined group " + group);
        }
    }

    public void leave(Group group) throws NoUserInGroup {
        if (!groups.remove(group)) {
            throw new NoUserInGroup("There is no user " + this + " in group " + group);
        }
    }

    public void saveUserLocation(Location location) {
        locations.add(location);
    }

    public int getId() {
        return id;
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

    public List<Location> getLocations() {
        return Collections.unmodifiableList(locations);
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
