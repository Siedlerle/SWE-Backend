package com.eventmaster.backend.controller;

import com.eventmaster.backend.entities.Organisation;

import com.eventmaster.backend.entities.User;
import com.eventmaster.backend.serviceswithouttoken.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * A class which handles the HTTP-requests for system admin functions.
 *
 * @author Fabian Eilber
 */

@RestController
@CrossOrigin
@RequestMapping("/sys-admin")
public class SystemAdminController {

    private OrganisationService organisationService;
    private UserService userService;


    /**
     * Endpoint to create an organisation.
     * @param organisation Organisation which will be saved.
     * @param sysAdminPassword Password of the System-Admin to authorize him.
     * @return success message
     */
    @PostMapping("/create-orga")
    public ResponseEntity<String> createOrganisation(@RequestParam Organisation organisation,
                                                @RequestParam String sysAdminPassword){
        //TODO passwort vom Sysadmin abfragen?
        return ResponseEntity.ok(organisationService.createOrganisation(organisation));
    }

    /**
     * Endpoint to change an already existing organisation.
     * @param organisation Organisation which will be changed.
     * @param sysAdminPassword Password of the System-Admin to authorize him.
     * @return success message
     */
    @PostMapping("/edit-orga")
    public ResponseEntity<String> editOrganisation(@RequestParam Organisation organisation,
                                              @RequestParam String sysAdminPassword){
        //TODO passwort vom Sysadmin abfragen?
        return ResponseEntity.ok(organisationService.editOrganisation(organisation));
    }

    /**
     * Endpoint to delete an organisation.
     * @param organisationId ID of the organisation which will be deleted.
     * @param sysAdminPassword Password of the System-Admin to authorize him.
     * @return success message
     */
    @PostMapping("/delete-orga")
    public ResponseEntity<String> deleteOrganisation(@RequestParam long organisationId,
                                                     @RequestParam String sysAdminPassword) {
        //TODO passwort vom Sysadmin abfragen?
        return ResponseEntity.ok(organisationService.deleteOrganisation(organisationId));
    }

    /**
     * Endpoint to delete a user.
     * @param userId ID of the user which will be deleted.
     * @param sysAdminPassword Password of the System-Admin to authorize him.
     * @return success message
     */
    @PostMapping("/delete-user")
    public ResponseEntity<String> deleteUser(@RequestParam long userId,
                                             @RequestParam String sysAdminPassword) {
        //TODO passwort vom Sysadmin abfragen?
        return ResponseEntity.ok(userService.deleteUser(userId));
    }

}
