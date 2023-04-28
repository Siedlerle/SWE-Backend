package com.eventmaster.backend.controller;

import com.eventmaster.backend.entities.Event;
import com.eventmaster.backend.entities.Group;
import com.eventmaster.backend.entities.Organisation;
import com.eventmaster.backend.services.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    public AdminController(OrganisationService organisationService, EventService eventService,
                           UserInOrgaWithRoleService userInOrgaWithRoleService,
                           UserInEventWithRoleService userInEventWithRoleService, GroupService groupService, UserInGroupService userInGroupService) {
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
     * @param organisation New Organisation object with new data.
     * @return String about success or failure.
     */
    @PostMapping("/orga/change")
    public ResponseEntity<String> changeOrganisationDetails(@RequestBody Organisation organisation) {
        return ResponseEntity.ok(organisationService.changeOrganisation(organisation));
    }

    /**
     * Endpoint for an admin to remove a user from an organisation.
     * @param orgaId ID of the organisation from which the user will be removed.
     * @param userMail Mail of the user.
     * @return String about success of failure.
     */
    @PostMapping("/orga/{orgaId}/user/remove")
    public ResponseEntity<String> removeUserFromOrganisation(@PathVariable long orgaId,
                                                             @RequestBody String userMail) {
        return ResponseEntity.ok(userInOrgaWithRoleService.removeUserFromOrganisation(orgaId, userMail));
    }

    /**
     * Endpoint to set the role of a user in an organisation to admin.
     * @param orgaId ID of the organisation.
     * @param userMail Mail of the user who will be admin.
     * @return String about success or failure.
     */
    @PostMapping("/orga/{orgaId}/user/role/admin")
    public ResponseEntity<String> setPersonAdmin(@PathVariable long orgaId,
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
    public ResponseEntity<String> setPersonOrganizer(@PathVariable long orgaId,
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
    public ResponseEntity<String> setPersonUser(@PathVariable long orgaId,
                                                @RequestBody String userMail) {
        return ResponseEntity.ok(userInOrgaWithRoleService.setPersonUser(orgaId, userMail));
    }

    /**
     * Endpoint for an admin to save a group in the database.
     * @param group Group object which will be saved in the database.
     * @return String about success of failure.
     */
    @PostMapping("/orga/{orgaId}/group/add")
    public ResponseEntity<String> createGroup(@PathVariable long orgaId,
                                              @RequestBody Group group) {
        return ResponseEntity.ok(groupService.createGroup(group, orgaId));
    }

    /**
     * Endpoint for an admin to change a group in the database.
     * @param group New group which will overwrite the existing Group.
     * @return String about success of failure.
     */
    @PostMapping("/group/change")
    public ResponseEntity<String> changeGroup(@RequestBody Group group) {
        return ResponseEntity.ok(groupService.changeGroup(group));
    }

    /**
     * Endpoint to delete a group.
     * @param groupId ID of the group which will be deleted.
     * @return String about success or failure.
     */
    @PostMapping("/group/delete")
    public ResponseEntity<String> deleteGroup(@RequestBody long groupId) {
        return ResponseEntity.ok(groupService.deleteGroup(groupId));
    }

    /**
     * Endpoint to add a user to a group.
     * @param groupId ID of the group.
     * @param userMail Mail address of the user.
     * @return String about success or failure.
     */
    @PostMapping("/group/{groupId}/user/add")
    public ResponseEntity<String> addUserToGroup(@PathVariable long groupId,
                                                 @RequestBody String userMail) {
        return ResponseEntity.ok(userInGroupService.addUserToGroup(groupId, userMail));
    }

    /**
     * Endpoint to remove a user from a group.
     * @param groupId ID of the group.
     * @param userMail Mail address of the user.
     * @return String about success or failure.
     */
    @PostMapping("/group/{groupId}/user/remove")
    public ResponseEntity<String> removeUserFromGroup(@PathVariable long groupId,
                                                      @RequestBody String userMail) {
        return ResponseEntity.ok(userInGroupService.removeUserFromGroup(groupId, userMail));
    }

    /**
     * Endpoint to change the organizer of an event.
     * @param eventId ID of the event.
     * @param userMail Mail address of the new organizer.
     * @return String about success or failure.
     */
    @PostMapping("/event/{eventId}/organizer/change")
    public ResponseEntity<String> changeOrganizerOfEvent(@PathVariable long eventId,
                                                         @RequestBody String userMail) {
        return ResponseEntity.ok(userInEventWithRoleService.changeOrganizerOfEvent(eventId, userMail));
    }

}
