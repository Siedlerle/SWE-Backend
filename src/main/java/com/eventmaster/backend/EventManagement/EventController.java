package com.eventmaster.backend.EventManagement;


import com.eventmaster.backend.UserManagement.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * A class which handles the HTTP-requests for events.
 *
 * @author Fabian Eilber
 */

@RestController
@CrossOrigin
@RequestMapping("/event")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    /**
     * All events in the Database will be returned
     *
     * @return HTTP response with a body of type Event
     */
    @PostMapping("/get-all")
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    /**
     * Gets a single id of an event which will be searched in the database and returned.
     *
     * @param eventId ID of the event which will be searched.
     * @return HTTP response with a body of type Event
     */
    @PostMapping("/get-single/{eventId}")
    public ResponseEntity<Event> getEvent(@PathVariable Long eventId) {
        Event event = eventService.getEventById(eventId);
        return ResponseEntity.ok(event);
    }

    /**
     * Gets an Event and adds it to the database
     *
     * @param event Event which will be added to the database
     * @return HTTP response with a body including a boolean as status for succes
     */
    @PostMapping("/add")
    public ResponseEntity<Boolean> addEvent(@RequestBody Event event) {
        boolean eventAdded = eventService.createEvent(event);
        return ResponseEntity.ok(eventAdded);
    }

    /**
     * Gets an id of an event and this will be deleted in the database.
     *
     * @param eventId ID of the event which will be deleted in the database.
     * @return HTTP response with a body including a boolean as status for succes
     */
    @PostMapping("/delete")
    public ResponseEntity<Boolean> deleteEvent(@PathVariable Long eventId){
        boolean eventDeleted = eventService.deleteEvent(eventId);
        return ResponseEntity.ok(eventDeleted);
    }

    /**
     * Gets an id of an event to update it.
     *
     * @param eventId ID of the event which will be searched in the database to get updated.
     * @param event Event Object with all the changes
     * @return HTTP response with a body including a boolean as status for succes
     */
    @PostMapping("/get-participants/{eventId}")
    public ResponseEntity<Boolean> getParticipants(@PathVariable Long eventId, @RequestBody Event event){
        boolean eventUpdated = eventService.updateEvent(eventId, event);
        return ResponseEntity.ok(eventUpdated);
    }

    /**
     * Gets an id of an event to search the database for participants.
     *
     * @param eventId ID of the event which will be searched in the database.
     * @return HTTP response with a body including a List of Users.
     */
    @PostMapping("/get-participants/{eventId}")
    public ResponseEntity<List<User>> getParticipants(@PathVariable Long eventId){
        List<User> participants = eventService.getParticipants(eventId);
        return ResponseEntity.ok(participants);
    }


}
