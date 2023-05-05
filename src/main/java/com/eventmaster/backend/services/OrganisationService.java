package com.eventmaster.backend.services;

import com.eventmaster.backend.entities.Group;
import com.eventmaster.backend.entities.MessageResponse;
import com.eventmaster.backend.entities.Organisation;

import com.eventmaster.backend.repositories.OrganisationRepository;
import local.variables.LocalizedStringVariables;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;


/**
 * A class which receives and processes the requests of the SysAdminController.
 *
 * @author Fabian Unger
 * @author Fabian Eilber
 */
@Service
public class OrganisationService {
    private final OrganisationRepository organisationRepository;
    private final UserInOrgaWithRoleService userInOrgaWithRoleService;
    private final UserService userService;
    private final DocumentService documentService;
    private final EventService eventService;
    private final GroupService groupService;
    private final PresetService presetService;

    public OrganisationService(OrganisationRepository organizationRepository,
                               @Lazy UserInOrgaWithRoleService userInOrgaWithRoleService,
                               UserService userService,
                               DocumentService documentService,
                               @Lazy EventService eventService,
                               @Lazy GroupService groupService,
                               @Lazy PresetService presetService) {
        this.organisationRepository = organizationRepository;
        this.userInOrgaWithRoleService = userInOrgaWithRoleService;
        this.userService = userService;
        this.documentService = documentService;
        this.eventService =eventService;
        this.groupService = groupService;
        this.presetService = presetService;
    }
    /**
     * Deletes an organisation in the database.
     *
     * @param organisationId ID of the organisation which will be deleted.
     * @return String if successful or not.
     */
    public String deleteOrganisation(long organisationId) {
        Organisation organisation = this.getOrganisationById(organisationId);
        organisation.getEvents().forEach(event->{
            eventService.deleteEvent(event.getId());
        });
        organisation.getGroups().forEach(group -> {
            groupService.deleteGroup(group.getId());
        });
        organisation.getPresets().forEach(preset -> {
            presetService.deletePreset(preset.getId());
        });
        organisation.getOrgaUserRoles().forEach(orgaUserRole->{
            //userInOrgaWithRoleService.
        });
        try {

            this.organisationRepository.delete(organisation);

            return LocalizedStringVariables.ORGADELETEDSUCCESSMESSAGE;
        } catch (Exception e) {
            e.printStackTrace();
            return LocalizedStringVariables.ORGADELETEDFAILUREMESSAGE;
        }
    }
    /**
     * Saves the organisation in the database.
     *
     * @param organisation Organisation object which will be saved.
     */
    public void saveOrganisation(Organisation organisation) {
        this.organisationRepository.save(organisation);
    }

    /**
     * Get all organisations in the database in a list.
     *
     * @return List of organisations in the database.
     */
    public List<Organisation> getAllOrganisations() {
        return organisationRepository.findAll();
    }

    /**
     * Get the complete organisation by her id.
     *
     * @param organisationId ID of the organisation which will be returned.
     * @return Organisation Object
     */
    public Organisation getOrganisationById(long organisationId) {
        return organisationRepository.findById(organisationId);
    }

    /**
     * Saves a new organisation in the database.
     *
     * @param organisation Organisation object which will be saved.
     * @return String if successful or not.
     */
    public String createOrganisation(Organisation organisation) {
        try {
            Organisation save = new Organisation();
            save.setName(organisation.getName());
            save.setLocation(organisation.getLocation());
            organisationRepository.save(save);
            return LocalizedStringVariables.ORGACREATEDSUCCESSMESSAGE;
        } catch (Exception e) {
            e.printStackTrace();
            return LocalizedStringVariables.ORGACREATEDFAILUREMESSAGE;
        }
    }

    /**
     * Changes an already existing organisation.
     *
     * @param newOrganisation The new organisation which will replace the old organisation.
     * @return String if successful or not.
     */
    public MessageResponse changeOrganisation(Organisation newOrganisation, MultipartFile image) {
        try {
            long id = newOrganisation.getId();
            Organisation oldOrganisation = this.organisationRepository.findById(id);

            String newName = newOrganisation.getName();
            String newLocation = newOrganisation.getLocation();
            String oldImageLink = oldOrganisation.getImage();


            oldOrganisation.setName(newName);
            oldOrganisation.setLocation(newLocation);

            this.organisationRepository.save(oldOrganisation);

            if (image != null) {
                File oldfile = new File("src/main/upload" + oldImageLink);
                if (oldfile.exists()) {
                    oldfile.delete();
                }
                String imageUrl = documentService.saveOrgaImage(oldOrganisation.getId(), image);
                oldOrganisation.setImage(imageUrl);
                this.organisationRepository.save(oldOrganisation);
            }

            return MessageResponse.builder()
                    .message(LocalizedStringVariables.ORGACHANGEDSUCCESSMESSAGE)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return MessageResponse.builder()
                    .message(LocalizedStringVariables.ORGACHANGEDFAILUREMESSAGE)
                    .build();
        }
    }



    /**
     * Gets all groups of an organisation.
     *
     * @param organisationId ID of the organisation.
     * @return List of groups in the organisation.
     */
    public List<Group> getAllGroupsOfOrganisation(long organisationId) {
        Organisation organisation = this.getOrganisationById(organisationId);
        try {
            List<Group> groups = organisation.getGroups().stream().toList();
            return groups;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Admin Method to change Name and Location of an organisation
     *
     * @param organisation The organisation to be changed
     * @return Success or failure String
     */
    public String changeOrganisation(Organisation organisation) {
        try {
            Organisation change = this.organisationRepository.findById(organisation.getId());
            change.setName(organisation.getName());
            change.setLocation(organisation.getLocation());
            this.organisationRepository.save(change);

            return LocalizedStringVariables.ORGACHANGEDSUCCESSMESSAGE;
        } catch (Exception e) {
            return LocalizedStringVariables.ORGACHANGEDFAILUREMESSAGE;
        }
    }

    public List<Organisation> getAllOrganisationsForAdmin() {
        return this.organisationRepository.findAll()
                .stream()
                .peek(organisation -> {
                    organisation.setEvents(null);
                    organisation.setGroups(null);
                    organisation.setPresets(null);
                    organisation.setOrgaUserRoles(null);
                    organisation.setImage(null);
                })
                .toList();
    }
}
