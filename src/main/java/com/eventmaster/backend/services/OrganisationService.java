package com.eventmaster.backend.services;

import com.eventmaster.backend.entities.Organisation;
import com.eventmaster.backend.repositories.OrganisationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * A class which receives and processes the requests of the SysAdminController.
 *
 * @author Fabian Eilber
 */
public class OrganisationService {
    private final OrganisationRepository organisationRepository;

    @Autowired
    private OrgaUserRoleService orgaUserRoleService;

    public OrganisationService(OrganisationRepository organizationRepository) {
        this.organisationRepository = organizationRepository;
    }

    public String createOrganisation(Organisation organisation, String sysAdminPassword){
        //TODO passwort vom Sysadmin abfragen?
        try {
            organisationRepository.save(organisation);
            return "Organization succesfully created";
        } catch (Exception e) {
            e.printStackTrace();
            return "Organization creation failed";
        }
    }

    public String editOrganisation(Organisation organisation, String sysAdminPassword){
        //TODO passwort vom Sysadmin abfragen?
        try {
            //TODO Organisation finden und mit neuen daten überladen und speichern
            return "Organization succesfully created";
        } catch (Exception e) {
            e.printStackTrace();
            return "Organization creation failed";
        }
    }


    public String deleteOrganisation(Organisation organisation, String sysAdminPassword){
        //TODO passwort vom Sysadmin abfragen?
        try {
            //TODO Organisation finden und mit neuen daten überladen und speichern
            return "Organization succesfully created";
        } catch (Exception e) {
            e.printStackTrace();
            return "Organization creation failed";
        }
    }

}
