package com.eventmaster.backend.EventManagement;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/events")
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
    @GetMapping("/get-all")
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
    @GetMapping("/get-single/{eventId}")
    public ResponseEntity<Event> getEvent(@PathVariable Long eventId) {
        Event event = eventService.getEventById(eventId);
        return ResponseEntity.ok(event);
    }
}
