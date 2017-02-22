package com.whereis.model;



import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
@Table(name = "groups")
public class Group {
    @Id
    @GeneratedValue
    protected int id;

    @NotNull
    @Column(nullable = false)
    protected String name;

    @NotNull
    @Column(nullable = false)
    protected String identity;

    @OneToMany(mappedBy = "group", fetch = FetchType.EAGER, targetEntity = UsersInGroup.class)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    protected Set<UsersInGroup> users = new HashSet<>();

    @OneToMany(mappedBy = "group", fetch = FetchType.EAGER, targetEntity = Location.class)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    protected Set<Location> locations = new HashSet<>();

    @OneToMany(mappedBy = "group", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    protected Set<Invite> invites = new HashSet<>();

    public int getId() {
        return id;
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
    @JsonIgnore
    public Set<User> getUsersInGroup() {
        Set<User> usersToReturn = new HashSet<>();
        for (UsersInGroup user : users) {
            usersToReturn.add(user.getUser());
        }
        return Collections.unmodifiableSet(usersToReturn);
    }

    public void addUserToGroup(UsersInGroup usersInGroup) {
        users.add(usersInGroup);
    }

    public Set<Invite> getInvites() {
        return Collections.unmodifiableSet(invites);
    }

    public boolean addInviteToGroup(Invite invite) {return invites.add(invite);}

    public boolean addLocationOfUser(Location location) {
        return locations.add(location);
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

    @Override
    public int hashCode() {
        return Objects.hash(id, identity, name);
    }
}
