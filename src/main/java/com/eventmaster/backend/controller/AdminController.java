package com.eventmaster.backend.controller;

import com.eventmaster.backend.entities.Event;
import com.eventmaster.backend.entities.Group;
import com.eventmaster.backend.serviceswithouttoken.EventService;
import com.eventmaster.backend.serviceswithouttoken.GroupService;
import com.eventmaster.backend.serviceswithouttoken.UserInEventWithRoleService;
import com.eventmaster.backend.serviceswithouttoken.UserInOrgaWithRoleService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private  EventService eventService;
    @Autowired
    private  UserInOrgaWithRoleService userInOrgaWithRoleService;
    @Autowired
    private  GroupService groupService;
    @Autowired
    private  UserInEventWithRoleService userInEventWithRoleService;

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
    @PostMapping("/group/add")
    public ResponseEntity<String> createGroup(@RequestBody Group group) {
        return ResponseEntity.ok(groupService.createGroup(group));
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

}
