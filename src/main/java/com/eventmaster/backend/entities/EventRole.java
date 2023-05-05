package com.eventmaster.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

/**
 * This class serves as an entity to save a role for an event in the database.
 *
 * @author Fabian Eilber
 */

@Entity
public class EventRole {


    @Id
    private Long id;

    @Enumerated(EnumType.ORDINAL)
    private EnumEventRole role;

    @JsonIgnore
    @OneToMany(mappedBy = "eventRole",cascade = CascadeType.PERSIST,orphanRemoval = true)
    private Set<UserInEventWithRole> userInEventWithRoles  = new HashSet<>();

    //---------------------------------------------------------------------------

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public EnumEventRole getRole() {
        return role;
    }

    public void setRole(EnumEventRole role) {
        this.role = role;
    }
}
