package com.eventmaster.backend.controller;

//Todo import explizit definieren
import com.eventmaster.backend.entities.*;
import com.eventmaster.backend.services.UserService;
import com.eventmaster.backend.security.authentification.VerificationResponse;
import com.eventmaster.backend.services.OrganisationService;
import com.eventmaster.backend.services.UserInEventWithRoleService;
import com.eventmaster.backend.services.UserInOrgaWithRoleService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import local.variables.LocalizedStringVariables;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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

    private final UserService userService;
    private final OrganisationService organisationService;
    private final UserInEventWithRoleService userInEventWithRoleService;
    private final UserInOrgaWithRoleService userInOrgaWithRoleService;

    public UserController(UserService userService,
                          OrganisationService organisationService,
                          UserInEventWithRoleService userInEventWithRoleService,
                          UserInOrgaWithRoleService userInOrgaWithRoleService) {
        this.userService = userService;
        this.organisationService = organisationService;
        this.userInEventWithRoleService = userInEventWithRoleService;
        this.userInOrgaWithRoleService = userInOrgaWithRoleService;
    }



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
    public ResponseEntity<VerificationResponse> verify(@RequestParam String authToken) {
        return ResponseEntity.ok(userService.verify(authToken));
    }

    /**
     * Endpoint to login as a user
     * @param user Userobject with neccesary information
     * @return AuthenticationResponse with tokens
     */
    @PostMapping("/auth/login")
    public ResponseEntity<?> login (@RequestBody User user){return ResponseEntity.ok(userService.login(user));}


    /**
     * Endpoint to request to reset a users password
     * @param emailAdress Mail of the corresponding user
     * @return successmessage
     */
    @PostMapping("/auth/pwd-reset-request/{emailAdress}")
    public ResponseEntity<?> requestPasswordReset(@PathVariable String emailAdress){
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
     * @param emailAdress Email of the corresponding user
     * @return success message
     */
    @PostMapping("/auth/delete/{emailAdress}")
    public ResponseEntity<MessageResponse> delete(@PathVariable String emailAdress){
        return ResponseEntity.ok(userService.deleteUser(emailAdress));
    }

    /**
     * Endpoint to refresh jwt tokens and with that also the session
     * @param request httpservlert request
     * @param response httpservlet response
     * @throws IOException
     */
    @PostMapping("auth/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request, HttpServletResponse response) throws IOException {
        return userService.refreshToken(request, response);
    }


    //Operations regarding user information

    /**
     * Endpoint to get userinformation
     * @param emailAdress EMail of the corresponding user
     * @return Userobject
     */
    @PostMapping("/info/{emailAdress}")
    public ResponseEntity<User> getUserInformation (@PathVariable String emailAdress){
        return ResponseEntity.ok(userService.getUserByMail(emailAdress));
    }


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
    @PostMapping("/orga/get-orga/{organisationId}")
    public ResponseEntity<Organisation> getOrganisation(@PathVariable long organisationId){
        return ResponseEntity.ok(organisationService.getOrganisationById(organisationId));
    }

    /**
     * Endpoint for a user to get all organisations a User is part of
     * @param emailAdress Email to retrieve the id of the user requesting
     * @return List of organisations the user is part of
     */
    @PostMapping("/orga/get-for-user/{emailAdress}")
    public ResponseEntity<List<Organisation>> getOrganisationsForUser(@PathVariable String emailAdress){
        return ResponseEntity.ok(userInOrgaWithRoleService.getOrgasForUser(emailAdress));
    }

    /**
     * Endpoint for a user to get his Role in an organisation
     * @param organisationId Id of the corresponding organisation
     * @param emailAdress Email of the corresponding user
     * @return organisation role
     */
    @PostMapping("/orga/{organisationId}/get-role-for-user/{emailAdress}")
    public ResponseEntity<OrgaRole> getRoleInOrganisation(@PathVariable long organisationId, @PathVariable String emailAdress){
        return ResponseEntity.ok(userInOrgaWithRoleService.getRoleInOrganisation(organisationId, emailAdress));
    }

    /**
     * Endpoint for a user to request to join a organisation
     * @param organisationId Id of the corresponding Organisation
     * @param emailAdress Email to retrieve the id of the user requesting
     * @return successmessage
     */
    @PostMapping("/orga/{organisationId}/request-join/{emailAdress}")
    public ResponseEntity<MessageResponse> requestJoin(@PathVariable long organisationId,@PathVariable String emailAdress){
        return ResponseEntity.ok(userInOrgaWithRoleService.requestJoin(organisationId, emailAdress));
    }

    /**
     * Endpoint for a user to accept an invitation to an orga
     * @param organisationId Id of the corresponding Organisation
     * @param emailAdress Email to retrieve the id of the user requesting
     * @return successmessage
     */
    @PostMapping("/orga/{organisationId}/accept-invitation/{emailAdress}")
    public ResponseEntity<MessageResponse> acceptOrganisationInvitation(@PathVariable long organisationId, @PathVariable String emailAdress){
        return ResponseEntity.ok(userInOrgaWithRoleService.acceptOrganisationInvite(organisationId, emailAdress));
    }

    /**
     * Endpoint for a user to decline an invitation to an orga
     * @param organisationId Id of the corresponding Organisation
     * @param emailAdress Email to retrieve the id of the user requesting
     * @return successmessage
     */
    @PostMapping("/orga/{organisationId}/decline-invitation/{emailAdress}")
    public ResponseEntity<MessageResponse> declineOrganisationInvitation(@PathVariable long organisationId, @PathVariable String emailAdress){
        return ResponseEntity.ok(userInOrgaWithRoleService.declineOrganisationInvitation(organisationId, emailAdress));
    }

    /**
     * Endpoint for a user to leave an organisation
     * @param organisationId Id of the corresponding Organisation
     * @param emailAdress Email to retrieve the id of the user requesting
     * @return successmessage
     */
    @PostMapping("/orga/{organisationId}/leave/{emailAdress}")
    public ResponseEntity<MessageResponse> leaveOrganisation(@PathVariable long organisationId, @PathVariable String emailAdress){
        return ResponseEntity.ok(userInOrgaWithRoleService.leaveOrganisation(organisationId, emailAdress));
    }




    //Operations regarding user, event, orga connection
    /**
     * Endpoint for a user to get all events inside an organisation where he is not attending.
     * @param organisationId Id of the corresponding orga
     * @param emailAddress Mail of user who requests.
     * @return List of events
     */
    @PostMapping("/orga/{organisationId}/event/get-available-events/{emailAddress}")
    public ResponseEntity<List<Event>> getAllVisibleEventsOfOrganisationForUser(@PathVariable long organisationId,
                                                                                @PathVariable String emailAddress){
        return ResponseEntity.ok(userInOrgaWithRoleService.getAllVisibleEventsOfOrganisationForUser(organisationId, emailAddress));
    }

    /**
     * Endpoint for a user to get all events inside an organisation he is registered for
     * @param organisationId Id of the corresponding organisation
     * @param emailAdress Email of the corresponding user
     * @return List of events
     */
    @PostMapping("/orga/{organisationId}/event/get-registered/{emailAdress}")
    public ResponseEntity<List<Event>> getRegisteredEventsForUserInOrganisation(@PathVariable long organisationId, @PathVariable String emailAdress){
        return ResponseEntity.ok(userInOrgaWithRoleService.getRegisteredEventsForUserInOrganisation(organisationId, emailAdress));
    }

    /**
     * Endpoint for a user to get his invitations to organisations.
     * @param emailAddress Mail of the user.
     * @return List of organisations.
     */
    @PostMapping("/orga/get-invitations/{emailAddress}")
    public ResponseEntity<List<Organisation>> getOrganisationInvitationsForUser(@PathVariable String emailAddress) {
        return ResponseEntity.ok(userInOrgaWithRoleService.getOrganisationInvitationsForUser(emailAddress));
    }


    //Operations regarding user, event connection
    /**
     * Endpoint for a user to accept an invitation to an event
     * @param eventId Id of the event user is about to accept
     * @param emailAdress Email from user to get further information
     * @return success message
     */
    @PostMapping("/event/{eventId}/accept-invitation/{emailAdress}")
    public ResponseEntity<MessageResponse> acceptEventInvitation(@PathVariable long eventId, @PathVariable String emailAdress){
        return ResponseEntity.ok(userInEventWithRoleService.acceptEventInvitation(eventId, emailAdress));
    }

    /**
     * Endpoint for a user to decline an invitation to an event
     * @param eventId Id of the event user is about to decline
     * @param emailAdress Email from user to get further information
     * @return success message
     */
    @PostMapping("/event/{eventId}/decline-invitation/{emailAdress}")
    public ResponseEntity<MessageResponse> declineEventInvitation(@PathVariable long eventId, @PathVariable String emailAdress){
        return ResponseEntity.ok(userInEventWithRoleService.declineEventInvitation(eventId, emailAdress));
    }

    /**
     * Endpont for an user to get his role for a specific event
     * @param eventId Id of the event a user wants to get his role for
     * @param emaiAdress Email from user to get further information
     * @return role of the user for the corresponding event
     */
    @PostMapping("/event/{eventId}/get-role/{emaiAdress}")
    public ResponseEntity<EventRole> getRoleForEvent(@PathVariable long eventId, @PathVariable String emaiAdress){
        return ResponseEntity.ok(userInEventWithRoleService.getRoleForEvent(eventId, emaiAdress));
    }

    /**
     * Endpoint for a user to register for an event
     * @param eventId Id of the event a user is about to register for
     * @param emailAdress Email from user to get further information
     * @return success message
     */
    @PostMapping("/event/{eventId}/register/{emailAdress}")
    public ResponseEntity<MessageResponse> registerForEvent( @PathVariable long eventId, @PathVariable String emailAdress){
        return ResponseEntity.ok(userInEventWithRoleService.registerForEvent(eventId, emailAdress));
    }

    /**
     * Endpoint to get all events a user is registered for
     * @param emailAdress Email from user to get further information
     * @return List of events a user is registered for
     */
    @PostMapping("/event/get-registered/{emailAdress}")
    public ResponseEntity<List<Event>> getRegisteredEventsForUser(@PathVariable String emailAdress){
        return ResponseEntity.ok(userInEventWithRoleService.getRegisteredEventsForUser(emailAdress));
    }

    /**
     * Endpoint to unregister a user from an event
     * @param eventId Corresponding event for unregistration
     * @param emailAdress Token from user to get further information
     * @return success message
     */
    //TODO reason wieder einpflegen
    @PostMapping("/event/{eventId}/unregister/{emailAdress}")
    public ResponseEntity<MessageResponse> ungregisterFromEvent(@PathVariable long eventId, @PathVariable String emailAdress){
        return ResponseEntity.ok(userInEventWithRoleService.unregisterFromEvent(eventId,emailAdress));
    }


    /**
     * Endpoint to get all available Events for a user, which he hasn't joined yet.
     * @param emailAdress Email of the corresponding user.
     * @return List of events.
     */
    @PostMapping("/event/get-all/{emailAdress}")
    public ResponseEntity<List<Event>> getAllAvailableEventsForUser(@PathVariable String emailAdress){
        return ResponseEntity.ok(userInEventWithRoleService.getAllAvailableEventsForUser(emailAdress));
    }

    /**
     * Endpoint to get all events where a user is invited to.
     * @param emailAddress Mail of the user.
     * @return List of events.
     */
    @PostMapping("/event/get-invitations/{emailAddress}")
    public ResponseEntity<List<Event>> getAllEventInvitationsForUser(@PathVariable String emailAddress) {
        System.out.println(emailAddress);
        return ResponseEntity.ok(userInEventWithRoleService.getEventInvitationsForUser(emailAddress));
    }

}
