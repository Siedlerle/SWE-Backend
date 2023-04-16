package com.eventmaster.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * This class serves as an entity to save an eventseries in the database. An eventseries consists of many same events at different times.
 *
 * @author Fabian Unger
 */
@Entity
public class EventSeries {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @JsonIgnore
    @OneToMany(mappedBy = "eventSeries", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Event> events = new HashSet<>();

    private int daysBetweenEvents;

    private Date lastEventDate;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    public int getDaysBetweenEvents() {
        return daysBetweenEvents;
    }

    public void setDaysBetweenEvents(int daysBetweenEvents) {
        this.daysBetweenEvents = daysBetweenEvents;
    }

    public Date getLastEventDate() {
        return lastEventDate;
    }

    public void setLastEventDate(Date lastEventDate) {
        this.lastEventDate = lastEventDate;
    }
}
