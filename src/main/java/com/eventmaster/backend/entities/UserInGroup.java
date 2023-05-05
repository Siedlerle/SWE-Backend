package com.eventmaster.backend.entities;

import jakarta.persistence.*;

/**
 * This class serves as an entity to connect the entities Group and User in the database.
 *
 * @author Fabian Unger
 */
@Entity
public class UserInGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(cascade=CascadeType.PERSIST)
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private User user;

    @ManyToOne(cascade=CascadeType.PERSIST)
    @JoinColumn(name = "groupId", referencedColumnName = "id")
    private Group group;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
