package com.eventmaster.backend.services;

import com.eventmaster.backend.entities.Organisation;
import com.eventmaster.backend.entities.User;
import com.eventmaster.backend.repositories.OrganisationRepository;
import local.variables.LocalizedStringVariables;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
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

    public OrganisationService(OrganisationRepository organizationRepository,
                               @Lazy UserInOrgaWithRoleService userInOrgaWithRoleService,
                               UserService userService, DocumentService documentService) {
        this.organisationRepository = organizationRepository;
        this.userInOrgaWithRoleService = userInOrgaWithRoleService;
        this.userService = userService;
        this.documentService = documentService;
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
    public String createOrganisation(Organisation organisation, User admin) {
        try {
            User orgaAdmin;
            orgaAdmin = userService.getUserById(admin.getId());
            if (orgaAdmin == null) {
                userService.register(admin);
                orgaAdmin = userService.getUserById(admin.getId());
            }
            Organisation orga =organisationRepository.save(organisation);
            return userInOrgaWithRoleService.setAdminForOrga(orga, orgaAdmin);
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
    public String changeOrganisation(Organisation newOrganisation, MultipartFile image) {
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

            return LocalizedStringVariables.ORGACHANGEDSUCCESSMESSAGE;
        } catch (Exception e) {
            e.printStackTrace();
            return LocalizedStringVariables.ORGACHANGEDFAILUREMESSAGE;
        }
    }

    /**
     * Deletes an organisation in the database.
     *
     * @param organisationId ID of the organisation which will be deleted.
     * @return String if successful or not.
     */
    public String deleteOrganisation(long organisationId) {
        try {
            Organisation organisation = this.getOrganisationById(organisationId);
            this.organisationRepository.deleteById(organisationId);

            return LocalizedStringVariables.ORGADELETEDSUCCESSMESSAGE;
        } catch (Exception e) {
            e.printStackTrace();
            return LocalizedStringVariables.ORGADELETEDFAILUREMESSAGE;
        }
    }
}
