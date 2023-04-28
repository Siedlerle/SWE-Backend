package com.eventmaster.backend.services;

import com.eventmaster.backend.entities.*;
import com.eventmaster.backend.repositories.GroupInEventRepository;
import local.variables.LocalizedStringVariables;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * A class which receives and processes the requests of the controller concerning the management of the relation between groups and events.
 *
 * @author Fabian Unger
 */
@Service
public class GroupInEventService {
    private final GroupInEventRepository groupInEventRepository;
    private final EventService eventService;
    private final EventRoleService eventRoleService;
    private final GroupService groupService;
    private final UserInGroupService userInGroupService;
    private final UserInEventWithRoleService userInEventWithRoleService;

    public GroupInEventService(GroupInEventRepository groupInEventRepository, EventService eventService, EventRoleService eventRoleService, GroupService groupService, UserInGroupService userInGroupService, UserInEventWithRoleService userInEventWithRoleService) {
        this.groupInEventRepository = groupInEventRepository;
        this.eventService = eventService;
        this.eventRoleService = eventRoleService;
        this.groupService = groupService;
        this.userInGroupService = userInGroupService;
        this.userInEventWithRoleService = userInEventWithRoleService;
    }


    /**
     * Invites a group of users to an event.
     * @param eventId ID of the event.
     * @param groupId Group of users who will be invited to the event.
     * @return String about success or failure.
     */
    public String inviteGroupToEvent(long eventId, long groupId) {
        Event event = eventService.getEventById(eventId);

        List<User> usersOfGroup = userInGroupService.getUsersOfGroup(groupId);

        try {
            for (User user : usersOfGroup) {
                userInEventWithRoleService.inviteUserToEvent(event.getId(), user.getEmailAdress(), false);
            }
            return LocalizedStringVariables.INVITEDGROUPTOEVENTSUCCESSMESSAGE;
        } catch (Exception e) {
            e.printStackTrace();
            return LocalizedStringVariables.INVITEDGROUPTOEVENTFAILUREMESSAGE;
        }

    }

    /**
     * Gets the users of a group and calls the method to remove them from the event.
     * @param eventId ID of the event.
     * @param groupId ID of the group which will be removed.
     * @param reason Reason why the group will be removed.
     * @return String about success or failure.
     */
    public String removeGroupFromEvent(long eventId, long groupId, String reason) {
        List<User> usersOfGroup = userInGroupService.getUsersOfGroup(groupId);
        return userInEventWithRoleService.removeUsersOfGroupFromEvent(eventId, usersOfGroup, reason);
    }
}
