package com.eventmaster.backend.RoleManagement.UserOrgaRole;

import com.eventmaster.backend.OrganizationManagement.Organization;
import com.eventmaster.backend.RoleManagement.Role.Role;
import com.eventmaster.backend.UserManagement.User;
import jakarta.persistence.*;

@Entity
public class OrgaUserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "userId",referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "organizationId",referencedColumnName = "id")
    private Organization organization;

    @ManyToOne
    @JoinColumn(name = "roleId",referencedColumnName = "id")
    private Role role;
    //---------------------------------------------------------------------------
}
