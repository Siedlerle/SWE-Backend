package com.eventmaster.backend.entities;

import jakarta.persistence.*;

/**
 * This class serves as an entity to save an already answered question by a user in the database.
 *
 * @author Lars Holweger
 */
@Entity
public class QuestionAnsweredByUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "questionId",referencedColumnName = "id")
    private Question question;

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "userId",referencedColumnName = "id")
    private User user;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
