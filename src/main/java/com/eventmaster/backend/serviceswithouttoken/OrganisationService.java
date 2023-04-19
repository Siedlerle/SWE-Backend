package com.eventmaster.backend.serviceswithouttoken;

import com.eventmaster.backend.entities.Organisation;
import com.eventmaster.backend.repositories.OrganisationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public OrganisationService(OrganisationRepository organizationRepository) {
        this.organisationRepository = organizationRepository;
    }

    /**
     * Get all organisations in the database in a list.
     * @return List of organisations in the database.
     */
    public List<Organisation> getAllOrganisations(){
        return organisationRepository.findAll();
    }

    /**
     * Get the complete organisation by her id.
     * @param organisationId ID of the organisation which will be returned.
     * @return Organisation Object
     */
    public Organisation getOrganisationById(long organisationId){
        return organisationRepository.findById(organisationId);
    }

    /**
     * Saves a new organisation in the database.
     * @param organisation Organisation object which will be saved.
     * @return String if successful or not.
     */
    public String createOrganisation(Organisation organisation){
        try {
            organisationRepository.save(organisation);
            return "Organisation created successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Organisation creation failed";
        }
    }

    /**
     * Changes an already existing organisation.
     * @param newOrganisation The new organisation which will replace the old organisation.
     * @return String if successful or not.
     */
    public String changeOrganisation(Organisation newOrganisation){
        try {
            long id = newOrganisation.getId();
            Organisation oldOrganisation = this.organisationRepository.findById(id);

            String newName = newOrganisation.getName();
            String newLocation = newOrganisation.getLocation();

            oldOrganisation.setName(newName);
            oldOrganisation.setLocation(newLocation);

            //TODO Wird das beim Saven eine neue Entität oder wird die alte überschrieben?
            this.organisationRepository.save(oldOrganisation);

            return "Organisation changed successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Organisation change failed";
        }
    }

    /**
     * Deletes an organisation in the database.
     * @param organisationId ID of the organisation which will be deleted.
     * @return String if successful or not.
     */
    public String deleteOrganisation(long organisationId){
        try {
            Organisation organisation = this.getOrganisationById(organisationId);
            this.organisationRepository.deleteById(organisationId);

            return "Organisation " + organisation.getName() + " deleted successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Organisation not deleted";
        }
    }
}
