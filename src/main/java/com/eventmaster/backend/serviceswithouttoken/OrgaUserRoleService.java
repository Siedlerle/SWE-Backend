package com.eventmaster.backend.serviceswithouttoken;

import com.eventmaster.backend.repositories.OrgaRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrgaUserRoleService {
    private final OrgaRoleRepository repository;

    @Autowired
    private UserService userService;
    @Autowired
    private OrganisationService organizationService;
    @Autowired
    private RoleService roleService;

    public OrgaUserRoleService(OrgaRoleRepository orgaUserRoleRepository) {
        this.repository = orgaUserRoleRepository;
    }

}