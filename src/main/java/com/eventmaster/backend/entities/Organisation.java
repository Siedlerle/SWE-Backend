package com.eventmaster.backend.entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Organisation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToMany(mappedBy = "organisation",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<Event> events = new HashSet<>();

    @OneToMany(mappedBy = "organisation",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<UserInOrgaWithRole> orgaUserRoles = new HashSet<>();

    private String name;

    private String location; //@TODO Auch hier wieder das location/Address Problem

    //---------------------------------------------------------------------------

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}