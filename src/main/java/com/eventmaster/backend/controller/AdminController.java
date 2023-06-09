package com.eventmaster.backend.controller;

import com.eventmaster.backend.entities.*;
import com.eventmaster.backend.services.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * A class which handles the HTTP-requests for admin functions.
 *
 * @author Fabian Eilber
 * @author Fabian Unger
 */

@RestController
@CrossOrigin
@RequestMapping("/admin")
public class AdminController {

    private final OrganisationService organisationService;
    private final EventService eventService;
    private final UserInOrgaWithRoleService userInOrgaWithRoleService;
    private final UserInEventWithRoleService userInEventWithRoleService;
    private final GroupService groupService;
    private final UserInGroupService userInGroupService;

    public AdminController(OrganisationService organisationService,
                           EventService eventService,
                           UserInOrgaWithRoleService userInOrgaWithRoleService,
                           UserInEventWithRoleService userInEventWithRoleService,
                           GroupService groupService,
                           UserInGroupService userInGroupService) {
        this.organisationService = organisationService;
        this.eventService = eventService;
        this.userInOrgaWithRoleService = userInOrgaWithRoleService;
        this.userInEventWithRoleService = userInEventWithRoleService;
        this.groupService = groupService;
        this.userInGroupService = userInGroupService;
    }

    /**
     * Endpoint for a part of the organisation to get all of its events
     * @param orgaId Id of the corresponding orga
     * @return List of events
     */
    @PostMapping("/orga/{orgaId}/events")
    public ResponseEntity<List<Event>> getEventsOfOrganisation(@PathVariable Long orgaId){
        return ResponseEntity.ok(eventService.getEventsOfOrganisation(orgaId));
    }

    /**
     * Endpoint for the admin of an organisation to change her details.
     * @param organisationJson New Organisation object with new data.
     * @return String about success or failure.
     */
    @PostMapping("/orga/change")
    public ResponseEntity<MessageResponse> changeOrganisationDetails(@RequestParam("organisation") String organisationJson,
                                                            @RequestParam(value = "image", required = false) MultipartFile image) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Organisation organisation = mapper.readValue(organisationJson, Organisation.class);

