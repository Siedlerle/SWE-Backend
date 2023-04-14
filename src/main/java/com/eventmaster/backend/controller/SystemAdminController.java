package com.eventmaster.backend.controller;

import com.eventmaster.backend.entities.Organisation;
import com.eventmaster.backend.services.OrganisationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * A class which handles the HTTP-requests for system admin functions.
 *
 * @author Fabian Eilber
 */

@RestController
@CrossOrigin
@RequestMapping("/sys-admin")
public class SystemAdminController {

    private OrganisationService organizationService;



    @PostMapping("create-orga")
    public ResponseEntity<?> createOrganisation(Organisation organisation, String sysAdminPassword){
        return ResponseEntity.ok(organizationService.createOrganisation(organisation, sysAdminPassword));
    }

    @PostMapping("edit-orga")
    public ResponseEntity<?> editOrganisation(Organisation organisation, String sysAdminPassword){
        return ResponseEntity.ok(organizationService.editOrganisation(organisation, sysAdminPassword));
    }

}
