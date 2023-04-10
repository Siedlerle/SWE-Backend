package com.eventmaster.backend.SurveyManagement.Answer;


import com.eventmaster.backend.SurveyManagement.Question.Question;
import jakarta.persistence.*;

@Entity
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "questionId",referencedColumnName = "id")
    private Question question;
    //---------------------------------------------------------------------------
    String text;
    long amount;
}
