package com.eventmaster.backend.OrganizationManagement;

import com.eventmaster.backend.EventManagement.Event;
import com.eventmaster.backend.RoleManagement.OrgaUserRole.OrgaUserRole;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToMany(mappedBy = "organization",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<Event> events = new HashSet<>();

    @OneToMany(mappedBy = "organization",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<OrgaUserRole> orgaUserRoles = new HashSet<>();

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
}
