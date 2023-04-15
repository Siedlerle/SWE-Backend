package com.eventmaster.backend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Preset {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
}
