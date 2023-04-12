package com.eventmaster.backend.OrganizationManagement;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/organization")
public class OrganizationController {
    private final OrganizationService organizationService;

    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }


    @PostMapping("get-single/{organizationId}")
    public ResponseEntity<Organization> getOrganization(@PathVariable Long organizationId) {
        return ResponseEntity.ok(organizationService.getOrganizationById(organizationId));
    }

}
