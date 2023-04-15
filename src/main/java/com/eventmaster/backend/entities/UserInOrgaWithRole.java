package com.eventmaster.backend.entities;

import jakarta.persistence.*;

@Entity
public class UserInOrgaWithRole {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "userId",referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "organizationId",referencedColumnName = "id")
    private Organisation organization;

    @ManyToOne
    @JoinColumn(name = "roleId",referencedColumnName = "id")
    private OrgaRole role;
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

    public Organisation getOrganization() {
        return organization;
    }
    public void setOrganization(Organisation organization) {
        this.organization = organization;
    }

    public OrgaRole getRole() {
        return role;
    }
    public void setRole(OrgaRole role) {
        this.role = role;
    }
}