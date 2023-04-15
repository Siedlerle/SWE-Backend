package com.eventmaster.backend.entities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class EventSeries {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

}
