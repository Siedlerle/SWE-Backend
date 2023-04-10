package com.eventmaster.backend.RoleManagement.Role;


import com.eventmaster.backend.RoleManagement.UserEventRole.EventUserRole;
import com.eventmaster.backend.RoleManagement.UserOrgaRole.OrgaUserRole;
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
