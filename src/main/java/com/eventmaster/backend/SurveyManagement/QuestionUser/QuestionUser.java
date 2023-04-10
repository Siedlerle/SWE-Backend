package com.eventmaster.backend.SurveyManagement.QuestionUser;

import com.eventmaster.backend.SurveyManagement.Question.Question;
import com.eventmaster.backend.UserManagement.User;
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
