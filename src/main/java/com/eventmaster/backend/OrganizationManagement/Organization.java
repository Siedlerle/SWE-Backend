package com.eventmaster.backend.OrganizationManagement;

import com.eventmaster.backend.EventManagement.Event;
import com.eventmaster.backend.RoleManagement.UserOrgaRole.OrgaUserRole;
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
    //---------------------------------------------------------------------------
    String name;
    //@TODO Auch hier wieder das lcoation/Adress Problem
    String location;
}
