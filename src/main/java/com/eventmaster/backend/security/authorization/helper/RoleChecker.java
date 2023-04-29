package com.eventmaster.backend.security.authorization.helper;

import com.eventmaster.backend.security.authorization.authority.CustomAuthority;

import java.util.Collection;

public abstract class RoleChecker {
    public static boolean isUserTutorForEvent(long eventId, long orgaId, Collection<CustomAuthority> authorities){
        return RoleInOrgaFinder.isUserTutor(orgaId, authorities)
                || RoleInEventFinder.isUserTutorInEvent(eventId, authorities);
    }
}
