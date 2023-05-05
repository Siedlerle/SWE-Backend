package com.eventmaster.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

/**
 * This class serves as an entity to save the connection from a user, an event and a role in the database.
 *
 * @author Fabian Eilber
 * @author Lars Holweger
 */
@Entity
public class UserInEventWithRole {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(cascade=CascadeType.PERSIST)
    @JoinColumn(name = "userId",referencedColumnName = "id")
    private User user;

    @ManyToOne(cascade=CascadeType.PERSIST)
    @JoinColumn(name = "eventId",referencedColumnName = "id")
    private Event event;

    @ManyToOne(cascade=CascadeType.PERSIST)
    @JoinColumn(name = "roleId", referencedColumnName = "id")
    private EventRole eventRole;
    //---------------------------------------------------------------------------

    private boolean hasAttended;

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

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }


    public EventRole getEventRole() {
        return eventRole;
    }

    public void setEventRole(EventRole eventRole) {
        this.eventRole = eventRole;
    }

    public boolean isHasAttended() {
        return hasAttended;
    }

    public void setHasAttended(boolean hasAttended) {
        this.hasAttended = hasAttended;
    }
}
