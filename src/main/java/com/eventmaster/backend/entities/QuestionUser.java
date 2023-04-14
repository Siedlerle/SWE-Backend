package com.eventmaster.backend.entities;

import jakarta.persistence.*;

@Entity
public class QuestionUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "questionId",referencedColumnName = "id")
    private Question question;

    @ManyToOne
    @JoinColumn(name = "userId",referencedColumnName = "id")
    private User user;
    //---------------------------------------------------------------------------
}
