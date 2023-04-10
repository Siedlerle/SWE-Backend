package com.eventmaster.backend.InformationDistribution.Document;

import com.eventmaster.backend.EventManagement.Event;
import jakarta.persistence.*;

@Entity
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name ="eventId",referencedColumnName = "id")
    private Event event;
    //---------------------------------------------------------------------------
    String name;
    String downloadUri;
    long size;
}
