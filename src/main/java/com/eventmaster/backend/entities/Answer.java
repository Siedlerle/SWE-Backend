package com.eventmaster.backend.entities;


import jakarta.persistence.*;

/**
 * This class serves as an entity to save a multiple-choice-answer in the database.
 *
 * @author Fabian Eilber
 * @authoer Lars Holweger
 */
@Entity
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(cascade=CascadeType.PERSIST)
    @JoinColumn(name = "questionId",referencedColumnName = "id")
    private Question question;

    //---------------------------------------------------------------------------
    private String text;
    private long amount;

    //---------------------------------------------------------------------------


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}
