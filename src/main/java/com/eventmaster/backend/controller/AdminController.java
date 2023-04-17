package com.eventmaster.backend.controller;

import com.eventmaster.backend.entities.Event;
import com.eventmaster.backend.serviceswithouttoken.EventService;
import com.eventmaster.backend.serviceswithouttoken.GroupService;
import com.eventmaster.backend.serviceswithouttoken.UserInEventWithRoleService;
import com.eventmaster.backend.serviceswithouttoken.UserInOrgaWithRoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * A class which handles the HTTP-requests for admin functions.
 *
 * @author Fabian Eilber
 */

@RestController
@CrossOrigin
@RequestMapping("/admin")
public class AdminController {

    private  EventService eventService;
    private  UserInOrgaWithRoleService userInOrgaWithRoleService;
    private  GroupService groupService;
    private  UserInEventWithRoleService userInEventWithRoleService;

    /**
     * Endpoint for a part of the organisation to get all of its events
     * @param orgaId Id of the corresponding orga
     * @return List of events
     */
    @PostMapping("/get-events-of-orga/{orgaId}")
    public ResponseEntity<List<Event>> getEventsOfOrganisation(@PathVariable Long orgaId){
        return ResponseEntity.ok(eventService.getEventsOfOrganisation(orgaId));
    }

}
