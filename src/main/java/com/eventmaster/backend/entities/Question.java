package com.eventmaster.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

/**
 * This class serves as an entity to save a question for a survey in the database.
 *
 * @author Lars Holweger
 * @author Fabian Eilber
 * @author Fabian Unger
 */
@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "eventId",referencedColumnName = "id")
    private Event event;

    @JsonIgnore
    @OneToMany(mappedBy = "question",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<Answer> answers = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "question",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<QuestionAnsweredByUser> questionUsers = new HashSet<>();

    //---------------------------------------------------------------------------
    private String text;
    private QuestionType type;
    private enum QuestionType {
        multipleChoice,
        text
    }
    //---------------------------------------------------------------------------

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Set<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<Answer> answers) {
        this.answers = answers;
    }

    public Set<QuestionAnsweredByUser> getQuestionUsers() {
        return questionUsers;
    }

    public void setQuestionUsers(Set<QuestionAnsweredByUser> questionUsers) {
        this.questionUsers = questionUsers;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public QuestionType getType() {
        return type;
    }

    public String getTypeVal(){
        return type.toString();
    }

    public void setType(QuestionType type) {
        this.type = type;
    }
}
