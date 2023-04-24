package com.eventmaster.backend.controller;

import com.eventmaster.backend.entities.Organisation;

import com.eventmaster.backend.entities.User;
import com.eventmaster.backend.serviceswithouttoken.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * A class which handles the HTTP-requests for system admin functions.
 *
 * @author Fabian Unger
 * @author Fabian Eilber
 */

@RestController
@CrossOrigin
@RequestMapping("/sys-admin")
public class SystemAdminController {

    private final OrganisationService organisationService;
    private final UserService userService;

    public SystemAdminController(OrganisationService organisationService,
                                 UserService userService) {
        this.organisationService = organisationService;
        this.userService = userService;
    }


    /**
     * Endpoint to create an organisation.
     * @param organisation Organisation which will be saved.
     * @return success message
     */
    @PostMapping("/organisation/create")
    public ResponseEntity<String> createOrganisation(@RequestBody Organisation organisation){
        //TODO passwort vom Sysadmin abfragen?
        return ResponseEntity.ok(organisationService.createOrganisation(organisation));
    }

    /**
     * Endpoint to change an already existing organisation.
     * @param organisation Organisation which will be changed.
     * @return success message
     */
    @PostMapping("/organisation/change")
    public ResponseEntity<String> changeOrganisation(@RequestBody Organisation organisation){
        //TODO passwort vom Sysadmin abfragen?
        return ResponseEntity.ok(organisationService.changeOrganisation(organisation));
    }

    /**
     * Endpoint to delete an organisation.
     * @param organisationId ID of the organisation which will be deleted.
     * @return success message
     */
    @PostMapping("/organisation/delete/{organisationId}")
    public ResponseEntity<String> deleteOrganisation(@PathVariable long organisationId) {
        //TODO passwort vom Sysadmin abfragen?
        return ResponseEntity.ok(organisationService.deleteOrganisation(organisationId));
    }

    /**
     * Endpoint to delete a user.
     * @param userId ID of the user which will be deleted.
     * @param sysAdminPassword Password of the System-Admin to authorize him.
     * @return success message
     */
    @PostMapping("/user/delete")
    public ResponseEntity<String> deleteUser(@RequestParam long userId,
                                             @RequestParam String sysAdminPassword) {
        //TODO passwort vom Sysadmin abfragen?
        return ResponseEntity.ok(userService.deleteUser(userId));
    }


    /**
     * Endpoint to add a user to the database directly.
     * @param user User who will be saved.
     * @param sysAdminPassword Password of the System-Admin to authorize him.
     * @return success message
     */
    @PostMapping("/user/add")
    public ResponseEntity<String> addUser(@RequestBody User user,
                        @RequestParam String sysAdminPassword) {
        return ResponseEntity.ok(userService.saveUser(user));
    }
}
