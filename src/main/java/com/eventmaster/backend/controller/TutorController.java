package com.eventmaster.backend.controller;

import com.eventmaster.backend.entities.User;
import com.eventmaster.backend.serviceswithouttoken.UserInEventWithRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * A class which handles the HTTP-requests for tutor functions.
 *
 * @author Fabian Unger
 */

@RestController
@CrossOrigin
@RequestMapping("/tutor")
public class TutorController {

    @Autowired
    UserInEventWithRoleService userInEventWithRoleService;


    /**
     * Endpoint to get all attendees of an event.
     * @param eventId ID of the event.
     * @return List of users who attend at the event.
     */
    @PostMapping("/event/{eventId}/attendees/get-all")
    public ResponseEntity<List<User>> getAttendeesForEvent(@PathVariable long eventId) {
        return ResponseEntity.ok(userInEventWithRoleService.getAttendeesForEvent(eventId));
    }


}
