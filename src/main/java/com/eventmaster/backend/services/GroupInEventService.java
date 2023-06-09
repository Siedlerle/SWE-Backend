package com.eventmaster.backend.services;

import com.eventmaster.backend.entities.*;
import com.eventmaster.backend.repositories.GroupInEventRepository;
import local.variables.LocalizedStringVariables;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public MessageResponse inviteGroupToEvent(long eventId, long groupId) {
        Event event = eventService.getEventById(eventId);
        List<User> usersOfGroup = userInGroupService.getUsersOfGroup(groupId);

        try {
            for (User user : usersOfGroup) {
                userInEventWithRoleService.inviteUserToEvent(event.getId(), user.getEmailAdress(), false);
            }
            GroupInEvent gIE = new GroupInEvent();
            gIE.setGroup(groupService.getGroupById(groupId));
            gIE.setEvent(event);
            this.groupInEventRepository.save(gIE);
            return MessageResponse.builder()
                    .message(LocalizedStringVariables.INVITEDGROUPTOEVENTSUCCESSMESSAGE)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return MessageResponse.builder()
                    .message(LocalizedStringVariables.INVITEDGROUPTOEVENTFAILUREMESSAGE)
                    .build();
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
    /**
     * Gets all unaffiliated groups of organisation for this event.
     * @param event Event to be checked.
     * @return List of groups that aren't affiliated.
     */
    public List<Group> getUnaffiliatedGroupsForEvent(Event event) {
        long eventId = event.getId();
        List<Group> allGroupsInOrga = groupService.findByOrganisationId(event.getOrganisation().getId());
        List<Group> affiliated = findByEventId(eventId)
                .stream()
                .map(GroupInEvent::getGroup)
                .toList();
        List<Group> unaffiliatedGroups = allGroupsInOrga
                .stream()
                .filter(group -> !affiliated.contains(group))
                .toList();
        return unaffiliatedGroups;
    }
    public List<GroupInEvent> findByEventId(long eventId){
        return groupInEventRepository.findByEvent_Id(eventId);
    }
}
