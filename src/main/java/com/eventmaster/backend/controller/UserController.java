package com.eventmaster.backend.controller;

import com.eventmaster.backend.entities.*;
import com.eventmaster.backend.serviceswithouttoken.OrganisationService;
import com.eventmaster.backend.serviceswithouttoken.UserInEventWithRoleService;
import com.eventmaster.backend.serviceswithouttoken.UserService;
import com.eventmaster.backend.serviceswithouttoken.UserInOrgaWithRoleService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * A class which handles the HTTP-requests for user functions.
 *
 * @author Fabian Eilber
 */

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    //Services needed for operations
    private final UserService userService;
    private final OrganisationService organisationService;
    private final UserInEventWithRoleService userInEventWithRoleService;
    private final UserInOrgaWithRoleService userInOrgaWithRoleService;



    //Operations regarding user authentications

    /**
     * Endpoint to register a user
     * @param user User object with userdata
     * @return AuthenticationResponse with tokens
     */
    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody User user){
        return ResponseEntity.ok(userService.register(user));
    }

    /**
     * Endpoint to verify a user after registering
     * @param authToken jwt token which is valid for a certain time
     * @return successmessage
     */
    @PostMapping("/auth/verify")
    public ResponseEntity<?> verify(@RequestParam String authToken) {
        return ResponseEntity.ok(userService.verify(authToken));
    }

    /**
     * Endpoint to login as a user
     * @param user Userobject with neccesary information
     * @return AuthenticationResponse with tokens
     */
    @PostMapping("/auth/login")
    public ResponseEntity<?> login (@RequestBody User user){
        return ResponseEntity.ok(userService.login(user));
    }


    /**
     * Endpoint to request to reset a users password
     * @param emailAdress Mail of the corresponding user
     * @return successmessage
     */
    @PostMapping("/auth/pwd-reset-request")
    public ResponseEntity<?> requestPasswordReset(@RequestParam String emailAdress){
        return ResponseEntity.ok(userService.requestPasswordReset(emailAdress));
    }

    /**
     * Endpoint to change password
     * @param user Userobject with new password
     * @return Authenticationresponse with tokens
     */
    @PostMapping("/auth/reset-pwd")
    public ResponseEntity<?> resetPassword(@RequestBody User user){
        return ResponseEntity.ok(userService.resetPassword(user));
    }

    /**
     * Endpoint to delete a user
     * @param userId Id of the corresponding user
     * @return success message
     */
    @PostMapping("/auth/delete")
    public ResponseEntity<?> delete(@RequestParam long userId){
        return ResponseEntity.ok(userService.deleteUser(userId));
    }

    /**
     * Endpoint to refresh jwt tokens and with that also the session
     * @param request httpservlert request
     * @param response httpservlet response
     * @throws IOException
     */
    @PostMapping("auth/refresh")
    public void refresh(HttpServletRequest request, HttpServletResponse response) throws IOException {
        userService.refreshToken(request, response);
    }


    //Operations regarding user, orga connection
    @PostMapping("/orga/get-all")
    public ResponseEntity<List<Organisation>> getAllOrganisations(){
        return ResponseEntity.ok(organisationService.getAllOrganisations());
    }

    @PostMapping("/orga/get-orga")
    public ResponseEntity<Organisation> getOrganisation(long organisationId){
        return ResponseEntity.ok(organisationService.getOrganisationById(organisationId));
    }

    @PostMapping("/orga/get-for-user")
    public ResponseEntity<List<Organisation>> getOrganisationsForUser(String authToken){
        long userId = 0;
        return ResponseEntity.ok(userInOrgaWithRoleService.getOrgaForUser(userId));
    }

    @PostMapping("/orga/request-join")
    public ResponseEntity<String> requestJoin(long organisationId, String authToken){
        long userId = 0;
        return ResponseEntity.ok("nicht implementiert");
    }

    @PostMapping("/orga/accept-invitation")
    public ResponseEntity<String> acceptOrganisationInvitation(long organisationId, String authToken){
        long userId = 0;
        return ResponseEntity.ok("nicht implementiert");
    }

    @PostMapping("/orga/decline-invitation")
    public ResponseEntity<String> declineOrganisationInvitation(long organisationId, String authToken){
        long userId = 0;
        return ResponseEntity.ok("nicht implementiert");
    }

    @PostMapping("/orga/leave")
    public ResponseEntity<String> leaveOrganisation(long organisationId, String authToken, String reason){
        long userId = 0;
        return ResponseEntity.ok("nicht implementiert");
    }


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
