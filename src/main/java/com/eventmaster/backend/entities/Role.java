package com.eventmaster.backend.entities;


import com.eventmaster.backend.entities.EventUserRole;
import com.eventmaster.backend.entities.OrgaUserRole;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Role {
    @Id
    @GeneratedValue
    private long id;

    @OneToMany(mappedBy = "role",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<EventUserRole> eventUserRoles = new HashSet<>();

    @OneToMany(mappedBy = "role",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<OrgaUserRole> orgaUserRoles = new HashSet<>();
    //---------------------------------------------------------------------------
    String name;//@TODO in Type umbennen?, kann man enums in MySql als statische Tabellen nutzen?
}
