package com.eventmaster.backend.entities;


import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class User {
    @Id
    @GeneratedValue
    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;

    @OneToMany(mappedBy = "user")
    private Set<UserInEventWithRole> userInEventWithRoles= new HashSet<>();
}
