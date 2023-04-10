package com.eventmaster.backend.InformationDistribution.Chat;

import com.eventmaster.backend.EventManagement.Event;
import jakarta.persistence.*;

@Entity
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "eventId",referencedColumnName = "id")
    private Event event;
    //---------------------------------------------------------------------------
    String message;
}
