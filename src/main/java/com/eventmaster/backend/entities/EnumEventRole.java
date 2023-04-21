package com.eventmaster.backend.entities;

public enum EnumEventRole {
    INVITED("invited"), ATTENDEE("attendee"), TUTOR("tutor"), ORGANIZER("organizer");

    public final String role;

    EnumEventRole(String role) {
        this.role = role;
    }
}
