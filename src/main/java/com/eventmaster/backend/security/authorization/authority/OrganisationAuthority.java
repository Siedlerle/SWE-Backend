package com.eventmaster.backend.security.authorization.authority;

import com.eventmaster.backend.entities.EnumOrgaRole;

public class OrganisationAuthority implements CustomAuthority {
    Long organisationId;
    EnumOrgaRole role;

    public OrganisationAuthority(EnumOrgaRole role, Long organisationId) {
        this.role = role;
        this.organisationId = organisationId;
    }

    @Override
    public String getAuthority() {
        return organisationId + "_" + role.role;
    }

    public EnumOrgaRole getRole() {
        return role;
    }

    public Long getOrganisationId() {
        return organisationId;
    }
}
