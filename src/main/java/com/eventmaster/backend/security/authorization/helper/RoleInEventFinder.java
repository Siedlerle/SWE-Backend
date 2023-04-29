package com.eventmaster.backend.security.authorization.helper;

import com.eventmaster.backend.security.authorization.authority.CustomAuthority;
import com.eventmaster.backend.security.authorization.authority.EventAuthority;

import java.util.Collection;

public abstract class RoleInEventFinder {
    public static boolean isUserTutorInEvent(long eventId, Collection<CustomAuthority> authorities){
        return authorities
                .stream()
                .filter(EventAuthority.class::isInstance)
                .map(EventAuthority.class::cast)
                .filter(auth-> auth.getEventId() == eventId)
                .anyMatch(auth -> auth.getRole().ordinal()>=8);
    }
}