        return ResponseEntity.ok(organisationService.changeOrganisation(organisation, image));
    }

    /**
     * Endpoint for an admin to remove a user from an organisation.
     * @param orgaId ID of the organisation from which the user will be removed.
     * @param userMail Mail of the user.
     * @return String about success of failure.
     */
    @PostMapping("/orga/{orgaId}/user/{userMail}/remove")
    public ResponseEntity<MessageResponse> removeUserFromOrganisation(@PathVariable long orgaId,
                                                                      @PathVariable String userMail) {
        return ResponseEntity.ok(userInOrgaWithRoleService.removeUserFromOrganisation(orgaId, userMail));
    }

    /**
     * Endpoint for a amdin to load all requests to join his organisation
     * @param orgaId Id of the corresponding organisation
     * @return list of users
     */
    @PostMapping("/orga/{orgaId}/get-requests")
    public ResponseEntity<List<User>> getJoinRequests(@PathVariable long orgaId){
       return ResponseEntity.ok(userInOrgaWithRoleService.getRequestingUsers(orgaId));
    }

    /**
     * Endpoint for an admin to accept join request
     * @param orgaId Id of the corresponding organisation
     * @param emailAdress EMail of the corresponding user
     * @return success message
     */
    @PostMapping("/orga/{orgaId}/user/{emailAdress}/accept")
    public ResponseEntity<MessageResponse> acceptJoinRequest(@PathVariable long orgaId,
                                                             @PathVariable String emailAdress){
        return ResponseEntity.ok(userInOrgaWithRoleService.acceptJoinRequest(orgaId, emailAdress));
    }

    /**
     * Endpoint for an admin to decline join request
     * @param orgaId Id of the corresponding organisation
     * @param emailAdress EMail of the corresponding user
     * @return success message
     */
    @PostMapping("/orga/{orgaId}/user/{emailAdress}/decline")
    public ResponseEntity<MessageResponse> declineJoinRequest(@PathVariable long orgaId,
                                                              @PathVariable String emailAdress){
        return ResponseEntity.ok(userInOrgaWithRoleService.declineJoinRequest(orgaId, emailAdress));
    }

    /**
     * Endpoint to set the role of a user in an organisation to admin.
     * @param orgaId ID of the organisation.
     * @param userMail Mail of the user who will be admin.
     * @return String about success or failure.
     */
    @PostMapping("/orga/{orgaId}/user/role/admin")
    public ResponseEntity<MessageResponse> setPersonAdmin(@PathVariable long orgaId,
                                                 @RequestBody String userMail) {
        return ResponseEntity.ok(userInOrgaWithRoleService.setPersonAdmin(orgaId, userMail));
    }

    /**
     * Endpoint to set the role of a user in an organisation to organizer.
     * @param orgaId ID of the organisation.
     * @param userMail Mail of the user who will be organizer.
     * @return String about success or failure.
     */
    @PostMapping("/orga/{orgaId}/user/role/organizer")
    public ResponseEntity<MessageResponse> setPersonOrganizer(@PathVariable long orgaId,
                                                     @RequestBody String userMail) {
        return ResponseEntity.ok(userInOrgaWithRoleService.setPersonOrganizer(orgaId, userMail));
    }

    /**
     * Endpoint to set the role of a user in an organisation to a normal user.
     * @param orgaId ID of the organisation.
     * @param userMail Mail of the user who will be user.
     * @return String about success or failure.
     */
    @PostMapping("/orga/{orgaId}/user/role/user")
    public ResponseEntity<MessageResponse> setPersonUser(@PathVariable long orgaId,
                                                @RequestBody String userMail) {
        return ResponseEntity.ok(userInOrgaWithRoleService.setPersonUser(orgaId, userMail));
    }

    /**
     * Endpoint for an admin to save a group in the database.
     * @param group Group object which will be saved in the database.
     * @return String about success of failure.
     */
    @PostMapping("/orga/{orgaId}/group/add")
    public ResponseEntity<MessageResponse> createGroup(@PathVariable long orgaId,
                                              @RequestBody Group group) {
        return ResponseEntity.ok(groupService.createGroup(group, orgaId));
    }

    /**
     * Endpoint for an admin to change a group in the database.
     * @param group New group which will overwrite the existing Group.
     * @return String about success of failure.
     */
    @PostMapping("/group/change")
    public ResponseEntity<MessageResponse> changeGroup(@RequestBody Group group) {
        return ResponseEntity.ok(groupService.changeGroup(group));
    }

    /**
     * Endpoint to delete a group.
     * @param groupId ID of the group which will be deleted.
     * @return String about success or failure.
     */
    @PostMapping("/group/{groupId}/delete")
    public ResponseEntity<MessageResponse> deleteGroup(@PathVariable long groupId) {
        return ResponseEntity.ok(groupService.deleteGroup(groupId));
    }

    /**
     * Endpoint to add a user to a group.
     * @param groupId ID of the group.
     * @param userMail Mail address of the user.
     * @return String about success or failure.
     */
    @PostMapping("/group/{groupId}/user/add")
    public ResponseEntity<MessageResponse> addUserToGroup(@PathVariable long groupId,
                                                 @RequestBody String userMail) {
        return ResponseEntity.ok(userInGroupService.addUserToGroup(groupId, userMail));
    }

    /**
     * Endpoint to remove a user from a group.
     * @param groupId ID of the group.
     * @param userMail Mail address of the user.
     * @return String about success or failure.
     */
    @PostMapping("/group/{groupId}/user/{userMail}/remove")
    public ResponseEntity<MessageResponse> removeUserFromGroup(@PathVariable long groupId,
                                                      @PathVariable String userMail) {
        return ResponseEntity.ok(userInGroupService.removeUserFromGroup(groupId, userMail));
    }

    /**
     * Endpoint to get users in organisation which are not in the group.
     * @param orgaId ID of the organisation.
     * @param groupId ID of the group.
     * @return List of Users
     */
    @PostMapping("/orga/{orgaId}/group/{groupId}/user/get-not-in-group")
    public ResponseEntity<List<User>> getUsersOfOrganisationAndNotInGroup(@PathVariable long orgaId,
                                                                          @PathVariable long groupId) {
        return ResponseEntity.ok(userInGroupService.getUsersOfOrgaNotInGroup(orgaId, groupId));
    }

    /**
     * Endpoint to change the organizer of an event.
     * @param eventId ID of the event.
     * @param userMail Mail address of the new organizer.
     * @return String about success or failure.
     */
    @PostMapping("/event/{eventId}/organizer/change/{userMail}")
    public ResponseEntity<MessageResponse> changeOrganizerOfEvent(@PathVariable long eventId,
                                                         @PathVariable String userMail) {
        return ResponseEntity.ok(userInEventWithRoleService.changeOrganizerOfEvent(eventId, userMail));
    }

    /**
     * Endpoint to get all organizers of an organisation.
     * @param orgaId ID of organisation.
     * @return List of organizers.
     */
    @PostMapping("/orga/{orgaId}/organizers/get-all")
    public ResponseEntity<List<User>> getOrganizersOfOrga(@PathVariable long orgaId) {
        return ResponseEntity.ok(userInOrgaWithRoleService.getOrganizersOfOrganisation(orgaId));
    }

}
