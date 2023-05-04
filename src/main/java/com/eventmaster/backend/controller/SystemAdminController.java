package com.eventmaster.backend.controller;

import com.eventmaster.backend.entities.Organisation;

import com.eventmaster.backend.entities.User;
import com.eventmaster.backend.services.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    private final UserInOrgaWithRoleService userInOrgaWithRoleService;

    public SystemAdminController(OrganisationService organisationService,
                                 UserService userService,
                                 UserInOrgaWithRoleService userInOrgaWithRoleService) {
        this.organisationService = organisationService;
        this.userService = userService;
        this.userInOrgaWithRoleService = userInOrgaWithRoleService;
    }


    /**
     * Endpoint to create an organisation.
     *
     * @param organisation Organisation which will be saved.
     * @param password     Password of the System-Admin to authorize him.
     * @return success message
     */
    @PostMapping("/organisation/create/{password}")
    public ResponseEntity<String> createOrganisation(@RequestBody Organisation organisation,
                                                     @PathVariable String password) {
        return ResponseEntity.ok(organisationService.createOrganisation(organisation));
    }

    /**
     * Endpoint to change an already existing organisation.
     *
     * @param organisation Organisation which will be changed.
     * @param password     Password of the System-Admin to authorize him.
     * @return success message
     */
    @PostMapping("/organisation/change/{password}")
    public ResponseEntity<String> changeOrganisation(@RequestBody Organisation organisation,
                                                     @PathVariable String password) {
        return ResponseEntity.ok(organisationService.changeOrganisation(organisation));
    }

    /**
     * Endpoint to delete an organisation.
     *
     * @param organisationId ID of the organisation which will be deleted.
     * @param password       Password of the System-Admin to authorize him.
     * @return success message
     */
    @PostMapping("/organisation/delete/{organisationId}/{password}")
    public ResponseEntity<String> deleteOrganisation(@PathVariable long organisationId,
                                                     @PathVariable String password) {
        return ResponseEntity.ok(organisationService.deleteOrganisation(organisationId));
    }

    /**
     * Endpoint to delete a user.
     *
     * @param emailAdress Email of the user which will be deleted.
     * @param password    Password of the System-Admin to authorize him.
     * @return success message
     */
    @PostMapping("/user/delete/{emailAdress}/{password}")
    public ResponseEntity<String> deleteUser(@PathVariable String emailAdress,
                                             @PathVariable String password) {
        return ResponseEntity.ok(userService.deleteUser(emailAdress));
    }


    /**
     * Endpoint to add a user to the database directly.
     *
     * @param user     User who will be saved and automatically authorized.
     * @param password Password of the System-Admin to authorize him.
     * @return success message
     */
    @PostMapping("/user/add/{password}")
    public ResponseEntity<String> addUser(@RequestBody User user,
                                          @PathVariable String password) {
        return ResponseEntity.ok(userService.saveUser(user));
    }
    /**
     * Endpoint to add an admin to an organisation.
     *
     * @param emailAdress     User who will be admin.
     * @param organisationId organisation where user will be admin.
     * @param password Password of the System-Admin to authorize him.
     * @return success message
     */
    @PostMapping("/admin/add/{organisationId}/{userId}/{password}")
    public ResponseEntity<String> addAdminToOrganisation(@PathVariable long organisationId,
                                                         @PathVariable String emailAdress,
                                                         @PathVariable String password) {
        return ResponseEntity.ok(userInOrgaWithRoleService.setAdminForOrga(organisationId,emailAdress));
    }
    @PostMapping("/orga/get-all/{password}")
    public ResponseEntity<List<Organisation>> getAllOrgas(@PathVariable String password){
        return ResponseEntity.ok(organisationService.getAllOrganisationsForAdmin());
    }
}
