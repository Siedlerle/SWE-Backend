package com.eventmaster.backend.RoleManagement.OrgaUserRole;

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

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public Organization getOrganization() {
        return organization;
    }
    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }
}
