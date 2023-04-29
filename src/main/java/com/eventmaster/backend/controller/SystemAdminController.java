package com.eventmaster.backend.controller;

import com.eventmaster.backend.entities.Organisation;

import com.eventmaster.backend.entities.User;
import com.eventmaster.backend.services.*;
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
     * @param password Password of the System-Admin to authorize him.
     * @return success message
     */
    @PostMapping("/organisation/create/{password}")
    public ResponseEntity<String> createOrganisation(@RequestBody Organisation organisation,@PathVariable String password){
        //TODO passwort vom Sysadmin abfragen?
        return ResponseEntity.ok(organisationService.createOrganisation(organisation));
    }

    /**
     * Endpoint to change an already existing organisation.
     * @param organisation Organisation which will be changed.
     * @param password Password of the System-Admin to authorize him.
     * @return success message
     */
    @PostMapping("/organisation/change/{password}")
    public ResponseEntity<String> changeOrganisation(@RequestBody Organisation organisation,@PathVariable String password){
        //TODO passwort vom Sysadmin abfragen?
        return ResponseEntity.ok(organisationService.changeOrganisation(organisation));
    }

    /**
     * Endpoint to delete an organisation.
     * @param organisationId ID of the organisation which will be deleted.
     * @param password Password of the System-Admin to authorize him.
     * @return success message
     */
    @PostMapping("/organisation/delete/{organisationId}/{password}")
    public ResponseEntity<String> deleteOrganisation(@PathVariable long organisationId,@PathVariable String password) {
        //TODO passwort vom Sysadmin abfragen?
        return ResponseEntity.ok(organisationService.deleteOrganisation(organisationId));
    }

    /**
     * Endpoint to delete a user.
     * @param emailAdress Email of the user which will be deleted.
     * @param password Password of the System-Admin to authorize him.
     * @return success message
     */
    @PostMapping("/user/delete/{password}")
    public ResponseEntity<String> deleteUser(@RequestParam String emailAdress,@PathVariable String password) {
        //TODO passwort vom Sysadmin abfragen?
        return ResponseEntity.ok(userService.deleteUser(emailAdress));
    }


    /**
     * Endpoint to add a user to the database directly.
     * @param user User who will be saved.
     * @param password Password of the System-Admin to authorize him.
     * @return success message
     */
    @PostMapping("/user/add/{password}")
    public ResponseEntity<String> addUser(@RequestBody User user,@PathVariable String password) {
        return ResponseEntity.ok(userService.saveUser(user));
    }
}
