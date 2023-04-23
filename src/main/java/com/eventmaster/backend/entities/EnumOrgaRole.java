package com.eventmaster.backend.entities;

public enum EnumOrgaRole {

    REQUESTED("requested"), INVITED("invited"), USER("user"), ORGANIZER("organizer"), ADMIN("admin");

    public final String role;

    EnumOrgaRole(String role) {
        this.role = role;
    }

}
