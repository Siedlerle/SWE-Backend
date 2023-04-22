package com.eventmaster.backend.entities;

public enum EnumEventRole {
    INVITED("invited"), GROUPINVITED("groupinvited"), SERIESINVITED("seriesinvited"), ATTENDEE("attendee"), GROUPATTENDEE("groupattendee"), TUTOR("tutor"), ORGANIZER("organizer");

    public final String role;

    EnumEventRole(String role) {
        this.role = role;
    }
}
