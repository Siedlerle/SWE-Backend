package com.eventmaster.backend.controller;

import com.eventmaster.backend.entities.Event;
import com.eventmaster.backend.entities.Group;
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
 * @author Fabian Unger
 */

@RestController
@CrossOrigin
@RequestMapping("/admin")
public class AdminController {

    private final EventService eventService;
    private final UserInOrgaWithRoleService userInOrgaWithRoleService;
    private final GroupService groupService;

    public AdminController(EventService eventService,
                           UserInOrgaWithRoleService userInOrgaWithRoleService,
                           GroupService groupService) {
        this.eventService = eventService;
        this.userInOrgaWithRoleService = userInOrgaWithRoleService;
        this.groupService = groupService;
    }

    /**
     * Endpoint for a part of the organisation to get all of its events
     * @param orgaId Id of the corresponding orga
     * @return List of events
     */
    @PostMapping("/get-events-of-orga/{orgaId}")
    public ResponseEntity<List<Event>> getEventsOfOrganisation(@PathVariable Long orgaId){
        return ResponseEntity.ok(eventService.getEventsOfOrganisation(orgaId));
    }

    /**
     * Endpoint for an admin to remove a user from an organisation.
     * @param orgaId ID of the organisation from which the user will be removed.
     * @param userMail Mail of the user.
     * @return String about success of failure.
     */
    @PostMapping("/remove-user-from-orga/{orgaId}")
    public ResponseEntity<String> removeUserFromOrganisation(@PathVariable long orgaId,
                                                             @RequestBody String userMail) {
        return ResponseEntity.ok(userInOrgaWithRoleService.removeUserFromOrganisation(orgaId, userMail));
    }

    /**
     * Endpoint for an admin to save a group in the database.
     * @param group Group object which will be saved in the database.
     * @return String about success of failure.
     */
    @PostMapping("/create-group")
    public ResponseEntity<String> createGroup(@RequestBody Group group) {
        return ResponseEntity.ok(groupService.createGroup(group));
    }

    /**
     * Endpoint for an admin to change a group in the database.
     * @param group New group which will overwrite the existing Group.
     * @return String about success of failure.
     */
    @PostMapping("/change-group")
    public ResponseEntity<String> changeGroup(@RequestBody Group group) {
        return ResponseEntity.ok(groupService.changeGroup(group));
    }

    /**
     * Endpoint to delete a group.
     * @param groupId ID of the group which will be deleted.
     * @return String about success or failure.
     */
    @PostMapping("/delete-group")
    public ResponseEntity<String> deleteGroup(@RequestBody long groupId) {
        return ResponseEntity.ok(groupService.deleteGroup(groupId));
    }

}
