package com.eventmaster.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.HashSet;
import java.util.Set;

/**
 * This class serves as an entity to save a role for an organisation in the database.
 *
 * @author Fabian Eilber
 */
@Entity
public class OrgaRole {
    @Id
    private long id;

    @Enumerated(EnumType.ORDINAL)
    private EnumOrgaRole role;

    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(mappedBy = "orgaRole",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<UserInOrgaWithRole> userInOrgaWithRoles  = new HashSet<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public EnumOrgaRole getRole() {
        return role;
    }

    public void setRole(EnumOrgaRole role) {
        this.role = role;
    }

    public void removeUserInOrgaWithRole(UserInOrgaWithRole userInOrgaWithRole) {
        this.userInOrgaWithRoles.remove(userInOrgaWithRole);
    }
}

