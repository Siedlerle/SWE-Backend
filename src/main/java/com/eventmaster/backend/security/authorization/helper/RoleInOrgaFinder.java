package com.eventmaster.backend.security.authorization.helper;

import com.eventmaster.backend.entities.EnumOrgaRole;
import com.eventmaster.backend.security.authorization.authority.CustomAuthority;
import com.eventmaster.backend.security.authorization.authority.OrganisationAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

public abstract class RoleInOrgaFinder {
    public static boolean isUserAdminInOrga(long orgaId, Collection<? extends GrantedAuthority> authorities) {
        String role = "ROLE_" + orgaId + "_" + EnumOrgaRole.ADMIN.role;
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(role);
        return authorities.stream().anyMatch(a -> a.equals(simpleGrantedAuthority));
    }

    public static boolean isUserTutor(long orgaId, Collection<CustomAuthority> authorities) {
        return authorities
                .stream()
                .filter(OrganisationAuthority.class::isInstance)
                .map(OrganisationAuthority.class::cast)
                .filter(auth -> auth.getOrganisationId() == orgaId)
                .anyMatch(auth -> auth.getRole().ordinal() >= 3);
    }
}
