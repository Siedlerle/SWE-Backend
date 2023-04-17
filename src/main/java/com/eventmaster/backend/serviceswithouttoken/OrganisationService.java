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

    public List<Organisation> getAllOrganisations(){
        return organisationRepository.findAll();
    }

    public Organisation getOrganisationById(long organisationId){
        return organisationRepository.findById(organisationId);
    }

    public String createOrganisation(Organisation organisation){
        try {
            organisationRepository.save(organisation);
            return "Organisation succesfully created";
        } catch (Exception e) {
            e.printStackTrace();
            return "Organisation creation failed";
        }
    }

    public String editOrganisation(Organisation newOrganisation){
        try {
            long id = newOrganisation.getId();
            Organisation oldOrganisation = this.organisationRepository.findById(id);

            String newName = newOrganisation.getName();
            String newLocation = newOrganisation.getLocation();

            oldOrganisation.setName(newName);
            oldOrganisation.setLocation(newLocation);

            //TODO Wird das beim Saven eine neue Entität oder wird die alte überschrieben?
            this.organisationRepository.save(oldOrganisation);

            return "Organisation succesfully changed";
        } catch (Exception e) {
            e.printStackTrace();
            return "Organisation change failed";
        }
    }

    public String deleteOrganisation(long organisationId){
        try {
            Organisation organisation = this.getOrganisationById(organisationId);
            this.organisationRepository.deleteById(organisationId);

            return "Organisation " + organisation.getName() + " deleted successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Organisation deleted";
        }
    }
}
