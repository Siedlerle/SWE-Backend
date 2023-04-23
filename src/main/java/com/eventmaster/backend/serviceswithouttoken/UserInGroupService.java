package com.eventmaster.backend.serviceswithouttoken;

import com.eventmaster.backend.entities.Group;
import com.eventmaster.backend.entities.User;
import com.eventmaster.backend.entities.UserInGroup;
import com.eventmaster.backend.repositories.UserInGroupRepository;
import local.variables.LocalizedStringVariables;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserInGroupService {
    private final UserInGroupRepository userInGroupRepository;
    private final GroupService groupService;
    private final UserService userService;
    public UserInGroupService(UserInGroupRepository userInGroupRepository, GroupService groupService, UserService userService) {
        this.userInGroupRepository = userInGroupRepository;
        this.groupService = groupService;
        this.userService = userService;
    }

    /**
     * Gets all the groups where the user is part of.
     * @param userMail Mail of the user.
     * @return List of groups.
     */
    public List<Group> getGroupsOfUser(String userMail) {
        User user = userService.getUserByMail(userMail);
        try {
            List<UserInGroup> userInGroupList = userInGroupRepository.findByUser(user);
            List<Group> groupsOfUser = new ArrayList<>();
            for (UserInGroup userInGroup : userInGroupList) {
                Group group = userInGroup.getGroup();
                groupsOfUser.add(group);
            }
            return groupsOfUser;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Searches for the UserInGroup Object according to a given user and group.
     * @param user User object
     * @param group Group object
     * @return UserInGroup object if existing.
     */
    public UserInGroup getUserInGroupByUserAndGroup(User user, Group group) {
        try {
            return userInGroupRepository.findByUserAndGroup(user, group);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get all the users who are part of the group.
     * @param groupId ID of the group.
     * @return List of users
     */
    public List<User> getUsersOfGroup(long groupId) {
        Group group = groupService.getGroupById(groupId);
        try {
            List<UserInGroup> userInGroupList = userInGroupRepository.findByGroup(group);
            List<User> users = new ArrayList<>();
            for (UserInGroup userInGroup : userInGroupList) {
                User user = userInGroup.getUser();
                users.add(user);
            }
            return users;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Adds a user to a group.
     * @param groupId ID of the group.
     * @param userMail Mail address of the user who will be added to the group.
     * @return String about success or failure.
     */
    public String addUserToGroup(long groupId, String userMail) {
        Group group = groupService.getGroupById(groupId);
        User user = userService.getUserByMail(userMail);
        //TODO testen, ob user gleicher organisation angehört wie Gruppe
        UserInGroup userInGroup = new UserInGroup();
        userInGroup.setUser(user);
        userInGroup.setGroup(group);
        try {
            userInGroupRepository.save(userInGroup);
            return LocalizedStringVariables.ADDEDUSERTOGROUPSUCCESSMESSAGE;
        } catch (Exception e) {
            e.printStackTrace();
            return LocalizedStringVariables.ADDEDUSERTOGROUPFAILUREMESSAGE;
        }
    }

    /**
     * Removes a user in a group by deleting the relation data between the user and the group.
     * @param groupId ID of the group from which the user will be removed.
     * @param userMail Mail of the user who lefts the group.
     * @return String about success or failure.
     */
    public String removeUserFromGroup(long groupId, String userMail) {
        Group group = groupService.getGroupById(groupId);
        User user = userService.getUserByMail(userMail);

        UserInGroup userInGroup = getUserInGroupByUserAndGroup(user, group);

        try {
            userInGroupRepository.delete(userInGroup);
            return LocalizedStringVariables.REMOVEDUSERFROMGROUPSUCCESSMESSAGE;
        } catch (Exception e) {
            e.printStackTrace();
            return LocalizedStringVariables.REMOVEDUSERFROMGROUPFAILUREMESSAGE;
        }
    }
}
