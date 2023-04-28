package com.eventmaster.backend.services;

import com.eventmaster.backend.entities.Group;
import com.eventmaster.backend.entities.Organisation;
import com.eventmaster.backend.repositories.GroupRepository;
import local.variables.LocalizedStringVariables;
import org.springframework.stereotype.Service;

/**
 * A class which receives and processes the requests of multiple controllers concerning the management of groups
 *
 * @author Fabian Unger
 */
@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final OrganisationService organisationService;

    public GroupService(GroupRepository groupRepository, OrganisationService organisationService) {
        this.groupRepository = groupRepository;
        this.organisationService = organisationService;
    }

    /**
     * Returns a group by a given id if it exists.
     * @param id ID of the group.
     * @return Group object if existing.
     */
    public Group getGroupById(long id) {
        try {
            return groupRepository.findById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Saves a new Group in the database.
     * @param group Group object which will be saved.
     * @return String about success of failure.
     */
    public String createGroup(Group group, long orgaId) {
        Organisation organisation = organisationService.getOrganisationById(orgaId);
        group.setOrganisation(organisation);
        organisation.addGroup(group);
        organisationService.saveOrganisation(organisation);
        try {
            this.groupRepository.save(group);
            return LocalizedStringVariables.GROUPCREATEDSUCCESSMESSAGE;
        } catch (Exception e) {
            e.printStackTrace();
            return LocalizedStringVariables.GROUPCREATEDFAILUREMESSAGE;
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

            return LocalizedStringVariables.GROUPCHANGEDSUCCESSMESSAGE;
        } catch (Exception e) {
            e.printStackTrace();
            return LocalizedStringVariables.GROUPCHANGEDFAILUREMESSAGE;
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
            return LocalizedStringVariables.GROUPDELETEDSUCCESSMESSAGE;
        } catch (Exception e) {
            e.printStackTrace();
            return LocalizedStringVariables.GROUPDELETEDFAILUREMESSAGE;
        }
    }
}
