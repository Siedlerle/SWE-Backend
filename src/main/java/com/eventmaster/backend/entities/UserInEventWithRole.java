package com.eventmaster.backend.entities;

import jakarta.persistence.*;

@Entity
public class UserInEventWithRole {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "userId",referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "eventId",referencedColumnName = "id")
    private Event event;

    //---------------------------------------------------------------------------
}
