package com.eventmaster.backend.controller;

import com.eventmaster.backend.entities.Event;
import com.eventmaster.backend.entities.EventRole;
import com.eventmaster.backend.entities.User;
import com.eventmaster.backend.entities.UserInEventWithRole;
import com.eventmaster.backend.services.OrganisationService;
import com.eventmaster.backend.services.UserInEventWithRoleService;
import com.eventmaster.backend.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * A class which handles the HTTP-requests for user functions.
 *
 * @author Fabian Eilber
 */

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    //Services needed for operations
    private UserService userService;
    private OrganisationService organisationService;

    private UserInEventWithRoleService userInEventWithRoleService;



/*
    //Operations regarding user authentications
    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody User user){
        return ResponseEntity.ok(userService.register(user));
    }
    @PostMapping("/auth/verify")
    public ResponseEntity<?> verify(String authToken) {
        return
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login (User user){
        return
    }

    @PostMapping("/auth/pwd-reset-request")
    public ResponseEntity<?> requestPasswordReset(String emailAdress){
        return
    }

    @PostMapping("/auth/reset-pwd")
    public ResponseEntity<?> resetPassword(User user, String authToken){
        return
    }

    @PostMapping("/auth/delete")
    public ResponseEntity<?> delete(User user, String authToken){
        return
    }


    //Operations regarding user, orga connection
    @PostMapping("/orga/get-all")
    public ResponseEntity<List<Organisation>> getAllOrganisations(String authToken){
        return
    }

    @PostMapping("/orga/get-for-user")
    public ResponseEntity<List<Organisation>> getOrganisationsForUser(String authToken){
        return
    }

    @PostMapping("/orga/get-orga")
    public ResponseEntity<Organisation> getOrganisation(long organisationId, String authToken){
        return
    }


    @PostMapping("/orga/request-join")
    public ResponseEntity<String> requestJoin(long organisationId, String authToken){
        return
    }

    @PostMapping("/orga/accept-invitation")
    public ResponseEntity<String> acceptOrganisationInvitation(long organisationId, String authToken){
        return
    }

    @PostMapping("/orga/decline-invitation")
    public ResponseEntity<String> declineOrganisationInvitation(long organisationId, String autToken){
        return
    }

    @PostMapping("/orga/leave")
    public ResponseEntity<String> leaveOrganisation(long organisationId, String authToken, String reason){
        return
    }




    //Operations regarding user, event, orga connection
    @PostMapping("/orga/event/get-all-events-of-orga")
    public ResponseEntity<List<Event>> getAllVisibleEventsOfOrganisationForUser(long organisationId, String authToken){
        return
    }

    @PostMapping("/orga/event/get-registerd")
    public ResponseEntity<String> getRegisteredEventsForUserInOrganisation(long organisationId, String authToken){
        return
    }



*/
    //Operations regarding user, event connection
    @PostMapping("/event/accept-invitation")
    public ResponseEntity<String> acceptEventInvitation(long eventId, String authToken){
        return ResponseEntity.ok(userInEventWithRoleService.acceptEventInvitation(eventId, authToken));
    }

    @PostMapping("/event/decline-invitation")
    public ResponseEntity<String> declineEventInvitation(long eventId, String authToken){
        return ResponseEntity.ok(userInEventWithRoleService.declineEventInvitation(eventId, authToken));
    }

    @PostMapping("/event/get-role")
    public ResponseEntity<EventRole> getRoleForEvent(long eventId, String authToken){
        return ResponseEntity.ok(userInEventWithRoleService.getRoleForEvent(eventId, authToken));
    }

    @PostMapping("/event/register")
    public ResponseEntity<String> registerForEvent(long eventId, String authToken){
        return ResponseEntity.ok(userInEventWithRoleService.registerForEvent(eventId, authToken));
    }

    @PostMapping("/event/get-registered")
    public ResponseEntity<List<Event>> getRegisteredEventsForUser(String authToken){
        return ResponseEntity.ok(userInEventWithRoleService.getRegisteredEventsForUser(authToken));
    }

    @PostMapping("/event/unregister")
    public ResponseEntity<String> ungregisterFromEvent(Event event, String authToken, String reason){
        return ResponseEntity.ok(userInEventWithRoleService.unregisterFromEvent(event, reason, authToken));
    }

    @PostMapping("/event/get-all")
    public ResponseEntity<List<Event>> getAllEventsForUser(String authToken){
        return ResponseEntity.ok(userInEventWithRoleService.getAllEventsForUser(authToken));
    }

}
