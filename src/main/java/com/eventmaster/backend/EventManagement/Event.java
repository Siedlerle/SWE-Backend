package com.eventmaster.backend.EventManagement;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.sql.Time;
import java.util.Date;

/**
 * This class serves as an entity to save events with their attributes in the database.
 * The entity has a 1:N relationship to the entity EventUser.
 *
 * @author Fabian Eilber
 */

public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private String type;
    private String description;
    private String image; //Platzhalter für Image
    private String place;
    private Date startDate;
    private Time starTime;
    private Date endDate;
    private Date endTime;
}
