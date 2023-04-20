package com.eventmaster.backend.controller;

//Todo import explizit definieren
import com.eventmaster.backend.entities.*;
import com.eventmaster.backend.serviceswithouttoken.UserService;
import com.eventmaster.backend.serviceswithouttoken.OrganisationService;
import com.eventmaster.backend.serviceswithouttoken.UserInEventWithRoleService;
import com.eventmaster.backend.serviceswithouttoken.UserInOrgaWithRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//Todo Version der ersten Erstellung und der letzten Änderung hinzufügen
/**
 * A class which handles the HTTP-requests for user functions.
 *
 * @author Fabian Eilber
 * @version 1.0
 * @since 1.0
 */

//Todo CORS und CrossOrigin anhand von Artikel überdenken
@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    //Services needed for operations
    @Autowired
    private UserService userService;
    @Autowired
    private OrganisationService organisationService;
    @Autowired
    private UserInEventWithRoleService userInEventWithRoleService;
    @Autowired
    private UserInOrgaWithRoleService userInOrgaWithRoleService;


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
*/

    //Operations regarding user, orga connection

    /**
     * Endpoint for a user to get all existing organisations
     * @return List of Organisations
     */
    @PostMapping("/orga/get-all")
    public ResponseEntity<List<Organisation>> getAllOrganisations(){
        return ResponseEntity.ok(organisationService.getAllOrganisations());
    }

    /**
     * Endpoint for a user to get Information about Organisation
     * @param organisationId Id of the corresponding Organisation
     * @return Organisation object
     */
    @PostMapping("/orga/get-orga")
    public ResponseEntity<Organisation> getOrganisation(long organisationId){
        return ResponseEntity.ok(organisationService.getOrganisationById(organisationId));
    }

    /**
     * Endpoint for a user to get all organisations a User is part of
     * @param authToken Token to retrieve the id of the user requesting
     * @return List of organisations the user is part of
     */
    @PostMapping("/orga/get-for-user")
    public ResponseEntity<List<Organisation>> getOrganisationsForUser(String authToken){
        long userId = 0;
        return ResponseEntity.ok(userInOrgaWithRoleService.getOrgaForUser(userId));
    }

    /**
     * Endpoint for a user to request to join a organisation
     * @param organisationId Id of the corresponding Organisation
     * @param authToken Token to retrieve the id of the user requesting
     * @return successmessage
     */
    @PostMapping("/orga/request-join")
    public ResponseEntity<String> requestJoin(long organisationId, String authToken){
        long userId = 0;
        return ResponseEntity.ok(userInOrgaWithRoleService.requestJoin(organisationId, authToken));
    }

    /**
     * Endpoint for a user to accept an invitation to an orga
     * @param organisationId Id of the corresponding Organisation
     * @param authToken Token to retrieve the id of the user requesting
     * @return successmessage
     */
    @PostMapping("/orga/accept-invitation")
    public ResponseEntity<String> acceptOrganisationInvitation(long organisationId, String authToken){
        long userId = 0;
        return ResponseEntity.ok("nicht implementiert");
    }

    /**
     * Endpoint for a user to decline an invitation to an orga
     * @param organisationId Id of the corresponding Organisation
     * @param authToken Token to retrieve the id of the user requesting
     * @return successmessage
     */
    @PostMapping("/orga/decline-invitation")
    public ResponseEntity<String> declineOrganisationInvitation(long organisationId, String authToken){
        long userId = 0;
        return ResponseEntity.ok("nicht implementiert");
    }

    /**
     * Endpoint for a user to leave an organisation
     * @param organisationId Id of the corresponding Organisation
     * @param authToken Token to retrieve the id of the user requesting
     * @param reason Reason why the user leaves the organisation
     * @return successmessage
     */
    @PostMapping("/orga/leave")
    public ResponseEntity<String> leaveOrganisation(long organisationId, String authToken, String reason){
        long userId = 0;
        return ResponseEntity.ok(userInOrgaWithRoleService.leaveOrganisation(organisationId, authToken, reason));
    }

    //Todo Controller aufteilen, falls diese zu groß werden
/*
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


    //TODO für alle Endpoints muss noch die userId aus den Tokensretrieved werden
    //Operations regarding user, event connection

    /**
     * Endpoint for a user to accept an invitation to an event
     * @param eventId Id of the event user is about to accept
     * @param authToken Token from user to get further information
     * @return success message
     */
    @PostMapping("/event/accept-invitation")
    public ResponseEntity<String> acceptEventInvitation(long eventId, String authToken){
        //Todo Validierung der Inputs
        long userId = 0;
        return ResponseEntity.ok(userInEventWithRoleService.acceptEventInvitation(eventId, userId));
    }

    /**
     * Endpoint for a user to decline an invitation to an event
     * @param eventId Id of the event user is about to decline
     * @param authToken Token from user to get further information
     * @return success message
     */
    @PostMapping("/event/decline-invitation")
    public ResponseEntity<String> declineEventInvitation(long eventId, String authToken){
        long userId = 0;
        return ResponseEntity.ok(userInEventWithRoleService.declineEventInvitation(eventId, userId));
    }

    /**
     * Endpont for an user to get his role for a specific event
     * @param eventId Id of the event a user wants to get his role for
     * @param authToken Token from user to get further information
     * @return role of the user for the corresponding event
     */
    @PostMapping("/event/get-role")
    public ResponseEntity<EventRole> getRoleForEvent(long eventId, String authToken){
        long userId = 0;
        return ResponseEntity.ok(userInEventWithRoleService.getRoleForEvent(eventId, userId));
    }

    /**
     * Endpoint for a user to register for an event
     * @param eventId Id of the event a user is about to register for
     * @param authToken Token from user to get further information
     * @return success message
     */
    @PostMapping("/event/register")
    public ResponseEntity<String> registerForEvent(long eventId, String authToken){
        long userId = 0;
        return ResponseEntity.ok(userInEventWithRoleService.registerForEvent(eventId, userId));
    }

    /**
     * Endpoint to get all events a user is registered for
     * @param authToken Token from user to get further information
     * @return List of events a user is registered for
     */
    @PostMapping("/event/get-registered")
    public ResponseEntity<List<Event>> getRegisteredEventsForUser(String authToken){
        long userId = 0;
        return ResponseEntity.ok(userInEventWithRoleService.getRegisteredEventsForUser(userId));
    }

    /**
     * Endpoint to unregister a user from an event
     * @param event Corresponding event for unregistration
     * @param authToken Token from user to get further information
     * @param reason Reason why the user is unregistering
     * @return success message
     */
    @PostMapping("/event/unregister")
    public ResponseEntity<String> ungregisterFromEvent(Event event, String authToken, String reason){
        long userId = 0;
        return ResponseEntity.ok(userInEventWithRoleService.unregisterFromEvent(event,userId, reason));
    }

    /*
    @PostMapping("/event/get-all")
    public ResponseEntity<List<Event>> getAllEventsForUser(String authToken){
        return ResponseEntity.ok(userInEventWithRoleService.getAllEventsForUser(authToken));
    }*/

}
