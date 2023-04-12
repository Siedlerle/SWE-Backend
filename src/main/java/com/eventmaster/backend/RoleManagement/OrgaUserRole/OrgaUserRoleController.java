package com.eventmaster.backend.RoleManagement.OrgaUserRole;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/orgauserrole")
public class OrgaUserRoleController {
    private final OrgaUserRoleService service;

    public OrgaUserRoleController(OrgaUserRoleService service) {
        this.service = service;
    }

    @PostMapping("/add/admin/{orgaId}")
    public ResponseEntity<Boolean> addAdminToOrganization(@PathVariable long orgaId,
                                                          @RequestBody String userMail) {
        return ResponseEntity.ok(service.addAdminToOrga(orgaId, userMail));
    }
}
