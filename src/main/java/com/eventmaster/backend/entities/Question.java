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
    private String questionText;
    @Transient
    private String[] answerString;

    @Enumerated(EnumType.ORDINAL)
    EnumQuestionType questionType;
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
        return questionText;
    }

    public void setText(String questionText) {
        this.questionText = questionText;
    }

    public EnumQuestionType getType() {
        return questionType;
    }

    public String getTypeVal(){
        return questionType.toString();
    }

    public void setType(EnumQuestionType questionType) {
        this.questionType = questionType;
    }

    public String[] getAnswerString() {
        return answerString;
    }

    public void setAnswerString(String[] answerString) {
        this.answerString = answerString;
    }
}
