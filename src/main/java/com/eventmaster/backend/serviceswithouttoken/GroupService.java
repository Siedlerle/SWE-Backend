package com.eventmaster.backend.serviceswithouttoken;

import com.eventmaster.backend.entities.Group;
import com.eventmaster.backend.repositories.GroupRepository;
import org.springframework.stereotype.Service;

/**
 * A class which receives and processes the requests of multiple controllers concerning the management of groups
 *
 * @author Fabian Unger
 */
@Service
public class GroupService {

    private final GroupRepository groupRepository;

    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    /**
     * Saves a new Group in the database.
     * @param group Group object which will be saved.
     * @return String about success of failure.
     */
    public String createGroup(Group group) {
        try {
            this.groupRepository.save(group);
            return "Group saved successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Creation failed";
        }
    }

    /**
     * Changes the name of an existing group in the database.
     * @param group New Group with another name.
     * @return String about success of failure.
     */
    public String changeGroup(Group group) {
        try {
            long id = group.getId();
            Group oldGroup = this.groupRepository.findById(id);

            String newName = group.getName();

            oldGroup.setName(newName);

            this.groupRepository.save(oldGroup);

            return "Group changed successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Change failed";
        }
    }

    /**
     * Deletes an existing group in the database.
     * @param groupId ID of the group which will be deleted.
     * @return String about success of failure.
     */
    public String deleteGroup(long groupId) {
        try {
            this.groupRepository.deleteById(groupId);
            return "Group deleted successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Group not deleted";
        }
    }
}
