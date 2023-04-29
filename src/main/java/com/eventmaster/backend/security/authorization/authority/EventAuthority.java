package com.eventmaster.backend.security.authorization.authority;

import com.eventmaster.backend.entities.EnumEventRole;


public class EventAuthority implements CustomAuthority {
    Long eventId;
    EnumEventRole role;

    public EventAuthority(EnumEventRole role,Long eventId){
        this.role = role;
        this.eventId = eventId;
    }
    @Override
    public String getAuthority() {
        return eventId + "_" +role.role;
    }

    public EnumEventRole getRole() {
        return role;
    }

    public Long getEventId() {
        return eventId;
    }
}
