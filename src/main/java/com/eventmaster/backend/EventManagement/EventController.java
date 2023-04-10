package com.eventmaster.backend.EventManagement;


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
     */
    @PostMapping("/delete")
    public ResponseEntity<Boolean> deleteEvent(@PathVariable Long eventId){
        boolean eventDeleted = eventService.deleteEvent(eventId);
        return ResponseEntity.ok(eventDeleted);
    }


}
