package com.eventmaster.backend.entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "eventId",referencedColumnName = "id")
    private Event event;

    @OneToMany(mappedBy = "question",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<Answer> answers = new HashSet<>();

    @OneToMany(mappedBy = "question",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<QuestionAnsweredByUser> questionUsers = new HashSet<>();
    //---------------------------------------------------------------------------
    String text;
    String type;//@TODO auch hier wieder, kann man die Types auch nicht als String speichern (sondern als ENUM)?
}
