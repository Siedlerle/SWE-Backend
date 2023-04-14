package com.eventmaster.backend.RoleManagement.OrgaUserRole;

import com.eventmaster.backend.OrganizationManagement.Organization;
import com.eventmaster.backend.OrganizationManagement.OrganizationService;
import com.eventmaster.backend.RoleManagement.Role.Role;
import com.eventmaster.backend.RoleManagement.Role.RoleService;
import com.eventmaster.backend.UserManagement.User;
import com.eventmaster.backend.UserManagement.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrgaUserRoleService {
    private final OrgaUserRoleRepository repository;

    @Autowired
    private UserService userService;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private RoleService roleService;

    public OrgaUserRoleService(OrgaUserRoleRepository orgaUserRoleRepository) {
        this.repository = orgaUserRoleRepository;
    }



    public boolean addAdminToOrga(long orgaId, String userMail) {
        try {
            User user = userService.getUserByMail(userMail);
            Organization organization = organizationService.getOrganizationById(orgaId);
            Role role = null;
            //TODO: role = roleService.getByName("Administrator");

            OrgaUserRole orgaUserRole = new OrgaUserRole();
            orgaUserRole.setUser(user);
            orgaUserRole.setOrganization(organization);
            orgaUserRole.setRole(role);
            repository.save(orgaUserRole);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}