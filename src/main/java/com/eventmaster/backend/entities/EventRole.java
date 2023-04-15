package com.eventmaster.backend.entities;

import com.eventmaster.backend.serviceswithouttoken.UserInEventWithRoleService;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class EventRole {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String Role;


    @OneToMany(mappedBy = "eventRole",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<UserInEventWithRole> userInEventWithRoles  = new HashSet<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }
}
