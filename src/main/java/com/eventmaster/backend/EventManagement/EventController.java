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
     * Gets a single id of an event which will be searched in the database and returned.
     *
     * @param eventId ID of the event which will be searched.
     * @return HTTP response with a body of type Event
     */
    @PostMapping("/get-single/{eventId}")
    public ResponseEntity<Event> getEvent(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventService.getEventById(eventId));
    }

    /**
     * Gets an Event and adds it to the database
     *
     * @param event Event which will be added to the database
     * @return HTTP response with a body including a boolean as status for succes
     */
    @PostMapping("/add")
    public ResponseEntity<Boolean> addEvent(@RequestBody Event event) {
        return ResponseEntity.ok(eventService.createEvent(event));
    }

    /**
     * Gets an id of an event and this will be deleted in the database.
     *
     * @param eventId ID of the event which will be deleted in the database.
     * @return HTTP response with a body including a boolean as status for succes
     */
    @PostMapping("/delete")
    public ResponseEntity<Boolean> deleteEvent(@PathVariable Long eventId){
        return ResponseEntity.ok(eventService.deleteEvent(eventId));
    }

    /**
     * Gets an id of an event to update it.
     *
     * @param event Event Object with all the changes
     * @return HTTP response with a body including a boolean as status for succes
     */
    @PostMapping("/update")
    public ResponseEntity<Boolean> updateEvent(@RequestBody Event event){
        return ResponseEntity.ok(eventService.createEvent(event));
    }

    /**
     * Gets an id of an event to search the database for participants.
     *
     * @param eventId ID of the event which will be searched in the database.
     * @return HTTP response with a body including a List of Users.
     */
    @PostMapping("/get-participants/{eventId}")
    public ResponseEntity<List<User>> getParticipants(@PathVariable Long eventId){
        return ResponseEntity.ok(eventService.getParticipants(eventId));
    }
}
