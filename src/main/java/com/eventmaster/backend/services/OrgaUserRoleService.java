package com.eventmaster.backend.services;

import com.eventmaster.backend.repositories.OrgaUserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrgaUserRoleService {
    private final OrgaUserRoleRepository repository;

    @Autowired
    private UserService userService;
    @Autowired
    private OrganisationService organizationService;
    @Autowired
    private RoleService roleService;

    public OrgaUserRoleService(OrgaUserRoleRepository orgaUserRoleRepository) {
        this.repository = orgaUserRoleRepository;
    }

}