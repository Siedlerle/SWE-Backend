package com.eventmaster.backend.OrganizationManagement;

import org.springframework.stereotype.Service;


/**
 * A class which receives and processes the requests of the OrganizationController.
 *
 * @author Fabian Unger
 */
@Service
public class OrganizationService {
    private final OrganizationRepository organizationRepository;

    public OrganizationService(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }


    /**
     * The database is searched for the organization with the corresponding ID.
     * @param organizationId ID of the organization wich will be searched.
     * @return Organization object
     */
    public Organization getOrganizationById(Long organizationId) {
        return organizationRepository.findById(organizationId).orElse(null);
    }

    /**
     * A new organization will be added to the database and the method to invite a user as admin to the organization will be called.
     * @param newOrganization Organization which will be added to the database
     * @return Boolean as status for success
     */
    public boolean createOrganization(Organization newOrganization) {
        try {
            organizationRepository.save(newOrganization);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
